package com.easycard.reader;



import java.util.Arrays;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import com.easycard.utilities.Util;

public class PPR_Reset extends APDU {

	static Logger logger = Logger.getLogger(PPR_Reset.class);
	public static final String scDescription = "重置Reader, 將初始化參數傳給Reader, 並取得SAToken/S-TAC"; 
	
	//private static PPR_Reset sThis = null;
	
	private static final int scReqDataLength = 64;
	private static final int scReqLength = scReqDataLength + scReqMinLength;
	private static final int scReqInfoLength = scReqDataLength + scReqInfoMinLength;
	private static final int scRespDataLength = 250;
	private static final int scRespLength = scRespDataLength + scRespMinLength;
	
	private boolean mReqDirty = true;
	
	private byte[] mRequest = new byte[scReqLength];
	private byte[] mRespond = null;
	
	/*
	public static PPR_Reset sGetInstance() {
		if (sThis == null) {
			sThis = new PPR_Reset();
		}
		return sThis;
	}
	*/
	//private PPR_Reset() {
	public PPR_Reset(){
		
		logger.info("Start");
		Req_NAD = 0;
		Req_PCB = 0; 
		Req_LEN = scReqInfoLength;
		
		Req_CLA = (byte) 0x80;
		Req_INS = 0x01;			
		Req_P1 = 0x00;
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
		
		logger.info("End");
	}
	
	/* PPR_Reset_Offline, (離線)重製Reader, 將初始化參數傳給Reader */
	public void SetOffline(boolean bOffline) {
		
		logger.info("setter:"+bOffline);
		if (bOffline) {
			mRequest[6] = Req_P2 = 0x01;
		} else {
			mRequest[6] = Req_P2 = 0x00;
		}
		mReqDirty = true;
	}
	
	/* TM Location ID, 10 bytes, TM, 終端機(TM)店號, ASCII, 右靠左補0 */
	private static final int scReqData_TMLocationID = scReqDataOffset + 0;
	private static final int scReqData_TMLocationID_Len = 10;
	public boolean SetReq_TMLocationID(String id) {
		
		logger.info("setter:"+id);
		if (id == null || id.length() > scReqData_TMLocationID_Len) {
			logger.error("id null or len wrong");
			return false;
		}
		
		int j = id.length() - 1;
		for (int i = scReqData_TMLocationID_Len - 1; i >= 0; i --) {
			if (j >= 0) {
				mRequest[scReqData_TMLocationID + i] = (byte) id.charAt(j --);
			} else {
				mRequest[scReqData_TMLocationID + i] = '0';
			}
		}
		
		mReqDirty = true;
				
		return true;
	}
	
	public String GetReq_TMLocationID() {
		byte[] b = Arrays.copyOfRange(mRequest, scReqData_TMLocationID, 
										scReqData_TMLocationID + scReqData_TMLocationID_Len);
		String result = new String(b);		
		logger.info("getter:"+result);
		return result;
	}
	
	/* TM ID, 2 bytes, TM, 終端機(TM)機號, ASCII, 右靠左補0 */
	private static final int scReqData_TMID = scReqData_TMLocationID + scReqData_TMLocationID_Len;
	private static final int scReqData_TMID_Len = 2;
	public boolean SetReq_TMID(String id) {
		
		logger.info("setter:"+id);
		if (id == null || id.length() > scReqData_TMID_Len) {
			logger.error("id null or len wrong");
			return false;
		}
		
		int j = id.length() - 1;
		for (int i = scReqData_TMID_Len - 1; i >= 0; i --) {
			if (j >= 0) {
				mRequest[scReqData_TMID + i] = (byte) id.charAt(j --);
			} else {
				mRequest[scReqData_TMID + i] = '0';
			}
		}
		
		mReqDirty = true;
				
		return true;
	}
	
	public String GetReq_TMID() {
		byte[] b = Arrays.copyOfRange(mRequest, scReqData_TMID, 
											scReqData_TMID + scReqData_TMID_Len);
				
		String result = new String(b);
		logger.info("getter:"+result);
		return result;
	}
	
	/* TM TXN Date Time, 14 bytes, TM, 終端機(TM)交易日期時間, ASCII, YYYYMMDDhhmmss */
	private static final int scReqData_TMTXNDateTime = scReqData_TMID + scReqData_TMID_Len;
	private static final int scReqData_TMTXNDateTime_Len = 14;
	public void SetReq_TMTXNDateTime(int unixTimeStamp) {
		
		logger.info("Start");
		String dateTime = Util.sGetDateTime(unixTimeStamp);
		
		logger.info("setter: int("+unixTimeStamp+"), format:"+dateTime);
		for (int i = 0; i < scReqData_TMTXNDateTime_Len; i ++) {
			mRequest[scReqData_TMTXNDateTime + i] = (byte) dateTime.charAt(i);
		}
		mReqDirty = true;
		
		logger.info("End");
	}
	
	public String GetReq_TMTXNDateTime() {
		byte[] b = Arrays.copyOfRange(mRequest, scReqData_TMTXNDateTime, 
										scReqData_TMTXNDateTime + scReqData_TMTXNDateTime_Len);
		String result = new String(b);
		logger.info("getter:"+result);
		return result;
	}
	
	public String GetReq_TMTXNDate() {
		String s = GetReq_TMTXNDateTime().substring(0, 8);		
		logger.info("getter:"+s);		
		return s;
	}
	
	public String GetReq_TMTXNDateShort() {
		String s = GetReq_TMTXNDateTime().substring(2, 8);		 
		logger.info("getter:"+s);
		return s;
	}
	
	public String GetReq_TMTXNTime() {
		String s = GetReq_TMTXNDateTime().substring(8, 14);
		logger.info("getter:"+s);
		return s;
	}
	
	/* TM Serial Number, 6 bytes, TM, 終端機(TM)交易序號, ASCII, 靠右左補0, 值須為0~9, 交易成功時進號, 失敗時不進號 */
	private static final int scReqData_TMSerialNumber = scReqData_TMTXNDateTime + scReqData_TMTXNDateTime_Len;
	private static final int scReqData_TMSerialNumber_Len = 6;
	public boolean SetReq_TMSerialNumber(int sn) {
		
		
		logger.info("setter:"+sn);
		String s = String.format("%06d", sn);
		
		logger.info("setter:"+s);
		for (int i = 0; i < s.length(); i ++) {
			mRequest[scReqData_TMSerialNumber + i] = (byte) s.charAt(i);
		}
		
		mReqDirty = true;
		
		
		return true;
	}
	
