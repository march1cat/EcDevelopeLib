package ec.swing;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JRootPane;

public class EcQuickMenu extends EcInternalFrame{

	private List<JButton> opionLst = null;
	protected QuickMenuInvoker invoker = null;
	private boolean isShowing = false;
	
	public EcQuickMenu(List<JButton> opionLst,int optBtnWidth,int optBtnHeight,QuickMenuInvoker invoker) {
		this.opionLst = opionLst;
		this.removeTitleBar();
		int totalHeight = 0;
		this.invoker = invoker;
		this.useNoLayoutManager();
		int xpos = 0;
		int ypos = 0;
		for(JButton b : opionLst){
			b.setSize(optBtnWidth, optBtnHeight);
			b.setLocation(xpos, ypos);
			ypos+=optBtnHeight;
			totalHeight+=optBtnHeight;
			this.add(b);
		}
		this.setSize(optBtnWidth,totalHeight);
		this.prepareButtonActionListenerInFrame();
	}

	public void showMenu(int xpos,int ypos) {
		this.setLocation(xpos, ypos);
		isShowing = true;
		this.showup();
	}

	@Override
	protected void onClickJButtonAction(ActionEvent e, JButton onclickButton) {
		this.hideSelf();
		this.invoker.onChooseQuickMenuOption(onclickButton);
	}
	
	
	
	
	@Override
	public void hideSelf() {
		super.hideSelf();
		isShowing = false;
	}

	public boolean isShowing(){
		return isShowing;
	}
	
	
	

}
