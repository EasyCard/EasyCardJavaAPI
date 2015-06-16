package com.easycard.pc.CMAS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.easycard.utilities.Util;

public class ConfigManager implements IConfigManager{

	static Logger logger = Logger.getLogger(ConfigManager.class);
	private ArrayList<Properties> cfgList = null;
	
	//public static final String ROOT_DIR="config/";
	public static final String EASYCARD_API_FILE = ROOT_DIR + "EasycardAPI.properties"; 
	public static final String TXN_INFO_FILE = ROOT_DIR + "TxnInfo.properties";
	public static final String HOST_DEVE_INFO_FILE = ROOT_DIR + "HostDeve.properties";
	public static final String HOST_TEST_INFO_FILE = ROOT_DIR + "HostTest.properties";
	public static final String HOST_PROD_INFO_FILE = ROOT_DIR + "HostProduction.properties";
	public static final String USER_DEFINITION_FILE = ROOT_DIR + "UserDefinition.properties";
	
	
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
	
	/*
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
	}*/
	
	/*
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
*/
	@Override
	public boolean initial() {
		// TODO Auto-generated method stub
		logger.info("Start");
		boolean result = true;
		cfgList = new ArrayList<Properties>();
		Properties easyCardApip= new Properties();
		Properties txnInfo = new Properties();
		Properties hostInfo = new Properties();
		Properties userDef = new Properties();
		
		String filename=null;
		try {				
		
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
												
						if(userDef.getProperty("HostEnvironment").equalsIgnoreCase("0")) {
							logger.info("Develop Environment");
							filename = ConfigManager.HOST_DEVE_INFO_FILE;
						}
						else if(userDef.getProperty("HostEnvironment").equalsIgnoreCase("1")) {
							logger.info("Test Environment");
							filename = ConfigManager.HOST_TEST_INFO_FILE;
						}
						else{
							logger.info("Production HostEnvironment");
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
			result = false;
			logger.error("IllegalArgumentException:"+e.getMessage());
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			result = false;
			logger.error("FileNotFoundException:"+e.getMessage());
			e.printStackTrace();
		}
		catch (IOException e) {
			result = false;
			// TODO Auto-generated catch block		
			logger.error("IOException:"+e.getMessage());
			e.printStackTrace();
		}
		catch (NullPointerException e)
		{	
			result = false;
			logger.error("NullPointerException:"+e.getMessage());
			e.printStackTrace();
		}
		catch (Exception e)
		{		
			result = false;
			logger.error("Exception:"+e.getMessage());
			e.printStackTrace();			
		}
		
		logger.info("End");
		return result;
	}

	@Override
	public boolean finish() {
		// TODO Auto-generated method stub
		logger.info("Start");
		boolean result = true;
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
			result = false;
			logger.error(ne.getMessage());
		}
		catch (FileNotFoundException fe) {
			// TODO Auto-generated catch block
			result = false;
			logger.error(fe.getMessage());
		}
		catch (IOException ioe )
		{
			result = false;
			logger.error(ioe.getMessage());
		}		
		logger.info("end");
		return result;
	}

	@Override
	public String getApiName() {
		// TODO Auto-generated method stub
		String name = cfgList.get(ConfigOrder.EASYCARD_API.ordinal()).getProperty("ApiName");
		logger.info("getter:"+name);
		return name;
	}

	@Override
	public void setApiName(String apiName) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.EASYCARD_API.ordinal()).setProperty("ApiName", apiName);
		logger.info("setter:"+apiName);
	}

	@Override
	public String getApiVersion() {
		// TODO Auto-generated method stub
		String ver = cfgList.get(ConfigOrder.EASYCARD_API.ordinal()).getProperty("ApiVer");
		logger.info("getter:"+ver);
		return ver;
	}

	@Override
	public void setApiVersion(String verName) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.EASYCARD_API.ordinal()).setProperty("ApiVer", verName);
		logger.info("setter:"+verName);
	}

	@Override
	public String getBlackListVersion() {
		// TODO Auto-generated method stub
		String ver = cfgList.get(ConfigOrder.EASYCARD_API.ordinal()).getProperty("BlackListVer");
		logger.info("getter:"+ver);
		return ver;
	}

	@Override
	public void setBlackListVersion(String verName) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.EASYCARD_API.ordinal()).setProperty("BlackListVer", verName);
		logger.info("setter:"+verName);
	}
