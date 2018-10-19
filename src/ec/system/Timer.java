package ec.system;

public class Timer extends Runner{

	private TimerInvoker invoker = null;
	private int countingSecond = 5;
	
	public Timer(TimerInvoker invoker){
		this.invoker = invoker;
	}
	
	public Timer(TimerInvoker invoker,int countingSecond){
		this.invoker = invoker;
		this.countingSecond = countingSecond;
	}

	
	@Override
	protected void beforeRunning() {
		invoker.timerStartCounting();
	}

	@Override
	protected void running() {
		int nowCount = 0;
		do{
			threadHold(1000);
			nowCount++;
			invoker.timerCounting(nowCount);
		} while(countingSecond == 0 || nowCount < countingSecond);
		this.stopRunner();
	}

	@Override
	protected void end() {
		invoker.timerEnd();
	}
}
