package ec.net.socketserver;

import ec.system.controller.QueneDataController;

public class SocketClientRequestHandler extends QueneDataController{

	@Override
	protected boolean validateNewDataInQuene(Object dataWillInQuene) {
		return  dataWillInQuene instanceof SocketClientRequest;
	}

	@Override
	protected void process(Object dataInQuene) {
		SocketClientRequest sClientReq = (SocketClientRequest) dataInQuene;
		try{
			Object resData = sClientReq.process();
			if(resData != null) sClientReq.responseToClient(resData);
			else log("Res Data is Null,Skip Response Phase!!");
		} catch(Exception e){
			exportExceptionText(e);
			sClientReq.responseToClient(sClientReq.processFail());
		}
	}

}
