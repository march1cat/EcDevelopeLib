package ec.log;

import ec.string.StringManager;
import ec.system.Basis;

public class TimeInterval extends Basis {
	
	private String startDateTime = null;
	private String endDateTime = null;

	private long startTimes = 0;
	private long endTimes = 0;
	
	
	private String timeFormat = "yyyy-MM-dd HH:mm:ss";
	
	public TimeInterval() {
		this.init();
	}
	
	public TimeInterval(String timeFormat) {
		this.timeFormat = timeFormat;
		this.init();
	}
	
	private void init() {
		startDateTime = StringManager.getSystemDate(timeFormat);
		startTimes = StringManager.getCurrentTimeStamp().getTime();
	}
	
	
	public void end() {
		endDateTime = StringManager.getSystemDate(timeFormat);
		endTimes = StringManager.getCurrentTimeStamp().getTime();
	}
	
	public long getInterval() {
		return endTimes - startTimes;
	}

	public String getStartDateTime() {
		return startDateTime;
	}

	public String getEndDateTime() {
		return endDateTime;
	}
	
	
	
	
}
