package ec.net.cmd;

import org.json.JSONException;

import ec.net.httpserver.EcHttpServer;
import ec.net.httpserver.HttpClientRequest;
import ec.parser.JsonFactory;

public  class EcHttpCommandPlatform extends EcHttpServer{

	private EcCommandProcessor commandProcessor = null;
	
	
	public EcHttpCommandPlatform(int servicePort,EcCommandProcessor commandProcessor,Class cmdInterfaceCls) {
		super(servicePort, "Ec Http Command Platform");
		this.commandProcessor = commandProcessor;
		this.embedClassServiceEngine(new CommandClsServiceEngine(this, cmdInterfaceCls));
	}

	@Override
	protected void onGenereateRequestFail(HttpClientRequest clientRequest, Exception e) {
		clientRequest.response("reuqest parsing fail");
	}

	public EcCommandProcessor getCommandProcessor() {
		return commandProcessor;
	}
	
	
	
}
