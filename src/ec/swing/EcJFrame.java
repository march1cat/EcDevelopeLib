package ec.swing;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;

import ec.log.ExceptionLogger;
import ec.log.QueneLogger;
import ec.string.StringManager;

public abstract class EcJFrame extends JFrame{
	
	public enum Module{
		SYSTEM,UI
	}
	
	public enum Confirm{
		YES,NO,CANCEL
	}
	
	public enum OS {
		MAC,WINDOWS
	}
	public static Object DeveloperEnvironmentOS = OS.MAC;
	public static int deltaBetweenWindowsAndMac = 17;
	
	
	private QueneLogger logger = null;
	private ExceptionLogger exceptLogger = null;
	
	public EcJFrame(int paddingScreen){
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(paddingScreen,paddingScreen,screenSize.width - paddingScreen * 2,screenSize.height - paddingScreen * 2);
		listenWindowCloseEvent();
	}
	
	public EcJFrame(int width,int height,int xpos,int ypos){
		setBounds(xpos,ypos,width,height);
		listenWindowCloseEvent();
	}
	public EcJFrame(int width,int height){
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();	// 取得螢幕尺寸
		int xpos = screenSize.width / 2 - width / 2;
		int ypos = screenSize.height / 2 - height / 2;
		setBounds(xpos,ypos,width,height);
		listenWindowCloseEvent();
	}
	
	protected void paddingX(int paddingX){
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(paddingX,getY(),screenSize.width - paddingX * 2,getHeight());
	}
	
	protected void paddingY(int paddingY){
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(getX(),paddingY,this.getWidth(),screenSize.height - paddingY * 2);
	}
	
	protected void useNoLayoutManager(){
		getContentPane().setLayout(null);
	}
	
	protected void alterFrameTitle(String title){
		this.setTitle(title);
	}
	
	protected void locateToCenterOfScreen(){
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();	// 取得螢幕尺寸
		int xpos = screenSize.width / 2 - this.getWidth() / 2;
		int ypos = screenSize.height / 2 - this.getHeight() / 2;
		this.getLocation(new Point(xpos,ypos));
	}
	
	public void log(String data){
		data = "<Log> <Normal> " + StringManager.getSystemDate() + " ------> " + data;
		if (logger == null) logger = QueneLogger.getLogger();
		logger.log(data);
	}
	public void log(String data,Object module){
		String moduleText = "Normal"; 
		if(module == Module.UI){
			moduleText  = "UI"; 
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
	
	protected JComponent getChildByName(String name){
		for(int i =0;i< getContentPane().getComponentCount();i++){
			 JComponent c = (JComponent)getContentPane().getComponent(i);
			 if(name.equals(c.getName())){
				 return c;
			 }
		}
		return null;
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
	
	private void listenWindowCloseEvent(){
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) { 
				userClickCloseButtonOnFrame();
			}
		});
	}
	
	protected void userClickCloseButtonOnFrame(){
		System.exit(0);
	}
	
	public int getOSConvertorWidth(){
		String os = System.getProperty("os.name").toLowerCase();
		if(DeveloperEnvironmentOS == OS.MAC){
			if (os.indexOf("win") >= 0){
				return getWidth() - deltaBetweenWindowsAndMac;
			} else if (os.indexOf("mac") >= 0){
				return getWidth();
			}
			return getWidth();
		} else{
			if (os.indexOf("win") >= 0){
				return getWidth();
			} else if (os.indexOf("mac") >= 0){
				return getWidth() + deltaBetweenWindowsAndMac;
			}
			return getWidth();
		}
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
	
	protected void hideWindowOperationArea(){
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
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
