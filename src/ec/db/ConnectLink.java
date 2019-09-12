package ec.db;

import java.sql.Connection;

import ec.system.Basis;

public class ConnectLink extends Basis{

	
	private String driverClassName = null;
	private String connText = null;
	private boolean connTextContainUserInfo = false;
	
	private String dbName = null;
	private String userName = null;
	private String pwd = null;
	private String dbHost = null;
	private int dbPort = 0;
	private Connection dbConn = null;
	
	public ConnectLink(String dbName) {
		this.dbName = dbName;
	}
	
	public void closeLink() {
		try {
			if(dbConn != null) dbConn.close();
		} catch(Exception e) {
			this.exportExceptionText(e);
		}
		dbConn = null;
	}
	
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getDbHost() {
		return dbHost;
	}
	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}
	public int getDbPort() {
		return dbPort;
	}
	public void setDbPort(int dbPort) {
		this.dbPort = dbPort;
	}
	public Connection getDbConn() {
		return dbConn;
	}
	public void setDbConn(Connection dbConn) {
		this.dbConn = dbConn;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getConnText() {
		return connText;
	}

	public void setConnText(String connText) {
		this.connText = connText;
	}

	public boolean isConnTextContainUserInfo() {
		return connTextContainUserInfo;
	}

	public void setConnTextContainUserInfo(boolean connTextContainUserInfo) {
		this.connTextContainUserInfo = connTextContainUserInfo;
	}
	
}
