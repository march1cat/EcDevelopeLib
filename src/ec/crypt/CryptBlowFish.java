package ec.crypt;

import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import ec.system.Basis;

public class CryptBlowFish extends Basis {
	
	private String keyText = null;
	private SecretKeySpec key = null;
	private int ivParameterLength = 8;
	
	public CryptBlowFish(String keyText) {
		this.keyText = keyText;
		key = new SecretKeySpec(keyText.getBytes(), "BLOWFISH");
	}

	public byte[] encrypt(byte[] bts) throws Exception {
		byte[] ivB = new byte[ivParameterLength];
		for(int i = 0;i < ivB.length;i++) {
			ivB[i] = (byte)( (int)(236456 * Math.random()) % 10);
		}
		Cipher cipher = buildCipher(ivB,Cipher.ENCRYPT_MODE);
		byte[] encrypted = cipher.doFinal(bts);
		byte[] n_encrypted = new byte[ivB.length + encrypted.length];
		System.arraycopy(ivB, 0, n_encrypted, 0, ivB.length);
		System.arraycopy(encrypted, 0, n_encrypted, ivB.length, encrypted.length);
		return n_encrypted;
	}
	
	public byte[] decrypt(byte[] bts) throws Exception {
		byte[] ivB = new byte[ivParameterLength];
		for(int i = 0;i < ivB.length;i++) {
			ivB[i] = bts[i];
		}
		byte[] n_bts = new byte[bts.length - ivB.length];
		for(int i = 0 ;i < n_bts.length;i++) {
			n_bts[i] = bts[i + ivB.length];
		}
		Cipher cipher = buildCipher(ivB,Cipher.DECRYPT_MODE);
		byte[] decrypted = cipher.doFinal(n_bts);
		return decrypted;
	}
	
	private Cipher buildCipher(byte[] ivB,int mode) throws Exception {
		AlgorithmParameterSpec iv = new IvParameterSpec(ivB);
		Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
		cipher.init(mode, key, iv);
		return cipher;
	}
	
	

}
