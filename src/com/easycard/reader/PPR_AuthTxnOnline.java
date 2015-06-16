package com.easycard.reader;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.TimeZone;

import org.apache.log4j.Logger;



import com.easycard.utilities.Util;

public class PPR_AuthTxnOnline extends APDU{

	static Logger logger = Logger.getLogger(PPR_AuthTxnOnline.class);
	public static final String scDescription = "將0110連線帳務交易訊息傳入CPU卡/SAM卡中做認證";
	
	//private static PPR_SignOn sThis = null;
	
	private static final int scReqDataLength = 0x28;
	private static final int scReqLength = scReqDataLength + scReqMinLength;
	private static final int scReqInfoLength = scReqDataLength + scReqInfoMinLength;
	private static final int scRespDataLength = 0x40;
	private static final int scRespLength = scRespDataLength + scRespMinLength;
	
	
	
	private byte[] mRequest = new byte[scReqLength];
	private byte[] mRespond = null;
	

	
	
	public PPR_AuthTxnOnline(){
		
		Req_NAD = 0;
		Req_PCB = 0; 
		Req_LEN = (byte) scReqInfoLength;
		
		Req_CLA = (byte) 0x80;
		Req_INS = 0x11;			
		Req_P1 = 0x02;
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
	private static final int pReqNewExpiryDate = scReqDataOffset + 0;
	private static final int lReqNewExpiryDate = 4;
	public boolean setReqNewExpiryDate(int unixTimeStamp, String timeZone) {
		
		logger.info("setter: intUnixTime("+unixTimeStamp+"),timeZone:"+timeZone);
		TimeZone tz = TimeZone.getTimeZone(timeZone);
		int offset = tz.getOffset(unixTimeStamp * 1000L);
		unixTimeStamp += offset / 1000;
		
		mRequest[pReqNewExpiryDate] = (byte) (unixTimeStamp & 0x000000FF);
		mRequest[pReqNewExpiryDate + 1] = (byte) ((unixTimeStamp & 0x0000FF00) >> 8);
		mRequest[pReqNewExpiryDate + 2] = (byte) ((unixTimeStamp & 0x00FF0000) >> 16);
		mRequest[pReqNewExpiryDate + 3] = (byte) ((unixTimeStamp & 0xFF000000) >> 24);
		
		return true;
	}
	
	
	private static final int pReqNewPersonalExpiryDate1 = pReqNewExpiryDate + lReqNewExpiryDate;
	private static final int lReqNewPersonalExpiryDate1 = 4;
	public boolean setReqNewPersonalExpiryDate1(int unixTimeStamp, String timeZone) {
		
		logger.info("setter: intUnixTime("+unixTimeStamp+"),timeZone:"+timeZone);
		TimeZone tz = TimeZone.getTimeZone(timeZone);
		int offset = tz.getOffset(unixTimeStamp * 1000L);
		unixTimeStamp += offset / 1000;
		
		mRequest[pReqNewPersonalExpiryDate1] = (byte) (unixTimeStamp & 0x000000FF);
		mRequest[pReqNewPersonalExpiryDate1 + 1] = (byte) ((unixTimeStamp & 0x0000FF00) >> 8);
		mRequest[pReqNewPersonalExpiryDate1 + 2] = (byte) ((unixTimeStamp & 0x00FF0000) >> 16);
		mRequest[pReqNewPersonalExpiryDate1 + 3] = (byte) ((unixTimeStamp & 0xFF000000) >> 24);
		
		return true;
	}
	
	private static final int pReqNewPersonalExpiryDate2 = pReqNewPersonalExpiryDate1 + lReqNewPersonalExpiryDate1;
	private static final int lReqNewPersonalExpiryDate2 = 2;
	public boolean setReqNewPersonalExpiryDate2(int unixTimeStamp, String timeZone) {
		return false;
		
		/*
		logger.info("setter: intUnixTime("+unixTimeStamp+"),timeZone:"+timeZone);
		TimeZone tz = TimeZone.getTimeZone(timeZone);
		int offset = tz.getOffset(unixTimeStamp * 1000L);
		unixTimeStamp += offset / 1000;
		
		mRequest[pReqNewPersonalExpiryDate1] = (byte) (unixTimeStamp & 0x000000FF);
		mRequest[pReqNewPersonalExpiryDate1 + 1] = (byte) ((unixTimeStamp & 0x0000FF00) >> 8);
		mRequest[pReqNewPersonalExpiryDate1 + 2] = (byte) ((unixTimeStamp & 0x00FF0000) >> 16);
		mRequest[pReqNewPersonalExpiryDate1 + 3] = (byte) ((unixTimeStamp & 0xFF000000) >> 24);
		*/
	}
	
	private static final int pReqTxnToken = pReqNewPersonalExpiryDate2 + lReqNewPersonalExpiryDate2;
	private static final int lReqTxnToken = 16;
	public boolean setReqTxnToken(byte[] txnToken) {
		
		if(txnToken == null){
			logger.error("txnToken NULL");
			return false;			
		}
		
		if(txnToken.length != lReqTxnToken){
			logger.error("txnToken Len not equal 32");
			return false;			
		}
		
		System.arraycopy(txnToken, 0, mRequest, pReqTxnToken, lReqTxnToken);
		return true;
	}
	
	private static final int pReqHTAC = pReqTxnToken + lReqTxnToken;
	private static final int lReqHTAC = 8;
	public boolean setReqHTAC(byte[] htac) {
		
		if(htac == null){
			logger.error("htac NULL");
			return false;			
		}
		
		if(htac.length != lReqHTAC){
			logger.error("htac Len not equal 8");
			return false;			
		}
		
		System.arraycopy(htac, 0, mRequest, pReqHTAC, lReqHTAC);
		return true;
	}
	
	
	private static final int pReqLCDControl = pReqHTAC + lReqHTAC;
	private static final int lReqLCDControl = 1;
	public boolean setReqLCDControl(byte flag) {		
		logger.info("setter:"+flag);
		mRequest[pReqLCDControl] = flag;
		return true;
	}
	
	private static final int pReqRFU = pReqLCDControl + lReqLCDControl;
	//-------------------- Request --------------------
	
	//++++++++++++++++++++ Response ++++++++++++++++++++
	private ResponseField respFld = null;
	private class ResponseField extends BaseResponseAutoParser{
		private int dataBodyLen;
		private final int NORMAL_LEN = 64 + 2;
		private final int ERROR1_LEN = 2;
		
		public byte[] TSQN=null;
		public byte[] purseBalance=null;
		public byte[] confirmCode=null;
		public byte[] SIGN=null;
		public byte[] SID=null;
		
		public byte[] MAC=null;
		public byte[] txnDateTime=null;		
		public byte[] statusCode=null;
		
		public ResponseField(int len){
			this.dataBodyLen = len;
		}
		
		public byte[] getTSQN() {
			return TSQN;
		}

		public byte[] getPurseBalance() {
			return purseBalance;
		}

		public byte[] getConfirmCode() {
			return confirmCode;
		}

		public byte[] getSIGN() {
			return SIGN;
		}

		public byte[] getSID() {
			return SID;
		}

		public byte[] getMAC() {
			return MAC;
		}

		public byte[] getTxnDateTime() {
			return txnDateTime;
		}

		@Override
		protected LinkedHashMap<String, Integer> getFields() {
			// TODO Auto-generated method stub
			LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
			
			if(this.dataBodyLen == NORMAL_LEN){
				map.put("TSQN", 3);
				map.put("purseBalance",3);
				map.put("confirmCode",2);
				map.put("SIGN",16);
				map.put("SID",8);
				
				map.put("MAC",18);
				map.put("txnDateTime",4);
				//map.put("RFU",10);
				//map.put("statusCode",2);
			} else if(this.dataBodyLen == ERROR1_LEN){
				map.put("statusCode",2);
			} else {
				logger.error("Unknowen dataBody Len:"+dataBodyLen);
				return null;
			}
			return map;
			
		}
		
		
		
	}
	
	public byte[] getRespTSQN(){
		if (respFld == null || respFld.getTSQN() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getTSQN()));
		return Arrays.copyOfRange(respFld.getTSQN(), 0, 
				respFld.getTSQN().length);
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
	
	public byte[] getRespConfirmCode(){
		if (respFld == null || respFld.getConfirmCode() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getConfirmCode()));
		return Arrays.copyOfRange(respFld.getConfirmCode(), 0, 
				respFld.getConfirmCode().length);			
	}
	
