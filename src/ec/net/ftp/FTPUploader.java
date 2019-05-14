package ec.net.ftp;

import java.io.File;
import java.io.IOException;

import com.enterprisedt.net.ftp.FTPException;

public class FTPUploader extends FileUploader{
	
	public enum UploadEvent{
		UPLOAD_FAIL
	}
	
	
	private String ftpHost = null;
	private int ftpPort = 21;
	private String ftpAcc = null;
	private String ftpPass = null;
	private String remoteSaveUri = null;
	private boolean autoReUploadWhileFail = true;
	
	private FTPManager ftp = null;
	
	public FTPUploader(String ftpHost){
		this.ftpHost = ftpHost;
	}
	
	
	public FTPUploader(String ftpHost,int ftpPort){
		this.ftpHost = ftpHost;
		this.ftpPort = ftpPort;
	}
	
	@Override
	protected void uploadProcess(UploadRequest request) {
		if(ftp == null)  {
			ftp = new FTPManager(ftpHost,ftpPort);
			ftp.setFtp_acc(ftpAcc);
			ftp.setFtp_pass(ftpPass);
		}
		
		log("Upload File To FTP Server["+ftpHost+"],File Uri = " + 
				request.getFilePath() +  ", Remote File Uri = " + request.getSaveTo() );
		try {
			ftp.connect();
			ftp.uploadFile(request.getFilePath(), request.getSaveTo());
			ftp.LogoutFTPServer();
			request.markEndSuccess();
			onUploadComplete(request);
			log("Upload File Finish["+request.getFilePath()+"]!!");
		} catch (IOException e) {
			this.except("Upload File[" + request.getFilePath() + "] Fail,Retry Upload Later,Error = " + e.getMessage(), this.getClass().getName());
			this.exportExceptionText(e);
			request.markFailRetry();
			if(autoReUploadWhileFail) this.addQueneObject(request);
			onUploadFail(UploadEvent.UPLOAD_FAIL,request);
		} catch (FTPException e) {
			this.except("Upload File[" + request.getFilePath() + "] Fail,Retry Upload Later,Error = " + e.getMessage(), this.getClass().getName());
			this.exportExceptionText(e);
			request.markFailRetry();
			if(autoReUploadWhileFail) this.addQueneObject(request);			
			onUploadFail(UploadEvent.UPLOAD_FAIL,request);
		} catch (Exception e) {
			this.except("Upload File[" + request.getFilePath() + "] Fail,Retry Upload Later,Error = " + e.getMessage(), this.getClass().getName());
			this.exportExceptionText(e);
			request.markFailRetry();
			if(autoReUploadWhileFail) this.addQueneObject(request);
			onUploadFail(UploadEvent.UPLOAD_FAIL,request);
		}
		threadHold(3000);
	}


	public void setFtpAcc(String ftpAcc) {
		this.ftpAcc = ftpAcc;
	}


	public void setFtpPass(String ftpPass) {
		this.ftpPass = ftpPass;
	}
	
	
	
	
	
}
