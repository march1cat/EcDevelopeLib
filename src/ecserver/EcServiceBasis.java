package ecserver;

import ec.log.ExceptionLogger;
import ec.log.QueneLogger;
import ec.system.Basis;
import ecserver.log.ChannelExceptionLogger;
import ecserver.log.ChannelQueneLogger;

public abstract class EcServiceBasis extends Basis{
	
	@Override
	protected QueneLogger generateLogger() {
		return ChannelQueneLogger.getChannel(ServiceLoggerChanngelName());
	}
	@Override
	protected ExceptionLogger generateExceptionLogger(){
		return ChannelExceptionLogger.getChannel(ServiceLoggerChanngelName());
	}
	
	
	protected void registerLogger(String logFileUri,String logEncode){
		ChannelQueneLogger.registerChannel(ServiceLoggerChanngelName(), logFileUri, logEncode);
	}
	
	protected void registerExceptLogger(String exceptionFileStorageUri,String writeFileEnCode){
		ChannelExceptionLogger.registerChannel(ServiceLoggerChanngelName(), exceptionFileStorageUri, writeFileEnCode);
	}
	
	protected abstract String ServiceLoggerChanngelName();

}
