package ec.log;

import java.util.Properties;
import org.apache.log4j.*;
public class LogManager 
{
  private static Logger logger;
  
  public static void setLogger(String property_file_name,String setup_name){
    //property_file_name  --> *.properties
    Properties propt = new Properties();
    try{
      propt.load(LogManager.class.getClassLoader().getResourceAsStream(property_file_name));
      PropertyConfigurator.configure(propt);
    } catch(Exception e){
      System.out.println("log initial error");
      e.printStackTrace();
    }
    logger = Logger.getLogger(setup_name);
  }
  
  
  public static void writelog(String type,String log){
    type = type.toLowerCase();
    if(type.equals("info")){
      writeInfo(log);
    } else if(type.equals("warn")){
      writeWarn(log);
    } else if(type.equals("error")){
      writeError(log);
    } else if(type.equals("debug")){
      writeDebug(log);
    }
  }
  
  private static void writeDebug(String log){
    logger.debug(log);
  }
  private static void writeInfo(String log){
    logger.info(log);
  }
  private static void writeWarn(String log){
    logger.warn(log);
  }
  private static void writeError(String log){
    logger.error(log);
  }
}