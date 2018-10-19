package ec.string;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringManager {
	
	public static String formateDouble(double sumRate) {
		String resultStr = null;
		DecimalFormat df = new DecimalFormat("#.##");
		resultStr = df.format(sumRate);
		return resultStr;
	}
	//回傳字串的
	public static List<List<String>> RegSearch(String targetStr, String regStr, boolean isPrintGroupZero) {
		List<List<String>> resultArList = new ArrayList<List<String>>();
		Pattern pattern = Pattern.compile(regStr);
		Matcher match = pattern.matcher(targetStr);
		boolean isFind = match.find();
		while (isFind) {
			List dataList = new ArrayList();
			for (int i = 0; i <= match.groupCount(); i++) {
				if(isPrintGroupZero == false && i == 0){
					continue;
				}
				if(dataList == null) dataList = new ArrayList();
				dataList.add(match.group(i));
			}
			if(dataList!=null && dataList.size() > 0) resultArList.add(dataList);
			
			if (match.end() + 1 <= targetStr.length()) {
				isFind = match.find(match.end());
			} else {
				break;
			}
		}
		return resultArList;
	}
	
	public static long getSecondAmt(String timeFormat, String nowTimeRegExp) {
		long secAmt = 0;
		timeFormat = timeFormat.replace(nowTimeRegExp, ":");
		String[] tmpAr = timeFormat.split(":");
		int hour = Integer.parseInt(tmpAr[0]);
		int minute = Integer.parseInt(tmpAr[1]);
		int second = Integer.parseInt(tmpAr[2]);
		secAmt = hour * 60 * 60 + minute * 60 + second;
		return secAmt;
	}

	public static String getNowDate(String sepStr, String timeZone) {
		if (timeZone == null)
			timeZone = "GMT+8:00";
		Calendar ca = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		String dateStr = String.format("%1$tY" + sepStr + "%1$tm" + sepStr
				+ "%1$td", ca);
		return dateStr;
	}

	public static String getNowTimeStr(String sepStr, String timeZone) {
		if (timeZone == null)
			timeZone = "GMT+8:00";
		Calendar ca = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
		String timeStr = String.format("%1$tH" + sepStr + "%1$tM" + sepStr
				+ "%1$tS", ca);
		return timeStr;
	}
	
	public static String getSystemDate(){
        return getSystemDate("yyyy-MM-dd HH:mm:ss.ms");
	}
	public static String getSpecificDate(Long dateTimeLong){
        return getSpecificDate("yyyy-MM-dd HH:mm:ss.ms",dateTimeLong);
	}
	
	public static String getSystemDate(String format){
		DateFormat dateFormat = new SimpleDateFormat(format);  
        Date date = new Date();  
        return dateFormat.format(date);
	}
	
	public static String getSpecificDate(String format,Long dateTimeLong){
		DateFormat dateFormat = new SimpleDateFormat(format);  
        Date date = new Date(dateTimeLong);  
        return dateFormat.format(date);
	}
	
	public static long getDateFromToday(long dayUsed){
		Date date = new Date();
		long days = (date.getTime() - dayUsed) / (1000 * 24 * 60 * 60);
		return Math.abs(days);
	}
	
	public static String ConverDateToStr(Date date) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
		return sdf.format(date);
	}
	
	public static String ConverDateToStr(String format,Date date) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static Date ConverStrToDate(String data) throws ParseException{
		return ConverStrToDate("yyyy-MM-dd HH:mm:ss.SSSS",data);
	}
	
	public static Date ConverStrToDate(String format,String data) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = sdf.parse(data);
		return date;
	}
	
	public static boolean validateDateSensable(String dateFormat,String dateText){
		try{
			SimpleDateFormat dFormat = new SimpleDateFormat(dateFormat); 
			dFormat.setLenient(false);
			dFormat.parse(dateText); 
			return true;
		} catch(Exception e){
			return false;
		}
	}
	
	public static int getWeekDayToday(){
		Calendar c = Calendar.getInstance();
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		dayOfWeek--;
		if(dayOfWeek == 0) dayOfWeek = 7;
		return dayOfWeek;
	}
	
	public static int parsingDateToWeekDay(String format,String dateData) throws ParseException{
		Calendar c = Calendar.getInstance();
		c.setTime(ConverStrToDate(format,dateData));
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		dayOfWeek--;
		if(dayOfWeek == 0) dayOfWeek = 7;
		return dayOfWeek;
	}
	
	public static String transUnicodeToString(String unicodeText) throws Exception{
		String str = unicodeText.replace("\\","");
		String[] arr = str.split("u");
		StringBuffer text = new StringBuffer();
		for(int i = 1; i < arr.length; i++){
		    int hexVal = Integer.parseInt(arr[i], 16);
		    text.append((char)hexVal);
		}
		return text.toString().length() > 0 ? text.toString() : unicodeText;
	}
	
	public static void openUrlInBrowser(String url){
		try{
			String os = System.getProperty("os.name").toLowerCase();
			if (os.indexOf("win") >= 0){
				Runtime.getRuntime().exec("cmd /c start " + url);
			} else if (os.indexOf("mac") >= 0){
				Runtime.getRuntime().exec("open " + url);
			}			
		} catch(Exception e){}
	}
	//Transfer pure DateText[20160901] to DateText[2016/09/01] or DateText[2016-09-01]
	public static String insertSepCharIntoDateText(String orgDateText,String preferFormat){
		List<Integer> indexs = null;
		String sepChar = null;
		for(int i = 0;i < preferFormat.length();i++){
			String chr = preferFormat.substring(i, i+1);
			if("-".equals(chr) || "/".equals(chr)) {
				if(indexs == null) indexs = new ArrayList<>();
				indexs.add(i);
				if(sepChar == null) sepChar = chr;
			}
		}
		if(sepChar != null && indexs != null && orgDateText.length() + indexs.size() == preferFormat.length()){
			StringBuffer t = new StringBuffer();
			for(int i = 0; i < indexs.size();i++){
				if(i == 0) t.append(orgDateText.substring(0,indexs.get(i)) + sepChar);
				if(i != indexs.size() - 1) t.append(orgDateText.substring(indexs.get(i), indexs.get(i + 1) - 1) + sepChar);
				else {
					if(i != 0) t.append(orgDateText.substring(indexs.get(i) - 1));
					else t.append(orgDateText.substring(indexs.get(i)));
				}
			}
			return t.toString();
		} else return orgDateText;
	}
	
	public static String md5(String str) {
	    String md5=null;
	    try {
		      MessageDigest md=MessageDigest.getInstance("MD5");
		      byte[] barr=md.digest(str.getBytes());  //將 byte 陣列加密
		      StringBuffer sb=new StringBuffer();  //將 byte 陣列轉成 16 進制
		      for (int i=0; i < barr.length; i++) {sb.append(byte2Hex(barr[i]));}
		      String hex=sb.toString();
		      md5=hex.toUpperCase(); //一律轉成大寫
	    } catch(Exception e) {e.printStackTrace();}
	    	return md5;
	 }
	  private static String byte2Hex(byte b) {
		 String[] h={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
		 int i=b;
		 if (i < 0) {i += 256;} 
		 return h[i/16] + h[i%16];
	 }
	  

	
}
