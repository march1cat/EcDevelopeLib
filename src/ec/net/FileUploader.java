package ec.net;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ec.system.controller.QueneDataController;

public abstract class FileUploader extends QueneDataController{

	private List<FileUploadEventListener> uploadEventListeners = null;
	
	@Override
	protected void process(Object data) {
		File uploadFile = (File) data;
		uploadProcess(uploadFile);
	}
	
	@Override
	protected boolean validateNewDataInQuene(Object file) {
		return file instanceof File && ((File) file).exists();
	}
	
	public void registerEventListener(FileUploadEventListener evtListener){
		if(uploadEventListeners == null) uploadEventListeners = new ArrayList<>();
		uploadEventListeners.add(evtListener);
	}
	
	protected void onUploadComplete(File uploadFile){
		if(uploadEventListeners != null){
			for(FileUploadEventListener evtListener : uploadEventListeners){
				evtListener.onUploadSuccess(uploadFile);
			}
		}
	}
	
	protected void onUploadFail(Object evtType,File uploadFile){
		if(uploadEventListeners != null){
			for(FileUploadEventListener evtListener : uploadEventListeners){
				evtListener.onUploadFailEvent(evtType, uploadFile);
			}
		}
	}
	
	protected abstract void uploadProcess(File uploadFile);

}
