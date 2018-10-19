package ec.net.httpclient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import ec.system.Basis;
import ec.system.RunningPlatform;

public class DownloadFactory extends Basis{

	protected RequestGET req = null;
	protected int maxHeaderBuff = 800; //In Case System Crash
	protected int headerSize = -1;
	
	public DownloadFactory(String serverHost,int headerSize){
		req = new RequestGET(serverHost);
		this.headerSize = headerSize;
	}
	
	public RequestGET getRequest(){
		return req;
	}
	
	protected Socket openConnection() throws UnknownHostException, IOException{
		Socket socket = new Socket(InetAddress.getByName(req.HOST),req.port);
		OutputStream os = socket.getOutputStream();
		os.write(req.getToSendData());
		os.flush();
		return socket;
	}
	
	public void saveFileToLocal(String fileUri,String saveUri) throws UnknownHostException, IOException{
		req.webQueryPath = fileUri;
		Socket socket = openConnection();
		
		InputStream is = socket.getInputStream();
		String headerData = parsingHeader(is);
		int fileSize = parsingFileContentSize(headerData);
		if(fileSize !=-1){
			if(RunningPlatform.isInDebugMode()) log("Pasring Content Size = " + fileSize + " For Web File[" + fileUri + "]");
			downloadFile(is,fileSize,saveUri);
		} else this.except("Parsing File Size Fail In Download Factory,",this.getClass().getName());
	}
	
	protected String parsingHeader(InputStream is) throws IOException{
		StringBuffer headerBuf = new StringBuffer();
		int readData = -1;
		int r_cnt = 0;
		int changeLineShwAmt = 0;
		boolean lastCharIs13 = false;
		do{
			readData = is.read();
			headerBuf.append((char)readData);
			r_cnt++;
			if(readData == 10) {
				changeLineShwAmt++;
			}
			if(readData == 13) {
				lastCharIs13 = true;
			} else lastCharIs13 = false;
			if(changeLineShwAmt == headerSize) break;
		} while(readData != -1 && r_cnt <= maxHeaderBuff);
		return headerBuf.toString();
	}
	
	protected int parsingFileContentSize(String htmlHeader){
		String[] headerAr = htmlHeader.toString().split("\r\n");
		int contentLen = -1;
		for(String header : headerAr){
			if(header.indexOf("Content-Length") >= 0){
				String contentLength = header.substring(header.indexOf(":") + 1);
				contentLen =  Integer.parseInt(contentLength.trim());
			}
		}
		return contentLen;
	}
	
	protected void downloadFile(InputStream is,int fileSize,String saveUri) throws IOException{
		int size = fileSize + 1;
		int b = -1;
		byte[] ar = new byte[1024];
		int bufCnt = 0;
		int readCout = 0;
		FileOutputStream fos = new FileOutputStream(saveUri);
		do{
			b = is.read();
			if(b == -1) break;
			if(readCout > 0){
				ar[bufCnt++] = (byte)b;
				if(bufCnt >= ar.length){
					fos.write(ar,0,ar.length);
					ar = new byte[ar.length];
					bufCnt = 0;
				}
			}
			readCout++;
			if(fileSize != 0 && readCout < size) break;
		} while(true);
		if(bufCnt > 0) fos.write(ar,0,bufCnt);
		fos.close();
	}
	
	protected void downloadFile(InputStream is,String saveUri) throws IOException{
		int b = -1;
		byte[] ar = new byte[10240];
		int bufCnt = 0;
		int readCout = 0;
		FileOutputStream fos = new FileOutputStream(saveUri);
		do {
			b = is.read();
			if(b == -1) break;
			ar[bufCnt++] = (byte)b;
			if(bufCnt >= ar.length){
				fos.write(ar,0,ar.length);
				ar = new byte[ar.length];
				bufCnt = 0;
			}
			readCout++;
			if(b == -1) break;
		} while(true);
		if(bufCnt > 0) fos.write(ar,0,bufCnt);
		fos.close();
	}
}
