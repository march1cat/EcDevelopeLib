package ec.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OsCommandConsoleReporter extends Runner {

	public enum MessageType {
		STD_OUT,ERR_OUT
	}
	
	private Object messageType = null;
	private BufferedReader br = null;
	private OsCommandInvoker invoker = null;
	
	public OsCommandConsoleReporter(BufferedReader br,OsCommandInvoker invoker,Object messageType){
		this.br = br;
		this.invoker = invoker;
		this.messageType = messageType;
	}
	
	@Override
	protected void beforeRunning() {
		log("Start OsCommand StdOutputer("+messageType.toString()+")");
	}


	@Override
	protected void running() {
		String text = null;
    	try {
			while ((text = br.readLine()) != null) {
				if(messageType == MessageType.STD_OUT) invoker.onCommadResponse(text);
				else if(messageType == MessageType.ERR_OUT) invoker.onCommadErrResponse(text);
			}
		} catch (IOException e) {
			invoker.onError(e);
		}
	}

	@Override
	protected void end() {
		log("Close OsCommand StdOutputer("+messageType.toString()+")");
	}
	
}
