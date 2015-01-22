package Reader;



import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;

//import exception.RespCode;

import Utilities.*;

public class PPR_Reset extends APDU {

	static Logger logger = Logger.getLogger(PPR_Reset.class);
	public static final String scDescription = "���mReader, �N��l�ưѼƶǵ�Reader, �è��oSAToken/S-TAC"; 
	
	private static PPR_Reset sThis = null;
	
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
	
	/* PPR_Reset_Offline, (���u)���sReader, �N��l�ưѼƶǵ�Reader */
	public void SetOffline(boolean bOffline) {
		
		logger.info("setter:"+bOffline);
		if (bOffline) {
			mRequest[6] = Req_P2 = 0x01;
		} else {
			mRequest[6] = Req_P2 = 0x00;
		}
		mReqDirty = true;
	}
	
	/* TM Location ID, 10 bytes, TM, �׺ݾ�(TM)����, ASCII, �k�a����0 */
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
	
	/* TM ID, 2 bytes, TM, �׺ݾ�(TM)����, ASCII, �k�a����0 */
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
	
	/* TM TXN Date Time, 14 bytes, TM, �׺ݾ�(TM)�������ɶ�, ASCII, YYYYMMDDhhmmss */
	private static final int scReqData_TMTXNDateTime = scReqData_TMID + scReqData_TMID_Len;
	private static final int scReqData_TMTXNDateTime_Len = 14;
	public void SetReq_TMTXNDateTime(int unixTimeStamp) {
		String dateTime = Util.sGetDateTime(unixTimeStamp);
		
		logger.info("setter: int("+unixTimeStamp+"), format:"+dateTime);
		for (int i = 0; i < scReqData_TMTXNDateTime_Len; i ++) {
			mRequest[scReqData_TMTXNDateTime + i] = (byte) dateTime.charAt(i);
		}
		mReqDirty = true;
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
	
	/* TM Serial Number, 6 bytes, TM, �׺ݾ�(TM)����Ǹ�, ASCII, �a�k����0, �ȶ���0~9, ������\�ɶi��, ���Ѯɤ��i�� */
	private static final int scReqData_TMSerialNumber = scReqData_TMTXNDateTime + scReqData_TMTXNDateTime_Len;
	private static final int scReqData_TMSerialNumber_Len = 6;
	public boolean SetReq_TMSerialNumber(int sn) {
		String s = String.format("%06d", sn);
		
		logger.info("setter:"+s);
		for (int i = 0; i < s.length(); i ++) {
			mRequest[scReqData_TMSerialNumber + i] = (byte) s.charAt(i);
		}
		
		mReqDirty = true;
		
		return true;
	}
	/*
	public byte[] GetReq_TMSerialNumber() {
		return Arrays.copyOfRange(mRequest, scReqData_TMSerialNumber, scReqData_TMSerialNumber + scReqData_TMSerialNumber_Len);
	}
	*/
	
	/* TM Agent Number, 4 bytes, TM, �׺ݾ�(TM)���ȭ��N��, ASCII, �a�k����0, �ȶ���0~9 */
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
	
	/* TXN Date Time, 4 bytes, TM, ������, Unsigned and LSB First (UnixDateTime) */
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
	
	/* Location ID, 1 byte, �w��, �¼t���N�X, �ѱy�C�d���q���w */
	private static final int scReqData_LocationID = scReqData_TXNDateTime + scReqData_TXNDateTime_Len;
	private static final int scReqData_LocationID_Len = 1;
	public void SetReq_LocationID(byte id) {
		
		logger.info("setter:0x"+String.format("%02X", id));
		mRequest[scReqData_LocationID] = id;
		
		mReqDirty = true;
	}
	
	/* New Location ID, �w��, �s�t���N�X, Unsigned and LSB First, �ѱy�C�d���q���w */
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
		
		logger.info("getter:"+DataFormat.hex2StringLog(data));
		return data;
	
		
	}
	
	/* Service Provider ID, 1 byte, �w��, �ªA�ȷ~�̥N�X, ��0 */
	private static final int scReqData_ServiceProviderID = scReqData_NewLocationID + scReqData_NewLocationID_Len;
	private static final int scReqData_ServiceProviderID_Len = 1;
	
