package ec.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ec.system.Basis;

public class EcDirector extends Basis {

	private String uri = null;
	
	public EcDirector(String uri){
		this.uri = uri.replaceAll("\\\\", "/");
	}
	
	public EcDirector(String uri,boolean autoGenerate){
		this.uri = uri.replaceAll("\\\\", "/");
		if(autoGenerate) generateDirector();
	}
	
	public List<String> listFile(){
		return FileManager.getFolderDocumentList(uri);
	}
	
	public List<String> listFileInUri(){
		List<String> list = FileManager.getFolderDocumentList(uri);
		List<String> uriList = new ArrayList<>();
		if(!list.isEmpty()){
			for(String i : list){
				uriList.add(Uri() + i);
			}
			return uriList;
		} else return list;
	}
	
	
	public String Uri(){
		return uri.endsWith("/") ? uri : uri + "/";
	}
	public String FileUri(String fileName){
		return fileName.startsWith("/") ? Uri() + fileName.substring(1) : Uri() + fileName;
	}
	public void generateDirector(){
		File f = new File(uri);
		if(!f.exists()) f.mkdirs();
		else {
			if(!f.isDirectory()) f.mkdirs();
		}
	}
	
	public boolean isDirectory(){
		File f = new File(uri);
		if(f.exists()) return f.isDirectory();
		else return false;
	}
	
	public int clearAllFiles(){
		List<String> fs = listFileInUri();
		int removeCnt = 0;
		if(isListWithContent(fs)){
			try{
				for(String f : fs){
					File file = new File(f);
					if(file.exists()) {
						if(file.delete()) removeCnt++;
					}
				}
			} catch(Exception e){
				this.exportExceptionText(e);
			}
		}
		return removeCnt;
	}
	
}
