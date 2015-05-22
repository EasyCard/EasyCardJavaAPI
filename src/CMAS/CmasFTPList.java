package CMAS;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;




import Comm.FTP.Ftp4j;
import CMAS.ConfigManager;

public class CmasFTPList extends Thread{
	
	static Logger logger = Logger.getLogger(CmasFTPList.class);
	
	private Ftp4j ftps = new Ftp4j(null);
	private boolean isFtpsOK = false;
	
	ArrayList<CmasDataSpec.SubTag5595> t5595s = null;
	//ArrayList<Properties> cfgLsit = null;
	private ConfigManager configManager = null;
	private String url = null;
	private String bkIP=null;
	private int port = 0;
	private String id = null;
	private String pwd = null;
	
	public CmasFTPList(String url,
			String bkIP,
			int port, 
			String id,
			String pwd, 
			ArrayList<CmasDataSpec.SubTag5595> t5595s,
			ConfigManager config){
		
		this.url = url;
		this.bkIP = bkIP;
		this.port = port;
		this.id = id;
		this.pwd = pwd;
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
			for(CmasDataSpec.SubTag5595 t5595:t5595s)
			{						
				String t559501=t5595.getT559502();
				/*if(t559501.equalsIgnoreCase("TM10")){ //ssl CA Cert
					if(ftps.download(rootDir + t5595.getT559503(), ConfigManager.CA_CERT)){ // download to
						logger.info("download CA Cert OK");
						easycardApi.setProperty("SSL_CA_Ver", t5595.getT559501());
					} else logger.error("download CA Cert fail");
				} else*/ 
				if(t559501.equalsIgnoreCase("TM11")){
					if(ftps.download(rootDir + t5595.getT559503(), ConfigManager.CARD_NUMBER_BLACKLIST)){ // download to
						logger.info("download BlackList OK");
						configManager.setBlackListVersion(t5595.getT559501());
		
					} else logger.error("download BlackList fail");
				} else if(t559501.equalsIgnoreCase("TM12")) {
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
