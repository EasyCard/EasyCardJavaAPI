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
import Reader.PPR_Reset;
import Reader.ApduRecvSender;
import Reader.PPR_SignOn;
import Reader.PPR_SignOnQuery;
import Reader.PPR_TxnReqOffline;



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
				
				
				pprReset.SetReq_SAMSlotControlFlag(true, 1);
				
				logger.debug("ready to call reader");
				result = reader.exeCommand(pprReset);
				if(result != ReaderRespCode._9000)
				{
					logger.error("exe PPR_Reset error:"+String.format("%s", result.getId()));
					return result;
				}
			
				//prepare CMAS Data
				CmasDataSpec specResetReq = new CmasDataSpec();				
				kernel = new CmasKernel();
				kernel.readerField2CmasSpec(pprReset, specResetReq, cfgList, t5596);
				cmasRquest = kernel.packRequeset(signOn0800,specResetReq);
				
				
				//waitting connecting finish first
				ssl.join();	
				if(!ssl.isSocketOK())
				{
					logger.error("ssl connect fail");
					return ApiRespCode.SSL_CONNECT_FAIL;
				} else logger.info("ssl OK");
				
				if(ssl.isAlive()) logger.debug("SSL alive");
				else logger.debug("SSL no alive");
				

				
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
						logger.debug("After SerialNumber ++:"+txnInfo.getProperty("TM_Serial_Number"));							
					} else {
						logger.error("CMAS Reject Code:"+t3900);
						return ApiRespCode.fromCode(t3900, CmasRespCode.values());					
					}				
				} else{/*maybe TimeOut*/
					logger.error("CMAS maybe be timeout, nothing received");
					return ApiRespCode.HOST_NO_RESPONSE;
				}
			}//while
			
			//logger.debug("send SignOn again");
			//cmasResponse = ssl.sendRequest(cmasRquest);
			//logger.debug("send SignOn again OK");
			
			if(t3900.equalsIgnoreCase("00")){
				
				//SignOn Advice			
				CmasDataSpec specSignonAdv = new CmasDataSpec();
				kernel.readerField2CmasSpec(pprSignon, specSignonAdv, specResetResp, cfgList);
				String cmasAdv = kernel.packRequeset(signOn0820, specSignonAdv);
				logger.debug("SignOn Adv:"+cmasAdv);
				cmasResponse = ssl.sendRequest(cmasAdv);
				logger.debug("SignOn Adv Response:"+cmasResponse);
				
				//FTP download Start
				cmasFTP = new CmasFTPList(hostInfo.getProperty("FtpUrl"), 
						hostInfo.getProperty("FtpIP"),
						990,
						hostInfo.getProperty("FtpLoginId"),
						hostInfo.getProperty("FtpLoinPwd"),
						specResetResp.getT5595s());
				cmasFTP.start();					
				
				
				
			
				cmasFTP.join();
						
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
	
	public IRespCode doSignOnQuery(){
		
		IRespCode result = null;
		
		PPR_SignOnQuery pprSignonQuery = new PPR_SignOnQuery();
		
		result = reader.exeCommand(pprSignonQuery);
		if(result != ReaderRespCode._9000)
			logger.error("errID:"+result.getId()+", msg:"+result.getMsg());
		//byte request[] = pprSignonQuery.GetRequest();
		
		return result;
	}
	
	public IRespCode doDeduct(){
		
		IRespCode result = null;
		//Properties easyCardApip = cfgList.get(ConfigManager.ConfigOrder.EASYCARD_API.ordinal());
		Properties txnInfo = cfgList.get(ConfigManager.ConfigOrder.TXN_INFO.ordinal());
		Properties uderDef = cfgList.get(ConfigManager.ConfigOrder.USER_DEF.ordinal());
		Properties hostInfo = cfgList.get(ConfigManager.ConfigOrder.HOST_INFO.ordinal());
		PPR_TxnReqOffline pprTxnReqOffline = new PPR_TxnReqOffline();
		
		
		pprTxnReqOffline.setReqMsgType((byte)0x01);
		
		
		pprTxnReqOffline.setReqTMLocationID(uderDef.getProperty("TM_Location_ID"));		
		pprTxnReqOffline.setReqTMID(uderDef.getProperty("TM_ID"));		
		int unixTimeStamp = (int) (System.currentTimeMillis() / 1000L);
		pprTxnReqOffline.setReqTMTXNDateTime(unixTimeStamp);
		
		pprTxnReqOffline.setReqTMSerialNumber(Integer.valueOf(txnInfo.getProperty("TM_Serial_Number")));
		pprTxnReqOffline.setReqTMAgentNumber(uderDef.getProperty("TM_Agent_Number"));
		pprTxnReqOffline.setReqTXNDateTime(unixTimeStamp, this.getmTimeZone());
		pprTxnReqOffline.setReqTxnAmt(50);
		
		//byte request[] = pprTxnReqOffline.GetRequest();
		
		
		result = reader.exeCommand(pprTxnReqOffline);
		if(result != ReaderRespCode._9000)
			logger.error("errID:"+result.getId()+", msg:"+result.getMsg());
		//byte request[] = pprSignonQuery.GetRequest();
		
		return result;
	}

}

