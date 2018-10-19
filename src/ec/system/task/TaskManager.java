package ec.system.task;

import java.util.ArrayList;
import java.util.List;

import ec.string.StringManager;
import ec.system.Runner;
import ec.system.Basis.Module;

public class TaskManager extends Runner{

	private long actionDelay = 3000;
	protected long taskIntervalDelay = 1000;
	protected List<Task> taskLst = null;
	private List<TaskActivatedValidationStep> validationSteps = null;
	protected String name = null;
	private boolean doLogStartRunTaskMessage = true;
	
	public TaskManager(){
		
	}
	@Override
	protected void beforeRunning() {
		log(name() + " is Starting,Now Time = " + 
				StringManager.getSystemDate() + 
				" and Action Delay Time = " + actionDelay + 
				", Delay Between Tasks = " + taskIntervalDelay,Module.TASK);
	}
	
	protected void running() {
		try{
			if(activateCheckPoint() && validateBySettingSteps()){
				activateTask();
				finishJob();
			}
		} catch(Exception e){
			this.exportExceptionText(e);
		}
		threadHold(actionDelay);
	}
	
	private boolean validateBySettingSteps(){
		if(validationSteps != null && validationSteps.size() > 0){
			for(TaskActivatedValidationStep step : validationSteps){
				if(!step.validate()) return false;
			}
		}
		return true;
	}
	
	public void addValidateExecuteStep(TaskActivatedValidationStep step){
		if(validationSteps == null) validationSteps = new ArrayList<TaskActivatedValidationStep>();
		validationSteps.add(step);
	}
	
	protected boolean activateCheckPoint(){
		//wait to override
		return true;
	}
	
	protected void activateTask(){
		if(doLogStartRunTaskMessage) 
			log("Start Activate Task For ["+name()+"],Task Amount = " + (taskLst != null ? taskLst.size() : 0),Module.TASK);
		if(taskLst != null){
			processTasks();
		} else this.stopRunner();
	}
	
	protected void processTasks(){
		for(int i = 0;i < taskLst.size();i++){
			try {
				Task task = taskLst.get(i);
				task.taskAction();
				if(doLogStartRunTaskMessage) log("Task Process Done Rate = " +  (i + 1) + " / " + taskLst.size());
				onCompleteTask(task);
				threadHold(taskIntervalDelay);
			} catch(Exception e){
				this.except("Notice Error when activated task for ["+name()+"],Error="+e.getMessage(), this.getClass().getName());
				this.exportExceptionText(e);
			}
		}
	}
	
	protected void finishJob(){
		
	}
	
	protected void onCompleteTask(Task doneTask){
		
	}
	public void setActionDelay(long actionDelay) {
		this.actionDelay = actionDelay;
	}
	
	public void setDelayBetweenActions(long taskIntervalDelay) {
		this.taskIntervalDelay = taskIntervalDelay;
	}
	public void addTask(Task task){
		if(taskLst == null) taskLst = new ArrayList();
		taskLst.add(task);
	}
	public void removeTask(Task task){
		if(taskLst == null) taskLst = new ArrayList();
		taskLst.remove(task);
	}
	
	@Override
	protected void end() {
		log(name() + " is Done,Close self",Module.TASK);
	}
	public String name() {
		return name != null ? name : "Task Manager";
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDoLogStartRunTaskMessage(boolean doLogStartRunTaskMessage) {
		this.doLogStartRunTaskMessage = doLogStartRunTaskMessage;
	}
	
	

}
