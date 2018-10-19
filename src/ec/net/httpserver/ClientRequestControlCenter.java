package ec.net.httpserver;

import ec.system.Runner;
import ec.system.controller.QueneDataController;

public class ClientRequestControlCenter extends QueneDataController{

	private EcHttpServer httpServer = null;
	private ClassServiceEngine clsServiceEngine = null;
	
	public ClientRequestControlCenter(EcHttpServer httpServer){
		this.httpServer = httpServer;
	}
	
	public ClientRequestControlCenter(EcHttpServer httpServer,ClassServiceEngine clsServiceEngine){
		this.httpServer = httpServer;
		this.clsServiceEngine = clsServiceEngine;
	}
	
	
	@Override
	protected boolean validateNewDataInQuene(Object dataWillInQuene) {
		return dataWillInQuene instanceof HttpClientRequest;
	}

	@Override
	protected void process(Object dataInQuene) {
		HttpClientRequest clientRequest = (HttpClientRequest) dataInQuene;
		boolean isClsServiceHandleDone = false;
		if(clsServiceEngine != null) {
			if(clsServiceEngine.isClassServiceRequest(clientRequest.QueryURI())){
				try {
					clsServiceEngine.callClassService(clientRequest);
					isClsServiceHandleDone = true;
				} catch(Exception e){
					exportExceptionText(e);
					clientRequest.response404NotFound();
				}
			}
		}
		if(!isClsServiceHandleDone){
			if (clientRequest.isGetMethod()) httpServer.onClientGetRequest((ClientGetRequest) clientRequest);
			else if(clientRequest.isPostMethod()) httpServer.onClientPostRequest((ClientPostRequest) clientRequest);
		}
		if(!clientRequest.isAsyncJob()) clientRequest.closeClientConnection();
	}

}
