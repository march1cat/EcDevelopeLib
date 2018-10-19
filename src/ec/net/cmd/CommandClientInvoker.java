package ec.net.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import ec.net.execute.WebQueryFactory;
import ec.parser.JsonFactory;
import ec.system.Basis;

public class CommandClientInvoker extends Basis{

	private int port = -1;
	private List<Command> cmds = null;
	private String serviceIP = "127.0.0.1";
	
	public CommandClientInvoker(int port){
		this.port = port;
	}
	
	public void setServiceIP(String ip){
		serviceIP = ip;
	}
	
	public void reloadfunctions(){
		WebQueryFactory web = new WebQueryFactory();
		web.setHost("http://"+serviceIP+":"+port+"/functions");
		String text = web.queryWeb("");
		try {
			JsonFactory json = new JsonFactory(text);
			String result = json.getObjectValue("result");
			if(compareValue(result, "ok")) {
				List<JsonFactory> datas = json.getJsonDataArrayList("data");
				if(isListWithContent(datas)) {
					cmds = new ArrayList<>();
					for(JsonFactory data : datas){
						Command cmd = new Command(data.getObjectValue("URI"), data.getObjectValue("NAME"));
						cmds.add(cmd);
					}
				} else {
					throw new Exception("Functions  amount is empty");
				}
			} else {
				throw new Exception("Load Function Fail,Server res data = " + text);
			}
		} catch (Exception e) {
			this.exportExceptionText(e);
		}
	}
	
	public boolean invokeCommand(String uri) throws Exception{
		WebQueryFactory web = new WebQueryFactory();
		web.setHost("http://"+serviceIP+":"+port+"/" + uri);
		String text = web.queryWeb("");
		JsonFactory json = new JsonFactory(text);
		String result = json.getObjectValue("result");
		if(compareValue(result, "ok")) {
			return true;
		} else {
			throw new Exception("Invoke Command Fail,Uri = " + uri);
		}
	}

	public List<Command> getCmds() {
		return cmds;
	}
	
	public List<Map<String,String>> getCmdsInMaps() {
		List<Map<String,String>> ls = null;
		if(isListWithContent(cmds)){
			for (Command cmd : cmds){
				if(ls == null) ls = new ArrayList<>();
				Map<String,String> mp = new HashMap<>();
				mp.put("URI", cmd.getUri());
				mp.put("NAME", cmd.getName());
				ls.add(mp);
			}
		}
		return ls;
	}
	
	
	
	
	
}
