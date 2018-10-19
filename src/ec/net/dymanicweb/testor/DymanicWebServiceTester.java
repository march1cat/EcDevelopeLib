package ec.net.dymanicweb.testor;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;

import ec.net.dymanicweb.HttpEntry;

public class DymanicWebServiceTester {
	
	private EntryTestSimulatedRequest req = null;
	private EntryTestSimulatedResponse res = null;
	
	
	public DymanicWebServiceTester(){
		req = new EntryTestSimulatedRequest();
		res = new EntryTestSimulatedResponse();
	}
	
	public void setTestingRequestIPPosition(String ip){
		req.setIP(ip);
	}
	
	public void setRequestParamters(Map<String,String> paraMp){
		req.clearParamter();
		Iterator<String> iter = paraMp.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			String value = paraMp.get(key);
			req.setParameter(key, value);
		}
	}

	public void testPostEntry(HttpEntry entry) throws ServletException, IOException{
		entry.testDoPost(req, res);
	}
	public void testGetEntry(HttpEntry entry) throws ServletException, IOException{
		entry.testDoGet(req, res);
	}
}
