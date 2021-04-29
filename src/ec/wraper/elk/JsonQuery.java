package ec.wraper.elk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.parser.JsonFactory;
import ec.system.Basis;

public class JsonQuery extends Basis{

	
	public enum SortBy {
		ASC,DESC
	}
	
	private List<JsonQueryCondition> conditions = null;
	private List<JsonQuerySort> sorts = null;
	
	public void addRangeCondition(String colName,String fromValue,String toValue){
		JsonRangeCondition con = new JsonRangeCondition();
		con.setCondition(colName, fromValue, toValue);
		addCondition(con);
	}
	
	public void addMatchCondition(String colName,String matchValue){
		JsonMatchCondition con = new JsonMatchCondition();
		con.setCondition(colName, matchValue);
		addCondition(con);
	}
	
	public void addCondition(JsonQueryCondition con){
		if(conditions == null) conditions = new ArrayList<>();
		conditions.add(con);
	}
	
	public void addSort(String colName,Object sortType){
		JsonQuerySort sort = new JsonQuerySort();
		sort.setColName(colName);
		sort.setStype(sortType);
		if(sorts == null) sorts = new ArrayList<>();
		sorts.add(sort);
	}
	
	public String transToQueryText(){
		Map<String,Object> data = new HashMap<>();
		if(true){
			//Condition Setting Section
			Map<String,Map> conditionMp = new HashMap<>();
			data.put("query", conditionMp);
			if(isListWithContent(conditions)){
				if(conditions.size() > 1) {
					List cons = new ArrayList<>();
					conditionMp.put("bool", new HashMap<>());
					conditionMp.get("bool").put("must", cons);
					for(JsonQueryCondition con : conditions){
						Map m = new HashMap<>();
						m.put(con.QueryMethodText(), con.toQueryExpressMap());
						cons.add(m);
					}
				} else  conditionMp.put(conditions.get(0).QueryMethodText(), conditions.get(0).toQueryExpressMap());	
			} else conditionMp.put("match_all", new HashMap<>());
		}
		
		if(true && isListWithContent(sorts)){
			//Sort Setting Section
			List s = new ArrayList<>();
			data.put("sort", s);
			for(JsonQuerySort sort : sorts){
				s.add(sort.toQueryExpressMap());
			}
		}
		
		
		JsonFactory t = new JsonFactory(data);
		return t.encodeJSON();
	}
	
}
