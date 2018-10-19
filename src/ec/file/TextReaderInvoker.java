package ec.file;

public interface TextReaderInvoker {

	
	public void readingProcessStart(String fileUri);
	public boolean onLineTextData(int readLineCount,String lineText);
	public void actionReadFileProcessError(int readLineCount,String fileUri,Exception error);
	public void actionTargetFileNotExist(String fileUri);
	public void readingProcessEnd(String fileUri);
	
}
