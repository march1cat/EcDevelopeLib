package ec.net.socketserver;

import ec.system.Basis;

public abstract class SocketClientRequest extends Basis{

	protected NetConnectionServant client = null;
	
	public SocketClientRequest(NetConnectionServant client){
		this.client = client;
	}
	
	public abstract Object process();
	public abstract Object processFail();
	
	public void responseToClient(Object resData){
		client.response(resData);
	}
}
