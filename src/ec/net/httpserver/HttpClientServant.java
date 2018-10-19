package ec.net.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import ec.net.socketserver.NetConnectionServant;
import ec.net.socketserver.ServerService;

public class HttpClientServant extends NetConnectionServant{

	
	private boolean OnReadLineMode = true;
	private int ContentSize = 0;
	
	
	public HttpClientServant(ServerService server, Socket socket, String serviceEncode) {
		super(server, socket, serviceEncode);
	}
	
	@Override
	protected void receivingData(InputStream inputIs) throws Exception {
		BufferedReader in = new BufferedReader( new InputStreamReader(inputIs,serviceEncode));
		String inputLine = null;
		while(true) {
			if(OnReadLineMode) inputLine = in.readLine();
			else {
				char[] k = new char[ContentSize];
				in.read(k);
				inputLine = new String(k);
			}
			if(inputLine != null) onSocketData(inputLine);
			
			if(OnReadLineMode){
				if(inputLine == null) break;
			} else {
				if (inputLine.length() > 0) break;
			}
		}
		//throw new Exception("Http Client Connection Close");
	}
	
	public void closeClientConnection(){
		this.stopRunner();
		try {
			socket.close();
		} catch (IOException e) {
			exportExceptionText(e);
		}
		socket = null;
	}
	
	public void switchReadLineModeOff(int contentSize){
		OnReadLineMode = false;
		ContentSize = contentSize;
	}

	public void response(Response res){
		try{
			if(socket != null){
				socket.getOutputStream().write(res.getToResponseData());
				socket.getOutputStream().flush();
			} else throw new Exception("Servant's socket component can't be null!!");
		} catch(Exception e){
			this.exportExceptionText(e);
		}
	}

	@Override
	protected void autoStartProcessService() {}
	public void triggerRunning(){
		this.running();
	}
	
}
