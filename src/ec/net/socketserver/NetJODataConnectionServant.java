package ec.net.socketserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class NetJODataConnectionServant extends NetConnectionServant{

	private ObjectOutputStream connectionJavaOWriter = null;
	private JODataServerService server = null;
	
	public NetJODataConnectionServant(JODataServerService server, Socket socket) {
		super(server, socket, null);
		this.server = server;
	}
	
	@Override
	protected void receivingData(InputStream inputIs) throws Exception{
		boolean isConnecting = true;
		ObjectInputStream in = new ObjectInputStream(inputIs);
		while(isConnecting){
			Object dataO = in.readObject(); 
			onClientData(dataO);
		}
		throw new Exception("Connection Close");
	}
	
	protected void onClientData(Object dataO){
		try{
			server.dispatchDataToListener(dataO,this);
			server.onClientData(dataO,this);
		} catch(Exception e){
			this.exportExceptionText(e);
		}
	}
	
	@Override
	public void response(Object JavaClassObect){
		if(socket != null){
			try {
				if(connectionJavaOWriter == null) {
					connectionJavaOWriter = new ObjectOutputStream(socket.getOutputStream()); 
				}
				connectionJavaOWriter.writeObject(JavaClassObect); 
				connectionJavaOWriter.flush();
			} catch (IOException e) {
				except("Servant Response Java Seriablable Object Info To Client Fail,Error="+e.getMessage(), getClass().getName());
				exportExceptionText(e);
			}
		}
	}

}
