package ec.net.httpclient;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class AllTrustSSLCertificate extends SSLCertificate{

	private String sskCtxType = null;
	
	public AllTrustSSLCertificate(){
		super(null,null,null);
		sskCtxType = "SSL";
	}
	
	public AllTrustSSLCertificate(String sskCtxType) {
		super(null,null,null);
		this.sskCtxType = sskCtxType;
	}
	
	@Override
	protected SSLSocketFactory generateSSLFactory() throws Exception {
		 TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
             public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                 return null;
             }
             public void checkClientTrusted(X509Certificate[] certs, String authType) {}
             public void checkServerTrusted(X509Certificate[] certs, String authType) {}
         }};
		SSLContext ctx = SSLContext.getInstance(sskCtxType);
	 	ctx.init(null, trustAllCerts, new java.security.SecureRandom());
	 	return ctx.getSocketFactory();
	}

	
	
	
}
