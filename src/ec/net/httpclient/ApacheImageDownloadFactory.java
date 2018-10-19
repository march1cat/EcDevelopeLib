package ec.net.httpclient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import ec.system.Basis;

public class ApacheImageDownloadFactory extends ImageDownloadFactory{

	
	private int bufferSize = 1024;
	
	public ApacheImageDownloadFactory(String serverHost,int bufferSize) {
		super(serverHost, 0);
		this.bufferSize = bufferSize;
	}

	//override
	public void saveWebImage(String imgUri,String saveUri) throws ClientProtocolException, IOException {
		saveWebImage("http://" + req.HOST,imgUri,saveUri);
	}
	
	
	public void saveWebImage(String webHost,String imgUri,String saveUri) throws ClientProtocolException, IOException {
		String auditImgUri = imgUri.startsWith("/") ? imgUri : "/" + imgUri;
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(webHost + auditImgUri);
		HttpResponse response = client.execute(get);
		InputStream input = null;
        OutputStream output = null;
        byte[] buffer = new byte[bufferSize];
        try {
            input = response.getEntity().getContent();
            output = new FileOutputStream(saveUri);
            for (int length; (length = input.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
        } finally {
            if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
            if (input != null) try { input.close(); } catch (IOException logOrIgnore) {}
        }
	}
	
	
	
	
}
