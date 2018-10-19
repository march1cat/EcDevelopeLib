package ec.swing;


public abstract class EcConfirmBox extends EcJInfoBox{

	private ConfirmInvoker invoker = null;
	private EcCofirmPassParameter para = null;
	
	public EcConfirmBox(int width, int height, int xpos, int ypos,ConfirmInvoker invoker) {
		super(width, height, xpos, ypos);
		this.invoker = invoker;
	}
	
	protected void onConfirmAction(){
		invoker.onConfirm(para);
	}
	
	protected void onCancelAction(){
		invoker.onCancel(para);
	}
	
	public void showConfirm(String showText,EcCofirmPassParameter para){
		setInfoMessage(showText);
		this.para = para;
		this.showup();
	}
	
	public abstract void setInfoMessage(String text);

}
