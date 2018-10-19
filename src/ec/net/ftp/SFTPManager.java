package ec.net.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPManager {
	
	private String host;
	private String account;
	private String password;
	private int port = 22;
	
	private ChannelSftp sftp;
	private JSch jsch;
	private Channel channel;
	private Session sshSession;
	
	public SFTPManager(String host,String account,String password) throws JSchException{
		jsch = new JSch();
		this.host = host;
		this.account = account;
		this.password = password;
		doConnect();
	} 
	
	public SFTPManager(String host,String account,String password,int port) throws JSchException{
		jsch = new JSch();
		this.host = host;
		this.account = account;
		this.password = password;
		this.port = port;
		doConnect();
	}  
	
	
	private void doConnect() throws JSchException{
		jsch.getSession(account, host, port);
		sshSession = jsch.getSession(account, host, port);
		sshSession.setPassword(password);
		sshSession.setConfig(getSessionConfig());
		sshSession.connect();
		channel = sshSession.openChannel("sftp");
		channel.connect();
		sftp = (ChannelSftp) channel;
	}
	
	private Properties getSessionConfig(){
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		return sshConfig;
	}
	
	public void closeConnection(){
		sftp.disconnect();
		sftp.exit();
		channel.disconnect();
		sftp.quit();		
		sshSession.disconnect();
	}
	
	//************************************************
	//Download
	public void downloadFile(String saveLocalFileName,String remotePath,String remoteFileName) throws SftpException, FileNotFoundException{
		sftp.cd(remotePath);
		File file = new File(saveLocalFileName);
		sftp.get(remoteFileName, new FileOutputStream(file));
	}
	//************************************************
	//Upload
	public void uploadFile(String saveLocalFileName,String remotePath) throws FileNotFoundException, SftpException{
		sftp.cd(remotePath);
		File file=new File(saveLocalFileName);
		sftp.put(new FileInputStream(file), file.getName());
	}
	
	public void uploadFile(String saveLocalFileName,String remotePath,String remoteSaveFileName) throws FileNotFoundException, SftpException{
		sftp.cd(remotePath);
		File file=new File(saveLocalFileName);
		sftp.put(new FileInputStream(file), remoteSaveFileName);
	}
	//************************************************
	//Delete
	public void deleteFile(String remoteFilePath,String remoteFileName) throws SftpException{
		sftp.cd(remoteFilePath);
		sftp.rm(remoteFileName);
	}
	//************************************************
	//Else Functions
	public Object[] listFiles(String remoteFilePath) throws SftpException{
		Vector<String> vt = sftp.ls(remoteFilePath);
		return vt.toArray();
	}
	
}
