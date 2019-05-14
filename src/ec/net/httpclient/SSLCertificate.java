package ec.net.httpclient;

import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class SSLCertificate {

	public static enum SSL_FORMAT{
		JKS,PKCS12
	}
	
	private String certPos = null;
	private String pwd = null;
	private Object format = null;
	
	private SSLSocketFactory sslFac = null;
	
	public SSLCertificate(){
		sslFac = ((SSLSocketFactory)SSLSocketFactory.getDefault());
	}
	
	
	public SSLCertificate(String certPos,String pwd){
		this.certPos = certPos;
		this.pwd = pwd;
		this.format = SSL_FORMAT.JKS;
	}
	
	public SSLCertificate(String certPos,String pwd,Object format){
		this.certPos = certPos;
		this.pwd = pwd;
		this.format = format;
	}
	
	public SSLCertificate(String certPos,String pwd,Object format,TrustStoreCertificate trustStore){
		System.setProperty("javax.net.ssl.trustStore", trustStore.Uri());
		System.setProperty("javax.net.ssl.trustStorePassword", trustStore.Pwd());
		this.certPos = certPos;
		this.pwd = pwd;
		this.format = format;
	}
	
	
	protected SSLSocketFactory generateSSLFactory() throws Exception{
		 SSLSocketFactory factory = null;
		 try {
	            SSLContext ctx;
	            KeyManagerFactory kmf;
	            KeyStore ks = null;
	            char[] passphrase = pwd.toCharArray();

	            ctx = SSLContext.getInstance("TLS");
	            kmf = KeyManagerFactory.getInstance("SunX509");
	            if(format == SSL_FORMAT.JKS){
	            	ks = KeyStore.getInstance("JKS");
	            } else if(format == SSL_FORMAT.PKCS12) {
	            	ks = KeyStore.getInstance("PKCS12");
	            } else {
	            	throw new Exception("Not Allow Certificate Format Error " + format);
	            }
	            ks.load(new FileInputStream(certPos), passphrase);
	            kmf.init(ks, passphrase);
	            ctx.init(kmf.getKeyManagers(), null, null);

	            factory = ctx.getSocketFactory();
	        } catch (Exception e) {
	            throw e;
	        }
		 return factory;
	}
	
	public SSLSocketFactory getSSLFactory() throws Exception{
		if(sslFac == null) sslFac = generateSSLFactory();
		return sslFac;
	}
}
