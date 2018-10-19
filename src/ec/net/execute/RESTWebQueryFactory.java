package ec.net.execute;

import java.net.Authenticator;
import java.net.ProtocolException;

import ec.net.auth.HttpAuthorization;
import ec.system.RunningPlatform;

public class RESTWebQueryFactory extends WebQueryFactory {

	public final static String HTTP_REQUEST_METHOD_GET = "GET";
	public final static String HTTP_REQUEST_METHOD_POST = "POST";
	public final static String HTTP_REQUEST_METHOD_PUT = "PUT";
	public final static String HTTP_REQUEST_METHOD_DELETE = "DELETE";
	
	public final static String ACCEPT_TYPE_JSON = "application/json";
	
	
	public RESTWebQueryFactory(String host,String username,String password) throws ProtocolException {
		super.setHost(host);
		setAuthorization(username,password);
	}

	private void setAuthorization(String username,String password) throws ProtocolException{
		Authenticator.setDefault(new HttpAuthorization(username,password));
	}
	
	public void setReturnDataType(String ACCEPT_TYPE){
		urlCon.setRequestProperty("Accept", ACCEPT_TYPE);
	}
	
	public void setHttpMethod(String HTTP_REQUEST_METHOD) throws ProtocolException{
		urlCon.setRequestMethod(HTTP_REQUEST_METHOD);
	}

	public String queryWeb() {
		try {
			return Read();
		} catch (Exception e) {
			this.exportExceptionText(e);
			if(RunningPlatform.isInDebugMode()) except("Query Restful Web API Fail,Error = " + e.getMessage());
			return WebQueryFactory.QueryFailReason.CONNECTION_FAIL.toString();
		}
	}
}
