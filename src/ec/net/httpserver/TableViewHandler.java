package ec.net.httpserver;

import ec.net.httpserver.ecrtable.EcRenderTable;
import ec.parser.JsonFactory;

public abstract class TableViewHandler extends ClassServiceHandler{

	private EcRenderTable bindDefEcTable = null;
	
	public TableViewHandler(HttpClientRequest request, EcHttpServer httpServer) {
		super(request, httpServer);
	}
	
	
	public void querySchema() {
		JsonFactory resData = new JsonFactory();
		try {
			resData.setJSONVariable("result", "ok");
			resData.setJSONVariable("data", bindDefEcTable().getColumnInfos());
			resData.setJSONVariable("allowupdate", bindDefEcTable().isAllowUpdate() ? "true" : "false");
			log(resData.encodeJSON());
			this.response(resData.encodeJSON());
		} catch (Exception e) {
			this.exportExceptionText(e);
			this.except("Prepare Response Json Data Fail,Error = " + e.getMessage());
			this.response("{\"result\":\"fail\"}");
		}
	}
	
	public void onOperationMethodNotDefined(){
		JsonFactory resData = new JsonFactory();
		try {
			resData.setJSONVariable("result", "fail");
			resData.setJSONVariable("message", "not defined ec table method");
			this.response(resData.encodeJSON());
		} catch (Exception e) {
			this.exportExceptionText(e);
			this.except("Prepare Response Json Data Fail,Error = " + e.getMessage());
			this.response("{\"result\":\"fail\"}");
		}
	}
	
	public void commitUpdate() {
		JsonFactory resData = new JsonFactory();
		try {
			resData.setJSONVariable("result", "fail");
			resData.setJSONVariable("message", "operation(commit update) not allow!!");
			this.response(resData.encodeJSON());
		} catch (Exception e) {
			this.exportExceptionText(e);
			this.except("Prepare Response Json Data Fail,Error = " + e.getMessage());
			this.response("{\"result\":\"fail\"}");
		}
	}
	
	public abstract void queryData();
	public abstract void onParamResovleFail();
	
	

	public EcRenderTable bindDefEcTable() {
		return bindDefEcTable;
	}

	public void setBindDefEcTable(EcRenderTable bindDefEcTable) {
		this.bindDefEcTable = bindDefEcTable;
	}
	
	

}
