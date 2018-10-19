package ec.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MySQL {

  private Connection conn = null;
  
  private String host;
  
  public MySQL(String host,int port,String DbName){
    this.host = new String("jdbc:mysql://" + host + ":" + port + "/" + DbName);
  }
  public MySQL(String host,String DbName){
    this.host = new String("jdbc:mysql://" + host + "/" + DbName);
  }
  
  public void connectDB(String drivername,String username,String password) throws SQLException, ClassNotFoundException{
    Class.forName(drivername);
    conn = DriverManager.getConnection(host,username,password);
  }
  
  public void connectDB(String username,String password) throws SQLException, ClassNotFoundException{
    String driver = "com.mysql.jdbc.Driver";
    this.connectDB(driver,username, password);
  }
  
  public ResultSet query(String sql) throws SQLException{
    Statement stmt = conn.createStatement();
    ResultSet set = stmt.executeQuery(sql);
    return set;
  }
  
  public ArrayList<Map<String,String>> queryToList(String sql,String[] colNameAr) throws SQLException{
    ResultSet set = query(sql);
    ArrayList<Map<String,String>> arlist = parseResultSetToArrayList(set,colNameAr);
    return arlist;
  }
  
  private ArrayList<Map<String,String>> parseResultSetToArrayList(ResultSet set,String[] colNameAr) throws SQLException{
    ArrayList<Map<String,String>> arlist = new ArrayList<Map<String,String>>();
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
  
}
