package ec.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ec.parser.JsonFactory;
import ec.system.Basis;
import ec.system.DeveloperMode;

public abstract class SQLCenter extends Basis{

	protected Connection conn = null;
	
	private Map<Object,PreparedStatement> SQLExecuteBatchers = null;
	protected String dbName = null;
	protected boolean outSQLText = false;
	public SQLCenter(){}
	
	
	public SQLCenter(String dbName){
		this.dbName = dbName;
	}
	
	
	
	protected void emptyTable(String tableName) throws SQLException{
		String SQL = "delete from " + tableName;
		conn().createStatement().executeUpdate(SQL);
	}
	
	public PreparedStatement gereateInsertPreparedStatement(String tableName,String[] insertColNameAr) throws SQLException{
		String cols = "";
		String para = "";
		for(int i = 0 ;i < insertColNameAr.length; i++){
			cols += insertColNameAr[i] + ",";
			para += "?,";
			if(i == insertColNameAr.length - 1) {
				cols = trimEndChar(cols, ",");
				para = trimEndChar(para, ",");
			}
		}
		String SQL = "insert into " + tableName + " ("+cols+") values ("+para+")";
		return conn().prepareStatement(SQL);
	}
	
	public PreparedStatement generateSQLBatcher(Object batcherName,String tableName,String[] insertColNameAr) throws SQLException{
		if(SQLExecuteBatchers == null) SQLExecuteBatchers = new HashMap<>();
		if(SQLExecuteBatchers.get(batcherName) == null) {
			PreparedStatement pstmt = gereateInsertPreparedStatement(tableName,insertColNameAr);
			SQLExecuteBatchers.put(batcherName, pstmt);
			return pstmt;
		} else return SQLExecuteBatchers.get(batcherName);
	}
	
	public PreparedStatement generateSQLBatcher(Object batcherName,String SQL) throws SQLException{
		if(SQLExecuteBatchers == null) SQLExecuteBatchers = new HashMap<>();
		if(SQLExecuteBatchers.get(batcherName) == null) {
			PreparedStatement pstmt = conn().prepareStatement(SQL);
			SQLExecuteBatchers.put(batcherName, pstmt);
			return pstmt;
		} else return SQLExecuteBatchers.get(batcherName);
	}
	
	protected boolean isBatchExecuterGenerated(Object batcherName){
		return SQLExecuteBatchers!=null && SQLExecuteBatchers.get(batcherName) != null;
	}
	
	protected PreparedStatement getBatcherExecuter(Object batcherName){
		if(isBatchExecuterGenerated(batcherName)) return SQLExecuteBatchers.get(batcherName);
		else return null;
	}
	
	protected void executeBatchExecuter(Object batcherName) throws SQLException{
		if(isBatchExecuterGenerated(batcherName)) {
			SQLExecuteBatchers.get(batcherName).executeBatch();
			SQLExecuteBatchers.get(batcherName).close();
			SQLExecuteBatchers.remove(batcherName);
		}
	}
	
	public void executeAllBatchExecuter() throws SQLException{
		if(SQLExecuteBatchers!=null){
			Iterator<Object> iter = SQLExecuteBatchers.keySet().iterator();
			while(iter.hasNext()){
				Object key = iter.next();
				SQLExecuteBatchers.get(key).executeBatch();
				SQLExecuteBatchers.get(key).close();
				iter.remove();
			}
		}
	}
	
	public void resetAllBatcher() throws SQLException{
		if(SQLExecuteBatchers != null) {
			Iterator<Object> iter = SQLExecuteBatchers.keySet().iterator();
			while(iter.hasNext()){
				Object key = iter.next();
				SQLExecuteBatchers.get(key).close();
				iter.remove();
			}
			SQLExecuteBatchers = null;
		}
	}
	
	protected void closeDBComm(Statement stmt,ResultSet rs){
		try{
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
		} catch(Exception e){
			this.exportExceptionText(e);
			this.except("Close DB Commponent Fail,Error = " + e.getMessage(), this.getClass().getName());
		}
	}
	
	
	protected PreparedStatement generateReturnKeyInsertPstmt(String SQL) throws SQLException{
		if(conn() != null) return conn().prepareStatement(SQL,PreparedStatement.RETURN_GENERATED_KEYS);
		else return null;
	}
	
	
	protected int getInsertKeyValueInReturnKeyPStmt(PreparedStatement pstmt) throws SQLException{
		ResultSet rs = pstmt.getGeneratedKeys();
		if(rs.next()) return rs.getInt(1);
		else return -1;
	}
	
