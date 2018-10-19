package ec.net.httpclient;

public class RequestSOAP extends Request {

	private String functionName;
	
	public RequestSOAP(String url,String functionName){
		super(url);
		this.functionName = functionName;
	}
	public RequestSOAP(String url,int port,String functionName){
		super(url,port);
		this.functionName = functionName;
	}
	public RequestSOAP(String url,int port,String functionName,boolean isSecure){
		super(url,port,isSecure);
		this.functionName = functionName;
	}
	
	protected void customizedHeader(){
		this.setHttpHeader(HEADER.USER_AGENT, "Apache-HttpClient/4.1.1 (java 1.5)");
		this.setHttpHeader(HEADER.CONTENT_TYPE, "text/xml;charset=UTF-8");
	}
	
	public String getRequestHeader(){
		String headerStr = REQUEST_POST + " /" + webQueryPath + " HTTP/1.1\r\n";
		headerStr += "Host: "+ HOST  + (port == 80 ? "" : ":" + port) + "\r\n";
		headerStr += "Connection: " + headerMap.get(HEADER.CONNECTION) + "\r\n";
		headerStr += "User-Agent: " + headerMap.get(HEADER.USER_AGENT) + "\r\n";
		headerStr += "SOAPAction: \"http://tempuri.org/"+functionName+"\"\r\n";
		headerStr += "Accept-Encoding: " + headerMap.get(HEADER.ACCEPT_ENCODING) + "\r\n";
		headerStr += "Content-Length: " + headerMap.get(HEADER.CONTENT_LENGTH)  + "\r\n";
		headerStr += "Content-Type: " + headerMap.get(HEADER.CONTENT_TYPE) + "\r\n";
		headerStr += "\r\n";
		return headerStr;
	}
	
	public static String parsingContent(String sopaRes){
		if(sopaRes != null && !"".equals(sopaRes)){
			if(sopaRes.indexOf("<?xml") >= 0){
				return sopaRes.substring(sopaRes.indexOf("<?xml"), sopaRes.length());
			} else return "";
		} else return "";
	}
}
