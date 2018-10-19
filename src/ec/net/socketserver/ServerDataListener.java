package ec.net.socketserver;

public interface ServerDataListener {
	public boolean isMatchListener(String data);
	public void onListeningData(String data,NetConnectionServant servant);
}