	/* New Service Provider ID, 3 bytes, �w��, �s�A�ȷ~�̥N�X, ��0, Unsigned and LSB First, �ѱy�C�d���q���w */
	private static final int scReqData_NewServiceProviderID = scReqData_ServiceProviderID + scReqData_ServiceProviderID_Len;
	private static final int scReqData_NewServiceProviderID_Len = 3;
	public void SetReq_NewServiceProviderID(byte[] bytes) {
		
		logger.info("setter:"+DataFormat.hex2StringLog(bytes));
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
		
		logger.info("getter:"+DataFormat.hex2StringLog(result));
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
	 * PPR_Reset_Offline�M�ΰѼƳ]�w, 1 byte, TM
	 * One Day Quota Write For Micro Payment: Bit 0~1, �p�B���O�魭�B�g�J
	 * Check EV Flag For Mifare Only: Bit 2, �ˬd�l�B�X��
	 * Merchant Limit Use: Bit 3, �p�B���O�q������ϥκX��
	 * One Day Quota Flag For Micro Payment: Bit 4~5, �p�B���O�魭�B�X��
	 * Once Quota Flag For Micro Payment: Bit 6, �p�B���O�����B�X�� 
	 * Pay On Behalf Flag: Bit 7, �O�_���\�N��
	 */
	private static final int scReqData_OfflineParams = scReqData_NewServiceProviderID + scReqData_NewServiceProviderID_Len;
	private static final int scReqData_OfflineParams_Len = 1;
	public enum OneDayQuotaWriteForMicroPayment {
		NMNC, // T=Mifare�PT-CPU�����g�J
		WMNC, // T=Mifare�g�J, T-CPU���g�J
		NMWC, // T=Mifare���g�J, T-CPU�g�J
		WNWC  // T=Mifare�PT-CPU���g�J
	}
	
	public enum OneDayQuotaFlagForMicroPayment {
		NCNA, // ���ˬd, ���֭p�魭�B
		NCYA, // ���ˬd, �֭p�魭�B
		YCNA, // �ˬd, ���֭p�魭�B
		YCYA, // �ˬd, �֭p�魭�B
	}
	/* One Day Quota Write For Micro Payment: Bit 0~1, �p�B���O�魭�B�g�J, TM */
	public void SetReq_OneDayQuotaWriteForMicroPayment(OneDayQuotaWriteForMicroPayment e) {
	
		byte b = mRequest[scReqData_OfflineParams];
	
		logger.info("setter:"+e);
		b = ClearBit(b, 0);
		b = ClearBit(b, 1);
		switch (e) {
		case NMNC: // T=Mifare�PT-CPU�����g�J
			break;
		case WMNC: // T=Mifare�g�J, T-CPU���g�J
			b = SetBit(b, 0);
			break;
		case NMWC: // T=Mifare���g�J, T-CPU�g�J
			b = SetBit(b, 1);
			break;
		case WNWC:  // T=Mifare�PT-CPU���g�J
			b = SetBit(b, 0);
			b = SetBit(b, 1);
			break;
		}
		
		mRequest[scReqData_OfflineParams] = b;
		mReqDirty = true;
	}
	
	/* Check EV Flag For Mifare Only: Bit 2, �ˬd�l�B�X��, TM */
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
	
	/* Merchant Limit Use: Bit 3, �p�B���O�q������ϥκX��, TM */
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
	
	/* One Day Quota Flag For Micro Payment: Bit 4~5, �p�B���O�魭�B�X��, TM */
	public void SetReq_OneDayQuotaFlagForMicroPayment(OneDayQuotaFlagForMicroPayment e) {
		byte b = mRequest[scReqData_OfflineParams];
		
		b = ClearBit(b, 4);
		b = ClearBit(b, 5);
		
		switch (e) {
		case NCNA: // ���ˬd, ���֭p�魭�B
			break;
		case NCYA: // ���ˬd, �֭p�魭�B
			b = SetBit(b, 4);
			break;
		case YCNA: // �ˬd, ���֭p�魭�B
			b = SetBit(b, 5);
			break;
		case YCYA: // �ˬd, �֭p�魭�B
			b = SetBit(b, 4);
			b = SetBit(b, 5);
			break;
		}
		
		mRequest[scReqData_OfflineParams] = b;
		mReqDirty = true;
	}
	
	/* Once Quota Flag For Micro Payment: Bit 6, �p�B���O�����B�X��, TM */ 
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
	
	/* Pay On Behalf Flag: Bit 7, �O�_���\�N��, TM */
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
	 * �p�B���O�魭�B�B��, 2 bytes, TM, Unsigned and LSB First 
	 */
	private static final int scReqData_OneDayQuota_ForMicroPayment = scReqData_OfflineParams + scReqData_OfflineParams_Len;
	private static final int scReqData_OneDayQuota_ForMicroPayment_Len = 2;
	public void SetReq_OneDayQuotaForMicroPayment(short amount) {
		mRequest[scReqData_OneDayQuota_ForMicroPayment] = (byte) (amount & 0x00FF);
		mRequest[scReqData_OneDayQuota_ForMicroPayment + 1] = (byte) ((amount & 0xFF00) >> 8);
		
		mReqDirty = true;
	}
	
