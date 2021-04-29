package ec.wraper.ffmpeg;

public interface ProcessInvoker {

    public void onCommadResponse(String consoleLineData);
    public void onCommadErrResponse(String consoleLineData);
    public void onExist(int exitValue);
    public void onError(Exception e);

}
