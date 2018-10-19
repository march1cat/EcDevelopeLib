package ec.string;

public class Obfucator {

	public static String smoothEncode(String orgData,int movingAmount){
		StringBuffer resultData = new StringBuffer("");
		for(char k : orgData.toCharArray()){
			byte t = (byte)k;
			t = (byte) (t + movingAmount);
			resultData.append((char)t);
		}
		return resultData.toString();
	}
	
	public static String smoothDecode(String encodeData,int recoverAmount){
		StringBuffer resultData = new StringBuffer("");
		for(char k :encodeData.toCharArray()){
			byte t = (byte)k;
			t = (byte) (t - recoverAmount);
			resultData.append((char)t);
		}
		return resultData.toString();
	}
	
	
}
