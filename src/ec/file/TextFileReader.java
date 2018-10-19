package ec.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import ec.system.Basis;

public class TextFileReader extends Basis{

	private TextReaderInvoker invoker = null;
	
	public TextFileReader(TextReaderInvoker invoker){
		this.invoker = invoker;
	}
	
	public void readFileLineByLine(String textFileUri){
		File file = new File(textFileUri);
		if(file.exists()){
			String line = null;
			int readLineCount = 1;
			try {
				invoker.readingProcessStart(textFileUri);
				BufferedReader br = new BufferedReader(new FileReader(textFileUri));
				while ((line = br.readLine()) != null) {
					if(!invoker.onLineTextData(readLineCount++,line)) break;
				}
				invoker.readingProcessEnd(textFileUri);
			} catch (Exception e) {
				this.exportExceptionText(e);
				invoker.actionReadFileProcessError(readLineCount,textFileUri,e);
			}
		} else invoker.actionTargetFileNotExist(textFileUri);
	}
	
	public void readFileLineByLine(String textFileUri,String encode){
		File file = new File(textFileUri);
		if(file.exists()){
			String line = null;
			int readLineCount = 1;
			try {
				invoker.readingProcessStart(textFileUri);
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(textFileUri), encode));
				while ((line = br.readLine()) != null) {
					if(!invoker.onLineTextData(readLineCount++,line)) break;
				}
				invoker.readingProcessEnd(textFileUri);
			} catch (Exception e) {
				this.exportExceptionText(e);
				invoker.actionReadFileProcessError(readLineCount,textFileUri,e);
			}
		} else invoker.actionTargetFileNotExist(textFileUri);
	}
	
	
	
}
