package ec.system;

public abstract class OsCommandInvoker extends Basis{

	private OsCommandExecuter executer = null;
	
	public void linkCommandExecuter(OsCommandExecuter executer) {
		this.executer = executer;
	}
	public void reExecute(){
		executer.addQueneObject(this);
	}
	public abstract String command();
	public abstract void onCommadResponse(String data);
	public abstract void onCommadErrResponse(String data);
	public abstract void onStart();
	public abstract void onExist(int exitValue);
	public abstract void onError(Exception e);
}
