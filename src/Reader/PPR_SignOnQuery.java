package Reader;

import java.util.Arrays;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;



import Utilities.Util;

public class PPR_SignOnQuery extends APDU{

	static Logger logger = Logger.getLogger(PPR_SignOnQuery.class);
	public static final String scDescription = "查詢端末開機是否已認證成功";
	
	//private static PPR_SignOn sThis = null;
	
	private static final int scReqDataLength = 0;
	private static final int scReqLength = scReqDataLength + scReqMinLength -1;
	private static final int scRespDataLength = 40;
	private static final int scRespLength = scRespDataLength + scRespMinLength;
	
	
	
	private byte[] mRequest = new byte[scReqLength];
	private byte[] mRespond = null;
	

	//=========== Response ================
	private ResponseField respFld = null;
	private class ResponseField extends BaseResponseAutoParser{
		public byte[] authCreditLimit = null;
		public byte[] authCreditBalance = null;
		public byte[] authCreditCumulative = null;
		public byte[] authCancelCreditCumulative = null;
		public byte param1;
		public byte param2;
		public byte[] oneDayQuotaForMicroPayment = null;
		public byte[] onceQuotaForMicroPayment = null;
		public byte[] checkDevitValue = null;
		public byte addQuotaFlag;
		public byte[] addQuota = null;
		public byte[] lastTxnDateTime = null;
		public byte serviceProviderID;
		public byte[] newServiceProviderID = null;
		public byte[] RFU = null;
		
		public byte[] theRemaindeOfAddQuota = null;
		public byte[] CancelCreditQuota = null;
		
		
		public byte[] getAuthCreditLimit() {
			return authCreditLimit;
		}
		public byte[] getAuthCreditBalance() {
			return authCreditBalance;
		}
		public byte[] getAuthCreditCumulative() {
			return authCreditCumulative;
		}
		public byte[] getAuthCancelCreditCumulative() {
			return authCancelCreditCumulative;
		}
		public byte getParam1() {
			return param1;
		}
		public byte getParam2() {
			return param2;
		}
		public byte[] getOneDayQuotaForMicroPayment() {
			return oneDayQuotaForMicroPayment;
		}
		public byte[] getOnceQuotaForMicroPayment() {
			return onceQuotaForMicroPayment;
		}
		public byte[] getCheckDevitValue() {
			return checkDevitValue;
		}
		public byte getAddQuotaFlag() {
			return addQuotaFlag;
		}
		public byte[] getAddQuota() {
			return addQuota;
		}
		public byte[] getLastTxnDateTime() {
			return lastTxnDateTime;
		}
		public byte getServiceProviderID() {
			return serviceProviderID;
		}
		public byte[] getNewServiceProviderID() {
			return newServiceProviderID;
		}

		public byte[] getTheRemaindeOfAddQuota() {
			return theRemaindeOfAddQuota;
		}
		public byte[] getCancelCreditQuota() {
			return CancelCreditQuota;
		}
		
		@Override
		protected LinkedHashMap<String, Integer> getFields() {
			// TODO Auto-generated method stub
			LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
			
			map.put("authCreditLimit", 3);			
			map.put("authCreditBalance", 3);
			map.put("authCreditCumulative", 3);
			map.put("authCancelCreditCumulative", 3);
			map.put("param1", 1);
			map.put("param2", 1);
			map.put("oneDayQuotaForMicroPayment", 2);
			map.put("onceQuotaForMicroPayment", 2);
			map.put("checkDevitValue", 2);					
			map.put("addQuotaFlag", 1);
			map.put("addQuota", 3);
			map.put("lastTxnDateTime", 4);
			map.put("serviceProviderID", 1);
			map.put("newServiceProviderID", 3);
			map.put("RFU", 2);
			
			map.put("theRemaindeOfAddQuota", 3);
			map.put("CancelCreditQuota", 3);
			
			
			return map;
		}
	}
	

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
	
