package ec.db;

import java.sql.PreparedStatement;

public class SQLBatch {

	private PreparedStatement ptmt = null;
	private Object[] keys = null;
	public SQLBatch(PreparedStatement ptmt,Object[] keys) {
		this.ptmt = ptmt;
		this.keys = keys;
	}
	public PreparedStatement getPtmt() {
		return ptmt;
	}
	public Object[] getKeys() {
		return keys;
	}
	
	
	
}
