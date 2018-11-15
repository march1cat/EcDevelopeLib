package ec.system;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ec.file.ConfigManager;
import ec.file.EcDirector;

public abstract class ApplicationConfig extends Basis{

	public static String ConfigFile = null;
	private boolean forceRegenerate = false;
	
	
	public ApplicationConfig(String ConfigFile){
		this.ConfigFile = ConfigFile;
	}
	
	public ApplicationConfig(String ConfigFile,boolean forceRegenerate){
		this.ConfigFile = ConfigFile;
		this.forceRegenerate = forceRegenerate;
	}
	
	
	public boolean initial(){
		try{
			File configF = new File(ConfigFile + ".conf");
			if(forceRegenerate && configF.exists())  configF.delete();
			
			if(configF.exists()) {
				if(DeveloperMode.isON()) log("Start Read Config File["+ConfigFile + ".conf]",Module.EC_LIB);
				Map<String,String> config = loadConfig();
				setNotConifParameterDefaultValue();
				boolean isDone = fillOtherConfigParameter(config);
				if(isDone) isDone = finallyOnIni();
				if(DeveloperMode.isON() && isDone) showAllConfiguration();
				return isDone;
			} else {
				log("Config File Not Exist,Auto Generate it!!,File Uri = " + ConfigFile + ".conf",Module.EC_LIB);
				List<Field> notConfigParameters = null;
				Field[] f_ar = getClass().getFields();
				
				setNotConifParameterDefaultValue();
				for(Field f : f_ar){
					if(f.getType() == int.class || f.getType() == Integer.class){
						if(Integer.parseInt(f.get(this).toString()) == 0) continue;
					} else {
						if(f.get(this) == null) continue;  
					}
					if(notConfigParameters == null) notConfigParameters = new ArrayList<>();
					notConfigParameters.add(f);
				}
				
				setDefaultForGenerate();
				StringBuffer txt = new StringBuffer();
				for(Field f : f_ar){
					if(notConfigParameters!= null && notConfigParameters.contains(f)) continue;
					if(f.getType() == EcDirector.class){
						txt.append(f.getName() + "="+(f.get(this) != null ? ((EcDirector)f.get(this)).Uri() : "Value")+"\r\n");
					} else {
						txt.append(f.getName() + "="+(f.get(this) != null ? f.get(this) : "Value")+"\r\n");
					}
				}
				FileOutputStream fos = new FileOutputStream(configF);
				fos.write(txt.toString().getBytes());
				fos.flush();
				fos.close();
				log("Generate Config Success!!,Please Restart System!!",Module.EC_LIB);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	private Map<String,String> loadConfig() throws Exception {
		//setNotConifParameterDefaultValue();
		Map<String,String> config = ConfigManager.getConfigMap(ConfigFile);
		Iterator<String> iter = config.keySet().iterator();
		Field[] f_ar = getClass().getFields();
		while(iter.hasNext()){
			String attrName = iter.next();
			String attrValue = config.get(attrName);
			for(Field f : f_ar){
				if(compareValue(attrName, f.getName())){
					if(f.getType() == String.class){
						f.set(this, attrValue);
					} else if(f.getType() == EcDirector.class){
						f.set(this, new EcDirector(attrValue,true));
					} else if(f.getType() == int.class || f.getType() == Integer.class){
						f.set(this, Integer.parseInt(attrValue));
					} else if(f.getType() == boolean.class || f.getType() == Boolean.class){
						try {
							f.set(this, Integer.parseInt(attrValue) == 1);
						} catch(Exception e){
							try{
								f.set(this, Boolean.parseBoolean(attrValue));
							} catch(Exception e1){
								f.set(this, false);
							}
						}
					} else {
						log(f.getName() + " " + f.getType());
					}
				}
			}
		}
		return config;
	}
	
	private void showAllConfiguration(){
		log("System Config Parameter : ");
		Field[] f_ar = getClass().getFields();
		for(Field f : f_ar){
			if(this.compareValue("ConfigFile", f.getName())) continue;
			try {
				if(f.getType() == String.class || f.getType() == int.class || f.getType() == Integer.class){
					log(f.getName() + "=" + f.get(this));
				} else if(f.getType() == EcDirector.class){
					log(f.getName() + "[EcDirector]=" + ((EcDirector)f.get(this)).Uri());
				} else if(f.getType() == boolean.class || f.getType() == Boolean.class){
					log(f.getName() + "=" + f.get(this));
				} else {
					log(f.getName() + " " + f.getType());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void log(String data) {
		System.out.println(data);
	}
	
	protected void setNotConifParameterDefaultValue(){
		//Wait to be Override
	}
	
	protected void setDefaultForGenerate(){
		//Wait to be Override
	}
	
	protected boolean fillOtherConfigParameter(Map<String,String> config){
		//Wait to be Override
		return true;
	}
	
	protected boolean finallyOnIni(){
		//Wait to be Override
		return true;
	}
	
}
