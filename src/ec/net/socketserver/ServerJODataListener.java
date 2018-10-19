package ec.net.socketserver;

public interface ServerJODataListener {
	public boolean isMatchListener(Object JavaObjectData);
	public void onListeningData(Object JavaObjectData,NetJODataConnectionServant servant);
}
