package ec.net.httpclient.iorequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ec.file.FileManager;
import ec.net.httpclient.Request;

public class IORequest extends Request{

	private String requestDocUri = null;
	private String requestContent = null;
	private Map<String,String> replaceContentMp = null;
	
	public IORequest(String requestDocUri,String host){
		super(host);
		this.requestDocUri = requestDocUri;
	}
	
	public IORequest(String requestDocUri,String host,int port){
		super(host,port);
		this.requestDocUri = requestDocUri;
	}
	
	public void setReplaceContentPattern(String key,String value){
		if(replaceContentMp == null) replaceContentMp = new HashMap<String, String>();
		replaceContentMp.put(key, value);
	}

	
	public void loadRequestContent(){
		List<String> datas = FileManager.ReadTextAllDataInLineByLine(requestDocUri);
		StringBuffer data = new StringBuffer("");
		for(String i : datas){
			data.append(i + "\r\n");
		}
		requestContent = data.toString() + "\r\n";
	}
	
	

	@Override
	public byte[] getToSendData() {
		if(requestContent == null) loadRequestContent();
		String targetToSendData = null;
		if(replaceContentMp != null){
			String requestNewContent = requestContent;
			Iterator<String> iter = replaceContentMp.keySet().iterator();
			while(iter.hasNext()){
				String key = iter.next();
				String value = replaceContentMp.get(key);
				requestNewContent = requestNewContent.replace(key, value);
			}
			targetToSendData =  requestNewContent;
		}  else targetToSendData =  requestContent;
		
		
		if(contBytAr == null || contBytAr.length <= 0) return targetToSendData.getBytes();
		else {
			String end = "";
			byte[] rv = new byte[ targetToSendData.getBytes().length + end.getBytes().length + contBytAr.length];
			System.arraycopy(targetToSendData.getBytes(), 0, rv, 0, targetToSendData.getBytes().length);
			System.arraycopy(contBytAr, 0, rv, targetToSendData.getBytes().length , contBytAr.length);
			System.arraycopy(end.getBytes(), 0, rv, targetToSendData.getBytes().length + contBytAr.length, end.getBytes().length);
			return rv;
		}
	}

}
