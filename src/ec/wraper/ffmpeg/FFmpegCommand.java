package ec.wraper.ffmpeg;

import ec.system.OsCommandExecuter;
import ec.system.OsCommandInvoker;

public abstract class FFmpegCommand extends OsCommandInvoker {

    private OsCommandExecuter executer = null;
    private ProcessInvoker invoker = null;

    public FFmpegCommand(ProcessInvoker invoker) {
        this.invoker = invoker;
    }


    public void start(){
        executer.addQueneObject(this);
        executer.startRunner();
    }

    @Override
    public void onCommadResponse(String data) {
        this.invoker.onCommadResponse(data);
    }

    @Override
    public void onCommadErrResponse(String data) {
        this.invoker.onCommadErrResponse(data);
    }

    @Override
    public void onStart() {
        this.log("FFMpeg processor starting , execute command = " + this.command());
    }

    @Override
    public void onExist(int exitValue) {
        this.log("FFMpeg processor done , exit process!!");
        this.executer.stopRunner();
        this.invoker.onExist(exitValue);
    }

    @Override
    public void onError(Exception e) {
        this.invoker.onError(e);
    }

    public void setExecuter(OsCommandExecuter executer) {
        this.executer = executer;
    }

    protected abstract void validate() throws Exception;


}
