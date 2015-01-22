package Reader;

import java.util.Arrays;


import Utilities.DataFormat;


public class PPR_SignOn extends APDU {
public static final String scDescription = "�N0810�ݥ��}���T���z�LReader�ǤJSAM�d�����{��";
	
	private static PPR_SignOn sThis = null;
	
	private static final int scReqDataLength = 128;
	private static final int scReqLength = scReqDataLength + scReqMinLength;
	private static final int scReqInfoLength = scReqDataLength + scReqInfoMinLength;
	private static final int scRespDataLength = 29;
	private static final int scRespLength = scRespDataLength + scRespMinLength;
	
	private boolean mReqDirty = true;
	
	private byte[] mRequest = new byte[scReqLength];
	private byte[] mRespond = null;
	
	public static PPR_SignOn sGetInstance() {
		if (sThis == null) {
			sThis = new PPR_SignOn();
		}
		return sThis;
	}
	
	private PPR_SignOn() {
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
	
	/* H-TAC, 8 bytes, Host, Host�{�ҽX, �Ω���SAM Card, T6401 */
	private static final int scReqData_H_TAC = scReqDataOffset + 0;
	private static final int scReqData_H_TAC_Len = 8;
 	public boolean SetReq_H_TAC(String htac) { 
 		if (htac == null || htac.length() != scReqData_H_TAC_Len * 2) {
			return false;
		}
 		
 		//byte[] b = Util.sGetBinaryfromString(htac);
 		
 		byte[] b = DataFormat.hexStringToByteArray(htac);
 		if (b == null) {
 			return false;
 		}
 		System.arraycopy(b, 0, mRequest, scReqData_H_TAC, scReqData_H_TAC_Len);
 		
		mReqDirty = true;
		return true;
	}
	
	/* HAToken, 16 bytes, Host, Host Authentication Token, �Ω�sSAM Card, T6409 */
	private static final int scReqData_HAToken = scReqData_H_TAC + scReqData_H_TAC_Len;
	private static final int scReqData_HAToken_Len = 16;
	public boolean SetReq_HAToken(String haToken) {
		if (haToken == null || haToken.length() != scReqData_HAToken_Len * 2) {
			return false;
		}
		
		//byte[] b = Util.sGetBinaryfromString(haToken);
		byte[] b = DataFormat.hexStringToByteArray(haToken);
		if (b == null) {
			return false;
		}
		System.arraycopy(b, 0, mRequest, scReqData_HAToken, scReqData_HAToken_Len);

		mReqDirty = true;
		return true;
	}
	
	/* SAM Update Option, 1 byte, Host, SAM�ѼƧ�s�ﶵ (0: ����s, 1: ��s), �Ω�sSAM Card */
	private static final int scReqData_SAMUpdateOption = scReqData_HAToken + scReqData_HAToken_Len;
	private static final int scReqData_SAMUpdateOption_Len = 1;
	private static final int scReqData_SAMUpdateOption_ACB = 0; 			// T5367#0, �[�ȱ��v�B�� (ACB)
	private static final int scReqData_SAMUpdateOption_ACL = 1; 			// T5367#0, �[���B�׹w�]�� (ACL)
	private static final int scReqData_SAMUpdateOption_SAMUsageControl = 2; // T5367#0, SAM����Ѽ� (SAM Usage Control)
	private static final int scReqData_SAMUpdateOption_TagListTable = 3; 	// T5367#0, Tag����Ѽ� (Tag List Table)
	private static final int scReqData_SAMUpdateOption_HasNext = 7; 		// T5367#0, �O�_�٦��U�@�ӰѼƶ���s (0: �_, 1: �O)
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
	// �Ӧ�CMAS T5367���^��
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
		
		//byte[] b = Util.sGetBinaryfromString(option);
		byte[] b = DataFormat.hexStringToByteArray(option);
		if (b == null) {
			return false;
		}
		mRequest[scReqData_SAMUpdateOption] = b[0];
		
		mReqDirty = true;
		return true;
	}
	
