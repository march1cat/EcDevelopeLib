package ec.net.httpclient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


public class RequestFactory {

	
	
	public RequestFactory(){}
	
	public String sendRequest(Request req,String encode) throws IOException{
		return send(req,encode);
	}
	
	public String sendSecureRequest(Request req,String encode) throws IOException{
		Socket socket = (SSLSocket)((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket(InetAddress.getByName(req.HOST), req.port);
		return sendSSL(req,encode,socket);
	}
	
	public String sendSecureRequest(Request req,String encode,SSLCertificate cert) throws Exception{
		Socket socket = cert.getSSLFactory().createSocket(InetAddress.getByName(req.HOST), req.port);
		return sendSSL(req,encode,socket);
	}
	
	protected Socket openHttpConnection(Request req) throws UnknownHostException, IOException{
		Socket socket = new Socket(InetAddress.getByName(req.HOST),req.port);
		OutputStream os = socket.getOutputStream();
		os.write(req.getToSendData());
		os.flush();
		return socket;
	}
	
	protected String send(Request req,String encode) throws IOException{
		Socket socket = openHttpConnection(req);
		
		Response res = new Response(socket.getInputStream(),encode);
		res.parsingServerResponseData();
		String content = res.getContent();
		return content;
	}
	
	private String sendSSL(Request req,String encode,Socket socket) throws IOException{
		OutputStream os = socket.getOutputStream();
		os.write(req.getToSendData());
		os.flush();
		
		Response res = new Response(socket.getInputStream(),encode);
		res.parsingServerResponseData();
		String content = res.getContent();
		return content;
	}
	
}
