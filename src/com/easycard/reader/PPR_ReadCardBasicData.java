package com.easycard.reader;

import java.util.Arrays;
import java.util.LinkedHashMap;


import org.apache.log4j.Logger;


import com.easycard.utilities.Util;

public class PPR_ReadCardBasicData extends APDU{

	static Logger logger = Logger.getLogger(PPR_ReadCardBasicData.class);
	public static final String scDescription = "讀取票卡基本資料";
	
	//private static PPR_SignOn sThis = null;
	
	private static final int scReqDataLength = 0x01; //1 byte
	private static final int scReqLength = scReqDataLength + scReqMinLength;
	private static final int scReqInfoLength = scReqDataLength + scReqInfoMinLength;
	private static final int scRespDataLength = 0x88;
	
	private static final int scRespLength = scRespDataLength + scRespMinLength;
	
	
	
	private byte[] mRequest = new byte[scReqLength];
	private byte[] mRespond = null;
	

	
	
	public PPR_ReadCardBasicData(){
		
		Req_NAD = 0;
		Req_PCB = 0; 
		Req_LEN = (byte) scReqInfoLength;
		
		Req_CLA = (byte) 0x80;
		Req_INS = 0x51;			
		Req_P1 = 0x04;
		Req_P2 = 0x00;
		
		Req_Lc = scReqDataLength;
		Req_Le = (byte) scRespDataLength;
		
		mRequest[0] = Req_NAD;
		mRequest[1] = Req_PCB;
		mRequest[2] = Req_LEN;
		mRequest[3] = Req_CLA;
		mRequest[4] = Req_INS;
		mRequest[5] = Req_P1;
		mRequest[6] = Req_P2;
		mRequest[7] = Req_Lc;
		
		mRequest[scReqLength - 2] = Req_Le;
		mRequest[scReqLength - 1] = 0; // EDC
	}
	//++++++++++++++++++++ Request ++++++++++++++++++++
	//LCD Control Flag, 0x00:票卡餘額(default), 0x01:請勿移動票卡
	private static final int pLCDControlFlag = scReqDataOffset + 0;
	private static final int lLCDControlFlag = 1;
	public boolean setReqLCDControlFlag(byte lcdControlFlag) {		
		logger.info("setter:"+String.format("%02X", lcdControlFlag));
		if(lcdControlFlag != 0x01) lcdControlFlag = 0x00;
		mRequest[pLCDControlFlag] = lcdControlFlag;
		return true;
	}
	
	public byte getReqLCDControlFlag() {		
		logger.info("getter:"+mRequest[pLCDControlFlag]);
		return mRequest[pLCDControlFlag];
	}
	//-------------------- Request --------------------
	
	//++++++++++++++++++++ Response ++++++++++++++++++++
	private ResponseField respFld = null;
	private ErrorResponse errRespFld = null;
	private class ResponseField extends BaseResponseAutoParser{
		private int dataBodyLen;
		private final int NORMAL_LEN = 0x88 + 2;//9000, 6403(餘額不足), 6415(需授權交易) 
		
		private final int ERROR1_LEN = 2;//
		
		
		
		public byte purseVersionNumber;
		public byte purseUsageControl;
		public byte[] singleAutoLoadTxnAmt=null;
		public byte[] PID=null;
		public byte cpuAdminKeyKVN;
		
		public byte creditKeyKVN;
		public byte signatureKeyKVN;
		public byte CPUIssuerKeyKVN;
		public byte numberOfDebitTxnRecords;
		public byte[] purseMiniBalanceAllowed = null;
	
		
		public byte[] subAreaCode=null;
		public byte[] purseIssuedDate = null;		
		public byte[] purseExpDate=null;
		public byte[] purseBalance=null;
		public byte[] txnSN=null;
		
		public byte cardType;
		public byte personalProfile;
		public byte[] profileExpDate=null;
		public byte areaCode;
		public byte[] cardPhysicalID=null;
		
		public byte cardPhysicalIDLength;
		public byte[] deposit=null;		
		public byte issuerCode;
		public byte bankCode;
		public byte[] loyaltyCounter = null;
		
		
		public byte[] lastCreditTxnLog=null;
		public byte[] lastDebitTxnLog=null;
		public byte[] cardOneDayQuota=null;
		public byte[] cardOneDayQuotaDate=null;
		public byte merchantLimitUseFlag;
		
		public byte issueStatus;
		public byte blockStatus;
		public byte autoloadCounter;
		public byte[] autoloadDate = null;
		
