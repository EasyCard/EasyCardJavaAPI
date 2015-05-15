package Reader;

import java.util.Arrays;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import Utilities.Util;

public class PPR_AuthTxnOffline extends APDU{

	static Logger logger = Logger.getLogger(PPR_AuthTxnOffline.class);
	public static final String scDescription = "將0312驗證授權交易訊息傳入CPU卡/SAM卡中做認證";
	
	//private static PPR_SignOn sThis = null;
	
	private static final int scReqDataLength = 0x16;
	private static final int scReqLength = scReqDataLength + scReqMinLength;
	private static final int scReqInfoLength = scReqDataLength + scReqInfoMinLength;
	private static final int scRespDataLength = 0x40;
	private static final int scRespLength = scRespDataLength + scRespMinLength;
	
	
	
	private byte[] mRequest = new byte[scReqLength];
	private byte[] mRespond = null;
	

	
	
	public PPR_AuthTxnOffline(){
		
		Req_NAD = 0;
		Req_PCB = 0; 
		Req_LEN = (byte) scReqInfoLength;
		
		Req_CLA = (byte) 0x80;
		Req_INS = 0x32;			
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
	//Msg Type
	private static final int pReqHVCrypto = scReqDataOffset + 0;
	private static final int lReqHVCrypto = 16;
	public boolean setReqHVCrypto(byte[] b) {
		
		logger.info("setter:bcd2Ascii=>"+Util.bcd2Ascii(b));
		if (b == null || b.length != lReqHVCrypto) {
			logger.error("HVCrypto null or len wrong");
			return false;
		}
		
				
		System.arraycopy(b, 0, mRequest, pReqHVCrypto, lReqHVCrypto);
		
		return true;
	}
	
	
	private static final int pReqLCDControl = pReqHVCrypto + lReqHVCrypto;
	private static final int lReqLCDControl = 1;
	public boolean setReqLCDControl(byte flag) {		
		logger.info("setter:"+flag);
		mRequest[pReqLCDControl] = flag;
		return true;
	}
	
	private static final int pReqRFU = pReqLCDControl + lReqLCDControl;
	private static final int lReqRFU = 5;
	//-------------------- Request --------------------
	
	//++++++++++++++++++++ Response ++++++++++++++++++++
	private static final int pRespTSQN = scRespDataOffset + 0;
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
	
	private static final int pRespConfirmCode = pRespTSQN + lRespTSQN;
	private static final int lRespConfirmCode = 2;
	public byte[] getRespConfirmCode(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespConfirmCode, 
				pRespConfirmCode + lRespConfirmCode);		
	}
	
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
	
	private static final int pRespCardOneDayQuota = pRespTxnDateTime + lRespTxnDateTime;
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
	
	
	private static final int pRFU = pRespCardOneDayQuotaDate + lRespCardOneDayQuotaDate;
	private static final int lRFU = 5;
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
		logger.debug(PPR_AuthTxnOffline.class.getName()+" request:" + Util.hex2StringLog(mRequest));
		
		
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
			logger.debug(PPR_AuthTxnOffline.class.getName()+" recv:" + Util.hex2StringLog(mRespond));
	
			logger.debug("TSQN:"+Util.hex2StringLog(this.getRespTSQN()));
			logger.debug("Purse Balance:"+Util.hex2StringLog(this.getRespPurseBalance()));
			logger.debug("Confirm Code:"+Util.hex2StringLog(this.getRespConfirmCode()));

			logger.debug("SIGN:"+Util.hex2StringLog(this.getRespSIGN()));
			logger.debug("SID:"+Util.hex2StringLog(this.getRespSID()));
			logger.debug("MAC:"+Util.hex2StringLog(this.getRespMAC()));
			logger.debug("Txn Date Time:"+Util.hex2StringLog(this.getRespTxnDateTime()));
			
			logger.debug("Card One Day Quota:"+Util.hex2StringLog(this.getRespCardOneDayQuota()));
			logger.debug("Card One Day Quota Date:"+Util.hex2StringLog(this.getRespCardOneDayQuotaDate()));
			
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
