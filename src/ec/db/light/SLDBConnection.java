package ec.db.light;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SLDBConnection {
	public static Map<String,Connection> c_mp;
	public static void initialConnection(String dbPos){
		if(c_mp == null) c_mp = new HashMap<String,Connection>();
		Connection c = c_mp.get(dbPos);
		if(c == null){
			try {
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:" + dbPos);
				c_mp.put(dbPos, c);
			} catch (Exception e) {
				e.printStackTrace();
				c = null;
			}
		}
	}
	public static Connection getConnection(String dbPos){
		return c_mp.get(dbPos);
	}
	public static void closeConnection(String dbPos){
		if(c_mp != null){
			Connection c = c_mp.get(dbPos);
			if(c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				c_mp.remove(dbPos);
			}
		}
	}
}
