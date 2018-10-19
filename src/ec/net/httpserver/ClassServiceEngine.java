package ec.net.httpserver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import ec.system.Basis;

public class ClassServiceEngine extends Basis{
	
	private EcHttpServer httpServer = null;
	private String lockHandlerPackage = null;
	
	public ClassServiceEngine(EcHttpServer httpServer){
		this.httpServer = httpServer;
	}
	

	public boolean isClassServiceRequest(String queryUri){
		String[] ends = {"html","js","css","jpg","gif","png"};
		for(String end : ends){
			if(queryUri.endsWith(end)) return false;
		}
		return true;
	}
	
	public void lockHandlerPackge(String lockHandlerPackage){
		this.lockHandlerPackage = lockHandlerPackage;
	}
	
	public void callClassService(HttpClientRequest clientRequest) throws Exception{
		String str = clientRequest.QueryURI();
		if(str.startsWith("/")) str = str.substring(1);
		String method = str.substring(str.lastIndexOf(".") + 1);
		Class c = requestClass(clientRequest);
		Constructor cc = c.getDeclaredConstructor(HttpClientRequest.class,EcHttpServer.class);
		Object o = cc.newInstance(clientRequest,httpServer);
		Method m = c.getDeclaredMethod(method);
		Object k = m.invoke(o);
	}
	
	protected Class requestClass(HttpClientRequest clientRequest) throws Exception{
		String str = clientRequest.QueryURI();
		if(str.startsWith("/")) str = str.substring(1);
		String className = str.substring(0, str.lastIndexOf("."));
		Class c = lockHandlerPackage == null ? getClass().getClassLoader().loadClass(className) :
			getClass().getClassLoader().loadClass(lockHandlerPackage + "." + className);
		return c;
	}
	
}
