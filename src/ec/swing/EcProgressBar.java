package ec.swing;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public abstract class EcProgressBar extends EcJInfoBox{

	protected JPanel progressP = null;
	protected JLabel infoText = null;
	protected int tick = 0;
	
	public EcProgressBar(int width, int height, int xpos, int ypos,Color barColor) {
		super(width, height, xpos, ypos);
		autoMeetWidth();
		this.useNoLayoutManager();
		
		
		
		progressP = new JPanel();
		progressP.setBounds(0, 0, 0, getHeight());
		progressP.setBackground(barColor);
		
		
		infoText = new JLabel("",SwingConstants.CENTER);
		infoText.setSize(getWidth(),40);
		infoText.setLocation(20, progressP.getY() + progressP.getHeight() / 2 - 40 / 2) ;
		
		add(infoText);
		add(progressP);
		
		//default
		tick = (getWidth()) / 100;
	}
	
	private void autoMeetWidth(){
		if((getWidth()) % 100 != 0){
			int y = getWidth() / 100;
			this.setSize(y * 100 + (getWidth()  % 100 == 0 ? 0 : 100),getHeight());
		} 
	}
	
	public void goProcess(int tickAmount){
		if(tickAmount * tick >= getWidth()){
			progressP.setSize(getWidth(), progressP.getHeight());
		} else {
			progressP.setSize(tickAmount * tick, progressP.getHeight());
		}
	}
	
	public void resetProgressBar(){
		progressP.setSize(0, progressP.getHeight());
		setInfo(null);
	}
	
	
	public void runEndProgress(){
		progressP.setSize(getWidth(), progressP.getHeight());
	}
	
	public void setInfo(String text){
		infoText.setText(text);
	}
	
	
	
}
