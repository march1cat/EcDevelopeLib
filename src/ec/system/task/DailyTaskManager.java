package ec.system.task;

import ec.string.StringManager;


public class DailyTaskManager extends TaskManager{
	
	
	private String dailyTime = null;
	private String lastFinishJobDay = null;
	private DailyTaskProcessor taskProcessor = null;
	
	public DailyTaskManager(String dailyTime){
		//16:35
		this.dailyTime = dailyTime;
	}
	
	
	@Override
	protected void beforeRunning() {
		log(name() + " is starting,Now Time = " + StringManager.getSystemDate() + ",Activate task on " + dailyTime + " every day",Module.TASK);
	}


	@Override
	protected boolean activateCheckPoint() {
		String nowTime = StringManager.getSystemDate("HH:mm");
		if(dailyTime.compareTo(nowTime) <= 0){
			if(lastFinishJobDay == null){
				return true;
			} else {
				String today = StringManager.getSystemDate("yyyy-MM-dd");
				return !today.equals(lastFinishJobDay);
			}
		} else return false;
	}
	@Override
	protected void finishJob(){
		log("Finish Daily Task Trigger Job For [" + name() + "],Now = " + StringManager.getSystemDate(),Module.TASK);
		lastFinishJobDay = StringManager.getSystemDate("yyyy-MM-dd");
	}
	
	@Override
	protected void processTasks() {
		if(taskProcessor != null  && !taskProcessor.isProcessOver()) {
			new Thread(taskProcessor).stop();
			log("Daily Task Processer For [" + name() + "] is not working,force done!!");
		}
		log("Trigger Daily Task Processer!! For [" + name() + "]");
		taskProcessor = new DailyTaskProcessor(taskLst, this);
		taskProcessor.startRunner();
	}


	@Override
	public String name() {
		return name != null ? name : "Daily Task Manager";
	}
	
	
	
	
}
