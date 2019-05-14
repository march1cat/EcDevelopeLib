package ec.net.ftp;

import java.io.File;

import ec.system.Basis;

public class UploadRequest extends Basis {

	public enum Status {
		PROCESSING,WAIT,DONE,FAIL,FAIL_RETRY
	}
	
	private String filePath = null;
	private String saveTo = null;
	private Object status = Status.WAIT;
	public UploadRequest( String filePath, String saveTo) {
		this.filePath = filePath;
		this.saveTo = saveTo;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getSaveTo() {
		return saveTo;
	}
	public void setSaveTo(String saveTo) {
		this.saveTo = saveTo;
	}
	
	public boolean isFileExist() {
		File f = new File(filePath);
		return f.exists() && !f.isDirectory();
	}
	public void markEndSuccess() {
		status = Status.DONE;
	}
	public void markFail() {
		status = Status.FAIL;
	}
	public void markFailRetry() {
		status = Status.FAIL_RETRY;
	}
	public void markProcessing() {
		status = Status.PROCESSING;
	}
	public Object getStatus() {
		return status;
	}
	
	
	
}
