package ec.net.httpserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ec.system.Basis;


public class Response extends Basis{
	public enum Header {
		CACH_CONTROL,CONTENT_TYPE,SERVER,CONTENT_LENGTH
	}
	
	private Map headerMp = null;
	protected byte[] contBytAr = null;
	
	public Response(String content){
		headerMp = new HashMap<>();
		contBytAr = content.getBytes();
		setDefaultHeaderValue("UTF-8");
	}
	public Response(String content,String resEncode){
		headerMp = new HashMap<>();
		contBytAr = content.getBytes();
		setDefaultHeaderValue(resEncode);
	}
	
	private void setDefaultHeaderValue(String resEnCode){
		headerMp.put(Header.CACH_CONTROL, "private");
		headerMp.put(Header.CONTENT_TYPE, "text/html; charset=" + resEnCode);
		headerMp.put(Header.SERVER, "Microsoft-IIS/10.0");
		if(contBytAr != null) headerMp.put(Header.CONTENT_LENGTH, contBytAr.length);
	}
	
	public void setHttpHeader(Object headerKey,String value){
		headerMp.put(headerKey, value);
	}
	
	protected String iniHeader(){
		return "HTTP/1.1 200 OK\r\n";
	}
	
	public String getHeader(){
		String headerStr = iniHeader();
		Iterator<Object> iter =  headerMp.keySet().iterator();
		while(iter.hasNext()){
			Object o = iter.next();
			if(o == Header.CACH_CONTROL){
				if(headerMp.get(Header.CACH_CONTROL) != null ) headerStr += "Cache-Control: " + headerMp.get(Header.CACH_CONTROL) + "\r\n";
			} else if(o == Header.CONTENT_TYPE){
				if(headerMp.get(Header.CONTENT_TYPE) != null ) headerStr += "Content-Type: " + headerMp.get(Header.CONTENT_TYPE) + "\r\n";
			} else if(o == Header.SERVER){
				if(headerMp.get(Header.SERVER) != null ) headerStr += "Server: " + headerMp.get(Header.SERVER) + "\r\n";
			} else if(o == Header.CONTENT_LENGTH){
				if(headerMp.get(Header.CONTENT_LENGTH) != null ) headerStr += "Content-Length: " + headerMp.get(Header.CONTENT_LENGTH) + "\r\n";
				else headerStr += "Content-Length: 0\r\n";
			} else {
				headerStr +=  o + ": " + headerMp.get(o) + "\r\n";
			}
		}
		return headerStr + "\r\n";
	}
	
	public byte[] getToResponseData(){
		String end = "";
		if(contBytAr == null || contBytAr.length <= 0) return getHeader().getBytes();
		byte[] rv = new byte[ getHeader().getBytes().length + end.getBytes().length + contBytAr.length];
		System.arraycopy(getHeader().getBytes(), 0, rv, 0, getHeader().getBytes().length);
		System.arraycopy(contBytAr, 0, rv, getHeader().getBytes().length , contBytAr.length);
		System.arraycopy(end.getBytes(), 0, rv, getHeader().getBytes().length + contBytAr.length, end.getBytes().length);
		return rv;
	}
	
}
