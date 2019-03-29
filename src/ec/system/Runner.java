package ec.system;

public abstract class Runner extends Basis implements Runnable{
	
	private boolean isRun = false;
	protected Thread thread = null;
	
	
	public void startRunner(){
		isRun = true;
		thread = new Thread(this);
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
}
