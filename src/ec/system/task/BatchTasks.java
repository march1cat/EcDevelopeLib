package ec.system.task;

import java.util.ArrayList;
import java.util.List;

import ec.system.Basis;

public class BatchTasks extends Basis implements Task{

	private String batchName = null;
	private List<ChainTask> batchTsks = null;
	
	public BatchTasks(){
		this.batchName = "UnDefineNameBatch";
	}
	
	public BatchTasks(String batchName){
		this.batchName = batchName;
	}
	

	@Override
	public void taskAction() {
		if(batchTsks != null && !batchTsks.isEmpty()){
			log("Batch Tasks[" + batchName + "] Has " + batchTsks.size() + " Sub Chain Tasks, Start Activated Them!!",Module.TASK);
			boolean isAllFinish = true;
			for(ChainTask subTask : batchTsks){
				subTask.taskAction();
				if(!subTask.isTaskFinishSuccess()) {
					isAllFinish = false;
					break;
				}
			}
			if(isAllFinish) log("Execute Batch Tasks[" + batchName + "] Finish All !!");
			else log("Execute Batch Tasks[" + batchName + "] Finish At Break Mode !!");
		} else log("Batch Tasks[" + batchName + "] Has no Sub Chain Task,Skip Doing It");
	}
	
	public void addBatch(ChainTask task){
		if(batchTsks == null) batchTsks = new ArrayList<>();
		batchTsks.add(task);
		log("Add Sub Chaine Task To Batch Tasks[" + batchName + "],Now Batch Task Size = " + batchTsks.size(),Module.TASK);
	}
	

}
