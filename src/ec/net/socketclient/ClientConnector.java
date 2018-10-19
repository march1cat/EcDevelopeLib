package ec.net.socketclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ec.system.Runner;

public class ClientConnector extends Runner{
	
	
	private boolean isOnWorking = true;
	private String name = null;
	private Socket clientSkt = null;
	private PrintWriter connectionDataWriter = null;
	private String targetServerIP = null;
	private int targetServerPort = -1;
	private String dataEncode = "UTF-8";
	private NetClient clientUser = null;
	public static long reconnectWaitTime = 3000;
	private List<NetDataListener> dataListeners = null;
	
	public boolean IsMakingLogReceiveData = true;
	
	private int retryTimeLimit = -1;
	
	public ClientConnector(String name,String targetServerIP, int targetServerPort,NetClient clientUser) {
		super();
		this.name = name;
		this.targetServerIP = targetServerIP;
		this.targetServerPort = targetServerPort;
		this.clientUser = clientUser;
	}
	public ClientConnector(String name,String targetServerIP, int targetServerPort,NetClient clientUser,String dataEncode) {
		super();
		this.name = name;
		this.targetServerIP = targetServerIP;
		this.targetServerPort = targetServerPort;
		this.clientUser = clientUser;
		this.dataEncode = dataEncode;
	}

	@Override
	protected void running() {
		if(targetServerIP == null || targetServerPort == -1){
			this.stopRunner();
			log("Client Connector["+name+"] Handler Stop Due to Wrong Config IP="+targetServerIP + ",Port="+targetServerPort);
		} else {
			int retryReconnecFailCount = 0;
			while(isOnWorking){
				try {
					log(name + " Try to Build Connection With Server("+targetServerIP+":"+targetServerPort+")",Module.MIDDLE_WARE);
					clientSkt = new Socket(targetServerIP, targetServerPort);
					clientUser.onConnectionBuiltSuccess();
					log(name + " Connection With Server("+targetServerIP+":"+targetServerPort+") is Built Successfully",Module.MIDDLE_WARE);
				} catch(Exception e){
					this.except(name + " Build Connection with Server["+targetServerIP+":"+targetServerPort+"] Fail", getClass().getName());
					this.exportExceptionText(e);
					clientSkt = null;
					if(retryTimeLimit > 0){
						retryReconnecFailCount++;
						if(retryReconnecFailCount >= retryTimeLimit){
							log("Retry Reconnec Fail Times has been limit values[" + retryTimeLimit + "],Stop Retry Build Connection!!");
							stopRunner();
							break;
						}
					}
				}
				if(clientSkt != null){
					actionBeforeReceiveData();
					try {
						receivingData(clientSkt.getInputStream());
					} catch (Exception e) {
						this.except("Connection is Closed", this.getClass().getName());
						onConnectionBreak();
					}
				}
				
				if(clientSkt == null){
					clientUser.onConnectionBreakEvent();
					connectionDataWriter = null;
					log(name + " Will Retry to build Connection to Server["+targetServerIP+":"+targetServerPort+"] in " + reconnectWaitTime + "ms");
					try {
						Thread.sleep(reconnectWaitTime);
					} catch (InterruptedException e) {}
				}
			}
		}
	}
	
	protected void onConnectionBreak(){
		clientSkt = null;
	}

	@Override
	protected void end() {
		log("Client Connector["+name+"] Handler is down");
	}
	
	protected void receivingData(InputStream inputIs) throws Exception{
		BufferedReader in = new BufferedReader( new InputStreamReader(inputIs,dataEncode));
		String inputLine = null;
		while ((inputLine = in.readLine()) != null) {
			if("".equals(inputLine)) continue;
			onSocketData(inputLine);
		}
		throw new Exception("Connection Close");
	}
	
	protected void onSocketData(String data){
		if(IsMakingLogReceiveData) 
			log(name + " Receive Data From [Server:" + targetServerIP + ":" + targetServerPort+"] ,Data = " + data + "",Module.MIDDLE_WARE);
		if(dataListeners != null && !dataListeners.isEmpty())
			for(NetDataListener listener : dataListeners){
				if(listener.isMatchListeningData(data)) listener.onListeningData(data, this);
			}
		if(clientUser != null) clientUser.onClientConnectorData(data);
	}
	
	public void registerDataListener(NetDataListener listener){
		if(dataListeners == null) dataListeners = new ArrayList<>();
		dataListeners.add(listener);
	}
	private void actionBeforeReceiveData(){
		if(clientUser != null) clientUser.actionBeforeWaitingReceivingData(this);
	}
	public void sendRequestToServerWithLineSeparator(String cmd) throws Exception{
		log(name + " Send Request Command To Server,Cmd = " + cmd,Module.MIDDLE_WARE);
		if(clientSkt != null){
			if(connectionDataWriter == null) {
				connectionDataWriter = new PrintWriter( new BufferedWriter(new OutputStreamWriter(clientSkt.getOutputStream(), dataEncode)), true);
			}
			cmd += "\r\n";
			connectionDataWriter.write(cmd);
			connectionDataWriter.flush();
		} else {
			log(name + " Connection isn't built yet",Module.MIDDLE_WARE);
		}
	}
	
	public String name(){
		return this.name;
	}
	
	
	public void closeConnection() throws IOException{
		if(clientSkt != null) clientSkt.close();
		this.stopRunner();
		isOnWorking = false;
	}
	
	protected Socket ClientSkt(){
		return clientSkt;
	}
	
	public void closeAutoLogger(){
		this.IsMakingLogReceiveData = false;
	}
	
	public void setPreferRetryTimes(int retryTimeLimit){
		this.retryTimeLimit = retryTimeLimit;
	}
	public Socket getClientSkt() {
		return clientSkt;
	}
	
	
	
}
