package ec.net.cmd;

import ec.net.httpserver.ClassServiceHandler;
import ec.net.httpserver.EcHttpServer;
import ec.net.httpserver.HttpClientRequest;
import ec.parser.JsonFactory;

public abstract class HttpCommandHandler extends ClassServiceHandler {

	public HttpCommandHandler(HttpClientRequest request, EcHttpServer httpServer) {
		super(request, httpServer);
	}
	
	public abstract void functions();

	protected JsonFactory getFailJsonTemplate(){
		JsonFactory json = new JsonFactory();
		try {
			json.setJSONVariable("result", "fail");
			return json;
		} catch (Exception e) {
			this.exportExceptionText(e);
			return null;
		}
	}
	
	protected JsonFactory getSuccessJsonTemplate(){
		JsonFactory json = new JsonFactory();
		try {
			json.setJSONVariable("result", "ok");
			return json;
		} catch (Exception e) {
			this.exportExceptionText(e);
			return null;
		}
	}
	
	protected void response(JsonFactory data){
		this.response(data.encodeJSON());
	}

	@Override
	protected EcHttpCommandPlatform httpServer() {
		return (EcHttpCommandPlatform) super.httpServer();
	}
	
	
	protected EcCommandProcessor getCommandProccessor(){
		return httpServer().getCommandProcessor();
	}
	
	
}