	public byte[] GetReq_TMSerialNumber() {
		
		byte []result = Arrays.copyOfRange(mRequest, scReqData_TMSerialNumber, scReqData_TMSerialNumber + scReqData_TMSerialNumber_Len);
		logger.info("getter:"+Util.bcd2Ascii(result));
		return result;
	}
	
	
	/* TM Agent Number, 4 bytes, TM, 終端機(TM)收銀員代號, ASCII, 靠右左補0, 值須為0~9 */
	private static final int scReqData_TMAgentNumber = scReqData_TMSerialNumber + scReqData_TMSerialNumber_Len;
	private static final int scReqData_TMAgentNumber_Len = 4;
	public boolean SetReq_TMAgentNumber(String an) {
		
		logger.info("setter:"+an);
		if (an == null || an.length() > scReqData_TMAgentNumber_Len) {
			logger.error("an is null or len wrong");
			return false;
		}
		
		for (int i = 0; i < an.length(); i ++) {
			if (an.charAt(i) < '0' || an.charAt(i) > '9') {
				return false;
			}
		}
		
		int j = an.length() - 1;
		for (int i = scReqData_TMAgentNumber_Len - 1; i >= 0; i --) {
			if (j >= 0) {
				mRequest[scReqData_TMAgentNumber + i] = (byte) an.charAt(j --);
			} else {
				mRequest[scReqData_TMAgentNumber + i] = '0';
			}
		}
		
		mReqDirty = true;
		
		return true;
	}
	
	public String GetReq_TMAgentNumber() {
		byte[] b = Arrays.copyOfRange(mRequest, scReqData_TMAgentNumber, 
										scReqData_TMAgentNumber + scReqData_TMAgentNumber_Len);
		String result = new String(b); 
		return result;
	}
	
	/* TXN Date Time, 4 bytes, TM, 交易日期, Unsigned and LSB First (UnixDateTime) */
	private static final int scReqData_TXNDateTime = scReqData_TMAgentNumber + scReqData_TMAgentNumber_Len;
	private static final int scReqData_TXNDateTime_Len = 4;
	public void SetReq_TXNDateTime(int unixTimeStamp, String timeZone) {
		
		logger.info("setter: intUnixTime("+unixTimeStamp+"),timeZone:"+timeZone);
		TimeZone tz = TimeZone.getTimeZone(timeZone);
		int offset = tz.getOffset(unixTimeStamp * 1000L);
		unixTimeStamp += offset / 1000;
		
		mRequest[scReqData_TXNDateTime] = (byte) (unixTimeStamp & 0x000000FF);
		mRequest[scReqData_TXNDateTime + 1] = (byte) ((unixTimeStamp & 0x0000FF00) >> 8);
		mRequest[scReqData_TXNDateTime + 2] = (byte) ((unixTimeStamp & 0x00FF0000) >> 16);
		mRequest[scReqData_TXNDateTime + 3] = (byte) ((unixTimeStamp & 0xFF000000) >> 24);
		
		mReqDirty = true;
	}
	
	/* Location ID, 1 byte, 定值, 舊廠站代碼, 由悠遊卡公司指定 */
	private static final int scReqData_LocationID = scReqData_TXNDateTime + scReqData_TXNDateTime_Len;
	private static final int scReqData_LocationID_Len = 1;
	public void SetReq_LocationID(byte id) {
		
		logger.info("setter:0x"+String.format("%02X", id));
		mRequest[scReqData_LocationID] = id;
		
		mReqDirty = true;
	}
	
	/* New Location ID, 定值, 新廠站代碼, Unsigned and LSB First, 由悠遊卡公司指定 */
	private static final int scReqData_NewLocationID = scReqData_LocationID + scReqData_LocationID_Len;
	private static final int scReqData_NewLocationID_Len = 2;
	public void SetReq_NewLocationID(short id) {
		logger.info("setter:"+id);
		mRequest[scReqData_NewLocationID] = (byte) (id & 0x00FF);
		mRequest[scReqData_NewLocationID + 1] = (byte) ((id & 0xFF00) >> 8);
		
		mReqDirty = true;
	}
	
	public byte[] GetReq_NewLocationID() {
		byte data[] = Arrays.copyOfRange(mRequest, scReqData_NewLocationID, 
				scReqData_NewLocationID + scReqData_NewLocationID_Len);
		
		Util.arrayReverse(data);
		logger.info("getter:"+Util.hex2StringLog(data));
		return data;
	
		
	}
	
	/* Service Provider ID, 1 byte, 定值, 舊服務業者代碼, 補0 */
	private static final int scReqData_ServiceProviderID = scReqData_NewLocationID + scReqData_NewLocationID_Len;
	private static final int scReqData_ServiceProviderID_Len = 1;
	
	/* New Service Provider ID, 3 bytes, 定值, 新服務業者代碼, 補0, Unsigned and LSB First, 由悠遊卡公司指定 */
	private static final int scReqData_NewServiceProviderID = scReqData_ServiceProviderID + scReqData_ServiceProviderID_Len;
	private static final int scReqData_NewServiceProviderID_Len = 3;
	public void SetReq_NewServiceProviderID(byte[] bytes) {
		
		logger.info("setter:"+Util.hex2StringLog(bytes));
		if (bytes == null || bytes.length != 3) {
			logger.error("null or len wrong");
			return;
		}
		for (int i = 0; i < bytes.length; i ++) {
			mRequest[scReqData_NewServiceProviderID + i] = bytes[i];
		}
	}
	
	public byte[] GetReq_NewServiceProviderID() {
		byte[] result = Arrays.copyOfRange(mRequest, scReqData_NewServiceProviderID, 
				scReqData_NewServiceProviderID + scReqData_NewServiceProviderID_Len); 
		
		logger.info("getter:"+Util.hex2StringLog(result));
		return result; 
	}
	
	public String GetReq_NewServiceProviderIDByNewDeviceID() {
		int id = mRespond[scRespData_NewDeviceID + 3];
		id += mRespond[scRespData_NewDeviceID + 4] << 8;
		String s = String.format("%08d", id);
		logger.info("getter:"+s);
		
		return s;
		
		
	}
	
	/* 
	 * PPR_Reset_Offline專用參數設定, 1 byte, TM
	 * One Day Quota Write For Micro Payment: Bit 0~1, 小額消費日限額寫入
	 * Check EV Flag For Mifare Only: Bit 2, 檢查餘額旗標
	 * Merchant Limit Use: Bit 3, 小額消費通路限制使用旗標
	 * One Day Quota Flag For Micro Payment: Bit 4~5, 小額消費日限額旗標
	 * Once Quota Flag For Micro Payment: Bit 6, 小額消費次限額旗標 
	 * Pay On Behalf Flag: Bit 7, 是否允許代墊
	 */
	private static final int scReqData_OfflineParams = scReqData_NewServiceProviderID + scReqData_NewServiceProviderID_Len;
	private static final int scReqData_OfflineParams_Len = 1;
	public enum OneDayQuotaWriteForMicroPayment {
		NMNC, // T=Mifare與T-CPU都不寫入
		WMNC, // T=Mifare寫入, T-CPU不寫入
		NMWC, // T=Mifare不寫入, T-CPU寫入
		WNWC  // T=Mifare與T-CPU都寫入
	}
	
