package ec.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ConfigManager {

	//普通沒有中文的config
	public static HashMap getConfigMap(String fileName) throws Exception {
		HashMap<String, String> configMap = new HashMap<String, String>();
		String[] cfAr = readConfig(fileName).split("\n");
		for (int i = 0; i < cfAr.length; i++) {
			cfAr[i] = cfAr[i].trim();
			if (cfAr[i].startsWith("//") || cfAr[i].indexOf("=") < 0 || cfAr[i].indexOf("=") < 0) {
				continue;
			}
			String key = cfAr[i].split("=")[0].trim();
			String value = cfAr[i].split("=")[1].trim();
			configMap.put(key, value);
		}
		return configMap;
	}

	public static String readConfig(String fileName) throws Exception {
		StringBuffer buffer = new StringBuffer("");
		FileInputStream fis = new FileInputStream(new File(fileName + ".conf"));
		InputStreamReader isr = new InputStreamReader(fis);
		int c = 0;
		int k = 0;
		byte[] dataAr = new byte[1024];
		do {
			c = isr.read();
			dataAr[k++] = (byte) c;
			if (k == 1024) {
				buffer.append(new String(dataAr, 0, 1024, "UTF-8"));
				k = 0;
			}
		} while (c != -1);
		buffer.append(new String(dataAr, 0, k - 1, "UTF-8"));
		return buffer.toString();
	}
	//有中文的config要用這個
	public static HashMap getUTF8ConfigMap(String fileName) {
		HashMap<String, String> configMap = new HashMap<String, String>();
		String[] cfAr = ReadConfigAllData(fileName).split("\n");
		for (int i = 0; i < cfAr.length; i++) {
			cfAr[i] = cfAr[i].trim();
			if (cfAr[i].startsWith("//") || cfAr[i].indexOf("=") < 0 || cfAr[i].indexOf("=") < 0) {
				continue;
			}
			String key = cfAr[i].split("=")[0].trim();
			String value = cfAr[i].split("=")[1].trim();
			configMap.put(key, value);
		}
		return configMap;
	}
	
	
	
	//Config
	private static String ReadConfigAllData(String fileName) {
		StringBuffer bs = new StringBuffer();

		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName + ".conf"));
			while ((line = br.readLine()) != null) {
				bs.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bs.toString();
	}

}
