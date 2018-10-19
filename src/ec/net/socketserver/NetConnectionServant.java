package ec.net.socketserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import ec.system.DeveloperMode;
import ec.system.Runner;
import ec.system.RunningPlatform;
import ec.system.TimerInvoker;

public class NetConnectionServant extends Runner implements TimerInvoker{

	protected Socket socket = null;
	private ServerService server = null;
	protected String clientIP = null;
	protected String serviceEncode = null;
	private PrintWriter connectionDataWriter = null;
	private SystemMainController sysController = null;
	private NetServeCustomer serveCustomer = null;
	
	public NetConnectionServant(ServerService server,Socket socket,String serviceEncode){
		this.server = server;
		this.socket = socket;
		this.serviceEncode = serviceEncode;
		clientIP = socket.getInetAddress().getHostAddress();
		if(DeveloperMode.isON()) sysController = new SystemMainController(server, this);
		autoStartProcessService();
	}
	
	
	protected void autoStartProcessService(){
		this.startRunner();
	}
	
	@Override
	protected void beforeRunning() {
		log("Generate Net Connection Servant For Client IP = " + this.clientIP + ",Port = " + server.port(),Module.MIDDLE_WARE);
	}

	@Override
	protected void end() {
		log("End Net Connection Servant For Client IP = " + this.clientIP + ",Port = " + server.port(),Module.MIDDLE_WARE);
		server.onClientCloseConnction(this);
	}

	@Override
	protected void running() {
		try {
			receivingData(socket.getInputStream());
		} catch (Exception e) {
			stopRunner();
			try {
				socket.close();
			} catch (Exception e1) {}
			socket = null;
		}
	}

	public void response(Object data){
		if(socket != null && data != null){
			try {
				if(connectionDataWriter == null) {
					connectionDataWriter = new PrintWriter( new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), serviceEncode)), true);
				}
				data += "\r\n";
				connectionDataWriter.write(data.toString());
				connectionDataWriter.flush();
			} catch (IOException e) {
				except("Servant Response Info To Client Fail,Data="+data+",Error="+e.getMessage(), getClass().getName());
				exportExceptionText(e);
			}
		}
	}
	

	protected void receivingData(InputStream inputIs) throws Exception{
		BufferedReader in = new BufferedReader( new InputStreamReader(inputIs,serviceEncode));
		String inputLine = null;
		while ((inputLine = in.readLine()) != null) {
			if("".equals(inputLine)) continue;
			try{
				onSocketData(inputLine);
			} catch(Exception e){
				this.exportExceptionText(e);
			}
		}
		throw new Exception("Connection Close");
	}
	
	protected void onSocketData(String data){
		if(isTextStartWith(data, "admin")){
			String[] ar = data.split(" ");
			if(ar.length >= 3){
				if(SystemMainController.validMainController(ar[1], ar[2])) {
					sysController = new SystemMainController(server, this);
					response("Welcome System Admin["+ar[1]+"]!!");
				}
			}
		} else if(sysController != null && sysController.isSystemControlCommand(data)) {
			sysController.onSystemCommand(data);
		} else {
			if(RunningPlatform.isInDebugMode()) 
				log("Receive Data = " + data + "[From:" + this.clientIP+",Port="+server.port()+"]",Module.MIDDLE_WARE);
			if(compareValue("exit", data) || compareValue("quit", data)){
				try {
					socket.close();
				} catch (IOException e) {
					except("Servant Close Client Connection Fail,Error="+e.getMessage(), getClass().getName());
					exportExceptionText(e);
				}
				stopRunner();
			} else {
				try{
					onClientData(data);
				} catch(Exception e){
					this.exportExceptionText(e);
				}
			}
		}
	}
	
	protected void onClientData(String data){
		server.dispatchDataToListener(data,this);
		server.onClientData(data,this);
	}

	public String getClientIP() {
		return clientIP;
	}
	
	@Override
	public void timerStartCounting() {
		response("Server Will Shutdown after 5 sec.");
	}

	@Override
	public void timerCounting(int nowCount) {
		response("Server Will Shutdown after " + (5 - nowCount) + " sec.");
	}

	@Override
	public void timerEnd() {
		server.remoteShutDownSystem();
	}
	
	public void bindCustomer(NetServeCustomer serveCustomer){
		this.serveCustomer = serveCustomer;
	}
	
	public NetServeCustomer serveCustomer(){
		return serveCustomer;
	}

	public String getServiceEncode() {
		return serviceEncode;
	}
}