	public byte[] getRespSIGN(){
		if (respFld == null || respFld.getSIGN() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getSIGN()));
		return Arrays.copyOfRange(respFld.getSIGN(), 0, 
				respFld.getSIGN().length);			
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
	
	public byte[] getRespMAC(){
		if (respFld == null || respFld.getMAC() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getMAC()));
		return Arrays.copyOfRange(respFld.getMAC(), 0, 
				respFld.getMAC().length);		
	}
	
	public byte[] getRespTxnDateTime(){
		if (respFld == null || respFld.getTxnDateTime() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getTxnDateTime()));
		return Arrays.copyOfRange(respFld.getTxnDateTime(), 0, 
				respFld.getTxnDateTime().length);		
	}
	
	
	//-------------------- Response --------------------
	@Override
	public byte[] GetRequest() {
		// TODO Auto-generated method stub
		
		mRequest[scReqLength - 1] = Req_EDC = getEDC(mRequest, scReqLength);
		logger.debug(PPR_AuthTxnOnline.class.getName()+" request:" + Util.hex2StringLog(mRequest));
		
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
			logger.debug(PPR_AuthTxnOnline.class.getName()+" recv:" + Util.hex2StringLog(mRespond));
	
			logger.debug("TSQN:"+Util.hex2StringLog(this.getRespTSQN()));
			logger.debug("Purse Balance:"+Util.hex2StringLog(this.getRespPurseBalance()));
			logger.debug("Confirm Code:"+Util.hex2StringLog(this.getRespConfirmCode()));

			logger.debug("SIGN:"+Util.hex2StringLog(this.getRespSIGN()));
			logger.debug("SID:"+Util.hex2StringLog(this.getRespSID()));
			logger.debug("MAC:"+Util.hex2StringLog(this.getRespMAC()));
			logger.debug("Txn Date Time:"+Util.hex2StringLog(this.getRespTxnDateTime()));
			
			
			
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
			
			
			//Success Response
			respFld = new ResponseField(dataLength);
			respFld.parse(b);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}		
		return true;
	}

}
