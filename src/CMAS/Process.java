package CMAS;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;


import org.apache.log4j.PropertyConfigurator;

import CMAS.CmasDataSpec;
import CMAS.CmasDataSpec.SubTag5596;
import CMAS.CmasFTPList;
import CMAS.CmasKernel;
import Comm.Socket.*;
import ErrorMessage.ApiRespCode;
import ErrorMessage.CmasRespCode;
import ErrorMessage.IRespCode;
import ErrorMessage.ReaderRespCode;
import CMAS.ConfigManager;
import Reader.EZReader;
import Reader.PPR_AuthTxnOffline;
import Reader.PPR_Reset;
import Reader.ApduRecvSender;
import Reader.PPR_SignOn;
import Reader.PPR_SignOnQuery;
import Reader.PPR_TxnReqOffline;
import Utilities.Util;



public class Process {

	 static Logger logger = Logger.getLogger(Process.class);
 
	 private EZReader reader;// = new EZReader(recvSender);
	 private String mTimeZone = "Asia/Taipei"; 
	 private ConfigManager cfgManager = null;
	 private ArrayList<Properties> cfgList = null;
	//RS232 controller was EasyCard
	 public Process()
	 {				 
		 initConfig();
		 
		 Properties userDef = cfgList.get(ConfigManager.ConfigOrder.USER_DEF.ordinal());
		 
		 ApduRecvSender rs = new ApduRecvSender();
		 rs.setPortName(userDef.getProperty("ReaderPort"));
		 reader = new EZReader(rs);
		 logger.info("End");
	 }
	
		
	//RS232 controller was POS
	 public Process(ApduRecvSender rs)
	 {	
		 initConfig();
		reader = new EZReader(rs);
	 }

	
	 private void initConfig() //throws FileNotFoundException, IOException	
	 {
		
		//init log4j configFile
		//PropertyConfigurator.configure(Process.class.getResourceAsStream("log4j.properties"));//for rootDir
		 PropertyConfigurator.configure(Process.class.getClassLoader().getResourceAsStream(ConfigManager.LOG4J_CONFIG_FILE));
		logger.info("=============== API Start ===============");
		
		//init api needed configFile
		cfgManager = new ConfigManager();
		cfgList = cfgManager.prepareConfig();		
		logger.info("End");	
	 }
	
	/**
	 * @return the mTimeZone
	 */
	public String getmTimeZone() {
		logger.info("getter:"+this.mTimeZone);
		return mTimeZone;
	}


	/**
	 * @param mTimeZone the mTimeZone to set
	 */
	public void setmTimeZone(String mTimeZone) {
		logger.info("setter:"+mTimeZone);
		this.mTimeZone = mTimeZone;
	}


