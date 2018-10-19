package ec.net.socketserver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.system.Basis;
import ec.system.DeveloperMode;

public class ServerDataListenerManager extends Basis implements ServerDataListener{

	private SocketClientRequestHandler reqHandler = null;
	private List<Class> RegisterdRequestTypes = null;
	private Map<String,Object> RegisterdRequests = null;
	
	public ServerDataListenerManager(SocketClientRequestHandler reqHandler){
		this.reqHandler = reqHandler;
	}
	
	public void addRegisterRequest(Class req){
		if(RegisterdRequestTypes == null) RegisterdRequestTypes = new ArrayList<>();
		RegisterdRequestTypes.add(req);
	}
	
	@Override
	public boolean isMatchListener(String data) {
		if(isListWithContent(RegisterdRequestTypes)) {
			for(Class reqType : RegisterdRequestTypes){
				if(isDataMatchAutoDispatchClassType(reqType,data)) return true;
			}
		} 
		return false;
	}
	
	private boolean isDataMatchAutoDispatchClassType(Class cls,String data){
		if(RegisterdRequests == null) RegisterdRequests = new HashMap<>();
		String clsKey = cls.getPackage() + "." + cls.getName();
		Object o = RegisterdRequests.get(clsKey);
		if(o == null) {
			try {
				Constructor c = cls.getDeclaredConstructor(NetConnectionServant.class,String.class);
				o = c.newInstance(null,"ManagerCheckGenereated");
				RegisterdRequests.put(clsKey, o);
			} catch (Exception e) {
				this.exportExceptionText(e);
			}
		}
		if(o != null && o instanceof AutoDispatchStClientRequest) {
			try {
				if((boolean) cls.getMethod("isMatchDefined",String.class).invoke(o, data)) return true;
			} catch (Exception e) {
				this.exportExceptionText(e);
			}
		}
		return false;
	}
	
	

	@Override
	public void onListeningData(String data, NetConnectionServant servant) {
		if(isListWithContent(RegisterdRequestTypes)) {
			for(Class reqType : RegisterdRequestTypes){
				if(isDataMatchAutoDispatchClassType(reqType,data)){
					try {
						Constructor c = reqType.getDeclaredConstructor(NetConnectionServant.class,String.class);
						reqHandler.addQueneObject(c.newInstance(servant,data));
						if(DeveloperMode.isON()) log("Receive Client Request["+reqType.getName()+"]");
					} catch (Exception e) {
						this.exportExceptionText(e);
					}
				}
			}
		}
	}
}
