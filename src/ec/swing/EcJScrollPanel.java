package ec.swing;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class EcJScrollPanel extends EcJPanel{

	private JScrollPane scrollPane = null;
	
	public EcJScrollPanel(int width, int height, Color panelBcColor) {
		super(width, height, panelBcColor);
		itemContainer().setAutoscrolls(true);
		scrollPane = new JScrollPane(itemContainer());
		scrollPane.setSize(width, height);
	}
	
	
	public void updateContentPrefferedSize(int width,int height){
		itemContainer().setPreferredSize(new Dimension(width,height));
	}
	
	@Override
	public void setLocation(int xpos,int ypos){
		if(scrollPane != null) scrollPane.setLocation(xpos,ypos);
	}

	@Override
	public void joinContainer(JComponent container) {
		if(scrollPane != null) container.add(scrollPane);
	}
	
	@Override
	public void joinContainer(JFrame container) {
		if(scrollPane != null) container.add(scrollPane);
	}
	
	@Override
	public int getWidth(){
		return scrollPane != null ? scrollPane.getWidth() : 0;
	}
	@Override
	public int getHeight(){
		return scrollPane != null ? scrollPane.getHeight() : 0;
	}
	@Override
	public int getX(){
		return scrollPane.getX();
	}
	@Override
	public int getY(){
		return scrollPane.getY();
	}

}
