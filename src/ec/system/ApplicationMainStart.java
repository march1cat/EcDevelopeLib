package ec.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.log.ExceptionLogger;
import ec.log.QueneLogger;

public abstract class ApplicationMainStart extends Basis{
	
	protected boolean NoWriteLogToFileInDeveloperMode = true;
	private Map<String,String> startingParameters = null;
	private List<String> helps = null;
	
	public void applicationMain(String[] mainArgs){
		RunningPlatform.initialRunningOS();
		System.out.println("Notice System OS = " + RunningPlatform.runningOs);
		if(mainArgs != null && mainArgs.length > 0){
			for(String arg : mainArgs){
				if(compareValue(arg, "RunDeveloperMode")) DeveloperMode.switchOn();
			}
		}
		if(DeveloperMode.isON() && NoWriteLogToFileInDeveloperMode){
			QueneLogger.writeToFile = false;
			ExceptionLogger.writeToFile = false;
		}
		if(DeveloperMode.isON()){
			System.out.println("Now Run Developer Mode!!,Debug Mode is On!!");
			RunningPlatform.runningMode = RunningPlatform.RunningMode.DEBUG;
		}
		
		if(mainArgs.length > 0) assignStartParameters(mainArgs);
		
		if(isStartParameterEqual("help","1")){
			registerParameterHelp();
			printHelps();
		} else {
			if(applicationInitial()){
				if(isStartParameterEqual("debug","true")){
					RunningPlatform.runningMode = RunningPlatform.RunningMode.DEBUG;
				}
				startApplication(mainArgs);
				applicationMainEnd(mainArgs);
				
			} else applicationInitialFail(mainArgs);
		}
	}
	
	protected abstract void startApplication(String[] mainArgs);
	protected abstract void applicationInitialFail(String[] mainArgs);
	protected abstract boolean applicationInitial();
	
	protected void applicationMainEnd(String[] mainArgs){
		//Wait to override
	}
	protected void registerParameterHelp(){
		//Wait to override
	}
	
	protected void addParameterHelpDesc(String cmd,String desc){
		if(helps == null) helps = new ArrayList<>();
		if(cmd.startsWith("-")) helps.add(cmd + "=1 --> " + desc);
		else helps.add("-" + cmd + "=1 --> " + desc);
	}
	protected void addParameterHelpDesc(String cmd,String valFmt,String desc){
		if(helps == null) helps = new ArrayList<>();
		if(cmd.startsWith("-")) helps.add(cmd + "="+valFmt+" --> " + desc);
		else helps.add("-" + cmd + "=$value --> " + desc);
	}
	
	protected boolean mountConfiguration(ApplicationConfig configuration){
		return configuration.initial();
	}
	
	protected void mountSystemLogger(String logFileUri,String errorLogStorage,String fileEncode){
		QueneLogger.initial(logFileUri, fileEncode,true);
		ExceptionLogger.initial(errorLogStorage, fileEncode);
	}

	@Override
	public void log(String data) {
		super.log(data,Module.SYSTEM);
	}
	
	private void assignStartParameters(String[] args){
		for(String arg : args){
			if(arg.startsWith("-") && arg.indexOf("=") >= 0) {
				String[] kk = arg.substring(1).split("=");
				if(kk.length >= 2) {
					if(startingParameters == null) startingParameters = new HashMap<>();
					startingParameters.put(kk[0], kk[1]);
				}
			} else {
				if(compareValue(arg, "-h") || compareValue(arg, "-help")) {
					if(startingParameters == null) startingParameters = new HashMap<>();
					startingParameters.put("help", "1");
				}
			}
		}
		if(startingParameters != null) log("Application Start Parameter = " + startingParameters.toString());
		else log("There's no application start parameter");
	}
	
	protected String getStartParameter(String key){
		return startingParameters != null ? startingParameters.get(key) : null;
	}
	
	protected boolean isStartParameterEqual(String key,String val){
		String v = getStartParameter(key.toLowerCase());
		if(v != null) return this.compareValue(v, val);
		else return false;
	}
	
	private void printHelps(){
		if(isListWithContent(helps)) {
			for(String help : helps){
				System.out.println(help);
			}
		} else {
			System.out.println("No Helper Infomations!!");
		}
	}
}
