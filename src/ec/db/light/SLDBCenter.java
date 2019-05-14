package ec.db.light;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.db.SQLCenter;
import ec.system.Basis;

public abstract class SLDBCenter extends SQLCenter{
	
	private String dbPosition = null;
	public SLDBCenter(String dbPosition){
		this.dbPosition = dbPosition;
	}
	
	@Override
	public Connection conn(){
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
	
	
	public void resetDB() throws SQLException{
		resetDB(null);
	}
	public void resetDB(String[] exceptTables) throws SQLException{
		Map<Object,Object> query = new HashMap<>();
		query.put("type", "table");
		List<Map<Object,String>> datas = queryRecords("sqlite_master", query);
		if(this.isListWithContent(datas)){
			for(Map<Object,String> data : datas){
				if(compareValue(data.get("name"), "sqlite_sequence")) {
					//Sequence
					String SQL = "update sqlite_sequence set seq=0";
					this.conn().createStatement().executeUpdate(SQL);
					log("Reset SEQUENCE["+data.get("name")+"] done!!");
				} else {
					//Normal tables
					boolean isInclude = true;
					if(exceptTables != null){
						for(String eName : exceptTables){
							if(compareValue(data.get("name"), eName)) {
								isInclude = false;
								break;
							}
						}
					}
					if(isInclude) {
						emptyTable(data.get("name"));
						log("Reset table["+data.get("name")+"] done!!");
					}
				}
			}
			log("Reset db done!!");
		} else {
			log("No table information found when process reset db!!");
		}
	}
	
	@Override
	public void connectionNotBuild() {}
	
	
}
