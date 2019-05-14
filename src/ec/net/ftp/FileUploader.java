package ec.net.ftp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ec.net.ftp.UploadRequest;
import ec.system.controller.QueneDataController;

public abstract class FileUploader extends QueneDataController{

	private List<FileUploadEventListener> uploadEventListeners = null;
	
	@Override
	protected void process(Object data) {
		UploadRequest request = (UploadRequest) data;
		request.markProcessing();
		onProcessNewRequest(request);
		uploadProcess(request);
	}
	
	@Override
	protected boolean validateNewDataInQuene(Object r) {
		return r instanceof UploadRequest && ((UploadRequest)r).isFileExist();
	}
	
	public void registerEventListener(FileUploadEventListener evtListener){
		if(uploadEventListeners == null) uploadEventListeners = new ArrayList<>();
		uploadEventListeners.add(evtListener);
	}
	private void onProcessNewRequest(UploadRequest r) {
		if(uploadEventListeners != null){
			for(FileUploadEventListener evtListener : uploadEventListeners){
				evtListener.onStartHandleRequest(r);
			}
		}
	}
	
	protected void onUploadComplete(UploadRequest r){
		if(uploadEventListeners != null){
			for(FileUploadEventListener evtListener : uploadEventListeners){
				evtListener.onUploadSuccess(r);
			}
		}
	}
	
	protected void onUploadFail(Object evtType,UploadRequest r){
		if(uploadEventListeners != null){
			for(FileUploadEventListener evtListener : uploadEventListeners){
				evtListener.onUploadFailEvent(evtType, r);
			}
		}
	}
	
	protected abstract void uploadProcess(UploadRequest request);

}
