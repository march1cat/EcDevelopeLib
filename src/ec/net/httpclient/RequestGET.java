package ec.net.httpclient;


public class RequestGET extends Request {
	
	public RequestGET(String url){
		super(url);
		methodType = REQUEST_GET;
	}
	public RequestGET(String url,int port){
		super(url,port);
		methodType = REQUEST_GET;
	}
	public RequestGET(String url,int port,boolean isSecure){
		super(url,port,isSecure);
		methodType = REQUEST_GET;
	}
}