	/* New SAM Value, 40 bytes, Host, �sSAM�Ѽƭ�(SAM Update Option=0x00��, ��0x00),
	 * �sSAM�ѼƭȪ��פ���40bytes��, ���a�k��0, �Ω�sSAM Card, T5367#2 */
	private static final int scReqData_NewSAMValue = scReqData_SAMUpdateOption + scReqData_SAMUpdateOption_Len;
	private static final int scReqData_NewSAMValue_Len = 40;
	public boolean SetReq_NewSAMValue(String values) {
		if (values == null || values.length() != scReqData_NewSAMValue_Len * 2) {
			return false;
		}
		
		//byte[] b = Util.sGetBinaryfromString(values);
		byte[] b = DataFormat.hexStringToByteArray(values);
		if (b == null) {
			return false;
		}
		System.arraycopy(b, 0, mRequest, scReqData_NewSAMValue, scReqData_NewSAMValue_Len);
		
		mReqDirty = true;
		return true;
	}
	
	/* Update SAM Value MAC, 16 bytes, Host, ��sSAM�ѼƩһݤ�MAC(SAM Update Option=0x00��, ��0x00), 
	 * �Ω�sSAM Card, T5367#82 */
	private static final int scReqData_UpdateSAMValueMAC = scReqData_NewSAMValue + scReqData_NewSAMValue_Len;
	private static final int scReqData_UpdateSAMValueMAC_Len = 16;
	public boolean SetReq_UpdateSAMValueMAC(String mac) {
		if (mac == null || mac.length() != scReqData_UpdateSAMValueMAC_Len * 2) {
			return false;
		}
		
		//byte[] b = Util.sGetBinaryfromString(mac);
		byte[] b = DataFormat.hexStringToByteArray(mac);
		if (b == null) {
			return false;
		}
		System.arraycopy(b, 0, mRequest, scReqData_UpdateSAMValueMAC, scReqData_UpdateSAMValueMAC_Len);
	
		mReqDirty = true;
		return true;
	}
	
	/* 
	 * PPR_SignOn�ѼƳ]�w, 1 byte, Host, �A�Ω�SignOn���]��
	 * CPD Read Flag: Bit 0~1, �G�NCPDŪ�������ҳ]�w, T4824
	 * One Day Quota Write For Micro Payment: Bit 2~3, �p�B���O�魭�B�g�J, T4823
	 * SAM SignOnControl Flag: Bit 4~5, SAM�dSignOn����X��, T5369
	 * Check EV Flag For Mifare Only: Bit 6, �ˬd�l�B�X��
	 * Merchant Limit Use For Micro Payment: Bit 7, �p�B���O�q������ϥκX�� 
	 */
	private static final int scReqData_SignOnParams1 = scReqData_UpdateSAMValueMAC + scReqData_UpdateSAMValueMAC_Len;
	private static final int scReqData_SignOnParams1_Len = 1;
	
	/* CPD Read Flag: Bit 0~1, �G�NCPDŪ�������ҳ]�w, T4824 */
	public boolean SetReq_CPDReadFlag(String flag) {
		if (flag == null || flag.length() != 2) {
			return false;
		}
		
		byte b = mRequest[scReqData_SignOnParams1];
		b = ClearBit(b, 0);
		b = ClearBit(b, 1); // ��Ū��Host�BReader������
		
		if (flag.equals("01")) { // Ū��Host�BReader������
			b = SetBit(b, 0); 
		} else if (flag.equals("10")) { // ��Ū��Host�BReader�n����
			b = SetBit(b, 1); 
		} else if (flag.equals("11")) { // Ū��Host�BReader�n����
			b = SetBit(b, 0); 
			b = SetBit(b, 1); 
		} 
		
		mRequest[scReqData_SignOnParams1] = b;
		mReqDirty = true;
		return true;
	}
	