	public Connection conn(){
		if(DBConnection.getConnection(dbName) != null)  {
			Connection conn = DBConnection.getConnection(dbName);
			try {
				if(conn.isClosed()) {
					connectionNotBuild();
				}
			} catch (SQLException e) {
				this.exportExceptionText(e);
			}
			return DBConnection.getConnection(dbName);
		}
		else {
			connectionNotBuild();
			return null;
		}
	}
	
	public void closeConnection() throws SQLException {
		if(dbName != null) DBConnection.closeConnection(dbName);
	}
	
	public  boolean executeJsonInsert(String tableName,String[] colsAr,JsonFactory jsonData){
		try {
			PreparedStatement ptmt =  gereateInsertPreparedStatement(tableName,colsAr);
			for(int i = 1;i <= colsAr.length;i++){
				String value = jsonData.getObjectValue(colsAr[i - 1]);
				ptmt.setString(i, value != null ? value : "");
			}
			ptmt.executeUpdate();
			return true;
		} catch(Exception e){
			exportExceptionText(e);
		}
		return false;
	}
	
	
	
	
	
	public void removeView(String viewName) throws SQLException {
		String SQL = "DROP VIEW IF EXISTS " + viewName;
		log("View Operaton, SQL = " + SQL);
		Statement stmt = this.conn().createStatement();
		stmt.executeUpdate(SQL);
		
	}
	
	
	public void createView(String viewName,String viewSQL) throws SQLException{
		String SQL = "create view " + viewName + " as ";
		SQL += viewSQL;
		log("View Operaton, SQL = " + SQL);
		Statement stmt = this.conn().createStatement();
		stmt.executeUpdate(SQL);
	}
	
	
	public boolean insertWithMap(String tableName,Map<Object,Object> data){
		try {
			Object[] keysAr = new Object[data.size()];
			String[] colsAr = new String[data.size()];
			Iterator<Object> iter = data.keySet().iterator();
			int cursor = 0;
			while(iter.hasNext()){
				Object key = iter.next();
				colsAr[cursor] = key.toString();
				keysAr[cursor] = key;
				cursor++;
			}
			PreparedStatement ptmt =  gereateInsertPreparedStatement(tableName,colsAr);
			for(int i = 1;i <= colsAr.length;i++){
				Object value = data.get(keysAr[i - 1]);
				if(value instanceof Integer) 
					ptmt.setInt(i, value != null ? parseInt(value.toString(), -1) : null);
				else if(value instanceof Date) 
					ptmt.setTimestamp(i, value != null ? new Timestamp(((Date) value).getTime())  : null);
				else 
					ptmt.setString(i, value != null ? value.toString() : null);
			}
			ptmt.executeUpdate();
			this.closeDBComm(ptmt, null);
			return true;
		} catch (Exception e){
			exportExceptionText(e);
			return false;
		}
	}
	public List<Map<Object,String>> queryRecords(String tablename) throws SQLException{
		return queryRecords(tablename,new SQLOrder());
	}
	public List<Map<Object,String>> queryRecords(String tablename, SQLOrder order) throws SQLException{
		String SQL = "select * from " + tablename ;
		if(order != null) SQL += " " + order.toSQL();
		Statement stmt = this.conn().createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		List<Map<Object,String>> results = convertQueryResultToList(rs);
		this.closeDBComm(stmt, rs);
		return results;
	}
	
	public List<Map<Object,String>> queryRecords(String tablename, SQLOrder order, SQLLimit limit) throws SQLException{
		String SQL = "select * from " + tablename ;
		if(order != null) SQL += " " + order.toSQL();
		if(limit != null) SQL += " " + limit.toSQL();
		Statement stmt = this.conn().createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		List<Map<Object,String>> results = convertQueryResultToList(rs);
		this.closeDBComm(stmt, rs);
		return results;
	}
	
	
	public List<Map<Object,String>> queryRecords(String tablename,Map<Object,Object> data) throws SQLException{
		return queryRecords(tablename,data,null);
	}
	
	//order --> order by COL1,COL2 desc
	public List<Map<Object,String>> queryRecords(String tablename,Map<Object,Object> data, SQLOrder order) throws SQLException{
		String SQL = "select * from " + tablename + " where ";
		SQLCriterion whereCon = new SQLCriterion(data);
		
		SQL += whereCon.toWhereClause();
		if(order != null) SQL += order.toSQL();
		if(DeveloperMode.isON() || outSQLText) log("Query Record, SQL = " + SQL);
		PreparedStatement pstmt = this.conn().prepareStatement(SQL);
		whereCon.fillPrepareStmtParas(pstmt);
		
		ResultSet rs = pstmt.executeQuery();
		List<Map<Object,String>> results = convertQueryResultToList(rs);
		this.closeDBComm(pstmt, rs);
		return results;
	}
	
