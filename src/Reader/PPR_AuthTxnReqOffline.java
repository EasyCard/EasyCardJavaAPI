package Reader;

import java.util.Arrays;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import Utilities.Util;

public class PPR_AuthTxnReqOffline extends APDU{

	static Logger logger = Logger.getLogger(PPR_AuthTxnReqOffline.class);
	public static final String scDescription = "讀取驗證授權交易相關欄位";
	
	//private static PPR_SignOn sThis = null;
	
	private static final int scReqDataLength = 0x40;
	private static final int scReqLength = scReqDataLength + scReqMinLength;
	private static final int scReqInfoLength = scReqDataLength + scReqInfoMinLength;
	private static final int scRespDataLength = 0xFA;
	private static final int scRespLength = scRespDataLength + scRespMinLength;
	
	
	
	private byte[] mRequest = new byte[scReqLength];
	private byte[] mRespond = null;
	

	
	
	public PPR_AuthTxnReqOffline(){
		
		Req_NAD = 0;
		Req_PCB = 0; 
		Req_LEN = (byte) scReqInfoLength;
		
		Req_CLA = (byte) 0x80;
		Req_INS = 0x32;			
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
		
		if(msgType == 0x05){//for 退卡交易
			Arrays.fill(mRequest,pReqNewRefundFee,pReqLCDControlFlag,(byte)0x00);
		}
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
		
		logger.info("setter:"+s);
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
	
	// LCD Control Flag
	private static final int pReqLCDControlFlag = pReqCustomerFee + pReqCustomerFee;
	private static final int lReqLCDControlFlag = 1;
	public boolean setReqLCDControlFlag(byte flag) {		
		logger.info("setter:"+flag);
		mRequest[pReqSubType] = flag;				
		return true;
	}
	
	public byte getReqLCDControlFlag() {		
		logger.info("getter:"+mRequest[pReqLCDControlFlag]);
		 				
		return mRequest[pReqLCDControlFlag];
	}

	
	//-------------------- Request --------------------
	
	//++++++++++++++++++++ Response ++++++++++++++++++++
	private static final int pRespPurseVersionNumber = scRespDataOffset + 0;
	private static final int lRespPurseVersionNumber = 1;
	public byte getRespPurseVersionNumber(){
		if (mRespond == null) {
			return 0x00;
		}		
		return mRespond[pRespPurseVersionNumber];			
	}
	
	private static final int pRespPurseUsageControl = pRespPurseVersionNumber + lRespPurseVersionNumber;
	private static final int lRespPurseUsageControl = 1;
	public byte getRespPurseUsageControl(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespPurseUsageControl];			
	}
	
