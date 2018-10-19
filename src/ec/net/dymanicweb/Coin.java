package ec.net.dymanicweb;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ec.system.Basis;

public abstract class Coin extends Basis{

	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private String requestEnCode = "UTF-8";
	private Object httpMethod = HttpEntry.HttpMethod.GET;
	private boolean isGenerateOK = false;
	private boolean doParsing8859_1 = true; //use for chinese post data
	
	
	public String clientIP;
	
	public Coin(HttpServletRequest request,HttpServletResponse response){
		this.request = request;
		this.response = response;
		try {
			initialClientEncodeType();
			putValueToVariable();
			isGenerateOK = true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			exportExceptionText(e);
		}
	}
	public Coin(HttpServletRequest request,HttpServletResponse response,String requestEnCode){
		this.request = request;
		this.response = response;
		this.requestEnCode = requestEnCode;
		try {
			initialClientEncodeType();
			putValueToVariable();
			isGenerateOK = true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			exportExceptionText(e);
		}
	}
	
	public Coin(HttpServletRequest request,HttpServletResponse response,String requestEnCode,boolean doParsing8859_1){
		this.request = request;
		this.response = response;
		this.requestEnCode = requestEnCode;
		this.doParsing8859_1 = doParsing8859_1;
		try {
			initialClientEncodeType();
			putValueToVariable();
			isGenerateOK = true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			exportExceptionText(e);
		}
	}
	
	
	
	public abstract boolean isNeccessaryVariableFilled();
	public abstract boolean validateDataFormat();
	public abstract boolean isAccessCheckPass();
	
	private void initialClientEncodeType(){
		try{
			log("Initial Client Encode Type = " + requestEnCode);
			request.setCharacterEncoding(requestEnCode);
			response.setCharacterEncoding(requestEnCode);
		} catch(Exception e){
			this.except("Set Client Request Encode Fail,Error = " + e.getMessage(), this.getClass().getName());
			this.exportExceptionText(e);
		}
	}
	
	protected void putValueToVariable() throws IllegalArgumentException, IllegalAccessException{
		log("Start Assign Request Parameter For Encode = " + requestEnCode + ",Doing 8859_1 = " + doParsing8859_1);
		Iterator<String> iter = request.getParameterMap().keySet().iterator();
		Field[] f_ar = getClass().getFields();
		while(iter.hasNext()){
			String key = iter.next();
			for(Field f : f_ar){
				if(compareValue("request", f.getName()) || 
						compareValue("response", f.getName()) || 
						compareValue("httpMethod", f.getName()) || 
						compareValue("requestEnCode", f.getName()) || 
						compareValue("clientIP", f.getName()) || 
						compareValue("isGenerateOK", f.getName())) continue;
				if(compareValue(key, f.getName())){
					String tempVal = getNotEmptyValue(request.getParameter(key), "");
					try {
						String value = null;
						if(doParsing8859_1) value = new String(tempVal.getBytes("8859_1"),requestEnCode);
						else value = new String(tempVal.getBytes(),requestEnCode);
						log("Assign Key["+key+"],Org Value="+tempVal + ", Encode Value = " + value);
						f.set(this, value);
					} catch (Exception e) {
						this.except("Parsing String Data To "+requestEnCode+" Fail,Error=" + e.getMessage(), this.getClass().getName());
						f.set(this, null);
					}
				}
			}
		}
		this.clientIP = request.getRemoteAddr();
	}
	
	public boolean isSessionCreatedCoin(){
		HttpSession session = request.getSession(false);
		return session != null;
	}
	
	public String getSessionValue(String key){
		if(isSessionCreatedCoin()){
			HttpSession session = request.getSession(true);
			return getNotEmptyValue(session.getAttribute(key), "");
		} 
		return "";	
	}
	
	public void setPostMethod(){
		this.httpMethod = HttpEntry.HttpMethod.POST;
	}
	
	public HttpServletRequest getClientRequest(){
		return this.request;
	}
	public HttpServletResponse getResponseComponent(){
		return this.response;
	}
	
	
	public boolean isGenerateOK(){
		return this.isGenerateOK;
	}
	
	public boolean TextValueNotEmpty(String textValue){
		return textValue != null && textValue.length() > 0;
	}
	
	public boolean RequestIsGet(){
		return httpMethod == HttpEntry.HttpMethod.GET;
	}
	
	public boolean RequestIsPost(){
		return httpMethod == HttpEntry.HttpMethod.POST;
	}
	
}
