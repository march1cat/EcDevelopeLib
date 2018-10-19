package ec.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import ec.log.ExceptionLogger;
import ec.log.QueneLogger;
import ec.string.StringManager;
import ec.swing.EcJFrame.Confirm;

public abstract class EcInternalFrame extends JInternalFrame{

	public enum Module{
		SYSTEM,MODULE_UI
	}
	
	public enum Confirm{
		YES,NO,CANCEL
	}
	
	private QueneLogger logger = null;
	private ExceptionLogger exceptLogger = null;
	
	public EcInternalFrame(){
		
	}
	
	protected void useNoLayoutManager(){
		getContentPane().setLayout(null);
	}
	
	protected void removeTitleBar(){
		putClientProperty("JInternalFrame.isPalette", Boolean.TRUE); getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		((BasicInternalFrameUI) this.getUI()).setNorthPane(null);
		this.setBorder(null);
	}
	
	protected void alterFrameTitle(String title){
		this.setTitle(title);
	}
	
	public void log(String data){
		data = "<Log> <Normal> " + StringManager.getSystemDate() + " ------> " + data;
		if (logger == null) logger = QueneLogger.getLogger();
		logger.log(data);
	}
	public void log(String data,Object module){
		String moduleText = "Normal"; 
		if(module == Module.MODULE_UI){
			moduleText  = "Module_UI"; 
		} else if(module == Module.SYSTEM){
			moduleText  = "System";
		}
		data = "<Log> <" + moduleText + "> " + StringManager.getSystemDate() + " ------> " + data;
		if (logger == null) logger = QueneLogger.getLogger();
		logger.log(data);
	}
	
	public void except(String data,String classname){
		data = "<Error> " + StringManager.getSystemDate() + " (At " + classname + ")" + " ------> " + data;
		if (logger == null) logger = QueneLogger.getLogger();
		logger.log(data);
	}
	public void exportExceptionText(Exception e){
		if(exceptLogger == null)exceptLogger = ExceptionLogger.getLogger();
		if(exceptLogger != null) {
			exceptLogger.writeException(e);
		}
	}
	
	protected String getNotEmptyValue(Object data,String nvsl){
		if(data == null) return nvsl;
		return data.toString().trim();
	}
	
	public void showup(){
		this.setVisible(true);
	}
	public void hideSelf(){
		this.setVisible(false);
	}
	
	public void refreshCanvas(){
		hideSelf();
		showup();
	}
	
	public void add(JComponent comp){
		this.getContentPane().add(comp);
	}
	
	
	protected void prepareButtonActionListenerInFrame(){
		for(int i =0;i< getContentPane().getComponentCount();i++){
			 JComponent c = (JComponent)getContentPane().getComponent(i);
			 if(c instanceof JButton){
				 ((JButton)c).addActionListener(new ActionListener() {
					  public void actionPerformed(ActionEvent e)  {
						  onClickJButtonAction(e,(JButton)e.getSource());
					  }
				});
			 }
		}
	}
	
	
	protected void prepareRadioButtonValueChangeActionListenerInFrame(){
		for(int i =0;i< getContentPane().getComponentCount();i++){
			 JComponent c = (JComponent)getContentPane().getComponent(i);
			 if(c instanceof JRadioButton){
				 ((JRadioButton)c).addItemListener(new ItemListener() {
					 public void itemStateChanged(ItemEvent e) {
					        JRadioButton rd = (JRadioButton) e.getSource();
					        onRadioButtonStateChangedAction(e,rd,e.getStateChange()==ItemEvent.SELECTED);
					    }
				});
			 }
		}
	}
	
	
	protected void prepareCheckboxValueChangeActionListenerInFrame(){
		for(int i =0;i< getContentPane().getComponentCount();i++){
			 JComponent c = (JComponent)getContentPane().getComponent(i);
			 if(c instanceof JCheckBox){
				 ((JCheckBox)c).addItemListener(new ItemListener() {
					 public void itemStateChanged(ItemEvent e) {
						 JCheckBox rd = (JCheckBox) e.getSource();
						 onCheckBoxStateChangedAction(e,rd,e.getStateChange()==ItemEvent.SELECTED);
					 }
				});
			 }
		}
	}
	
	protected void prepareComboboxValueChangeActionListenerInFrame(){
		for(int i =0;i< getContentPane().getComponentCount();i++){
			 JComponent c = (JComponent)getContentPane().getComponent(i);
			 if(c instanceof JComboBox){
				 ((JComboBox)c).addItemListener(new ItemListener() {
					 public void itemStateChanged(ItemEvent e) {
						 if (e.getStateChange() == ItemEvent.SELECTED) {
							 JComboBox comboBx = (JComboBox) e.getSource();
							 onComboBoxStateChangedAction(e,comboBx,comboBx.getSelectedItem());
						 }
					 }
				});
			 }
		}
	}
	
	protected JComponent getChildByName(String name){
		for(int i =0;i< getContentPane().getComponentCount();i++){
			 JComponent c = (JComponent)getContentPane().getComponent(i);
			 if(name.equals(c.getName())){
				 return c;
			 }
		}
		return null;
	}
	
	
	protected void enableAllJButtonInFrame(){
		for(int i =0;i< getContentPane().getComponentCount();i++){
			 JComponent c = (JComponent)getContentPane().getComponent(i);
			 if(c instanceof JButton){
				 ((JButton)c).setEnabled(true);
			 }
		}
	}
	protected void disableAllJButtonInFrame(){
		for(int i =0;i< getContentPane().getComponentCount();i++){
			 JComponent c = (JComponent)getContentPane().getComponent(i);
			 if(c instanceof JButton){
				 ((JButton)c).setEnabled(false);
			 }
		}
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
	
	//============================================================
	//Wait to Override
	protected void onClickJButtonAction(ActionEvent e,JButton onclickButton){
		//wait to override
	}
	
	protected void onRadioButtonStateChangedAction(ItemEvent e,JRadioButton onChangedRd,boolean isSelected){
		//wait to override
	}
	protected void onCheckBoxStateChangedAction(ItemEvent e,JCheckBox onChangeChk,boolean isSelected){
		//wait to override
	}
	
	protected void onComboBoxStateChangedAction(ItemEvent e,JComboBox stateChangedBx,Object selectItem){
		//wait to override
	}
	
}
