package Reader;

import java.util.Arrays;

import org.apache.log4j.Logger;

import Utilities.Util;

public class PPR_SignOnQuery extends APDU{

	static Logger logger = Logger.getLogger(PPR_SignOnQuery.class);
	public static final String scDescription = "查詢端末開機是否已認證成功";
	
	//private static PPR_SignOn sThis = null;
	
	private static final int scReqDataLength = 0;
	private static final int scReqLength = scReqDataLength + scReqMinLength -1;
	private static final int scReqInfoLength = scReqDataLength + scReqInfoMinLength;
	private static final int scRespDataLength = 40;
	private static final int scRespLength = scRespDataLength + scRespMinLength;
	
	
	
	private byte[] mRequest = new byte[scReqLength];
	private byte[] mRespond = null;
	

	
	
	public PPR_SignOnQuery(){
		
		Req_NAD = 0;
		Req_PCB = 0; 
		Req_LEN = (byte) ((byte) this.scReqInfoMinLength -1);
		
		Req_CLA = (byte) 0x80;
		Req_INS = 0x03;			
		Req_P1 = 0x00;
		Req_P2 = 0x00;
		
		
		Req_Le = (byte) scRespDataLength;
		
		mRequest[0] = Req_NAD;
		mRequest[1] = Req_PCB;
		mRequest[2] = Req_LEN;
		mRequest[3] = Req_CLA;
		mRequest[4] = Req_INS;
		mRequest[5] = Req_P1;
		mRequest[6] = Req_P2;
		mRequest[7] = Req_Le;	
	}
	private static final byte pRespAuthCreditLimit = scRespDataOffset + 0;
	private static final byte lRespAuthCreditLimit = 3;
	public byte[] getRespAuthCreditLimit(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespAuthCreditLimit, 
				pRespAuthCreditLimit + lRespAuthCreditLimit);		
	}
	
	private static final byte pRespAuthCreditBalance = pRespAuthCreditLimit + lRespAuthCreditLimit;
	private static final byte lRespAuthCreditBalance = 3;
	public byte[] getRespAuthCreditBalance(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespAuthCreditBalance, 
				pRespAuthCreditBalance + lRespAuthCreditBalance);		
	}
	
	private static final byte pRespAuthCreditCumulative = pRespAuthCreditBalance + lRespAuthCreditBalance;
	private static final byte lRespAuthCreditCumulative = 3;
	public byte[] getRespAuthCreditCumulative(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespAuthCreditCumulative, 
				pRespAuthCreditCumulative + lRespAuthCreditCumulative);		
	}
	
	private static final byte pRespAuthCancelCreditCumulative = pRespAuthCreditCumulative + lRespAuthCreditCumulative;
	private static final byte lRespAuthCancelCreditCumulative = 3;
	public byte[] getlRespAuthCreditCumulative(){
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespAuthCancelCreditCumulative, 
				pRespAuthCancelCreditCumulative + lRespAuthCancelCreditCumulative);		
	}
	

	/* 
	 * PPR_SignOn參數設定, 1 byte, Host, 適用於有SignOn之設備
	 * CPD Read Flag: Bit 0~1, 二代CPD讀取及驗證設定, T4824
	 * One Day Quota Write For Micro Payment: Bit 2~3, 小額消費日限額寫入, T4823
	 * SAM SignOnControl Flag: Bit 4~5, SAM卡SignOn控制旗標, T5369
	 * Check EV Flag For Mifare Only: Bit 6, 檢查餘額旗標
	 * Merchant Limit Use For Micro Payment: Bit 7, 小額消費通路限制使用旗標 
	 */
	private static final byte pRespSignOnParams1 = pRespAuthCancelCreditCumulative + lRespAuthCancelCreditCumulative;
	private static final byte lRespSignOnParams1 = 1;	
	/* CPD Read Flag: Bit 0~1, 二代CPD讀取及驗證設定, T4824 */
	public byte getRespCPDReadFlag() {
		if (mRespond == null) {
			//return CPDReadFlag.NRHostNCReader;
			return 0x00;
		}
		
		byte b = (byte) (mRespond[pRespSignOnParams1] & 0x03);

		logger.info("getter:"+b);
		return b;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, One Day Quota Write For Micro Payment: Bit 2~3, 小額消費日限額寫入 */
	//public OneDayQuotaWriteForMicroPayment GetResp_OneDayQuotaWriteForMicroPayment() {
	public byte getRespOneDayQuotaWriteForMicroPayment() {
		if (mRespond == null) {
			return 0x00;
		}  
		byte b = (byte) ((mRespond[pRespSignOnParams1] & 0x0C) >> 2);
		logger.info("getter:"+b);
		return b;
		
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, SAM SignOnControl Flag: Bit 4~5, SAM卡SignOn控制旗標 */
	//public SAMSignOnControlFlag GetResp_SAMSignOnControlFlag() {
	public byte getRespSAMSignOnControlFlag() {
		if (mRespond == null) {
			return 0x00;//SAMSignOnControlFlag.NoSignOnForBoth;
		}
		byte b = (byte) ((mRespond[pRespSignOnParams1] & 0x30) >> 4);
		logger.info("getter:"+b);
		return b;
		
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Check EV Flag For Mifare Only: Bit 6, 檢查餘額旗標 */
	public byte getRespCheckEVFlagForMifareOnly() {
		if (mRespond == null) {
			return 0x00;
		}
		byte b = (byte) ((mRespond[pRespSignOnParams1] & 0x40) >> 6);
		
		return b;
		
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Merchant Limit Use For Micro Payment: Bit 7, 小額消費通路限制使用旗標 */
	public byte getRespMerchantLimitUseForMicroPayment() {
		if (mRespond == null) {
			return 0x00;
		}
		byte b = (byte) ((mRespond[pRespSignOnParams1] & 0x80) >> 7);
		return b;
	}
	
	private static final byte pRespSignOnParams2 = pRespSignOnParams1 + lRespSignOnParams1;
	private static final byte lRespSignOnParams2 = 1;
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, One Day Quota Flag For Micro Payment: Bit 0~1, 小額消費日限額旗標 */
	//public OneDayQuotaFlagForMicroPayment GetResp_OneDayQuotaFlagForMicroPayment() {
	public byte getRespOneDayQuotaFlagForMicroPayment() {
		if (mRespond == null) {
			return 0x00;//OneDayQuotaFlagForMicroPayment.NCNA;
		}  
		byte b = (byte) (mRespond[pRespSignOnParams2] & 0x03);
		logger.info("getter:"+b);
		return b;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Once Quota Flag For Micro Payment: Bit 2, 小額消費次限額旗標 */
	public byte getRespOnceQuotaFlagForMicroPayment() {
		if (mRespond == null) {
			return 0x00;
		}  
				
		byte b = (byte) ((mRespond[pRespSignOnParams2] & 0x04) >> 2);
		
		logger.debug("getter:orig:"+String.format("%02X", mRespond[pRespSignOnParams2])+",bit:"+String.format("%02X", b));
		return b;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Check Debit Flag: Bit 3, 扣值交易合法驗證旗標 */
	public byte getRespCheckDebitFlag() {
		if (mRespond == null) {
			return 0x00;
		}  
		byte b = (byte) ((mRespond[pRespSignOnParams2] & 0x08) >> 3);
		return b;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Mifare Check Enable Flag: Bit 4, 二代卡Level 1 */
	public byte getRespMifareCheckEnableFlag() {
		if (mRespond == null) {
			return 0x00;
		}  
		byte b = (byte) ((mRespond[pRespSignOnParams2] & 0x10) >> 4);
		return b;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Pay On Behalf Flag: Bit 5, 是否允許代墊 */
	public boolean getRespPayOnBehalfFlag() {
		if (mRespond == null) {
			return false;
		}  
		byte b = (byte) (mRespond[pRespSignOnParams2] & 0x20);
		if (b == 0) {
			return false; // 不允許代墊
		} else {
			return true; // 允許代墊一次
		}
	}
	
	
	/* One Day Quota For Micro Payment, 2 bytes, Reader (適用於有SignOn之設備), 小額消費日限額額度, Unsigned and LSB First */
	private static final byte pRespOneDayQuotaForMicroPayment = pRespSignOnParams2 + lRespSignOnParams2;
	private static final byte lRespOneDayQuotaForMicroPayment = 2;
	public byte[] getRespOneDayQuotaForMicroPayment() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespOneDayQuotaForMicroPayment, 
				pRespOneDayQuotaForMicroPayment + lRespOneDayQuotaForMicroPayment);
	}
	
	/* Once Quota For Micro Payment, 2 bytes, Reader (適用於有SignOn之設備), 小額消費次限額額度, Unsigned and LSB First */
	private static final byte pRespDataOnceQuotaForMicroPayment = pRespOneDayQuotaForMicroPayment + lRespOneDayQuotaForMicroPayment;
	private static final byte lRespDataOnceQuotaForMicroPayment = 2;
	public byte[] getRespOnceQuotaForMicroPayment() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespDataOnceQuotaForMicroPayment, 
				pRespDataOnceQuotaForMicroPayment + lRespDataOnceQuotaForMicroPayment);
	}
	
	/* Check Debit Value, 2 bytes, Reader (適用於有SignOn之設備), 扣值交易合法驗證金額, Unsigned and LSB First */
	private static final byte pRespDataCheckDebitValue = pRespDataOnceQuotaForMicroPayment + lRespDataOnceQuotaForMicroPayment;
	private static final byte lRespDataCheckDebitValue = 2;
	public byte[] getRespCheckDebitValue() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespDataCheckDebitValue, 
				pRespDataCheckDebitValue + lRespDataCheckDebitValue);
	}
	
	/* Add Quota Flag, 1 byte, Reader (適用於舊的額度控管), 加值額度控管旗標 */
	private static final byte pRespAddQuotaFlag = pRespDataCheckDebitValue + lRespDataCheckDebitValue;
	private static final byte lRespAddQuotaFlag = 1;
	public byte getRespAddQuotaFlag() {
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespAddQuotaFlag];
	}
	
	/* Add Quota, 3 bytes, Reader (適用於舊的額度控管), 加值額度, Unsigned and LSB First */
	private static final byte pRespAddQuota = pRespAddQuotaFlag + lRespAddQuotaFlag;
	private static final byte lRespAddQuota = 3;
	public byte[] getRespAddQuota() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespAddQuota, 
				pRespAddQuota + lRespAddQuota);
	}
	
	private static final byte pRespLastTXNDateTime = pRespAddQuotaFlag + lRespAddQuotaFlag;
	private static final byte lRespLastTXNDateTime = 4;
	public byte[] getRespLastTXNDateTime() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespLastTXNDateTime, 
				pRespLastTXNDateTime + lRespLastTXNDateTime);
	}
	
	private static final byte pRespServiceProviderID = pRespLastTXNDateTime + lRespLastTXNDateTime;
	private static final byte lRespServiceProviderID = 1;
	public byte getRespServiceProviderID() {
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[pRespAddQuotaFlag];
	}
	
	private static final byte pRespNewServiceProviderID = pRespServiceProviderID + lRespServiceProviderID;
	private static final byte lRespNewServiceProviderID = 3;
	public byte[] getRespNewServiceProviderID() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespNewServiceProviderID, 
				pRespNewServiceProviderID + lRespNewServiceProviderID);
	}
	
	private static final byte pRespRFU = pRespNewServiceProviderID + lRespNewServiceProviderID;
	private static final byte lRespRFU = 2;
	
	private static final byte pRespTheRemainderOfAddQuota = pRespRFU + lRespRFU;
	private static final byte lRespTheRemainderOfAddQuota = 3;
	public byte[] getRespTheRemainderOfAddQuota() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespTheRemainderOfAddQuota, 
				pRespTheRemainderOfAddQuota + lRespTheRemainderOfAddQuota);
	}
	
	private static final byte pRespCancelCreditQuota = pRespTheRemainderOfAddQuota + lRespTheRemainderOfAddQuota;
	private static final byte lRespCancelCreditQuota = 3;
	public byte[] getRespCancelCreditQuota() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, pRespCancelCreditQuota, 
				pRespCancelCreditQuota + lRespCancelCreditQuota);
	}
	
	@Override
	public byte[] GetRequest() {
		// TODO Auto-generated method stub
		
		mRequest[scReqLength - 1] = Req_EDC = getEDC(mRequest, scReqLength);
		logger.debug(PPR_SignOnQuery.class.getName()+" request:" + Util.hex2StringLog(mRequest));
		
		
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
			logger.debug(PPR_SignOnQuery.class.getName()+" recv:" + Util.hex2StringLog(mRespond));
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
