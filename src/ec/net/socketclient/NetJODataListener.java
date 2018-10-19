package ec.net.socketclient;

public interface NetJODataListener {
	
	public boolean isMatchListeningData(Object JavaObjectData);
	public void onListeningData(Object JavaObjectData,ClientConnector clientConnector);
	
}
