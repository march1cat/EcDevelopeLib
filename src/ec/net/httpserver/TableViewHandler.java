package ec.net.httpserver;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ec.net.httpserver.ecrtable.EcRenderTable;
import ec.parser.JsonFactory;
import ec.system.DeveloperMode;

public abstract class TableViewHandler extends ClassServiceHandler{

	//Client Request Common Definition
	public static final String REQ_ACTION_KEY = "action";
	
	//Response Common Definition
	public static final String RES_RESULT_KEY = "result";
	public static final String RES_RESULT_STATUS_SUCCESS = "ok";
	public static final String RES_RESULT_STATUS_FAIL = "fail";
	
	public static final String RES_MESSAGE_KEY = "message";
	
	public static final String RES_DATA_KEY = "data";
	public static final String RES_DATA_STATUS_EMPTY = "empty";
	
	
	//Allow Action Definition
	public static final String METHOD_REFFER_QUERY_DATA = "queryData";
	public static final String METHOD_REFFER_QUERY_SCHEMA = "querySchema";
	public static final String METHOD_REFFER_QUERY_FORM_DEF = "queryFormDefinition";
	public static final String METHOD_REFFER_COMMIT_INSERT = "commitInsert";
	public static final String METHOD_REFFER_COMMIT_UPDATE = "commitUpdate";
	public static final String METHOD_REFFER_COMMIT_DELETE = "commitDelete";
	public static final String METHOD_REFFER_EXCEPT_METHOD_UNDEFINED = "onOperationMethodNotDefined";
	public static final String METHOD_REFFER_EXCEPT_PARAMETER_PARSE_FAIL = "onParamResovleFail";
	
	private EcRenderTable bindDefEcTable = null;
	private String viewAction = null;
	
	public TableViewHandler(HttpClientRequest request, EcHttpServer httpServer) {
		super(request, httpServer);
	}
	
	
	public void querySchema() {
		Map<String,Object> extraData = new HashMap<>();
		extraData.put("allowupdate", bindDefEcTable().isAllowUpdate() ? "true" : "false");
		extraData.put("allowdelete", bindDefEcTable().isAllowDelete() ? "true" : "false");
		this.responseSuccessWithQueryData( bindDefEcTable().getColumnInfos(),extraData);
	}
	
	public void queryFormDefinition(){
		this.responseSuccessWithQueryData( bindDefEcTable().getColumnInfos());
	}
	
	public void onOperationMethodNotDefined(){
		//wait to override
		responseFailWithMessage("not defined ec table method");
	}
	public void commitInsert() {
		//wait to override
		this.responseFailWithMessage("operation(commit insert) not allow!!");
	}

	public void commitUpdate() {
		//wait to override
		this.responseFailWithMessage("operation(commit update) not allow!!");
	}
	
	public void commitDelete() {
		//wait to override
		this.responseFailWithMessage("operation(commit delete) not allow!!");
	}
	
	
	
	
	protected void responseSuccess(){
		this.response("{\""+RES_RESULT_KEY+"\":\""+RES_RESULT_STATUS_SUCCESS+"\"}");
	}
	
	protected void responseSuccessWithQueryData(List<Map<Object,String>> data){
		JsonFactory resData = new JsonFactory();
		try {
			if(compareValue(viewAction, METHOD_REFFER_QUERY_DATA))  sortDataList(data);
			
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
			if(compareValue(viewAction, METHOD_REFFER_QUERY_DATA))  sortDataList(data);
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
	
	protected String getParameter(String ColumnId){
		if(compareValue(viewAction, METHOD_REFFER_QUERY_DATA)){
			String data = bindDefEcTable().isQueryConditionExist(ColumnId) ? bindDefEcTable().getQueryTextValue(ColumnId) : null;
			return data;
		} else if (compareValue(viewAction, METHOD_REFFER_COMMIT_INSERT)){
			return bindDefEcTable().getCommiValue(ColumnId);
		} else if (compareValue(viewAction, METHOD_REFFER_COMMIT_UPDATE)){
			return bindDefEcTable().getCommiValue(ColumnId);
		} else if (compareValue(viewAction, METHOD_REFFER_COMMIT_DELETE)){
			return bindDefEcTable().getCommiValue(ColumnId);
		} else return null;
	}

	public EcRenderTable bindDefEcTable() {
		return bindDefEcTable;
	}

	public void setBindDefEcTable(EcRenderTable bindDefEcTable) {
		this.bindDefEcTable = bindDefEcTable;
	}
	
	
	
	public void setViewAction(String viewAction) {
		this.viewAction = viewAction;
	}
	
	private void sortDataList(List<Map<Object,String>> datas){
		
		if(this.isListWithContent(datas)) {
			if(bindDefEcTable.getOrderByColumeID() != null){
				Collections.sort(datas, new Comparator<Map<Object,String>>() {
				    @Override
				    public int compare(Map<Object,String> m1, Map<Object,String> m2) {
				    	String v1 = null;
				    	if(true){
				    		Iterator<Object> iter = m1.keySet().iterator();
					    	while(iter.hasNext()){
					    		Object key = iter.next();
					    		if(bindDefEcTable.getOrderByColumeID().equalsIgnoreCase(key.toString())){
					    			v1 = m1.get(key);
					    			break;
					    		}
					    	}
				    	}
				    	
				    	String v2 = null;
				    	if(true){
				    		Iterator<Object> iter = m2.keySet().iterator();
					    	while(iter.hasNext()){
					    		Object key = iter.next();
					    		if(bindDefEcTable.getOrderByColumeID().equalsIgnoreCase(key.toString())){
					    			v2 = m2.get(key);
					    			break;
					    		}
					    	}
				    	}
				    	if(v1 != null && v2 != null){
				    		if(bindDefEcTable.getOrderByType() == EcRenderTable.OrderByType.DESC)
					    		return v1.compareTo(v2) * -1;
					    	else 
					    		return v1.compareTo(v2);
				    	} else {
				    		return 0;
				    	}
				    	
				    	
				    }
				});
			}
		}
	}


	public abstract void queryData();

}