		public byte[] statusCode=null;
		
		/*
		//120bytes additional fields
		public byte[] loyaltyCounter = null;
		public byte[] anotherEV = null;
		public byte mifareSettingParameter;
		public byte CPUSettingParameter;
		*/
		//constructor
		public ResponseField(int len){
			dataBodyLen = len;
		}

		public byte getPurseVersionNumber() {
			return purseVersionNumber;
		}

		public byte getPurseUsageControl() {
			return purseUsageControl;
		}

		public byte[] getSingleAutoLoadTxnAmt() {
			return singleAutoLoadTxnAmt;
		}

		public byte[] getPID() {
			return PID;
		}

		public byte getCpuAdminKeyKVN() {
			return cpuAdminKeyKVN;
		}

		public byte getCreditKeyKVN() {
			return creditKeyKVN;
		}

		public byte getSignatureKeyKVN() {
			return signatureKeyKVN;
		}

		public byte getCPUIssuerKeyKVN() {
			return CPUIssuerKeyKVN;
		}

		public byte getNumberOfDebitTxnRecords() {
			return numberOfDebitTxnRecords;
		}

		public byte[] getPurseMiniBalanceAllowed() {
			return purseMiniBalanceAllowed;
		}

		public byte[] getSubAreaCode() {
			return subAreaCode;
		}

		public byte[] getPurseIssuedDate() {
			return purseIssuedDate;
		}

		public byte[] getPurseExpDate() {
			return purseExpDate;
		}

		public byte[] getPurseBalance() {
			return purseBalance;
		}

		public byte[] getTxnSN() {
			return txnSN;
		}

		public byte getCardType() {
			return cardType;
		}

		public byte getPersonalProfile() {
			return personalProfile;
		}

		public byte[] getProfileExpDate() {
			return profileExpDate;
		}

		public byte getAreaCode() {
			return areaCode;
		}

		public byte[] getCardPhysicalID() {
			return cardPhysicalID;
		}

		public byte getCardPhysicalIDLength() {
			return cardPhysicalIDLength;
		}

		public byte[] getDeposit() {
			return deposit;
		}

		public byte getIssuerCode() {
			return issuerCode;
		}

		public byte getBankCode() {
			return bankCode;
		}

		public byte[] getLoyaltyCounter() {
			return loyaltyCounter;
		}

		public byte[] getLastCreditTxnLog() {
			return lastCreditTxnLog;
		}

		public byte[] getLastDebitTxnLog() {
			return lastDebitTxnLog;
		}

		public byte[] getCardOneDayQuota() {
			return cardOneDayQuota;
		}

		public byte[] getCardOneDayQuotaDate() {
			return cardOneDayQuotaDate;
		}

		public byte getMerchantLimitUseFlag() {
			return merchantLimitUseFlag;
		}

		public byte getIssueStatus() {
			return issueStatus;
		}

		public byte getBlockStatus() {
			return blockStatus;
		}

		public byte getAutoloadCounter() {
			return autoloadCounter;
		}

		public byte[] getAutoloadDate() {
			return autoloadDate;
		}

