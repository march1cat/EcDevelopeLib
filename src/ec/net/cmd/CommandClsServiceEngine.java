package ec.net.cmd;

import ec.net.httpserver.ClassServiceEngine;
import ec.net.httpserver.EcHttpServer;
import ec.net.httpserver.HttpClientRequest;

public class CommandClsServiceEngine extends ClassServiceEngine{

	private Class cmdInterfaceCls = null;
	public CommandClsServiceEngine(EcHttpServer httpServer,Class cmdInterfaceCls) {
		super(httpServer);
		this.cmdInterfaceCls = cmdInterfaceCls;
	}
	@Override
	protected Class requestClass(HttpClientRequest clientRequest) throws Exception {
		return cmdInterfaceCls;
	}
	
}
