package ec.file.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import ec.file.FileManager;
import ec.system.Basis;

public class Gzipper extends Basis{

	
	public static boolean compressFile(String source,String saveGzTo) throws Exception {
		File sourceFile = new File(source);
		if(sourceFile.exists()) {
			File saveToFile = new File(saveGzTo);
			if(saveToFile.exists() && !saveToFile.isDirectory()) saveToFile.delete();
			FileOutputStream fos = new FileOutputStream(saveGzTo);
			GZIPOutputStream gzip = new GZIPOutputStream(fos);
			gzip.write(Files.readAllBytes(new File(source).toPath()));
            gzip.close();
            File gzFile = new File(saveGzTo);
            return gzFile.exists();
		} else {
			throw new Exception("Source File not found when gzip compress file : " + source);
		}
	}
	
	
	public static boolean decompressFile(String source,String saveOutTo) throws Exception {
		File sourceFile = new File(source);
		if(sourceFile.exists()) {
			File saveToFile = new File(saveOutTo);
			if(saveToFile.exists() && !saveToFile.isDirectory()) saveToFile.delete();
			FileInputStream fis = new FileInputStream(source);
			GZIPInputStream gis = new GZIPInputStream(fis);
			int c = 0;
			byte[] t = new byte[2048];
			int count = 0;
			while(c != -1) {
				c = gis.read();
				if(c == -1) break;
				t[count++] = (byte) c;
				if(count >= t.length) {
					FileManager.writeByteToFile(saveOutTo, t, true);
					t = new byte[2048];
					count = 0;
				}
			}
			if(count > 0) {
				byte[] k = new byte[count];
				for(int i = 0;i < k.length;i++) {
					k[i] = t[i];
				}
				FileManager.writeByteToFile(saveOutTo, k, true);
			}
			gis.close();
			File destFile = new File(saveOutTo);
            return destFile.exists();
		} else {
			throw new Exception("Source File not found when gzip decompress file : " + source);
		}
	}
}