		public byte[] getStatusCode() {
			return statusCode;
		}
		
		
		@Override
		protected LinkedHashMap<String, Integer> getFields() {
			// TODO Auto-generated method stub
			LinkedHashMap<String, Integer> maps = new LinkedHashMap<String, Integer>();
			
			if(dataBodyLen == NORMAL_LEN){
				maps.put("purseVersionNumber", 1);
				maps.put("purseUsageControl", 1);
				maps.put("singleAutoLoadTxnAmt", 3);
				maps.put("PID", 8);
				maps.put("cpuAdminKeyKVN", 1);
				
				maps.put("creditKeyKVN", 1);
				maps.put("signatureKeyKVN", 1);
				maps.put("CPUIssuerKeyKVN", 1);
				maps.put("numberOfDebitTxnRecords", 1);
				maps.put("purseMiniBalanceAllowed", 3);
			
				
				maps.put("subAreaCode", 2);
				maps.put("purseIssuedDate", 4);		
				maps.put("purseExpDate", 4);
				maps.put("purseBalance", 3);
				maps.put("txnSN", 3);
				
				maps.put("cardType", 1);
				maps.put("personalProfile", 1);
				maps.put("profileExpDate", 4);
				maps.put("areaCode", 1);
				maps.put("cardPhysicalID", 7);
				
				maps.put("cardPhysicalIDLength", 1);
				maps.put("deposit", 3);		
				maps.put("issuerCode", 1);
				maps.put("bankCode", 1);
				maps.put("loyaltyCounter", 2);
				
				
				maps.put("lastCreditTxnLog", 33);
				maps.put("lastDebitTxnLog", 33);
				maps.put("cardOneDayQuota", 3);
				maps.put("cardOneDayQuotaDate", 2);
				maps.put("merchantLimitUseFlag", 1);
				
				maps.put("issueStatus", 1);
				maps.put("blockStatus", 1);
				maps.put("autoloadCounter", 1);
				maps.put("autoloadDate", 2);
				
				maps.put("statusCode", 2);
				
			} /*else if(dataBodyLen == ERROR1_LEN) {
				
				maps.put("purseVersionNumber",1);
				maps.put("purseUsageControl",1);
				maps.put("singleAutoLoadTxnAmt",3);
				maps.put("PID",8);
				maps.put("cpuAdminKeyKVN",1);
				
				
				maps.put("subAreaCode",2);
				maps.put("purseExpDate",4);
				maps.put("purseBalanceBeforeTxn",3);
				maps.put("TxnSNBeforeTxn",3);				
				maps.put("cardType",1);
				
				maps.put("personalProfile",1);
				maps.put("profileExpDate",4);
				maps.put("areaCode",1);
				maps.put("cardPhysicalID",7);
				maps.put("cardPhysicalIDLength",1);
				
				
				maps.put("deviceID",4);
				maps.put("newDeviceID",6);
				maps.put("serviceProviderID",1);
				maps.put("newServiceProviderID",3);
				maps.put("locationID",1);
				
				maps.put("newLocationID",2);				
				maps.put("deposit",3);
				maps.put("issuerCode",1);
				maps.put("bankCode",1);
				maps.put("loyaltyCounter",2);
				
				
				maps.put("LastCreditTxnLog",33);
				maps.put("msgType",1);
				maps.put("subType",1);
				maps.put("anotherEV",3);				
				maps.put("mifareSettingParameter",1);
				
				maps.put("CPUSettingParameter",1);
				maps.put("statusCode",2);
				
			} else if(dataBodyLen == ERROR2_LEN) {
				
				maps.put("purseVersionNumber",1);				
				maps.put("PID",8);				
				maps.put("CTC",3);				
				maps.put("cardType",1);
				maps.put("personalProfile",1);
				
				
				maps.put("cardPhysicalID",7);				
				maps.put("cardPhysicalIDLength",1);
				maps.put("deviceID",4);				
				maps.put("newDeviceID",6);
				maps.put("serviceProviderID",1);
				
				maps.put("newServiceProviderID",3);
				maps.put("locationID",1);
				maps.put("newLocationID",2);
				maps.put("issuerCode",1);
				maps.put("statusCode",2);
				
			}*/else if(dataBodyLen == ERROR1_LEN) {
				maps.put("statusCode",2);
			} else {
				logger.error("Unknowen dataBody Len:"+dataBodyLen);
				return null;
			}
		
			return maps;
		}
	
	}
	