	public List<Map<Object,String>> queryRecords(String tablename,Map<Object,Object> data, SQLOrder order, SQLLimit limit) throws SQLException{
		String SQL = "select * from " + tablename + " where ";
		SQLCriterion whereCon = new SQLCriterion(data);
		
		SQL += whereCon.toWhereClause();
		if(order != null) SQL += " " + order.toSQL();
		if(limit != null) SQL += " " + limit.toSQL();
		if(DeveloperMode.isON() || outSQLText) log("Query Record, SQL = " + SQL);
		PreparedStatement pstmt = this.conn().prepareStatement(SQL);
		whereCon.fillPrepareStmtParas(pstmt);
		
		ResultSet rs = pstmt.executeQuery();
		List<Map<Object,String>> results = convertQueryResultToList(rs);
		this.closeDBComm(pstmt, rs);
		return results;
	}
	
	
	
	public boolean isRecordExist(String tableName,Map<Object,Object> data) throws SQLException{
		String SQL = "select * from " + tableName + " where ";
		SQLCriterion whereCon = new SQLCriterion(data);
		SQL += whereCon.toWhereClause();
		if(DeveloperMode.isON() || outSQLText) log("Check Record Exist, SQL = " + SQL);
		PreparedStatement pstmt = this.conn().prepareStatement(SQL);
		whereCon.fillPrepareStmtParas(pstmt);
		ResultSet rs = pstmt.executeQuery();
		boolean isExist = rs.next();
		this.closeDBComm(pstmt, rs);
		return isExist;
	}
	
	public int getCounts(String tableName) throws SQLException{
		String SQL = "select count(*) as rAmt from " + tableName;
		
		if(DeveloperMode.isON() || outSQLText) log("Get Counts , SQL = " + SQL);
		Statement stmt = this.conn().createStatement();
		
		ResultSet rs = stmt.executeQuery(SQL);
		int count = 0;
		if(rs.next()) {
			count = rs.getInt(1);
		}
		this.closeDBComm(stmt, rs);
		return count;
	}
	
	public int getCounts(String tableName,Map<Object,Object> data) throws SQLException{
		String SQL = "select count(*) as rAmt from " + tableName + " where ";
		
		SQLCriterion whereCon = new SQLCriterion(data);
		SQL += whereCon.toWhereClause();
		if(DeveloperMode.isON() || outSQLText) log("Get Counts , SQL = " + SQL);
		PreparedStatement pstmt = this.conn().prepareStatement(SQL);
		whereCon.fillPrepareStmtParas(pstmt);
		
		ResultSet rs = pstmt.executeQuery();
		int count = 0;
		if(rs.next()) {
			count = rs.getInt(1);
		}
		this.closeDBComm(pstmt, rs);
		return count;
	}
	
	
	public int getMinValue(String tableName,String colName) throws SQLException{
		String SQL = "select min(" + colName + ") as MinValue from " + tableName;
		
		if(DeveloperMode.isON()  || outSQLText) log("Get MinValue , SQL = " + SQL);
		Statement stmt = this.conn().createStatement();
		
		ResultSet rs = stmt.executeQuery(SQL);
		int value = -1;
		if(rs.next()) {
			value = rs.getInt(1);
		}
		this.closeDBComm(stmt, rs);
		return value;
	}
	
	public int getMinValue(String tableName,String colName,Map<Object,Object> data) throws SQLException{
		String SQL = "select min(" + colName + ") as MinValue from " + tableName + " where ";
		
		SQLCriterion whereCon = new SQLCriterion(data);
		SQL += whereCon.toWhereClause();
		if(DeveloperMode.isON() || outSQLText) log("Get MinValue , SQL = " + SQL);
		PreparedStatement pstmt = this.conn().prepareStatement(SQL);
		whereCon.fillPrepareStmtParas(pstmt);
		
		ResultSet rs = pstmt.executeQuery();
		int value = -1;
		if(rs.next()) {
			value = rs.getInt(1);
		}
		this.closeDBComm(pstmt, rs);
		return value;
	}
	
	
	public int getMaxValue(String tableName,String colName) throws SQLException{
		String SQL = "select max(" + colName + ") as MaxValue from " + tableName;
		
		if(DeveloperMode.isON() || outSQLText) log("Get MaxValue , SQL = " + SQL);
		Statement stmt = this.conn().createStatement();
		
		ResultSet rs = stmt.executeQuery(SQL);
		int value = -1;
		if(rs.next()) {
			value = rs.getInt(1);
		}
		this.closeDBComm(stmt, rs);
		return value;
	}
	
