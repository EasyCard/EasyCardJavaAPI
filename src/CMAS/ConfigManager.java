package CMAS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigManager {

	static Logger logger = Logger.getLogger(ConfigManager.class);
	private ArrayList<Properties> cfgList = null;
	
	public static final String ROOT_DIR="config/";
	public static final String EASYCARD_API_FILE = ROOT_DIR + "EasycardAPI.properties"; 
	public static final String TXN_INFO_FILE = ROOT_DIR + "TxnInfo.properties";
	public static final String HOST_DEVE_INFO_FILE = ROOT_DIR + "HostDeve.properties";
	public static final String HOST_TEST_INFO_FILE = ROOT_DIR + "HostTest.properties";
	public static final String HOST_PROD_INFO_FILE = ROOT_DIR + "HostProduction.properties";
	public static final String USER_DEFINITION_FILE = ROOT_DIR + "UserDefinition.properties";
	public static final String CARD_NUMBER_BLACKLIST = ROOT_DIR + "CardNumber.black";
	public static final String CA_CERT = ROOT_DIR + "CA.cer";
	public static final String API_JAR = ROOT_DIR + "EasyCardApi.jar";
	public static final String LOG4J_CONFIG_FILE = ROOT_DIR+"log4j.properties";
	
	public enum ConfigOrder
	{
		USER_DEF,
		EASYCARD_API,
		TXN_INFO,
		HOST_INFO,
	}
	
	public ConfigManager(){
		logger.info("Start");
		
		logger.info("End");
	}
	
	public ArrayList<Properties> prepareConfig()
	{
		logger.info("Start");
		cfgList = new ArrayList<Properties>();
		Properties easyCardApip= new Properties();
		Properties txnInfo = new Properties();
		Properties hostInfo = new Properties();
		Properties userDef = new Properties();
		
		String filename=null;
		try {
			//txnInfo.load(ConfigManager.class.getResourceAsStream("../../config/TxnInfo.properties"));	
		
			int len = ConfigOrder.values().length;
			for(int i=0; i<len; i++)
			{
				ConfigOrder c = ConfigOrder.values()[i];
				switch(c)
				{
					case EASYCARD_API:
						//easyCardApip.load(ConfigManager.class.getResourceAsStream(ConfigManager.EASYCARD_API_FILE));
						easyCardApip.load(ConfigManager.class.getClassLoader().getResourceAsStream(ConfigManager.EASYCARD_API_FILE));
						cfgList.add(easyCardApip);
						break;
					case TXN_INFO:
						txnInfo.load(ConfigManager.class.getClassLoader().getResourceAsStream(ConfigManager.TXN_INFO_FILE));
						cfgList.add(txnInfo);
						break;				
					case HOST_INFO:	
												
						if(userDef.getProperty("Environment").equalsIgnoreCase("0")) {
							logger.info("Develop Environment");
							filename = ConfigManager.HOST_DEVE_INFO_FILE;
						}
						else if(userDef.getProperty("Environment").equalsIgnoreCase("1")) {
							logger.info("Test Environment");
							filename = ConfigManager.HOST_TEST_INFO_FILE;
						}
						else{
							logger.info("Production Environment");
							filename = ConfigManager.HOST_PROD_INFO_FILE;
						}
						hostInfo.load(ConfigManager.class.getClassLoader().getResourceAsStream(filename));
						cfgList.add(hostInfo);
						break;	
					
					case USER_DEF://because UserDefine file would not package in jar file
						//userDef.load(ConfigManager.class.getClassLoader().getResourceAsStream(ConfigManager.USER_DEFINITION_FILE));
						userDef.load(ConfigManager.class.getClassLoader().getResourceAsStream(ConfigManager.USER_DEFINITION_FILE));
						//String filepath = System.getProperty("user.dir")+"\\"+ConfigManager.USER_DEFINITION_FILE;
						//logger.debug("UserDefine file path:"+filepath);
						//userDef.load(new FileInputStream(filepath));
						cfgList.add(userDef);
						break;
					default:
						logger.error("oh! maybe some Properties forgot to add2Arraylist");
						break;
				}
				
			}
			
		} 
		catch(IllegalArgumentException e)
		{			
			logger.error("IllegalArgumentException:"+e.getMessage());
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			logger.error("FileNotFoundException:"+e.getMessage());
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block		
			logger.error("IOException:"+e.getMessage());
			e.printStackTrace();
		}
		catch (NullPointerException e)
		{	
			logger.error("NullPointerException:"+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e)
		{		
			logger.error("Exception:"+e.getMessage());
			e.printStackTrace();			
		}
		
		logger.info("End");
		return cfgList;
	}
	
	public void saveConfig(){
		
		logger.info("Start");
		Properties p = cfgList.get(ConfigOrder.TXN_INFO.ordinal());
		Properties p2 = cfgList.get(ConfigOrder.EASYCARD_API.ordinal());
		
		//save TxnInfo pro.
		try{
			File f = new File(ConfigManager.TXN_INFO_FILE);
			FileOutputStream os;			
			os = new FileOutputStream(f);			 		
			p.store(os, null);			
			os.close();
			
			f = new File(ConfigManager.EASYCARD_API_FILE);			
			os = new FileOutputStream(f);			 		
			p2.store(os, null);			
			os.close();
		}
		catch(NullPointerException ne)
		{
			logger.error(ne.getMessage());
		}
		catch (FileNotFoundException fe) {
			// TODO Auto-generated catch block
			logger.error(fe.getMessage());
		}
		catch (IOException ioe )
		{
			logger.error(ioe.getMessage());
		}		
		logger.info("end");
	}
	
	
}
