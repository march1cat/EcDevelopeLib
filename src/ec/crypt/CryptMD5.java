package ec.crypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ec.system.Basis;

public class CryptMD5 extends Basis {
	
	
	public static String hash(String data) throws Exception {
		return hash(data.getBytes());
	}
	
	public static String hash(byte[] dataBytes) throws Exception {
		byte[] messageDigest = digest(dataBytes);
		StringBuffer hexString = new StringBuffer();
        for(int i=0; i<messageDigest.length; i++)
        {
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
        }
        return hexString.toString();
	}
	
	public static byte[] digest(byte[] dataBytes) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(dataBytes);
		byte[] messageDigest = md.digest();
		return messageDigest;
	}

}
