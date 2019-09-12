package ec.system;

import java.util.Set;

public class ThreadTracer extends Runner {
	
	
	public final static String threadName = "EcThreadTracer";
	private long checkDistance = 10;
	private String[] tNames = null;
	
	public ThreadTracer() {
		this.setThreadName(threadName);
	}
	
	public void setMonitorThreads(String[] tNames) {
		this.tNames = tNames;
	}
	
	@Override
	protected void beforeRunning() {
		super.beforeRunning();
	}

	@Override
	protected void running() {
		try {
			log("Start check threads!!");
			Set<Thread> threadSet = Thread.getAllStackTraces().keySet();			
			boolean isAllOnProcessing = true;
			boolean isOnProcessing = false;
			if(tNames != null && tNames.length > 0) {
				for(String tName : tNames) {
					for(Thread t : threadSet) {
						if(this.compareValue(t.getName(), tName)) {
							isOnProcessing = true;
							break;
						}
					}
					if (!isOnProcessing) {
						log("Monitor thread["+tName+"] not exist, prepare force shutdown system!!");
						this.exportExceptionText(new UnExpectThreadDownException("Critical Thread["+tName+"] doesn't exist in jvm thread list!!"));
						this.stopRunner();
						isAllOnProcessing = false;
						break;
					}
				}
			}
			if(isAllOnProcessing) {
				log("Thread check ok!!");
			}
		} catch(Exception e) {
			this.exportExceptionText(e);
		}
		this.threadHold(checkDistance * 60 * 1000);
	}

	@Override
	protected void end() {
		System.out.println("System prepare shutdown due to unexpect thread down!!");
		this.threadHold(5 * 1000);
		System.exit(1);
	}

	public void setCheckDistance(long checkDistanceInMinute) {
		this.checkDistance = checkDistance;
	}
}
