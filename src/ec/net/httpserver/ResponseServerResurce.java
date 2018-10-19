package ec.net.httpserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import ec.net.httpserver.Response.Header;

public class ResponseServerResurce extends Response{

	public ResponseServerResurce(File resF) throws Exception {
		super("");
		loadFileToStream(resF);
	}
	
	private void loadFileToStream(File resF) throws Exception{
		FileInputStream fis = new FileInputStream(resF);
		contBytAr = new byte[(int) resF.length()];
		fis.read(contBytAr);
		this.setHttpHeader(Header.CONTENT_LENGTH, String.valueOf(contBytAr.length));
	}

}
