package ec.net.httpserver;

import java.util.HashMap;

import ec.net.socketserver.NetConnectionServant;


public class ClientGetRequest extends HttpClientRequest{

	
	public ClientGetRequest(String clientMethodText,HttpClientServant servant,SessionController sessionController){
		super(clientMethodText,servant,sessionController);
	}

	@Override
	public void setData(String requestData) {
		if("".equals(requestData)){
			markFinishListen();
			if(QueryURI().indexOf("?") >= 0 && QueryURI().indexOf("?") != QueryURI().length() - 1){
				try{
					String paraText = QueryURI().substring(QueryURI().indexOf("?") + 1);
					String[] ar = paraText.split("&");
					if(ar.length > 0){
						for(String i : ar){
							String[] iar = i.split("=");
							if(iar.length == 2) {
								if(parameters == null) parameters = new HashMap<String, String>();
								parameters.put(iar[0], iar[1]);
							}
						}
					}
				} catch(Exception e){
					this.exportExceptionText(e);
				}
			}
			
			queryUri = queryUri.indexOf("?") >= 0 ? queryUri.substring(0, queryUri.indexOf("?")) : queryUri;
			if( compareValue(queryUri, "/"))  queryUri = indexFileName;
		} else {
			String[] ar = requestData.split(":");
			if(ar.length >= 2){
				assignHeader(ar[0].trim(), ar[1].trim());
			} else log("Receiving Unknown Format Header Value, Data = " + requestData);
			
		}
	}

	@Override
	protected String toQueryUri(String methodDefText) {
		//GET /test.html HTTP/1.1
		String[] ar = methodDefText.split(" ");
		if(ar.length == 3){
			return ar[1];
		} else return null;
	}
}
