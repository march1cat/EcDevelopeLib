package ec.net.httpserver;

import java.util.HashMap;
import java.util.Map;

import ec.system.Basis;

public class EcHttpSession extends Basis{

	private String sessionID = null;
	private boolean isClientSettled = false;
	private Map<String,String> datas = null;
	private boolean isLoginUser = false;
	
	public EcHttpSession(String sessionID){
		this.sessionID = sessionID;
	}

	public String getSessionID() {
		return sessionID;
	}

	public boolean isClientSettled() {
		return isClientSettled;
	}

	public void setClientSettled() {
		this.isClientSettled = true;
	}
	
	public void setSessionData(String key,String value){
		if(datas == null) datas = new HashMap<>();
		datas.put(key, value);
	}
	public String getSessionData(String key){
		return datas == null ? null : datas.get(key);
	}
	
	public boolean isSessionDataSettled(String key){
		return getSessionData(key) != null;
	}

	public boolean isLogin() {
		return isLoginUser;
	}

	public void login() {
		this.isLoginUser = true;
	}
	
	public void logout() {
		this.isLoginUser = false;
	}
	
	
	
}
