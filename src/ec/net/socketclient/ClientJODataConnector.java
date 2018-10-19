package ec.net.socketclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ClientJODataConnector extends ClientConnector{

	private ObjectOutputStream connectionJavaOWriter = null;
	private List<NetJODataListener> dataListeners = null;
	
	public ClientJODataConnector(String name, String targetServerIP, int targetServerPort, NetClient clientUser) {
		super(name, targetServerIP, targetServerPort, clientUser);
	}
	
	@Override
	protected void receivingData(InputStream inputIs) throws Exception{
		boolean isConnecting = true;
		ObjectInputStream in = new ObjectInputStream(inputIs);
		while(isConnecting){
			Object dataO = in.readObject(); 
			onNetJOData(dataO);
		}
		throw new Exception("Connection Close");
	}
	
	public void registerDataListener(NetJODataListener dataListener){
		if(dataListeners == null) dataListeners = new ArrayList<>();
		dataListeners.add(dataListener);
	}
	
	private void onNetJOData(Object JavaObjectData){
		if(isListWithContent(dataListeners)){
			for(NetJODataListener dataListener : dataListeners){
				if(dataListener.isMatchListeningData(JavaObjectData)) dataListener.onListeningData(JavaObjectData, this);
			}
		}
	}
	
	public void sendJOData(Object JavaObjectData){
		if(ClientSkt() != null){
			try {
				if(connectionJavaOWriter == null) {
					connectionJavaOWriter = new ObjectOutputStream(ClientSkt().getOutputStream()); 
				}
				connectionJavaOWriter.writeObject(JavaObjectData); 
				connectionJavaOWriter.flush();
			} catch (IOException e) {
				except("Servant Send Java Seriablable Object Info To Server Fail,Error="+e.getMessage(), getClass().getName());
				connectionJavaOWriter = null;
				exportExceptionText(e);
			}
		}
	}
	
	@Override
	protected void onConnectionBreak(){
		System.err.println("Connection Close!!!!!!");
		connectionJavaOWriter = null;
		super.onConnectionBreak();
	}

}
