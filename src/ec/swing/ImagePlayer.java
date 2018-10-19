package ec.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import ec.system.Runner;

public class ImagePlayer extends Runner{

	private JLabel defaultImg = null;
	private List<JLabel> playlist = null;
	
	private long imgDelay = 1000;
	
	private boolean isPlayDone = true;
	
	public ImagePlayer(JLabel defaultImg){
		this.defaultImg = defaultImg;	
	}
	
	@Override
	protected void running() {
		if(playlist != null && playlist.size() > 0){
			defaultImg.setVisible(false);
			for(JLabel lbl : playlist){
				lbl.setVisible(true);
				try {
					Thread.sleep(imgDelay);
				} catch (InterruptedException e) {}
				lbl.setVisible(false);
			}
		}
		this.stopRunner();
	}
	


	@Override
	protected void beforeRunning() {
		synchronized(this){
			isPlayDone = false;
		}
		reset();
	}

	@Override
	protected void end() {
		reset();
		synchronized(this){
			isPlayDone = true;
		}
	}

	public void reset(){
		defaultImg.setVisible(true);
		if(playlist != null && playlist.size() > 0){
			for(JLabel lbl : playlist){
				lbl.setVisible(false);
			}
		}
	}

	
	public void setDelay(long delay){
		imgDelay = delay;
	}
	

	public void addImgToPlayList(JLabel imgLbl){
		if(playlist == null) playlist = new ArrayList<>();
		playlist.add(imgLbl);
	}
	
	public synchronized boolean isPlayDone(){
		return isPlayDone;
	}
	
}
