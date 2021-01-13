package ec.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Base64;

public class FileBase64Handler {

	
	public void base64ToFile(String base64Code , String saveTo) throws Exception {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		byte[] bytes = Base64.getDecoder().decode(base64Code);
        File file = new File( saveTo );
        fos = new FileOutputStream(file);
        bos = new BufferedOutputStream(fos);
        bos.write(bytes);
		if(bos != null) bos.close();
		if(fos != null) fos.close();
	}
}
