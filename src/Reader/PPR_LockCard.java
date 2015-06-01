package Reader;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.TimeZone;

import org.apache.log4j.Logger;



import Utilities.Util;

public class PPR_LockCard extends APDU{

	static Logger logger = Logger.getLogger(PPR_LockCard.class);
	public static final String scDescription = "當發現為禁用名單票卡，需將票卡鎖卡";
	
	//private static PPR_SignOn sThis = null;
	
	private static final int scReqDataLength = 0x0E; //14 bytes
	private static final int scReqLength = scReqDataLength + scReqMinLength;
	private static final int scReqInfoLength = scReqDataLength + scReqInfoMinLength;
	private static final int scRespDataLength = 0x28; //40 bytes
	
	private static final int scRespLength = scRespDataLength + scRespMinLength;
	
	
	
	private byte[] mRequest = new byte[scReqLength];
	private byte[] mRespond = null;
	

	
	
	public PPR_LockCard(){
		
		Req_NAD = 0;
		Req_PCB = 0; 
		Req_LEN = (byte) scReqInfoLength;
		
		Req_CLA = (byte) 0x80;
		Req_INS = 0x41;			
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
	private static final int pMsgType = scReqDataOffset + 0;
	private static final int lMsgType = 1;
	public boolean setReqMsgType(byte msgType) {		
		logger.info("setter:"+String.format("%02X", msgType));		
		mRequest[pMsgType] = msgType;
		return true;
	}
	
	public byte getReqMsgType() {		
		logger.info("getter:"+mRequest[pMsgType]);
		return mRequest[pMsgType];
	}
	
	private static final int pSubType = pMsgType + lMsgType;
	private static final int lSubType = 1;
	public boolean setReqSubType(byte subType) {		
		logger.info("setter:"+String.format("%02X", subType));		
		mRequest[pSubType] = subType;
		return true;
	}
	
	public byte getReqSubType() {		
		logger.info("getter:"+mRequest[pSubType]);
		return mRequest[pSubType];
	}
	
	private static final int pCardPhysicalID = pSubType + lSubType;
	private static final int lCardPhysicalID = 7;
	public boolean setReqCardPhysicalID(byte[] cardID) {	
		if(cardID.length != lCardPhysicalID) return false;
		logger.info("setter:"+Util.hex2StringLog(cardID));
		
		System.arraycopy(cardID, 0, mRequest, pCardPhysicalID, lCardPhysicalID);
		
		return true;
	}
	
	public byte[] getReqCardPhysicalID() {		
		byte[] cardID = Arrays.copyOfRange(mRequest, pCardPhysicalID, lCardPhysicalID);
		
		logger.info("getter:"+Util.hex2StringLog(cardID));
		return cardID;
	}
	
	/* TxN Date Time, 4 bytes, TM, 交易日期, Unsigned and LSB First (UnixDateTime) */
	private static final int pReqTxnDateTime = pCardPhysicalID + lCardPhysicalID;
	private static final int lReqTxnDateTime = 4;
	public void setReqTxNDateTime(int unixTimeStamp, String timeZone) {
		
		logger.info("setter: intUnixTime("+unixTimeStamp+"),timeZone:"+timeZone);
		TimeZone tz = TimeZone.getTimeZone(timeZone);
		int offset = tz.getOffset(unixTimeStamp * 1000L);
		unixTimeStamp += offset / 1000;
		
		mRequest[pReqTxnDateTime] = (byte) (unixTimeStamp & 0x000000FF);
		mRequest[pReqTxnDateTime + 1] = (byte) ((unixTimeStamp & 0x0000FF00) >> 8);
		mRequest[pReqTxnDateTime + 2] = (byte) ((unixTimeStamp & 0x00FF0000) >> 16);
		mRequest[pReqTxnDateTime + 3] = (byte) ((unixTimeStamp & 0xFF000000) >> 24);
				
	}
	
	private static final int pBlockingReason = pReqTxnDateTime + lReqTxnDateTime;
	private static final int lBlockingReason = 1;
	public boolean setReqBlockingReason(byte blockingReason) {		
		logger.info("setter:"+String.format("%02X", blockingReason));		
		mRequest[pBlockingReason] = blockingReason;
		return true;
	}
	
	public byte getReqBlockingReason() {		
		logger.info("getter:"+mRequest[pBlockingReason]);
		return mRequest[pBlockingReason];
	}
	//-------------------- Request --------------------
	
	//++++++++++++++++++++ Response ++++++++++++++++++++
	private ResponseField respFld = null;
	
	private class ResponseField extends BaseResponseAutoParser{
		private int dataBodyLen;
		private final int NORMAL_LEN = 0x28 + 2;//9000 
		
		private final int ERROR1_LEN = 2;//
		
		
		
		public byte purseVersionNumber;
		public byte[] PID=null;
		public byte[] CTC=null;
		public byte cardType;
		public byte personalProfile;	
		
		public byte[] cardPhysicalID=null;		
		public byte cardPhysicalIDLength;		
		public byte[] deviceID=null;		
		public byte[] newDeviceID=null;		
		public byte serviceProviderID;
		
		public byte[] newServiceProviderID=null;
		public byte locationID;
		public byte[] newLocationID=null;
		public byte issuerCode;
		
		public byte[] statusCode=null;
		
		//constructor
		public ResponseField(int len){
			dataBodyLen = len;
		}
		public int getDataBodyLen() {
			return dataBodyLen;
		}
		public byte getPurseVersionNumber() {
			return purseVersionNumber;
		}
		public byte[] getPID() {
			return PID;
		}
		public byte[] getCTC() {
			return CTC;
		}
		public byte getCardType() {
			return cardType;
		}
		public byte getPersonalProfile() {
			return personalProfile;
		}
		public byte[] getCardPhysicalID() {
			return cardPhysicalID;
		}
		public byte getCardPhysicalIDLength() {
			return cardPhysicalIDLength;
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
		public byte getIssuerCode() {
			return issuerCode;
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
				maps.put("PID", 8);
				maps.put("CTC", 3);
				maps.put("cardType", 1);
				maps.put("personalProfile", 1);	
				
				maps.put("cardPhysicalID", 7);		
				maps.put("cardPhysicalIDLength", 1);		
				maps.put("deviceID", 4);		
				maps.put("newDeviceID", 6);		
				maps.put("serviceProviderID", 1);
				
				maps.put("newServiceProviderID", 3);
				maps.put("locationID", 1);
				maps.put("newLocationID", 2);
				maps.put("issuerCode", 1);
				
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
	
	public byte[] getRespPID(){
		if (respFld == null || respFld.getPID() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}
		logger.info("getter:"+Util.hex2StringLog(respFld.getPID()));
		return Arrays.copyOfRange(respFld.getPID(), 0, 
				respFld.getPID().length);			
	}
		
		
	public byte[] getRespCTC(){
		if (respFld == null || respFld.getCTC() == null){ 
			logger.error("respFld or getter was null");
			return null;
		}		
		logger.info("getter:"+String.format("%02X", respFld.getCTC()));
		return respFld.getCTC();
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
	
	public byte getRespIssuerCode(){
		if (respFld == null) {
			logger.error("respFld was null");
			return 0x00;
		}
		logger.info("getter:"+String.format("%02X", respFld.getIssuerCode()));
		return respFld.getIssuerCode();
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
	
	@Override
	public byte[] GetRequest() {
		// TODO Auto-generated method stub
		
		mRequest[scReqLength - 1] = Req_EDC = getEDC(mRequest, scReqLength);
		logger.debug(PPR_LockCard.class.getName()+" request:" + Util.hex2StringLog(mRequest));
		
		
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
			logger.debug(PPR_LockCard.class.getName()+" recv:" + Util.hex2StringLog(mRespond));
	
			logger.debug("Purse Version No.:"+String.format("(%02X)", this.getRespPurseVersionNumber()));
			
			logger.debug("PID:"+Util.hex2StringLog(this.getRespPID()));
			logger.debug("CTC:"+String.format("(%02X)", this.getRespCTC()));
			
			
			logger.debug("Card Type:"+String.format("(%02X)", this.getRespCardType()));
			logger.debug("Personal Profile:"+String.format("(%02X)", this.getRespPersonalProfile()));
			
			logger.debug("Card Physical ID:"+Util.hex2StringLog(this.getRespCardPhysicalID()));
			
			logger.debug("Card Physical ID Len:"+String.format("(%02X)", this.getRespCardPhysicalIDLength()));
			
			
			logger.debug("Device ID:"+Util.hex2StringLog(this.getRespDeviceID()));
			logger.debug("New Device ID:"+Util.hex2StringLog(this.getRespNewDeviceID()));
			logger.debug("Service Provider ID:"+String.format("(%02X)", this.getRespServiceProviderID()));
			logger.debug("New Service Provider ID:"+Util.hex2StringLog(this.getRespNewServiceProviderID()));

			logger.debug("Location ID:"+String.format("(%02X)", this.getRespLocationID()));
			logger.debug("New Location ID:"+Util.hex2StringLog(this.getRespNewLocationID()));
			
			logger.debug("Issuer Code:"+String.format("(%02X)", this.getRespIssuerCode()));
	
						
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
			
			
			//Success Response
			respFld = new ResponseField(dataLength);
			respFld.parse(b);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		

		logger.info("end");			
		return true;
	}

}
