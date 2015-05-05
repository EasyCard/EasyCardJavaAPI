package Reader;

import java.util.Arrays;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import Utilities.Util;

public class PPR_TxnReqOffline extends APDU{

	static Logger logger = Logger.getLogger(PPR_TxnReqOffline.class);
	public static final String scDescription = "讀取驗證授權交易相關欄位";
	
	//private static PPR_SignOn sThis = null;
	
	private static final int scReqDataLength = 0x40;
	private static final int scReqLength = scReqDataLength + scReqMinLength;
	private static final int scReqInfoLength = scReqDataLength + scReqInfoMinLength;
	private static final int scRespDataLength = 0xFA;
	private static final int scRespLength = scRespDataLength + scRespMinLength;
	
	
	
	private byte[] mRequest = new byte[scReqLength];
	private byte[] mRespond = null;
	

	
	
	public PPR_TxnReqOffline(){
		
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
	//Request
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
	//////////////////// Request Finish //////////////////////////////
	
	
	
	@Override
	public byte[] GetRequest() {
		// TODO Auto-generated method stub
		
		mRequest[scReqLength - 1] = Req_EDC = getEDC(mRequest, scReqLength);
		logger.debug(PPR_TxnReqOffline.class.getName()+" request:" + Util.hex2StringLog(mRequest));
		
		
		return mRequest;
	}

	@Override
	public boolean SetRequestData(byte[] bytes) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int GetReqRespLength() {
		// TODO Auto-generated method stub
		return scRespLength;
	}

	@Override
	public void debugResponseData() {
		// TODO Auto-generated method stub
		if(mRespond != null)
			logger.debug(PPR_TxnReqOffline.class.getName()+" recv:" + Util.hex2StringLog(mRespond));
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
		int length = bytes.length;
		if (scRespLength != length) {
			// invalid respond format... 
			logger.error("RespLen wrong:"+scRespLength);
			return false;
		}
		
		if (bytes[2] != (byte) (scRespDataLength + 2)) { // Data + SW1 + SW2
			// invalid data format...
			logger.error("RespLen wrong:"+scRespLength);
			return false;
		}
		
		byte sum = getEDC(bytes, length);
		if (sum != bytes[scRespLength - 1]) {
			// check sum error...
			logger.error("CheckSum error");
			return false;
		}
		
		mRespond = Arrays.copyOf(bytes, length);
		
		int dataLength = mRespond[2] & 0x000000FF;
		Resp_SW1 = mRespond[scRespDataOffset + dataLength - 2];
		Resp_SW2 = mRespond[scRespDataOffset + dataLength + 1 - 2];
		
		logger.info("end");
		return true;
	}

}
