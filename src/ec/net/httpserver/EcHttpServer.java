package ec.net.httpserver;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.file.EcDirector;
import ec.file.FileManager;
import ec.net.socketserver.NetConnectionServant;
import ec.net.socketserver.ServerService;
import ec.system.DeveloperMode;

public abstract class EcHttpServer extends ServerService{

	private Map<NetConnectionServant,HttpClientRequest> clientMp = null;
	private Map<String,String> htmlCach = null;
	private boolean useCach = true;
	private ClientRequestControlCenter reqController = null;
	private ClassServiceEngine clsServiceEngine = null;
	private String faviconUri = null;
	private EcDirector webResourceDirectory = null;
	private SessionController sessionController = null;
	private List<String> allowAccessIPs = null;
	private String indexFileName = "index.html";
	
	public EcHttpServer(int servicePort, String serviceName) {
		super(servicePort, serviceName);
		this.embedClassServiceEngine(new ClassServiceEngine(this));
		sessionController = new SessionController();
	}
	
	public EcHttpServer(int servicePort, String serviceName,boolean useCach) {
		super(servicePort, serviceName);
		this.embedClassServiceEngine(new ClassServiceEngine(this));
		sessionController = new SessionController();
		this.useCach = useCach;
	}

	@Override
	protected void onClientData(String data, NetConnectionServant servant) {
		if(clientMp == null) clientMp = new HashMap<NetConnectionServant, HttpClientRequest>();
			HttpClientRequest cachRequest =  clientMp.get(servant);
			if(cachRequest == null){
				if(data.toUpperCase().startsWith("GET".toUpperCase())) {
					log("Http Server Initial Http Client [GET] Request For Client["+servant.getClientIP()+":"+port()+"]!!",Module.MIDDLE_WARE);
					cachRequest = new ClientGetRequest(data,(HttpClientServant)servant,sessionController);
					cachRequest.setIndexFileName(indexFileName);
					clientMp.put(servant, cachRequest);
				} else if(data.toUpperCase().startsWith("POST".toUpperCase())) {
					log("Http Server Initial Http Client [POST] Request For Client["+servant.getClientIP()+":"+port()+"]!!",Module.MIDDLE_WARE);
					cachRequest = new ClientPostRequest(data,(HttpClientServant)servant,sessionController);
					clientMp.put(servant, cachRequest);
				}
			} else {
				try{
					cachRequest.setData(data);
					if(cachRequest.isFinishListen()) {
						clientMp.remove(servant);
						log("Http Server Receive Request, Query Uri = " + cachRequest.QueryURI(),Module.MIDDLE_WARE);
						if(checkIPAccess(servant.getClientIP()) && isAccessCheckOK(cachRequest)) onClientRequest(cachRequest);
						else isAccessCheckFail(cachRequest);
					}
				} catch(Exception e){
					exportExceptionText(e);
					onGenereateRequestFail(cachRequest,e);
				}
			}
	}
	
	

	@Override
	protected void onCreateHttpServant(NetConnectionServant servant) {
		HttpClientServant httpServant = (HttpClientServant) servant;
		httpServant.triggerRunning();
	}

	@Override
	protected NetConnectionServant onGeneratingConnection(Socket socket) {
		return new HttpClientServant(this,socket,serviceEncode);
	}
	
	
	protected String loadHtml(String htmlFileUri){
		if(htmlCach == null) htmlCach = new HashMap<>();
		if(htmlCach.get(htmlFileUri) == null || !useCach) {
			log("Load Server Resouce " + htmlFileUri + ",Cach Mode is " + (useCach ? "On" : "Off"));
			File f = new File(htmlFileUri);
			if(!f.exists()) return null;
			String htmlText = FileManager.ReadTextInSpCode(htmlFileUri,"UTF-8",1024);
			htmlCach.put(htmlFileUri, htmlText);
			return htmlText;
		} else {
			return htmlCach.get(htmlFileUri);
		}
	}
	
	protected void clearServerCach(){
		htmlCach = null;
	}
	
	
	public void embedClassServiceEngine(ClassServiceEngine engine){
		clsServiceEngine = engine;
	}
	
