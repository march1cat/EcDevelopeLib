package ec.net.httpserver;

import java.util.List;
import java.util.Map;

import ec.parser.JsonFactory;
import ec.system.Basis;
import ec.system.DeveloperMode;

public abstract class ClassServiceHandler extends Basis{
	
	private HttpClientRequest request = null;
	private EcHttpServer httpServer = null;
	
	public ClassServiceHandler(HttpClientRequest request,EcHttpServer httpServer){
		this.request = request;
		this.httpServer = httpServer;
	}
	protected HttpClientRequest request(){
		return request;
	}
	
	protected void response(String data){
		request.response(data);
	}
	
	protected boolean isRequestWithParameter(){
		return request.getParameters() != null;
	}
	
	protected String getRequestParameter(String name){
		if(isRequestWithParameter()) {
			return request.getParameters().get(name);
		} else return null;
	}
	
	protected EcHttpServer httpServer(){
		return httpServer;
	}
	
	
	protected void response404(){
		ResponseNoResource res = new ResponseNoResource();
		this.request().response(res);
	}
	
	
	protected void responseListInJson(List<Map<Object,Object>> datas){
		JsonFactory resData = new JsonFactory();
		try {
			resData.setJSONVariable("data", datas);
			this.response(resData.encodeJSON());
			if(DeveloperMode.isON()) {
				log("Response List in Json, data = " + resData.encodeJSON());
			}
		} catch (Exception e) {
			this.exportExceptionText(e);
			this.except("Prepare Response Json Data Fail,Error = " + e.getMessage());
		}
	}
	
	
	protected void responseRangeFile(String fpath) throws Exception{
		ResponseServerBytesResource res = new ResponseServerBytesResource(fpath);
		this.request().response(res);
	}
	
	
}
