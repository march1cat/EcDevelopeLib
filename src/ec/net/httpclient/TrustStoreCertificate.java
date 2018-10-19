package ec.net.httpclient;

public class TrustStoreCertificate {

	private String trustStoreFileUri = null;
	private String filePwd = null;
	
	public TrustStoreCertificate(String trustStoreFileUri,String filePwd){
		this.trustStoreFileUri = trustStoreFileUri;
		this.filePwd = filePwd;
	}

	public String Uri() {
		return trustStoreFileUri;
	}


	public String Pwd() {
		return filePwd;
	}
	
	
}
