package ec.string;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import ec.file.FileManager;

public class StringTool {

	private static Map T2S = null;
	private static Map S2T = null;
	private static String codeFileUri = "簡繁字碼轉換表.txt";
	
	public static void setTSWordingMappingFile(String fileUri){
		codeFileUri = fileUri;
	}
	
	private static void loadTsWordingToSWord() throws Exception{
		String content = FileManager.ReadTextInSpCode(codeFileUri,"UTF-8",1024);
		String[] dataAr = content.split(",");
		char[] UTF8T = null;
		char[] UTF8S = null;
		for(int i = 0;i<2;i++){
			String data = dataAr[i];
			if(i == 1) {
				UTF8T = data.trim().toCharArray();
			} else {
				UTF8S = data.trim().toCharArray();
			}
		}
		T2S = new HashMap();
		S2T = new HashMap();
		for (int i = 0, n = Math.min(UTF8T.length, UTF8S.length); i < n; i++) {
			final Character cT = Character.valueOf(UTF8T[i]);
			final Character cS = Character.valueOf(UTF8S[i]);
			T2S.put(cT, cS);
			S2T.put(cS, cT);
		}

	}
	
	public static String transStoT(String sdata) throws Exception{
		if(T2S == null || S2T == null) loadTsWordingToSWord();
		StringBuffer dataBuffer = new StringBuffer("");
		char[] UTF8S = sdata.toCharArray();
		for(char schar : UTF8S){
			if(S2T.get(schar) != null){
				dataBuffer.append(S2T.get(schar));
			} else {
				dataBuffer.append(schar);
			}
		}
		return dataBuffer.toString();
	}
	
	public static String transTtoS(String tdata) throws Exception{
		if(T2S == null || S2T == null) loadTsWordingToSWord();
		StringBuffer dataBuffer = new StringBuffer("");
		char[] UTF8T = tdata.toCharArray();
		for(char tchar : UTF8T){
			if(T2S.get(tchar) != null){
				dataBuffer.append(T2S.get(tchar));
			} else {
				dataBuffer.append(tchar);
			}
		}
		return dataBuffer.toString();
	}
	
	
}