	public enum OneDayQuotaFlagForMicroPayment {
		NCNA, // 不檢查, 不累計日限額
		NCYA, // 不檢查, 累計日限額
		YCNA, // 檢查, 不累計日限額
		YCYA, // 檢查, 累計日限額
	}
	/* One Day Quota Write For Micro Payment: Bit 0~1, 小額消費日限額寫入, TM */
	public void SetReq_OneDayQuotaWriteForMicroPayment(OneDayQuotaWriteForMicroPayment e) {
	
		byte b = mRequest[scReqData_OfflineParams];
	
		logger.info("setter:"+e);
		b = ClearBit(b, 0);
		b = ClearBit(b, 1);
		switch (e) {
		case NMNC: // T=Mifare與T-CPU都不寫入
			break;
		case WMNC: // T=Mifare寫入, T-CPU不寫入
			b = SetBit(b, 0);
			break;
		case NMWC: // T=Mifare不寫入, T-CPU寫入
			b = SetBit(b, 1);
			break;
		case WNWC:  // T=Mifare與T-CPU都寫入
			b = SetBit(b, 0);
			b = SetBit(b, 1);
			break;
		}
		
		mRequest[scReqData_OfflineParams] = b;
		mReqDirty = true;
	}
	
	/* Check EV Flag For Mifare Only: Bit 2, 檢查餘額旗標, TM */
	public void SetReq_CheckEVFlagForMifareOnly(boolean bCheck) {
		byte b = mRequest[scReqData_OfflineParams];
		logger.info("setter:"+bCheck);
		if (bCheck) {
			b = SetBit(b, 2);
		} else {
			b = ClearBit(b, 2);
		}
		
		mRequest[scReqData_OfflineParams] = b;
		mReqDirty = true;
	}
	
	/* Merchant Limit Use: Bit 3, 小額消費通路限制使用旗標, TM */
	public void SetReq_MerchantLimitUse(boolean bLimit) {
		byte b = mRequest[scReqData_OfflineParams];
		if (bLimit) {
			b = SetBit(b, 3);
		} else {
			b = ClearBit(b, 3);
		}
		
		mRequest[scReqData_OfflineParams] = b;
		mReqDirty = true;
	}
	
	/* One Day Quota Flag For Micro Payment: Bit 4~5, 小額消費日限額旗標, TM */
	public void SetReq_OneDayQuotaFlagForMicroPayment(OneDayQuotaFlagForMicroPayment e) {
		byte b = mRequest[scReqData_OfflineParams];
		
		b = ClearBit(b, 4);
		b = ClearBit(b, 5);
		
		switch (e) {
		case NCNA: // 不檢查, 不累計日限額
			break;
		case NCYA: // 不檢查, 累計日限額
			b = SetBit(b, 4);
			break;
		case YCNA: // 檢查, 不累計日限額
			b = SetBit(b, 5);
			break;
		case YCYA: // 檢查, 累計日限額
			b = SetBit(b, 4);
			b = SetBit(b, 5);
			break;
		}
		
		mRequest[scReqData_OfflineParams] = b;
		mReqDirty = true;
	}
	
	/* Once Quota Flag For Micro Payment: Bit 6, 小額消費次限額旗標, TM */ 
	public void SetReq_OnceQuotaFlagForMicroPayment(boolean bLimit) {
		byte b = mRequest[scReqData_OfflineParams];
		if (bLimit) {
			b = SetBit(b, 6);
		} else {
			b = ClearBit(b, 6);
		}
		
		mRequest[scReqData_OfflineParams] = b;
		mReqDirty = true;
	}
	
	/* Pay On Behalf Flag: Bit 7, 是否允許代墊, TM */
	public void SetReq_PayOnBehalfFlag(boolean bAllowed) {
		byte b = mRequest[scReqData_OfflineParams];
		if (bAllowed) {
			b = SetBit(b, 7);
		} else {
			b = ClearBit(b, 7);
		}
		
		mRequest[scReqData_OfflineParams] = b;
		mReqDirty = true;
	}
	
	/* One Day Quota For Micro Payment,
	 * 小額消費日限額額度, 2 bytes, TM, Unsigned and LSB First 
	 */
	private static final int scReqData_OneDayQuota_ForMicroPayment = scReqData_OfflineParams + scReqData_OfflineParams_Len;
	private static final int scReqData_OneDayQuota_ForMicroPayment_Len = 2;
	public void SetReq_OneDayQuotaForMicroPayment(short amount) {
		mRequest[scReqData_OneDayQuota_ForMicroPayment] = (byte) (amount & 0x00FF);
		mRequest[scReqData_OneDayQuota_ForMicroPayment + 1] = (byte) ((amount & 0xFF00) >> 8);
		
		mReqDirty = true;
	}
	
	/* Once Quota For Micro Payment,
	 * 小額消費次限額額度, 2 bytes, TM, Unsigned and LSB First 
	 */
	private static final int scReqData_OnceQuota_ForMicroPayment = scReqData_OneDayQuota_ForMicroPayment + scReqData_OneDayQuota_ForMicroPayment_Len;
	private static final int scReqData_OnceQuota_ForMicroPayment_Len = 2;
	public void SetReq_OnceQuotaForMicroPayment(short amount) {
		mRequest[scReqData_OnceQuota_ForMicroPayment] = (byte) (amount & 0x00FF);
		mRequest[scReqData_OnceQuota_ForMicroPayment + 1] = (byte) ((amount & 0xFF00) >> 8);
		
		mReqDirty = true;
	}
	
	/* SAM Slot Control Flag,
	 * SAM卡位置控制旗標, 1 byte, TM 
	 */
	private static final int scReqData_SAMSlotControlFlag = scReqData_OnceQuota_ForMicroPayment + scReqData_OnceQuota_ForMicroPayment_Len;
	private static final int scReqData_SAMSlotControlFlag_Len = 1;
	public boolean SetReq_SAMSlotControlFlag(boolean bGen2, int slot) {
		
		logger.info("Start");
		if (slot < 0 || slot > 15) {
			logger.error("slot("+slot+") wrong");
			return false;
		}
		
		byte b = mRequest[scReqData_SAMSlotControlFlag];
		if (bGen2) {
			b = (byte) (b & 0xF0);
			b += slot;
		} else {
			b = (byte) (b & 0x0F);
			slot = (slot << 4);
			b = (byte) (b | slot);
		}
		mRequest[scReqData_SAMSlotControlFlag] = b;
		mReqDirty = true;
		
		logger.info("End");
		return true;
	}
	
	/* RFU (Reserved For Use), 保留, TM */
	@SuppressWarnings("unused")
	private static final int scReqData_RFU = scReqData_SAMSlotControlFlag + scReqData_SAMSlotControlFlag_Len;
	@SuppressWarnings("unused")
	private static final int scReqData_RFU_Len = 11;
	
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
	
	/* Spec. Version Number, 1 byte, Reader, Host辨識版號, 定值0x01 */
	private static final int scRespData_SpecVersionNumber = scRespDataOffset;
	private static final int scRespData_SpecVersionNumber_Len = 1;
	public byte GetResp_SpecVersionNumber() {
		if (mRespond == null) {
			return 0;
		}
		return mRespond[scRespData_SpecVersionNumber];
	}
	
