package com.easycard.pc.CMAS;

import java.io.File;
import java.util.ArrayList;
import org.apache.log4j.Logger;


import com.easycard.pc.communication.ftp.Ftp4j;
import com.easycard.pc.CMAS.ConfigManager;
import com.easycard.reader.BigBlackList;
//import com.easycard.utilities.Util;

public class CmasFTPList extends Thread{
	
	static Logger logger = Logger.getLogger(CmasFTPList.class);
	
	private Ftp4j ftps = new Ftp4j(null);
	private boolean isFtpsOK = false;
	
	ArrayList<CmasTag.SubTag5595> t5595s = null;
	//ArrayList<Properties> cfgLsit = null;
	private IConfigManager configManager = null;
	private String url = null;
	private String bkIP=null;
	private int port = 0;
	private String id = null;
	private String pwd = null;
	
	public CmasFTPList(ArrayList<CmasTag.SubTag5595> t5595s,
			IConfigManager config){
		
		this.url = config.getFtpUrl();
		this.bkIP = config.getFtpIP();
		this.port = config.getFtpPort();
		this.id = config.getFtpLoginID();
		this.pwd = config.getFtpLoginPwd();
		this.t5595s = t5595s;
		this.configManager = config;
	}
	
	/**
	 * @return the isFtpsOK
	 */
	public boolean isFtpsOK() {
		return isFtpsOK;
	}

	/**
	 * @param isFtpsOK the isFtpsOK to set
	 */
	public void setFtpsOK(boolean isFtpsOK) {
		this.isFtpsOK = isFtpsOK;
	}

	private void startDownload(){
		
		try{			
			
			String rootDir = ftps.getCurrentDIR();
			

			logger.info("FTP Connect OK");
			for(CmasTag.SubTag5595 t5595:t5595s)
			{						
				String tagName=t5595.getT559502();
				/*if(t559501.equalsIgnoreCase("TM10")){ //ssl CA Cert
					if(ftps.download(rootDir + t5595.getT559503(), ConfigManager.CA_CERT)){ // download to
						logger.info("download CA Cert OK");
						easycardApi.setProperty("SSL_CA_Ver", t5595.getT559501());
					} else logger.error("download CA Cert fail");
				} else*/ 
				if(tagName.equalsIgnoreCase("TM11")){
					
					if(ftps.download(rootDir + t5595.getT559503(), ConfigManager.CARD_NUMBER_BLACKLIST_TEMP)){ // download to
						BigBlackList bigBlc = new BigBlackList();
						if(bigBlc.configure(BigBlackList.class.getClassLoader().getResourceAsStream(ConfigManager.CARD_NUMBER_BLACKLIST_TEMP))==false){
							// maybe blackFile wrong!!!
							logger.error("FTPS download blackList file something wrong!!!");
						} else {	
							File preBlc = new File(ConfigManager.CARD_NUMBER_BLACKLIST_PREVER);
							File blc = new File(ConfigManager.CARD_NUMBER_BLACKLIST);
							File blcTemp = new File(ConfigManager.CARD_NUMBER_BLACKLIST_TEMP);
							if(preBlc.exists()==true && preBlc.delete()==false) logger.error(ConfigManager.CARD_NUMBER_BLACKLIST_PREVER+" deleted fail");
							if(blc.renameTo(preBlc) == false) logger.error(ConfigManager.CARD_NUMBER_BLACKLIST+" renameTo "+ConfigManager.CARD_NUMBER_BLACKLIST_PREVER);
							if(blcTemp.renameTo(blc) ==false) logger.error(ConfigManager.CARD_NUMBER_BLACKLIST_TEMP+" renameTo "+ConfigManager.CARD_NUMBER_BLACKLIST);
							
							logger.info("download BlackList OK");
							configManager.setBlackListVersion(t5595.getT559501());
							
						}
					} else logger.error("download BlackList fail");
				} else if(tagName.equalsIgnoreCase("TM12")) {
					if(ftps.download(rootDir + t5595.getT559503(), ConfigManager.API_JAR)){ // download to
						logger.info("download API OK");						
						configManager.setApiVersion(t5595.getT559501());
					} else logger.error("download API Fail");
				}
			}
		} catch(Exception e) {
			
			logger.error(e.getMessage());
		}
		return;
	}
	
	public void disconnect(){		
		ftps.disconnect();
	}
	
	
	public void run()
	{
		int retry = 4;//twice try url, twice try ip
		int cnt = 0;
			
		while(cnt < retry)
		{
			if(ftps.connect2Server(url,port,id,pwd,true) == false){
				logger.error("FTP Connect Fail no."+cnt);
				cnt++;
				continue;
			}
			if(cnt == 2) {
				logger.error("url connect fail, change backup IP:"+this.bkIP);
				url = bkIP;
			}
			
			if(cnt == retry) {
				logger.error("ftps connect fail");
				this.setFtpsOK(false);
			}
			else {
				logger.info("ftps connect OK");
				this.setFtpsOK(true);
				break;
			}
		}
		
		if(this.isFtpsOK){
			startDownload();
		}
	}
	
	
	
}
