package ec.javascript;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import ec.file.FileManager;

public class JavaScriptCaller {

	ScriptEngineManager mgr = null;
	ScriptEngine jsEngine = null;
	
	public JavaScriptCaller(){
		 mgr = new ScriptEngineManager();
		 jsEngine = mgr.getEngineByName("JavaScript");
	}
	
	public void importJavascriptCode(String jsText) throws ScriptException{
		jsEngine.eval(jsText);
	}
	
	public void importJavaScriptFile(String jsFileUri) throws Exception{
		if(jsFileUri.endsWith(".js")){
			String jsText = FileManager.ReadTextAllData(jsFileUri);
			importJavascriptCode(jsText);
		}
	}
	public String callFunction(String functionName) throws NoSuchMethodException, ScriptException{
		return callFunction(functionName,null);
	}
	
	public String callFunction(String functionName,Object[] para) throws NoSuchMethodException, ScriptException{
		Object jsFunctionReturn = null;
		Invocable invocableEngine = (Invocable)jsEngine;
		if(para != null){
			jsFunctionReturn = invocableEngine.invokeFunction(functionName,para);
		} else {
			jsFunctionReturn = invocableEngine.invokeFunction(functionName);
		}
		return jsFunctionReturn != null ? jsFunctionReturn.toString() : null;
	}
	
}