	/* Reader ID, 4 bytes, Reader, 讀卡機編號 */
	private static final int scRespData_ReaderID = scRespData_SpecVersionNumber + scRespData_SpecVersionNumber_Len;
	private static final int scRespData_ReaderID_Len = 4;
	public byte[] GetResp_ReaderID() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_ReaderID, scRespData_ReaderID + scRespData_ReaderID_Len);
	}
	
	/* Reader FW Version, 6 bytes, Reader, 讀卡機韌體版本 */
	private static final int scRespData_ReaderFWVersion = scRespData_ReaderID + scRespData_ReaderID_Len;
	private static final int scRespData_ReaderFWVersion_Len = 6;
	public byte[] GetResp_ReaderFWVersion() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_ReaderFWVersion, scRespData_ReaderFWVersion + scRespData_ReaderFWVersion_Len);
	}
	
	/* SAM ID, 8 bytes, 舊SAM Card, SAM編號, Unsigned and MSB First, P2=0x01時補0x00 */
	private static final int scRespData_SAMID = scRespData_ReaderFWVersion + scRespData_ReaderFWVersion_Len;
	private static final int scRespData_SAMID_Len = 8;
	public byte[] GetResp_SAMID() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SAMID, scRespData_SAMID + scRespData_SAMID_Len);
	}
	
	/* SAM SN, 4 bytes, 舊SAM Card, SAM使用次數, Unsigned and MSB First, P2=0x01時補0x00 */
	private static final int scRespData_SAMSN = scRespData_SAMID + scRespData_SAMID_Len;
	private static final int scRespData_SAMSN_Len = 4;
	public byte[] GetResp_SAMSN() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SAMSN, scRespData_SAMSN + scRespData_SAMSN_Len);
	}
	
	/* SAM CRN, 8 bytes, 舊SAM Card, SAM產生的Random Number, P2=0x01時補0x00 */
	private static final int scRespData_SAMCRN = scRespData_SAMSN + scRespData_SAMSN_Len;
	private static final int scRespData_SAMCRN_Len = 8;
	public byte[] GetResp_SAMCRN() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SAMCRN, scRespData_SAMCRN + scRespData_SAMCRN_Len);
	}
	
	/* Device ID, 4 bytes, 舊SAM Card, 舊設備編號, Unsigned and LSB First */
	private static final int scRespData_DeviceID = scRespData_SAMCRN + scRespData_SAMCRN_Len;
	private static final int scRespData_DeviceID_Len = 4;
	/* Device ID, 舊設備編號, 舊SAM Card, Unsigned and LSB First, SAM */
	public byte[] GetResp_DeviceID() {
		if (mRespond == null) {
			return null;
		}
		byte[] result = Arrays.copyOfRange(mRespond, scRespData_DeviceID, scRespData_DeviceID + scRespData_DeviceID_Len); 
		
		logger.info("getter:"+Util.bcd2Ascii(result));
		return result;
	}
	
	/* SAM Key Version, 1 byte, 舊SAM Card, SAM金鑰版本 */
	private static final int scRespData_SAMKeyVersion = scRespData_DeviceID + scRespData_DeviceID_Len;
	private static final int scRespData_SAMKeyVersion_Len = 1;
	public byte GetResp_SAMKeyVersion() {
		if (mRespond == null) {
			return 0;
		}
		return mRespond[scRespData_SAMKeyVersion];
	}
	
	/* S-TAC, 8 bytes, 舊SAM Card, SAM認證碼, P2=0x01時補0x00 */
	private static final int scRespData_S_TAC = scRespData_SAMKeyVersion + scRespData_SAMKeyVersion_Len;
	private static final int scRespData_S_TAC_Len = 8;
	/* S-TAC, SAM認證碼, 舊SAM Card, P2=0x01時補0x00, SAM */
	public byte[] GetResp_S_TAC() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_S_TAC, scRespData_S_TAC + scRespData_S_TAC_Len);
	}
	
	/* SAM Version Number, 1 byte, 新SAM Card, SAM版本 */
	private static final int scRespData_SAMVersionNumber = scRespData_S_TAC + scRespData_S_TAC_Len;
	private static final int scRespData_SAMVersionNumber_Len = 1;
	public byte GetResp_SAMVersionNumber() {
		if (mRespond == null) {
			return 0;
		}
		return mRespond[scRespData_SAMVersionNumber];
	}
	
	/* SID, 8 bytes, 新SAM Card, SAM ID, Unsigned and MSB First */
	private static final int scRespData_SAMIDNew = scRespData_SAMVersionNumber + scRespData_SAMVersionNumber_Len;
	private static final int scRespData_SAMIDNew_Len = 8;
	public byte[] GetResp_SAMIDNew() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SAMIDNew, scRespData_SAMIDNew + scRespData_SAMIDNew_Len);
	}
	
	/* SAM Usage Control, 3 bytes, 新SAM Card, SAM控制參數 */
	private static final int scRespData_SAMUsageControl = scRespData_SAMIDNew + scRespData_SAMIDNew_Len;
	private static final int scRespData_SAMUsageControl_Len = 3;
	public byte[] GetResp_SAMUsageControl() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SAMUsageControl, scRespData_SAMUsageControl + scRespData_SAMUsageControl_Len);
	}
	
	/* SAM Admin KVN, 1 bytes, 新SAM Card, SAM Admin Key Version Number */
	private static final int scRespData_SAMAdminKVN = scRespData_SAMUsageControl + scRespData_SAMUsageControl_Len;
	private static final int scRespData_SAMAdminKVN_Len = 1;
	public byte GetResp_SAMAdminKVN() {
		if (mRespond == null) {
			return 0;
		}
		return mRespond[scRespData_SAMAdminKVN];
	}
	
	/* SAM Issuer KVN, 1 bytes, 新SAM Card, SAM Issuer Key Version Number */
	private static final int scRespData_SAMIssuerKVN = scRespData_SAMAdminKVN + scRespData_SAMAdminKVN_Len;
	private static final int scRespData_SAMIssuerKVN_Len = 1;
	public byte GetResp_SAMIssuerKVN() {
		if (mRespond == null) {
			return 0;
		}
		return mRespond[scRespData_SAMIssuerKVN];
	}
	
	/* ACL, 3 bytes, 新SAM Card, Authorized Credit Limit(加值額度預設值), Unsigned and LSB First */
	private static final int scRespData_ACL = scRespData_SAMIssuerKVN + scRespData_SAMIssuerKVN_Len;
	private static final int scRespData_ACL_Len = 3;
	/* ACL, Authorized Credit Limit(加值額度預設值), Unsigned and LSB First, 新SAM Card */
	public byte[] GetResp_ACL() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_ACL, scRespData_ACL + scRespData_ACL_Len);
	}
	
	/* Single Credit TXN AMT Limit, 3 bytes, 新SAM Card, 加值交易金額限額, Unsigned and LSB First */
	private static final int scRespData_SingleCreditTXNAMTLimit = scRespData_ACL + scRespData_ACL_Len;
	private static final int scRespData_SingleCreditTXNAMTLimit_Len = 3;
	public byte[] GetResp_SingleCreditTXNAMTLimit() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SingleCreditTXNAMTLimit, 
				scRespData_SingleCreditTXNAMTLimit + scRespData_SingleCreditTXNAMTLimit_Len);
	}
	
	/* ACB, 3 bytes, 新SAM Card, Authorized Credit Balance(加值授權額度), Unsigned and LSB First */
	private static final int scRespData_ACB = scRespData_SingleCreditTXNAMTLimit + scRespData_SingleCreditTXNAMTLimit_Len;
	private static final int scRespData_ACB_Len = 3;
	public byte[] GetResp_ACB() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_ACB, scRespData_ACB + scRespData_ACB_Len);
	}
	
	/* ACC, 3 bytes, 新SAM Card, Authorized Credit Cumulative(加值累積已用額度), Unsigned and LSB First */
	private static final int scRespData_ACC = scRespData_ACB + scRespData_ACB_Len;
	private static final int scRespData_ACC_Len = 3;
	public byte[] GetResp_ACC() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_ACC, scRespData_ACC + scRespData_ACC_Len);
	}
	
	/* ACCC, 3 bytes, 新SAM Card, Authorized Cancel Credit Cumulative(取消加值累積已用額度), Unsigned and LSB First */
	private static final int scRespData_ACCC = scRespData_ACC + scRespData_ACC_Len;
	private static final int scRespData_ACCC_Len = 3;
	public byte[] GetResp_ACCC() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_ACCC, scRespData_ACCC + scRespData_ACCC_Len);
	}
	
	/* New Device ID, 6 bytes, 新SAM Card, 新設備編號, Unsigned and LSB First */
	/* EasyCard: long的前兩個bytes就是SPID code */
	private static final int scRespData_NewDeviceID = scRespData_ACCC + scRespData_ACCC_Len;
	private static final int scRespData_NewDeviceID_Len = 6;
	public byte[] GetResp_NewDeviceID() {
		if (mRespond == null) {
			return null;
		}
		byte[] result = Arrays.copyOfRange(mRespond, scRespData_NewDeviceID, 
				scRespData_NewDeviceID + scRespData_NewDeviceID_Len); 
		
		logger.debug("getter newDeviceID:"+String.format("(%02X)(%02X)(%02X)(%02X)(%02X)(%02X)", result[0],result[1],result[2],result[3],result[4],result[5]));
		logger.info("getter:"+Util.bcd2Ascii(result));
		
		return result;
	}
	
	/* Tag List Table, 40 bytes, 新SAM Card, Tag控制參數 */
	private static final int scRespData_TagListTable = scRespData_NewDeviceID + scRespData_NewDeviceID_Len;
	private static final int scRespData_TagListTable_Len = 40;
	public byte[] GetResp_TagListTable() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_TagListTable, 
				scRespData_TagListTable + scRespData_TagListTable_Len);
	}
	
	/* SAM Issuer Specific Data, 32 bytes, 新SAM Card, SAM自訂資料 */
	private static final int scRespData_SAMIssuerSpecificData = scRespData_TagListTable + scRespData_TagListTable_Len;
	private static final int scRespData_SAMIssuerSpecificData_Len = 32;
	public byte[] GetResp_SAMIssuerSpecificData() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SAMIssuerSpecificData, 
				scRespData_SAMIssuerSpecificData + scRespData_SAMIssuerSpecificData_Len);
	}
	
	/* STC, SAM TXN Counter, 4 bytes, 新SAM Card, Unsigned and MSB First, P2=0x01時補0x00 */
	private static final int scRespData_STC = scRespData_SAMIssuerSpecificData + scRespData_SAMIssuerSpecificData_Len;
	private static final int scRespData_STC_Len = 4;
	public byte[] GetResp_STC() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_STC, scRespData_STC + scRespData_STC_Len);
	}
	
	/* RSAM, 8 btes, 新SAM Card, SAM產生之Random Number, P2=0x01時補0x00 */
	private static final int scRespData_RSAM = scRespData_STC + scRespData_STC_Len;
	private static final int scRespData_RSAM_Len = 8;
	public byte[] GetResp_RSAM() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_RSAM, scRespData_RSAM + scRespData_RSAM_Len);
	}
	
	/* RHOST, 8 bytes, Reader, Host之Random Number (Reader產生), P2=0x01時補0x00 */
	private static final int scRespData_RHOST = scRespData_RSAM + scRespData_RSAM_Len;
	private static final int scRespData_RHOST_Len = 8;
	public byte[] GetResp_RHOST() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_RHOST, scRespData_RHOST + scRespData_RHOST_Len);
	}
	
	/* SATOKEN, 16 bytes, 新SAM Card, SAM Authentication Token, P2=0x01時補0x00 */
	private static final int scRespData_SATOKEN = scRespData_RHOST + scRespData_RHOST_Len;
	private static final int scRespData_SATOKEN_Len = 16;
	public byte[] GetResp_SATOKEN() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SATOKEN, scRespData_SATOKEN + scRespData_SATOKEN_Len);
	}
	
	/* 
	 * PPR_SignOn參數設定, 1 byte, Reader (適用於有SignOn之設備)
	 * CPD Read Flag: Bit 0~1, 二代CPD讀取及驗證設定
	 * One Day Quota Write For Micro Payment: Bit 2~3, 小額消費日限額寫入
	 * SAM SignOnControl Flag: Bit 4~5, SAM卡SignOn控制旗標
	 * Check EV Flag For Mifare Only: Bit 6, 檢查餘額旗標
	 * Merchant Limit Use For Micro Payment: Bit 7, 小額消費通路限制使用旗標 
	 */
	private static final int scRespData_SignOnParams1 = scRespData_SATOKEN + scRespData_SATOKEN_Len;
	private static final int scRespData_SignOnParams1_Len = 1;
	
	public enum CPDReadFlag {
		NRHostNCReader,		// 不讀回Host且Reader不驗證
		YRHostNCReader, 	// 讀回Host且Reader不驗證
		NRHostYCReader,		// 不讀回Host且Reader要驗證
		YRHostYCReader		// 讀回Host且Reader要驗證
	};
	
	/*
	public enum SAMSignOnControlFlag {
		NoSignOnForBoth,	// 兩張SAM卡都不須SignOn(離線)
		SignOnForNewOnly,	// 只SignOn新SAM卡(淘汰Mifare時)
		SignOnForOldOnly,	// 只SignOn舊SAM卡
		SignOnForBoth		// 兩張SAM卡都要SignOn
	};*/
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, CPD Read Flag: Bit 0~1, 二代CPD讀取及驗證設定 */
	//public CPDReadFlag GetResp_CPDReadFlag() {
	public byte GetResp_CPDReadFlag() {
		if (mRespond == null) {
			//return CPDReadFlag.NRHostNCReader;
			return 0x00;
		}
		
		byte b = (byte) (mRespond[scRespData_SignOnParams1] & 0x03);

		logger.info("getter:"+b);
		return b;
		/*
		switch (b) {	
		case 1: // 讀回Host且Reader不驗證
			return CPDReadFlag.YRHostNCReader;
		case 2: // 不讀回Host且Reader要驗證
			return CPDReadFlag.NRHostYCReader;
		case 3: // 讀回Host且Reader要驗證
			return CPDReadFlag.YRHostYCReader;
		default: // 不讀回Host且Reader不驗證
			return CPDReadFlag.NRHostNCReader;	
		}*/
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, One Day Quota Write For Micro Payment: Bit 2~3, 小額消費日限額寫入 */
	//public OneDayQuotaWriteForMicroPayment GetResp_OneDayQuotaWriteForMicroPayment() {
	public byte GetResp_OneDayQuotaWriteForMicroPayment() {
		if (mRespond == null) {
			return 0x00;
		}  
		byte b = (byte) ((mRespond[scRespData_SignOnParams1] & 0x0C) >> 2);
		logger.info("getter:"+b);
		return b;
		/*
		switch (b) {
		case 1: // T=Mifare寫入, T-CPU不寫入
			return OneDayQuotaWriteForMicroPayment.WMNC;
		case 2: // T=Mifare不寫入, T-CPU寫入
			return OneDayQuotaWriteForMicroPayment.NMWC;
		case 3: // T=Mifare與T-CPU都寫入
			return OneDayQuotaWriteForMicroPayment.WNWC;
		default: // T=Mifare與T-CPU都不寫入
			return OneDayQuotaWriteForMicroPayment.NMNC;	
		}
		*/
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, SAM SignOnControl Flag: Bit 4~5, SAM卡SignOn控制旗標 */
	//public SAMSignOnControlFlag GetResp_SAMSignOnControlFlag() {
	public byte GetResp_SAMSignOnControlFlag() {
		if (mRespond == null) {
			return 0x00;//SAMSignOnControlFlag.NoSignOnForBoth;
		}
		byte b = (byte) ((mRespond[scRespData_SignOnParams1] & 0x30) >> 4);
		logger.info("getter:"+b);
		return b;
		/*
		switch (b) {
		case 1: // 只SignOn新SAM卡(淘汰Mifare時)
			return SAMSignOnControlFlag.SignOnForNewOnly;
		case 2: // 只SignOn舊SAM卡
			return SAMSignOnControlFlag.SignOnForOldOnly;
		case 3: // 兩張SAM卡都要SignOn
			return SAMSignOnControlFlag.SignOnForBoth;
		default: // 兩張SAM卡都不須SignOn(離線)
			return SAMSignOnControlFlag.NoSignOnForBoth;	
		}*/
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Check EV Flag For Mifare Only: Bit 6, 檢查餘額旗標 */
	public byte GetResp_CheckEVFlagForMifareOnly() {
		if (mRespond == null) {
			return 0x00;
		}
		byte b = (byte) ((mRespond[scRespData_SignOnParams1] & 0x40) >> 6);
		
		return b;
		/*
		if (b == 0) {
			return true;
		} else {
			return false;
		}*/
	}
	/*
	//add by bruce
	public byte bGetResp_CheckEVFlagForMifareOnly() {
		if (mRespond == null) {
			return 0x00;
		}
		byte b = (byte) (mRespond[scRespData_SignOnParams1] & 0x40);
		if (b == 0) {
			return 0x01;
		} else {
			return 0x00;
		}
	}*/
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Merchant Limit Use For Micro Payment: Bit 7, 小額消費通路限制使用旗標 */
	public byte GetResp_MerchantLimitUseForMicroPayment() {
		if (mRespond == null) {
			return 0x00;
		}
		byte b = (byte) ((mRespond[scRespData_SignOnParams1] & 0x80) >> 7);
		return b;
		
		/*
		if (b == 0) {
			return true; // 限制使用
		} else { 
			return false; // 不限制使用
		}*/
	}
	
	/* 
	 * PPR_SignOn參數設定, 1 byte, Reader (適用於有SignOn之設備)
	 * One Day Quota Flag For Micro Payment: Bit 0~1, 小額消費日限額旗標
	 * Once Quota Flag For Micro Payment: Bit 2, 小額消費次限額旗標
	 * Check Debit Flag: Bit 3, 扣值交易合法驗證旗標
	 * Mifare Check Enable Flag: Bit 4, 二代卡Level 1
	 * Pay On Behalf Flag: Bit 5, 是否允許代墊
	 * RFU: Bit 6~7, 保留 
	 */
	private static final int scRespData_SignOnParams2 = scRespData_SignOnParams1 + scRespData_SignOnParams1_Len;
	private static final int scRespData_SignOnParams2_Len = 1;
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, One Day Quota Flag For Micro Payment: Bit 0~1, 小額消費日限額旗標 */
	//public OneDayQuotaFlagForMicroPayment GetResp_OneDayQuotaFlagForMicroPayment() {
	public byte GetResp_OneDayQuotaFlagForMicroPayment() {
		if (mRespond == null) {
			return 0x00;//OneDayQuotaFlagForMicroPayment.NCNA;
		}  
		byte b = (byte) (mRespond[scRespData_SignOnParams2] & 0x03);
		logger.info("getter:"+b);
		return b;
		/*
		switch (b) {
		case 1: // 不檢查, 累計日限額
			return OneDayQuotaFlagForMicroPayment.NCYA;
		case 2: // 檢查, 不累計日限額
			return OneDayQuotaFlagForMicroPayment.YCNA;
		case 3: // 檢查, 累計日限額
			return OneDayQuotaFlagForMicroPayment.YCYA;
		default: // 不檢查, 不累計日限額
			return OneDayQuotaFlagForMicroPayment.NCNA;	
		}*/
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Once Quota Flag For Micro Payment: Bit 2, 小額消費次限額旗標 */
	public byte GetResp_OnceQuotaFlagForMicroPayment() {
		if (mRespond == null) {
			return 0x00;
		}  
		
		
		byte b = (byte) ((mRespond[scRespData_SignOnParams2] & 0x04) >> 2);
		
		logger.debug("getter:orig:"+String.format("%02X", mRespond[scRespData_SignOnParams2])+",bit:"+String.format("%02X", b));
		return b;
		/*
		if (b == 0) {
			return false; // 不限制次限額
		} else {
			return true; // 限制次限額
		}*/
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Check Debit Flag: Bit 3, 扣值交易合法驗證旗標 */
	public byte GetResp_CheckDebitFlag() {
		if (mRespond == null) {
			return 0x00;
		}  
		byte b = (byte) ((mRespond[scRespData_SignOnParams2] & 0x08) >> 3);
		return b;
		/*
		if (b == 0) {
			return false; // 不限制扣值交易合法驗證金額
		} else {
			return true; // 限制扣值交易合法驗證金額
		}*/
	}
	/*
	//add by bruce
	public byte bGetResp_CheckDebitFlag() {
		if (mRespond == null) {
			return 0x00;
		}  
		byte b = (byte) (mRespond[scRespData_SignOnParams2] & 0x08);
		if (b == 0) {
			return 0x00; // 不限制扣值交易合法驗證金額
		} else {
			return 0x01; // 限制扣值交易合法驗證金額
		}
	}*/
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Mifare Check Enable Flag: Bit 4, 二代卡Level 1 */
	public byte GetResp_MifareCheckEnableFlag() {
		if (mRespond == null) {
			return 0x00;
		}  
		byte b = (byte) ((mRespond[scRespData_SignOnParams2] & 0x10) >> 4);
		return b;
		/*
		if (b == 0) {
			return false; // 不啟動Mifare餘額檢查
		} else {
			return true; // 啟動Mifare餘額檢查
		}*/
	}
	
	/* PPR_SignOn參數設定, 適用於有SignOn之設備, Pay On Behalf Flag: Bit 5, 是否允許代墊 */
	public boolean GetResp_PayOnBehalfFlag() {
		if (mRespond == null) {
			return false;
		}  
		byte b = (byte) (mRespond[scRespData_SignOnParams2] & 0x20);
		if (b == 0) {
			return false; // 不允許代墊
		} else {
			return true; // 允許代墊一次
		}
	}
	
	/* One Day Quota For Micro Payment, 2 bytes, Reader (適用於有SignOn之設備), 小額消費日限額額度, Unsigned and LSB First */
	private static final int scRespData_OneDayQuotaForMicroPayment = scRespData_SignOnParams2 + scRespData_SignOnParams2_Len;
	private static final int scRespData_OneDayQuotaForMicroPayment_Len = 2;
	public byte[] GetResp_OneDayQuotaForMicroPayment() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_OneDayQuotaForMicroPayment, 
				scRespData_OneDayQuotaForMicroPayment + scRespData_OneDayQuotaForMicroPayment_Len);
	}
	
	/* Once Quota For Micro Payment, 2 bytes, Reader (適用於有SignOn之設備), 小額消費次限額額度, Unsigned and LSB First */
	private static final int scRespData_OnceQuotaForMicroPayment = scRespData_OneDayQuotaForMicroPayment + scRespData_OneDayQuotaForMicroPayment_Len;
	private static final int scRespData_OnceQuotaForMicroPayment_Len = 2;
	public byte[] GetResp_OnceQuotaForMicroPayment() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_OnceQuotaForMicroPayment, 
				scRespData_OnceQuotaForMicroPayment + scRespData_OnceQuotaForMicroPayment_Len);
	}
	
	/* Check Debit Value, 2 bytes, Reader (適用於有SignOn之設備), 扣值交易合法驗證金額, Unsigned and LSB First */
	private static final int scRespData_CheckDebitValue = scRespData_OnceQuotaForMicroPayment + scRespData_OnceQuotaForMicroPayment_Len;
	private static final int scRespData_CheckDebitValue_Len = 2;
	public byte[] GetResp_CheckDebitValue() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_CheckDebitValue, 
				scRespData_CheckDebitValue + scRespData_CheckDebitValue_Len);
	}
	
	/* Add Quota Flag, 1 byte, Reader (適用於舊的額度控管), 加值額度控管旗標 */
	private static final int scRespData_AddQuotaFlag = scRespData_CheckDebitValue + scRespData_CheckDebitValue_Len;
	private static final int scRespData_AddQuotaFlag_Len = 1;
	public byte GetResp_AddQuotaFlag() {
		if (mRespond == null) {
			return 0x00;
		}
		return mRespond[scRespData_AddQuotaFlag];
		/*
		if (mRespond[scRespData_AddQuotaFlag] == 0) {
			return false; // 不檢查額度
		} else {
			return true; // 檢查額度
		}*/
	}
	
	/*
	//add by bruce
	public byte bGetResp_AddQuotaFlag() {
		if (mRespond == null) {
			return 0x00;
		}
		
		if (mRespond[scRespData_AddQuotaFlag] == 0) {
			return 0x00; // 不檢查額度
		} else {
			return 0x01; // 檢查額度
		}
	}
	*/
	/* Add Quota, 3 bytes, Reader (適用於舊的額度控管), 加值額度, Unsigned and LSB First */
	private static final int scRespData_AddQuota = scRespData_AddQuotaFlag + scRespData_AddQuotaFlag_Len;
	private static final int scRespData_AddQuota_Len = 3;
	public byte[] GetResp_AddQuota() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_AddQuota, 
				scRespData_AddQuota + scRespData_AddQuota_Len);
	}
	
	/* The Remainder of Add Quota, 3 bytes, Reader (適用於舊的額度控管), 剩餘加值額度, Unsigned and LSB First */
	private static final int scRespData_RemainderofAddQuota = scRespData_AddQuota + scRespData_AddQuota_Len;
	private static final int scRespData_RemainderofAddQuota_Len = 3;
	public byte[] GetResp_RemainderofAddQuota() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_RemainderofAddQuota, 
				scRespData_RemainderofAddQuota + scRespData_RemainderofAddQuota_Len);
	}
	
	/* Cancel Credit Quota, 3 bytes, Reader (適用於舊的額度控管), 取消加值累計額度, Unsigned and LSB First */
	private static final int scRespData_CancelCreditQuota = scRespData_RemainderofAddQuota + scRespData_RemainderofAddQuota_Len;
	private static final int scRespData_CancelCreditQuota_Len = 3;
	public byte[] GetResp_CancelCreditQuota() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_CancelCreditQuota, 
				scRespData_CancelCreditQuota + scRespData_CancelCreditQuota_Len);
	}
	
	/* deMAC Parameter, 8 bytes, Reader (適用於舊的SAM Card), 某些超商Dongle deMAC需要用到此參數, P2=0x01時補0x00 */
	private static final int scRespData_deMACParameter = scRespData_CancelCreditQuota + scRespData_CancelCreditQuota_Len;
	private static final int scRespData_deMACParameter_Len = 8;
	public byte[] GetResp_deMACParameter() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_deMACParameter, 
				scRespData_deMACParameter + scRespData_deMACParameter_Len);		
	}
	
	/* Last TXN Date Time, 4 bytes, Reader, 最後一次Mutual Authentication成功時間(UnixDateTime) */
	private static final int scRespData_LastTXNDateTime = scRespData_deMACParameter + scRespData_deMACParameter_Len;
	private static final int scRespData_LastTXNDateTime_Len = 4;
	public int GetResp_LastTXNDateTime() {
		if (mRespond == null) {
			return 0;
		}
		return Util.sByteToInt(mRespond, scRespData_LastTXNDateTime, true);
	}
	//add by bruce
	public byte[] bGetResp_LastTXNDateTime() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_LastTXNDateTime, 
				scRespData_LastTXNDateTime +scRespData_LastTXNDateTime_Len);
	
	}
	/* Previous New Device ID, 6 bytes, Reader, 新設備編號, 
	 * 新SAM Card SignOn補confirm之用, 含成功或失敗, 
	 * Unsigned and LSB First, P2=0x01時補0x00 
	 */
	private static final int scRespData_PreviousNewDeviceID = scRespData_LastTXNDateTime + scRespData_LastTXNDateTime_Len;
	private static final int scRespData_PreviousNewDeviceID_Len = 6;
	public byte[] GetResp_PreviousNewDeviceID() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_PreviousNewDeviceID, 
				scRespData_PreviousNewDeviceID + scRespData_PreviousNewDeviceID_Len);
	}
	
	/* Previous STC, 4 bytes, Reader, SAM TXN Counter, 
	 * 新SAM Card SignOn補confirm之用, 含成功或失敗, 
	 * Unsigned and MSB First, P2=0x01時補0x00 
	 */
	private static final int scRespData_PreviousSTC = scRespData_PreviousNewDeviceID + scRespData_PreviousNewDeviceID_Len;
	private static final int scRespData_PreviousSTC_Len = 4;
	public byte[] GetResp_PreviousSTC() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_PreviousSTC, 
				scRespData_PreviousSTC + scRespData_PreviousSTC_Len);
	}
	
	/* Previous TXN Date Time, 4 bytes, Reader 交易日期時間, 
	 * 新SAM Card SignOn補confirm之用, 含成功或失敗, 
	 * UnixDateTime, Unsigned and LSB First, P2=0x01時補0x00 
	 */
	private static final int scRespData_PreviousTXNDateTime = scRespData_PreviousSTC + scRespData_PreviousSTC_Len;
	private static final int scRespData_PreviousTXNDateTime_Len = 4;
	public byte[] GetResp_PreviousTXNDateTime() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_PreviousTXNDateTime, 
				scRespData_PreviousTXNDateTime + scRespData_PreviousTXNDateTime_Len);
	}
	
	/* Previous Credit Balance Change Flag, 1 byte, Reader, 加值授權額度(ACB)變更旗標, 
	 * 新SAM Card SignOn補confirm之用, 含成功或失敗, 
	 * UnixDateTime, Unsigned and LSB First, P2=0x01時補0x00 
	 */
	private static final int scRespData_PreviousCreditBalanceChangeFlag = scRespData_PreviousTXNDateTime + scRespData_PreviousTXNDateTime_Len;
	private static final int scRespData_PreviousCreditBalanceChangeFlag_Len = 1;
	public boolean GetResp_PreviousCreditBalanceChangeFlag() {
		if (mRespond == null) {
			return false;
		}
		
		if (mRespond[scRespData_PreviousCreditBalanceChangeFlag] == 0) {
			return false; // 未變更
		} else {
			return true; // 額度有變更
		}
	}
	//add by bruce
	public byte bGetResp_PreviousCreditBalanceChangeFlag() {
		if (mRespond == null) {
			return 0x00;
		}
		
		if (mRespond[scRespData_PreviousCreditBalanceChangeFlag] == 0) {
			return 0x00; // 未變更
		} else {
			return 0x01; // 額度有變更
		}
	}
	/* Previous Confirm Code, 2 bytes, Reader, Status Code1 + Status Code2, 
	 * 新SAM Card SignOn補confirm之用, 含成功或失敗, 
	 * P2=0x01時補0x00 
	 */
	private static final int scRespData_PreviousConfirmCode = scRespData_PreviousCreditBalanceChangeFlag + scRespData_PreviousCreditBalanceChangeFlag_Len;
	private static final int scRespData_PreviousConfirmCode_Len = 2;
	public byte[] GetResp_PreviousConfirmCode() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_PreviousConfirmCode, 
				scRespData_PreviousConfirmCode + scRespData_PreviousConfirmCode_Len);
	}

	/* Previous CACrypto, 16 bytes, Reader, Credit Authentication Cryptogram, 
	 * 新SAM Card SignOn補confirm之用, 含成功或失敗, 
	 * 額度變更失敗或未變更或P2=0x01時補0x00 
	 */
	private static final int scRespData_PreviousCACrypto = scRespData_PreviousConfirmCode + scRespData_PreviousConfirmCode_Len;
	private static final int scRespData_PreviousCACrypto_Len = 16; 
	public byte[] GetResp_PreviousCACrypto() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_PreviousCACrypto, 
				scRespData_PreviousCACrypto + scRespData_PreviousCACrypto_Len);
	}
	
	@Override
	public byte[] GetRespond() {
		return mRespond;
	}

	@Override
	public boolean SetRespond(byte[] bytes) {
		int length = bytes.length;
		
		if (scRespLength != length) {
			// invalid respond format... 
			return false;
		}
		
		if (bytes[2] != (byte) (scRespDataLength + 2)) { // Data + SW1 + SW2
			// invalid data format...
			return false;
		}
		
		byte sum = getEDC(bytes, length);
		if (sum != bytes[scRespLength - 1]) {
			// check sum error...
			return false;
		}
		
		mRespond = Arrays.copyOf(bytes, length);
		
		int dataLength = mRespond[2] & 0x000000FF;
		Resp_SW1 = mRespond[scRespDataOffset + dataLength - 2];
		Resp_SW2 = mRespond[scRespDataOffset + dataLength + 1 - 2];
		
		
		return true;
	}

	@Override
	public void debugResponseData() {
		// TODO Auto-generated method stub
		logger.debug("pprReset recv:" + Util.hex2StringLog(mRespond));
		
	}

	// for debug only...
	/*
	public void DumpRespData(TextView txt) {
		txt.append("PPR_Reset (" + scDescription + ") Respond Data: \n");
		txt.append("SW1 = " + String.format("%02X", GetRespStatus1()) + "\n");
		txt.append("SW2 = " + String.format("%02X", GetRespStatus2()) + "\n");
		
		txt.append("Spec. Version Number = " + GetResp_SpecVersionNumber() + "\n");
		txt.append("Reader ID = " + Util.sGetHexString(GetResp_ReaderID()) + "\n");
		txt.append("Reader FW Version = " + Util.sGetHexString(GetResp_ReaderFWVersion()) + "\n");
		txt.append("SAM ID = " + Util.sGetHexString(GetResp_SAMID()) + "\n");
		txt.append("SAM SN = " + Util.sGetHexString(GetResp_SAMSN()) + "\n");
		txt.append("SAM CRN = " + Util.sGetHexString(GetResp_SAMCRN()) + "\n");
		txt.append("Device ID = " + Util.sGetHexString(GetResp_DeviceID()) + "\n");
		txt.append("SAM Key Version = " + String.format("%X", GetResp_SAMKeyVersion()) + "\n");
		txt.append("S-TAC = " + Util.sGetHexString(GetResp_S_TAC()) + "\n");
		txt.append("SAM Verion Number = " + String.format("%X", GetResp_SAMVersionNumber()) + "\n");
		txt.append("SAM ID = " + String.format("%08X", GetResp_SAMIDNew()) + "\n");
		
		txt.append("SAM Usage Control = " + Util.sGetHexString(GetResp_SAMUsageControl()) + "\n");
		txt.append("SAM Usage Control = " + Util.sGetHexString(GetResp_SAMUsageControl()) + "\n");
		txt.append("SAM Admin Key Version Number = " + String.format("%X", GetResp_SAMAdminKVN()) + "\n");
		txt.append("SAM Issuer Key Version Number = " + String.format("%X", GetResp_SAMIssuerKVN()) + "\n");
		txt.append("New Device ID = " + Util.sGetHexString(GetResp_NewDeviceID()) + "\n");
	}
	*/
}
