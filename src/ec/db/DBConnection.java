package ec.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import ec.log.ExceptionLogger;
import ec.log.QueneLogger;

public class DBConnection {

	public static Map<String,ConnectLink> c_mp;
	
	public static void initialMySQLConnection(String host,int port,String dbName,String userName,String userPwd){
		ConnectLink cl = initConnLink(host,port,dbName,userName,userPwd);
		String className = "com.mysql.jdbc.Driver";
		String connString = "jdbc:mysql://" + cl.getDbHost() + ":" + cl.getDbPort() + "/" + cl.getDbName();
		buildConnection(cl,className,connString,false);
	}
	
	
	public static void initialSQLServerConnection(String host,int port,String dbName,String userName,String userPwd){
		ConnectLink cl = initConnLink(host,port,dbName,userName,userPwd);
		String className = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String connString = "jdbc:sqlserver://"+cl.getDbHost()+":"+cl.getDbPort()+";databaseName="+cl.getDbName()+";user="+cl.getUserName()+";password="+cl.getPwd()+";loginTimeout=500;socketTimeout=500"; //for jdbc 2.0
		buildConnection(cl,className,connString,true);
	}
	
	public static void initialSQLServerExConnectionByJTDS(String host,int port,String dbName,String domain,String userName,String userPwd){
		ConnectLink cl = initConnLink(host,port,dbName,userName,userPwd);
		String className = "net.sourceforge.jtds.jdbc.Driver";
		String connString = "jdbc:jtds:sqlserver://"+cl.getDbHost()+";instance=SQLEXPRESS;DatabaseName=" + cl.getDbName() + ";Domain=" + domain; //for jdbc 2.0
		buildConnection(cl,className,connString,false);
	}
	
	public static void initialPostgrelSQLConnection(String host,int port,String dbName,String userName,String userPwd){
		ConnectLink cl = initConnLink(host,port,dbName,userName,userPwd);
		String className = "org.postgresql.Driver";
		String connString = "jdbc:postgresql://" + cl.getDbHost() + ":" + cl.getDbPort()+ "/" + cl.getDbName();
		buildConnection(cl,className,connString,false);
	}
	
	public static void initialDB2SQLConnection(String host,int port,String dbName,String userName,String userPwd){
		ConnectLink cl = initConnLink(host,port,dbName,userName,userPwd);
		String className = "com.ibm.db2.jcc.DB2Driver";
		String connString = "jdbc:db2://"+cl.getDbHost()+":"+cl.getDbPort()+"/"+cl.getDbName();
		buildConnection(cl,className,connString,false);
	}
	
	private static void buildConnection(ConnectLink cl , String driverClassName,String connectionString,boolean textContainUserInfo) {
		try {
			cl.setDriverClassName(driverClassName);
			cl.setConnText(connectionString);
			cl.setConnTextContainUserInfo(textContainUserInfo);
			
			Class.forName(driverClassName);
			Connection c = textContainUserInfo ? 
					DriverManager.getConnection(connectionString) : 
						DriverManager.getConnection(connectionString,cl.getUserName(),cl.getPwd());
			cl.setDbConn(c);
		} catch(Exception e) {
			QueneLogger logger = QueneLogger.getLogger();
			if(logger != null) logger.log("Build Connection With MySQL Server Fail,"
					+ "Host["+cl.getDbHost()+"],Port["+cl.getDbPort()+"],DBName["+cl.getDbName()+"],UName["+cl.getUserName()+"],UPwd["+cl.getPwd()+"]");
			ExceptionLogger errorLogger = ExceptionLogger.getLogger();
			if(errorLogger != null) errorLogger.writeException(e);
			else e.printStackTrace();
			if(cl != null) cl.closeLink();
		}
	}
	
	
	private static ConnectLink initConnLink(String host,int port,String dbName,String userName,String userPwd) {
		if(c_mp == null) c_mp = new HashMap<String,ConnectLink>();
		ConnectLink cl = c_mp.get(dbName);
		if(cl != null) cl.closeLink();
		else {
			cl = new ConnectLink(dbName);
			cl.setDbHost(host);
			cl.setDbPort(port);
			cl.setUserName(userName);
			cl.setPwd(userPwd);
			c_mp.put(dbName, cl);
		}
		return cl;
	}
	
	public static Connection getConnection(String dbName){
		if(c_mp != null){
			ConnectLink cl = c_mp.get(dbName);
			return cl != null ? cl.getDbConn() : null;
		} return null;
	}
	
	public static void reBuildConnection(String dbName) {
		if(c_mp != null){
			ConnectLink cl = c_mp.get(dbName);
			if(cl != null) {
				cl.closeLink();
				buildConnection(cl,cl.getDriverClassName(),cl.getConnText(),cl.isConnTextContainUserInfo());
			}
		}
	}
	
	public static void closeConnection(String dbName){
		if(c_mp != null){
			ConnectLink cl = c_mp.get(dbName);
			if(cl != null) {
				cl.closeLink();
				c_mp.remove(dbName);
			}
		}
	}
	
}
