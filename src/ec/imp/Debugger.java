package ec.imp;

import java.util.Iterator;
import java.util.Map;

import ec.file.FileManager;

public class Debugger {

	
	private static String parsingMap(Map dataMp){
		if(dataMp == null) return "";
		String result = "";
		Iterator iter = dataMp.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next().toString();
			String value = dataMp.get(key) != null ? dataMp.get(key).toString() : "null";
			result += key + "=" + value + "&";
		}
		if(result.endsWith("&"))result = result.substring(0,result.length() - 1);
		return result;
	}
	
	
	public static void printMap(Map dataMp){
		String data = parsingMap(dataMp);
		System.out.println(data);
	}
	
	
	public static void printMap(Map dataMp,String logFile) throws Exception{
		String data = parsingMap(dataMp);
		System.out.println(data);
		FileManager.FileWrite(logFile, data);
	}
	
	
	
}
