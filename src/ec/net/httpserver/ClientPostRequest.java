package ec.net.httpserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.string.StringManager;

public class ClientPostRequest extends HttpClientRequest{

	
	private boolean requestDataIsEnd = false;
	
	
	private HttpAttached attached = null;
	
	
	
	public ClientPostRequest(String methodDefText, HttpClientServant servant,SessionController sessionController) {
		super(methodDefText, servant,sessionController);
	}

	@Override
	protected String toQueryUri(String methodDefText) {
		//POST /UploadExcludeList HTTP/1.1
		String[] ar = methodDefText.split(" ");
		if(ar.length == 3){
			return ar[1];
		} else return null;
	}

	@Override
	public void setData(String requestData) {
		if(!requestDataIsEnd){
			if(compareValue("",requestData)){
				requestDataIsEnd = true;
				if(attached == null){
					if(headers() != null && headers().get("Content-Length") != null){
						try{
							int postContentSize = Integer.parseInt(headers().get("Content-Length"));
							if(postContentSize <= 0){
								markFinishListen(); 
							} else {
								getClientServant().switchReadLineModeOff(postContentSize);
							}
						} catch(Exception e){
							this.exportExceptionText(e);
							markFinishListen(); 
						}
					} else {
						markFinishListen(); 
					}
				}
			} else {
				String[] ar = requestData.split(":");
				if(ar.length >= 2){
					assignHeader(ar[0].trim(), ar[1].trim());
					if(compareValue("Content-Type", ar[0])){
						if(ar[1].indexOf("boundary") >= 0){
							attached = new HttpAttached(ar[1].substring(ar[1].indexOf("=") + 1));
							log("Notice One Attached File Client Post Request!!,Attached File Ref Code = " + attached.getRefCode());
						}
					}
				} else log("Receiving Unknown Format Header Value, Data = " + requestData);
			}
		} else {
			if(attached != null) {
				onAttachedFileWay(requestData);
				if(compareValue(requestData, "--" + attached.getRefCode() + "--")) markFinishListen();
			} else {
				if(!compareValue("", requestData)){
					onPureTextWay(requestData);
					markFinishListen();
				} 
			}
		}
	}
	private void onPureTextWay(String requestData){
		if(parameters == null) parameters = new HashMap<String, String>();
		//ExcludeList=ytuytu&ExcludeListA=ytj
		if(requestData.length() > 0){
			String[] ar = requestData.split("&");
			for(String i : ar){
				String[] iar = i.split("=");
				if(iar.length == 2) parameters.put(iar[0], iar[1]);
			}
		}
	}
	
	private void onAttachedFileWay(String requestData){		
		if(attached.nowCachParameterName() != null){
			if(!compareValue("", requestData)){
				attached.setParameter(requestData);
			}
		} else if(attached.isSettingAttachedFile()){
			if(compareValue(requestData, "--" + attached.getRefCode() + "--") || 
					compareValue(requestData, "--" + attached.getRefCode())) attached.closeAttachedFileSetting();
			else if(!compareValue("", requestData) && requestData.indexOf("Content-Type") < 0){
				attached.fillFileContent(requestData + "\r\n");
			}
		} else {
			if(requestData.indexOf("Content-Disposition") >= 0 && requestData.indexOf("filename") < 0){
				//Content-Disposition: form-data; name="ExcludeListA"
				String name = requestData.substring(requestData.indexOf("name=\"") + "name=\"".length(), requestData.lastIndexOf("\""));
				attached.assignCachParameterKey(name);
			}
			if(!attached.isSettingAttachedFile()){
				if(requestData.indexOf("Content-Disposition") >= 0 && requestData.indexOf("filename") >= 0){
					//Content-Disposition: form-data; name="ExcludeList"; filename="testlist"
					String reg = "Content-Disposition: form-data; name=\"(.*?)\"; filename=\"(.*?)\"";
					List<List<String>> list = StringManager.RegSearch(requestData, reg, false);
					if(!list.isEmpty() && !list.get(0).isEmpty()){
						String name = list.get(0).get(0);
						String fileName = list.get(0).get(1);
						attached.generateAttachFile(name, fileName);
					}
				}
			}
		}
	}

	
	public boolean hasAttached(){
		return attached != null;
	}
	
	
	public List<AttachedFile> getAttachedFiles(){
		return hasAttached() ? attached.getAttachFiles() : null;
	}
	
	@Override
	public Map<String,String> getParameters(){
		return hasAttached() ? attached.getParameters() : parameters;
	}
	
}
