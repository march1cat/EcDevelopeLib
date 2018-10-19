package ec.net.httpclient;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

import ec.system.RunningPlatform;

public class ImageDownloadFactory extends DownloadFactory{
	
	public ImageDownloadFactory(String serverHost,int headerSize){
		super(serverHost,headerSize);
	}
	
	
	public void saveWebImage(String imgUri,String saveUri) throws UnknownHostException, IOException{
		req.webQueryPath = imgUri;
		Socket socket = openConnection();
		
		InputStream is = socket.getInputStream();
		String headerData = parsingHeader(is);
		int imageSize = parsingFileContentSize(headerData);
		if(imageSize !=-1){
			if(RunningPlatform.isInDebugMode()) log("Pasring Image Content Size = " + imageSize + " For Web File[" + imgUri + "]");
			downloadFile(is,imageSize,saveUri);
		} else {
			downloadFile(is,saveUri);
		}
	}
	
	//override
	protected int parsingFileContentSize(String htmlHeader){
		String[] headerAr = htmlHeader.toString().split("\r\n");
		boolean isImage = false;
		int contentLen = 0;
		for(String header : headerAr){
			if(header.indexOf("Content-Length") >= 0){
				String contentLength = header.substring(header.indexOf(":") + 1);
				contentLen =  Integer.parseInt(contentLength.trim());
			}
			if(header.indexOf("Content-Type: image/jpeg") >= 0){
				isImage = true;
			}
		}
		return isImage && contentLen != 0 ? contentLen : -1;
	}
}
