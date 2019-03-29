package ec.net.httpserver;

import java.io.File;

public class ResponseServerBytesResource extends Response{

	private String fpath = null;
	public ResponseServerBytesResource(String fpath) throws Exception {
		super("");
		this.fpath = fpath;
		iniMimeType();
		
	}
	public String getFpath() {
		return fpath;
	}
	
	private void iniMimeType() throws Exception{
		if(fpath.endsWith("mp4")) {
			this.setHttpHeader(Response.Header.CONTENT_TYPE, "video/mp4");
			this.setHttpHeader("Accept-Ranges", "bytes");
		} else throw new Exception("Not defined MIME type for " + fpath);
	}

}
