package com.easycard.pc.CMAS;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;











import java.util.List;

import org.apache.log4j.Logger;


import org.apache.log4j.PropertyConfigurator;

import com.easycard.pc.CMAS.CmasDataSpec.MsgType;
import com.easycard.pc.CMAS.CmasTag.SubTag5596;
import com.easycard.pc.communication.socket.*;
import com.easycard.pc.database.BatchDetail;
import com.easycard.pc.database.CmasDB;
import com.easycard.pc.database.CmasDB.CmasDBException;
import com.easycard.pc.database.TxnBatch;
import com.easycard.errormessage.ApiRespCode;
import com.easycard.errormessage.IRespCode;
import com.easycard.reader.ApduRecvSender;
import com.easycard.reader.BigBlackList;
import com.easycard.reader.EZReader;
import com.easycard.reader.PPR_AuthTxnOffline;
import com.easycard.reader.PPR_AuthTxnOnline;
import com.easycard.reader.PPR_LockCard;
import com.easycard.reader.PPR_ReadCardBasicData;
import com.easycard.reader.PPR_Reset;
import com.easycard.reader.PPR_SignOn;
import com.easycard.reader.PPR_SignOnQuery;
import com.easycard.reader.PPR_TxnReqOffline;
import com.easycard.reader.PPR_TxnReqOnline;
import com.easycard.reader.ReaderRespCode;
import com.easycard.utilities.*;



public class Process {

	 static Logger logger = Logger.getLogger(Process.class);
 
	 private EZReader reader;// = new EZReader(recvSender);
	 private String mTimeZone = "Asia/Taipei"; 
	 
	 //private ArrayList<Properties> cfgList = null;
	 private CmasDB configManager = null;
	 private static final String CONFIG_INIT_FAIL = "CMAS Config Initial Error";
	 
	
	@SuppressWarnings("serial")
	public class ProcessException extends Exception{

		public ProcessException(String msg){
			 super(msg);
		 }		 
	 }
	 
	//RS232 controller was EasyCard
	 public Process() throws ProcessException
	 {				 
		 if(initConfig() == false) throw new ProcessException(CONFIG_INIT_FAIL);
		 
		 //Properties userDef = cfgList.get(ConfigManager.ConfigOrder.USER_DEF.ordinal());
		 logger.debug("process init ok");
		 ApduRecvSender rs = new ApduRecvSender();
		 logger.debug("process init ok");
		 logger.debug("getComPort:"+configManager.getReaderPort());
		 rs.setPortName(configManager.getReaderPort());
		 reader = new EZReader(rs);
		 logger.info("End");
	 }
	 
	
		
	//RS232 controller was POS
	 public Process(ApduRecvSender rs) throws ProcessException
	 {			 
		 if(initConfig() == false) throw new ProcessException(CONFIG_INIT_FAIL);		
		 reader = new EZReader(rs);
	 }
	