	public IRespCode doSignon()
	{
		logger.info("Start");
		int []signOn0800 = {100, 300, 1100, 1101, 1200, 1201, 1300, 1301, 3700, 4100, 4101, 4102, 4103, 4104, 4200, 4210, 4802, 4820, 4823, 4824, 5301, 5307, 5308, 5361, 5362, 5363, 5364, 5365, 5366, 5368, 5369, 5370, 5371, 5501, 5503, 5504, 5510, 5588, 5596, 6000, 6002, 6003, 6004, 6400, 6408};
		int []signOn0820 = {100, 300, 1100, 1101, 1200, 1300, 3700, 4100, 4200, 4210, 4825, 5501, 5503, 5504, 5510, 6406};
		
		IRespCode result;
		//Properties easyCardApip = cfgList.get(ConfigManager.ConfigOrder.EASYCARD_API.ordinal());
		Properties txnInfo = cfgList.get(ConfigManager.ConfigOrder.TXN_INFO.ordinal());
		Properties uderDef = cfgList.get(ConfigManager.ConfigOrder.USER_DEF.ordinal());
		Properties hostInfo = cfgList.get(ConfigManager.ConfigOrder.HOST_INFO.ordinal());
		Properties easycardApi = cfgList.get(ConfigManager.ConfigOrder.EASYCARD_API.ordinal());
		
		String cmasRquest=null;
		String cmasResponse=null;
		PPR_Reset pprReset = null;
		PPR_SignOn pprSignon = null;
		CmasDataSpec specResetResp = null;
		CmasKernel kernel = null;
		SSL ssl = null;
		CmasFTPList cmasFTP = null;
		String t3900 = "19";
		try{
			
			SubTag5596 t5596 = new CmasDataSpec().new SubTag5596();
			int recvCnt = 0;
			int totalCnt = 0;
			ssl = new SSL(hostInfo.getProperty("HostUrl"), 
					Integer.valueOf(hostInfo.getProperty("HostPort")), 
					null,
					null);
			
			//preConnect
			//ssl.setPriority(Thread.MAX_PRIORITY);
			ssl.start();
			
			/*
			if(ssl.connect()) logger.debug("SSL OK");
			else logger.debug("SSL FAIL");
			*/
			while(t3900.equalsIgnoreCase("19") || (recvCnt < totalCnt))
			{
			
				//PPR_Reset
				pprReset = new PPR_Reset();		
				pprReset.SetOffline(false);
				pprReset.SetReq_TMLocationID(uderDef.getProperty("TM_Location_ID"));		
				pprReset.SetReq_TMID(uderDef.getProperty("TM_ID"));		
				int unixTimeStamp = (int) (System.currentTimeMillis() / 1000L);
				pprReset.SetReq_TMTXNDateTime(unixTimeStamp);
				
				pprReset.SetReq_TMSerialNumber(Integer.valueOf(txnInfo.getProperty("TM_Serial_Number")));
				pprReset.SetReq_TMAgentNumber(uderDef.getProperty("TM_Agent_Number"));
				pprReset.SetReq_TXNDateTime(unixTimeStamp, this.getmTimeZone());
				
				
				byte[] b = Util.ascii2Bcd(easycardApi.getProperty("New_Location_ID"));
				pprReset.SetReq_LocationID(b[0]);
				
				pprReset.SetReq_NewLocationID(Short.valueOf(easycardApi.getProperty("New_Location_ID")));
				
				pprReset.SetReq_SAMSlotControlFlag(true, 1);
				
				logger.debug("ready to call reader");
				result = reader.exeCommand(pprReset);
				if(result != ReaderRespCode._9000)
				{
					logger.error("exe PPR_Reset error:"+String.format("%s", result.getId()));
					return result;
				}
				//update Reader ID
				easycardApi.setProperty("Reader_ID", Util.bcd2Ascii(pprReset.GetResp_ReaderID()));
				
				
				//prepare CMAS Data
				CmasDataSpec specResetReq = new CmasDataSpec();				
				kernel = new CmasKernel();
				kernel.readerField2CmasSpec(pprReset, specResetReq, cfgList, t5596);				
				cmasRquest = kernel.packRequeset(signOn0800,specResetReq);
				
				
				//waiting connection finish first
				ssl.join();	
				if(!ssl.isSocketOK())
				{
					logger.error("ssl connect fail");
					return ApiRespCode.SSL_CONNECT_FAIL;
				} else logger.info("ssl OK");
				
				//if(ssl.isAlive()) logger.debug("SSL alive");
				//else logger.debug("SSL no alive");

				if((cmasResponse = ssl.sendRequest(cmasRquest))!=null){
					//got response
					logger.debug("CMAS Response:"+cmasResponse);
						
					specResetResp = new CmasDataSpec(cmasResponse);
					t3900 = specResetResp.getT3900();
					if(t3900.equalsIgnoreCase("19")) {//txn serialNo. duplicate
						logger.info("TM Serial Number:"+specResetReq.getT1100()+", needed to change:"+specResetResp.getT1100());
						//update SerialNumber
						txnInfo.setProperty("TM_Serial_Number", specResetResp.getT1100());											
					} else if(t3900.equalsIgnoreCase("00")){
						pprSignon= new PPR_SignOn();						
						kernel.cmasSpec2ReaderField(specResetResp, pprSignon, cfgList);						
						totalCnt = Integer.valueOf(specResetResp.getT5596().getT559601());
						recvCnt = Integer.valueOf(specResetResp.getT5596().getT559603());
						t5596.setT559601(specResetResp.getT5596().getT559601());
						t5596.setT559602(specResetResp.getT5596().getT559602());
						t5596.setT559603(String.format("%08d", recvCnt+1));// recvCnt ++
						t5596.setT559604(specResetResp.getT5596().getT559604());
						
						reader.exeCommand(pprSignon);
						
						//SerialNumber
						txnInfo.setProperty("TM_Serial_Number", specResetResp.getT1100());
						//branch_company
						easycardApi.setProperty("Company_Branch", specResetResp.getT4210());
						//new locatoin id
						ArrayList<CmasDataSpec.SubTag5595> t5595s = specResetResp.getT5595s();
						for(int i=0; i<t5595s.size(); i++){
							logger.debug("5595:"+t5595s.get(i).getT559502());
							if(t5595s.get(i).getT559502().equalsIgnoreCase("TM03")){ // 分公司代號								
								easycardApi.setProperty("New_Location_ID", t5595s.get(i).getT559503());
								break;
							}
						}
						
						
					} else {
						logger.error("CMAS Reject Code:"+t3900);
						return ApiRespCode.fromCode(t3900, CmasRespCode.values());					
					}				
				} else{/*maybe TimeOut*/
					logger.error("CMAS maybe be timeout, nothing received");
					return ApiRespCode.HOST_NO_RESPONSE;
				}
			}//while
			
		
			
			if(t3900.equalsIgnoreCase("00")){		
				//FTP download Start another thread
				if(anyFileNeededToDownload(specResetResp.getT5595s())){
					cmasFTP = new CmasFTPList(hostInfo.getProperty("FtpUrl"), 
						hostInfo.getProperty("FtpIP"),
						990,
						hostInfo.getProperty("FtpLoginId"),
						hostInfo.getProperty("FtpLoinPwd"),
						specResetResp.getT5595s(),
						cfgList);
					
					cmasFTP.start();					
				}
				//SignOn Advice			
				CmasDataSpec specSignonAdv = new CmasDataSpec();
				kernel.readerField2CmasSpec(pprSignon, specSignonAdv, specResetResp, cfgList);
				String cmasAdv = kernel.packRequeset(signOn0820, specSignonAdv);
				logger.debug("SignOn Adv:"+cmasAdv);
				cmasResponse = ssl.sendRequest(cmasAdv);
				logger.debug("SignOn Adv Response:"+cmasResponse);
				
			
				if(cmasFTP!=null)cmasFTP.join();
						
			}
		} catch(Exception e) {			
			logger.error("Exception:"+e.getMessage());
			result = ApiRespCode.ERROR;
			e.printStackTrace();
		}
		
		finally{			
			try{
				logger.debug("finally");
				ssl.disconnect();
				cmasFTP.disconnect();
				cfgManager.saveConfig();
			} catch(Exception e) {
				logger.error(e.getMessage());
			}			
		}
		
		logger.info("End");
		return ApiRespCode.SUCCESS;
	}
	private boolean anyFileNeededToDownload(ArrayList<CmasDataSpec.SubTag5595> t5595s){
		boolean result = false;
		String tag=null;
		for(int i=0; i<t5595s.size(); i++){
			tag = t5595s.get(i).getT559502();
			if(//tag.equalsIgnoreCase("TM10") ||
			   tag.equalsIgnoreCase("TM11")	||
			   tag.equalsIgnoreCase("TM12")){				
				logger.info("Host notified something needed to download:"+tag);
				result = true;
				break;				
			}
		}		
		return result;
	}
	
	
	public IRespCode doSignOnQuery(){
		
		IRespCode result = null;
		
		PPR_SignOnQuery pprSignonQuery = new PPR_SignOnQuery();
		
		result = reader.exeCommand(pprSignonQuery);
		if(result != ReaderRespCode._9000)
			logger.error("errID:"+result.getId()+", msg:"+result.getMsg());
		//byte request[] = pprSignonQuery.GetRequest();
		
		return result;
	}
	