	public byte[] getRespAuthCreditLimit(){
		if (respFld == null || respFld.getAuthCreditLimit() == null) {
			return null;
		}
		return Arrays.copyOfRange(respFld.getAuthCreditLimit(), 0, 
				respFld.getAuthCreditLimit().length);		
	}
	
	public byte[] getRespAuthCreditBalance(){
		if(respFld == null || respFld.getAuthCreditBalance() == null) {
			return null;
		}
		return Arrays.copyOfRange(respFld.getAuthCreditBalance(), 0, 
				respFld.getAuthCreditBalance().length);		
	}
	
	public byte[] getRespAuthCreditCumulative(){
		if (respFld == null || respFld.getAuthCreditCumulative() == null) {
			return null;
		}
		return Arrays.copyOfRange(respFld.getAuthCreditCumulative(), 0, 
				respFld.getAuthCreditCumulative().length);		
	}
	
	public byte[] getRespAuthCancelCreditCumulative(){
		if (respFld == null || respFld.getAuthCancelCreditCumulative() == null) {
			return null;
		}
		return Arrays.copyOfRange(respFld.getAuthCancelCreditCumulative(), 0, 
				respFld.getAuthCancelCreditCumulative().length);		
	}
	

	/* 
	 * PPR_SignOn參數設定, 1 byte, Host, 適用於有SignOn之設備
	 * CPD Read Flag: Bit 0~1, 二代CPD讀取及驗證設定, T4824
	 * One Day Quota Write For Micro Payment: Bit 2~3, 小額消費日限額寫入, T4823
	 * SAM SignOnControl Flag: Bit 4~5, SAM卡SignOn控制旗標, T5369
	 * Check EV Flag For Mifare Only: Bit 6, 檢查餘額旗標
	 * Merchant Limit Use For Micro Payment: Bit 7, 小額消費通路限制使用旗標 
	 */
	
