package ec.net.execute;

import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Iterator;

public class WebServiceFactory extends WebQueryFactory {

	public HashMap<String,String> requestHeaderMap = new HashMap<String, String>();
	
	public static final String HTTP_METHOD_POST = "POST";
	public static final String HTTP_METHOD_GET = "GET";
	
	public WebServiceFactory(String host) throws ProtocolException{
		this.setHost(host);
		setRequestMethod(HTTP_METHOD_POST);
		iniDefaultHeaderAttr();
		flushHttpHeader();
	}
	
	public void setRequestMethod(String method) throws ProtocolException{
		urlCon.setRequestMethod(method);
	}
	
	public void addHttpHeader(String key,String value){
		requestHeaderMap.put(key,value);
		iniDefaultHeaderAttr();
	}
	
	private void iniDefaultHeaderAttr(){
		requestHeaderMap.put("Content-Type", "text/xml;charset=UTF-8");
		requestHeaderMap.put("SOAPAction", "\"\"");
	}
	
	private void flushHttpHeader(){
		Iterator iter = requestHeaderMap.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next().toString();
			urlCon.setRequestProperty(key, requestHeaderMap.get(key));
		}
	}
	

}
