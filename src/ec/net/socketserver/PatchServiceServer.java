package ec.net.socketserver;


import java.net.Socket;

import ec.net.socketserver.NetConnectionServant;
import ec.net.socketserver.ServerService;

public abstract class PatchServiceServer extends ServerService{

	public PatchServiceServer(int portServicePort,String ServiceNme) {
		super(portServicePort, ServiceNme);
		this.startRunner();
	}
	
	
	@Override
	protected NetConnectionServant onGeneratingConnection(Socket socket) {
		return generateNewPatchClientServant(socket);
	}
	
	protected abstract PatchServant generateNewPatchClientServant(Socket socket);


	@Override
	protected void onClientData(String arg0, NetConnectionServant arg1) {}
}
