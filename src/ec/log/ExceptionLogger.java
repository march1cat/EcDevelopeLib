package ec.log;

import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import ec.file.EcDirector;
import ec.file.FileManager;
import ec.string.StringManager;

public class ExceptionLogger implements Runnable {

	
	private static ExceptionLogger logger = null;
	
	
	private EcDirector storageDir = null;
	private Queue<Exception> logQuene = null;
	public static boolean writeToFile = true;
	public static boolean goPrintTrace = true;
	private boolean isRun = true;
	private String encode = null;
	
	public ExceptionLogger(String exceptionFileStorageUri,String encode){
		storageDir = new EcDirector(exceptionFileStorageUri,true);
		logQuene = new ConcurrentLinkedQueue<Exception>(); 
		this.encode = encode;
		(new Thread(this)).start();
	}
	
	public synchronized void writeException(Exception e){
		logQuene.add(e);
	}
	
	private synchronized Exception getLog(){
		if(logQuene.isEmpty()) return null;
		else return logQuene.poll();
	}
	
	
	@Override
	public void run() {
		while(isRun){
			while(!logQuene.isEmpty()){
				Exception error = getLog();
				if(error != null){
					if(goPrintTrace) error.printStackTrace();
					String exceptTime = StringManager.getSystemDate("yyyy-MM-dd HH_mm_ss");
					if(writeToFile) FileManager.bufferWrite(storageDir.Uri() + "Error_" + exceptTime + "." + pureExceptionType(error) + ".html",toExceptionHtmlDesc(exceptTime,error), encode);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	private String toExceptionHtmlDesc(String exceptTime,Exception error){
		StackTraceElement[] stAr = error.getStackTrace();
		StringBuffer html = new StringBuffer("");
		html.append("<html>");
		html.append("<head>");
		html.append("<meta HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=utf-8\">");
		html.append("</head>");
		html.append("<body>");
		html.append("<table border='1' align='left'>");
		
		
		html.append("<tr>");
		html.append("<th align='left'>");
		html.append("Exception Time:");
		html.append("</th>");
		html.append("<td>");
		html.append(exceptTime.replaceAll("_", ":"));
		html.append("</td>");
		html.append("</tr>");
		
		html.append("<tr>");
		html.append("<th align='left'>");
		html.append("Exception Type:");
		html.append("</th>");
		html.append("<td>");
		html.append(error.getClass().getName());
		html.append("</td>");
		html.append("</tr>");
		
		html.append("<tr>");
		html.append("<th align='left'>");
		html.append("Exception Message:");
		html.append("</th>");
		html.append("<td>");
		html.append(error.getMessage());
		html.append("</td>");
		html.append("</tr>");
		
		html.append("<tr>");
		html.append("<th colspan='2'>");
		html.append("Exception Cause Path:");
		html.append("</th>");
		html.append("</tr>");
		
		html.append("<tr>");
		html.append("<th colspan='2' align='left'>");
		for(StackTraceElement stkEle : stAr){
			html.append("at " + stkEle.getClassName() + "." + stkEle.getMethodName() + "("+stkEle.getFileName()+":"+stkEle.getLineNumber()+")");
			html.append("<br>");
		}
		html.append("</th>");
		html.append("</tr>");
		
		
		if(error instanceof SQLException){
			html.append("<tr>");
			html.append("<td colspan='2' align='left'>");
			html.append("Error SQL Text = " + ((SQLException)error).getSQLState());
			html.append("</td>");
			html.append("</tr>");
		}
		
		
		html.append("</table>");
		html.append("</body>");
		html.append("</html>");
		
		return html.toString();
	}
	
	private String pureExceptionType(Exception e) {
		String name = e.getClass().getName();
		String k = name.indexOf(".") >= 0 ? name.substring(name.lastIndexOf(".") + 1) : name;
		return k;
	}
	
	
	public static void initial(String exceptionFileStorageUri,String encode){
		if(logger == null) logger = new ExceptionLogger(exceptionFileStorageUri, encode);
	}
	public static synchronized ExceptionLogger getLogger(){
		return logger;
	}
}
