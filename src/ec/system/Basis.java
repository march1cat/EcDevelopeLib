package ec.system;

import java.util.Date;
import java.util.List;

import ec.log.ExceptionLogger;
import ec.log.QueneLogger;
import ec.string.StringManager;


public abstract class Basis {

	public enum Module{
		NORMAL,MIDDLE_WARE,SYSTEM,COMMANDER,ANNOUCE,TASK,MODULE_START,EC_LIB
	}
	
	private QueneLogger logger = null;
	private ExceptionLogger exceptLogger = null;
	
	protected QueneLogger generateLogger(){
		return QueneLogger.getLogger();
	}
	protected ExceptionLogger generateExceptionLogger(){
		return ExceptionLogger.getLogger();
	}
	
	
	public void log(String data){
		data = "<Log> <Normal> <"+getClass().getName()+"> " + StringManager.getSystemDate() + " ------> " + data;
		if(DeveloperMode.isON()) System.out.println(data);
		if(logger == null) logger = generateLogger();
		if(logger != null) logger.log(data);
	}
	public void log(String data,Object module){
		String moduleText = "Normal"; 
		if(module == Module.MIDDLE_WARE){
			moduleText  = "MiddleWare"; 
		} else if(module == Module.COMMANDER){
			moduleText  = "Command"; 
		} else if(module == Module.SYSTEM){
			moduleText  = "System";
		} else if(module == Module.ANNOUCE){
			moduleText  = "Annouce";
		} else if(module == Module.TASK){
			moduleText  = "Task";
		} else if(module == Module.MODULE_START){
			moduleText  = "ModuleStart";
		} else if(module == Module.EC_LIB){
			moduleText  = "EcLibrary";
		}
		data = "<Log> <" + moduleText + ">  <"+getClass().getName()+"> " + StringManager.getSystemDate() + " ------> " + data;
		if(DeveloperMode.isON()) System.out.println(data);
		if(logger == null) logger = generateLogger();
		if(logger != null) logger.log(data);
	}
	
	public void except(String data){
		data = "<Error> " + StringManager.getSystemDate() + " (At " + getClass().getName() + ")" + " ------> " + data;
		if(DeveloperMode.isON()) System.out.println(data);
		if(logger == null) logger = generateLogger();
		if(logger != null) logger.log(data);
	}
	
	public void except(String data,String classname){
		data = "<Error> " + StringManager.getSystemDate() + " (At " + classname + ")" + " ------> " + data;
		if(DeveloperMode.isON()) System.out.println(data);
		if(logger == null) logger = generateLogger();
		if(logger != null) logger.log(data);
	}
	
	public void exportExceptionText(Exception e){
		if(exceptLogger == null) exceptLogger = generateExceptionLogger();
		if(exceptLogger != null) {
			exceptLogger.writeException(e);
		} else e.printStackTrace();
	}
	
	
	public void warn(String data,String classname){
		data = "<Warn> " + StringManager.getSystemDate() + " (At " + classname + ")" + " ------> " + data;
		if(DeveloperMode.isON()) System.out.println(data);
		if(logger == null) logger = generateLogger();
		if(logger != null) logger.log(data);
	}
	
	protected boolean compareValue(String value1,String value2){
		if(value1 == null || value2 == null){
			return value1 == value2;
		} else {
			return value1.trim().toUpperCase().equals(value2.trim().toUpperCase());
		}
	}
	
	protected boolean compareValueIn(String value,String[] values){
		if(value == null || values == null){
			return false;
		} else {
			boolean isMatch = false;
			for(String v : values){
				if(compareValue(value,v)) {
					isMatch = true;
					break;
				}
			}
			return isMatch;
		}
	}
	
	
	protected String getNotEmptyValue(Object data,String nvsl){
		if(data == null) return nvsl;
		return data.toString().trim();
	}
	
	protected int parseInt(String text,int errorValue){
		try {
			return Integer.parseInt(text);
		} catch(Exception e){
			return errorValue;
		}
	}
	
	protected double parseDouble(String text,double errorValue){
		try {
			return Double.parseDouble(text);
		} catch(Exception e){
			return errorValue;
		}
	}
	
	protected boolean isTextInt(String text){
		try {
			Integer.parseInt(text);
			return true;
		} catch(Exception e){
			return false;
		}
	}
	
	protected Date parsingToDate(String data) throws Exception{
		try {
			String format = (data.indexOf("T") >= 0) ? "yyyy-MM-dd'T'HH:mm:ss.SSS" : "yyyy-MM-dd HH:mm:ss.SSS";
			Date date = StringManager.ConverStrToDate(format, data);
			return date;
		} catch (Exception e) {
			except("Parsing Data To Date Error ,Data is " + data + ", Error is " + e.getMessage(), this.getClass().getName());
			throw e;
		}
	}
	
	protected Date parsingToDate(String data,String format) throws Exception{
		try {
			Date date = StringManager.ConverStrToDate(format, data);
			return date;
		} catch (Exception e) {
			except("Parsing Data To Date Error ,Data is " + data + ", Error is " + e.getMessage(), this.getClass().getName());
			throw e;
		}
	}
	
	protected boolean timeDiffInInterval(Date time1,Date time2,long interval){
		return (Math.abs(time1.getTime() - time2.getTime()) <=  interval);
	}
	
	protected boolean threadHold(long holdTime){
		try {
			Thread.sleep(holdTime);
			return true;
		} catch (InterruptedException e) {
			return false;
		}
	}
	
	
	protected boolean isListWithContent(List list){
		return list != null && !list.isEmpty();
	}
	
	protected boolean isTextStartWith(String data,String startWithText){
		if(data != null && startWithText != null){
			return data.trim().toUpperCase().startsWith(startWithText.trim().toUpperCase());
		} else return false;
	}
	
	protected boolean isTextEndWith(String data,String endWithText){
		if(data != null && endWithText != null){
			return data.trim().toUpperCase().endsWith(endWithText.trim().toUpperCase());
		} else return false;
	}
	
	protected String trimEndChar(String data,String trimTargetChar){
		if(data.length() > 0)  return data.toUpperCase().endsWith(trimTargetChar.toUpperCase()) ? data.substring(0, data.length() - trimTargetChar.length()) : data;
		else return data;
	}


	public void setLogger(QueneLogger logger) {
		this.logger = logger;
	}

	public QueneLogger getLogger() {
		return logger;
	}

	protected String[] trimSplit(String data, String splitChar){
		String[] ar = data.split(splitChar);
		for(int i = 0 ; i < ar.length; i++){
			ar[i] = ar[i].trim();
		}
		return ar;
	}
	
	
}
