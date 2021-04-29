package ec.wraper.elk;

import java.util.HashMap;
import java.util.Map;

import ec.system.Basis;

public class JsonQuerySort extends Basis{

	
	private String colName = null;
	private Object stype = null;
	
	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public Object getStype() {
		return stype;
	}

	public void setStype(Object stype) {
		this.stype = stype;
	}
	
	
	public boolean isSortValidateSetting(){
		return colName != null && stype != null && (stype == JsonQuery.SortBy.ASC || stype == JsonQuery.SortBy.DESC);
	}
	
	public Map toQueryExpressMap() {
		Map<String,String> mp = new HashMap<>();
		mp.put(getColName(), stype.toString().toLowerCase());
		return mp;
	}
	
}
