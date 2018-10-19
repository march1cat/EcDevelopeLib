package ec.net.socketclient;

public interface NetDataListener {

	public boolean isMatchListeningData(String data);
	public void onListeningData(String data,ClientConnector clientConnector);
}
