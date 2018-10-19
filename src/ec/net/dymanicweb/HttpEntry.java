package ec.net.dymanicweb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ec.log.ExceptionLogger;
import ec.log.QueneLogger;
import ec.net.dymanicweb.testor.EntryTestSimulatedRequest;
import ec.net.dymanicweb.testor.EntryTestSimulatedResponse;
import ec.string.StringManager;

public abstract class HttpEntry extends HttpServlet {
	public enum HttpMethod{
		GET,POST
	}
	
	private QueneLogger logger = null;
	private ExceptionLogger exceptLogger = null;
	
	
	protected abstract void validateNeccassryParameterFilledFailAction(Coin coin);
	protected abstract void accessDenyAction(Coin coin);
	protected abstract void entryAction(Coin coin);
	protected abstract Coin generateCoin(HttpServletRequest req,HttpServletResponse response);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleClientRequest(request,response,HttpMethod.GET);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleClientRequest(request,response,HttpMethod.POST);
	}
	
	private void handleClientRequest(HttpServletRequest request, HttpServletResponse response,Object method){
		logRequest(request,method);
		Coin coin = generateCoin(request,response);
		if(method == HttpMethod.POST) coin.setPostMethod();
		if(coin.isNeccessaryVariableFilled()){
			if(coin.isAccessCheckPass()){
				entryAction(coin);
			} else accessDenyAction(coin);
		} else validateNeccassryParameterFilledFailAction(coin);
	}
	
	protected void responseToClient(HttpServletResponse response,String data){
		if(response instanceof EntryTestSimulatedResponse){
			QueneLogger.getLogger().log("Response Data = " + data);
		} else {
			try {
				response.getOutputStream().write(data.getBytes());
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} catch (Exception e) {
				except("Response Data To Client Fail,Error = " + e.getMessage(), this.getClass().getName());
				exportExceptionText(e);
			}
		}
	}
	private void logRequest(HttpServletRequest req,Object httpMethod){
		Iterator<String> iter = req.getParameterMap().keySet().iterator();
		StringBuffer text = new StringBuffer("");
		while(iter.hasNext()){
			String key = iter.next();
			String value = req.getParameter(key);
			text.append(key+"="+value+"&");
		}
		log("Receive Request("+(httpMethod == HttpMethod.GET ? "GET" : "POST")+") For Entry("+this.getClass().getName()+") "+req.getRemoteAddr()+",Parameter = " + text.toString());
	}
	
	public void log(String data){
		String moduleText = "Entry"; 
		data = "<Log> <" + moduleText + "> " + StringManager.getSystemDate() + " ------> " + data;
		if (logger == null) logger = QueneLogger.getLogger();
		logger.log(data);
	}
	
	public void except(String data){
		data = "<Error> " + StringManager.getSystemDate() + " (At " + getClass().getName() + ")" + " ------> " + data;
		if (logger == null) logger = QueneLogger.getLogger();
		logger.log(data);
	}
	
	public void except(String data,String classname){
		data = "<Error> " + StringManager.getSystemDate() + " (At " + classname + ")" + " ------> " + data;
		if (logger == null) logger = QueneLogger.getLogger();
		logger.log(data);
	}
	public void exportExceptionText(Exception e){
		if(exceptLogger == null)exceptLogger = ExceptionLogger.getLogger();
		if(exceptLogger != null) {
			exceptLogger.writeException(e);
		}
	}
	
	public void testDoGet(EntryTestSimulatedRequest req,EntryTestSimulatedResponse res) throws ServletException, IOException{
		this.doGet(req, res);
	}
	public void testDoPost(EntryTestSimulatedRequest req,EntryTestSimulatedResponse res) throws ServletException, IOException{
		this.doPost(req, res);
	}
	
	
	protected boolean isListWithContent(List list){
		return list != null && !list.isEmpty();
	}
	
	protected boolean isTextStartWith(String data,String startWithText){
		if(data != null && startWithText != null){
			return data.trim().toUpperCase().startsWith(startWithText.trim().toUpperCase());
		} else return false;
	}
	
	protected boolean isTextEndWith(String data,String endWithText){
		if(data != null && endWithText != null){
			return data.trim().toUpperCase().endsWith(endWithText.trim().toUpperCase());
		} else return false;
	}
	
	protected String trimEndChar(String data,String trimTargetChar){
		if(data.length() > 0)  return data.toUpperCase().endsWith(trimTargetChar.toUpperCase()) ? data.substring(0, data.length() - trimTargetChar.length()) : data;
		else return data;
	}
	
	protected String[] trimSplit(String data,String splitChar){
		String[] ar = data.split(splitChar);
		for(int i = 0 ; i < ar.length; i++){
			ar[i] = ar[i].trim();
		}
		return ar;
	}
	
	protected boolean compareValue(String value1,String value2){
		if(value1 == null || value2 == null){
			return value1 == value2;
		} else {
			return value1.trim().toUpperCase().equals(value2.trim().toUpperCase());
		}
	}
	
	
	protected String getNotEmptyValue(Object data,String nvsl){
		if(data == null) return nvsl;
		return data.toString().trim();
	}
	
	
	
}
