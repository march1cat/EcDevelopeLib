package ec.system.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import ec.system.Runner;



public abstract class QueneDataController extends Runner{

	private Queue<Object> quene = null;
	public QueneDataController(){
		quene = new ConcurrentLinkedQueue<Object>();
	}
	
	public synchronized void addQueneObject(Object queneData){
		if(validateNewDataInQuene(queneData)) quene.add(queneData);
		else this.except("Add Wrong Data to Quene!!",this.getClass().getName());
	}
	
	private synchronized Object getObjectFromQuene(){
		return quene.poll();
	}
	
	public List<Object> getQueneDatas(){
		List<Object> datas = new ArrayList<>();
		Iterator<Object> qDataIter = quene.iterator();
		while(qDataIter.hasNext()){
			datas.add(qDataIter.next());
		}
		return datas;
	}
	
	@Override
	protected void running() {
		while(!quene.isEmpty()){
			Object obj = getObjectFromQuene();
			if(obj!=null){
				try {
					process(obj);
				} catch(Exception e){
					this.exportExceptionText(e);				
				}
			}
		}
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {}
	}
	
	protected abstract boolean validateNewDataInQuene(Object dataWillInQuene);
	protected abstract void process(Object dataInQuene);
	
	
}
