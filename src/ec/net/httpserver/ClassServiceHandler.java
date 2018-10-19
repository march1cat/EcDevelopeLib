package ec.net.httpserver;

import ec.system.Basis;

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
}