	 private boolean initConfig()	
	 {
		
		//init log4j configFile
		//PropertyConfigurator.configure(Process.class.getResourceAsStream("log4j.properties"));//for rootDir
		boolean result = true;
		PropertyConfigurator.configure(Process.class.getClassLoader().getResourceAsStream(ConfigManager.LOG4J_CONFIG_FILE));
		logger.info("=============== API Start ===============");
		
		/*
		//init api needed configFile
		configManager = new ConfigManager();
		if(!configManager.initial()){
			logger.error("config initial fail");
			result = false;
		}		
		*/
		
		//initial DB test
		try {
			
			configManager = new CmasDB();
			
			if(!configManager.initial()){
				logger.error("config initial fail");
				result = false;
			}
			logger.debug("Comport:"+configManager.getReaderPort());	
		} catch (CmasDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.debug("Cmas DB Constructer fail");
			e.printStackTrace();
			result = false;		
		}
		logger.info("End");
		return result;
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


	private SendAdvice bgUploadAdvice(){
		SSL ssl = null;
		Connection con = null;		
		con = configManager.getConnection();
		String key = configManager.getTxnBatch().getNewDeviceIDMixBatchNo();		
		List<BatchDetail.DBFields> data = configManager.getBatchDetail().selectUnUploadAdvice(con, key);
		if(data.size() == 0) {
			logger.debug("no advice needed to send, so, return null");
			return null;
		}
		
		//SSL preConnect
		ssl = this.getSSLInstance();
		
		//create another thread to process advice
		SendAdvice advice = new SendAdvice(data, ssl, con);
		advice.start();
		
		return advice;
	}
	
	private void updateTMS5595(ArrayList<CmasTag.SubTag5595> t5595s){
		try{
			String tag = null;
			String value = null;
			int nDate = Integer.valueOf(Util.sGetDate());//now date
			int gDate = 0;//CMAS gived date
			
			for(int i=0; i<t5595s.size(); i++){
				try{
					tag = t5595s.get(i).getT559502();
					value = t5595s.get(i).getT559503();
					gDate = Integer.valueOf(t5595s.get(i).getT559504());
					logger.debug("nowDate:"+nDate+",tag:"+tag+"startDate:"+gDate);
					if(gDate > nDate) continue;
					//logger.debug("5595:"+t5595s.get(i).getT559502());
					if(tag.equalsIgnoreCase("TM03")){ // 分公司代號								
						configManager.setNewLocationID(value);					
					} else if(tag.equalsIgnoreCase("TM05")){						
						configManager.setFtpIP(value);					
					} else if(tag.equalsIgnoreCase("TM06")){						
						configManager.setFtpPort(Integer.valueOf(value));					
					} else if(tag.equalsIgnoreCase("TM07")){						
						configManager.setFtpLoginID(value);					
					} else if(tag.equalsIgnoreCase("TM08")){							
						configManager.setFtpLoginPwd(value);					
					} else if(tag.equalsIgnoreCase("TM26")){
						configManager.setAdviceLimit(Integer.valueOf(value));
					} else if(tag.equalsIgnoreCase("TM27")){
						configManager.setAdviceLimitLock(Integer.valueOf(value));
					} else if(tag.equalsIgnoreCase("TM30")){
						configManager.setTxnSwitch(value);
					} else if(tag.equalsIgnoreCase("TM31")){
						configManager.setCashAddUnit(Integer.valueOf(value));;
					} else if(tag.equalsIgnoreCase("TM77")){
						configManager.setAutoloadAmtLimit(Integer.valueOf(value));;
					} else{}
				}catch(Exception e){
					e.printStackTrace();
					logger.error("Exception:"+e.getMessage());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Exception:"+e.getMessage());
		}
			
	}
	
	private void updateTMS5588(ArrayList<CmasTag.SubTag5588> t5588s){
		for(int i=0; i<t5588s.size(); i++){
			//logger.debug("5595:"+t5595s.get(i).getT559502());
			if(t5588s.get(i).getT558801().equalsIgnoreCase("04")){ // 參數版號
				logger.debug("update ApiParameter Ver to:"+t5588s.get(i).getT558803());
				configManager.setApiParaVer(t5588s.get(i).getT558803());												
			} else if(t5588s.get(i).getT558801().equalsIgnoreCase("02")) {
				configManager.setBlackListType(t5588s.get(i).getT558802());
			}
		}
	}
	
	public IRespCode doSignon()
	{
		logger.info("Start");
		
		IRespCode result;		
		String cmasRquest=null;
		String cmasResponse=null;
		PPR_Reset pprReset = null;
		PPR_SignOn pprSignon = null;
		CmasDataSpec specResetResp = null;
		CmasKernel kernel = null;
		SSL ssl = null;		
		CmasFTPList cmasFTP = null;
		SendAdvice advices = null;
		
		String t3900 = "19";
		try{
			
			
			int recvCnt = 0;
			int totalCnt = 0;
			
			//ssl preConnect
			ssl = this.getSSLInstance();
			
			//process advice
			advices = this.bgUploadAdvice();
			
			
			while(t3900.equalsIgnoreCase("19") || (recvCnt < totalCnt))
			{
			
				//PPR_Reset
				logger.debug("===== exe PPR_Reset =====");
				pprReset = new PPR_Reset();		
				pprReset.SetOffline(false);
				pprReset.SetReq_TMLocationID(configManager.getTMLocationID());		
				pprReset.SetReq_TMID(configManager.getTMID());		
				int unixTimeStamp = (int) (System.currentTimeMillis() / 1000L);
				pprReset.SetReq_TMTXNDateTime(unixTimeStamp);				
				pprReset.SetReq_TMSerialNumber(Integer.valueOf(configManager.getTMSerialNo()));
				pprReset.SetReq_TMAgentNumber(configManager.getTMAgentNo());
				pprReset.SetReq_TXNDateTime(unixTimeStamp, this.getmTimeZone());							
				byte[] b = Util.ascii2Bcd(configManager.getNewLocationID());
				pprReset.SetReq_LocationID(b[0]);				
				pprReset.SetReq_NewLocationID(Short.valueOf(configManager.getNewLocationID()));				
				pprReset.SetReq_SAMSlotControlFlag(true, 1);
								
				result = reader.exeCommand(pprReset);
				if(result != ReaderRespCode._9000)
				{
					logger.error("exe PPR_Reset error:"+String.format("%s", result.getId()));
					return result;
				}
				
				
				//update Reader ID
				configManager.setReaderID(Util.bcd2Ascii(pprReset.GetResp_ReaderID()));
				//update NewDeviceID
				String readerNewDeviceID = Util.newDeviceID2Decimal(pprReset.GetResp_NewDeviceID());				
				if(configManager.getNewDeviceID()==null){
					configManager.setNewDeviceID(readerNewDeviceID);
					configManager.selectTxnBatchRec();
				}
				else if(configManager.getNewDeviceID().equalsIgnoreCase(readerNewDeviceID) == false)
					return ApiRespCode.READER_CHANGED_PLZ_SETTLE_FIRST;
				
				
				logger.info("===== exe CMAS 0800 =====");
				//prepare CMAS Data
				CmasDataSpec specResetReq = new CmasDataSpec();	
				//SubTag5596 t5596 = specResetReq.getCmasTag().new SubTag5596();
				SubTag5596 t5596 = specResetReq.getSubTag5596Instance();
				kernel = new CmasKernel();
				kernel.readerField2CmasSpec(pprReset, specResetReq, configManager, t5596);				
				cmasRquest = kernel.packRequeset(CmasDataSpec.CmasReqField._0800.getField(),specResetReq);
				
				
				
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
						configManager.setTMSerialNo(specResetResp.getT1100());
						
															
					} else if(t3900.equalsIgnoreCase("00")){
						//update SerialNo++
						configManager.setTMSerialNo(String.format("%d", Integer.valueOf(specResetResp.getT1100())+1));
						
						pprSignon= new PPR_SignOn();						
						kernel.cmasSpec2ReaderField(specResetResp, pprSignon, configManager);						
						totalCnt = Integer.valueOf(specResetResp.getT5596().getT559601());
						recvCnt = Integer.valueOf(specResetResp.getT5596().getT559603());
						t5596.setT559601(specResetResp.getT5596().getT559601());
						t5596.setT559602(specResetResp.getT5596().getT559602());
						t5596.setT559603(String.format("%08d", recvCnt+1));// recvCnt ++
						t5596.setT559604(specResetResp.getT5596().getT559604());
						
						result = reader.exeCommand(pprSignon);
						if(result != ReaderRespCode._9000){
							logger.error("pprSignOn fail");
							//saved TMSerialNo. rightNow
							configManager.getDeviceInfo().updateRec(configManager.getConnection());
							return result;
						}
						
						
						//branch_company		
						//easycardApi.setProperty("Company_Branch", specResetResp.getT4210());
						//new locatoin id
						ArrayList<CmasTag.SubTag5595> t5595s = specResetResp.getT5595s();
						this.updateTMS5595(t5595s);
						
						//5588
						ArrayList<CmasTag.SubTag5588> t5588s = specResetResp.getT5588s();
						this.updateTMS5588(t5588s);
						
						
					} else {
						logger.error("CMAS Reject Code:"+t3900);
						return ApiRespCode.fromCode(t3900, CmasRespCode.values());					
					}
					
					//saved TMSerialNo. rightNow
					configManager.getDeviceInfo().updateRec(configManager.getConnection());
				} else{/*maybe TimeOut*/
					logger.error("CMAS maybe be timeout, nothing received");
					return ApiRespCode.HOST_NO_RESPONSE;
				}
			}//while
			
		
			
			
			if(t3900.equalsIgnoreCase("00")){							
				
				//FTPS download file, Start another thread
				if(anyFileNeededToDownload(specResetResp.getT5595s())){
					cmasFTP = new CmasFTPList(specResetResp.getT5595s(),
						configManager);
					
					cmasFTP.start();					
				}
				//SignOn Advice
				CmasDataSpec specSignonAdv = new CmasDataSpec();
				kernel.readerField2CmasSpec(pprSignon, specSignonAdv, specResetResp, configManager);
				String cmasAdv = kernel.packRequeset(CmasDataSpec.CmasReqField._0820.getField(), specSignonAdv);
				logger.debug("save SignOn Adv:"+cmasAdv);
				this.addRecord2BatchDetail(pprReset.GetResp_NewDeviceID(),						
						configManager.getBatchNo(), 
						specSignonAdv.getT0100(),
						specSignonAdv.getT0300(), 
						specSignonAdv.getT3700(), 
						cmasAdv, 0);
				
				//send advice immediately
				if(configManager.getAdviceLimit() == 1){
					SendAdvice.immediatelySend(ssl, configManager.getBatchDetail(), configManager.getConnection(), cmasAdv);					
				}
				//logger.debug("SignOn Adv Response:"+cmasResponse);
				
				//main process waited that FTPS thread finished
				if(cmasFTP!=null) cmasFTP.join();
						
			}
		} catch(Exception e) {			
			logger.error("Exception:"+e.getMessage());
			result = ApiRespCode.ERROR;
			e.printStackTrace();
		}
		
		finally{			
			try{
				logger.debug("finally");
				if(advices != null) advices.stopSendAdvice();
				ssl.disconnect();
				if(cmasFTP != null) cmasFTP.disconnect();
				reader.finish();
				//config finish @ last step
				configManager.finish();
				
				
			} catch(Exception e) {
				logger.error("doSignon finally exception:"+e.getMessage());
			}			
		}
		
		logger.info("End");
		return ApiRespCode.SUCCESS;
	}
	
	private void addRecord2BatchDetail(byte[] newDeviceID,
			String batchNo,
			String msgType,
			String pCode,					
			String rrn,
			String adviceReq,			
			int txnAmt){
		BatchDetail bd = configManager.getBatchDetail();
		String newDeviceIDMixBatchNo = Util.newDeviceID2Decimal(newDeviceID)+batchNo;
		
		bd.setNewDeviceIDMixBatchNo(newDeviceIDMixBatchNo);
		bd.setMsgType(msgType);
		bd.setPCode(pCode);
		bd.setRRN(rrn);
		bd.setAdviceReq(adviceReq);
		bd.setTxnAmt(txnAmt);
		
		bd.insertRec(configManager.getConnection());
	}
	
	private void deleteRecord4BatchDetail(){
		BatchDetail bd = configManager.getBatchDetail();			
		bd.deleteRec(configManager.getConnection());
	}
	
	private boolean anyFileNeededToDownload(ArrayList<CmasTag.SubTag5595> t5595s){
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
	
	
	

	
	//check cardID, if cardID existed in blacklist, reader locked it
	private boolean checkCardIDInBlackList(byte[] cardID){
		boolean result = false;
		
		BigBlackList bigBlc = new BigBlackList();
		if(!bigBlc.configure(Process.class.getClassLoader().getResourceAsStream(IConfigManager.CARD_NUMBER_BLACKLIST))){
			logger.error("configure "+IConfigManager.CARD_NUMBER_BLACKLIST+" fail");
			if(!bigBlc.configure(Process.class.getClassLoader().getResourceAsStream(IConfigManager.CARD_NUMBER_BLACKLIST_PREVER))){
				logger.error("configure "+IConfigManager.CARD_NUMBER_BLACKLIST_PREVER+" fail");
			}
		} else result = bigBlc.checkCardID(cardID); 
			
		return result;
	}
	
	private SSL getSSLInstance(){
		SSL ssl = null;
		try{
			ssl = new SSL(configManager.getHostUrl(), 
					Integer.valueOf(configManager.getHostPort()), 
					null,
					null);
			ssl.start();
		} catch(Exception e) {
			logger.error("Exception:"+e.getMessage());
			e.printStackTrace();
		}
		return ssl;
	}
	
	//直接執行鎖卡的指令，並且回應結果
	private IRespCode exeLockCard(byte[] cardID, int unixTimeStamp){
		
		IRespCode result=null;
		PPR_LockCard pprLockCard = new PPR_LockCard();
		pprLockCard.setReqCardPhysicalID(cardID);
		pprLockCard.setReqTxNDateTime(unixTimeStamp, this.getmTimeZone());
		pprLockCard.setReqBlockingReason((byte)0x01);
		result = reader.exeCommand(pprLockCard);
		
		return result;
	}
	
	//直接執行 PPR_TxnOffline, 並且回應結果
	private IRespCode exePPRTxnReqOffline(byte msgType, byte subType, int amt, int unixTimeStamp, PPR_TxnReqOffline pprTxnReqOffline){
		IRespCode result = null;
		int tmSerialNo = 0;
		
		try {
			//PPR_TxnReq_OFfline		
			pprTxnReqOffline.setReqMsgType(msgType);
			pprTxnReqOffline.setReqSubType(subType);
			pprTxnReqOffline.setReqTMLocationID(configManager.getTMLocationID());		
			pprTxnReqOffline.setReqTMID(configManager.getTMID());		
			//int unixTimeStamp = (int) (System.currentTimeMillis() / 1000L);
			pprTxnReqOffline.setReqTMTXNDateTime(unixTimeStamp);
			
			tmSerialNo = Integer.valueOf(configManager.getTMSerialNo());
			pprTxnReqOffline.setReqTMSerialNumber(tmSerialNo);
			pprTxnReqOffline.setReqTMAgentNumber(configManager.getTMAgentNo());
			pprTxnReqOffline.setReqTXNDateTime(unixTimeStamp, this.getmTimeZone());
			pprTxnReqOffline.setReqTxnAmt(amt);
			
			result = reader.exeCommand(pprTxnReqOffline);			
		} catch(Exception e) {
			logger.error("Exception:"+e.getMessage());
			e.printStackTrace();
			result = ApiRespCode.ERROR;
		}
		return result;
	}
	
	//直接執行 PPR_TxnOnline, 並且回應結果
	private IRespCode exePPRTxnReqOnline(byte msgType, byte subType, int amt, int unixTimeStamp, PPR_TxnReqOnline pprTxnReqOnline){
		
		IRespCode result = ReaderRespCode._9000; 
		//PPR_TxnReqOnline pprTxnReqOnline = null;
		try {
					
			//pprTxnReqOnline = new PPR_TxnReqOnline();		
			//CmasDataSpec onlineSpec = new CmasDataSpec();
			
			//onlineSpec.setT0300(CmasDataSpec.PCode.CPU_AUTOLOAD.toString());
			pprTxnReqOnline.setReqMsgType(msgType);
			pprTxnReqOnline.setReqSubType(subType);
			pprTxnReqOnline.setReqTMLocationID(configManager.getTMLocationID());
			pprTxnReqOnline.setReqTMID(configManager.getTMID());
			pprTxnReqOnline.setReqTMTXNDateTime(unixTimeStamp);
			pprTxnReqOnline.setReqTMSerialNumber(Integer.valueOf(configManager.getTMSerialNo()));
			pprTxnReqOnline.setReqTMAgentNumber(configManager.getTMAgentNo());
			pprTxnReqOnline.setReqTXNDateTime(unixTimeStamp, getmTimeZone());
			pprTxnReqOnline.setReqTxnAmt(amt);
			
			result = reader.exeCommand(pprTxnReqOnline);
			
		} catch(Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			result = ApiRespCode.ERROR;
		}	
		return result;
	}
	
	private void checkSendAdviceNow(SSL ssl, String advice){
		
		if(configManager.getAdviceLimit()>1) return;
		
		String cmasResponse = null;
		if((cmasResponse = ssl.sendRequest(advice)) != null){
			CmasDataSpec spec = new CmasDataSpec(cmasResponse);
			if(spec.getT3900().equalsIgnoreCase("00") == false){
				logger.error("WTF... why advice response t3900("+spec.getT3900()+") was not 00");
			}
			BatchDetail bd = configManager.getBatchDetail();
			bd.setAdviceResp(cmasResponse);
			bd.updateRec(configManager.getConnection());
		} else logger.error("send advice no response!!!");
	}
	
	private IRespCode exeAutoloadReaderAndCmasFormat(int amt, SSL ssl){
		IRespCode result = ApiRespCode.ERROR ;//= ApiRespCode.SUCCESS;
		int tmSerialNo=Integer.valueOf(configManager.getTMSerialNo());
		CmasKernel kernel = null;
		CmasDataSpec reqSpec = null;
		CmasDataSpec respSpec = null;
		PPR_AuthTxnOnline pprAuthTxnOnline = null;
		PPR_TxnReqOnline pprTxnReqOnline = null;
		int testCnt = 1;
		int unixTimeStamp = (int) (System.currentTimeMillis() / 1000L);	
		String reversalReq=null;
		try{
			while(true){//loop for reTry
				// exe PPR_TxnReq_Online
				pprTxnReqOnline = new PPR_TxnReqOnline(); 					
				result = exePPRTxnReqOnline((byte)0x02, (byte)0x40, amt, unixTimeStamp, pprTxnReqOnline);
				if(result == ReaderRespCode._6415){
				
					//autoLoad enable?
					if(pprTxnReqOnline.getAutoloadFlag()==false)
						return ApiRespCode.CARD_NO_SUPPORT_AUTOLOAD;
					
					//snedRecv CMAS 0100 format
					kernel = new CmasKernel();
					reqSpec = new CmasDataSpec();			
					reqSpec.setT0300(CmasDataSpec.PCode.CPU_AUTOLOAD.toString());			
					kernel.readerField2CmasSpec(pprTxnReqOnline, reqSpec, configManager);
					String cmasReq = kernel.packRequeset(CmasDataSpec.CmasReqField._0100_AUTH.getField(), reqSpec);
					
					//reversal
					reqSpec.setT0100(MsgType.REVERSAL_REQ.toString());
					reversalReq = kernel.packRequeset(CmasDataSpec.CmasReqField._0100_AUTH.getField(), reqSpec);			
					// reversal todo...
					
					// sendRecv from CMAS Host
					try {
						ssl.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						logger.debug("InterruptedException:"+e.getMessage());
						e.printStackTrace();
						return ApiRespCode.SSL_CONNECT_FAIL;
					}
					//save Reversal first
					this.addRecord2BatchDetail(pprTxnReqOnline.getRespNewDeviceID(), 
							reqSpec.getT0100(),
							configManager.getBatchNo(), 
							reqSpec.getT0300(), 
							reqSpec.getT3700(), 
							reversalReq, amt);
					
					String cmasResp = ssl.sendRequest(cmasReq);
					if(cmasResp==null) {
						configManager.setTMSerialNo(String.valueOf(++tmSerialNo));
						return ApiRespCode.HOST_NO_RESPONSE;
					} 
					logger.debug("autoLoad CMAS Resp:"+cmasResp);
					
					//testReversal
					//String reversalResp = ssl.sendRequest(reversalReq);
					//if(reversalResp==null) {
					//	configManager.setTMSerialNo(String.valueOf(++tmSerialNo));
					//	return ApiRespCode.HOST_NO_RESPONSE;
					//}
					//logger.debug("autoLoad CMAS Reversal Resp:"+reversalResp);
					
					
					respSpec = new CmasDataSpec(cmasResp);	
					if(respSpec.getT3900().equalsIgnoreCase(CmasRespCode._00.getId()) == false)
					{						
						//delete Reversal request
						this.deleteRecord4BatchDetail();
						
						logger.error("T3900:"+respSpec.getT3900());
						configManager.setTMSerialNo(String.valueOf(++tmSerialNo));
						return ApiRespCode.fromCode(respSpec.getT3900(), CmasRespCode.values());
					}
				
					//AuthOnline
					pprAuthTxnOnline = new PPR_AuthTxnOnline();
					kernel.cmasSpec2ReaderField(respSpec, pprAuthTxnOnline, configManager);
					
				} else if(result != ReaderRespCode._9000){
					return result;
				}	
				
				result = reader.exeCommand(pprAuthTxnOnline);
				/*test reTry
				if(testCnt %2 != 0) {
					result = ReaderRespCode._6088;
					testCnt++;
				}*/
				
				
				if(EZReader.reTryEnabled(result)){
					logger.warn("go2ReTry");
					continue;
				} else if(result == ReaderRespCode._9000) break;
				else {
					//AuthOnline fail, send Reversal first
					String reversalResp = ssl.sendRequest(reversalReq);
					if( reversalResp !=null) this.deleteRecord4BatchDetail();
				
					//update serialNo
					configManager.setTMSerialNo(String.valueOf(++tmSerialNo));
					return result;
				}
			
			}
			
			//autoload OK, delete Reversal request
			this.deleteRecord4BatchDetail();
			
			//pack AuthOnline Advice
			respSpec = new CmasDataSpec();
			kernel.readerField2CmasSpec(pprTxnReqOnline, pprAuthTxnOnline, respSpec, configManager);
			String autoloadAdvice = kernel.packRequeset(CmasDataSpec.CmasReqField._0220.getField(), respSpec);
			logger.debug("save autoload advice");
			this.addRecord2BatchDetail(pprTxnReqOnline.getRespNewDeviceID(), 
					configManager.getBatchNo(), 
					respSpec.getT0100(), 
					respSpec.getT0300(), 
					respSpec.getT3700(), 
					autoloadAdvice, amt);
			
			//check advice send immediately or not
			this.checkSendAdviceNow(ssl, autoloadAdvice);
			//String cmasResp = ssl.sendRequest(advice);
			//logger.debug("autoLoad Advice CMAS Resp:"+cmasResp);
			
			//update tmSerialNo
			configManager.setTMSerialNo(String.valueOf(++tmSerialNo));
			
			result = ApiRespCode.SUCCESS;	
			
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("Exception:"+e.getMessage());
			result = ApiRespCode.ERROR;
		}
			
		return result;
	}
	public IRespCode doAutoload(int amt){
		IRespCode result = null;		
		SSL ssl = null;	
			
			
		try{
			//preConnect
			ssl = getSSLInstance();
			
			if((result = exeAutoloadReaderAndCmasFormat(amt, ssl)) != ApiRespCode.SUCCESS)
				return result;
		
		} catch(Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally {
			ssl.disconnect();
			configManager.finish();
			reader.finish();
		}
		return result;
	}
	
	public IRespCode doDeduct(int amt, boolean exeAutoload){
		SSL ssl = null;
		IRespCode result = null;
		PPR_AuthTxnOffline pprAuthTxnOffline = null;
		PPR_TxnReqOffline pprTxnReqOffline = null;
		SendAdvice advices = null;
		
		long autoloadAmtLimit = configManager.getAutoloadAmtLimit();		
		int tmSerialNo = Integer.valueOf(configManager.getTMSerialNo());
		int unixTimeStamp = (int) (System.currentTimeMillis() / 1000L);
		
		try {
		
			logger.info("=========SSL PreConnect==========");
			//SSL PreConnect
			ssl = getSSLInstance();
			
			//process advice
			advices = this.bgUploadAdvice();
			
			
			logger.info("=========PPR_TxnReq_Offline==========");
			//PPR_TxnReq_Offline
			pprTxnReqOffline = new PPR_TxnReqOffline();		
			result = exePPRTxnReqOffline((byte)0x01, (byte)0x00, amt, unixTimeStamp, pprTxnReqOffline);
				
			//check newDeviceID in config file
			logger.debug("check newDeviceID:"+configManager.getNewDeviceID());
			//if(configManager.getNewDeviceID()==)
			
			//640E,610F...error Code
			if(pprTxnReqOffline.getErrorRespFld() != null){
				// pack LockCard Advice
					
				CmasDataSpec cmaslockAdvice = new CmasDataSpec();
				CmasKernel kernel = new CmasKernel();
				if(kernel.readerErrorCode2CmasLockAdvice(pprTxnReqOffline.getErrorRespFld(), cmaslockAdvice, configManager, result)==false)
					return result;
				String lockAdvice = kernel.packRequeset(CmasKernel.CMAS_LOCKCARD_ADVICE_FLD, cmaslockAdvice);
				logger.debug("Save Lock Advice:"+lockAdvice);
				this.addRecord2BatchDetail(pprTxnReqOffline.getErrorRespFld().getNewDeviceID(),
						cmaslockAdvice.getT0100(),
						configManager.getBatchNo(), 
						cmaslockAdvice.getT0300(), 
						cmaslockAdvice.getT3700(), 
						lockAdvice, 0);
				
				try {
					if(configManager.getAdviceLimit() == 1){
						ssl.join();
						String resp = ssl.sendRequest(lockAdvice);
						logger.debug("Save Lock Advice Resp:"+resp);			
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("InterruptedException:"+e.getMessage());
					//result = ApiRespCode.SSL_CONNECT_FAIL;
				}
				configManager.setTMSerialNo(String.valueOf(++tmSerialNo));
				return result;
			}
			//not 9000, 6403, 6415, return	
			if(result != ReaderRespCode._9000 && result != ReaderRespCode._6415 && result != ReaderRespCode._6403) return result;
				
			logger.info("=========Check BlackList==========");
			//check blackList ID
			if(checkCardIDInBlackList(pprTxnReqOffline.getRespCardPhysicalID())){
					
				try {
					//int unixTimeStamp = (int) (System.currentTimeMillis() / 1000L);
					result = ApiRespCode.CARDID_IN_BLACKLIST;
					this.exeLockCard(pprTxnReqOffline.getRespCardPhysicalID(), unixTimeStamp);
					CmasKernel kernel = new CmasKernel();
					CmasDataSpec cmaslockAdvice = new CmasDataSpec();
					kernel.blackList2CmasLockAdvice(pprTxnReqOffline, cmaslockAdvice, configManager);
					String blckListAdvice = kernel.packRequeset(CmasKernel.CMAS_LOCKCARD_ADVICE_FLD, cmaslockAdvice);
					logger.debug("save LockCard Advice:"+blckListAdvice);					
					this.addRecord2BatchDetail(pprTxnReqOffline.getRespNewDeviceID(), 
							cmaslockAdvice.getT0100(),
							configManager.getBatchNo(), 
							cmaslockAdvice.getT0300(), 
							cmaslockAdvice.getT3700(), 
							blckListAdvice, 0);
					
					
					ssl.join();
					//check send blackAdvice immediately or not
					this.checkSendAdviceNow(ssl, blckListAdvice);
				} catch(InterruptedException e){ 
					logger.error(e.getMessage());
					e.printStackTrace();
					
				} catch(Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
				configManager.setTMSerialNo(String.valueOf(++tmSerialNo));
				return result;
			}
				
	
			//autoLoad
			if (result == ReaderRespCode._6403) {//6403(餘額不足)，check autoload flag
					
				if(pprTxnReqOffline.getAutoloadFlag() == false) 
					return result;//餘額不足, return 6403
					
				//purseBalance+autoloadAmt
				long singleAutoloadAmt = Util.bytes2Long(pprTxnReqOffline.getRespSingleAutoLoadTxnAmt(), 0, pprTxnReqOffline.getRespSingleAutoLoadTxnAmt().length, true);
				long totalAutoloadAmt = (autoloadAmtLimit/singleAutoloadAmt)*singleAutoloadAmt;
				
				long ev = Util.bytes2Long(pprTxnReqOffline.getRespPurseBalanceBeforeTxn(), 0, pprTxnReqOffline.getRespPurseBalanceBeforeTxn().length, true);
					
				if(amt > (ev+totalAutoloadAmt)) {
					logger.warn("after autoload, amt not enough");
					return result;//return 6403
				}
				if(exeAutoload == false){
					logger.info("purseBalance no enough, autload Allowed, exe autoload?");
					return ApiRespCode.AUTOLOAD_YES_OR_NO;
				} else {
					logger.info("=========exe autoLoad==========");
					//exe autoLoad
					IRespCode r;
					if((r = exeAutoloadReaderAndCmasFormat((int)totalAutoloadAmt, ssl)) != ApiRespCode.SUCCESS)
						return r;
					unixTimeStamp = (int) (System.currentTimeMillis() / 1000L);
					result = ReaderRespCode._6088;//important!!!, 只是為了流程順一點, 進入底下retry重跑一次deduct流程
				}
					
			}// autoLoad end
				
			tmSerialNo = Integer.valueOf(configManager.getTMSerialNo());//重新抓取serialNo，autoload可能已經+1了
			pprAuthTxnOffline = new PPR_AuthTxnOffline();
			while(true){
					
					if(EZReader.reTryEnabled(result)==true)
						result = exePPRTxnReqOffline((byte)0x01, (byte)0x00, amt, unixTimeStamp, pprTxnReqOffline);
					
					if(result == ReaderRespCode._6415){
						
						logger.info("=========TxnReqoffline:6415==========");
						CmasKernel kernel = new CmasKernel();
						CmasDataSpec spec = new CmasDataSpec();
						spec.setT0300(CmasDataSpec.PCode.CPU_TXN_VERIFICATION.toString());
						kernel.readerField2CmasSpec(pprTxnReqOffline, spec, configManager);
						String cmasReq = kernel.packRequeset(CmasDataSpec.CmasReqField._0100_VERI.getField(), spec);
						logger.debug("_6415 CMAS goOnline Request:"+cmasReq);
												
						try{
							ssl.join();
						} catch(InterruptedException e){
							e.printStackTrace();
							logger.error("InterruptedException:"+e.getMessage());
							return ApiRespCode.SSL_CONNECT_FAIL;
						}
						
						//prePare & save Reversal
						spec.setT0100(MsgType.REVERSAL_REQ.toString());
						String reversalReq = kernel.packRequeset(CmasDataSpec.CmasReqField._0100_VERI.getField(), spec);
						this.addRecord2BatchDetail(pprTxnReqOffline.getRespNewDeviceID(), 
								configManager.getBatchNo(), 
								spec.getT0100(), 
								spec.getT0300(), 
								spec.getT3700(), 
								reversalReq, amt);
						//send onLine Auth Request
						String cmasResp = ssl.sendRequest(cmasReq);
						logger.debug("_6415 CMAS goOnline Response:"+cmasResp);
						if(cmasResp == null) {
							configManager.setTMSerialNo(String.valueOf(++tmSerialNo));
							String reversalResp = ssl.sendRequest(reversalReq);
							if(reversalResp!=null) this.deleteRecord4BatchDetail();
							return ApiRespCode.HOST_NO_RESPONSE;
						}
						//delete Reversal Req
						this.deleteRecord4BatchDetail();
						
						
						//check online request CMAS tag3900
						CmasDataSpec specResp = new CmasDataSpec(cmasResp);
						if(specResp.getT3900().equalsIgnoreCase(CmasRespCode._00.getId()) == false){ 
							configManager.setTMSerialNo(String.valueOf(++tmSerialNo));
							logger.error("goOnline Result T3900 was:"+specResp.getT3900());
							return ApiRespCode.fromCode(specResp.getT3900(), CmasRespCode.values());
						}
						
						//prePare PPR_AuthTxnOffline()
						kernel.cmasSpec2ReaderField(specResp, pprAuthTxnOffline, configManager);
					}
					
					result = reader.exeCommand(pprAuthTxnOffline);
					if(EZReader.reTryEnabled(result)==true){
						logger.warn("reTry happend! go2ReTry");
						continue;
					}
					
					if(result != ReaderRespCode._9000) {
						configManager.setTMSerialNo(String.valueOf(++tmSerialNo));
						return result;
					} else break;//9000					
			}
			
		
			
			//pack advice	
			String advice = null;
			CmasKernel kernel = new CmasKernel();
			CmasDataSpec deductAdvice = new CmasDataSpec();
			kernel.readerField2CmasSpec(pprTxnReqOffline, pprAuthTxnOffline, deductAdvice, configManager);
			advice = kernel.packRequeset(CmasDataSpec.CmasReqField._0220.getField(), deductAdvice);
			
			//update TxnBatch table
			TxnBatch tb = configManager.getTxnBatch();
			tb.setDeductCnt(tb.getDeductCnt()+1);
			tb.setDeductAmt(tb.getDeductAmt()+amt);			
			//update Advice request into batch_detail table
			this.addRecord2BatchDetail(pprTxnReqOffline.getRespNewDeviceID(), 					
					configManager.getBatchNo(),
					deductAdvice.getT0100(),
					deductAdvice.getT0300(), 
					deductAdvice.getT3700(), 
					advice, amt);			
			//update TMSerialNo
			configManager.setTMSerialNo(String.valueOf(++tmSerialNo));
			
			//check send advice request immediately or not
			this.checkSendAdviceNow(ssl, advice);
			
			/*
			//send advice
			try {
				ssl.join();
				String cmasResp = ssl.sendRequest(cmasReq);
				logger.debug("CMAS deduct advice Response:"+cmasResp);
				if(cmasResp==null){logger.error("deduct Advice no response");}
				// Txn Success, SN++				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("ssl join exception:"+e.getMessage());
			}	
			*/
			
			
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			
		} finally{			
			try{
				logger.debug("finally");
				if(advices!=null) advices.stopSendAdvice();
				ssl.disconnect();		
				reader.finish();
				configManager.finish();
			} catch(Exception e) {
				logger.error(e.getMessage());
			}			
		}
		return result;
	}

	
	public IRespCode doReadCardBasicData(){
		IRespCode result = null;
		
		PPR_ReadCardBasicData pprReadCardBasicData = new PPR_ReadCardBasicData();
		
		result = reader.exeCommand(pprReadCardBasicData);
		
		if(result == ReaderRespCode._640E){
			
		}
		
		return result;
	}
	
	public IRespCode doCmasRequestTest(String request){
		IRespCode result=ApiRespCode.SUCCESS;
		
		SSL ssl = getSSLInstance();
		String cmasResp = null;
		try {
			ssl.join();
			cmasResp = ssl.sendRequest(request);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			result = ApiRespCode.HOST_NO_RESPONSE;
			e.printStackTrace();
		}
		
	
		logger.debug("CMAS Resp:"+cmasResp);
		return result;
	}
}

