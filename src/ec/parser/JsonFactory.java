package ec.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ec.file.EcDirector;
import ec.file.FileManager;
import ec.string.StringManager;
import ec.system.Basis;

public class JsonFactory extends Basis{
		
	private JSONObject json_obj = null;
	
	public JsonFactory(){
		json_obj = new JSONObject(new HashMap());
	}
	//initial by map
	public JsonFactory(Map data){
			json_obj = new JSONObject(data);
	}
	public JsonFactory(String json_str) throws JSONException{
		decodeJSON(json_str);
	}
	
	public JsonFactory(String json_str,EcDirector errExportFile) throws Exception{
		try{
			decodeJSON(json_str);
		} catch(JSONException e){
			String exceptTime = StringManager.getSystemDate("yyyy-MM-dd HH_mm_ss");
			FileManager.FileWrite(errExportFile.Uri() + "JSONError_" + exceptTime + ".html", json_str + "\r\n" + e.getMessage());
			throw e;
		}
	}
	
	//initial by java bean
	public JsonFactory(Object JavaBean){
		json_obj = new JSONObject(JavaBean);
	}
	//=====================================================
	public void setJSONVariable(String key,Object value) throws JSONException{
		json_obj.put(key, value);
	}
	public void insertJSONFac(String key,JsonFactory fac) throws JSONException{
		setJSONVariable(key,fac.encodeJSON());
	}
	//=====================================================
	public void decodeJSON(String json_string) throws JSONException{
		json_obj = new JSONObject(json_string);
	}
	
	public String encodeJSON(){
		return json_obj.toString();
	}
	//===========================================================
	public String getObjectValue(String key){
		try{
			Object value = json_obj.get(key);
			return value.toString();
		} catch(JSONException e){
			return null;
		}
	}
	
	public JsonFactory getSubJFactory(String key){
		try{
			Object value = json_obj.get(key);
			if(value!=null) return new JsonFactory(value.toString());
			else return null;
		} catch(JSONException e){
			return null;
		}
	}
	public ArrayList<JsonFactory> getJsonDataArrayList(String key){
		try{
			JSONArray json_ar = json_obj.getJSONArray(key);
			ArrayList<JsonFactory> arlist = new ArrayList<JsonFactory>();
			for(int i=0;i < json_ar.length();i++){
				arlist.add(new JsonFactory(json_ar.get(i).toString()));
			}
			return arlist;
		} catch(JSONException e){
			return null;
		}
	}
	//===========================================================
	public Map<String,String> transToMap() throws JSONException{
		Map<String,String> mp = new HashMap<>();
		Iterator<String> iter = json_obj.keys();
		while (iter.hasNext()) {
			String key = iter.next();
			mp.put(key, json_obj.getString(key));
		}
		return mp;
	}
	
	
	
}
