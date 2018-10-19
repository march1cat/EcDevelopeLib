package ec.net.socketserver;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public abstract class JODataServerService extends ServerService{

	
	private List<ServerJODataListener> dataListeners = null;
	
	public JODataServerService(int servicePort, String serviceName) {
		super(servicePort, serviceName);
	}
	
	@Override
	protected NetJODataConnectionServant onGeneratingConnection(Socket socket){
		return new NetJODataConnectionServant(this,socket);
	}
	
	public void registerDataListener(ServerJODataListener listener){
		if(dataListeners == null) dataListeners = new ArrayList<>();
		dataListeners.add(listener);
	}
	
	protected void dispatchDataToListener(Object JavaObjectData,NetJODataConnectionServant servant){
		if(dataListeners!=null && !dataListeners.isEmpty()){
			boolean isMatched = false;
			for(ServerJODataListener listener : dataListeners){
				if(listener.isMatchListener(JavaObjectData)) {
					listener.onListeningData(JavaObjectData, servant);
					isMatched = true;
				}
			}
			if(!isMatched) onNoMatchListenerData(JavaObjectData,servant);
		}
	}
	
	protected void onClientData(Object JavaObjectData,NetJODataConnectionServant servant){
		//Wait to ovveride
	}
	protected void onNoMatchListenerData(Object JavaObjectData,NetJODataConnectionServant servant){
		//Wait to ovveride
	}

}
