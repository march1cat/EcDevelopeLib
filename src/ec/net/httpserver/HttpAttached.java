package ec.net.httpserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpAttached {

	private String attachedFileRefCode = null;
	private Map<String,String> attachParameter = null;
	private List<AttachedFile> attachFiles = null;
	
	private String cachParameterKey = null;
	
	
	public HttpAttached(String attachedFileRefCode){
		this.attachedFileRefCode = attachedFileRefCode;
	}
	
	
	public String getRefCode(){
		return attachedFileRefCode;
	}
	
	public void assignCachParameterKey(String key){
		cachParameterKey = key;
	}
	
	public String nowCachParameterName(){
		return cachParameterKey;
	}
	
	public void setParameter(String para){
		if(attachParameter == null) attachParameter = new HashMap<String, String>();
		attachParameter.put(cachParameterKey, para);
		cachParameterKey = null;
	}

	public Map<String,String> getParameters(){
		return attachParameter;
	}
	
	
	public void generateAttachFile(String httpRefName,String fileName){
		if(attachFiles == null) attachFiles = new ArrayList<>();
		attachFiles.add(new AttachedFile(httpRefName,fileName));
	}
	
	public void fillFileContent(String contentText){
		if(isSettingAttachedFile())  attachFiles.get(attachFiles.size() - 1).appendContent(contentText);
	}
	
	
	public boolean isSettingAttachedFile(){
		if(attachFiles == null || attachFiles.isEmpty()) return false;
		else {
			return !attachFiles.get(attachFiles.size() - 1).isLoadOver();
		}
	}
	
	
	public void closeAttachedFileSetting(){
		if(isSettingAttachedFile()) attachFiles.get(attachFiles.size() - 1).markFileLoadOver();
	}


	public List<AttachedFile> getAttachFiles() {
		return attachFiles;
	}
	
	
	
	
}
