package ec.net.execute;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class HttpDownloadFile {

	private HttpDownloadInvoker invoker = null;
	
	public void registerProgressListener(HttpDownloadInvoker invoker){
		this.invoker = invoker;
	}
	
	public void downloadToFile(String httpAdr, String toFileUri) throws Exception {
		URL url = new URL(httpAdr);
		FileOutputStream fos = new FileOutputStream(toFileUri, false);
		InputStream is = url.openStream();
		DataInputStream dis = new DataInputStream(is);
		final int chunkSize = 1024 * 8;
		byte[] buf = new byte[chunkSize];
		int readLen;
		int readK = 0;
		while ((readLen = is.read(buf, 0, buf.length)) != -1) {
			fos.write(buf, 0, readLen);
			if(invoker != null){
				readK+=(chunkSize/1024);
				invoker.onProgress(false,readK);
			}
		}
		if(invoker != null){
			invoker.onProgress(true,-1);
		}
		is.close();
		fos.close();
	}
}
