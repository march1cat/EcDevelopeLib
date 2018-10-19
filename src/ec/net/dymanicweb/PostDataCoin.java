package ec.net.dymanicweb;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class PostDataCoin extends Coin{

	private String data;
	
	public PostDataCoin(HttpServletRequest request,HttpServletResponse response){
		super(request,response);
	}
	public PostDataCoin(HttpServletRequest request,HttpServletResponse response,String requestEnCode){
		super(request,response,requestEnCode);
	}
	
	public PostDataCoin(HttpServletRequest request,HttpServletResponse response,String requestEnCode,boolean doParsing8859_1){
		super(request,response,requestEnCode,doParsing8859_1);
	}
	
	@Override
	public boolean isNeccessaryVariableFilled() {
		return isDataPrepareSuccess();
	}

	
	@Override
	protected void putValueToVariable() throws IllegalArgumentException, IllegalAccessException {
		StringBuffer tmp = new StringBuffer();
		String line = null;
		try {
		    BufferedReader reader = this.getClientRequest().getReader();
		    while ((line = reader.readLine()) != null) tmp.append(line);
		    if(TextValueNotEmpty(tmp.toString())) data = tmp.toString();
		    log("Parsing Pure Text Post Request Data Finish,Data = " + data);
		} catch (Exception e) { 
			this.except("Load Request Post Data Fail,Error = " + e.getMessage(), this.getClass().getName());
			this.exportExceptionText(e);
		}
	}
	
	public boolean isDataPrepareSuccess(){
		return data != null && validateDataFormat();
	}
	
	public String Data(){
		return data;
	}
	
	
	
}
