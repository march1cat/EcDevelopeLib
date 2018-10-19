package ec.net.httpserver;

public class ResponseAccessDeny extends Response{
	
	public ResponseAccessDeny() {
		super("");
	}
	
	public ResponseAccessDeny(String resEncode) {
		super("", resEncode);
	}

	@Override
	protected String iniHeader() {
		return "HTTP/1.1 403 Access Deny\r\n";
	}
	
}