	private boolean checkIPAccess(String reqIP){
		if(isListWithContent(allowAccessIPs)){
			for(String allowIP : allowAccessIPs){
				if(compareValue(reqIP, allowIP)) return true;
			}
			log("IP Block For Request From " + reqIP);
			return false;
		} else return true;
	}
	
	protected  void onClientRequest(HttpClientRequest clientRequest){
		if(clientRequest.QueryURI().indexOf("favicon") >= 0){
			try{
				if(faviconUri == null) throw new Exception();
				File fIcon = new File(faviconUri);
				if(!fIcon.exists()) throw new Exception();
				ResponseServerResurce res = new ResponseServerResurce(fIcon);
				res.setHttpHeader(Response.Header.CONTENT_TYPE, "image/png");
				clientRequest.response(res);
			} catch(Exception e){
				clientRequest.response404NotFound();
			}
			clientRequest.closeClientConnection();
		} else {
			if(clsServiceEngine != null && clsServiceEngine.isClassServiceRequest(clientRequest.QueryURI())){
				if(reqController == null) {
					reqController = new ClientRequestControlCenter(this,clsServiceEngine);
					reqController.startRunner();
				}
				reqController.addQueneObject(clientRequest);
			} else {
				if (clientRequest.isGetMethod()) {
					if(webResourceDirectory != null) {
						try {
							responseWebResource((ClientGetRequest) clientRequest);
						} catch(Exception e){
							onClientGetRequest((ClientGetRequest) clientRequest);
						}
					} else {
						onClientGetRequest((ClientGetRequest) clientRequest);
					}
				}
				else if(clientRequest.isPostMethod()) {
					onClientPostRequest((ClientPostRequest) clientRequest);
				}
				clientRequest.closeClientConnection();
			}
		}
	}
	
	public void setWebFavicon(String faviconUri){
		this.faviconUri = faviconUri;
	}
	
	
	public void setWebResource(EcDirector webResourceDirectory){
		this.webResourceDirectory = webResourceDirectory;
	}
	
	private void responseWebResource(ClientGetRequest req){
		String uri = req.QueryURI();
		if(uri.endsWith("png") || uri.endsWith("gif") || uri.endsWith("jpg")){
			File f = new File(webResourceDirectory.FileUri(uri));
			try {
				if(f.exists()) {
					ResponseServerResurce res = new ResponseServerResurce(f);
					if(uri.endsWith("png")) res.setHttpHeader(Response.Header.CONTENT_TYPE, "image/png");
					if(uri.endsWith("jpg")) res.setHttpHeader(Response.Header.CONTENT_TYPE, "image/jpg");
					if(uri.endsWith("gif")) res.setHttpHeader(Response.Header.CONTENT_TYPE, "image/gif");
					req.response(res);
				} else throw new Exception("");
			} catch (Exception e) {
				if (DeveloperMode.isON()) this.exportExceptionText(e);
				ResponseNoResource res = new ResponseNoResource();
				req.response(res);
			}
		} else {
			String pageText = loadHtml(webResourceDirectory.FileUri(uri));
			req.response(pageText);
		}
	}
	protected void addAllowAccessIP(String ip){
		if(allowAccessIPs == null) allowAccessIPs = new ArrayList<>();
		allowAccessIPs.add(ip);
	}
	
	protected void onClientGetRequest(ClientGetRequest req){
		//TODO
	}
	
	protected void onClientPostRequest(ClientPostRequest req){
		//TODO
	}
	protected boolean isAccessCheckOK(HttpClientRequest clientRequest){
		//TODO
		return true;
	}
	protected void isAccessCheckFail(HttpClientRequest clientRequest){
		ResponseAccessDeny res = new ResponseAccessDeny();
		clientRequest.response(res);
	}
	protected abstract void onGenereateRequestFail(HttpClientRequest clientRequest,Exception e);

	public void setIndexFileName(String indexFileName) {
		this.indexFileName = indexFileName;
	}

	public ClassServiceEngine getClsServiceEngine() {
		return clsServiceEngine;
	}

	
	
	
}