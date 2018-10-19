package ec.net.ftp;

import java.io.File;
import java.io.IOException;

import com.enterprisedt.net.ftp.FTPException;
import ec.net.FileUploader;

public class FTPUploader extends FileUploader{
	
	public enum UploadEvent{
		UPLOAD_FAIL
	}
	
	
	private String ftpHost = null;
	private String ftpAcc = null;
	private String ftpPass = null;
	private String remoteSaveUri = null;
	private boolean autoReUploadWhileFail = true;
	
	private FTPManager ftp = null;
	
	public FTPUploader(String ftpHost,String ftpAcc,String ftpPass,String remoteSaveUri){
		this.ftpHost = ftpHost;
		this.ftpAcc = ftpAcc;
		this.ftpPass = ftpPass;
		this.remoteSaveUri = remoteSaveUri;
	}
	
	
	public FTPUploader(String ftpHost,String ftpAcc,String ftpPass,String remoteSaveUri,boolean autoReUploadWhileFail){
		this.ftpHost = ftpHost;
		this.ftpAcc = ftpAcc;
		this.ftpPass = ftpPass;
		this.remoteSaveUri = remoteSaveUri;
		this.autoReUploadWhileFail = autoReUploadWhileFail;
	}
	
	@Override
	protected void uploadProcess(File uploadFile) {
		if(ftp == null)  ftp = new FTPManager(ftpHost, ftpAcc, ftpPass);
		log("Upload File To FTP Server["+ftpHost+"],File Uri = " + 
				uploadFile.getAbsolutePath() +  ", Remote File Uri = " + remoteSaveUri + "/" + uploadFile.getName());
		try {
			ftp.connect();
			ftp.uploadFile(uploadFile.getAbsolutePath(), remoteSaveUri, uploadFile.getName());
			ftp.LogoutFTPServer();
			onUploadComplete(uploadFile);
			log("Upload File Finish["+uploadFile.getAbsolutePath()+"]!!");
		} catch (IOException e) {
			this.except("Upload File[" + uploadFile.getAbsolutePath() + "] Fail,Retry Upload Later,Error = " + e.getMessage(), this.getClass().getName());
			this.exportExceptionText(e);
			if(autoReUploadWhileFail) this.addQueneObject(uploadFile);
			onUploadFail(UploadEvent.UPLOAD_FAIL,uploadFile);
		} catch (FTPException e) {
			this.except("Upload File[" + uploadFile.getAbsolutePath() + "] Fail,Retry Upload Later,Error = " + e.getMessage(), this.getClass().getName());
			this.exportExceptionText(e);
			if(autoReUploadWhileFail) this.addQueneObject(uploadFile);
			onUploadFail(UploadEvent.UPLOAD_FAIL,uploadFile);
		} catch (Exception e) {
			this.except("Upload File[" + uploadFile.getAbsolutePath() + "] Fail,Retry Upload Later,Error = " + e.getMessage(), this.getClass().getName());
			this.exportExceptionText(e);
			if(autoReUploadWhileFail) this.addQueneObject(uploadFile);
			onUploadFail(UploadEvent.UPLOAD_FAIL,uploadFile);
		}
		threadHold(3000);
	}
}