/*
	@Override
	public String getCompanyBranchID() {
		// TODO Auto-generated method stub
		String id = cfgList.get(ConfigOrder.EASYCARD_API.ordinal()).getProperty("CompanyBranchID");
		logger.info("getter:"+id);
		return id;
	}

	@Override
	public void setCompanyBranchID(String id) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.EASYCARD_API.ordinal()).setProperty("CompanyBranchID", id);
		logger.info("setter:"+id);
	}
*/
	@Override
	public String getNewLocationID() {
		// TODO Auto-generated method stub
		String id = cfgList.get(ConfigOrder.EASYCARD_API.ordinal()).getProperty("NewLocationID");
		logger.info("getter:"+id);
		return id;
	}

	@Override
	public void setNewLocationID(String id) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.EASYCARD_API.ordinal()).setProperty("NewLocationID", id);
		logger.info("setter:"+id);
	}

	@Override
	public String getReaderID() {
		// TODO Auto-generated method stub
		String id = cfgList.get(ConfigOrder.EASYCARD_API.ordinal()).getProperty("ReaderID");
		logger.info("getter:"+id);
		return id;
	}

	@Override
	public void setReaderID(String id) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.EASYCARD_API.ordinal()).setProperty("ReaderID", id);
		logger.info("setter:"+id);
	}

	@Override
	public String getHostUrl() {
		// TODO Auto-generated method stub
		String id = cfgList.get(ConfigOrder.HOST_INFO.ordinal()).getProperty("HostUrl");
		logger.info("getter:"+id);
		return id;
	}

	@Override
	public void setHostUrl(String url) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.HOST_INFO.ordinal()).setProperty("HostUrl", url);
		logger.info("setter:"+url);
	}

	@Override
	public String getHostIP() {
		// TODO Auto-generated method stub
		String ip = cfgList.get(ConfigOrder.HOST_INFO.ordinal()).getProperty("HostIP");
		logger.info("getter:"+ip);
		return ip;
	}

	@Override
	public void setHostIP(String ip) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.HOST_INFO.ordinal()).setProperty("HostIP", ip);
		logger.info("setter:"+ip);
	}

	@Override
	public String getHostPort() {
		// TODO Auto-generated method stub
		String port = cfgList.get(ConfigOrder.HOST_INFO.ordinal()).getProperty("HostPort");
		logger.info("getter:"+port);
		return port;
	}

	@Override
	public void setHostPort(String port) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.HOST_INFO.ordinal()).setProperty("HostPort", port);
		logger.info("setter:"+port);
	}

	@Override
	public String getFtpUrl() {
		// TODO Auto-generated method stub
		String url = cfgList.get(ConfigOrder.HOST_INFO.ordinal()).getProperty("FtpUrl");
		logger.info("getter:"+url);
		return url;
	}

	@Override
	public void setFtpUrl(String url) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.HOST_INFO.ordinal()).setProperty("FtpUrl", url);
		logger.info("setter:"+url);
	}

	@Override
	public String getFtpIP() {
		// TODO Auto-generated method stub
		String ip = cfgList.get(ConfigOrder.HOST_INFO.ordinal()).getProperty("FtpIP");
		logger.info("getter:"+ip);
		return ip;
	}

	@Override
	public void setFtpIP(String ip) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.HOST_INFO.ordinal()).setProperty("FtpIP", ip);
		logger.info("setter:"+ip);
	}

	@Override
	public String getFtpLoginID() {
		// TODO Auto-generated method stub
		String id = cfgList.get(ConfigOrder.HOST_INFO.ordinal()).getProperty("FtpLoginID");
		logger.info("getter:"+id);
		return id;
	}

	@Override
	public void setFtpLoginID(String id) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.HOST_INFO.ordinal()).setProperty("FtpLoginID", id);
		logger.info("setter:"+id);
	}

	@Override
	public String getFtpLoinPwd() {
		// TODO Auto-generated method stub
		String pwd = cfgList.get(ConfigOrder.HOST_INFO.ordinal()).getProperty("FtpLoinPwd");
		logger.info("getter:"+pwd);
		return pwd;
	}

	@Override
	public void setFtpLoinPwd(String pwd) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.HOST_INFO.ordinal()).setProperty("FtpLoginID", pwd);
		logger.info("setter:"+pwd);
	}

	@Override
	public String getTMSerialNo() {
		// TODO Auto-generated method stub
		String no = cfgList.get(ConfigOrder.TXN_INFO.ordinal()).getProperty("TMSerialNo");
		logger.info("getter:"+no);
		return no;
	}

	@Override
	public void setTMSerialNo(String no) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.TXN_INFO.ordinal()).setProperty("TMSerialNo", no);
		logger.info("setter:"+no);
	}

	@Override
	public String getBatchNo() {
		// TODO Auto-generated method stub
		String no = cfgList.get(ConfigOrder.TXN_INFO.ordinal()).getProperty("BatchNo");
		if(no.equals("00000000")){
			logger.info("batchNo init to yyMMDD01");
			String date = Util.sGetDateShort();
			no = date + "01";
		}
		logger.info("getter:"+no);
		return no;
	}

	@Override
	public void setBatchNo(String no) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.TXN_INFO.ordinal()).setProperty("BatchNo", no);
		logger.info("setter:"+no);
	}

	@Override
	public String getReaderPort() {
		// TODO Auto-generated method stub
		String port = cfgList.get(ConfigOrder.USER_DEF.ordinal()).getProperty("ReaderPort");
		logger.info("getter:"+ port);
		return port;
	}

	@Override
	public void setReaderPort(String port) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.USER_DEF.ordinal()).setProperty("ReaderPort", port);
		logger.info("setter:"+port);
	}

	@Override
	public String getTMLocationID() {
		// TODO Auto-generated method stub
		String id = cfgList.get(ConfigOrder.USER_DEF.ordinal()).getProperty("TMLocationID");
		id = new String(Util.paddingChar(id, false, (byte) '0', 10));
		logger.info("getter:"+ id);
		return id;
	}

	@Override
	public void setTMLocationID(String id) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.USER_DEF.ordinal()).setProperty("TMLocationID", id);
		logger.info("setter:"+id);
	}

	@Override
	public String getTMID() {
		// TODO Auto-generated method stub
		String id = cfgList.get(ConfigOrder.USER_DEF.ordinal()).getProperty("TMID");
		logger.info("getter:"+ id);
		return id;
	}

	@Override
	public void setTMID(String id) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.USER_DEF.ordinal()).setProperty("TMID", id);
		logger.info("setter:"+id);
	}

	@Override
	public String getTMAgentNo() {
		// TODO Auto-generated method stub
		String no = cfgList.get(ConfigOrder.USER_DEF.ordinal()).getProperty("TMAgentNo");
		logger.info("getter:"+ no);
		return no;
	}

	@Override
	public void setTMAgentNo(String no) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.USER_DEF.ordinal()).setProperty("TMAgentNo", no);
		logger.info("setter:"+no);
	}

	@Override
	public String getHostEnvironment() {
		// TODO Auto-generated method stub
		String e = cfgList.get(ConfigOrder.USER_DEF.ordinal()).getProperty("HostEnvironment");
		logger.info("getter:"+ e);
		return e;
	}

	@Override
	public void setHostEnvironment(String e) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.USER_DEF.ordinal()).setProperty("HostEnvironment", e);
		logger.info("setter:"+e);
	}

	@Override
	public String getNewServiceProviderID() {
		// TODO Auto-generated method stub
		String id = cfgList.get(ConfigOrder.USER_DEF.ordinal()).getProperty("NewServiceProviderID");
		logger.info("getter:"+ id);
		return id;
	}

	@Override
	public void setNewServiceProviderID(String id) {
		// TODO Auto-generated method stub
		cfgList.get(ConfigOrder.USER_DEF.ordinal()).setProperty("HostEnvironment", id);
		logger.info("setter:"+id);
	}
	
	
}
