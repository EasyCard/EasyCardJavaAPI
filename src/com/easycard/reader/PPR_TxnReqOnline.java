package com.easycard.reader;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.TimeZone;

import org.apache.log4j.Logger;


import com.easycard.utilities.Util;

public class PPR_TxnReqOnline extends APDU{

	static Logger logger = Logger.getLogger(PPR_TxnReqOnline.class);
	public static final String scDescription = "讀取連線帳務交易相關欄位";
	
	
	
	private static final int scReqDataLength = 0x40;
	private static final int scReqLength = scReqDataLength + scReqMinLength;
	private static final int scReqInfoLength = scReqDataLength + scReqInfoMinLength;
	private static final int scRespDataLength = 0xFD;	
	private static final int scRespLength = scRespDataLength + scRespMinLength;
	
	
	
	private byte[] mRequest = new byte[scReqLength];
	private byte[] mRespond = null;
	

	
	
	public PPR_TxnReqOnline(){
		
		Req_NAD = 0;
		Req_PCB = 0; 
		Req_LEN = (byte) scReqInfoLength;
		
		Req_CLA = (byte) 0x80;
		Req_INS = 0x11;			
		Req_P1 = 0x01;
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
	//Msg Type
	private static final int pReqMsgType = scReqDataOffset + 0;
	private static final int lReqMsgType = 1;
	public boolean setReqMsgType(byte msgType) {		
		logger.info("setter:"+msgType);
		mRequest[pReqMsgType] = msgType;
		
		return true;
	}
	
	public byte getReqMsgType() {		
		logger.info("getter:"+mRequest[pReqMsgType]);
		return mRequest[pReqMsgType];
	}
	
	//Sub Type
	private static final int pReqSubType = pReqMsgType + lReqMsgType;
	private static final int lReqSubType = 1;
	public boolean setReqSubType(byte subType) {		
		logger.info("setter:"+subType);
		mRequest[pReqSubType] = subType;				
		return true;
	}
	
	public byte getReqSubType() {		
		logger.info("getter:"+mRequest[pReqSubType]);
		 				
		return mRequest[pReqSubType];
	}
	
	//TM Location ID
	private static final int pReqTMLocationID = pReqSubType + lReqSubType;
	private static final int lReqTMLocationID = 10;
	public boolean setReqTMLocationID(String id) {
		
		logger.info("setter:"+id);
		if (id == null || id.length() > lReqTMLocationID) {
			logger.error("id null or len wrong");
			return false;
		}
		
		byte []b = Util.paddingChar(id, false, (byte) '0', lReqTMLocationID);		
		System.arraycopy(b, 0, mRequest, pReqTMLocationID, lReqTMLocationID);
		
		return true;
	}
	
	public String getReqTMLocationID() {
		byte[] b = Arrays.copyOfRange(mRequest, pReqTMLocationID, 
				pReqTMLocationID + lReqTMLocationID);
		String result = new String(b);		
		logger.info("getter:"+result);
		return result;
	}
	
	//TM ID
	/* TM ID, 2 bytes, TM, 終端機(TM)機號, ASCII, 右靠左補0 */
	private static final int pReqTMID = pReqTMLocationID + lReqTMLocationID;
	private static final int lReqTMID = 2;
	public boolean setReqTMID(String id) {
		
		logger.info("setter:"+id);
		if (id == null || id.length() > lReqTMID) {
			logger.error("id null or len wrong");
			return false;
		}
		
		byte []b = Util.paddingChar(id, false, (byte) '0', lReqTMID);		
		System.arraycopy(b, 0, mRequest, pReqTMID, lReqTMID);
				
		return true;
	}
	
	public String getReqTMID() {
		byte[] b = Arrays.copyOfRange(mRequest, pReqTMID, 
				pReqTMID + lReqTMID);
				
		String result = new String(b);
		logger.info("getter:"+result);
		return result;
	}
	
	/* TM TXN Date Time, 14 bytes, TM, 終端機(TM)交易日期時間, ASCII, YYYYMMDDhhmmss */
	private static final int pReqTMTXNDateTime = pReqTMID + lReqTMID;
	private static final int lReqTMTXNDateTime = 14;
	public void setReqTMTXNDateTime(int unixTimeStamp) {
		
		logger.info("setter:"+unixTimeStamp);
		String dateTime = Util.sGetDateTime(unixTimeStamp);
		
		//logger.info("setter: int("+unixTimeStamp+"), format:"+dateTime);
		for (int i = 0; i < lReqTMTXNDateTime; i ++) {
			mRequest[pReqTMTXNDateTime + i] = (byte) dateTime.charAt(i);
		}
	}
	
	public String getReqTMTXNDateTime() {
		byte[] b = Arrays.copyOfRange(mRequest, pReqTMTXNDateTime, 
				pReqTMTXNDateTime + lReqTMTXNDateTime);
		String result = new String(b);
		logger.info("getter:"+result);
		return result;
	}
	
	/* TM Serial Number, 6 bytes, TM, 終端機(TM)交易序號, ASCII, 靠右左補0, 值須為0~9, 交易成功時進號, 失敗時不進號 */
	private static final int pcReqTMSerialNumber = pReqTMTXNDateTime + lReqTMTXNDateTime;
	private static final int lcReqTMSerialNumber = 6;
	public boolean setReqTMSerialNumber(int sn) {
				
		logger.info("setter:"+sn);
		String s = String.format("%06d", sn);
		
		
		for (int i = 0; i < s.length(); i ++) {
			mRequest[pcReqTMSerialNumber + i] = (byte) s.charAt(i);
		}
		return true;
	}
	
	public byte[] getReqTMSerialNumber() {
		
		byte []result = Arrays.copyOfRange(mRequest, pcReqTMSerialNumber, 
				pcReqTMSerialNumber + lcReqTMSerialNumber);
		logger.info("getter:"+Util.bcd2Ascii(result));
		return result;
	}
	
	/* TM Agent Number, 4 bytes, TM, 終端機(TM)收銀員代號, ASCII, 靠右左補0, 值須為0~9 */
	private static final int pReqTMAgentNumber = pcReqTMSerialNumber + lcReqTMSerialNumber;
	private static final int lReqTMAgentNumber = 4;
	public boolean setReqTMAgentNumber(String an) {
		
		logger.info("setter:"+an);
		if (an == null || an.length() > lReqTMAgentNumber) {
			logger.error("an is null or len wrong");
			return false;
		}
		
		for (int i = 0; i < an.length(); i ++) {
			if (an.charAt(i) < '0' || an.charAt(i) > '9') {
				logger.error("value'range was 0~9");
				return false;
			}
		}
		
		byte []b = Util.paddingChar(an, false, (byte) '0', lReqTMAgentNumber);		
		System.arraycopy(b, 0, mRequest, pReqTMAgentNumber, lReqTMAgentNumber);
		return true;
	}
	
	public String getReqTMAgentNumber() {
		byte[] b = Arrays.copyOfRange(mRequest, pReqTMAgentNumber, 
				pReqTMAgentNumber + lReqTMAgentNumber);
		String result = new String(b); 
		return result;
	}
	
	/* TXN Date Time, 4 bytes, TM, 交易日期, Unsigned and LSB First (UnixDateTime) */
	private static final int pReqTXNDateTime = pReqTMAgentNumber + lReqTMAgentNumber;
	private static final int lReqTXNDateTime = 4;
	public void setReqTXNDateTime(int unixTimeStamp, String timeZone) {
		
		logger.info("setter: intUnixTime("+unixTimeStamp+"),timeZone:"+timeZone);
		TimeZone tz = TimeZone.getTimeZone(timeZone);
		int offset = tz.getOffset(unixTimeStamp * 1000L);
		unixTimeStamp += offset / 1000;
		
		mRequest[pReqTXNDateTime] = (byte) (unixTimeStamp & 0x000000FF);
		mRequest[pReqTXNDateTime + 1] = (byte) ((unixTimeStamp & 0x0000FF00) >> 8);
		mRequest[pReqTXNDateTime + 2] = (byte) ((unixTimeStamp & 0x00FF0000) >> 16);
		mRequest[pReqTXNDateTime + 3] = (byte) ((unixTimeStamp & 0xFF000000) >> 24);
				
	}
	
	/* TXN Date Time, 4 bytes, TM, 交易日期, Unsigned and LSB First (UnixDateTime) */
	private static final int pReqTxnAmt = pReqTXNDateTime + lReqTXNDateTime;
	private static final int lReqTxnAmt = 3;
	public void setReqTxnAmt(int txnAmt) {
		
		logger.info("setter:"+txnAmt);

		byte []result = Util.paddingChar(Util.int2ByteArray(txnAmt), false, (byte)0x00, lReqTxnAmt);
		Util.arrayReverse(result);
		
		System.arraycopy(result, 0, mRequest, pReqTxnAmt, lReqTxnAmt);		
	}
	
	public byte[] getReqTxnAmt() {
		byte []result = Arrays.copyOfRange(mRequest, pReqTxnAmt, 
				pReqTxnAmt + lReqTxnAmt);
		logger.info("getter:"+Util.bcd2Ascii(result));
		return result;
	}
	
	//Read Purse Flag
	private static final int pReqReadPurseFlag = pReqTxnAmt + lReqTxnAmt;
	private static final int lReqReadPurseFlag = 1;
	public void setReqReadPurseFlag(byte b) {
		
		logger.info("setter:"+b);
		mRequest[pReqReadPurseFlag] = b;		
	}
	
	public byte getReqReadPurseFlag() {		
		logger.info("getter:"+mRequest[pReqReadPurseFlag]);
		 				
		return mRequest[pReqReadPurseFlag];
	}
	
	//New Refund Fee，MsgType <> 0x05時，補0x00, Unsigned and LSB First	
	private static final int pReqNewRefundFee = pReqReadPurseFlag + lReqReadPurseFlag;
	private static final int lReqNewRefundFee = 2;
	public void setReqNewRefundFee(int fee) {
		
		logger.info("setter:"+fee);

		byte []result = Util.paddingChar(Util.int2ByteArray(fee), false, (byte)'0', lReqNewRefundFee);
		Util.arrayReverse(result);
		
		System.arraycopy(result, 0, mRequest, pReqNewRefundFee, lReqNewRefundFee);		
	}
	
	public byte[] getReqNewRefundFee() {
		byte []result = Arrays.copyOfRange(mRequest, pReqNewRefundFee, 
				pReqNewRefundFee + lReqNewRefundFee);
		logger.info("getter:"+Util.bcd2Ascii(result));
		return result;
	}
	
	//Broken Fee
	private static final int pReqBrokenFee = pReqNewRefundFee + lReqNewRefundFee;
	private static final int lReqBrokenFee = 2;
	public void setReqBrokenFee(int fee) {
		
		logger.info("setter:"+fee);

		byte []result = Util.paddingChar(Util.int2ByteArray(fee), false, (byte)'0', lReqBrokenFee);
		Util.arrayReverse(result);
		
		System.arraycopy(result, 0, mRequest, pReqBrokenFee, lReqBrokenFee);		
	}
	
	public byte[] getReqBrokenFee() {
		byte []result = Arrays.copyOfRange(mRequest, pReqBrokenFee, 
				pReqBrokenFee + lReqBrokenFee);
		logger.info("getter:"+Util.bcd2Ascii(result));
		return result;
	}
	
	//Customer Fee
	private static final int pReqCustomerFee = pReqBrokenFee + lReqBrokenFee;
	private static final int lReqCustomerFee = 2;
	public void setReqCustomerFee(int fee) {
		
		logger.info("setter:"+fee);

		byte []result = Util.paddingChar(Util.int2ByteArray(fee), false, (byte)'0', lReqCustomerFee);
		Util.arrayReverse(result);
		
		System.arraycopy(result, 0, mRequest, pReqCustomerFee, lReqCustomerFee);		
	}
	
	public byte[] getReqCustomerFee() {
		byte []result = Arrays.copyOfRange(mRequest, pReqCustomerFee, 
				pReqCustomerFee + lReqCustomerFee);
		logger.info("getter:"+Util.bcd2Ascii(result));
		return result;
	}
	//-------------------- Request --------------------
	
	
	
	
	//++++++++++++++++++++ Response ++++++++++++++++++++
	private ResponseField respFld = null;
	private ErrorResponse errRespFld = null;
	private class ResponseField extends BaseResponseAutoParser{
		private int dataBodyLen;
		private final int NORMAL_LEN = 0xFD + 2;//9000 
		
		private final int ERROR1_LEN = 2;
		
		
		
		public byte purseVersionNumber;
		public byte purseUsageControl;
		public byte[] singleAutoLoadTxnAmt=null;
		public byte[] PID=null;
		public byte cpuAdminKeyKVN;
		
		public byte creditKeyKVN;
		public byte signatureKeyKVN;
		public byte cpuIssuerKeyKVN;
		public byte[] CTC=null;
		public byte txnMode;
		
		public byte txnQualifier;
		public byte[] subAreaCode=null;
		public byte[] purseExpDate=null;
		public byte[] purseBalanceBeforeTxn=null;
		public byte[] txnSNBeforeTxn=null;
		
		public byte cardType;
		public byte personalProfile;
		public byte[] profileExpDate=null;
		public byte areaCode;
		public byte[] cardPhysicalID=null;
		
		public byte cardPhysicalIDLength;
		public byte[] txnAmt=null;
		public byte specVersionNumber;
		public byte[] readerFWVersion=null;
		public byte[] deviceID=null;
		
		public byte[] newDeviceID=null;
		public byte serviceProviderID;
		public byte[] newServiceProviderID=null;
		public byte locationID;
		public byte[] newLocationID=null;
		
		public byte[] deposit=null;
		public byte issuerCode;
		public byte bankCode;
		public byte[] loyaltyCounter=null;
		public byte[] samID=null;
		
		public byte[] samSN=null;
		public byte[] samCRN=null;
		public byte samKVN;
		public byte[] STAC=null;
		public byte[] tmSerialNumber=null;
		
		public byte[] lastCreditTxnLog=null;		
		public byte[] readerAVRData=null;		
		public byte msgType;
		public byte subType;
		public byte[] SID=null;
		
		public byte hostAdminKeyKVN;
		public byte[] txnCrypto=null;			
		public byte[] statusCode=null;
		
		
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
			return cpuIssuerKeyKVN;
		}

		public byte[] getCTC() {
			return CTC;
		}

		public byte getTxnMode() {
			return txnMode;
		}

		public byte getTxnQualifier() {
			return txnQualifier;
		}

		public byte[] getSubAreaCode() {
			return subAreaCode;
		}

		public byte[] getPurseExpDate() {
			return purseExpDate;
		}

		public byte[] getPurseBalanceBeforeTxn() {
			return purseBalanceBeforeTxn;
		}

		public byte[] getTxnSNBeforeTxn() {
			return txnSNBeforeTxn;
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

		public byte[] getTxnAmt() {
			return txnAmt;
		}

		public byte getSpecVersionNumber() {
			return specVersionNumber;
		}

		public byte[] getReaderFWVersion() {
			return readerFWVersion;
		}

		public byte[] getDeviceID() {
			return deviceID;
		}

		public byte[] getNewDeviceID() {
			return newDeviceID;
		}

		public byte getServiceProviderID() {
			return serviceProviderID;
		}

		public byte[] getNewServiceProviderID() {
			return newServiceProviderID;
		}

		public byte getLocationID() {
			return locationID;
		}

		public byte[] getNewLocationID() {
			return newLocationID;
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

		public byte[] getSamID() {
			return samID;
		}

		public byte[] getSamSN() {
			return samSN;
		}

		public byte[] getSamCRN() {
			return samCRN;
		}

		public byte getSamKVN() {
			return samKVN;
		}

		public byte[] getSTAC() {
			return STAC;
		}

		public byte[] getTmSerialNumber() {
			return tmSerialNumber;
		}

		public byte[] getLastCreditTxnLog() {
			return lastCreditTxnLog;
		}

		public byte[] getReaderAVRData() {
			return readerAVRData;
		}

		public byte getMsgType() {
			return msgType;
		}

		public byte getSubType() {
			return subType;
		}

		public byte[] getSID() {
			return SID;
		}

		public byte getHostAdminKeyKVN() {
			return hostAdminKeyKVN;
		}

		public byte[] getTxnCrypto() {
			return txnCrypto;
		}

		
		
		/*
		//120bytes additional fields
		public byte[] getLoyaltyCounter(){
			return loyaltyCounter;
		}
		
		public byte[] getAnotherEV(){
			return anotherEV;
		}
		
		public byte getMifareSettingParameter(){
			return mifareSettingParameter;
		}
		
		public byte getCPUSettingParameter(){
			return CPUSettingParameter;
		}
		*/
		@Override
		protected LinkedHashMap<String, Integer> getFields() {
			// TODO Auto-generated method stub
			LinkedHashMap<String, Integer> maps = new LinkedHashMap<String, Integer>();
			
			if(dataBodyLen == NORMAL_LEN){//253
				maps.put("purseVersionNumber", 1);
				maps.put("purseUsageControl", 1);
				maps.put("singleAutoLoadTxnAmt", 3);
				maps.put("PID", 8);
				maps.put("cpuAdminKeyKVN", 1);
				
				maps.put("creditKeyKVN", 1);
				maps.put("signatureKeyKVN", 1);
				maps.put("cpuIssuerKeyKVN", 1);
				maps.put("CTC", 3);
				maps.put("txnMode", 1);
				
				maps.put("txnQualifier", 1);
				maps.put("subAreaCode", 2);
				maps.put("purseExpDate", 4);
				maps.put("purseBalanceBeforeTxn", 3);
				maps.put("txnSNBeforeTxn", 3);
				
				maps.put("cardType", 1);
				maps.put("personalProfile", 1);
				maps.put("profileExpDate", 4);
				maps.put("areaCode", 1);
				maps.put("cardPhysicalID", 7);
				
				maps.put("cardPhysicalIDLength", 1);
				maps.put("txnAmt", 3);
				maps.put("specVersionNumber", 1);
				maps.put("readerFWVersion", 6);
				maps.put("deviceID", 4);
				
				maps.put("newDeviceID", 6);
				maps.put("serviceProviderID",1);
				maps.put("newServiceProviderID", 3);
				maps.put("locationID", 1);
				maps.put("newLocationID", 2);
				
				maps.put("deposit", 3);
				maps.put("issuerCode", 1);
				maps.put("bankCode", 1);
				maps.put("loyaltyCounter", 2);
				maps.put("samID", 8);
				
				maps.put("samSN", 4);
				maps.put("samCRN", 8);
				maps.put("samKVN", 1);
				maps.put("STAC", 8);
				maps.put("tmSerialNumber", 6);
				
				maps.put("lastCreditTxnLog", 33);		
				maps.put("readerAVRData", 83);		
				maps.put("msgType", 1);
				maps.put("subType", 1);
				maps.put("SID", 8);
				
				maps.put("hostAdminKeyKVN", 1);
				maps.put("txnCrypto", 8);			
				maps.put("statusCode", 2);
				
			} else if(dataBodyLen == ERROR1_LEN) {
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
	
	
	public boolean getActivedFlag(){
		byte b = getRespPurseUsageControl();		
		return IsBitSet(b, 0);
	}
	
	public boolean getBlockedFlag(){
		byte b = getRespPurseUsageControl();		
		return IsBitSet(b, 1);
	}
	
	public boolean getAutoloadFlag(){
		byte b = getRespPurseUsageControl();			
		return IsBitSet(b, 3);
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
		
		
	public byte[] getRespCTC(){
		if (respFld == null || respFld.getPID() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getCTC()));
		return Arrays.copyOfRange(respFld.getCTC(), 0, 
				respFld.getCTC().length);		
	}
		


	public byte getRespTxnMode(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getTxnMode()));
		return respFld.getTxnMode();
	}


	public byte getRespTxnQualifier(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getTxnQualifier()));
		return respFld.getTxnQualifier();
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
		
		
	public byte[] getRespPurseExpDate(){
		if (respFld == null || respFld.getPurseExpDate() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getPurseExpDate()));
		return Arrays.copyOfRange(respFld.getPurseExpDate(), 0, 
				respFld.getPurseExpDate().length);
	
	}
			

		
	public byte[] getRespPurseBalanceBeforeTxn(){
		if (respFld == null || respFld.getPurseBalanceBeforeTxn() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getPurseBalanceBeforeTxn()));
		return Arrays.copyOfRange(respFld.getPurseBalanceBeforeTxn(), 0, 
				respFld.getPurseBalanceBeforeTxn().length);
	}
		
		
	public byte[] getRespTxnSNBeforeTxn(){
		if (respFld == null || respFld.getTxnSNBeforeTxn() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getTxnSNBeforeTxn()));
		return Arrays.copyOfRange(respFld.getTxnSNBeforeTxn(), 0, 
				respFld.getTxnSNBeforeTxn().length);	
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
		
		
	public byte[] getRespTxnAmt(){
		if (respFld == null || respFld.getTxnAmt() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getTxnAmt()));
		return Arrays.copyOfRange(respFld.getTxnAmt(), 0, 
				respFld.getTxnAmt().length);	
	}
		
	public byte getRespSpecVersionNumber(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getSpecVersionNumber()));
		return respFld.getSpecVersionNumber();		
	}

		
	public byte[] getRespReaderFWVersion(){
		if (respFld == null || respFld.getReaderFWVersion() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getReaderFWVersion()));
		return Arrays.copyOfRange(respFld.getReaderFWVersion(), 0, 
				respFld.getReaderFWVersion().length);
	}
		
		
	public byte[] getRespDeviceID(){
		if (respFld == null || respFld.getDeviceID() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getDeviceID()));
		return Arrays.copyOfRange(respFld.getDeviceID(), 0, 
				respFld.getDeviceID().length);
	}


	public byte[] getRespNewDeviceID(){
		if (respFld == null || respFld.getNewDeviceID() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getNewDeviceID()));
		return Arrays.copyOfRange(respFld.getNewDeviceID(), 0, 
				respFld.getNewDeviceID().length);	
	}

	public byte getRespServiceProviderID(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getServiceProviderID()));
		return respFld.getServiceProviderID();
	}
		
		
	public byte[] getRespNewServiceProviderID(){
		if (respFld == null || respFld.getNewServiceProviderID() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getNewServiceProviderID()));
		return Arrays.copyOfRange(respFld.getNewServiceProviderID(), 0, 
				respFld.getNewServiceProviderID().length);
	}
		
		
	public byte getRespLocationID(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getLocationID()));
		return respFld.getLocationID();
	}


	public byte[] getRespNewLocationID(){
		if (respFld == null || respFld.getNewLocationID() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getNewLocationID()));
		return Arrays.copyOfRange(respFld.getNewLocationID(), 0, 
				respFld.getNewLocationID().length);
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
		//
		
	public byte[] getRespLoyaltyCounter(){
		if (respFld == null || respFld.getLoyaltyCounter() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getLoyaltyCounter()));
		return Arrays.copyOfRange(respFld.getLoyaltyCounter(), 0, 
				respFld.getLoyaltyCounter().length);
	}


	public byte[] getRespSamID(){
		if (respFld == null || respFld.getSamID() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getSamID()));
		return Arrays.copyOfRange(respFld.getSamID(), 0, 
				respFld.getSamID().length);
	}


	public byte[] getRespSamSN(){
		if (respFld == null || respFld.getSamSN() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getSamSN()));
		return Arrays.copyOfRange(respFld.getSamSN(), 0, 
				respFld.getSamSN().length);
		
	}

	public byte[] getRespSamCRN(){
		if (respFld == null || respFld.getSamCRN() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getSamCRN()));
		return Arrays.copyOfRange(respFld.getSamCRN(), 0, 
				respFld.getSamCRN().length);
		
	}

	public byte getRespSamKVN(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getSamKVN()));
		return respFld.getSamKVN();
	}
		


	public byte[] getRespSTAC(){
		if (respFld == null || respFld.getSTAC() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getSTAC()));
		return Arrays.copyOfRange(respFld.getSTAC(), 0, 
				respFld.getSTAC().length);
	}
		///
		
		
	public byte[] getRespTMSerialNumber(){
		if (respFld == null || respFld.getTmSerialNumber() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getTmSerialNumber()));
		return Arrays.copyOfRange(respFld.getTmSerialNumber(), 0, 
				respFld.getTmSerialNumber().length);
	
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
	


	public byte[] getRespReaderAVRData(){
		if (respFld == null || respFld.getReaderAVRData() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getReaderAVRData()));
		return Arrays.copyOfRange(respFld.getReaderAVRData(), 0, 
				respFld.getReaderAVRData().length);
	}

	
	
	public byte getRespMsgType(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getMsgType()));
		return respFld.getMsgType();
	}


	public byte getRespSubType(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getSubType()));
		return respFld.getSubType();
	}
	


	public byte[] getRespSID(){
		if (respFld == null || respFld.getSID() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getSID()));
		return Arrays.copyOfRange(respFld.getSID(), 0, 
				respFld.getSID().length);
	}
		
		
	public byte getRespHostAdminKeyKVN(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getHostAdminKeyKVN()));
		return respFld.getHostAdminKeyKVN();
	}
		
		
	public byte[] getRespTxnCrypto(){
		if (respFld == null || respFld.getTxnCrypto() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getTxnCrypto()));
		return Arrays.copyOfRange(respFld.getTxnCrypto(), 0, 
				respFld.getTxnCrypto().length);
	}	
	
	
	//-------------------- Response --------------------
	public ErrorResponse getErrorRespFld(){
		return errRespFld;
	}
	
	@Override
	public byte[] GetRequest() {
		// TODO Auto-generated method stub
		
		mRequest[scReqLength - 1] = Req_EDC = getEDC(mRequest, scReqLength);
		logger.debug(PPR_TxnReqOnline.class.getName()+" request:" + Util.hex2StringLog(mRequest));
		
		
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
			logger.debug(PPR_TxnReqOnline.class.getName()+" recv:" + Util.hex2StringLog(mRespond));
	
			logger.debug("Purse Version No.:"+String.format("(%02X)", this.getRespPurseVersionNumber()));
			logger.debug("Purse Usgage Control:"+String.format("(%02X)", this.getRespPurseUsageControl()));
			logger.debug("Single AutoLoad Txn Amt:"+Util.hex2StringLog(this.getRespSingleAutoLoadTxnAmt()));
			logger.debug("PID:"+Util.hex2StringLog(this.getRespPID()));
			logger.debug("CPU Admin. Key KVN:"+String.format("(%02X)", this.getRespCPUAdminKeyKVN()));
			
			logger.debug("Credit Key KVN:"+String.format("(%02X)", this.getRespCreditKeyKVN()));
			logger.debug("Sign.key KVN:"+String.format("(%02X)", this.getRespSignatureKeyKVN()));
			logger.debug("CPU Issuer key KVN:"+String.format("(%02X)", this.getRespCPUIssuerKeyKVN()));
			logger.debug("CTC:"+Util.hex2StringLog(this.getRespCTC()));
			logger.debug("TM:"+String.format("(%02X)", this.getRespTxnMode()));
			
			logger.debug("TQ:"+String.format("(%02X)", this.getRespTxnQualifier()));
			logger.debug("Sub Area Code:"+Util.hex2StringLog(this.getRespSubAreaCode()));
			logger.debug("Purse Exp. Date:"+Util.hex2StringLog(this.getRespPurseExpDate()));
			logger.debug("Purse Balance Before Txn:"+Util.hex2StringLog(this.getRespPurseBalanceBeforeTxn()));
			logger.debug("Txn SN Before Txn:"+Util.hex2StringLog(this.getRespTxnSNBeforeTxn()));
			
			logger.debug("Card Type:"+String.format("(%02X)", this.getRespCardType()));
			logger.debug("Personal Profile:"+String.format("(%02X)", this.getRespPersonalProfile()));
			logger.debug("Profile Exp Date:"+Util.hex2StringLog(this.getRespProfileExpDate()));
			logger.debug("Area Code:"+String.format("(%02X)", this.getRespAreaCode()));
			logger.debug("Card Physical ID:"+Util.hex2StringLog(this.getRespCardPhysicalID()));
			
			logger.debug("Card Physical ID Len:"+String.format("(%02X)", this.getRespCardPhysicalIDLength()));
			logger.debug("Txn Amt:"+Util.hex2StringLog(this.getRespTxnAmt()));
			logger.debug("Spec. Version Number:"+String.format("(%02X)", this.getRespSpecVersionNumber()));
			logger.debug("Reader FW Version:"+Util.hex2StringLog(this.getRespReaderFWVersion()));
			logger.debug("Device ID:"+Util.hex2StringLog(this.getRespDeviceID()));
			
			logger.debug("New Device ID:"+Util.hex2StringLog(this.getRespNewDeviceID()));
			logger.debug("Service Provider ID:"+String.format("(%02X)", this.getRespServiceProviderID()));
			logger.debug("New Service Provider ID:"+Util.hex2StringLog(this.getRespNewServiceProviderID()));
			logger.debug("Location ID："+String.format("(%02X)", this.getRespLocationID()));
			logger.debug("New Location ID:"+Util.hex2StringLog(this.getRespNewLocationID()));
			
			logger.debug("Deposit:"+Util.hex2StringLog(this.getRespDeposit()));
			logger.debug("Issuer Code:"+String.format("(%02X)", this.getRespIssuerCode()));
			logger.debug("Bank Code:"+String.format("(%02X)", this.getRespBankCode()));
			logger.debug("Loyalty Counter:"+Util.hex2StringLog(this.getRespLoyaltyCounter()));
			
			logger.debug("SAM ID:"+Util.hex2StringLog(this.getRespSamID()));			
			logger.debug("SAM SN:"+Util.hex2StringLog(this.getRespSamSN()));
			logger.debug("SAM CRN:"+Util.hex2StringLog(this.getRespSamCRN()));
			logger.debug("SAM KVN:"+String.format("(%02X)", this.getRespSamKVN()));
			logger.debug("SID S-TAC:"+Util.hex2StringLog(this.getRespSTAC()));
			
			logger.debug("TM Serial Number:"+Util.hex2StringLog(this.getRespTMSerialNumber()));
			logger.debug("Last Credit Txn Log:"+Util.hex2StringLog(this.getRespLastCreditTxnLog()));
			logger.debug("Reader AVR Data:"+Util.hex2StringLog(this.getRespReaderAVRData()));
			
			
			logger.debug("Msg Type:"+String.format("(%02X)", this.getRespMsgType()));
			logger.debug("Sub Type:"+String.format("(%02X)", this.getRespSubType()));
			
			
			
			logger.debug("SID:"+Util.hex2StringLog(this.getRespSID()));
			logger.debug("Host Admin Key KVN:"+String.format("(%02X)", this.getRespHostAdminKeyKVN()));
			
			logger.debug("Txn Crypto:"+Util.hex2StringLog(this.getRespTxnCrypto()));			
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
				
		try {
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
