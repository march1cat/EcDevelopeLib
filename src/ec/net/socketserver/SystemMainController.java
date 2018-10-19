package ec.net.socketserver;

import ec.system.Basis;
import ec.system.ResourceTracer;
import ec.system.RunningPlatform;

public class SystemMainController extends Basis{

	private final static String SystemMainControlAccount = "edison";
	private final static String SystemMainControlPassword = "1qaz2wsx";
	
	private ServerService server = null;
	private NetConnectionServant servant = null;
	
	public SystemMainController(ServerService server,NetConnectionServant servant){
		this.server = server;
		this.servant = servant;
	}
	
	
	public boolean isSystemControlCommand(String clientData){
		final String[] commands = {"shutdown","RunningMode","ResourceTrace","CloseResourceTrace","Show","$Cmd","$Command"};
		for(String command : commands){
			if (isTextStartWith(clientData, command)) return true;
		}
		return false;
	}
	
	
	public static boolean validMainController(String account,String pwd){
		return SystemMainControlAccount.toUpperCase().equals(account.toUpperCase()) && SystemMainControlPassword.equals(pwd);
	}
	
	public void onSystemCommand(String data){
		if(isTextStartWith(data, "shutdown")){
			server.prepareShutDownSystem(servant);
		} else if(isTextStartWith(data, "RunningMode")){
			String[] ar = data.split(" ");
			if(ar.length >= 2){
				boolean isSwitch = false;
				if(compareValue(ar[1], "Debug")) {
					isSwitch = true;
					RunningPlatform.runningMode = RunningPlatform.RunningMode.DEBUG;
				} else if(compareValue(ar[1], "Production")) {
					isSwitch = true;
					RunningPlatform.runningMode = RunningPlatform.RunningMode.PRODUCTION;
				} 
				if(isSwitch) servant.response("Switch Running Mode Success,Now Running Mode["+ RunningPlatform.runningMode +"]");
				else servant.response("Switch Running Mode Fail[No Such Mode Error]!!");
			}
		} else if(isTextStartWith(data, "ResourceTrace")){
			String[] ar = data.split(" ");
			if(ar.length >= 2){
				if(ResourceTracer.instance() == null) {
					ResourceTracer tracker = ResourceTracer.initial(ar[1],ar.length >= 3 ? Long.parseLong(ar[2]) : 30);
					tracker.startRunner();
				}
				servant.response("System Recource Tracker is Starting!!");
			}
		} else if(isTextStartWith(data, "CloseResourceTrace")){
			ResourceTracer tracker = ResourceTracer.instance();
			if(tracker != null){
				tracker.stopRunner();
				servant.response("Close System Recource Tracker Success!!");
			} else servant.response("System Recource Tracker is not Running!!");
		} else if(isTextStartWith(data, "Show")){
			String[] ar = data.split(" ");
			if(ar.length >= 2){
				if(compareValue(ar[1], "commands")){
					StringBuffer cmdDesc = new StringBuffer("");
					cmdDesc.append("1.ShutDown\r\n");
					cmdDesc.append("2.RunningMode $Mode[Production,Debug]\r\n");
					cmdDesc.append("3.ResourceTrace $RecordFileUri $RecordDelayTime(Option)\r\n");
					cmdDesc.append("4.CloseResourceTrace");
					servant.response(cmdDesc.toString());
				}
			}
		} else if(isTextStartWith(data, "$Cmd") || isTextStartWith(data, "$Command")){
			String[] ar = data.split(" ");
			String commandIndex = null;
			if(isTextStartWith(data, "$Cmd")) commandIndex = ar[0].substring("$Cmd".length());
			if(isTextStartWith(data, "$Command")) commandIndex = ar[0].substring("$Command".length());
			try{
				String cmdHeader = null;
				int icmdIndex = Integer.parseInt(commandIndex);
				switch(icmdIndex){
					case 1:
						cmdHeader = "ShutDown";
						break;
					case 2:
						cmdHeader = "RunningMode";
						break;
					case 3:
						cmdHeader = "ResourceTrace";
						break;
					case 4:
						cmdHeader = "CloseResourceTrace";
						break;
				}
				if(cmdHeader != null) {
					StringBuffer parameter = new StringBuffer();
					for(int i = 1;i < ar.length;i++){
						parameter.append(ar[i] + ((i == ar.length - 1) ? "" : " "));
					}
					onSystemCommand(cmdHeader + " " + parameter.toString());
				} else servant.response("Not Found Defined Quick Command Link!!");
			} catch(Exception e){ 
				if(RunningPlatform.isInDebugMode()) this.exportExceptionText(e);
			}
		} else servant.response("Not Defined Amin Command[" + data + "]");
	}
	
}
