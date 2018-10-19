package ec.net.httpserver;

public class ResponseNoResource extends Response{

	
	public ResponseNoResource() {
		super("");
	}
	
	public ResponseNoResource(String resEncode) {
		super("", resEncode);
	}

	@Override
	protected String iniHeader() {
		return "HTTP/1.1 404 Not Found\r\n";
	}
	
	

}
