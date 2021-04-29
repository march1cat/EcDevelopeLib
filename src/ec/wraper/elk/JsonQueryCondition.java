package ec.wraper.elk;

import java.util.Map;

import ec.system.Basis;

public abstract class JsonQueryCondition extends Basis{
	
	public enum ConditionType {
		RANGE,EQUAL
	}
	private Object type = null;
	private String colName = null;
	
	public JsonQueryCondition(Object type){
		this.type = type;
	}
	
	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}
	
	public abstract Map toQueryExpressMap();
	public abstract String QueryMethodText();
	
}
