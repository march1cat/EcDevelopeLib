package ec.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class PLSQL {
	
	private Connection conn = null;
	
	private String host;
	
	private boolean developerMode = false;
	
	public PLSQL(String host,int port,String SID){
		this.host = new String("jdbc:oracle:thin:@").concat(host).concat(":").concat(String.valueOf(port)).concat(":").concat(SID);
	}
	public PLSQL(String host,int port,String SID,boolean developerMode){
		this.host = new String("jdbc:oracle:thin:@").concat(host).concat(":").concat(String.valueOf(port)).concat(":").concat(SID);
		this.developerMode = developerMode;
	}
	public void connectDB(String drivername,String username,String password) throws SQLException, ClassNotFoundException{
		Class.forName(drivername);
		conn = DriverManager.getConnection(host,username,password);
	}
	public void connectDB(String username,String password) throws SQLException, ClassNotFoundException{
		//String driver = "oracle.jdbc.OracleDriver";
		String driver = "oracle.jdbc.driver.OracleDriver";
		this.connectDB(driver,username, password);
	}
	
	public ResultSet query(String sql) throws SQLException{
		if(developerMode) System.out.println(sql);
		Statement stmt = conn.createStatement();
		ResultSet set = stmt.executeQuery(sql);
		return set;
	}
	
	public ArrayList<HashMap<String,String>> queryToList(String sql,String[] colNameAr) throws SQLException{
		ResultSet set = query(sql);
		ArrayList<HashMap<String,String>> arlist = parseResultSetToArrayList(set,colNameAr);
		return arlist;
	}
	
	private ArrayList<HashMap<String,String>> parseResultSetToArrayList(ResultSet set,String[] colNameAr) throws SQLException{
		ArrayList<HashMap<String,String>> arlist = new ArrayList<HashMap<String,String>>();
		while(set.next()){
			HashMap<String,String> map = new HashMap<String, String>();
			for(String name : colNameAr){
				String value = (set.getString(name) != null) ? set.getString(name) : "";
				map.put(name, set.getString(name));
			}
			arlist.add(map);
		}
		return arlist;
	}
	
	public String getSequence(String sequenceName) throws SQLException{
		String SQL = "select " + sequenceName + ".nextval from dual";
		String sequence = "";
		ResultSet set =  query(SQL);
		while(set.next()){
			sequence = set.getString(1);
		}
		return sequence;
	}
	
	public void commit() throws SQLException{
		conn.commit();
	}
}
