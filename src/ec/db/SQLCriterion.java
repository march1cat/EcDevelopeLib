package ec.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import ec.system.Basis;

public class SQLCriterion extends Basis {

	private Object[] values = null;
	private Map<Object,Object> data = null;
	private Object[] primaryKeys = null;
	
	public SQLCriterion(Map<Object,Object> data){
		this.data = data;
		values = new Object[data.size()];
	}
	
	public SQLCriterion(Map<Object,Object> data,Object[] primaryKeys){
		this.data = data;
		values = new Object[data.size() - primaryKeys.length];
		this.primaryKeys = primaryKeys;
	}
	
	
	
	
	private String toQueryWhereClause(){
		String whereClause = "";
		int cnt = 0;
		Iterator<Object> iter = data.keySet().iterator();
		while (iter.hasNext()){
			Object key = iter.next();
			Object value = data.get(key);
			values[cnt++] = value;
			
			if(value instanceof SQLRange){
				SQLRange range = (SQLRange) value;
				whereClause += range.toSQLWhereText();
			} else {
				if(value != null && (isTextStartWith(value.toString(), "%") || isTextEndWith(value.toString(), "%"))){
					whereClause += key.toString() + " like ?";
				} else {
					whereClause += key.toString() + " = ?";
				}
			}
			
			if(cnt < values.length) whereClause += " and ";
		}
		return whereClause;
	}
	
	
	public String toUpdateClause(){
		Iterator<Object> iter = data.keySet().iterator();
		String setText = "set ";
		int valueAssignCnt = 0;
		while(iter.hasNext()){
			Object key = iter.next();
			boolean isPrimary = false;
			for(Object primaryKey : primaryKeys){
				if(key == primaryKey) {
					isPrimary =  true;
					break;
				}
			}
			if(!isPrimary) {
				setText += key.toString() + "=?,";
				values[valueAssignCnt++] = data.get(key);
			}
			
		}
		if(setText.length() > 0) setText = setText.substring(0, setText.length() - 1);
		return setText;
	}
	
	private String toUpdateWhereClause(){
		String whereClause = "";
		for(int i = 0;i < primaryKeys.length;i++) {
			whereClause += " " + primaryKeys[i].toString() + "=?";
			if(i != primaryKeys.length - 1) whereClause += " and ";
		}
		return whereClause;
	}
	public String toWhereClause(){
		if(primaryKeys == null) return toQueryWhereClause();
		else return toUpdateWhereClause();
	}
	
	
	private void fillQueryPStmtParas(PreparedStatement pstmt) throws SQLException {
		int paraSetIndex = 1;
		for(int i = 0;i < values.length;i++){
			Object v = values[i];
				
			if(v instanceof Integer)
				pstmt.setInt(paraSetIndex++, (Integer) v);
			else if(v instanceof SQLRange){
				SQLRange range = (SQLRange) v;
				if(range.getStartValue() != null) {
					if(range.getStartValue() instanceof Integer)
						pstmt.setInt(paraSetIndex++, (Integer)range.getStartValue());
					else
						pstmt.setString(paraSetIndex++, range.getStartValue().toString());
				}
				if(range.getEndValue() != null) {
					if(range.getEndValue() instanceof Integer)
						pstmt.setInt(paraSetIndex++, (Integer)range.getEndValue());
					else
						pstmt.setString(paraSetIndex++, range.getEndValue().toString());
				}
			} else 
				pstmt.setString(paraSetIndex++, v.toString());
		}
	}
	
	private void fillUpdatePStmtParas(PreparedStatement pstmt) throws SQLException {
		int paraSetCount = 1;
		for(Object v : values){
			if(v instanceof Integer)
				pstmt.setInt(paraSetCount++, Integer.parseInt(v.toString()));
			else if(v instanceof Date)
				pstmt.setTimestamp(paraSetCount++, v != null ? new Timestamp(((Date) v).getTime())  : null);
			else
				pstmt.setString(paraSetCount++, v != null ? v.toString() : null);
		}
		
		for(int i = 0;i < primaryKeys.length;i++) {
			Object v = data.get(primaryKeys[i]);
			if(v instanceof Integer)
				pstmt.setInt(paraSetCount++, Integer.parseInt(v.toString()));
			else if(v instanceof Date)
				pstmt.setTimestamp(paraSetCount++, v != null ? new Timestamp(((Date) v).getTime())  : null);
			else 
				pstmt.setString(paraSetCount++, v.toString());
		}
	}
	
	
	public void fillPrepareStmtParas(PreparedStatement pstmt) throws SQLException{
		if(primaryKeys == null) fillQueryPStmtParas(pstmt);
		else fillUpdatePStmtParas(pstmt);
	}
	
	
	
}
