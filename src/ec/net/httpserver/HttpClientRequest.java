package ec.net.httpserver;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ec.net.socketserver.NetConnectionServant;
import ec.system.Basis;
import ec.system.DeveloperMode;

public abstract class HttpClientRequest extends Basis{

	private boolean isFinishedReceivingReqeust = false;
	private Map<String,String> httpHeaders = null;
	private String methodDefText = null;
	private HttpClientServant servant = null;
	protected String queryUri = null;
	protected Map<String,String> parameters = null;
	private EcHttpSession session = null;
	private SessionController sessionController = null;
	protected String indexFileName = null;
	
	
	private boolean isAsyncJob = false;
	
	public HttpClientRequest(String methodDefText,HttpClientServant servant,SessionController sessionController){
		this.methodDefText = methodDefText;
		this.servant = servant;
		queryUri = toQueryUri(methodDefText);
		this.sessionController = sessionController;
	}
	
	
	protected abstract String toQueryUri(String methodDefText);
	
	
	public abstract void setData(String requestData);
	
	protected void assignHeader(String key,String value){
		if(httpHeaders == null) httpHeaders = new HashMap<String, String>();
		httpHeaders.put(key, value);
	}
	
	
	public String showHeaders(){
		if(httpHeaders != null){
			StringBuffer text = new StringBuffer(methodDefText + "\r\n");
			Iterator<String> iter = httpHeaders.keySet().iterator();
			while(iter.hasNext()){
				String key = iter.next();
				String value = httpHeaders.get(key);
				text.append(key + ":" + value + "\r\n");
			}
			return trimEndChar(text.toString(), "\r\n");
		} else return null;
	}
	
	public Map<String,String> headers(){
		return httpHeaders;
	}
	
	protected void markFinishListen(){
		isFinishedReceivingReqeust = true;
		onSessionHandleProcess();
	}
	
	public boolean isFinishListen(){
		return isFinishedReceivingReqeust;
	}
	
	private void onSessionHandleProcess(){
		//JSESSIONID=646445C3C10EC0F353C0064A49A5ECB9
		String settledSID = null;
		String cText = headers().get("Cookie");
		if(cText != null && cText.toUpperCase().startsWith("JSESSIONID=")){
			settledSID = cText.substring(cText.indexOf("JSESSIONID=") + "JSESSIONID=".length());
		}  
		if(settledSID == null || !sessionController.isSessionIDExist(settledSID)) {
		   session = sessionController.buildSession();
		} else {
		   session = sessionController.loadSession(settledSID);
		}
	}
	
	
	
	public void response(String data){
		if(servant != null){
			if(data != null) {
				Response res = new Response(data,servant.getServiceEncode());
				if(session != null && !session.isClientSettled()) {
					res.setHttpHeader("Set-Cookie", "JSESSIONID=" + session.getSessionID() + ";path=/;");
					session.setClientSettled();
				}
				if(QueryURI().endsWith("js")) res.setHttpHeader(Response.Header.CONTENT_TYPE, "text/javascript");
				if(QueryURI().endsWith("css")) res.setHttpHeader(Response.Header.CONTENT_TYPE, "text/css");
				servant.response(res);
			} else {
				ResponseNoResource res = new ResponseNoResource();
				servant.response(res);
			}
		}
	}
	
	public void response(Response res){
		if(servant != null){
			if(session != null &&  !session.isClientSettled()) {
				res.setHttpHeader("Set-Cookie", "JSESSIONID=" + session.getSessionID() + ";path=/;");
				session.setClientSettled();
			}
			if(res instanceof ResponseServerBytesResource) responseRange((ResponseServerBytesResource)res);
			else servant.response(res);
		}
	} 
	
	
	private void responseRange(ResponseServerBytesResource res){
		this.markAsyncJob();
		ResourceRangeProvider provider = new ResourceRangeProvider(this, res);
		provider.startRunner();
		
	}
	
	public void response404NotFound(){
		if(servant != null){
			ResponseNoResource res = new ResponseNoResource(servant.getServiceEncode());
			servant.response(res);
		}
	}
	
	public void closeClientConnection(){
		servant.closeClientConnection();
	}
	
	
	public boolean isPostMethod(){
		return this instanceof ClientPostRequest;
	}
	
	public boolean isGetMethod(){
		return this instanceof ClientGetRequest;
	}
	
	public String QueryURI(){
		return this.queryUri;
	}
	
	public Map<String,String> getParameters(){
		return parameters;
	}
	
	
	public HttpClientServant getClientServant(){
		return servant;
	}


	public EcHttpSession getSession() {
		return session;
	}



	public void setIndexFileName(String indexFileName) {
		this.indexFileName = indexFileName;
	}


	public boolean isAsyncJob() {
		return isAsyncJob;
	}


	public void markAsyncJob() {
		this.isAsyncJob = true;
	}

	
	
	
}
