package ec.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import ec.system.Basis;

public class FileController extends Basis{

	private FileTaskInvoker invoker = null;
	private File trg_file = null;
	private BufferedReader bff_reader = null;
	
	public FileController(FileTaskInvoker invoker){
		this.invoker = invoker;
	}
	
	public boolean mountFile(String f_uri){
		trg_file = new File(f_uri);
		boolean isExist = trg_file.exists();
		if(!isExist) trg_file = null;
		bff_reader = null;
		return isExist;
	}
	
	
	public void startLineRead() throws Exception{
		if(trg_file != null) {
			if(bff_reader ==  null)
				bff_reader = new BufferedReader(new FileReader(trg_file.getAbsolutePath()));
			String line = null;
			while ((line = bff_reader.readLine()) != null) {
				invoker.onFileLineData(line);
			}
		}
	}
	
	
}
