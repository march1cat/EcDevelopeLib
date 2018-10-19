package ec.log;

import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import ec.file.EcDirector;
import ec.file.FileManager;
import ec.string.StringManager;
import ec.system.DeveloperMode;

public class QueneLogger implements Runnable {

	public static boolean writeToFile = true;
	private static QueneLogger logger = null;
	private Queue<String> logQuene = null;
	private boolean isRun = true;
	private String logFileUri = null;
	private String encode = null;
	
	
	public static boolean storageMode = false;
	public static int storageRecordAmount = 2000;
	private static int writeRecordCnt = 0;
	
	public QueneLogger(String logFileUri,String encode){
		this.logFileUri = logFileUri;
		if(logFileUri.lastIndexOf("/") >= 0) {
			String holdPos = logFileUri.substring(0, logFileUri.lastIndexOf("/"));
			EcDirector dir = new EcDirector(holdPos, true);
		}
		this.encode = encode;
		logQuene = new ConcurrentLinkedQueue<String>(); 
		(new Thread(this)).start();
	}
	
	public synchronized void log(String data){
		logQuene.add(data);
	}
	
	private synchronized String getLog(){
		if(logQuene.isEmpty()) return null;
		else return logQuene.poll();
	}
	
	@Override
	public void run() {
		while(isRun){
			try {
				while(!logQuene.isEmpty()){
					String log = getLog();
					if(log != null){
						if(DeveloperMode.isOFF()) System.out.println(log);
						if(writeToFile) FileManager.bufferWrite(logFileUri, log + "\r\n", encode);
						if(writeToFile && storageMode){
							writeRecordCnt++;
							if(writeRecordCnt >= storageRecordAmount){
								try {
									File f = new File(logFileUri);
									if(f.exists()){
										FileManager.copyFile(logFileUri, logFileUri + "." + StringManager.getSystemDate("yyyyMMdd'T'HHmmdd"));
										f.delete();
									}
								} catch (Exception e) {
									if(writeToFile) FileManager.bufferWrite(logFileUri,  "Backup Log File Fail,Error = " + e.getMessage() +  "\r\n", encode);
								}
								writeRecordCnt = 0;
							}
						}
					}
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static synchronized void initial(String logFileName,String logEncode){
		initial(logFileName,logEncode,false);
	}
	
	public static synchronized void initial(String logFileName,String logEncode,boolean doClearOld){
		if(doClearOld && DeveloperMode.isOFF()){
			File f = new File(logFileName);
			if(f.exists()){
				try {
					FileManager.copyFile(logFileName, logFileName + ".InitialBackup." + StringManager.getSystemDate("yyyyMMdd'T'HHmmdd"));
					f.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(logger == null) logger = new QueneLogger(logFileName,logEncode);
	}
	
	public static synchronized QueneLogger getLogger(){
		return logger;
	}
	
	
}
