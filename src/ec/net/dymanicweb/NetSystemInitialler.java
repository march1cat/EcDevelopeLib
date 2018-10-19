package ec.net.dymanicweb;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public abstract class NetSystemInitialler extends HttpServlet{

	@Override
	public void init() throws ServletException {
		startInit();
	}
	
	protected String getInitParameterByName(String name){
		return getServletConfig().getInitParameter(name);
	}
	
	
	protected abstract void startInit();
}
