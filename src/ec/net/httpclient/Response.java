package ec.net.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/*
 * Version 1 At 20141223.2
 * 
 * */
public class Response {

	private InputStream responseIs;
	private String header;
	private String content;
	private String encode = "UTF-8";
	
	public Response(InputStream responseIs){
		this.responseIs = responseIs;
	}
	public Response(InputStream responseIs,String encode){
		this.responseIs = responseIs;
		this.encode = encode;
	}
	
	public void parsingServerResponseData() throws IOException{
		BufferedReader in = new BufferedReader( new InputStreamReader(responseIs,encode));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			   response.append(inputLine);
		}
		content = response.toString();
	}

	public String getContent() {
		return content;
	}
	public String getHeader() {
		return header;
	}
	
	
}
