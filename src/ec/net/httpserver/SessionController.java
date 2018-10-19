package ec.net.httpserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.string.StringManager;
import ec.system.Basis;
import ec.system.DeveloperMode;

public class SessionController extends Basis{

	private int k = 0;
	private List<EcHttpSession> sessions = null;

	
	public SessionController(){
		if(sessions == null) sessions = new ArrayList<>();
	}
	
	public EcHttpSession buildSession(){
		EcHttpSession session = new EcHttpSession(buildSessionID());
		sessions.add(session);
		return session;
	}
	
	private String buildSessionID(){
		String sid = StringManager.md5(StringManager.getSystemDate("yyyyMMddHHmmSS") + getSSerno());
		if(DeveloperMode.isON()) log("Build Session ID = " + sid);
		return sid;
	}
	
	private synchronized String getSSerno(){
		return ("000000" + (++k)).substring("000000".length() + 1 - 5);
	}
	
	public boolean isSessionIDExist(String sessionID){
		if(isListWithContent(sessions)) {
			for(EcHttpSession session : sessions){
				if(session != null && compareValue(session.getSessionID(), sessionID)) return true;
			}
		}
		return false;
	}
	
	public EcHttpSession loadSession(String sessionID){
		if(isListWithContent(sessions)) {
			for(EcHttpSession session : sessions){
				if(session != null && compareValue(session.getSessionID(), sessionID)) return session;
			}
		}
		return null;
	}
	
}
