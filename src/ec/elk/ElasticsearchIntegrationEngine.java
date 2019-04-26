package ec.elk;

import java.net.ProtocolException;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;


import ec.net.execute.RESTWebQueryFactory;
import ec.parser.JsonFactory;
import ec.system.Basis;
import ec.system.DeveloperMode;

public class ElasticsearchIntegrationEngine extends Basis{

	private String host = null;
	private int port = 80;
	
	private String userName = null;
	private String pwd = null;
	
	public ElasticsearchIntegrationEngine(String host,int port){
		this.host = host;
		this.port = port;
	}
	
	public ElasticsearchIntegrationEngine(String host,int port,String userName,String pwd){
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.pwd = pwd;
	}
	
	public boolean checkDataIsExist(String index,String key) throws Exception{
		RESTWebQueryFactory fac = new RESTWebQueryFactory(
				"http://"+host+":"+port+"/"+index+"/_count", userName, pwd);
		fac.setHeaderValue("Content-Type", "application/json");
		String result = fac.queryWeb("{\"query\": {\"terms\": {\"_id\": [ \""+key+"\"] }}}");
		if(result.indexOf("\"count\":1") >= 0) {
			return true;
		} else if(result.indexOf("\"count\":0") >= 0) {
			return false;
		} else {
			throw new Exception("Check Data Is Exist Fail,Server response = " + result);
		}
	}
	
	public boolean writeDataToElastic(String index,String type,String eKey,String postData){
		try {
			RESTWebQueryFactory fac = new RESTWebQueryFactory(
					"http://"+host+":"+port+"/"+index+"/"+type+"/"+eKey, userName, pwd);
			fac.setHeaderValue("Content-Type", "application/json");
			String result = fac.queryWeb(postData);
			if(result.indexOf("\"successful\":1") >= 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			this.except("Write Data To Elasticsearch Server Fail,Error = " + e.getMessage());
			this.exportExceptionText(e);
		}
		return false;
	}
	
	
	public boolean writeDataToElastic(String index,String type,String eKey,Map<String,Object> postData) throws Exception{
		JsonFactory json = new JsonFactory();
		Iterator<String> iter = postData.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			json.setJSONVariable(key, postData.get(key));
		}
		return writeDataToElastic(index,type,eKey,json.encodeJSON());
	}
	
	
	public QueryResponse query(String index,JsonQuery query) {
		QueryResponse qres = new QueryResponse();
		
		
		try {
			RESTWebQueryFactory fac = new RESTWebQueryFactory(
					"http://"+host+":"+port+"/"+index+"/_search", userName, pwd);
			fac.setHeaderValue("Content-Type", "application/json");
			String result = fac.queryWeb(query.transToQueryText());
			qres.parsingServerRes(result);
		} catch(Exception e){
			qres.setFailException(e);
			qres.setQueryResult(QueryResponse.QueryResult.QUERY_FAIL);
		}
		if(qres.result() == null) this.except("Query Data From Elasticsearch Result is null,Pls Check Class " + getClass().getName());
		return qres;
	}
	
	
	
}
