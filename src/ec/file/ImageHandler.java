package ec.file;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;

import ec.string.StringManager;

public class ImageHandler {

	public enum ImageFormat {
		PNG , JPG
	}
	
	private BufferedImage sourceImage = null;
	private ImageFormat format = null;
	private String sourceImageUri = null;
	private String md5 = null;
	
	public ImageHandler(String sourceImageUri , ImageFormat format) {
		this.sourceImageUri = sourceImageUri;
		this.format = format;
	}
	
	public void splitTo(int startX , int startY , int width , int height , String saveTo) throws Exception {
		this.mountImage();
		
		File to = new File(saveTo);
		if(to.exists()) to.delete();
		
		ImageIO.write(sourceImage.getSubimage(startX, startY, width, height), 
				getFormat(), 
				new File(saveTo));
	}
	
	
	public int getWidth() throws Exception {
		mountImage();
		return sourceImage.getWidth();
	}
	
	public int getHeight() throws Exception {
		mountImage();
		return sourceImage.getHeight();
	}
	
	private void mountImage() throws Exception {
		if(sourceImage == null) {
			File f = new File(sourceImageUri);
			if(!f.exists())  throw new Exception("File[" + sourceImageUri + "] not exist!!");
			sourceImage = ImageIO.read(f);
		}
	}
	
	private String getFormat() {
		return format.toString().toLowerCase();
	}
	
	
	public String getMD5() throws Exception {
		mountImage();
		if(md5 == null) {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(sourceImage, getFormat(), baos);
	        byte[] bytes = baos.toByteArray();
	        messageDigest.update(bytes);
	        byte[] out_bytes = messageDigest.digest();
	        md5 = StringManager.conver16HexStr(out_bytes);
		}
        return md5;
	}
	
	public boolean equals(ImageHandler target) throws Exception {
		return this.getMD5().equals(target.getMD5());
	}

	
	
}
