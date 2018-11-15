package ec.net.execute;


import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ec.net.httpclient.SSLCertificate;


public class HttpsWebQueryFactory extends WebQueryFactory {

	public void setHost(String host,SSLCertificate SSlCertificate) {
		try {
			url = new URL(host);
			super.urlCon = (HttpsURLConnection) url.openConnection(); 
			((HttpsURLConnection) urlCon).setSSLSocketFactory(SSlCertificate.getSSLFactory());
		} catch (Exception e) {
			this.exportExceptionText(e);
		}
	}
}
