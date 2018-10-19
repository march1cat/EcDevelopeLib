package ec.net;

import java.io.File;

public interface FileUploadEventListener {

	public void onUploadSuccess(File uploadTargetFile);
	public void onUploadFailEvent(Object evtType,File uploadTargetFile);
}