	/* One Day Quota Write For Micro Payment: Bit 2~3, �p�B���O�魭�B�g�J, T4823 */
	public boolean SetReq_OneDayQuotaWriteForMicroPayment(String flag) {
		if (flag == null || flag.length() != 2) {
			return false;
		}
		
		byte b = mRequest[scReqData_SignOnParams1];
		
		b = ClearBit(b, 0); // Mifare�PCPU�����g�J
		b = ClearBit(b, 1);
		if (flag.equals("01")) { // Mifare�g�J, CPU���g�J
			b = SetBit(b, 2);
		} else if (flag.equals("10")) { // Mifare���g�J, CPU�g�J
			b = SetBit(b, 3);
		} else if (flag.equals("11")) { // Mifare�g�J, CPU�g�J (default)
			b = SetBit(b, 2);
			b = SetBit(b, 3);
		}
		
		mRequest[scReqData_SignOnParams1] = b;
		mReqDirty = true;
		return true;
	}
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, SAM SignOnControl Flag: Bit 4~5, SAM�dSignOn����X��, T5309 */
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
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, Check EV Flag For Mifare Only: Bit 6, �ˬd�l�B�X��, T6002#12 */
	public boolean SetReq_CheckEVFlagForMifareOnly(String flag) {
		if (flag == null || flag.length() != 2) {
			return false;
		}
		
		byte b = mRequest[scReqData_SignOnParams1];
		if (flag.equals("01")) { 
			b = SetBit(b, 6); // ���ˬd�l�B
		} else { 
			b = ClearBit(b, 6); // �ˬd�l�B (default)
		}
		
		mRequest[scReqData_SignOnParams1] = b;
		mReqDirty = true;
		return true;
	}
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, Merchant Limit Use For Micro Payment: Bit 7, �p�B���O�q������ϥκX��, 
	 * T6002#28, 00: ������, 01: ����  
	 */
	public void SetReq_MerchantLimitUseForMicroPayment(boolean bLimit) {
		byte b = mRequest[scReqData_SignOnParams1];
		
		if (bLimit) {
			b = ClearBit(b, 7); // ����ϥ�
		} else {
			b = SetBit(b, 7); // ������ϥ�
		}
		
		mRequest[scReqData_SignOnParams1] = b;
		mReqDirty = true;
	}
	
	public void SetReq_MerchantLimitUseForMicroPayment(String flag) {
		// �o�̪������컡�������D, �n�T�w...
	}
	
	/* 
	 * PPR_SignOn�ѼƳ]�w, 1 byte, Host, �A�Ω�SignOn���]��
	 * One Day Quota Flag For Micro Payment: Bit 0~1, �p�B���O�魭�B�X��, T6002#0
	 * Once Quota Flag For Micro Payment: Bit 2, �p�B���O�����B�X��, T6002#6
	 * Check Debit Flag: Bit 3, ���ȥ���X�k���ҺX��, T6002#22
	 * Mifare Check Enable Flag: Bit 4, �G�N�dLevel 1
	 * Pay On Behalf Flag: Bit 5, �O�_���\�N��
	 * RFU: Bit 6~7, �O�d 
	 */
	private static final int scReqData_SignOnParams2 = scReqData_SignOnParams1 + scReqData_SignOnParams1_Len;
	private static final int scReqData_SignOnParams2_Len = 1;
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, One Day Quota Flag For Micro Payment: Bit 0~1, �p�B���O�魭�B�X��, T6002#0 */
	public boolean SetReq_OneDayQuotaFlagForMicroPayment(String flag) {
		if (flag == null || flag.length() != 2) {
			return false;
		}
		
		byte b = mRequest[scReqData_SignOnParams2];
		
		b = ClearBit(b, 0); // ���ˬd, ���֭p�魭�B
		b = ClearBit(b, 1);
		if (flag.equals("01")) { // ���ˬd, �֭p�魭�B
			b = SetBit(b, 0);
		} else if (flag.equals("10")) { // �ˬd, ���֭p�魭�B
			b = SetBit(b, 1);
		} else if (flag.equals("11")) { // �ˬd, �֭p�魭�B
			b = SetBit(b, 0);
			b = SetBit(b, 1);
		}
		
		mRequest[scReqData_SignOnParams2] = b;
		mReqDirty = true;
		return false;
	}
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, Once Quota Flag For Micro Payment: Bit 2, �p�B���O�����B�X��, T6002#6 */
	public boolean SetReq_OnceQuotaFlagForMicroPayment(String flag) {
		if (flag == null || flag.length() != 2) {
			return false;
		}
		
		byte b = mRequest[scReqData_SignOnParams2];
		
		if (flag.equals("01")) {
			b = SetBit(b, 2); // ������B
		} else {
			b = ClearBit(b, 2); // ��������B
		}
		
		mRequest[scReqData_SignOnParams2] = b;
		mReqDirty = true;
		return true;
	}
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, Check Debit Flag: Bit 3, ���ȥ���X�k���ҺX��, T6002#22 */
	public boolean SetReq_CheckDebitFlag(String flag) {
		if (flag == null || flag.length() != 2) {
			return false;
		}
		
		byte b = mRequest[scReqData_SignOnParams2];
		
		if (flag.equals("01")) {
			b = SetBit(b, 3); // ����ȥ���X�k���ҺX��
		} else {
			b = ClearBit(b, 3); // ������ȥ���X�k���ҺX��
		}
		
		mRequest[scReqData_SignOnParams2] = b;
		mReqDirty = true;
		return true;
	}
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, Mifare Check Enable Flag: Bit 4, �G�N�dLevel 1 */
	public boolean SetReq_MifareCheckEnableFlag(String flag) {
		// Bruce, �����a��
		return false;
	}
	