	public byte getRespPurseVersionNumber(){
		if (respFld==null) return 0x00;		
			
		logger.info("getter:"+String.format("%02X", respFld.getPurseVersionNumber()));
		return respFld.getPurseVersionNumber();	
	}
		
		
	public byte getRespPurseUsageControl(){		
		if (respFld == null) return 0x00;	
		logger.info("getter:"+String.format("%02X", respFld.getPurseUsageControl()));	
		return respFld.getPurseUsageControl();
	}
		
		
	public byte[] getRespSingleAutoLoadTxnAmt(){
		if (respFld == null || respFld.getSingleAutoLoadTxnAmt() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		
		logger.info("getter:"+Util.hex2StringLog(respFld.getSingleAutoLoadTxnAmt()));
		return Arrays.copyOfRange(respFld.getSingleAutoLoadTxnAmt(), 0, 
				respFld.getSingleAutoLoadTxnAmt().length);				
	}
		
	
		
	public byte[] getRespPID(){
		if (respFld == null || respFld.getPID() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getPID()));
		return Arrays.copyOfRange(respFld.getPID(), 0, 
				respFld.getPID().length);			
	}
		
		
	public byte getRespCPUAdminKeyKVN(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
			
		logger.info("getter:"+String.format("%02X", respFld.getCpuAdminKeyKVN()));
		return respFld.getCpuAdminKeyKVN();
	}
		
		
	public byte getRespCreditKeyKVN(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
			
		logger.info("getter:"+String.format("%02X", respFld.getCreditKeyKVN()));
		return respFld.getCreditKeyKVN();	
	}
		
		
	public byte getRespSignatureKeyKVN(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getSignatureKeyKVN()));
		return respFld.getSignatureKeyKVN();
	}
		
	public byte getRespCPUIssuerKeyKVN(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getCPUIssuerKeyKVN()));
		return respFld.getCPUIssuerKeyKVN();
	}
		

	public byte getRespNumberOfDebitTxnRecords(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getNumberOfDebitTxnRecords()));
		return respFld.getNumberOfDebitTxnRecords();
	}


	public byte[] getRespPurseMiniBalanceAllowed(){
		if (respFld == null || respFld.getPurseMiniBalanceAllowed() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getPurseMiniBalanceAllowed()));
		return Arrays.copyOfRange(respFld.getPurseMiniBalanceAllowed(), 0, 
				respFld.getPurseMiniBalanceAllowed().length);
	}
		
		
		
	public byte[] getRespSubAreaCode(){
		if (respFld == null || respFld.getSubAreaCode() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getSubAreaCode()));
		return Arrays.copyOfRange(respFld.getSubAreaCode(), 0, 
				respFld.getSubAreaCode().length);
	}
		
	public byte[] getRespPurseIssuedDate(){
		if (respFld == null || respFld.getPurseIssuedDate() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getPurseIssuedDate()));
		return Arrays.copyOfRange(respFld.getPurseIssuedDate(), 0, 
				respFld.getPurseIssuedDate().length);
	}
		
	public byte[] getRespPurseExpDate(){
		if (respFld == null || respFld.getPurseExpDate() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getPurseExpDate()));
		return Arrays.copyOfRange(respFld.getPurseExpDate(), 0, 
				respFld.getPurseExpDate().length);
	
	}
			

		
	public byte[] getRespPurseBalance(){
		if (respFld == null || respFld.getPurseBalance() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getPurseBalance()));
		return Arrays.copyOfRange(respFld.getPurseBalance(), 0, 
				respFld.getPurseBalance().length);
	}
		
		
	public byte[] getRespTxnSN(){
		if (respFld == null || respFld.getTxnSN() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getTxnSN()));
		return Arrays.copyOfRange(respFld.getTxnSN(), 0, 
				respFld.getTxnSN().length);	
	}
		
		
	public byte getRespCardType(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getCardType()));
		return respFld.getCardType();	
	}
		


	public byte getRespPersonalProfile(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getPersonalProfile()));
		return respFld.getPersonalProfile();
	}
		
		
	public byte[] getRespProfileExpDate(){
		if (respFld == null || respFld.getProfileExpDate() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getProfileExpDate()));
		return Arrays.copyOfRange(respFld.getProfileExpDate(), 0, 
				respFld.getProfileExpDate().length);
	}
		
		
	public byte getRespAreaCode(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getAreaCode()));
		return respFld.getAreaCode();
	}
		
		
	public byte[] getRespCardPhysicalID(){
		if (respFld == null || respFld.getCardPhysicalID() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getCardPhysicalID()));
		return Arrays.copyOfRange(respFld.getCardPhysicalID(), 0, 
				respFld.getCardPhysicalID().length);	
	}
		
		
	public byte getRespCardPhysicalIDLength(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getCardPhysicalIDLength()));
		return respFld.getCardPhysicalIDLength();
	}
		
	
	public byte[] getRespDeposit(){
		if (respFld == null || respFld.getDeposit() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getDeposit()));
		return Arrays.copyOfRange(respFld.getDeposit(), 0, 
				respFld.getDeposit().length);
	}


	public byte getRespIssuerCode(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getIssuerCode()));
		return respFld.getIssuerCode();
	}
		


	public byte getRespBankCode(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getBankCode()));
		return respFld.getBankCode();
	}

	public byte[] getRespLoyaltyCounter(){
		if (respFld == null || respFld.getLoyaltyCounter() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getLoyaltyCounter()));
		return Arrays.copyOfRange(respFld.getLoyaltyCounter(), 0, 
				respFld.getLoyaltyCounter().length);
	}

	
	public byte[] getRespLastCreditTxnLog(){
		if (respFld == null || respFld.getLastCreditTxnLog() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getLastCreditTxnLog()));
		return Arrays.copyOfRange(respFld.getLastCreditTxnLog(), 0, 
				respFld.getLastCreditTxnLog().length);
	}
	
	public byte[] getRespLastDebitTxnLog(){
		if (respFld == null || respFld.getLastDebitTxnLog() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getLastDebitTxnLog()));
		return Arrays.copyOfRange(respFld.getLastDebitTxnLog(), 0, 
				respFld.getLastDebitTxnLog().length);
	}


	public byte[] getRespCardOneDayQuota(){
		if (respFld == null || respFld.getCardOneDayQuota() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getCardOneDayQuota()));
		return Arrays.copyOfRange(respFld.getCardOneDayQuota(), 0, 
				respFld.getCardOneDayQuota().length);
	}
		
		
	public byte[] getRespCardOneDayQuotaDate(){
		if (respFld == null || respFld.getCardOneDayQuotaDate() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getCardOneDayQuotaDate()));
		return Arrays.copyOfRange(respFld.getCardOneDayQuotaDate(), 0, 
				respFld.getCardOneDayQuotaDate().length);
	}
		
		
	public byte getRespMerchantLimitUseFlag(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getMerchantLimitUseFlag()));
		return respFld.getMerchantLimitUseFlag();
	}
	
	public byte getRespIssueStatus(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getIssueStatus()));
		return respFld.getIssueStatus();
	}
	
	public byte getRespBlockStatus(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getBlockStatus()));
		return respFld.getBlockStatus();
	}
	
	public byte getRespAutoloadCounter(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getAutoloadCounter()));
		return respFld.getAutoloadCounter();
	}
	
	public byte[] getRespAutoloadDate(){
		if (respFld == null || respFld.getAutoloadDate() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getAutoloadDate()));
		return Arrays.copyOfRange(respFld.getAutoloadDate(), 0, 
				respFld.getAutoloadDate().length);
	}
		
		
	public byte[] getRespStatusCode(){
		if (respFld == null || respFld.getStatusCode() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getStatusCode()));
		return Arrays.copyOfRange(respFld.getStatusCode(), 0, 
				respFld.getStatusCode().length);
	}	
	
	//-------------------- Response --------------------
	public ErrorResponse getErrorRespFld(){
		return errRespFld;
	}
	
	@Override
	public byte[] GetRequest() {
		// TODO Auto-generated method stub
		
		mRequest[scReqLength - 1] = Req_EDC = getEDC(mRequest, scReqLength);
		logger.debug(PPR_ReadCardBasicData.class.getName()+" request:" + Util.hex2StringLog(mRequest));
		
		
		return mRequest;
	}

	@Override
	public boolean SetRequestData(byte[] bytes) {
		if (bytes == null || bytes.length != scReqDataLength) {
			return false;
		}
		
		System.arraycopy(bytes, 0, mRequest, scReqDataOffset, scReqDataLength);
		return true;
	}

	@Override
	public int GetReqRespLength() {
		// TODO Auto-generated method stub
		return scRespLength;
	}

	@Override
	public void debugResponseData() {
		// TODO Auto-generated method stub
		if(mRespond != null){
			logger.debug(PPR_ReadCardBasicData.class.getName()+" recv:" + Util.hex2StringLog(mRespond));
	
			logger.debug("Purse Version No.:"+String.format("(%02X)", this.getRespPurseVersionNumber()));
			logger.debug("Purse Usgage Control:"+String.format("(%02X)", this.getRespPurseUsageControl()));
			logger.debug("Single AutoLoad Txn Amt:"+Util.hex2StringLog(this.getRespSingleAutoLoadTxnAmt()));
			logger.debug("PID:"+Util.hex2StringLog(this.getRespPID()));
			logger.debug("CPU Admin. Key KVN:"+String.format("(%02X)", this.getRespCPUAdminKeyKVN()));
			
			logger.debug("Credit Key KVN:"+String.format("(%02X)", this.getRespCreditKeyKVN()));
			logger.debug("Sign.key KVN:"+String.format("(%02X)", this.getRespSignatureKeyKVN()));
			logger.debug("CPU Issuer key KVN:"+String.format("(%02X)", this.getRespCPUIssuerKeyKVN()));
			logger.debug("Number of Debit Txn Records:"+String.format("(%02X)", this.getRespNumberOfDebitTxnRecords()));
			logger.debug("Purse Mini. Balance Allowed:"+Util.hex2StringLog(this.getRespPurseMiniBalanceAllowed()));
			
			logger.debug("Sub Area Code:"+Util.hex2StringLog(this.getRespSubAreaCode()));
			logger.debug("Purse Issued Date:"+Util.hex2StringLog(this.getRespPurseIssuedDate()));
			
			logger.debug("Purse Exp. Date:"+Util.hex2StringLog(this.getRespPurseExpDate()));
			logger.debug("Purse Balance :"+Util.hex2StringLog(this.getRespPurseBalance()));
			logger.debug("Txn SN:"+Util.hex2StringLog(this.getRespTxnSN()));
			
			logger.debug("Card Type:"+String.format("(%02X)", this.getRespCardType()));
			logger.debug("Personal Profile:"+String.format("(%02X)", this.getRespPersonalProfile()));
			logger.debug("Profile Exp Date:"+Util.hex2StringLog(this.getRespProfileExpDate()));
			logger.debug("Area Code:"+String.format("(%02X)", this.getRespAreaCode()));
			logger.debug("Card Physical ID:"+Util.hex2StringLog(this.getRespCardPhysicalID()));
			
			logger.debug("Card Physical ID Len:"+String.format("(%02X)", this.getRespCardPhysicalIDLength()));
			
			
			logger.debug("Deposit:"+Util.hex2StringLog(this.getRespDeposit()));
			logger.debug("Issuer Code:"+String.format("(%02X)", this.getRespIssuerCode()));
			logger.debug("Bank Code:"+String.format("(%02X)", this.getRespBankCode()));
			
			logger.debug("Last Credit Txn Log:"+Util.hex2StringLog(this.getRespLastCreditTxnLog()));
			logger.debug("Last Debit Txn Log:"+Util.hex2StringLog(this.getRespLastDebitTxnLog()));
			
			
			logger.debug("Card One Day Quota:"+Util.hex2StringLog(this.getRespCardOneDayQuota()));
			logger.debug("Card One Day Quota Date:"+Util.hex2StringLog(this.getRespCardOneDayQuotaDate()));
			logger.debug("Merchant Limit Use Flag:"+String.format("(%02X)", this.getRespMerchantLimitUseFlag()));
			logger.debug("Issue Status:"+String.format("(%02X)", this.getRespIssueStatus()));
			logger.debug("Block Status:"+String.format("(%02X)", this.getRespBlockStatus()));
			logger.debug("Autoload Counter:"+String.format("(%02X)", this.getRespAutoloadCounter()));
			
			logger.debug("Autoload Date:"+Util.hex2StringLog(this.getRespAutoloadDate()));
			
						
		}
		else
			logger.error("responseBuffer NULL");
	
	}

	@Override
	public byte[] GetRespond() {
		// TODO Auto-generated method stub
		return mRespond;
	}

	@Override
	public boolean SetRespond(byte[] bytes) {
		// TODO Auto-generated method stub
				
		try{
			logger.info("Start");
			if(bytes==null || bytes.length <=scRespDataOffset){
				logger.error("setRespond buffer was Null or len<=3");
				return false;
			}
			
			//check total Length
			int dataLength = (bytes[2] & 0x000000FF) + (bytes[1] << 8 & 0x0000FF00) + (bytes[0] << 16 & 0x00FF0000);
			logger.debug("dataBody Len:"+dataLength);
			if(bytes.length != dataLength+scRespDataOffset+1){//+1 was checkSum
				logger.error("responsee totalLen was UnComplete");
				return false;
			}
			Resp_SW1 = bytes[scRespDataOffset + dataLength - 2];
			Resp_SW2 = bytes[scRespDataOffset + dataLength - 2 +1];
			
					
			//check checkSum
			byte sum = getEDC(bytes, bytes.length);
			if (sum != bytes[bytes.length - 1]) {
				// check sum error...
				logger.error("CheckSum error");
				return false;
			}
			
			
			//copy buffer to mResponse
			mRespond = Arrays.copyOf(bytes, bytes.length);
			byte[] b = Arrays.copyOfRange(bytes, scRespDataOffset, scRespDataOffset+dataLength);
			
			if(ErrorResponse.isErrorResponse(this.GetRespCode())){
				//for Reader Error Response, 640E, 610F, 6418, 6103 
				errRespFld = new ErrorResponse(dataLength);
				errRespFld.parse(b);
			} else {
				//Success Response
				respFld = new ResponseField(dataLength);
				respFld.parse(b);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		

		logger.info("end");			
		return true;
	}

}
