package ec.system;

public interface TimerInvoker {

	public void timerStartCounting();
	public void timerCounting(int nowCount);
	public void timerEnd();
	
}
