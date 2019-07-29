package ec.net.execute;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ec.net.auth.HttpAuthorization;
import ec.system.Basis;
import ec.system.RunningPlatform;


public class WebQueryFactory extends Basis{

	public enum QueryFailReason {
		CONNECTION_REFUSED,CONNECTION_FAIL,HOST_NOT_FOUND
	}
	
	public static String Data_encode = "UTF-8";
	private String dataEncode = "UTF-8";
	
	protected URL url = null;
	private InputStream is;
	protected HttpURLConnection urlCon;
	private Map<String,String> headers = null;
	
	private String cookieValue = null;

	public void setHost(String host) {
		try {
			url = new URL(host);
			urlCon = (HttpURLConnection) url.openConnection(); 
		} catch (Exception e) {
			this.exportExceptionText(e);
		}
	}
	
	public void setAuthorization(String username,String password) throws ProtocolException{
		Authenticator.setDefault(new HttpAuthorization(username,password));
	}

	public String queryWeb(String postData) {
		if (url != null) {
			try {
				setHeaderValue("User-Agent", "Macintosh; Intel Mac OS X 10_14_4");
				if(cookieValue != null) {
					setHeaderValue("Cookie", cookieValue);
				}
				if(headers != null) {
					Iterator<String> iter = headers.keySet().iterator();
					while(iter.hasNext()) {
						String key = iter.next();
						urlCon.setRequestProperty(key, headers.get(key));
					}
				}
				if(postData != null) Write(postData);
				return Read();
			} catch(Exception e){
				if(e.getMessage().toUpperCase().indexOf("Connection".toUpperCase()) >= 0 &&
						e.getMessage().toUpperCase().indexOf("refused".toUpperCase()) >= 0) {
					return QueryFailReason.CONNECTION_REFUSED.toString();
				} else {
					this.exportExceptionText(e);
					return QueryFailReason.CONNECTION_FAIL.toString();
				}
			}
		} else {
			return QueryFailReason.HOST_NOT_FOUND.toString();
		}
	}

	public InputStream getQueryWebStream(String postData) {
		if (url != null) {
			try {
				Write(postData);
				is = urlCon.getInputStream();
			} catch (IOException e) {
				this.exportExceptionText(e);
				this.except("Get Http Connection InputStream Fail", this.getClass().getName());
			}
			return is;
		} else {
			return null;
		}
	}

	// ==========================================================

	protected String Read() throws Exception {
		StringBuffer strBuffer = new StringBuffer("");
		try {
			is = urlCon.getInputStream();
			int readCode = 0;
			byte[] byteAr = new byte[1024];
			int readCount = 0;

			do {
				readCode = is.read();
				byteAr[readCount++] = (byte) readCode;
				if (readCount == 1024) {
					strBuffer.append(new String(byteAr, 0, readCount,
							dataEncode));
					readCount = 0;
				}
			} while (readCode != -1);
			strBuffer.append(new String(byteAr, 0, readCount,
					dataEncode));
		} catch (Exception e) {
			throw e;
		}
		if (dataEncode.equals("UTF-8")) {
			return (strBuffer.toString().length() >= 1) ? strBuffer.toString()
					.substring(0, strBuffer.toString().length() - 1)
					: strBuffer.toString();
		} else {
			return strBuffer.toString();
		}
	}

	private void Write(String postData) throws IOException {
		OutputStream os = null;
		if (!urlCon.getDoOutput()) {
			urlCon.setDoOutput(true);
		}
		os = urlCon.getOutputStream();
		os.write(postData.getBytes());
		os.flush();
	}

	// ==========================================================
	public static String GetHTMLSourceCode(String httpAddress, int bufferSize) {
		if (bufferSize < 1024)
			bufferSize = 1024;
		try {
			URL url = new URL(httpAddress);
			InputStream is = url.openStream();
			InputStreamReader isr = new InputStreamReader(is);

			StringBuffer dataBuf = new StringBuffer("");
			int c = 0;
			int k = 0;
			byte[] byteAr = new byte[bufferSize];
			do {
				c = isr.read();
				if(c != -1) {
					byteAr[k++] = (byte) c;
					if (k >= bufferSize) {
						dataBuf.append(new String(byteAr, 0, bufferSize,
								Data_encode));
						k = 0;
					}
				}
			} while (c != -1);
			if(k > 0) dataBuf.append(new String(byteAr, 0, k, Data_encode));
			return dataBuf.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String GetHTMLSourceCode(String httpAddress, int bufferSize,String Data_encode) {
		if (bufferSize < 1024)
			bufferSize = 1024;
		try {
			URL url = new URL(httpAddress);
			InputStream is = url.openStream();
			InputStreamReader isr = new InputStreamReader(is);

			StringBuffer dataBuf = new StringBuffer("");
			int c = 0;
			int k = 0;
			byte[] byteAr = new byte[bufferSize];
			do {
				c = isr.read();
				if(c != -1) {
					byteAr[k++] = (byte) c;
					if (k >= bufferSize) {
						dataBuf.append(new String(byteAr, 0, bufferSize,
								Data_encode));
						k = 0;
					}
				}
			} while (c != -1);
			if(k > 0) dataBuf.append(new String(byteAr, 0, k, Data_encode));
			return dataBuf.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void SaveHTMLFile(String httpAddress, String fileName,
			int bufferSize) {
		if (bufferSize < 1024)
			bufferSize = 1024;
		try {
			URL url = new URL(httpAddress);
			InputStream is = url.openStream();
			OutputStream to_file = new FileOutputStream(fileName);

			byte[] buffer = new byte[bufferSize];
			int bytes_read;

			while ((bytes_read = is.read(buffer)) != -1)
				to_file.write(buffer, 0, bytes_read);

			to_file.flush();
			to_file.close();
		} catch (Exception e) {
			try {
				OutputStream to_file = new FileOutputStream(fileName);
				to_file.flush();
				to_file.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}
	
	
	
	public String getCookie() {
		return cookieValue;
	}

	public void setCookie(String cookieValue) {
		this.cookieValue = cookieValue;
	}

	public void setHeaderValue(String headerName,String headerValue){
		if(headers == null) headers = new HashMap<String, String>();
		headers.put(headerName, headerValue);
	}

	public void setDataEncode(String dataEncode) {
		this.dataEncode = dataEncode;
	}
	
	
	

}
