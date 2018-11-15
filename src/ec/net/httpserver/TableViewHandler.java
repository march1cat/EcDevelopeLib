package ec.net.httpserver;

import ec.net.httpserver.ecrtable.EcRenderTable;

public abstract class TableViewHandler extends ClassServiceHandler{

	private EcRenderTable bindDefEcTable = null;
	
	public TableViewHandler(HttpClientRequest request, EcHttpServer httpServer) {
		super(request, httpServer);
	}
	
	public abstract void querySchema();
	public abstract void queryData();
	public abstract void onParamResovleFail();

	public EcRenderTable bindDefEcTable() {
		return bindDefEcTable;
	}

	public void setBindDefEcTable(EcRenderTable bindDefEcTable) {
		this.bindDefEcTable = bindDefEcTable;
	}
	
	

}
