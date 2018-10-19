package ec.system;

public class DeveloperMode {

	private static boolean isOn = false;
	
	public static void switchOn(){
		isOn = true;
		RunningPlatform.runningMode = RunningPlatform.RunningMode.DEBUG;
	}
	public static void switchOff(){
		isOn = false;
	}
	
	
	public static boolean isON(){
		return isOn == true;
	}
	public static boolean isOFF(){
		return isOn == false;
	}
}
