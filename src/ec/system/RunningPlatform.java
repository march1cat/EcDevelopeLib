package ec.system;

public class RunningPlatform {

	public enum RunningMode {
		PRODUCTION,DEBUG
	}
	public enum RunningOs {
		WINDOWS,LINUX_BASE
	}
	
	public static Object runningMode = RunningMode.PRODUCTION;
	public static Object runningOs = null;
	
	public static boolean isInProductionMode(){
		return runningMode == RunningMode.PRODUCTION;
	}
	
	public static boolean isInDebugMode(){
		return runningMode == RunningMode.DEBUG;
	}
	
	public static void initialRunningOS(){
		try{
			String os = System.getProperty("os.name").toLowerCase();
			if (os.indexOf("win") >= 0){
				runningOs = RunningOs.WINDOWS;
			} else if (os.indexOf("mac") >= 0){
				runningOs = RunningOs.LINUX_BASE;
			} else if (os.indexOf("linux") >= 0){
				runningOs = RunningOs.LINUX_BASE;
			}			
			if(runningOs == null) {
				System.err.println("Notice OS Fail,Property(os.name) = " + os);
			}
		} catch(Exception e){e.printStackTrace();}
	}
	
	public static boolean isWindowsBase(){
		return runningOs == RunningOs.WINDOWS;
	}
	
	public static boolean isLinuxBase(){
		return runningOs == RunningOs.LINUX_BASE;
	}
	
}
