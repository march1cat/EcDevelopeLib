package ec.net.dymanicweb;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import ec.file.EcDirector;
import ec.system.Basis;

public class FileUploadManager extends Basis{

	public enum FileData{
		FILE_NAME,FILE_SIZE,SAVING_URI
	}
	
	public enum Result{
		SUCCESS,FAIL,VALIDATE_FAIL
	}
	
	private Object uploadResult = null;
	private HttpServletRequest request = null;
	private List<FileUploadFilter> filters = null;
	private List<FileItem> itemList = null;
	private List<Map<Object,String>> uploadFileInfos = null;
	
	public FileUploadManager(HttpServletRequest request){
		this.request = request;
	}
	
	public void addFileFilter(FileUploadFilter filter){
		if(filters == null) filters = new ArrayList<>();
		filters.add(filter);
	}
	
	public boolean isFileUploadRequest(){
		return ServletFileUpload.isMultipartContent(request);
	}
	
	public String getParameter(String formReferName){
		if(isFileUploadRequest()){
			
			try {
				if(itemList == null){
					DiskFileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload sfu = new ServletFileUpload(factory);    
					sfu.setHeaderEncoding("UTF-8");    
					sfu.setFileSizeMax(1024*1024*2);    
					sfu.setSizeMax(1024*1024*10); 
					itemList = sfu.parseRequest(request);
				}
				for (FileItem fileItem : itemList) { 
			    	String fieldName = fileItem.getFieldName();
			    	if(fieldName.equals(formReferName) && fileItem.isFormField()){
			    		 return fileItem.getString();
			    	}
				}
			} catch(Exception e){
				this.except("Get Form Parameter Fail,Error = " + e.getMessage() + ", Key = " + formReferName, this.getClass().getName());
				this.exportExceptionText(e);
			}
		}
		return null;
	}
	
	public void  saveUploadFileToDisk(String formReferName,EcDirector saveFolder){
		saveUploadFileToDisk(formReferName,saveFolder,null);
	}
	
	public void saveUploadFileToDisk(String formReferName,EcDirector saveFolder,String saveName){
		if(isFileUploadRequest()){
			try {
				if(itemList == null){
					DiskFileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload sfu = new ServletFileUpload(factory);    
					sfu.setHeaderEncoding("UTF-8");    
					sfu.setFileSizeMax(1024*1024*2);    
					sfu.setSizeMax(1024*1024*10); 
					itemList = sfu.parseRequest(request);
				}
				for (FileItem fileItem : itemList) { 
			    	String fieldName = fileItem.getFieldName();
			    	if(fieldName.startsWith(formReferName) && !fileItem.isFormField()){
			    		Map<Object,String> mp = new HashMap<>();
			    		Long size = fileItem.getSize();          
			            String fileName = fileItem.getName();
			            
			            if(isListWithContent(filters)) {
			            	for(FileUploadFilter filter : filters){
			            		if(!filter.isValidateOK(fileName)){
			            			this.uploadResult = Result.VALIDATE_FAIL;
			            			break;
			            		}
			            	}
			            }
			            mp.put(FileData.FILE_NAME, fileName);
			            mp.put(FileData.FILE_SIZE, String.valueOf(size));
			            
			            File file = new File(saveFolder.Uri() + ((saveName == null) ? fileName : saveName));
			            try {
							fileItem.write(file);
							if(saveName == null) mp.put(FileData.SAVING_URI, saveFolder.Uri() + fileName);
							else mp.put(FileData.SAVING_URI, saveFolder.Uri() + saveName);
						} catch (Exception e) {
							this.except("Save Upload File Fail,Error = " + e.getMessage(), this.getClass().getName());
							this.exportExceptionText(e);
							this.uploadResult = Result.FAIL;
						}
			            if(uploadFileInfos == null) uploadFileInfos = new ArrayList<>(); 
			            uploadFileInfos.add(mp);
			    	}
				}
				if(!isListWithContent(uploadFileInfos)) log("Not Found Target Upload File,Refer Name = " + formReferName);
				uploadResult =  isListWithContent(uploadFileInfos) ? Result.SUCCESS : Result.FAIL;
			} catch (FileUploadException e) {
				this.except("Save Upload File Fail,Error = " + e.getMessage(), this.getClass().getName());
				this.exportExceptionText(e);
				this.uploadResult = Result.FAIL;
			}
		} else this.uploadResult = Result.FAIL;
	}
	
	public Object uploadResult(){
		return uploadResult;
	}

	public List<Map<Object, String>> getUploadFileInfos() {
		return uploadFileInfos;
	}
	
	
	
	
}
