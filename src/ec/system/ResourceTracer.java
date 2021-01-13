package ec.system;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import ec.file.FileManager;
import ec.string.StringManager;
import ec.system.Basis.Module;

public class ResourceTracer extends Runner{

	private static ResourceTracer tracer = null;
	
	private String recordFileUri = null;
	private Runtime runtime = null;
	private NumberFormat format = null;
	private long traceDelayTimeBetweenActions = 30;
	
	private ResourceTracer(String recordFileUri){
		this.recordFileUri = recordFileUri;
	}
	
	private ResourceTracer(String recordFileUri,long traceDelayTimeBetweenActions){
		this.recordFileUri = recordFileUri;
		this.traceDelayTimeBetweenActions = traceDelayTimeBetweenActions;
	}
	
	@Override
	protected void running() {
		writeDownMemoryValue();
		threadHold(traceDelayTimeBetweenActions * 60 * 1000);
	}
	
	private void writeDownMemoryValue(){
		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		
		output(
				format.format(maxMemory / 1024) , 
				format.format(freeMemory / 1024) , 
				format.format(allocatedMemory / 1024) , 
				format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024)
		);
	}
	
	protected void output(String maxMemory , String freeMemory , String allocatedMemory , String totalFreeMemory) {
		record(
				"MaxMemery : " + maxMemory + "; " + 
				"FreeMemery : " + freeMemory + "; " + 
				"AllocatedMemory : " +  allocatedMemory + "; " + 
				"Total Free memory: " + totalFreeMemory);
	}

	@Override
	protected void beforeRunning() {
		if(runtime == null) runtime = Runtime.getRuntime();
		if(format == null) format = NumberFormat.getInstance();
		File recordF = new File(recordFileUri);
		if(recordF.exists() && !recordF.isDirectory()) {
			try {
				FileManager.copyFile(recordFileUri, recordFileUri + ".bc." + StringManager.getSystemDate("yyyyMMddHH:mm"));
				recordF.delete();
			} catch (IOException e) {
				this.except("Backup Old Resource Tracer File Fail,Error = " + e.getMessage(), this.getClass().getName());
				this.exportExceptionText(e);
			}
		}
		log("System Resource Tracer is Starting!!,Record Once Per " + traceDelayTimeBetweenActions + "Min.",Module.EC_LIB);
		record("System Resource Tracer is Starting!!");
	}

	@Override
	public void end() {
		writeDownMemoryValue();
		log("System Resource Tracer is Ending!!",Module.EC_LIB);
		record("System Resource Tracer is Ending!!");
		tracer = null;
	}
	
	private void record(String data){
		FileManager.bufferWrite(recordFileUri, "<ResourceTracker> " + StringManager.getSystemDate() + " ------> " + data + "\r\n");
	}
	
	
	public static ResourceTracer initial(String recordFileUri){
		if(tracer != null) {
			tracer.stopRunner();
		}
		tracer = new ResourceTracer(recordFileUri);
		return tracer;
	}
	
	public static ResourceTracer initial(String recordFileUri,long traceDelayTimeBetweenActions){
		if(tracer != null) {
			tracer.stopRunner();
		}
		tracer = new ResourceTracer(recordFileUri,traceDelayTimeBetweenActions);
		return tracer;
	}


	public static ResourceTracer instance(){
		return tracer;
	}
	
	
}
