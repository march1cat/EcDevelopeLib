package ecserver.log;

import java.util.HashMap;
import java.util.Map;

import ec.log.ExceptionLogger;


public class ChannelExceptionLogger {

	private static ChannelExceptionLogger channelLog = null;
	private Map<String,ExceptionLogger> channlLogs =  null;
	
	
	public ExceptionLogger getLogger(String channelName){
		return channlLogs != null ? channlLogs.get(channelName) : null;
	}
	
	public void addLogger(String channelName,String exceptionFileStorageUri,String logEncode){
		if(channlLogs == null) channlLogs = new HashMap<>();
		channlLogs.put(channelName, new ExceptionLogger(exceptionFileStorageUri,logEncode));
	}
	//==========================================================================================================================
	
	public static void registerChannel(String channelName,String exceptionFileStorageUri,String logEncode){
		if(channelLog == null) channelLog = new ChannelExceptionLogger();
		channelLog.addLogger(channelName, exceptionFileStorageUri, logEncode);
	}
	
	public static ExceptionLogger getChannel(String channelName){
		if(channelLog != null){
			return channelLog.getLogger(channelName);
		} else return null;
	}
	
}
