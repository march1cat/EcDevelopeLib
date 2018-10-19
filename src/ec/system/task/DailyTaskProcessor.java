package ec.system.task;

import java.util.List;

import ec.system.Runner;

public class DailyTaskProcessor extends Runner{

	protected List<Task> taskLst = null;
	private DailyTaskManager manager = null;
	private boolean isProcessOver = false;
	
	public DailyTaskProcessor(List<Task> taskLst,DailyTaskManager manager){
		this.taskLst = taskLst;
		this.manager = manager;
	}

	@Override
	protected void running() {
		for(Task task : taskLst){
			try{
				task.taskAction();
				manager.onCompleteTask(task);
				threadHold(manager.taskIntervalDelay);
			} catch(Exception e){
				this.except("Notice Error when activated task for ["+manager.name()+"],Error="+e.getMessage(), this.getClass().getName());
				this.exportExceptionText(e);
			}
		}
		this.stopRunner();
	}

	@Override
	protected void end() {
		isProcessOver = true;
	}

	public boolean isProcessOver() {
		return isProcessOver;
	}
	
	
	
}
