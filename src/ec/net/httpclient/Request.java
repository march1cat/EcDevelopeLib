package ec.net.httpclient;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Request {
	public static final String REQUEST_GET = "GET";
	public static final String REQUEST_POST = "POST";
	public static final String REQUEST_PUT = "PUT";
	public static final String ENCODE_BIG5 = "Big5";
	public static final String ENCODE_UTF8 = "UTF-8";
	
	private boolean isSecure = false;
	
	protected Map headerMap;
	protected String methodType = REQUEST_GET;
	public enum HEADER{
		CONNECTION,CACHE_CONTROL,ACCEPT,
		USER_AGENT,ACCEPT_ENCODING,ACCEPT_LANGUAGE,
		CONTENT_TYPE,CONTENT_LENGTH
	}
	
	public String HOST;
	public int port;
	public String webQueryPath;
	
	protected byte[] contBytAr = null;
	
	public Request(String HOST){
		this.HOST = HOST;
		this.port = 80;
		initialDefaultHeader();
	}
	public Request(String HOST,int port){
		this.HOST = HOST;
		this.port = port;
		initialDefaultHeader();
	}
	public Request(String HOST,int port,boolean isSecure){
		this.HOST = HOST;
		this.port = port;
		this.isSecure = isSecure;
		initialDefaultHeader();
	}
	
	private void initialDefaultHeader(){
		headerMap = new HashMap();
		headerMap.put(HEADER.CONNECTION, "Close");
		headerMap.put(HEADER.CACHE_CONTROL, "max-age=0");
		headerMap.put(HEADER.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		headerMap.put(HEADER.USER_AGENT, "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36");
		headerMap.put(HEADER.ACCEPT_ENCODING, "deflate,sdch");
		headerMap.put(HEADER.ACCEPT_LANGUAGE, "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4");
		headerMap.put(HEADER.CONTENT_TYPE, "application/x-www-form-urlencoded");
		headerMap.put(HEADER.CONTENT_LENGTH, 0);
		customizedHeader();
	}
	
	public void setHttpHeader(Object headerKey,String value){
		headerMap.put(headerKey, value);
	}
	public void setAttachData(byte[] dataAr){
		headerMap.put(HEADER.CONTENT_LENGTH, dataAr.length);
		this.contBytAr = dataAr;
	}
	public String getRequestHeader(){
		String headerStr = methodType + " /" + (webQueryPath.startsWith("/") ? webQueryPath.substring(1) : webQueryPath) + " HTTP/1.1\r\n";
		headerStr += "Host: "+ HOST  + (port == 80 ? "" : ":" + port) + "\r\n";
		Iterator<Object> iter =  headerMap.keySet().iterator();
		while(iter.hasNext()){
			Object o = iter.next();
			if(o == HEADER.CONNECTION){
				if(headerMap.get(HEADER.CONNECTION) != null ) headerStr += "Connection: " + headerMap.get(HEADER.CONNECTION) + "\r\n";
			} else if(o == HEADER.CACHE_CONTROL){
				if(headerMap.get(HEADER.CACHE_CONTROL) != null ) headerStr += "Cache-Control: " + headerMap.get(HEADER.CACHE_CONTROL) + "\r\n";
			} else if(o == HEADER.ACCEPT){
				if(headerMap.get(HEADER.ACCEPT) != null ) headerStr += "Accept: " + headerMap.get(HEADER.ACCEPT) + "\r\n";
			} else if(o == HEADER.USER_AGENT){
				if(headerMap.get(HEADER.USER_AGENT) != null ) headerStr += "User-Agent: " + headerMap.get(HEADER.USER_AGENT) + "\r\n";
			} else if(o == HEADER.ACCEPT_ENCODING){
				if(headerMap.get(HEADER.ACCEPT_ENCODING) != null ) headerStr += "Accept-Encoding: " + headerMap.get(HEADER.ACCEPT_ENCODING) + "\r\n";
			} else if(o == HEADER.ACCEPT_LANGUAGE){
				if(headerMap.get(HEADER.ACCEPT_LANGUAGE) != null ) headerStr += "Accept-Language: " + headerMap.get(HEADER.ACCEPT_LANGUAGE) + "\r\n";
			} else if(o == HEADER.CONTENT_LENGTH || o == HEADER.CONTENT_TYPE){
				if(this instanceof RequestPost && o == HEADER.CONTENT_LENGTH){
					headerStr += "Content-Length: " + headerMap.get(HEADER.CONTENT_LENGTH) + "\r\n";
				} else if(this instanceof RequestPost && o == HEADER.CONTENT_TYPE){
					headerStr += "Content-Type: " + headerMap.get(HEADER.CONTENT_TYPE) + "\r\n";
				}
			} else {
				headerStr += o.toString() + ": " + headerMap.get(o.toString()) + "\r\n";
			}
		}
		headerStr += "\r\n";
		return headerStr;
	}
	public byte[] getToSendData(){
		String end = "";
		if(contBytAr == null || contBytAr.length <= 0) return getRequestHeader().getBytes();
		byte[] rv = new byte[ getRequestHeader().getBytes().length + end.getBytes().length + contBytAr.length];
		System.arraycopy(getRequestHeader().getBytes(), 0, rv, 0, getRequestHeader().getBytes().length);
		System.arraycopy(contBytAr, 0, rv, getRequestHeader().getBytes().length , contBytAr.length);
		System.arraycopy(end.getBytes(), 0, rv, getRequestHeader().getBytes().length + contBytAr.length, end.getBytes().length);
		return rv;
	}
	
	protected void customizedHeader(){
		//wait to override
	}
	
	
	public boolean isSecureTypeRequest(){
		return this.isSecure;
	}
}