	public int getMaxValue(String tableName,String colName,Map<Object,Object> data) throws SQLException{
		String SQL = "select max(" + colName + ") as MaxValue from " + tableName + " where ";
		
		SQLCriterion whereCon = new SQLCriterion(data);
		SQL += whereCon.toWhereClause();
		if(DeveloperMode.isON() || outSQLText) log("Get MaxValue , SQL = " + SQL);
		PreparedStatement pstmt = this.conn().prepareStatement(SQL);
		whereCon.fillPrepareStmtParas(pstmt);
		
		ResultSet rs = pstmt.executeQuery();
		int value = -1;
		if(rs.next()) {
			value = rs.getInt(1);
		}
		this.closeDBComm(pstmt, rs);
		return value;
	}
	
	
	
	
	
	
	
	public List<Map<Object,String>>  getDinstinct(String tableName,String colName) throws SQLException{
		String SQL = "select distinct(" + colName + ") as " + colName + " from " + tableName;
		
		
		if(DeveloperMode.isON() || outSQLText) log("Get Distinct , SQL = " + SQL);
		Statement stmt = this.conn().createStatement();
		
		ResultSet rs = stmt.executeQuery(SQL);
		List<Map<Object,String>> results = convertQueryResultToList(rs);
		this.closeDBComm(stmt, rs);
		return results;
	}
	
	public List<Map<Object,String>>  getDinstinct(String tableName,String colName,Map<Object,Object> data) throws SQLException{
		String SQL = "select distinct(" + colName + ") as " + colName + " from " + tableName + " where ";
		
		SQLCriterion whereCon = new SQLCriterion(data);
		SQL += whereCon.toWhereClause();
		if(DeveloperMode.isON()  || outSQLText) log("Get Distinct , SQL = " + SQL);
		PreparedStatement pstmt = this.conn().prepareStatement(SQL);
		whereCon.fillPrepareStmtParas(pstmt);
		
		ResultSet rs = pstmt.executeQuery();
		List<Map<Object,String>> results = convertQueryResultToList(rs);
		this.closeDBComm(pstmt, rs);
		return results;
	}
	
	
	public void updateRecord(String tableName,Map<Object,Object> data,Object[] primaryKeys) throws SQLException{
		String SQL = "update " + tableName + " ";
		
		SQLCriterion whereCon = new SQLCriterion(data,primaryKeys);
		SQL += whereCon.toUpdateClause();
		
		SQL += " where ";
		
		SQL += whereCon.toWhereClause();
		if(DeveloperMode.isON() || outSQLText) log("Update Record , SQL = " + SQL);
		PreparedStatement pstmt = this.conn().prepareStatement(SQL);
		whereCon.fillPrepareStmtParas(pstmt);
		
		pstmt.executeUpdate();
		this.closeDBComm(pstmt, null);
	}
	
	
	public void deleteRecord(String tableName,Map<Object,Object> data) throws SQLException{
		String SQL = "delete from " + tableName + " where ";
		SQLCriterion whereCon = new SQLCriterion(data);
		
		SQL += whereCon.toWhereClause();
		if(DeveloperMode.isON() || outSQLText) log("Delete Record , SQL = " + SQL);
		PreparedStatement pstmt = this.conn().prepareStatement(SQL);
		whereCon.fillPrepareStmtParas(pstmt);
		
		pstmt.executeUpdate();
		this.closeDBComm(pstmt, null);
	}
	
	protected List<Map<Object,String>> convertQueryResultToList(ResultSet rs) throws SQLException{
		ResultSetMetaData rsmd = rs.getMetaData();
		String[] colNames = new String[rsmd.getColumnCount()];
		for(int i = 1;i <= rsmd.getColumnCount();i++){
			colNames[i - 1] = rsmd.getColumnLabel(i);
		}
		List<Map<Object,String>> results = new ArrayList<>();
		while(rs.next()){
			Map<Object,String> result = new HashMap<>();
			for(String colName : colNames){
				String v = rs.getString(colName);
				result.put(colName, v);
			}
			results.add(result);
		}
		return results;
	}
	
	
	public abstract void connectionNotBuild();
	
}
