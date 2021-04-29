package ec.wraper.elk;

import java.util.HashMap;
import java.util.Map;

public class JsonMatchCondition extends JsonQueryCondition{

	private String matchValue = null;
	public JsonMatchCondition() {
		super(ConditionType.EQUAL);
	}
	public String getMatchValue() {
		return matchValue;
	}
	public void setMatchValue(String matchValue) {
		this.matchValue = matchValue;
	}
	
	public void setCondition(String colName,String matchValue) {
		setColName(colName);
		setMatchValue(matchValue);
	}
	@Override
	public Map toQueryExpressMap() {
		Map<String,String> mp = new HashMap<>();
		mp.put(getColName(), getMatchValue());
		return mp;
	}
	@Override
	public String QueryMethodText() {
		return "match";
	}
	

}