	/* PPR_SignOn�ѼƳ]�w, �A�Ω�SignOn���]��, Pay On Behalf Flag: Bit 5, �O�_���\�N�� */
	public boolean SetReq_PayOnBehalfFlag(String flag) {
		// Bruce, �����a��
		return false;
	}
	
	/* One Day Quota For Micro Payment, 2 bytes, Host, �p�B���O�魭�B�B��, �A�Ω�SignOn���]��, Unsigned and LSB First, T6002#2 */
	private static final int scReqData_OneDayQuotaForMicroPayment = scReqData_SignOnParams2 + scReqData_SignOnParams2_Len;
	private static final int scReqData_OneDayQuotaForMicroPayment_Len = 2;
	public boolean SetReq_OneDayQuotaForMicroPayment(String amount) {
		if (amount == null || amount.length() != scReqData_OneDayQuotaForMicroPayment_Len * 2) {
			return false;
		}
		
		//byte[] b = Util.sGetBinaryfromString(amount);
		byte[] b = DataFormat.hexStringToByteArray(amount);
		if (b == null) {
 			return false;
 		}
 		System.arraycopy(b, 0, mRequest, scReqData_OneDayQuotaForMicroPayment, scReqData_OneDayQuotaForMicroPayment_Len);
		
		mReqDirty = true;
		return true;
	}
	
	/* Once Quota For Micro Payment, 2 bytes, Host, �p�B���O�����B�B��, �A�Ω�SignOn���]��, Unsigned and LSB First, T6002#8 */
	private static final int scReqData_OnceQuotaForMicroPayment = scReqData_OneDayQuotaForMicroPayment + scReqData_OneDayQuotaForMicroPayment_Len;
	private static final int scReqData_OnceQuotaForMicroPayment_Len = 2;
	public boolean SetReq_OnceQuotaForMicroPayment(String amount) {
		if (amount == null || amount.length() != scReqData_OnceQuotaForMicroPayment_Len * 2) {
			return false;
		}
		
		//byte[] b = Util.sGetBinaryfromString(amount);
		byte[] b = DataFormat.hexStringToByteArray(amount);
		if (b == null) {
 			return false;
 		}
 		System.arraycopy(b, 0, mRequest, scReqData_OnceQuotaForMicroPayment, scReqData_OnceQuotaForMicroPayment_Len);
		
		mReqDirty = true;
		return true;
	}
	
