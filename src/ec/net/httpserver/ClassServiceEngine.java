package ec.net.httpserver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import ec.net.httpserver.ecrtable.EcRenderTable;
import ec.system.Basis;

public class ClassServiceEngine extends Basis{
	
	private EcHttpServer httpServer = null;
	private String lockHandlerPackage = null;
	
	public ClassServiceEngine(EcHttpServer httpServer){
		this.httpServer = httpServer;
	}
	

	public boolean isClassServiceRequest(String queryUri){
		String[] ends = {"html","js","css","jpg","gif","png","bootstrap.min.css.map"};
		for(String end : ends){
			if(queryUri.endsWith(end)) return false;
		}
		if(queryUri.endsWith("xml") && isListWithContent(httpServer.getEcRTables())) {
			return isEcTableRenderType(queryUri) != null;
		}
		return true;
	}
	
	
	public EcRenderTable isEcTableRenderType(String queryUri){
		String str = queryUri;
		if(str.startsWith("/")) str = str.substring(1);
		for(EcRenderTable et : httpServer.getEcRTables()){
			if(et.getDefinitionFileUri().toUpperCase().endsWith(str.toUpperCase())){
				return et;
			}
		}
		return null;
	}
	
	public void lockHandlerPackge(String lockHandlerPackage){
		this.lockHandlerPackage = lockHandlerPackage;
	}
	
	public void callEcTableRenderClassService(HttpClientRequest clientRequest) throws Exception{
		EcRenderTable ect = isEcTableRenderType(clientRequest.QueryURI());
		if(ect != null && ect.getBindingClass() != null) {
			String method = "queryData";
			if(clientRequest.getParameters() != null) {
				if(compareValue("querySchema", clientRequest.getParameters().get("action")))
				method = "querySchema";
			}
			Class c = getClass().getClassLoader().loadClass(ect.getBindingClass());
			Constructor cc = c.getDeclaredConstructor(HttpClientRequest.class,EcHttpServer.class);
			Object o = cc.newInstance(clientRequest,httpServer);
			
			if(o instanceof TableViewHandler) {
				TableViewHandler v = (TableViewHandler) o;
				Method[] ms = c.getMethods();
				if(compareValue("queryData", method)) {
					EcRenderTable r = (EcRenderTable) ect.clone();
					v.setBindDefEcTable(r);
					boolean isPassingParamterSuccess = r.parsingQueryCondition(clientRequest);
					if(!isPassingParamterSuccess) method = "onParamResovleFail";
				} else {
					v.setBindDefEcTable(ect);
				}
				for(Method m : ms){
					if(compareValue(m.getName(), method)){
						Object k = m.invoke(o);
						break;
					}
				}
			}
		}
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
