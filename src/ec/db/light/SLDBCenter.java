package ec.db.light;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ec.db.SQLCenter;
import ec.system.Basis;

public abstract class SLDBCenter extends SQLCenter{
	
	private String dbPosition = null;
	public SLDBCenter(String dbPosition){
		this.dbPosition = dbPosition;
	}
	
	@Override
	protected Connection conn(){
		if(conn == null){
			SLDBConnection.initialConnection(dbPosition);
			conn = SLDBConnection.getConnection(dbPosition);
		}
		return conn;
	}
	
	@Override
	public void closeConnection() throws SQLException{
		if(conn != null){
			SLDBConnection.closeConnection(dbPosition);
			conn = null;
		}
	}

	@Override
	public void connectionNotBuild() {}
	
	
}
