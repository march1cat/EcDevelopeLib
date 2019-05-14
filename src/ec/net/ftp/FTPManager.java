package ec.net.ftp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPMessageCollector;
import com.enterprisedt.net.ftp.FTPTransferType;

public class FTPManager {
	
	public enum FileTransferType{
		TEXT,BINARY
	}
	
	
	protected String ftp_server_host = null;
	protected int ftp_server_port = 21;
	
	
	private String ftp_acc = null;
	private String ftp_pass = null;
	private Object transferType = null;
	
	protected FTPClient ftp = null;
	private String nowCursor_Path = null;
	
	public FTPManager(String ftp_server_host){
		this.ftp_server_host = ftp_server_host;
		transferType = FileTransferType.BINARY;
	}
	
	public FTPManager(String ftp_server_host,int ftp_server_port){
		this.ftp_server_host = ftp_server_host;
		this.ftp_server_port = ftp_server_port;
		transferType = FileTransferType.BINARY;
	}
	
	private void iniConnector() throws IOException, FTPException{
		ftp = new FTPClient(ftp_server_host,ftp_server_port,0,"UTF-8");
	}

	public void connect() throws IOException, FTPException{
		iniConnector();
		FTPMessageCollector listener = new FTPMessageCollector();
		ftp.setMessageListener(listener);
		
		if(this.ftp_acc  != null && this.ftp_pass != null)  LoginFTPServer();
		
		ftp.setConnectMode(FTPConnectMode.PASV);
		if(transferType == FileTransferType.BINARY) ftp.setType(FTPTransferType.BINARY);
		else ftp.setType(FTPTransferType.ASCII);
	}
	
	private void LoginFTPServer() throws IOException, FTPException{
		ftp.login(ftp_acc, ftp_pass);
	}
	
	public void LogoutFTPServer() throws IOException, FTPException{
		ftp.quit();
	}
	
	public void CD_Path(String cursor_path){
		nowCursor_Path = (cursor_path.endsWith("/")) ? cursor_path : cursor_path.concat("/");
	}
	//download
	public void downloadFile(String localFilePath,String remoteFilePath) throws IOException, FTPException{
		FileOutputStream fos = new FileOutputStream(localFilePath);
		ftp.get(fos, nowCursor_Path != null ? nowCursor_Path.concat(remoteFilePath) : remoteFilePath);
	}
	public void downloadFile(String localFilePath,String remotePath,String remoteFileName) throws IOException, FTPException{
		FileOutputStream fos = new FileOutputStream(localFilePath);
		CD_Path(remotePath);
		downloadFile(localFilePath,remoteFileName);
	}
	public void downloadFile(String localPath,String localFileName,String remotePath,String remoteFileName) throws IOException, FTPException{
		String localFilePath = localPath.concat(localFileName);
		FileOutputStream fos = new FileOutputStream(localFilePath);
		CD_Path(remotePath);
		downloadFile(localFilePath,remoteFileName);
	}
	//upload
	public void uploadFile(String localFileName,String remoteFileName) throws IOException, FTPException{
		FileInputStream fis = new FileInputStream(localFileName);
		ftp.put(fis, nowCursor_Path != null ? nowCursor_Path.concat(remoteFileName) : remoteFileName);
	}
	public void uploadFile(String localFileName,String remoteFilePath,String remoteFileName) throws IOException, FTPException{
		CD_Path(remoteFilePath);
		uploadFile(localFileName,remoteFileName);
	}
	
	
	public void removeFile(String remoteFilePath,String remoteFileName) throws IOException, FTPException{
		CD_Path(remoteFilePath);
		ftp.delete(nowCursor_Path != null ? nowCursor_Path + remoteFileName : remoteFileName);
	}

	public void setFtp_acc(String ftp_acc) {
		this.ftp_acc = ftp_acc;
	}

	public void setFtp_pass(String ftp_pass) {
		this.ftp_pass = ftp_pass;
	}

	public void setTransferType(Object transferType) {
		this.transferType = transferType;
	}
	
	
	
}
