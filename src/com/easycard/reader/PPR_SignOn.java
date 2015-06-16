package com.easycard.reader;

import java.util.Arrays;

import org.apache.log4j.Logger;

import com.easycard.utilities.Util;


public class PPR_SignOn extends APDU {
	
	static Logger logger = Logger.getLogger(PPR_SignOn.class);
	public static final String scDescription = "將0810端末開機訊息透過Reader傳入SAM卡中做認證";
	
	//private static PPR_SignOn sThis = null;
	
	private static final int scReqDataLength = 128;
	private static final int scReqLength = scReqDataLength + scReqMinLength;
	private static final int scReqInfoLength = scReqDataLength + scReqInfoMinLength;
	private static final int scRespDataLength = 29;
	private static final int scRespLength = scRespDataLength + scRespMinLength;
	
	private boolean mReqDirty = true;
	
	private byte[] mRequest = new byte[scReqLength];
	private byte[] mRespond = null;
	
	
	
	public PPR_SignOn() {
		Req_NAD = 0;
		Req_PCB = 0; 
		Req_LEN = (byte) scReqInfoLength;
		
		Req_CLA = (byte) 0x80;
		Req_INS = 0x02;			
		Req_P1 = 0x00;
		Req_P2 = 0x00;
		
		Req_Lc = (byte) scReqDataLength;
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
	
	/* H-TAC, 8 bytes, Host, Host認證碼, 用於舊SAM Card, T6401 */
	private static final int scReqData_H_TAC = scReqDataOffset + 0;
	private static final int scReqData_H_TAC_Len = 8;
 	public boolean SetReq_H_TAC(String htac) { 
 		if (htac == null || htac.length() != scReqData_H_TAC_Len * 2) {
			return false;
		}
 		
 		//byte[] b = Util.ascii2Bcd(htac);
 		
 	//	byte[] b = DataFormat.hexStringToByteArray(htac);
 		byte[] b= Util.ascii2Bcd(htac);
 		if (b == null) {
 			return false;
 		}
 		System.arraycopy(b, 0, mRequest, scReqData_H_TAC, scReqData_H_TAC_Len);
 		
		mReqDirty = true;
		return true;
	}
	
	/* HAToken, 16 bytes, Host, Host Authentication Token, 用於新SAM Card, T6409 */
	private static final int scReqData_HAToken = scReqData_H_TAC + scReqData_H_TAC_Len;
	private static final int scReqData_HAToken_Len = 16;
	public boolean SetReq_HAToken(String haToken) {
		if (haToken == null || haToken.length() != scReqData_HAToken_Len * 2) {
			return false;
		}
		
		//byte[] b = Util.ascii2Bcd(haToken);
	//	byte[] b = DataFormat.hexStringToByteArray(haToken);
		byte[] b=  Util.ascii2Bcd(haToken);
		if (b == null) {
			return false;
		}
		System.arraycopy(b, 0, mRequest, scReqData_HAToken, scReqData_HAToken_Len);

		mReqDirty = true;
		return true;
	}
	
	/* SAM Update Option, 1 byte, Host, SAM參數更新選項 (0: 不更新, 1: 更新), 用於新SAM Card */
	private static final int scReqData_SAMUpdateOption = scReqData_HAToken + scReqData_HAToken_Len;
	private static final int scReqData_SAMUpdateOption_Len = 1;
	private static final int scReqData_SAMUpdateOption_ACB = 0; 			// T5367#0, 加值授權額度 (ACB)
	private static final int scReqData_SAMUpdateOption_ACL = 1; 			// T5367#0, 加值額度預設值 (ACL)
	private static final int scReqData_SAMUpdateOption_SAMUsageControl = 2; // T5367#0, SAM控制參數 (SAM Usage Control)
	private static final int scReqData_SAMUpdateOption_TagListTable = 3; 	// T5367#0, Tag控制參數 (Tag List Table)
	private static final int scReqData_SAMUpdateOption_HasNext = 7; 		// T5367#0, 是否還有下一個參數須更新 (0: 否, 1: 是)
	public void SetReq_UpdateACB(boolean bUpdate) {
		if (bUpdate) {
			SetBit(mRequest[scReqData_SAMUpdateOption], scReqData_SAMUpdateOption_ACB);
		} else {
			ClearBit(mRequest[scReqData_SAMUpdateOption], scReqData_SAMUpdateOption_ACB);
		}
		mReqDirty = true;
	}
	
	public void SetReq_UpdateACL(boolean bUpdate) {
		if (bUpdate) {
			SetBit(mRequest[scReqData_SAMUpdateOption], scReqData_SAMUpdateOption_ACL);
		} else {
			ClearBit(mRequest[scReqData_SAMUpdateOption], scReqData_SAMUpdateOption_ACL);
		}
		mReqDirty = true;
	}
	// 來自CMAS T5367的回報
	public void SetReq_UpdateSAMUsageControl(boolean bUpdate) {
		if (bUpdate) {
			SetBit(mRequest[scReqData_SAMUpdateOption], scReqData_SAMUpdateOption_SAMUsageControl);
		} else {
			ClearBit(mRequest[scReqData_SAMUpdateOption], scReqData_SAMUpdateOption_SAMUsageControl);
		}
		mReqDirty = true;
	}
	
	public void SetReq_UpdateTagListTable(boolean bUpdate) {
		if (bUpdate) {
			SetBit(mRequest[scReqData_SAMUpdateOption], scReqData_SAMUpdateOption_TagListTable);
		} else {
			ClearBit(mRequest[scReqData_SAMUpdateOption], scReqData_SAMUpdateOption_TagListTable);
		}
		mReqDirty = true;
	}
	
	public void SetReq_UpdateHasNext(boolean bUpdate) {
		if (bUpdate) {
			SetBit(mRequest[scReqData_SAMUpdateOption], scReqData_SAMUpdateOption_HasNext);
		} else {
			ClearBit(mRequest[scReqData_SAMUpdateOption], scReqData_SAMUpdateOption_HasNext);
		}
		mReqDirty = true;
	}
	
	public boolean SetReq_SAMUpdateOption(String option) {		
		if (option == null || option.length() != scReqData_SAMUpdateOption_Len * 2) {
			return false;
		}
		
		
		byte[] b=Util.ascii2Bcd(option);
		if (b == null) {
			return false;
		}
		mRequest[scReqData_SAMUpdateOption] = b[0];
		
		mReqDirty = true;
		return true;
	}
	
	/* New SAM Value, 40 bytes, Host, 新SAM參數值(SAM Update Option=0x00時, 補0x00),
	 * 新SAM參數值長度不足40bytes時, 左靠右補0, 用於新SAM Card, T5367#2 */
	private static final int scReqData_NewSAMValue = scReqData_SAMUpdateOption + scReqData_SAMUpdateOption_Len;
	private static final int scReqData_NewSAMValue_Len = 40;
	public boolean SetReq_NewSAMValue(String values) {
		if (values == null || values.length() != scReqData_NewSAMValue_Len * 2) {
			return false;
		}
		
		//byte[] b = Util.ascii2Bcd(values);
	//	byte[] b = DataFormat.hexStringToByteArray(values);
		byte[]b=Util.ascii2Bcd(values);
		if (b == null) {
			return false;
		}
		System.arraycopy(b, 0, mRequest, scReqData_NewSAMValue, scReqData_NewSAMValue_Len);
		
		mReqDirty = true;
		return true;
	}
	
	/* Update SAM Value MAC, 16 bytes, Host, 更新SAM參數所需之MAC(SAM Update Option=0x00時, 補0x00), 
	 * 用於新SAM Card, T5367#82 */
	private static final int scReqData_UpdateSAMValueMAC = scReqData_NewSAMValue + scReqData_NewSAMValue_Len;
	private static final int scReqData_UpdateSAMValueMAC_Len = 16;
	public boolean SetReq_UpdateSAMValueMAC(String mac) {
		if (mac == null || mac.length() != scReqData_UpdateSAMValueMAC_Len * 2) {
			return false;
		}
		
		byte[] b = Util.ascii2Bcd(mac);
		//byte[] b = DataFormat.hexStringToByteArray(mac);
		if (b == null) {
			return false;
		}
		System.arraycopy(b, 0, mRequest, scReqData_UpdateSAMValueMAC, scReqData_UpdateSAMValueMAC_Len);
	
		mReqDirty = true;
		return true;
	}
	
	/* 
	 * PPR_SignOn參數設定, 1 byte, Host, 適用於有SignOn之設備
	 * CPD Read Flag: Bit 0~1, 二代CPD讀取及驗證設定, T4824
	 * One Day Quota Write For Micro Payment: Bit 2~3, 小額消費日限額寫入, T4823
	 * SAM SignOnControl Flag: Bit 4~5, SAM卡SignOn控制旗標, T5369
	 * Check EV Flag For Mifare Only: Bit 6, 檢查餘額旗標
	 * Merchant Limit Use For Micro Payment: Bit 7, 小額消費通路限制使用旗標 
	 */
	private static final int scReqData_SignOnParams1 = scReqData_UpdateSAMValueMAC + scReqData_UpdateSAMValueMAC_Len;
	private static final int scReqData_SignOnParams1_Len = 1;
	
	/* CPD Read Flag: Bit 0~1, 二代CPD讀取及驗證設定, T4824 */
	public boolean SetReq_CPDReadFlag(String flag) {
		if (flag == null || flag.length() != 2) {
			return false;
		}
		
		byte b = mRequest[scReqData_SignOnParams1];
		b = ClearBit(b, 0);
		b = ClearBit(b, 1); // 不讀取Host且Reader不驗證
		
		if (flag.equals("01")) { // 讀取Host且Reader不驗證
			b = SetBit(b, 0); 
		} else if (flag.equals("10")) { // 不讀取Host且Reader要驗證
			b = SetBit(b, 1); 
		} else if (flag.equals("11")) { // 讀取Host且Reader要驗證
			b = SetBit(b, 0); 
			b = SetBit(b, 1); 
		} 
		
		mRequest[scReqData_SignOnParams1] = b;
		mReqDirty = true;
		return true;
	}
	
	/* One Day Quota Write For Micro Payment: Bit 2~3, 小額消費日限額寫入, T4823 */
	public boolean SetReq_OneDayQuotaWriteForMicroPayment(String flag) {
		if (flag == null || flag.length() != 2) {
			return false;
		}
		
		byte b = mRequest[scReqData_SignOnParams1];
		
		b = ClearBit(b, 0); // Mifare與CPU都不寫入
		b = ClearBit(b, 1);
		if (flag.equals("01")) { // Mifare寫入, CPU不寫入
			b = SetBit(b, 2);
		} else if (flag.equals("10")) { // Mifare不寫入, CPU寫入
			b = SetBit(b, 3);
		} else if (flag.equals("11")) { // Mifare寫入, CPU寫入 (default)
			b = SetBit(b, 2);
			b = SetBit(b, 3);
		}
		
		mRequest[scReqData_SignOnParams1] = b;
		mReqDirty = true;
		return true;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, SAM SignOnControl Flag: Bit 4~5, SAM卡SignOn控制旗標, T5309 */
	public boolean SetReq_SAMSignOnControlFlag(String flag) {
		if (flag == null || flag.length() != 2) {
			return false;
		}
		
		byte b = mRequest[scReqData_SignOnParams1];
		
		b = ClearBit(b, 4);
		b = ClearBit(b, 5);
		if (flag.equals("01")) {
			b = SetBit(b, 4);
		} else if (flag.equals("10")) {
			b = SetBit(b, 5);
		} else if (flag.equals("11")) {
			b = SetBit(b, 4);
			b = SetBit(b, 5);
		}
		
		mRequest[scReqData_SignOnParams1] = b;
		mReqDirty = true;
		return true;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Check EV Flag For Mifare Only: Bit 6, 檢查餘額旗標, T6002#12 */
	public boolean SetReq_CheckEVFlagForMifareOnly(String flag) {
		
		logger.info("setter:"+flag);
		if (flag == null || flag.length() != 2) {
			return false;
		}
		
		byte b = mRequest[scReqData_SignOnParams1];
		
		logger.debug("OneDay..CheckEV value:"+String.format("%02X", b));
		if (flag.equals("01")) { 
			logger.debug("setBit");
			b = SetBit(b, 6); // 不檢查餘額
		} else { 
			logger.debug("clearBit");
			b = ClearBit(b, 6); // 檢查餘額 (default)
		}
		logger.debug("OneDay..CheckEV value:"+String.format("%02X", b));
		mRequest[scReqData_SignOnParams1] = b;
		mReqDirty = true;
		return true;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Merchant Limit Use For Micro Payment: Bit 7, 小額消費通路限制使用旗標, 
	 * T6002#28, 00: 不限制, 01: 限制  
	 */
	//public void SetReq_MerchantLimitUseForMicroPayment(boolean bLimit) {
	public void SetReq_MerchantLimitUseForMicroPayment(String flag) {
	
		logger.info("setter:"+flag);
		byte b = mRequest[scReqData_SignOnParams1];
		
		logger.debug("OneDay..value:"+String.format("%02X", b));
		if (flag.equals("01")) { 
			logger.debug("setBit");
			b = SetBit(b, 7); // 不限制使用			
		} else {
			logger.debug("clearBit");
			b = ClearBit(b, 7); // 限制使用
		}
		
		logger.debug("OneDay..value:"+String.format("%02X", b));
		mRequest[scReqData_SignOnParams1] = b;
		mReqDirty = true;
	}
	
	
	
	/* 
	 * PPR_SignOn參數設定, 1 byte, Host, 適用於有SignOn之設備
	 * One Day Quota Flag For Micro Payment: Bit 0~1, 小額消費日限額旗標, T6002#0
	 * Once Quota Flag For Micro Payment: Bit 2, 小額消費次限額旗標, T6002#6
	 * Check Debit Flag: Bit 3, 扣值交易合法驗證旗標, T6002#22
	 * Mifare Check Enable Flag: Bit 4, 二代卡Level 1
	 * Pay On Behalf Flag: Bit 5, 是否允許代墊
	 * RFU: Bit 6~7, 保留 
	 */
	private static final int scReqData_SignOnParams2 = scReqData_SignOnParams1 + scReqData_SignOnParams1_Len;
	private static final int scReqData_SignOnParams2_Len = 1;
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, One Day Quota Flag For Micro Payment: Bit 0~1, 小額消費日限額旗標, T6002#0 */
	public boolean SetReq_OneDayQuotaFlagForMicroPayment(String flag) {
		
		logger.info("setter:"+flag);
		if (flag == null || flag.length() != 2) {
			return false;
		}
		
		byte b = mRequest[scReqData_SignOnParams2];
		
		b = ClearBit(b, 0); // 不檢查, 不累計日限額
		b = ClearBit(b, 1);
		if (flag.equals("01")) { // 不檢查, 累計日限額
			b = SetBit(b, 0);
		} else if (flag.equals("10")) { // 檢查, 不累計日限額
			b = SetBit(b, 1);
		} else if (flag.equals("11")) { // 檢查, 累計日限額
			b = SetBit(b, 0);
			b = SetBit(b, 1);
		}
		
		mRequest[scReqData_SignOnParams2] = b;
		mReqDirty = true;
		return false;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Once Quota Flag For Micro Payment: Bit 2, 小額消費次限額旗標, T6002#6 */
	public boolean SetReq_OnceQuotaFlagForMicroPayment(String flag) {
		if (flag == null || flag.length() != 2) {
			return false;
		}
		
		byte b = mRequest[scReqData_SignOnParams2];
		
		if (flag.equals("01")) {
			b = SetBit(b, 2); // 限制次限額
		} else {
			b = ClearBit(b, 2); // 不限制次限額
		}
		
		mRequest[scReqData_SignOnParams2] = b;
		mReqDirty = true;
		return true;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Check Debit Flag: Bit 3, 扣值交易合法驗證旗標, T6002#22 */
	public boolean SetReq_CheckDebitFlag(String flag) {
		if (flag == null || flag.length() != 2) {
			return false;
		}
		
		byte b = mRequest[scReqData_SignOnParams2];
		
		if (flag.equals("01")) {
			b = SetBit(b, 3); // 限制扣值交易合法驗證旗標
		} else {
			b = ClearBit(b, 3); // 不限制扣值交易合法驗證旗標
		}
		
		mRequest[scReqData_SignOnParams2] = b;
		mReqDirty = true;
		return true;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Mifare Check Enable Flag: Bit 4, 二代卡Level 1 */
	public boolean SetReq_MifareCheckEnableFlag(String flag) {
		// Bruce, 不須帶值
		return false;
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Pay On Behalf Flag: Bit 5, 是否允許代墊 */
	public boolean SetReq_PayOnBehalfFlag(String flag) {
		// Bruce, 不須帶值
		return false;
	}
	
	/* One Day Quota For Micro Payment, 2 bytes, Host, 小額消費日限額額度, 適用於有SignOn之設備, Unsigned and LSB First, T6002#2 */
	private static final int scReqData_OneDayQuotaForMicroPayment = scReqData_SignOnParams2 + scReqData_SignOnParams2_Len;
	private static final int scReqData_OneDayQuotaForMicroPayment_Len = 2;
	public boolean SetReq_OneDayQuotaForMicroPayment(String amount) {
		if (amount == null || amount.length() != scReqData_OneDayQuotaForMicroPayment_Len * 2) {
			return false;
		}
		
		byte[] b = Util.ascii2Bcd(amount);
	//	byte[] b = DataFormat.hexStringToByteArray(amount);
		if (b == null) {
 			return false;
 		}
 		System.arraycopy(b, 0, mRequest, scReqData_OneDayQuotaForMicroPayment, scReqData_OneDayQuotaForMicroPayment_Len);
		
		mReqDirty = true;
		return true;
	}
	
	/* Once Quota For Micro Payment, 2 bytes, Host, 小額消費次限額額度, 適用於有SignOn之設備, Unsigned and LSB First, T6002#8 */
	private static final int scReqData_OnceQuotaForMicroPayment = scReqData_OneDayQuotaForMicroPayment + scReqData_OneDayQuotaForMicroPayment_Len;
	private static final int scReqData_OnceQuotaForMicroPayment_Len = 2;
	public boolean SetReq_OnceQuotaForMicroPayment(String amount) {
		if (amount == null || amount.length() != scReqData_OnceQuotaForMicroPayment_Len * 2) {
			return false;
		}
		
		byte[] b = Util.ascii2Bcd(amount);
		//byte[] b = DataFormat.hexStringToByteArray(amount);
		if (b == null) {
 			return false;
 		}
 		System.arraycopy(b, 0, mRequest, scReqData_OnceQuotaForMicroPayment, scReqData_OnceQuotaForMicroPayment_Len);
		
		mReqDirty = true;
		return true;
	}
	
	/* Check Debit Value, 2bytes, Host, 扣值交易合法驗證金額, 適用於有SignOn之設備, Unsigned and LSB First, ?T6002#24? */
	private static final int scReqData_CheckDebitValue = scReqData_OnceQuotaForMicroPayment + scReqData_OnceQuotaForMicroPayment_Len;
	private static final int scReqData_CheckDebitValue_Len = 2;
	public boolean SetReq_CheckDebitValue(String amount) {
		if (amount == null || amount.length() != scReqData_CheckDebitValue_Len * 2) {
			return false;
		}
		
		byte[] b = Util.ascii2Bcd(amount);
	//byte[] b = DataFormat.hexStringToByteArray(amount);
		if (b == null) {
 			return false;
 		}
 		System.arraycopy(b, 0, mRequest, scReqData_CheckDebitValue, scReqData_CheckDebitValue_Len);
		
		mReqDirty = true;
		return true;
	}
	
	/* Add Quota Flag, 1 byte, Host, 加值額度控管旗標, 適用於舊的額度控管, T6002#14 */
	private static final int scReqData_AddQuotaFlag = scReqData_CheckDebitValue + scReqData_CheckDebitValue_Len;
	private static final int scReqData_AddQuotaFlag_Len = 1;
	public boolean SetReq_AddQuotaFlag(String flag) {
		if (flag == null || flag.length() != scReqData_AddQuotaFlag_Len * 2) {
			return false;
		}
		if (flag.equals("00")) { // 不檢查額度
			mRequest[scReqData_AddQuotaFlag] = 0x00;
		} else { // 0x01 (default), 檢查額度 
			mRequest[scReqData_AddQuotaFlag] = 0x01;
		}
		
		mReqDirty = true;
		return true;
	}
	
	/* Add Quota, 3 bytes, Host, 加值額度, 適用於舊的額度控管, Unsigned and LSB First, T6002#16 */
	private static final int scReqData_AddQuota = scReqData_AddQuotaFlag + scReqData_AddQuotaFlag_Len;
	private static final int scReqData_AddQuota_Len = 3;
	public boolean SetReq_AddQuota(String amount) {
		if (amount == null || amount.length() != scReqData_AddQuota_Len * 2) {
			return false;
		}
		
		byte[] b = Util.ascii2Bcd(amount);
		//byte[] b = DataFormat.hexStringToByteArray(amount);
		if (b == null) {
 			return false;
 		}
 		System.arraycopy(b, 0, mRequest, scReqData_AddQuota, scReqData_AddQuota_Len);
		
		mReqDirty = true;
		return true;
	}
	
	/* RFU, 保留(Reserved For Use), 補0 */
	private static final int scReqData_RFU = scReqData_AddQuota + scReqData_AddQuota_Len;
	private static final int scReqData_RFU_Len = 31;
	
	/* EDC, 檢核碼(Error Detection Code), Hash方式(1byte, T5303) + Hsh Value(3byte, T5306) */
	private static final int scReqData_EDC = scReqData_RFU + scReqData_RFU_Len;
	private static final int scReqData_EDC_Len = 4;
	public boolean SetReq_EDC(String t5303_t5306) {
		if (t5303_t5306 == null || t5303_t5306.length() != scReqData_EDC_Len * 2) {
			return false;
		}
		
		byte[] b = Util.ascii2Bcd(t5303_t5306);
		
		System.arraycopy(b, 0, mRequest, scReqData_EDC, scReqData_EDC_Len);
		
		mReqDirty = true;
		return true;
	}
	
	@Override
	public byte[] GetRequest() {
		if (mReqDirty) {
			mReqDirty = false;
			mRequest[scReqLength - 1] = Req_EDC = getEDC(mRequest, mRequest.length);
		}
		return mRequest;
	}

	@Override
	public boolean SetRequestData(byte[] bytes) {
		if (bytes == null || bytes.length != scReqDataLength) {
			return false;
		}
		
		System.arraycopy(bytes, 0, mRequest, scReqDataOffset, scReqDataLength);
		
		mReqDirty = true;
		return true;
	}
	
	@Override
	public int GetReqRespLength() {
		return scRespLength;
	}
	
	/* Credit Balance Change Flag, 1 byte, SAM,  加值授權額度(ACB)變更旗標
	 * 0x00: 未變更
	 * 0x01: 額度有變更  
	 */
	private static final int scRespData_CreditBalanceChangeFlag = scRespDataOffset;
	private static final int scRespData_CreditBalanceChangeFlag_Len = 1;
	public byte GetResp_CreditBalanceChangeFlag() {
		if (mRespond == null) {
			return 0;
		}
		return mRespond[scRespData_CreditBalanceChangeFlag]; 
	}
	
	/* Original Authorized Credit Limit, 3 bytes, 新SAM,  原加值額度預設值(ACL)
	 * Unsigned and LSB First  
	 */
	private static final int scRespData_OriginalAuthorizedCreditLimit = scRespData_CreditBalanceChangeFlag + scRespData_CreditBalanceChangeFlag_Len;
	private static final int scRespData_OriginalAuthorizedCreditLimit_Len = 3;
	public byte[] GetResp_OriginalAuthorizedCreditLimit() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_OriginalAuthorizedCreditLimit, 
						scRespData_OriginalAuthorizedCreditLimit + scRespData_OriginalAuthorizedCreditLimit_Len);
	}
	
	/* Original Authorized Credit Balance, 3 bytes, 新SAM,  原加值授權額度(ACB)
	 * Unsigned and LSB First  
	 */
	private static final int scRespData_OriginalAuthorizedCreditBalance = scRespData_OriginalAuthorizedCreditLimit + scRespData_OriginalAuthorizedCreditLimit_Len;
	private static final int scRespData_OriginalAuthorizedCreditBalance_Len = 3;
	public byte[] GetResp_OriginalAuthorizedCreditBalance() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_OriginalAuthorizedCreditBalance, 
						scRespData_OriginalAuthorizedCreditBalance + scRespData_OriginalAuthorizedCreditBalance_Len);
	}
	
	/* Original Authorized Credit Cumulative, 3 bytes, 新SAM,  原加值累積已用額度(ACC)
	 * Unsigned and LSB First  
	 */
	private static final int scRespData_OriginalAuthorizedCreditCumulative = scRespData_OriginalAuthorizedCreditBalance + scRespData_OriginalAuthorizedCreditBalance_Len;
	private static final int scRespData_OriginalAuthorizedCreditCumulative_Len = 3;
	public byte[] GetResp_OriginalAuthorizedCreditCumulative() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_OriginalAuthorizedCreditCumulative, 
						scRespData_OriginalAuthorizedCreditCumulative + scRespData_OriginalAuthorizedCreditCumulative_Len);
	}
	
	/* Original Authorized Cancel Credit Cumulative, 3 bytes, 新SAM,  原取消加值累積已用額度(ACCC)
	 * Unsigned and LSB First  
	 */
	private static final int scRespData_OriginalAuthorizedCancelCreditCumulative = scRespData_OriginalAuthorizedCreditCumulative + scRespData_OriginalAuthorizedCreditCumulative_Len;
	private static final int scRespData_OriginalAuthorizedCancelCreditCumulative_Len = 3;
	public byte[] GetResp_OriginalAuthorizedCancelCreditCumulative() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_OriginalAuthorizedCancelCreditCumulative, 
						scRespData_OriginalAuthorizedCancelCreditCumulative + scRespData_OriginalAuthorizedCancelCreditCumulative_Len);
	}

	/* CACrypto, 16 bytes, 新SAM,  Credit Authorization Cryptogram
	 * Unsigned and LSB First  
	 */
	private static final int scRespData_CACrypto = scRespData_OriginalAuthorizedCancelCreditCumulative + scRespData_OriginalAuthorizedCancelCreditCumulative_Len;
	private static final int scRespData_CACrypto_Len = 16;
	public byte[] GetResp_CACrypto() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_CACrypto, scRespData_CACrypto + scRespData_CACrypto_Len);
	}

	@Override
	public byte[] GetRespond() {
		return mRespond;
	}

	@Override
	public boolean SetRespond(byte[] bytes) {
		
		
		if(this.checkResponseFormat(bytes, scRespDataLength) == false)
			return false;
			
		mRespond = Arrays.copyOf(bytes, bytes.length);
		return true;
	}

	@Override
	public void debugResponseData() {
		// TODO Auto-generated method stub
		logger.debug("pprSignon recv:" + Util.hex2StringLog(mRespond));
	}
}
