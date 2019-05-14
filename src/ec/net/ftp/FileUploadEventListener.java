package ec.net.ftp;

import java.io.File;

import ec.net.ftp.UploadRequest;

public interface FileUploadEventListener {

	public void onStartHandleRequest(UploadRequest upRequest);
	public void onUploadSuccess(UploadRequest upRequest);
	public void onUploadFailEvent(Object evtType,UploadRequest upRequest);
}
