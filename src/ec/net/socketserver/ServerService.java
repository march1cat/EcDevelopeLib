package ec.net.socketserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ec.system.ResourceTracer;
import ec.system.Runner;
import ec.system.Timer;

public class ServerService extends Runner{

	private int servicePort = -1;
	private ServerSocket server = null;
	protected String serviceEncode = "UTF-8";
	private String serviceName = null;
	
	private List<NetConnectionServant> connectedServants = null;
	private List<ServerDataListener> dataListeners = null;
	private ServerDataListenerManager dataListenerMgr = null;
	
	private boolean SupportRemoteShutDown = false;
	
	public ServerService(int servicePort,String serviceName){
		this.servicePort = servicePort;
		this.serviceName = serviceName;
	}
	
	public ServerService(int servicePort,String serviceName,boolean SupportRemoteShutDown){
		this.servicePort = servicePort;
		this.serviceName = serviceName;
		this.SupportRemoteShutDown = SupportRemoteShutDown;
	}
	

	@Override
	protected void beforeRunning() {
		if(servicePort != -1){
			try {
				server = new ServerSocket(servicePort);
				log(serviceName + " is Staring at Port("+servicePort+")");
			} catch (IOException e) {
				except("Service Server["+servicePort+"] Listening Fail,Error="+e.getMessage(), getClass().getName());
				exportExceptionText(e);
				server = null;
			}
		}
	}

	@Override
	protected void running() {
		if(server != null){
			try {
				log(serviceName + " is waiting to built connection at Port("+servicePort+")");
				Socket socket = server.accept();
				log(serviceName + " is conected  at Port("+servicePort+")");
				NetConnectionServant servant = onGeneratingConnection(socket);
				onCreateHttpServant(servant);
			} catch (IOException e) {
				this.except("Accept Client Connection Fail,Error="+e.getMessage(), this.getClass().getName());
				this.exportExceptionText(e);
			}
		} else this.stopRunner();
	}
	
	
	protected void onCreateHttpServant(NetConnectionServant servant){
		if(connectedServants == null) connectedServants = new ArrayList<>();
		connectedServants.add(servant);
		log(serviceName + " Client Connection Amount is " + connectedServants.size());
	}
	
	protected NetConnectionServant onGeneratingConnection(Socket socket){
		return new NetConnectionServant(this,socket,this.serviceEncode);
	}

	@Override
	protected void end() {
		log(serviceName + " at Port("+servicePort+") is down!!");
	}
	
	public int port(){
		return this.servicePort;
	}
	
	protected void onClientCloseConnction(NetConnectionServant closedServant){
		connectedServants.remove(closedServant);
		log(serviceName + " Client Connection Amount is " + connectedServants.size());
	}
	
	public void announceToAllClient(String data){
		if(connectedServants != null && connectedServants.size() > 0) {
			for(NetConnectionServant servant : connectedServants){
				servant.response(data);
			}
		}
	}
	
	public void registerDataListener(ServerDataListener listener){
		if(dataListeners == null) dataListeners = new ArrayList<>();
		dataListeners.add(listener);
	}
	
	protected void dispatchDataToListener(String data,NetConnectionServant servant){
		if(dataListeners!=null && !dataListeners.isEmpty()){
			boolean isMatched = false;
			for(ServerDataListener listener : dataListeners){
				if(listener.isMatchListener(data)) {
					listener.onListeningData(data, servant);
					isMatched = true;
				}
			}
			if(!isMatched) onNoMatchListenerData(data,servant);
		}
		if(dataListenerMgr != null && dataListenerMgr.isMatchListener(data)) dataListenerMgr.onListeningData(data, servant);
	}
	
	protected void prepareShutDownSystem(NetConnectionServant servant){
		if(SupportRemoteShutDown){
			try{
				log("Service Support Remote Shutdown Module,Application Will Shut down after 5 sec!!");
				Timer timer = new Timer(servant);
				timer.startRunner();
			} catch(Exception e){
				this.except("Prepare Remote Shutdown module fail,error = " + e.getMessage(), this.getClass().getName());
				this.exportExceptionText(e);
			}
		} else {
			log("Service not support remote shutdown function!!");
			servant.response("Service Not Support Remote Shutdown Function!!");
		}
	}
	
	protected void remoteShutDownSystem(){
		if(SupportRemoteShutDown){
			try{
				if(ResourceTracer.instance() != null) ResourceTracer.instance().end();
				log("Shutdown Server by remote shutdown!!");
				threadHold(3000);
				System.exit(0);
			} catch(Exception e){
				this.except("Remote Shutdown Application fail,error = " + e.getMessage(), this.getClass().getName());
				this.exportExceptionText(e);
			}
		}
	}
	
	
	public void mountDataListenerManager(ServerDataListenerManager dataListenerMgr){
		this.dataListenerMgr = dataListenerMgr;
	}
	
	protected void onClientData(String data,NetConnectionServant servant){
		//Wait to ovveride
	}
	protected void onNoMatchListenerData(String data,NetConnectionServant servant){
		//Wait to ovveride
	}

}
