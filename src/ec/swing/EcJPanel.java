package ec.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ec.swing.EcJFrame.Confirm;
import ec.system.Basis;

public abstract class EcJPanel extends Basis{
	
	public enum Confirm{
		YES,NO,CANCEL
	}
	
	private JPanel itemContainer = null;
	
	public EcJPanel(int width,int height){
		itemContainer = new JPanel();
		itemContainer.setLayout(null);
		itemContainer.setSize(width,height);
	}
	
	public EcJPanel(int width,int height,Color panelBcColor){
		itemContainer = new JPanel();
		itemContainer.setLayout(null);
		itemContainer.setSize(width,height);
		itemContainer.setBackground(panelBcColor);
	}
	
	protected JPanel itemContainer(){
		return itemContainer;
	}
	
	
	protected void add(JComponent comp){
		if(itemContainer != null) itemContainer.add(comp);
	}
	
	protected void remove(JComponent comp){
		if(itemContainer != null) itemContainer.remove(comp);
	}
	
	
	public void removeFromContainer(){
		if(itemContainer != null && itemContainer.getParent() != null) itemContainer.getParent().remove(itemContainer);
	}
	
	protected void setBackground(Color color){
		if(itemContainer != null) itemContainer.setBackground(color);
	}
	
	public void setLocation(int xpos,int ypos){
		if(itemContainer != null) itemContainer.setLocation(xpos,ypos);
	}
	
	public void joinContainer(EcJPanel container){
		if(itemContainer != null) container.add(itemContainer);
	}
	
	public void joinContainer(JComponent container){
		if(itemContainer != null) container.add(itemContainer);
	}
	
	public void joinContainer(JFrame container){
		if(itemContainer != null) container.add(itemContainer);
	}
	
	public int getWidth(){
		return itemContainer != null ? itemContainer.getWidth() : 0;
	}
	
	public int getHeight(){
		return itemContainer != null ? itemContainer.getHeight() : 0;
	}
	
	public int getX(){
		return itemContainer.getX();
	}
	
	public int getY(){
		return itemContainer.getY();
	}
	
	protected JLabel generateImage(String imgUri) throws IOException{
		BufferedImage img = ImageIO.read(new File(imgUri));
		ImageIcon icon = new ImageIcon(img);
		JLabel label = new JLabel();
		label.setIcon(icon);
		return label;
	}
	
	protected void showSystemDefaultInfoBox(String message){
		JOptionPane.showMessageDialog(null, message);
	}
	
	protected Object showSystemDefaultConfirmBox(String message){
		int result = JOptionPane.showConfirmDialog(null, message);
		if(result == JOptionPane.YES_OPTION){
			return Confirm.YES;
		} else if(result == JOptionPane.NO_OPTION){
			return Confirm.NO;
		} else return Confirm.CANCEL; 
	}
	
	
	protected String showSystemDefaultInputBox(String questionText){
		return JOptionPane.showInputDialog(questionText);
	}
	
	protected void prepareButtonActionListenerInPanel(){
		for(int i =0;i< itemContainer().getComponentCount();i++){
			 JComponent c = (JComponent)itemContainer().getComponent(i);
			 if(c instanceof JButton){
				 ((JButton)c).addActionListener(new ActionListener() {
					  public void actionPerformed(ActionEvent e)  {
						  onClickJButtonAction(e,(JButton)e.getSource());
					  }
				});
			 }
		}
	}
	
	//============================================================
	//Wait to Override
	protected void onClickJButtonAction(ActionEvent e,JButton onclickButton){
		//wait to override
	}
}