	/* CPD Read Flag: Bit 0~1, 二代CPD讀取及驗證設定, T4824 */
	public byte getRespCPDReadFlag() {
		if(respFld == null) return 0x00;
		
		byte b = (byte) (respFld.getParam1() & 0x03);

		logger.info("getter:"+b);
		return b;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, One Day Quota Write For Micro Payment: Bit 2~3, 小額消費日限額寫入 */
	//public OneDayQuotaWriteForMicroPayment GetResp_OneDayQuotaWriteForMicroPayment() {
	public byte getRespOneDayQuotaWriteForMicroPayment() {
		if(respFld == null) return 0x00;
		
		byte b = (byte) ((respFld.getParam1() & 0x0C) >> 2);
		logger.info("getter:"+b);
		return b;
		
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, SAM SignOnControl Flag: Bit 4~5, SAM卡SignOn控制旗標 */
	//public SAMSignOnControlFlag GetResp_SAMSignOnControlFlag() {
	public byte getRespSAMSignOnControlFlag() {
		if(respFld == null) return 0x00;
		byte b = (byte) ((respFld.getParam1() & 0x30) >> 4);
		logger.info("getter:"+b);
		return b;
		
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Check EV Flag For Mifare Only: Bit 6, 檢查餘額旗標 */
	public byte getRespCheckEVFlagForMifareOnly() {
		if(respFld == null) return 0x00;
		byte b = (byte) ((respFld.getParam1() & 0x40) >> 6);		
		return b;		
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Merchant Limit Use For Micro Payment: Bit 7, 小額消費通路限制使用旗標 */
	public byte getRespMerchantLimitUseForMicroPayment() {
		if(respFld == null) return 0x00;
		byte b = (byte) ((respFld.getParam1() & 0x80) >> 7);
		return b;
	}
	
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, One Day Quota Flag For Micro Payment: Bit 0~1, 小額消費日限額旗標 */
	//public OneDayQuotaFlagForMicroPayment GetResp_OneDayQuotaFlagForMicroPayment() {
	public byte getRespOneDayQuotaFlagForMicroPayment() {
		if(respFld == null) return 0x00;
		byte b = (byte) (respFld.getParam2() & 0x03);
		logger.info("getter:"+b);
		return b;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Once Quota Flag For Micro Payment: Bit 2, 小額消費次限額旗標 */
	public byte getRespOnceQuotaFlagForMicroPayment() {
		if(respFld == null) return 0x00;
		byte b = (byte) ((respFld.getParam2() & 0x04) >> 2);
		
		//logger.debug("getter:orig:"+String.format("%02X", mRespond[pRespSignOnParams2])+",bit:"+String.format("%02X", b));
		return b;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Check Debit Flag: Bit 3, 扣值交易合法驗證旗標 */
	public byte getRespCheckDebitFlag() {
		if(respFld == null) return 0x00;
		byte b = (byte) ((respFld.getParam2() & 0x08) >> 3);
		return b;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Mifare Check Enable Flag: Bit 4, 二代卡Level 1 */
	public byte getRespMifareCheckEnableFlag() {
		if(respFld == null) return 0x00;
		byte b = (byte) ((respFld.getParam2() & 0x10) >> 4);
		return b;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Pay On Behalf Flag: Bit 5, 是否允許代墊 */
	public boolean getRespPayOnBehalfFlag() {
		if(respFld == null) return false;  
		byte b = (byte) (respFld.getParam2() & 0x20);
		if (b == 0) {
			return false; // 不允許代墊
		} else {
			return true; // 允許代墊一次
		}
	}
	
	
	/* One Day Quota For Micro Payment, 2 bytes, Reader (適用於有SignOn之設備), 小額消費日限額額度, Unsigned and LSB First */
	public byte[] getRespOneDayQuotaForMicroPayment() {
		if (respFld == null || respFld.getOneDayQuotaForMicroPayment() == null) {
			return null;
		}
		return Arrays.copyOfRange(respFld.getOneDayQuotaForMicroPayment(), 0, 
				respFld.getOneDayQuotaForMicroPayment().length);
	}
	
	/* Once Quota For Micro Payment, 2 bytes, Reader (適用於有SignOn之設備), 小額消費次限額額度, Unsigned and LSB First */
	public byte[] getRespOnceQuotaForMicroPayment() {
		if (respFld == null || respFld.getOnceQuotaForMicroPayment()== null) {
			return null;
		}
		return Arrays.copyOfRange(respFld.getOnceQuotaForMicroPayment(), 0, 
				respFld.getOnceQuotaForMicroPayment().length);
	}
	
	/* Check Debit Value, 2 bytes, Reader (適用於有SignOn之設備), 扣值交易合法驗證金額, Unsigned and LSB First */
	public byte[] getRespCheckDebitValue() {
		if (respFld == null || respFld.getCheckDevitValue() == null) {
			
			return null;
		}
		return Arrays.copyOfRange(respFld.getCheckDevitValue(), 0, 
				respFld.getCheckDevitValue().length);
	}
	
	/* Add Quota Flag, 1 byte, Reader (適用於舊的額度控管), 加值額度控管旗標 */
	public byte getRespAddQuotaFlag() {
		if(respFld == null) return 0x00;
		return respFld.getAddQuotaFlag();
	}
	
	/* Add Quota, 3 bytes, Reader (適用於舊的額度控管), 加值額度, Unsigned and LSB First */
	public byte[] getRespAddQuota() {
		if (respFld == null || respFld.getAddQuota() == null) {
			return null;
		}
		return Arrays.copyOfRange(respFld.getAddQuota(), 0, 
				respFld.getAddQuota().length);
	}
	
	public byte[] getRespLastTXNDateTime() {
		if (respFld == null || respFld.getLastTxnDateTime() == null) {
			return null;
		}
		return Arrays.copyOfRange(respFld.getLastTxnDateTime(), 0, 
				respFld.getLastTxnDateTime().length);
	}
	
	public byte getRespServiceProviderID() {
		if(respFld == null) return 0x00;
		return respFld.getServiceProviderID();
	}
	
	public byte[] getRespNewServiceProviderID() {
		if (respFld == null || respFld.getNewServiceProviderID() == null) {
			return null;
		}
		return Arrays.copyOfRange(respFld.getNewServiceProviderID(), 0, 
				respFld.getNewServiceProviderID().length);
	}
	

	public byte[] getRespTheRemainderOfAddQuota() {
		if (respFld == null || respFld.getTheRemaindeOfAddQuota() == null) {
			return null;
		}
		return Arrays.copyOfRange(respFld.getTheRemaindeOfAddQuota(), 0, 
				respFld.getTheRemaindeOfAddQuota().length);
	}
	
	
	public byte[] getRespCancelCreditQuota() {
		if (respFld == null || respFld.getCancelCreditQuota() == null) {
			return null;
		}
		return Arrays.copyOfRange(respFld.getCancelCreditQuota(), 0, 
				respFld.getCancelCreditQuota().length);
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
		if(respFld != null){
			logger.debug(PPR_SignOnQuery.class.getName()+" recv:" + Util.hex2StringLog(mRespond));
			
			logger.debug("Auth.Credit Limit:"+Util.hex2StringLog(getRespAuthCreditLimit()));
			logger.debug("Auth.Credit Balance:"+Util.hex2StringLog(getRespAuthCreditBalance()));
			logger.debug("Auth.Credit Cumulative:"+Util.hex2StringLog(getRespAuthCreditCumulative()));
			logger.debug("Auth.Credit Cancel Cumulative:"+Util.hex2StringLog(getRespAuthCancelCreditCumulative()));
			
			logger.debug("Param1:"+String.format("%02X", respFld.getParam1()));
			logger.debug("Param2:"+String.format("%02X", respFld.getParam2()));
			
			logger.debug("One Day Quota For Micropayment:"+Util.hex2StringLog(getRespOneDayQuotaForMicroPayment()));
			logger.debug("Once Quota For MicroPayment:"+Util.hex2StringLog(getRespOnceQuotaForMicroPayment()));
			logger.debug("Check Debit value:"+Util.hex2StringLog(getRespCheckDebitValue()));
			logger.debug("Add Quota Flag"+String.format("%02X", getRespAddQuotaFlag()));
			logger.debug("Add Quota:"+Util.hex2StringLog(getRespAddQuota()));
			
			logger.debug("Last Txn DateTime:"+Util.hex2StringLog(getRespLastTXNDateTime()));
			logger.debug("Service Provider ID:"+String.format("%02X", getRespServiceProviderID()));
			logger.debug("New Service Provider ID:"+Util.hex2StringLog(getRespNewServiceProviderID()));

			logger.debug("The Remainder of Add Quota:"+Util.hex2StringLog(getRespTheRemainderOfAddQuota()));
			logger.debug("Cancel Credit Quota:"+Util.hex2StringLog(getRespCancelCreditQuota()));
		}
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
		
		//check total length
		int length = bytes.length;
		if (scRespLength != length) {
			// invalid respond format... 
			logger.error("RespLen wrong:"+scRespLength);
			return false;
		}
		
		//check dataBody Length
		if (bytes[2] != (byte) (scRespDataLength + 2)) { // Data + SW1 + SW2
			// invalid data format...
			logger.error("RespLen wrong:"+scRespLength);
			return false;
		}

		//check checkSum
		byte sum = getEDC(bytes, length);
		if (sum != bytes[scRespLength - 1]) {
			// check sum error...
			logger.error("CheckSum error");
			return false;
		}
		int dataLength = bytes[2] & 0x000000FF;
		Resp_SW1 = bytes[scRespDataOffset + dataLength - 2];
		Resp_SW2 = bytes[scRespDataOffset + dataLength + 1 - 2];
		
		
		byte[] b = Arrays.copyOfRange(bytes, scRespDataOffset, scRespDataOffset+scRespDataLength);
		mRespond = Arrays.copyOf(bytes, length);
		respFld = new ResponseField();
		respFld.parse(b);
		
		
		logger.info("end");
		return true;
	}

}