	/* Check Debit Value, 2bytes, Host, ���ȥ���X�k���Ҫ��B, �A�Ω�SignOn���]��, Unsigned and LSB First, ?T6002#24? */
	private static final int scReqData_CheckDebitValue = scReqData_OnceQuotaForMicroPayment + scReqData_OnceQuotaForMicroPayment_Len;
	private static final int scReqData_CheckDebitValue_Len = 2;
	public boolean SetReq_CheckDebitValue(String amount) {
		if (amount == null || amount.length() != scReqData_CheckDebitValue_Len * 2) {
			return false;
		}
		
		//byte[] b = Util.sGetBinaryfromString(amount);
		byte[] b = DataFormat.hexStringToByteArray(amount);
		if (b == null) {
 			return false;
 		}
 		System.arraycopy(b, 0, mRequest, scReqData_CheckDebitValue, scReqData_CheckDebitValue_Len);
		
		mReqDirty = true;
		return true;
	}
	
	/* Add Quota Flag, 1 byte, Host, �[���B�ױ��޺X��, �A�Ω��ª��B�ױ���, T6002#14 */
	private static final int scReqData_AddQuotaFlag = scReqData_CheckDebitValue + scReqData_CheckDebitValue_Len;
	private static final int scReqData_AddQuotaFlag_Len = 1;
	public boolean SetReq_AddQuotaFlag(String flag) {
		if (flag == null || flag.length() != scReqData_AddQuotaFlag_Len * 2) {
			return false;
		}
		if (flag.equals("00")) { // ���ˬd�B��
			mRequest[scReqData_AddQuotaFlag] = 0x00;
		} else { // 0x01 (default), �ˬd�B�� 
			mRequest[scReqData_AddQuotaFlag] = 0x01;
		}
		
		mReqDirty = true;
		return true;
	}
	
	/* Add Quota, 3 bytes, Host, �[���B��, �A�Ω��ª��B�ױ���, Unsigned and LSB First, T6002#16 */
	private static final int scReqData_AddQuota = scReqData_AddQuotaFlag + scReqData_AddQuotaFlag_Len;
	private static final int scReqData_AddQuota_Len = 3;
	public boolean SetReq_AddQuota(String amount) {
		if (amount == null || amount.length() != scReqData_AddQuota_Len * 2) {
			return false;
		}
		
		//byte[] b = Util.sGetBinaryfromString(amount);
		byte[] b = DataFormat.hexStringToByteArray(amount);
		if (b == null) {
 			return false;
 		}
 		System.arraycopy(b, 0, mRequest, scReqData_AddQuota, scReqData_AddQuota_Len);
		
		mReqDirty = true;
		return true;
	}
	
	/* RFU, �O�d(Reserved For Use), ��0 */
	private static final int scReqData_RFU = scReqData_AddQuota + scReqData_AddQuota_Len;
	private static final int scReqData_RFU_Len = 31;
	
	/* EDC, �ˮֽX(Error Detection Code), Hash�覡(1byte, T5303) + Hsh Value(3byte, T5306) */
	private static final int scReqData_EDC = scReqData_RFU + scReqData_RFU_Len;
	private static final int scReqData_EDC_Len = 4;
	public void SetReq_EDC() {
		
		/* ???Not yet??? */
		
		mReqDirty = true;
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
	
	/* Credit Balance Change Flag, 1 byte, SAM,  �[�ȱ��v�B��(ACB)�ܧ�X��
	 * 0x00: ���ܧ�
	 * 0x01: �B�צ��ܧ�  
	 */
	private static final int scRespData_CreditBalanceChangeFlag = scRespDataOffset;
	private static final int scRespData_CreditBalanceChangeFlag_Len = 1;
	public byte GetResp_CreditBalanceChangeFlag() {
		if (mRespond == null) {
			return 0;
		}
		return mRespond[scRespData_CreditBalanceChangeFlag]; 
	}
	
	/* Original Authorized Credit Limit, 3 bytes, �sSAM,  ��[���B�׹w�]��(ACL)
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
	
	/* Original Authorized Credit Balance, 3 bytes, �sSAM,  ��[�ȱ��v�B��(ACB)
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
	
	/* Original Authorized Credit Cumulative, 3 bytes, �sSAM,  ��[�Ȳֿn�w���B��(ACC)
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
	
	/* Original Authorized Cancel Credit Cumulative, 3 bytes, �sSAM,  ������[�Ȳֿn�w���B��(ACCC)
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

	/* CACrypto, 16 bytes, �sSAM,  Credit Authorization Cryptogram
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
}
