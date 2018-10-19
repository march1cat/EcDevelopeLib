package ec.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ec.parser.JsonFactory;
import ec.system.Basis;

public abstract class SQLCenter extends Basis{

	protected Connection conn = null;
	
	private Map<Object,PreparedStatement> SQLExecuteBatchers = null;
	protected String dbName = null;
	
	public SQLCenter(){}
	
	
	public SQLCenter(String dbName){
		this.dbName = dbName;
	}
	
	
	
	protected void emptyTable(String tableName) throws SQLException{
		String SQL = "delete from " + tableName;
		conn().createStatement().executeUpdate(SQL);
	}
	
	protected PreparedStatement gereateInsertPreparedStatement(String tableName,String[] insertColNameAr) throws SQLException{
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
	
	protected PreparedStatement generateSQLBatcher(Object batcherName,String tableName,String[] insertColNameAr) throws SQLException{
		if(SQLExecuteBatchers == null) SQLExecuteBatchers = new HashMap<>();
		if(SQLExecuteBatchers.get(batcherName) == null) {
			PreparedStatement pstmt = gereateInsertPreparedStatement(tableName,insertColNameAr);
			SQLExecuteBatchers.put(batcherName, pstmt);
			return pstmt;
		} else return SQLExecuteBatchers.get(batcherName);
	}
	
	protected PreparedStatement generateSQLBatcher(Object batcherName,String SQL) throws SQLException{
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
	
	protected Connection conn(){
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
	
	
	public abstract void connectionNotBuild();
	
}
