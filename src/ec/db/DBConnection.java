package ec.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import ec.log.ExceptionLogger;
import ec.log.QueneLogger;

public class DBConnection {

	public static Map<String,Connection> c_mp;
	
	public static void initialMySQLConnection(String host,int port,String dbName,String userName,String userPwd){
		if(c_mp == null) c_mp = new HashMap<String,Connection>();
		Connection c = c_mp.get(dbName);
		if(c != null) c_mp.remove(dbName);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbName,userName,userPwd);
			c_mp.put(dbName, c);
		} catch (Exception e) {
			QueneLogger logger = QueneLogger.getLogger();
			if(logger != null) logger.log("Build Connection With MySQL Server Fail,"
					+ "Host["+host+"],Port["+port+"],DBName["+dbName+"],UName["+userName+"],UPwd["+userPwd+"]");
			ExceptionLogger errorLogger = ExceptionLogger.getLogger();
			if(errorLogger != null) errorLogger.writeException(e);
			else e.printStackTrace();
			c = null;
		}
	}
	
	
	public static void initialSQLServerConnection(String host,int port,String dbName,String userName,String userPwd){
		if(c_mp == null) c_mp = new HashMap<String,Connection>();
		Connection c = c_mp.get(dbName);
		if(c != null) c_mp.remove(dbName);
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String conURL = "jdbc:sqlserver://"+host+":"+port+";databaseName="+dbName+";user="+userName+";password="+userPwd+";loginTimeout=500;socketTimeout=500"; //for jdbc 2.0
			c = DriverManager.getConnection(conURL);
			c_mp.put(dbName, c);
		} catch (Exception e) {
			QueneLogger logger = QueneLogger.getLogger();
			if(logger != null) logger.log("Build Connection With SQL Server Fail,"
					+ "Host["+host+"],Port["+port+"],DBName["+dbName+"],UName["+userName+"],UPwd["+userPwd+"]");
			ExceptionLogger errorLogger = ExceptionLogger.getLogger();
			if(errorLogger != null) errorLogger.writeException(e);
			else e.printStackTrace();
			c = null;
		}
	}
	
	public static void initialSQLServerExConnectionByJTDS(String host,int port,String dbName,String domain,String userName,String userPwd){
		if(c_mp == null) c_mp = new HashMap<String,Connection>();
		Connection c = c_mp.get(dbName);
		if(c != null) c_mp.remove(dbName);
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			String conURL = "jdbc:jtds:sqlserver://"+host+";instance=SQLEXPRESS;DatabaseName=" + dbName + ";Domain=" + domain; //for jdbc 2.0
			c = DriverManager.getConnection(conURL,userName,userPwd);
			c_mp.put(dbName, c);
		} catch (Exception e) {
			QueneLogger logger = QueneLogger.getLogger();
			if(logger != null) logger.log("Build Connection With SQL Server Fail,"
					+ "Host["+host+"],Port["+port+"],DBName["+dbName+"],UName["+userName+"],UPwd["+userPwd+"]");
			ExceptionLogger errorLogger = ExceptionLogger.getLogger();
			if(errorLogger != null) errorLogger.writeException(e);
			else e.printStackTrace();
			c = null;
		}
	}
	
	public static Connection getConnection(String dbName){
		return c_mp.get(dbName);
	}
	public static void closeConnection(String dbName){
		if(c_mp != null){
			Connection c = c_mp.get(dbName);
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				c_mp.remove(dbName);
			}
		}
	}
	
}
