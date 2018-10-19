package ec.elk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import ec.parser.JsonFactory;
import ec.system.Basis;

public class QueryResponse extends Basis{

	public enum QueryResult{
		QUERY_FAIL,PARSING_FAIL,SUCCESS
	}
	
	
	private Object queryResult = null;
	private Exception e = null;
	private String message = null;
	private int resDataAmount = 0;
	private List<Map<String, String>> data = null;
	
	public void parsingServerRes(String resText){
		try {
			JsonFactory fac = new JsonFactory(resText);
			 parsingResult(fac.getSubJFactory("_shards"));
			 parsingContent(fac.getSubJFactory("hits"));
		} catch (Exception e) {
			setQueryResult(QueryResult.PARSING_FAIL);
			setFailException(e);
			message = "Parsing Elasticsearcg Res Data Fail,Data = " + resText;
		}
		
	}
	
	private void parsingResult(JsonFactory fac){
		if(fac.getObjectValue("successful") != null){
			queryResult = parseInt(fac.getObjectValue("successful"), 0) > 0 ? QueryResult.SUCCESS : QueryResult.QUERY_FAIL;
		} 
		if(queryResult == null) queryResult = QueryResult.QUERY_FAIL;
	}
	
	private void parsingContent(JsonFactory fac) throws Exception{
		resDataAmount = parseInt(fac.getObjectValue("total"), -1);
		if(resDataAmount == -1) throw new Exception("Parsing Response Data Amount Fail!!, parsing Target = " + fac.getObjectValue("total"));
		if(resDataAmount > 0) {
			List<JsonFactory> hits =  fac.getJsonDataArrayList("hits");
			if(isListWithContent(hits)){
				for(JsonFactory hit : hits){
					JsonFactory recordContainer = hit.getSubJFactory("_source");
					if(data == null) data = new ArrayList<>();
					data.add(recordContainer.transToMap());
				}
			}
		}
	}
	
	public void setQueryResult(Object o){
		queryResult = o;
	}
	public void setFailException(Exception e){
		this.e = e;
	}
	public Object result(){
		return queryResult;
	}
	

	public List<Map<String, String>> getDatas() {
		return data;
	}

	@Override
	public String toString() {
		Map<String,Object> info = new HashMap<>();
		info.put("RESULT", result());
		info.put("DATA_AMOUNT", resDataAmount);
		return info.toString();
	}

	public String getMessage() {
		return message;
	}

	public Exception getE() {
		return e;
	}
	
	
	
}
