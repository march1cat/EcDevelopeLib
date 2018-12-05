package ec.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

	
	//================================================================
	//Read File 可能會有編碼問題
	public static String ReadTextAllData(String fileName) {
		StringBuffer bs = new StringBuffer();
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null) {
				bs.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bs.toString();
	}
	
	
	public static List<String> ReadTextAllDataInLineByLine(String fileName){
		List<String> lst = new ArrayList();
		try {
			String line = null;
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			while ((line = br.readLine()) != null) {
				lst.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return lst;
	}
	
	public static String ReadTextInSpCode(String fileName, String code, int bufferSize) {
		if(bufferSize <= 0){
			bufferSize = 1024;
		}
		FileInputStream fis = null;
		StringBuffer dt_buf = new StringBuffer("");
		try {
			fis = new FileInputStream(new File(fileName));
			byte[] bt_ar = new byte[bufferSize];
			int c = 0;
			int k = 0;
			do {
				c = fis.read();
				bt_ar[k++] = (byte) c;
				if (k >= bufferSize) {
					dt_buf.append(new String(bt_ar, 0, bufferSize, code));
					k = 0;
				}

			} while (c != -1);
			dt_buf.append(new String(bt_ar, 0, k, code));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String ret = dt_buf.toString();
		ret = ret.substring(0, ret.length() - 1);
		return ret;
	}
	//================================================================
	//Write File
	
	public static void FileWrite(String fileName, String str) throws Exception {
		FileOutputStream os = null;
		os = new FileOutputStream(new File(fileName));
		os.write(str.getBytes("UTF8"));
		os.flush();
		os.close();
	}
	
	public static void FileWrite(String fileName, String str, String code) throws Exception {
		FileOutputStream os = null;
		os = new FileOutputStream(new File(fileName));
		os.write(str.getBytes(code));
		os.flush();
		os.close();
	}
	
	public static void bufferWrite(String fileName, String str) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true),"UTF8"));
			bw.write(str);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void bufferWrite(String fileName, String str, String code) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true),code));
			bw.write(str);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeByteToFile(String filename,byte[] bytes,boolean isAppend) throws IOException{
		File f = new File(filename);
		if(!f.exists()) f.createNewFile();
		if(isAppend) Files.write(f.toPath(), bytes, StandardOpenOption.APPEND);
		else Files.write(f.toPath(), bytes);
	}
	
	//==================================================
	//CopyFile
	public static void copyDiectory(EcDirector sourceDir,EcDirector saveToDir) throws IOException{
		if(sourceDir.isDirectory() && saveToDir.isDirectory()) {
			List<String> ls = sourceDir.listFile();
			if(ls != null && !ls.isEmpty())
				for(String s : ls){
					copyFile(sourceDir.Uri() + s,saveToDir.Uri() + s);
				}
		}
	}
	
	public static void copyFile(String srcFileName,String trgFileName) throws IOException{
		File srcFile =new File(srcFileName);
        File trgFile =new File(trgFileName);
        
        InputStream  inStream = new FileInputStream(srcFile);
        OutputStream outStream = new FileOutputStream(trgFile);
        
        byte[] buffer = new byte[1024];
        
        int length;
        while ((length = inStream.read(buffer)) > 0){
            outStream.write(buffer, 0, length);
        }

        if (inStream != null)inStream.close();
        if (outStream != null)outStream.close();
        
	}
	
	//==================================================
	//Folder
	public static ArrayList<String> getFolderDocumentList(String folderPath) {
		ArrayList<String> fileNameList = new ArrayList();
		try {
			File doc = new File(folderPath);
			String[] docList = doc.list();
			for (int i = 0; i < docList.length; i++) {
				fileNameList.add(docList[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileNameList;
	}
	
	public static void deleteFolder(String path){
		try {
			ArrayList<String> lst = getFolderDocumentList(path);
			for(String i : lst){
				File f = new File(path + File.separator + i);
				if(f.isDirectory()) {
					deleteFolder(path + File.separator + i);
				} else {
					f.delete();
				}
			}
			File targetFolder = new File(path);
			targetFolder.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
