package ec.wraper.elk;

import java.util.HashMap;
import java.util.Map;

public class JsonRangeCondition extends JsonQueryCondition{

	
	
	private String fromValue = null;
	private String toValue = null;
	
	public JsonRangeCondition() {
		super(ConditionType.RANGE);
	}
	
	
	

	public String getFromValue() {
		return fromValue;
	}

	public void setFromValue(String fromValue) {
		this.fromValue = fromValue;
	}

	public String getToValue() {
		return toValue;
	}

	public void setToValue(String toValue) {
		this.toValue = toValue;
	}


	public void setCondition(String colName,String fromValue,String toValue) {
		setColName(colName);
		setFromValue(fromValue);
		setToValue(toValue);
	}




	@Override
	public Map toQueryExpressMap() {
		Map<String,Map<String,String>> mp = new HashMap<>();
		Map<String,String> rangeMap = new HashMap<>();
		mp.put(getColName(),rangeMap);
		if(getFromValue() != null) rangeMap.put("gte", getFromValue());
		if(getToValue() != null) rangeMap.put("lt", getToValue());
		return mp;
	}




	@Override
	public String QueryMethodText() {
		return "range";
	}




}
