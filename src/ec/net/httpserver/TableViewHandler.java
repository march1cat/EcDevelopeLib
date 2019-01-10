package ec.net.httpserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ec.net.httpserver.ecrtable.EcRenderTable;
import ec.parser.JsonFactory;
import ec.system.DeveloperMode;

public abstract class TableViewHandler extends ClassServiceHandler{

	public static final String RES_RESULT_KEY = "result";
	public static final String RES_RESULT_STATUS_SUCCESS = "ok";
	public static final String RES_RESULT_STATUS_FAIL = "fail";
	
	public static final String RES_MESSAGE_KEY = "message";
	
	public static final String RES_DATA_KEY = "data";
	public static final String RES_DATA_STATUS_EMPTY = "empty";
	
	
	private EcRenderTable bindDefEcTable = null;
	
	public TableViewHandler(HttpClientRequest request, EcHttpServer httpServer) {
		super(request, httpServer);
	}
	
	
	public void querySchema() {
		Map<String,Object> extraData = new HashMap<>();
		extraData.put("allowupdate", bindDefEcTable().isAllowUpdate() ? "true" : "false");
		this.responseSuccessWithQueryData( bindDefEcTable().getColumnInfos(),extraData);
	}
	
	public void onOperationMethodNotDefined(){
		//wait to override
		responseFailWithMessage("not defined ec table method");
	}
	

	public void commitUpdate() {
		//wait to override
		this.responseFailWithMessage("operation(commit update) not allow!!");
	}
	
	protected void responseSuccess(){
		this.response("{\""+RES_RESULT_KEY+"\":\""+RES_RESULT_STATUS_SUCCESS+"\"}");
	}
	
	protected void responseSuccessWithQueryData(List<Map<Object,String>> data){
		JsonFactory resData = new JsonFactory();
		try {
			resData.setJSONVariable(RES_RESULT_KEY, RES_RESULT_STATUS_SUCCESS);
			resData.setJSONVariable(RES_DATA_KEY, this.isListWithContent(data) ? data : RES_DATA_STATUS_EMPTY);
			this.response(resData.encodeJSON());
		} catch (Exception e) {
			this.exportExceptionText(e);
			this.except("Prepare Response Json Data Fail,Error = " + e.getMessage());
			repsoneFailOnException();
		}
	}
	
	
	
	protected void responseSuccessWithQueryData(List<Map<Object,String>> data,Map<String,Object> extraData){
		JsonFactory resData = new JsonFactory();
		try {
			resData.setJSONVariable(RES_RESULT_KEY, RES_RESULT_STATUS_SUCCESS);
			resData.setJSONVariable(RES_DATA_KEY, this.isListWithContent(data) ? data : RES_DATA_STATUS_EMPTY);
			Iterator<String> iter = extraData.keySet().iterator();
			while(iter.hasNext()){
				String key = iter.next();
				Object value =  extraData.get(key);
				if(value != null) resData.setJSONVariable(key, value);
			}
			this.response(resData.encodeJSON());
		} catch (Exception e) {
			this.exportExceptionText(e);
			this.except("Prepare Response Json Data Fail,Error = " + e.getMessage());
			repsoneFailOnException();
		}
	}
	
	
	protected void responseSuccessWithQueryData(Map<String,String> data){
		JsonFactory resData = new JsonFactory();
		try {
			resData.setJSONVariable(RES_RESULT_KEY, RES_RESULT_STATUS_SUCCESS);
			resData.setJSONVariable(RES_DATA_KEY, data != null ? data : RES_DATA_STATUS_EMPTY);
			this.response(resData.encodeJSON());
		} catch (Exception e) {
			this.exportExceptionText(e);
			this.except("Prepare Response Json Data Fail,Error = " + e.getMessage());
			repsoneFailOnException();
		}
	}
	
	protected void responseFailWithMessage(String message){
		JsonFactory resData = new JsonFactory();
		try {
			resData.setJSONVariable(RES_RESULT_KEY, RES_RESULT_STATUS_FAIL);
			resData.setJSONVariable(RES_MESSAGE_KEY, message);
			this.response(resData.encodeJSON());
		} catch (Exception e) {
			this.exportExceptionText(e);
			this.except("Prepare Response Json Data Fail,Error = " + e.getMessage());
			repsoneFailOnException();
		}
	}
	
	
	protected void repsoneFailOnException(){
		this.response("{\""+RES_RESULT_KEY+"\":\""+RES_RESULT_STATUS_FAIL+"\"}");
	}
	
	
	public void onParamResovleFail() {
		this.except("Parsing Client Query Data Fail on Query Ec Table Data");
		this.repsoneFailOnException();
	}
	
	

	public EcRenderTable bindDefEcTable() {
		return bindDefEcTable;
	}

	public void setBindDefEcTable(EcRenderTable bindDefEcTable) {
		this.bindDefEcTable = bindDefEcTable;
	}
	
	public abstract void queryData();

}
