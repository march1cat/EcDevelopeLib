package ec.net.httpclient;

import ec.net.httpclient.Request.HEADER;

public class RequestPost extends Request {
	
	private String postData = null;
	
	public RequestPost(String url){
		super(url);
		methodType = REQUEST_POST;
	}
	public RequestPost(String url,int port){
		super(url,port);
		methodType = REQUEST_POST;
	}
	public RequestPost(String url,int port,boolean isSecure){
		super(url,port,isSecure);
		methodType = REQUEST_POST;
	}
	@Override
	public void setAttachData(byte[] dataAr) {
		super.setAttachData(dataAr);
		this.postData = new String(dataAr);
	}
	
	public String getPostData(){
		return postData;
	}
	
}
