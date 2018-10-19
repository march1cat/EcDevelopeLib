package ec.net.socketclient;

public interface NetClient {

	public void onClientConnectorData(String data);
	public void actionBeforeWaitingReceivingData(ClientConnector connector);
	public void onConnectionBuiltSuccess();
	public void onConnectionBreakEvent();
}
