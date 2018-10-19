package ec.system;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

public abstract class ServiceEntry extends Basis implements Daemon{
	
	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}


	@Override
	public void init(DaemonContext arg0) throws DaemonInitException, Exception {
		
	}


	@Override
	public void stop() throws Exception {
		if (service != null)
			service.shutdown();
	}


	@Override
	public void start() throws Exception {
		serviceStartAction();
	}
	
	protected abstract void serviceStartAction();

}
