package ec.net.socketserver;

public abstract class AutoDispatchStClientRequest extends SocketClientRequest{

	protected String clientTextData = null;
	
	public AutoDispatchStClientRequest(NetConnectionServant client,String clientTextData) {
		super(client);
		this.clientTextData = clientTextData;
	}
	
	
	public  abstract  boolean isMatchDefined(String clientData);
	

}
