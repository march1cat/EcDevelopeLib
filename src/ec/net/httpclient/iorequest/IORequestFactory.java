package ec.net.httpclient.iorequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import ec.net.httpclient.Request;
import ec.net.httpclient.RequestFactory;

public class IORequestFactory extends RequestFactory{

	@Override
	protected String send(Request req, String encode) throws IOException {
		Socket socket = openHttpConnection(req);
	
		InputStream is = socket.getInputStream();
		int a = -1;
		byte[] aa = new byte[1024];
		int rct = 0;
		StringBuffer content = new StringBuffer();
		do{
			a = is.read();
			aa[rct++] = (byte)a;
			if(rct>=aa.length){
				content.append(new String(aa,0,aa.length,encode));
				aa = new byte[1024];
				rct = 0;
			}
		} while(a != -1);
		if(rct > 0)  content.append(new String(aa,0,rct,encode));
		return content.toString();
	}

	
}