	/* Once Quota For Micro Payment,
	 * �p�B���O�����B�B��, 2 bytes, TM, Unsigned and LSB First 
	 */
	private static final int scReqData_OnceQuota_ForMicroPayment = scReqData_OneDayQuota_ForMicroPayment + scReqData_OneDayQuota_ForMicroPayment_Len;
	private static final int scReqData_OnceQuota_ForMicroPayment_Len = 2;
	public void SetReq_OnceQuotaForMicroPayment(short amount) {
		mRequest[scReqData_OnceQuota_ForMicroPayment] = (byte) (amount & 0x00FF);
		mRequest[scReqData_OnceQuota_ForMicroPayment + 1] = (byte) ((amount & 0xFF00) >> 8);
		
		mReqDirty = true;
	}
	
	/* SAM Slot Control Flag,
	 * SAM�d��m����X��, 1 byte, TM 
	 */
	private static final int scReqData_SAMSlotControlFlag = scReqData_OnceQuota_ForMicroPayment + scReqData_OnceQuota_ForMicroPayment_Len;
	private static final int scReqData_SAMSlotControlFlag_Len = 1;
	public boolean SetReq_SAMSlotControlFlag(boolean bGen2, int slot) {
		if (slot < 0 || slot > 15) {
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
		
		return true;
	}
	
	/* RFU (Reserved For Use), �O�d, TM */
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
	
	/* Spec. Version Number, 1 byte, Reader, Host���Ѫ���, �w��0x01 */
	private static final int scRespData_SpecVersionNumber = scRespDataOffset;
	private static final int scRespData_SpecVersionNumber_Len = 1;
	public byte GetResp_SpecVersionNumber() {
		if (mRespond == null) {
			return 0;
		}
		return mRespond[scRespData_SpecVersionNumber];
	}
	
	/* Reader ID, 4 bytes, Reader, Ū�d���s�� */
	private static final int scRespData_ReaderID = scRespData_SpecVersionNumber + scRespData_SpecVersionNumber_Len;
	private static final int scRespData_ReaderID_Len = 4;
	public byte[] GetResp_ReaderID() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_ReaderID, scRespData_ReaderID + scRespData_ReaderID_Len);
	}
	
	/* Reader FW Version, 6 bytes, Reader, Ū�d�����骩�� */
	private static final int scRespData_ReaderFWVersion = scRespData_ReaderID + scRespData_ReaderID_Len;
	private static final int scRespData_ReaderFWVersion_Len = 6;
	public byte[] GetResp_ReaderFWVersion() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_ReaderFWVersion, scRespData_ReaderFWVersion + scRespData_ReaderFWVersion_Len);
	}
	
	/* SAM ID, 8 bytes, ��SAM Card, SAM�s��, Unsigned and MSB First, P2=0x01�ɸ�0x00 */
	private static final int scRespData_SAMID = scRespData_ReaderFWVersion + scRespData_ReaderFWVersion_Len;
	private static final int scRespData_SAMID_Len = 8;
	public byte[] GetResp_SAMID() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SAMID, scRespData_SAMID + scRespData_SAMID_Len);
	}
	
	/* SAM SN, 4 bytes, ��SAM Card, SAM�ϥΦ���, Unsigned and MSB First, P2=0x01�ɸ�0x00 */
	private static final int scRespData_SAMSN = scRespData_SAMID + scRespData_SAMID_Len;
	private static final int scRespData_SAMSN_Len = 4;
	public byte[] GetResp_SAMSN() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SAMSN, scRespData_SAMSN + scRespData_SAMSN_Len);
	}
	
	/* SAM CRN, 8 bytes, ��SAM Card, SAM���ͪ�Random Number, P2=0x01�ɸ�0x00 */
	private static final int scRespData_SAMCRN = scRespData_SAMSN + scRespData_SAMSN_Len;
	private static final int scRespData_SAMCRN_Len = 8;
	public byte[] GetResp_SAMCRN() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SAMCRN, scRespData_SAMCRN + scRespData_SAMCRN_Len);
	}
	
	/* Device ID, 4 bytes, ��SAM Card, �³]�ƽs��, Unsigned and LSB First */
	private static final int scRespData_DeviceID = scRespData_SAMCRN + scRespData_SAMCRN_Len;
	private static final int scRespData_DeviceID_Len = 4;
	/* Device ID, �³]�ƽs��, ��SAM Card, Unsigned and LSB First, SAM */
	public byte[] GetResp_DeviceID() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_DeviceID, scRespData_DeviceID + scRespData_DeviceID_Len);
	}
	
	/* SAM Key Version, 1 byte, ��SAM Card, SAM���_���� */
	private static final int scRespData_SAMKeyVersion = scRespData_DeviceID + scRespData_DeviceID_Len;
	private static final int scRespData_SAMKeyVersion_Len = 1;
	public byte GetResp_SAMKeyVersion() {
		if (mRespond == null) {
			return 0;
		}
		return mRespond[scRespData_SAMKeyVersion];
	}
	
	/* S-TAC, 8 bytes, ��SAM Card, SAM�{�ҽX, P2=0x01�ɸ�0x00 */
	private static final int scRespData_S_TAC = scRespData_SAMKeyVersion + scRespData_SAMKeyVersion_Len;
	private static final int scRespData_S_TAC_Len = 8;
	/* S-TAC, SAM�{�ҽX, ��SAM Card, P2=0x01�ɸ�0x00, SAM */
	public byte[] GetResp_S_TAC() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_S_TAC, scRespData_S_TAC + scRespData_S_TAC_Len);
	}
	
	/* SAM Version Number, 1 byte, �sSAM Card, SAM���� */
	private static final int scRespData_SAMVersionNumber = scRespData_S_TAC + scRespData_S_TAC_Len;
	private static final int scRespData_SAMVersionNumber_Len = 1;
	public byte GetResp_SAMVersionNumber() {
		if (mRespond == null) {
			return 0;
		}
		return mRespond[scRespData_SAMVersionNumber];
	}
	
	/* SID, 8 bytes, �sSAM Card, SAM ID, Unsigned and MSB First */
	private static final int scRespData_SAMIDNew = scRespData_SAMVersionNumber + scRespData_SAMVersionNumber_Len;
	private static final int scRespData_SAMIDNew_Len = 8;
	public byte[] GetResp_SAMIDNew() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SAMIDNew, scRespData_SAMIDNew + scRespData_SAMIDNew_Len);
	}
	
	/* SAM Usage Control, 3 bytes, �sSAM Card, SAM����Ѽ� */
	private static final int scRespData_SAMUsageControl = scRespData_SAMIDNew + scRespData_SAMIDNew_Len;
	private static final int scRespData_SAMUsageControl_Len = 3;
	public byte[] GetResp_SAMUsageControl() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SAMUsageControl, scRespData_SAMUsageControl + scRespData_SAMUsageControl_Len);
	}
	
	/* SAM Admin KVN, 1 bytes, �sSAM Card, SAM Admin Key Version Number */
	private static final int scRespData_SAMAdminKVN = scRespData_SAMUsageControl + scRespData_SAMUsageControl_Len;
	private static final int scRespData_SAMAdminKVN_Len = 1;
	public byte GetResp_SAMAdminKVN() {
		if (mRespond == null) {
			return 0;
		}
		return mRespond[scRespData_SAMAdminKVN];
	}
	
	/* SAM Issuer KVN, 1 bytes, �sSAM Card, SAM Issuer Key Version Number */
	private static final int scRespData_SAMIssuerKVN = scRespData_SAMAdminKVN + scRespData_SAMAdminKVN_Len;
	private static final int scRespData_SAMIssuerKVN_Len = 1;
	public byte GetResp_SAMIssuerKVN() {
		if (mRespond == null) {
			return 0;
		}
		return mRespond[scRespData_SAMIssuerKVN];
	}
	
	/* ACL, 3 bytes, �sSAM Card, Authorized Credit Limit(�[���B�׹w�]��), Unsigned and LSB First */
	private static final int scRespData_ACL = scRespData_SAMIssuerKVN + scRespData_SAMIssuerKVN_Len;
	private static final int scRespData_ACL_Len = 3;
	/* ACL, Authorized Credit Limit(�[���B�׹w�]��), Unsigned and LSB First, �sSAM Card */
	public byte[] GetResp_ACL() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_ACL, scRespData_ACL + scRespData_ACL_Len);
	}
	
	/* Single Credit TXN AMT Limit, 3 bytes, �sSAM Card, �[�ȥ�����B���B, Unsigned and LSB First */
	private static final int scRespData_SingleCreditTXNAMTLimit = scRespData_ACL + scRespData_ACL_Len;
	private static final int scRespData_SingleCreditTXNAMTLimit_Len = 3;
	public byte[] GetResp_SingleCreditTXNAMTLimit() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SingleCreditTXNAMTLimit, 
				scRespData_SingleCreditTXNAMTLimit + scRespData_SingleCreditTXNAMTLimit_Len);
	}
	
	/* ACB, 3 bytes, �sSAM Card, Authorized Credit Balance(�[�ȱ��v�B��), Unsigned and LSB First */
	private static final int scRespData_ACB = scRespData_SingleCreditTXNAMTLimit + scRespData_SingleCreditTXNAMTLimit_Len;
	private static final int scRespData_ACB_Len = 3;
	public byte[] GetResp_ACB() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_ACB, scRespData_ACB + scRespData_ACB_Len);
	}
	
	/* ACC, 3 bytes, �sSAM Card, Authorized Credit Cumulative(�[�Ȳֿn�w���B��), Unsigned and LSB First */
	private static final int scRespData_ACC = scRespData_ACB + scRespData_ACB_Len;
	private static final int scRespData_ACC_Len = 3;
	public byte[] GetResp_ACC() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_ACC, scRespData_ACC + scRespData_ACC_Len);
	}
	
	/* ACCC, 3 bytes, �sSAM Card, Authorized Cancel Credit Cumulative(�����[�Ȳֿn�w���B��), Unsigned and LSB First */
	private static final int scRespData_ACCC = scRespData_ACC + scRespData_ACC_Len;
	private static final int scRespData_ACCC_Len = 3;
	public byte[] GetResp_ACCC() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_ACCC, scRespData_ACCC + scRespData_ACCC_Len);
	}
	
	/* New Device ID, 6 bytes, �sSAM Card, �s�]�ƽs��, Unsigned and LSB First */
	/* EasyCard: long���e���bytes�N�OSPID code */
	private static final int scRespData_NewDeviceID = scRespData_ACCC + scRespData_ACCC_Len;
	private static final int scRespData_NewDeviceID_Len = 6;
	public byte[] GetResp_NewDeviceID() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_NewDeviceID, 
										scRespData_NewDeviceID + scRespData_NewDeviceID_Len);
	}
	
	/* Tag List Table, 40 bytes, �sSAM Card, Tag����Ѽ� */
	private static final int scRespData_TagListTable = scRespData_NewDeviceID + scRespData_NewDeviceID_Len;
	private static final int scRespData_TagListTable_Len = 40;
	public byte[] GetResp_TagListTable() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_TagListTable, 
				scRespData_TagListTable + scRespData_TagListTable_Len);
	}
	
	/* SAM Issuer Specific Data, 32 bytes, �sSAM Card, SAM�ۭq��� */
	private static final int scRespData_SAMIssuerSpecificData = scRespData_TagListTable + scRespData_TagListTable_Len;
	private static final int scRespData_SAMIssuerSpecificData_Len = 32;
	public byte[] GetResp_SAMIssuerSpecificData() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SAMIssuerSpecificData, 
				scRespData_SAMIssuerSpecificData + scRespData_SAMIssuerSpecificData_Len);
	}
	
	/* STC, SAM TXN Counter, 4 bytes, �sSAM Card, Unsigned and MSB First, P2=0x01�ɸ�0x00 */
	private static final int scRespData_STC = scRespData_SAMIssuerSpecificData + scRespData_SAMIssuerSpecificData_Len;
	private static final int scRespData_STC_Len = 4;
	public byte[] GetResp_STC() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_STC, scRespData_STC + scRespData_STC_Len);
	}
	
	/* RSAM, 8 btes, �sSAM Card, SAM���ͤ�Random Number, P2=0x01�ɸ�0x00 */
	private static final int scRespData_RSAM = scRespData_STC + scRespData_STC_Len;
	private static final int scRespData_RSAM_Len = 8;
	public byte[] GetResp_RSAM() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_RSAM, scRespData_RSAM + scRespData_RSAM_Len);
	}
	
	/* RHOST, 8 bytes, Reader, Host��Random Number (Reader����), P2=0x01�ɸ�0x00 */
	private static final int scRespData_RHOST = scRespData_RSAM + scRespData_RSAM_Len;
	private static final int scRespData_RHOST_Len = 8;
	public byte[] GetResp_RHOST() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_RHOST, scRespData_RHOST + scRespData_RHOST_Len);
	}
	
	/* SATOKEN, 16 bytes, �sSAM Card, SAM Authentication Token, P2=0x01�ɸ�0x00 */
	private static final int scRespData_SATOKEN = scRespData_RHOST + scRespData_RHOST_Len;
	private static final int scRespData_SATOKEN_Len = 16;
	public byte[] GetResp_SATOKEN() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_SATOKEN, scRespData_SATOKEN + scRespData_SATOKEN_Len);
	}
	
	/* 
	 * PPR_SignOn�ѼƳ]�w, 1 byte, Reader (�A�Ω�SignOn���]��)
	 * CPD Read Flag: Bit 0~1, �G�NCPDŪ�������ҳ]�w
	 * One Day Quota Write For Micro Payment: Bit 2~3, �p�B���O�魭�B�g�J
	 * SAM SignOnControl Flag: Bit 4~5, SAM�dSignOn����X��
	 * Check EV Flag For Mifare Only: Bit 6, �ˬd�l�B�X��
	 * Merchant Limit Use For Micro Payment: Bit 7, �p�B���O�q������ϥκX�� 
	 */
	private static final int scRespData_SignOnParams1 = scRespData_SATOKEN + scRespData_SATOKEN_Len;
	private static final int scRespData_SignOnParams1_Len = 1;
	
	public enum CPDReadFlag {
		NRHostNCReader,		// ��Ū�^Host�BReader������
		YRHostNCReader, 	// Ū�^Host�BReader������
		NRHostYCReader,		// ��Ū�^Host�BReader�n����
		YRHostYCReader		// Ū�^Host�BReader�n����
	};
	
	public enum SAMSignOnControlFlag {
		NoSignOnForBoth,	// ��iSAM�d������SignOn(���u)
		SignOnForNewOnly,	// �uSignOn�sSAM�d(�^�OMifare��)
		SignOnForOldOnly,	// �uSignOn��SAM�d
		SignOnForBoth		// ��iSAM�d���nSignOn
	};
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, CPD Read Flag: Bit 0~1, �G�NCPDŪ�������ҳ]�w */
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
		case 1: // Ū�^Host�BReader������
			return CPDReadFlag.YRHostNCReader;
		case 2: // ��Ū�^Host�BReader�n����
			return CPDReadFlag.NRHostYCReader;
		case 3: // Ū�^Host�BReader�n����
			return CPDReadFlag.YRHostYCReader;
		default: // ��Ū�^Host�BReader������
			return CPDReadFlag.NRHostNCReader;	
		}*/
	}
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, One Day Quota Write For Micro Payment: Bit 2~3, �p�B���O�魭�B�g�J */
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
		case 1: // T=Mifare�g�J, T-CPU���g�J
			return OneDayQuotaWriteForMicroPayment.WMNC;
		case 2: // T=Mifare���g�J, T-CPU�g�J
			return OneDayQuotaWriteForMicroPayment.NMWC;
		case 3: // T=Mifare�PT-CPU���g�J
			return OneDayQuotaWriteForMicroPayment.WNWC;
		default: // T=Mifare�PT-CPU�����g�J
			return OneDayQuotaWriteForMicroPayment.NMNC;	
		}
		*/
	}
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, SAM SignOnControl Flag: Bit 4~5, SAM�dSignOn����X�� */
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
		case 1: // �uSignOn�sSAM�d(�^�OMifare��)
			return SAMSignOnControlFlag.SignOnForNewOnly;
		case 2: // �uSignOn��SAM�d
			return SAMSignOnControlFlag.SignOnForOldOnly;
		case 3: // ��iSAM�d���nSignOn
			return SAMSignOnControlFlag.SignOnForBoth;
		default: // ��iSAM�d������SignOn(���u)
			return SAMSignOnControlFlag.NoSignOnForBoth;	
		}*/
	}
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, Check EV Flag For Mifare Only: Bit 6, �ˬd�l�B�X�� */
	public boolean GetResp_CheckEVFlagForMifareOnly() {
		if (mRespond == null) {
			return false;
		}
		byte b = (byte) (mRespond[scRespData_SignOnParams1] & 0x40);
		if (b == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, Merchant Limit Use For Micro Payment: Bit 7, �p�B���O�q������ϥκX�� */
	public boolean GetResp_MerchantLimitUseForMicroPayment() {
		if (mRespond == null) {
			return false;
		}
		byte b = (byte) (mRespond[scRespData_SignOnParams1] & 0x80);
		if (b == 0) {
			return true; // ����ϥ�
		} else { 
			return false; // ������ϥ�
		}
	}
	
	/* 
	 * PPR_SignOn�ѼƳ]�w, 1 byte, Reader (�A�Ω�SignOn���]��)
	 * One Day Quota Flag For Micro Payment: Bit 0~1, �p�B���O�魭�B�X��
	 * Once Quota Flag For Micro Payment: Bit 2, �p�B���O�����B�X��
	 * Check Debit Flag: Bit 3, ���ȥ���X�k���ҺX��
	 * Mifare Check Enable Flag: Bit 4, �G�N�dLevel 1
	 * Pay On Behalf Flag: Bit 5, �O�_���\�N��
	 * RFU: Bit 6~7, �O�d 
	 */
	private static final int scRespData_SignOnParams2 = scRespData_SignOnParams1 + scRespData_SignOnParams1_Len;
	private static final int scRespData_SignOnParams2_Len = 1;
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, One Day Quota Flag For Micro Payment: Bit 0~1, �p�B���O�魭�B�X�� */
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
		case 1: // ���ˬd, �֭p�魭�B
			return OneDayQuotaFlagForMicroPayment.NCYA;
		case 2: // �ˬd, ���֭p�魭�B
			return OneDayQuotaFlagForMicroPayment.YCNA;
		case 3: // �ˬd, �֭p�魭�B
			return OneDayQuotaFlagForMicroPayment.YCYA;
		default: // ���ˬd, ���֭p�魭�B
			return OneDayQuotaFlagForMicroPayment.NCNA;	
		}*/
	}
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, Once Quota Flag For Micro Payment: Bit 2, �p�B���O�����B�X�� */
	public boolean GetResp_OnceQuotaFlagForMicroPayment() {
		if (mRespond == null) {
			return false;
		}  
		byte b = (byte) (mRespond[scRespData_SignOnParams2] & 0x04);
		if (b == 0) {
			return false; // ��������B
		} else {
			return true; // ������B
		}
	}
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, Check Debit Flag: Bit 3, ���ȥ���X�k���ҺX�� */
	public boolean GetResp_CheckDebitFlag() {
		if (mRespond == null) {
			return false;
		}  
		byte b = (byte) (mRespond[scRespData_SignOnParams2] & 0x08);
		if (b == 0) {
			return false; // ������ȥ���X�k���Ҫ��B
		} else {
			return true; // ����ȥ���X�k���Ҫ��B
		}
	}
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, Mifare Check Enable Flag: Bit 4, �G�N�dLevel 1 */
	public boolean GetResp_MifareCheckEnableFlag() {
		if (mRespond == null) {
			return false;
		}  
		byte b = (byte) (mRespond[scRespData_SignOnParams2] & 0x10);
		if (b == 0) {
			return false; // ���Ұ�Mifare�l�B�ˬd
		} else {
			return true; // �Ұ�Mifare�l�B�ˬd
		}
	}
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, Pay On Behalf Flag: Bit 5, �O�_���\�N�� */
	public boolean GetResp_PayOnBehalfFlag() {
		if (mRespond == null) {
			return false;
		}  
		byte b = (byte) (mRespond[scRespData_SignOnParams2] & 0x20);
		if (b == 0) {
			return false; // �����\�N��
		} else {
			return true; // ���\�N�Ԥ@��
		}
	}
	
	/* One Day Quota For Micro Payment, 2 bytes, Reader (�A�Ω�SignOn���]��), �p�B���O�魭�B�B��, Unsigned and LSB First */
	private static final int scRespData_OneDayQuotaForMicroPayment = scRespData_SignOnParams2 + scRespData_SignOnParams2_Len;
	private static final int scRespData_OneDayQuotaForMicroPayment_Len = 2;
	public byte[] GetResp_OneDayQuotaForMicroPayment() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_OneDayQuotaForMicroPayment, 
				scRespData_OneDayQuotaForMicroPayment + scRespData_OneDayQuotaForMicroPayment_Len);
	}
	
	/* Once Quota For Micro Payment, 2 bytes, Reader (�A�Ω�SignOn���]��), �p�B���O�����B�B��, Unsigned and LSB First */
	private static final int scRespData_OnceQuotaForMicroPayment = scRespData_OneDayQuotaForMicroPayment + scRespData_OneDayQuotaForMicroPayment_Len;
	private static final int scRespData_OnceQuotaForMicroPayment_Len = 2;
	public byte[] GetResp_OnceQuotaForMicroPayment() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_OnceQuotaForMicroPayment, 
				scRespData_OnceQuotaForMicroPayment + scRespData_OnceQuotaForMicroPayment_Len);
	}
	
	/* Check Debit Value, 2 bytes, Reader (�A�Ω�SignOn���]��), ���ȥ���X�k���Ҫ��B, Unsigned and LSB First */
	private static final int scRespData_CheckDebitValue = scRespData_OnceQuotaForMicroPayment + scRespData_OnceQuotaForMicroPayment_Len;
	private static final int scRespData_CheckDebitValue_Len = 2;
	public byte[] GetResp_CheckDebitValue() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_CheckDebitValue, 
				scRespData_CheckDebitValue + scRespData_CheckDebitValue_Len);
	}
	
	/* Add Quota Flag, 1 byte, Reader (�A�Ω��ª��B�ױ���), �[���B�ױ��޺X�� */
	private static final int scRespData_AddQuotaFlag = scRespData_CheckDebitValue + scRespData_CheckDebitValue_Len;
	private static final int scRespData_AddQuotaFlag_Len = 1;
	public boolean GetResp_AddQuotaFlag() {
		if (mRespond == null) {
			return false;
		}
		
		if (mRespond[scRespData_AddQuotaFlag] == 0) {
			return false; // ���ˬd�B��
		} else {
			return true; // �ˬd�B��
		}
	}
	
	/* Add Quota, 3 bytes, Reader (�A�Ω��ª��B�ױ���), �[���B��, Unsigned and LSB First */
	private static final int scRespData_AddQuota = scRespData_AddQuotaFlag + scRespData_AddQuotaFlag_Len;
	private static final int scRespData_AddQuota_Len = 3;
	public byte[] GetResp_AddQuota() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_AddQuota, 
				scRespData_AddQuota + scRespData_AddQuota_Len);
	}
	
	/* The Remainder of Add Quota, 3 bytes, Reader (�A�Ω��ª��B�ױ���), �Ѿl�[���B��, Unsigned and LSB First */
	private static final int scRespData_RemainderofAddQuota = scRespData_AddQuota + scRespData_AddQuota_Len;
	private static final int scRespData_RemainderofAddQuota_Len = 3;
	public byte[] GetResp_RemainderofAddQuota() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_RemainderofAddQuota, 
				scRespData_RemainderofAddQuota + scRespData_RemainderofAddQuota_Len);
	}
	
	/* Cancel Credit Quota, 3 bytes, Reader (�A�Ω��ª��B�ױ���), �����[�Ȳ֭p�B��, Unsigned and LSB First */
	private static final int scRespData_CancelCreditQuota = scRespData_RemainderofAddQuota + scRespData_RemainderofAddQuota_Len;
	private static final int scRespData_CancelCreditQuota_Len = 3;
	public byte[] GetResp_CancelCreditQuota() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_CancelCreditQuota, 
				scRespData_CancelCreditQuota + scRespData_CancelCreditQuota_Len);
	}
	
	/* deMAC Parameter, 8 bytes, Reader (�A�Ω��ª�SAM Card), �Y�ǶW��Dongle deMAC�ݭn�Ψ즹�Ѽ�, P2=0x01�ɸ�0x00 */
	private static final int scRespData_deMACParameter = scRespData_CancelCreditQuota + scRespData_CancelCreditQuota_Len;
	private static final int scRespData_deMACParameter_Len = 8;
	public byte[] GetResp_deMACParameter() {
		if (mRespond == null) {
			return null;
		}
		return Arrays.copyOfRange(mRespond, scRespData_deMACParameter, 
				scRespData_deMACParameter + scRespData_deMACParameter_Len);		
	}
	
	/* Last TXN Date Time, 4 bytes, Reader, �̫�@��Mutual Authentication���\�ɶ�(UnixDateTime) */
	private static final int scRespData_LastTXNDateTime = scRespData_deMACParameter + scRespData_deMACParameter_Len;
	private static final int scRespData_LastTXNDateTime_Len = 4;
	public int GetResp_LastTXNDateTime() {
		if (mRespond == null) {
			return 0;
		}
		return Util.sByteToInt(mRespond, scRespData_LastTXNDateTime, true);
	}
	
	/* Previous New Device ID, 6 bytes, Reader, �s�]�ƽs��, 
	 * �sSAM Card SignOn��confirm����, �t���\�Υ���, 
	 * Unsigned and LSB First, P2=0x01�ɸ�0x00 
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
	 * �sSAM Card SignOn��confirm����, �t���\�Υ���, 
	 * Unsigned and MSB First, P2=0x01�ɸ�0x00 
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
	
	/* Previous TXN Date Time, 4 bytes, Reader �������ɶ�, 
	 * �sSAM Card SignOn��confirm����, �t���\�Υ���, 
	 * UnixDateTime, Unsigned and LSB First, P2=0x01�ɸ�0x00 
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
	
	/* Previous Credit Balance Change Flag, 1 byte, Reader, �[�ȱ��v�B��(ACB)�ܧ�X��, 
	 * �sSAM Card SignOn��confirm����, �t���\�Υ���, 
	 * UnixDateTime, Unsigned and LSB First, P2=0x01�ɸ�0x00 
	 */
	private static final int scRespData_PreviousCreditBalanceChangeFlag = scRespData_PreviousTXNDateTime + scRespData_PreviousTXNDateTime_Len;
	private static final int scRespData_PreviousCreditBalanceChangeFlag_Len = 1;
	public boolean GetResp_PreviousCreditBalanceChangeFlag() {
		if (mRespond == null) {
			return false;
		}
		
		if (mRespond[scRespData_PreviousCreditBalanceChangeFlag] == 0) {
			return false; // ���ܧ�
		} else {
			return true; // �B�צ��ܧ�
		}
	}
	
	/* Previous Confirm Code, 2 bytes, Reader, Status Code1 + Status Code2, 
	 * �sSAM Card SignOn��confirm����, �t���\�Υ���, 
	 * P2=0x01�ɸ�0x00 
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
	 * �sSAM Card SignOn��confirm����, �t���\�Υ���, 
	 * �B���ܧ󥢱ѩΥ��ܧ��P2=0x01�ɸ�0x00 
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
