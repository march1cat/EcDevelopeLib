package ec.file;

import java.io.File;
import java.io.IOException;

import ec.file.FileManager;
import ec.system.Basis;
import ec.system.DeveloperMode;

public class WindowsBatchExecuter extends Basis{

	private String batchFileUri = null;
	protected boolean hasContent = false;
	
	public WindowsBatchExecuter(String batchFileUri){
		this.batchFileUri = batchFileUri;
		File btFile = new File(batchFileUri);
		if(btFile.exists()) hasContent = true;
	}
	
	public WindowsBatchExecuter(String batchFileUri,boolean doClean){
		this.batchFileUri = batchFileUri;
		if(DeveloperMode.isOFF() && doClean) cleanBatchFile();
		File btFile = new File(batchFileUri);
		if(btFile.exists()) hasContent = true;
	}
	
	public void cleanBatchFile(){
		File btFile = new File(batchFileUri);
		if(btFile.exists()) btFile.delete();
	}
	
	public void createUTF8BatchFile(){
		FileManager.bufferWrite(batchFileUri(), "chcp 65001\r\n");
	}
	
	public void addLineContent(String content){
		if(hasContent) FileManager.bufferWrite(batchFileUri(),  "\r\n" + content);
		else FileManager.bufferWrite(batchFileUri(),  content);
		hasContent = true;
	}

	public boolean isFileExist(){
		File btFile = new File(batchFileUri());
		return btFile.exists();
	}
	
	public void execute(){
		if(hasContent){
			File btFile = new File(batchFileUri());
			if(btFile.exists()){
				log("Prepare Process Batch File[" + batchFileUri() + "]");
				try {
		        	Runtime runtime = Runtime.getRuntime();
		        	Process p1 = runtime.exec("cmd /c start " + batchFileUri());
				} catch (IOException e) {
					this.exportExceptionText(e);
				}
			}
		}
	}
	
	public String batchFileUri(){
		return batchFileUri;
	}
	
}
