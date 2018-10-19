package ec.net.httpserver;

import ec.file.EcDirector;
import ec.file.FileManager;
import ec.system.Basis;

public class AttachedFile extends Basis{

	private String fileName = null;
	private String httpRefName = null;
	private StringBuffer content = null;
	
	private boolean isEndLoad = false;
	
	public AttachedFile(String httpRefName,String fileName){
		this.httpRefName = httpRefName;
		this.fileName = fileName;
	}
	
	public void markFileLoadOver(){
		isEndLoad = true;
	}
	
	public boolean isLoadOver(){
		return isEndLoad;
	}
	
	
	public void appendContent(String contentText){
		if(content == null) content = new StringBuffer("");
		content.append(contentText);
	}
	
	public String getFileContent(){
		return content != null ? this.trimEndChar(content.toString(), "\r\n") : null;
	}
	
	
	
	public String getFileName() {
		return fileName;
	}

	public void save(EcDirector saveDir){
		try {
			FileManager.FileWrite(saveDir.Uri() + fileName, getFileContent() != null ? getFileContent() : "");
		} catch (Exception e) {
			this.exportExceptionText(e);
		}
	}
}
