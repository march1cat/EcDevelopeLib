package ec.net.auth;

import java.net.Authenticator;
import java.net.InetAddress;
import java.net.PasswordAuthentication;

public class HttpAuthorization extends Authenticator {
	
	private String username = null;
	private String password = null;
	
	public HttpAuthorization(String username,String password){
		this.username = username;
		this.password = password;
	}
	
	protected PasswordAuthentication getPasswordAuthentication() {
        // Get information about the request
        String promptString = getRequestingPrompt();
        String hostname = getRequestingHost();
        InetAddress ipaddr = getRequestingSite();
        int port = getRequestingPort();
        // Return the information
        return new PasswordAuthentication(username, password.toCharArray());
    }
}
