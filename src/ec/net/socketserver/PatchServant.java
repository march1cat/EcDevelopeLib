package ec.net.socketserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;


import ec.net.socketserver.NetConnectionServant;
import ec.net.socketserver.ServerService;

public abstract class PatchServant extends NetConnectionServant{

	private Socket socket = null;
	
	public PatchServant(ServerService server, Socket socket, String serviceEncode) {
		super(server, socket, serviceEncode);
		this.socket = socket;
	}

	protected void sendFileToClientInBytes(String fileUri) throws IOException{
		FileInputStream fis = new FileInputStream(new File(fileUri));
		final int bufferSize = 1024;
		byte[] bt_ar = new byte[bufferSize];
		int c = 0;
		int k = 0;
		do {
			c = fis.read();
			bt_ar[k++] = (byte) c;
			if (k >= bufferSize) {
				socket.getOutputStream().write(bt_ar);
				k = 0;
			}
		} while (c != -1);
		if(k > 0) socket.getOutputStream().write(bt_ar);
		socket.getOutputStream().flush();
		fis.close();
	}
}