	private static final int pRespSingleAutoLoadTxnAmt = pRespPurseUsageControl + lRespPurseUsageControl;
	private static final int lRespSingleAutoLoadTxnAmt = 3;
	public byte[] getRespSingleAutoLoadTxnAmt(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespSingleAutoLoadTxnAmt, 
				pRespSingleAutoLoadTxnAmt + lRespSingleAutoLoadTxnAmt);		
	}
	
	private static final int pRespPID = pRespSingleAutoLoadTxnAmt + lRespSingleAutoLoadTxnAmt;
	private static final int lRespPID = 8;
	public byte[] getRespPID(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespPID, 
				pRespPID + lRespPID);		
	}
	
	private static final int pRespCPUAdminKeyKVN = pRespPID + lRespPID;
	private static final int lRespCPUAdminKeyKVN = 1;
	public byte getRespCPUAdminKeyKVN(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespCPUAdminKeyKVN];			
	}
	
	private static final int pRespCreditKeyKVN = pRespCPUAdminKeyKVN + lRespCPUAdminKeyKVN;
	private static final int lRespCreditKeyKVN = 1;
	public byte getRespCreditKeyKVN(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespCreditKeyKVN];			
	}
	
	private static final int pRespSignKeyKVN = pRespCreditKeyKVN + lRespCreditKeyKVN;
	private static final int lRespSignKeyKVN = 1;
	public byte getRespSignKeyKVN(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespSignKeyKVN];			
	}
	
	private static final int pRespCPUIssuerKVN = pRespSignKeyKVN + lRespSignKeyKVN;
	private static final int lRespCPUIssuerKVN = 1;
	public byte getRespCPUIssuerKVN(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespCPUIssuerKVN];			
	}
	
	private static final int pRespCTC = pRespCPUIssuerKVN + lRespCPUIssuerKVN;
	private static final int lRespCTC = 3;
	public byte[] getRespCTC(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespCTC, 
				pRespCTC + lRespCTC);		
	}
	
	private static final int pRespTM = pRespCTC + lRespCTC;
	private static final int lRespTM = 1;
	public byte getRespTM(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespTM];		
	}
	
	private static final int pRespTQ = pRespTM + lRespTM;
	private static final int lRespTQ = 1;
	public byte getRespTQ(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespTQ];		
	}
	
	private static final int pRespSubAreaCode = pRespTQ + lRespTQ;
	private static final int lRespSubAreaCode= 2;
	public byte[] getRespSubAreaCode(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespSubAreaCode, 
				pRespSubAreaCode + lRespSubAreaCode);		
	}
	
	private static final int pRespPurseExpDate = pRespSubAreaCode + lRespSubAreaCode;
	private static final int lRespPurseExpDate= 4;
	public byte[] getRespPurseExpDate(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespPurseExpDate, 
				pRespPurseExpDate + lRespPurseExpDate);		
	}
	
	private static final int pRespPurseBalanceBeforeTxn = pRespPurseExpDate + lRespPurseExpDate;
	private static final int lRespPurseBalanceBeforeTxn= 3;
	public byte[] getRespPurseBalanceBeforeTxn(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespPurseBalanceBeforeTxn, 
				pRespPurseBalanceBeforeTxn + lRespPurseBalanceBeforeTxn);		
	}
	
	private static final int pRespTxnSNBeforeTxn = pRespPurseBalanceBeforeTxn + lRespPurseBalanceBeforeTxn;
	private static final int lRespTxnSNBeforeTxn = 3;
	public byte[] getRespTxnSNBeforeTxn(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespTxnSNBeforeTxn, 
				pRespTxnSNBeforeTxn + lRespTxnSNBeforeTxn);		
	}
	
	private static final int pRespCardType = pRespTxnSNBeforeTxn + lRespTxnSNBeforeTxn;
	private static final int lRespCardType= 1;
	public byte getRespCardType(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespCardType];		
	}
	
	private static final int pRespPersonalProfile = pRespCardType + lRespCardType;
	private static final int lRespPersonalProfile = 1;
	public byte getRespPersonalProfile(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespPersonalProfile];			
	}
	
	private static final int pRespProfileExpDate = pRespPersonalProfile + lRespPersonalProfile;
	private static final int lRespProfileExpDate = 4;
	public byte[] getRespProfileExpDate(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespProfileExpDate, 
				pRespProfileExpDate + lRespProfileExpDate);		
	}
	
	private static final int pRespAreaCode = pRespProfileExpDate + lRespProfileExpDate;
	private static final int lRespAreaCode = 1;
	public byte getRespAreaCode(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespAreaCode];			
	}
	
	private static final int pRespCardPhysicalID = pRespAreaCode + lRespAreaCode;
	private static final int lRespCardPhysicalID = 7;
	public byte[] getRespCardPhysicalID(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespCardPhysicalID, 
				pRespCardPhysicalID + lRespCardPhysicalID);		
	}
	
	private static final int pRespCardPhysicalIDLen = pRespCardPhysicalID + lRespCardPhysicalID;
	private static final int lRespCardPhysicalIDLen = 1;
	public byte getRespCardPhysicalIDLen(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespCardPhysicalIDLen];			
	}
	
	private static final int pRespTxnAmt = pRespCardPhysicalIDLen + lRespCardPhysicalIDLen;
	private static final int lRespTxnAmt = 3;
	public byte[] getRespTxnAmt(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespTxnAmt, 
				pRespTxnAmt + lRespTxnAmt);		
	}
	
	private static final int pRespSpecVersionNumber = pRespTxnAmt + lRespTxnAmt;
	private static final int lRespSpecVersionNumber = 1;
	public byte getRespSpecVersionNumber(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespSpecVersionNumber];			
	}
	
	private static final int pRespReaderFWVersion = pRespSpecVersionNumber + lRespSpecVersionNumber;
	private static final int lRespReaderFWVersion = 6;
	public byte[] getRespReaderFWVersion(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespReaderFWVersion, 
				pRespReaderFWVersion + lRespReaderFWVersion);		
	}
	
	private static final int pRespDeviceID = pRespReaderFWVersion + lRespReaderFWVersion;
	private static final int lRespDeviceID = 4;
	public byte[] getRespDeviceID(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespDeviceID, 
				pRespDeviceID + lRespDeviceID);		
	}
	
	private static final int pRespNewDeviceID = pRespDeviceID + lRespDeviceID;
	private static final int lRespNewDeviceID = 6;
	public byte[] getRespNewDeviceID(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespNewDeviceID, 
				pRespNewDeviceID + lRespNewDeviceID);		
	}
	
	private static final int pRespServiceProviderID = pRespNewDeviceID + lRespNewDeviceID;
	private static final int lRespServiceProviderID = 1;
	public byte getRespServiceProviderID(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespServiceProviderID];		
	}
	
	private static final int pRespNewServiceProviderID = pRespServiceProviderID + lRespServiceProviderID;
	private static final int lRespNewServiceProviderID = 3;
	public byte[] getRespNewServiceProviderID(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespNewServiceProviderID, 
				pRespNewServiceProviderID + lRespNewServiceProviderID);		
	}
	
	private static final int pRespLocationID = pRespNewServiceProviderID + lRespNewServiceProviderID;
	private static final int lRespLocationID = 1;
	public byte getRespLocationID(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespLocationID];		
	}
	
	private static final int pRespNewLocationID = pRespLocationID + lRespLocationID;
	private static final int lRespNewLocationID = 2;
	public byte[] getRespNewLocationID(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespNewLocationID, 
				pRespNewLocationID + lRespNewLocationID);		
	}
	
	private static final int pRespDeposit = pRespNewLocationID + lRespNewLocationID;
	private static final int lRespDeposit = 3;
	public byte[] getRespDeposit(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespDeposit, 
				pRespDeposit + lRespDeposit);		
	}
	
	private static final int pRespIssuerCode = pRespDeposit + lRespDeposit;
	private static final int lRespIssuerCode = 1;
	public byte getRespIssuerCode(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespIssuerCode];		
	}
	
	private static final int pRespBankCode = pRespIssuerCode + lRespIssuerCode;
	private static final int lRespBankCode = 1;
	public byte getRespBankCode(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespBankCode];		
	}
	//
	private static final int pRespCPDReadFlag = pRespBankCode + lRespBankCode;
	private static final int lRespCPDReadFlag = 1;
	public byte getRespCPDReadFlag(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespCPDReadFlag];		
	}
	
	private static final int pRespSPDSAMID = pRespCPDReadFlag + lRespCPDReadFlag;
	private static final int lRespSPDSAMID = 16;
	public byte[] getRespCPDSAMID(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespSPDSAMID, 
				pRespSPDSAMID + lRespSPDSAMID);		
	}
	
	
	private static final int pRespCPDRAN = (byte) (pRespSPDSAMID + lRespSPDSAMID);
	private static final int lRespCPDRAN = 8;
	public byte[] getRespCPDRAN(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespCPDRAN, 
				pRespCPDRAN + lRespCPDRAN);		
	}
	
	
	private static final int pRespCPDKVN = pRespCPDRAN + lRespCPDRAN;
	private static final int lRespCPDKVN = 1;
	public byte getRespCPDKVN(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespCPDKVN];		
	}
	
	private static final int pRespSIDSTAC = pRespCPDKVN + lRespCPDKVN;
	private static final int lRespSIDSTAC = 8;
	public byte[] getRespSIDSTAC(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespSIDSTAC, 
				pRespSIDSTAC + lRespSIDSTAC);		
	}
	///
	private static final int pRespTMSerialNumber = pRespSIDSTAC + lRespSIDSTAC;
	private static final int lRespTMSerialNumber = 6;
	public byte[] getRespTMSerialNumber(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespTMSerialNumber, 
				pRespTMSerialNumber + lRespTMSerialNumber);		
	}
	private static final int pRespLastCreditTxnLog = pRespTMSerialNumber + lRespTMSerialNumber;
	private static final int lRespLastCreditTxnLog = 33;
	public byte[] getRespLastCreditTxnLog(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespLastCreditTxnLog, 
				pRespLastCreditTxnLog + lRespLastCreditTxnLog);		
	}
	private static final int pRespSVCrypto = pRespLastCreditTxnLog + lRespLastCreditTxnLog;
	private static final int lRespSVCrypto = 16;
	public byte[] getRespSVCrypto(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespSVCrypto, 
				pRespSVCrypto + lRespSVCrypto);		
	}
	private static final int pRespMsgType = pRespSVCrypto + lRespSVCrypto;
	private static final int lRespMsgType = 1;
	public byte getRespMsgType(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespMsgType];		
	}
	private static final int pRespSubType = pRespMsgType + lRespMsgType;
	private static final int lRespSubType = 1;
	public byte getRespSubType(){
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespSubType];		
	}
	
	///
	private static final int pRespCardOneDayQuota = pRespSubType + lRespSubType;
	private static final int lRespCardOneDayQuota = 3;
	public byte[] getRespCardOneDayQuota(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespCardOneDayQuota, 
				pRespCardOneDayQuota + lRespCardOneDayQuota);		
	}
	
	private static final int pRespCardOneDayQuotaDate = pRespCardOneDayQuota + lRespCardOneDayQuota;
	private static final int lRespCardOneDayQuotaDate = 2;
	public byte[] getRespCardOneDayQuotaDate(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespCardOneDayQuotaDate, 
				pRespCardOneDayQuotaDate + lRespCardOneDayQuotaDate);		
	}
	
	private static final int pRespTSQN = pRespCardOneDayQuotaDate + lRespCardOneDayQuotaDate;
	private static final int lRespTSQN = 3;
	public byte[] getRespTSQN(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespTSQN, 
				pRespTSQN + lRespTSQN);		
	}
	
	private static final int pRespPurseBalance = pRespTSQN + lRespTSQN;
	private static final int lRespPurseBalance = 3;
	public byte[] getRespPurseBalance(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespPurseBalance, 
				pRespPurseBalance + lRespPurseBalance);		
	}
	
	private static final int pRespConfirmCode = pRespPurseBalance + lRespPurseBalance;
	private static final int lRespConfirmCode = 2;
	public byte[] getRespConfirmCode(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespConfirmCode, 
				pRespConfirmCode + lRespConfirmCode);		
	}
	
	///
	private static final int pRespSIGN = pRespConfirmCode + lRespConfirmCode;
	private static final int lRespSIGN = 16;
	public byte[] getRespSIGN(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespSIGN, 
				pRespSIGN + lRespSIGN);		
	}
	
	private static final int pRespSID = pRespSIGN + lRespSIGN;
	private static final int lRespSID = 8;
	public byte[] getRespSID(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespSID, 
				pRespSID + lRespSID);		
	}
	
	private static final int pRespMAC = pRespSID + lRespSID;
	private static final int lRespMAC = 18;
	public byte[] getRespMAC(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespMAC, 
				pRespMAC + lRespMAC);		
	}
	
	private static final int pRespTxnDateTime = pRespMAC + lRespMAC;
	private static final int lRespTxnDateTime = 4;
	public byte[] getRespTxnDateTime(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespTxnDateTime, 
				pRespTxnDateTime + lRespTxnDateTime);		
	}
	
	private static final int pRFU = pRespTxnDateTime + lRespTxnDateTime;
	private static final int lRFU = 19;
	public byte[] getRespRFU(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRFU, 
				pRFU + lRFU);		
	}
	
	//-------------------- Response --------------------
	@Override
	public byte[] GetRequest() {
		// TODO Auto-generated method stub
		
		mRequest[scReqLength - 1] = Req_EDC = getEDC(mRequest, scReqLength);
		logger.debug(PPR_AuthTxnReqOffline.class.getName()+" request:" + Util.hex2StringLog(mRequest));
		
		
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
			logger.debug(PPR_AuthTxnReqOffline.class.getName()+" recv:" + Util.hex2StringLog(mRespond));
	
			logger.debug("Purse Version No.:"+String.format("(%02X)", this.getRespPurseVersionNumber()));
			logger.debug("Purse Usgage Control:"+String.format("(%02X)", this.getRespPurseUsageControl()));
			logger.debug("Single AutoLoad Txn Amt:"+Util.hex2StringLog(this.getRespSingleAutoLoadTxnAmt()));
			logger.debug("PID:"+Util.hex2StringLog(this.getRespPID()));
			logger.debug("CPU Admin. Key KVN:"+String.format("(%02X)", this.getRespCPUAdminKeyKVN()));
			
			logger.debug("Credit Key KVN:"+String.format("(%02X)", this.getRespCreditKeyKVN()));
			logger.debug("Sign.key KVN:"+String.format("(%02X)", this.getRespSignKeyKVN()));
			logger.debug("CPU Issuer key KVN:"+String.format("(%02X)", this.getRespCPUIssuerKVN()));
			logger.debug("CTC:"+Util.hex2StringLog(this.getRespCTC()));
			logger.debug("TM:"+String.format("(%02X)", this.getRespTM()));
			
			logger.debug("TQ:"+String.format("(%02X)", this.getRespTQ()));
			logger.debug("Sub Area Code:"+Util.hex2StringLog(this.getRespSubAreaCode()));
			logger.debug("Purse Exp. Date:"+Util.hex2StringLog(this.getRespPurseExpDate()));
			logger.debug("Purse Balance Before Txn:"+Util.hex2StringLog(this.getRespPurseBalanceBeforeTxn()));
			logger.debug("Txn SN Before Txn:"+Util.hex2StringLog(this.getRespTxnSNBeforeTxn()));
			
			logger.debug("Card Type:"+String.format("(%02X)", this.getRespCardType()));
			logger.debug("Personal Profile:"+String.format("(%02X)", this.getRespPersonalProfile()));
			logger.debug("Profile Exp Date:"+Util.hex2StringLog(this.getRespProfileExpDate()));
			logger.debug("Area Code:"+String.format("(%02X)", this.getRespAreaCode()));
			logger.debug("Card Physical ID:"+Util.hex2StringLog(this.getRespCardPhysicalID()));
			
			logger.debug("Card Physical ID Len:"+String.format("(%02X)", this.getRespCardPhysicalIDLen()));
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
			logger.debug("CPD Read Flag:"+String.format("(%02X)", this.getRespCPDReadFlag()));
			logger.debug("CPD SAM ID:"+Util.hex2StringLog(this.getRespCPDSAMID()));
			
			logger.debug("CPD RAN:"+Util.hex2StringLog(this.getRespCPDRAN()));
			logger.debug("CPD KVN:"+String.format("(%02X)", this.getRespCPDKVN()));
			logger.debug("SID S-TAC:"+Util.hex2StringLog(this.getRespSIDSTAC()));
			logger.debug("TM Serial Number:"+Util.hex2StringLog(this.getRespTMSerialNumber()));
			logger.debug("Last Credit Txn Log:"+Util.hex2StringLog(this.getRespLastCreditTxnLog()));
			
			logger.debug("SVCrypto:"+Util.hex2StringLog(this.getRespSVCrypto()));
			logger.debug("Msg Type:"+String.format("(%02X)", this.getRespMsgType()));
			logger.debug("Sub Type:"+String.format("(%02X)", this.getRespSubType()));
			logger.debug("Card One Day Quota:"+Util.hex2StringLog(this.getRespCardOneDayQuota()));
			logger.debug("Card One Day Quota Date:"+Util.hex2StringLog(this.getRespCardOneDayQuotaDate()));
			
			logger.debug("TSQN:"+Util.hex2StringLog(this.getRespTSQN()));
			logger.debug("Purse Balance:"+Util.hex2StringLog(this.getRespPurseBalance()));
			logger.debug("Confirm Code:"+Util.hex2StringLog(this.getRespConfirmCode()));
			logger.debug("SIGN:"+Util.hex2StringLog(this.getRespSIGN()));
			logger.debug("SID:"+Util.hex2StringLog(this.getRespSID()));
			
			logger.debug("MAC:"+Util.hex2StringLog(this.getRespMAC()));
			logger.debug("Txn Date Time:"+Util.hex2StringLog(this.getRespTxnDateTime()));
			logger.debug("RFU:"+Util.hex2StringLog(this.getRespRFU()));
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
		logger.info("Start");
		
		if(this.checkResponseFormat(bytes, scRespDataLength) != true)
			return false;
		
		mRespond = Arrays.copyOf(bytes, bytes.length);
		
		
		logger.info("end");
		return true;
	}

}