	public IRespCode doDeduct(int amt){
		SSL ssl = null;
		IRespCode result = null;
		int tmSerialNo = 0;
		//Properties easyCardApip = cfgList.get(ConfigManager.ConfigOrder.EASYCARD_API.ordinal());
		Properties txnInfo = cfgList.get(ConfigManager.ConfigOrder.TXN_INFO.ordinal());
		Properties userDef = cfgList.get(ConfigManager.ConfigOrder.USER_DEF.ordinal());
		Properties hostInfo = cfgList.get(ConfigManager.ConfigOrder.HOST_INFO.ordinal());
		
		//ssl connect to send advice or online txn
		ssl = new SSL(hostInfo.getProperty("HostUrl"), 
				Integer.valueOf(hostInfo.getProperty("HostPort")), 
				null,
				null);
		ssl.start();
		
		//PPR_TxnReq_OFfline
		PPR_TxnReqOffline pprTxnReqOffline = new PPR_TxnReqOffline();
		pprTxnReqOffline.setReqMsgType((byte)0x01);
		pprTxnReqOffline.setReqTMLocationID(userDef.getProperty("TM_Location_ID"));		
		pprTxnReqOffline.setReqTMID(userDef.getProperty("TM_ID"));		
		int unixTimeStamp = (int) (System.currentTimeMillis() / 1000L);
		pprTxnReqOffline.setReqTMTXNDateTime(unixTimeStamp);
		
		tmSerialNo = Integer.valueOf(txnInfo.getProperty("TM_Serial_Number"));
		pprTxnReqOffline.setReqTMSerialNumber(tmSerialNo);
		pprTxnReqOffline.setReqTMAgentNumber(userDef.getProperty("TM_Agent_Number"));
		pprTxnReqOffline.setReqTXNDateTime(unixTimeStamp, this.getmTimeZone());
		pprTxnReqOffline.setReqTxnAmt(amt);
		
		result = reader.exeCommand(pprTxnReqOffline);
		if(result == ReaderRespCode._6415){
			//需要onLine授權交易
			//todo...
			return result;
		}
		else if(result != ReaderRespCode._9000){
			logger.error("errID:"+result.getId()+", msg:"+result.getMsg());
			return result;
		}
		
		//PPR_TxnReq_OFfline end
		
		
		//PPR_AuthTxn_Offline
		PPR_AuthTxnOffline pprAuthTxnOffline = new PPR_AuthTxnOffline();
		result = reader.exeCommand(pprAuthTxnOffline);	
		if(result != ReaderRespCode._9000){
			logger.error("errID:"+result.getId()+", msg:"+result.getMsg());
			return result;
		}	
		
		//pack advice
		int[] field = {100,200,211,213,214,300,400,408,409,410,1100,1101,1200,1201,1300,1301,1400,3700,4100,4101,4103,4104,4200,4210,4800,4801,4802,4803,4804,4805,4808,4809,4810,4811,4812,4813,4826,5301,5303,5304,5305,5361,5362,5363,5371,5501,5503,5504,5510,6404,6405,6406};
		CmasKernel kernel = new CmasKernel();
		CmasDataSpec specAdvice = new CmasDataSpec();
		kernel.readerField2CmasSpec(pprTxnReqOffline, pprAuthTxnOffline, specAdvice, cfgList);
		String cmasReq = kernel.packRequeset(field, specAdvice);
		
		//send advice
		try {
			ssl.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("ssl join exception:"+e.getMessage());
		}	
		if(!ssl.isSocketOK())
		{
			logger.error("ssl connect fail");
			return ApiRespCode.SSL_CONNECT_FAIL;
		} else logger.info("ssl OK");
		String cmasResp = ssl.sendRequest(cmasReq);
		logger.debug("CMAS deduct advice Response:"+cmasResp);
		
		
		// Txn Success, SN++
		tmSerialNo = tmSerialNo+1;
		txnInfo.setProperty("TM_Serial_Number", String.valueOf(tmSerialNo));
		logger.debug("Txn OK, SN++:"+txnInfo.getProperty("TM_Serial_Number"));
		
		cfgManager.saveConfig();
		//PPR_AuthTxn_Offline end
		
		return result;
	}

}

