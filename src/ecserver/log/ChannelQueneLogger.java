package ecserver.log;

import java.util.HashMap;
import java.util.Map;

import ec.log.QueneLogger;

public class ChannelQueneLogger {

	public static ChannelQueneLogger channelLog = null;
	
	private Map<String,QueneLogger> channlLogs =  null;
	
	public QueneLogger getLogger(String channelName){
		return channlLogs != null ? channlLogs.get(channelName) : null;
	}
	
	public void addLogger(String channelName,String logFileUri,String logEncode){
		if(channlLogs == null) channlLogs = new HashMap<>();
		channlLogs.put(channelName, new QueneLogger(logFileUri,logEncode));
	}
	//==========================================================================================================================
	
	public static void registerChannel(String channelName,String logFileUri,String logEncode){
		if(channelLog == null) channelLog = new ChannelQueneLogger();
		channelLog.addLogger(channelName, logFileUri, logEncode);
	}
	
	public static QueneLogger getChannel(String channelName){
		if(channelLog != null){
			return channelLog.getLogger(channelName);
		} else return null;
	}

}
