package ec.system;

public abstract class Runner extends Basis implements Runnable{
	
	private boolean isRun = false;
	protected Thread thread = null;
	
	private String threadName = null;
	
	
	public void startRunner(){
		isRun = true;
		thread = new Thread(this);
		if(threadName != null) thread.setName(threadName);
		thread.start();
	}
	
	public void stopRunner(){
		isRun = false;
	}
	
	@Override
	public void run() {
		try{
			beforeRunning();
			while(isRun){
				try{
					running();
				} catch(Exception e){
					this.exportExceptionText(e);
				}
			}
			end();	
		} catch(Exception eAll){
			this.exportExceptionText(eAll);
		}
	}
	
	protected void beforeRunning(){
		//Wait to be override
	}
	
	protected void running(){
		//Wait to be override
	}
	
	protected void end(){
		//Wait to be override
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	
	
	
	
}
