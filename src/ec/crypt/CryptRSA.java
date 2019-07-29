package ec.crypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import ec.file.FileManager;
import ec.system.Basis;

public class CryptRSA extends Basis {

	public enum RASCryptType {
		MD5,SHA256
	}
	
	
	private String signKeyPath = null;
	private String verifyKeyPath = null;
	private Object rsaCryptType = RASCryptType.SHA256;
	
	public CryptRSA(String signKeyPath,String verifyKeyPath) {
		this.signKeyPath = signKeyPath;
		this.verifyKeyPath = verifyKeyPath;
	}
	
	public CryptRSA(String signKeyPath,String verifyKeyPath,Object rsaCryptType) {
		this.signKeyPath = signKeyPath;
		this.verifyKeyPath = verifyKeyPath;
		this.rsaCryptType = rsaCryptType;
	}
	
	
	public byte[] sign(byte[] signData) throws Exception {
		if(isFileExist(signKeyPath)) {
			PrivateKey pk = null;
			try {
				byte[] bytes = Files.readAllBytes(new File(signKeyPath).toPath());
				pk = preparePrivateKey(bytes);
			} catch(Exception e) {
				if(e.getMessage().indexOf("DerInputStream.getLength") >= 0 && e.getMessage().indexOf("too big") >= 0) {
					List<String> ls = FileManager.ReadTextAllDataInLineByLine(signKeyPath);
					StringBuffer t = new StringBuffer();
					for(int i = 1;i < ls.size() - 1;i++) {
						t.append(ls.get(i) + "\n");
					}
					pk = preparePrivateKey(t.toString().getBytes());
				} else {
					throw e;
				}
			}
			
			if(pk != null) {
				Signature ps = Signature.getInstance(rsaCryptType.toString() + "withRSA");
				ps.initSign(pk);
				ps.update(signData);
				byte[] newbs = ps.sign();
				return newbs;
			} else {
				log("Private key generate fail!! key file = " + signKeyPath);
				throw new Exception("Private key generate fail!!" + signKeyPath);
			}
		} else throw new Exception("Sign key file not exist!!, file = " + signKeyPath);
		
	}
	
	
	public boolean verify(byte[] verifyData,byte[] signature) throws Exception {
		if(isFileExist(verifyKeyPath)) {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			FileInputStream  fis = new FileInputStream(new File(verifyKeyPath));
			Certificate cer = cf.generateCertificate(fis);
			PublicKey pk = cer.getPublicKey();
			Signature ps = Signature.getInstance(rsaCryptType.toString() + "withRSA");
			ps.initVerify(pk);
			ps.update(verifyData);
			boolean isvalid = ps.verify(signature);
			return isvalid;
		} else throw new Exception("verify key file not exist!!, file = " + verifyKeyPath);
	}
	
	
	
	
	private PrivateKey preparePrivateKey(byte[] keyfileBytes) throws Exception {
		byte[] newB = Base64.decodeBase64(keyfileBytes);
		PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(newB);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PrivateKey pk = kf.generatePrivate(ks);
		return pk;
	}
	
	private boolean isFileExist(String fpath) {
		File f = new File(fpath);
		return f.exists();
	}
	

}
