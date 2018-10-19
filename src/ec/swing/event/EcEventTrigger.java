package ec.swing.event;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import ec.system.Basis;

public class EcEventTrigger extends Basis{

	public enum MouseClick{
		LEFT,RIGHT,DOUBLE_LEFT,DOUBLE_RIGHT
	}
	
	
	private JComponent eventTriggerComp = null;
	private EcTriggerEventListener listener = null;
	
	public EcEventTrigger(JComponent eventTriggerComp,EcTriggerEventListener listener){
		this.eventTriggerComp = eventTriggerComp;
		this.listener = listener;
	}
	
	public void listenClickEvent(){
		eventTriggerComp.setCursor(new Cursor(Cursor.HAND_CURSOR));
		eventTriggerComp.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				 if(e.getClickCount() == 2) {
					 if(SwingUtilities.isLeftMouseButton(e)){
						 listener.onEventTriggered(MouseClick.DOUBLE_LEFT, eventTriggerComp);
					 } else  if(SwingUtilities.isRightMouseButton(e)) {	 
						 listener.onEventTriggered(MouseClick.DOUBLE_RIGHT, eventTriggerComp);
					 }
				 } else {
					 if(SwingUtilities.isLeftMouseButton(e)) {
						 listener.onEventTriggered(MouseClick.LEFT, eventTriggerComp);
					 } else  if(SwingUtilities.isRightMouseButton(e)) {
						 listener.onEventTriggered(MouseClick.RIGHT, eventTriggerComp);
					 }
				 }
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	
	
	
}
