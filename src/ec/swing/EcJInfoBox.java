package ec.swing;



import javax.swing.JFrame;
import javax.swing.JRootPane;

public class EcJInfoBox extends EcJFrame{
	private int defaultXpos = -1;
	private int defaultYpos = -1;
	public EcJInfoBox(int width, int height, int xpos, int ypos) {
		super(width, height, xpos, ypos);
		defaultXpos = xpos;
		defaultYpos = ypos;
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
	}
	
	public EcJInfoBox(int width, int height) {
		super(width, height);
		defaultXpos = getX();
		defaultYpos = getY();
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
	}
	
	protected void locateToDefault(){
		this.setLocation(defaultXpos, defaultYpos);
	}
	
	@Override
	protected void userClickCloseButtonOnFrame(){
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		hideSelf();
	}
	

}
