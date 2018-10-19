package ec.file;

import java.io.File;
import java.io.IOException;

public class LinuxShellExecuter extends WindowsBatchExecuter{

	public LinuxShellExecuter(String batchFileUri) {
		super(batchFileUri);
	}
	
	
	public LinuxShellExecuter(String batchFileUri, boolean doClean) {
		super(batchFileUri, doClean);
	}

	
	
	

	@Override
	public void createUTF8BatchFile() {}


	@Override
	public void execute() {
		if(hasContent){
			File btFile = new File(batchFileUri());
			if(btFile.exists()){
				log("Prepare Process Shell File[" + batchFileUri() + "]");
				try {
		        	Runtime runtime = Runtime.getRuntime();
		        	Process p1 = runtime.exec("nohup sh " + batchFileUri() + " > sub_nohup.txt &");
				} catch (Exception e) {
					this.exportExceptionText(e);
				}
			}
		}
	}
	
	
}
