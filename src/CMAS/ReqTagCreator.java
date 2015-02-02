package CMAS;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import Reader.PPR_Reset.CPDReadFlag;
import Reader.PPR_Reset.OneDayQuotaFlagForMicroPayment;
import Reader.PPR_Reset.OneDayQuotaWriteForMicroPayment;
import Reader.PPR_Reset.SAMSignOnControlFlag;
import Utilities.Util;

public class ReqTagCreator {
	/* n 4, Message Type ID */
	public static final String scMessageTypeID = "T0100";
	public enum message_type {
		req,		// Request
		resp,		// Response
		adv			// Advise
	};
	public static String sGetMessageTypeID(message_type e) {
		String s = "";
		switch(e) {
		case req:
			s = MessageType.scDevControlReq;
			break;
		case resp: 
			s = MessageType.scDevControlReqResp;
			break;
		case adv:
			s = MessageType.scDevControlAdv;
			break;
		}
		s = "<" + scMessageTypeID + ">" + s + "</" + scMessageTypeID + ">";
		return s;
	}
	
	/* n, Card Physical ID, �y�C�d�����X, �@�N�d4bytes�ন�Q�i�쪺��, CPU�d7bytes�ন�Q�i�쪺��, 1~20bytes */
	public static final String scCardPhysicalID = "T0200";
	public static String sGetCardPhysicalID(byte[] bytes, boolean bGen2) {
		if (bytes.length != 7) {
			return "";
		}
		
		byte[] b = new byte[8];
		Arrays.fill(b, (byte) 0);
		if (bGen2) {
			for (int i = 1; i < b.length; i ++) {
				b[i] = bytes[i - 1];
			}
		} else {
			for (int i = 4; i < b.length; i ++) {
				b[i] = bytes[i - 4];
			}
		}
		
		long l = Util.sByteToLong(b, 0, true);
		String s = "<" + scCardPhysicalID + ">" + String.format("%d", l) + "</" + scCardPhysicalID + ">";
		return s;
	}
	
	/* n, Purse ID(PID), �~�[�d��, 16bytes */
	public static final String scPID = "T0211";
	public static String sGetPID(byte[] bytes) {
		if (bytes.length != 8) {
			return "";
		}
		
		long l = Util.sByteToLong(bytes, 0, true);
		String s = "<" + scPID + ">" + String.format("%d", l) + "</" + scPID + ">";
		return s;
	}
	
	/* an, �y�C�dauto-load flag, �G�N�dPurse usage control bit3, 1 byte */
	public static final String scAutoLoadFlag = "T0212";
	public static String sGetAutoFloadFlag(boolean bAutoLoad) {
		String s = "0"; // ���}��autoload�\��
		if (bAutoLoad) {
			return "1"; // �w�}��autoload�\��
		} 
		s = "<" + scAutoLoadFlag + ">" + s + "</" + scAutoLoadFlag + ">";
		return s;
	}
	
	/* an, Card type, 2 bytes */
	public static final String scCardType = "T0213";
	public enum card_type {
		normal, 			// �@�봶�q, �ǥ�, �u�ݥd
		force_named, 		// �j��O�W�d (�q��, �R��,�Υ��Ӯն�ǥͥd)
		normal_named,		// �@��O�W�d (�y�C�d���q�o�檺���q, �ǥͥd)
		memorial,			// �y�C�d���q�o�檺�����d
		replacement, 		// ���N�d (���|�����Ѫ��q�ѷR�ߴ��N�d)
		special_made,		// �S�s�d
		ic,					// IC�ӫ~
		test,				// ���եd
		co_brander,			// �p�W�d
		special_type,		// �S�إd (�[���@��, �@�鲼...��)
		digital_student,	// �Ʀ�ǥ���
		token,				// �N���d
		bike,				// �}��
		icash,				// iCash�d
	};
	public static String sGetCardType(card_type e) {
		String s = "";
		switch (e) {
		case normal: 			// �@�봶�q, �ǥ�, �u�ݥd
			s = "00";
			break;
		case force_named: 		// �j��O�W�d (�q��, �R��,�Υ��Ӯն�ǥͥd)
			s = "01";
			break;
		case normal_named:		// �@��O�W�d (�y�C�d���q�o�檺���q, �ǥͥd)
			s = "02";
			break;
		case memorial:			// �y�C�d���q�o�檺�����d
			s = "03";
			break;
		case replacement: 		// ���N�d (���|�����Ѫ��q�ѷR�ߴ��N�d)
			s = "04";
			break;
		case special_made:		// �S�s�d
			s = "05";
			break;
		case ic:				// IC�ӫ~
			s = "06";
			break;
		case test:				// ���եd
			s = "07";
			break;
		case co_brander:		// �p�W�d
			s = "08";
			break;
		case special_type:		// �S�إd (�[���@��, �@�鲼...��)
			s = "09";
			break;
		case digital_student:	// �Ʀ�ǥ���
			s = "0A";
			break;
		case token:				// �N���d
			s = "0B";
			break;
		case bike:				// �}��
			s = "0C";
			break;
		case icash:				// iCash�d
			s = "0D";
			break;
		}
		s = "<" + scCardType + ">" + s + "</" + scCardType + ">";
		return s; 
	}
	
	/* b, Personal Profile, 2 bytes */
	public static final String scPersonalProfile = "T0214";
	public enum personal_profile {
		normal, 		// ���q (to DTS : 0x01)
		elder1,			// �q�Ѥ@ (to DTS : 0x02)
		elder2, 		// �q�ѤG (to DTS : 0x03)
		compassion,		// �R�� (to DTS : 0x04)
		accompany,		// �R�� (to DTS : 0x05)
		student,		// �ǥ� (to DTS : 0x06)
		soldier,		// �x�H (to DTS : 0x07)
		police,			// ĵ�� (to DTS : 0x08)
		children		// �ĵ� (to DTS : 0x09)
	}
	public static String sGetPersonalProfile(personal_profile e) {
		String s = "";
		switch (e) {
		case normal: 		// ���q (to DTS : 0x01)
			s = "00";
			break;
		case elder1:		// �q�Ѥ@ (to DTS : 0x02)
			s = "01";
			break;
		case elder2: 		// �q�ѤG (to DTS : 0x03)
			s = "02";
			break;
		case compassion:	// �R�� (to DTS : 0x04)
			s = "03";
			break;
		case accompany:		// �R�� (to DTS : 0x05)
			s = "04";
			break;
		case student:		// �ǥ� (to DTS : 0x06)
			s = "05";
			break;
		case soldier:		// �x�H (to DTS : 0x07)
			s = "06";
			break;
		case police:		// ĵ�� (to DTS : 0x08)
			s = "07";
			break;
		case children:		// �ĵ� (to DTS : 0x09)
			s = "08";
			break;
		}
		
		s = "<" + scPersonalProfile + ">" + s + "</" + scPersonalProfile + ">";
		return s;
	}

	/* n 6, Processing Code */
	public static final String scProcessingCode = "T0300";
	public static String sGetProcessingCode(ProcessingCode.code e) {
		String s = "";
		switch (e) {
		case ParamsDownload: 				/* �ѼƤU��, TMS system�ѼƤU�� */
			s = ProcessingCode.scParamsDownload;
			break;
		case SignOnTMSSystem: 				/* ���ݳ]�ƹ�TMS system�i��sign on */
			s = ProcessingCode.scSignOnTMSSystem;
			break;
		case StatusReport:					/* ���ݳ]�ƹ�TMS system�^���ݥ��]�ƪ��A */
			s = ProcessingCode.scStatusReport;
			break;
		case Settlement:					/* ���b */
			s = ProcessingCode.scSettlement;
			break;
		case InquiryCardTradeDetail6: 		/* �y�C�d�d��������Ӭd��, 6�� */
			s = ProcessingCode.scInquiryCardTradeDetail6;
			break;
		case InquiryDongleTradeDetail1K:	/* �y�C�dDongle������Ӭd��, 1000�� */
			s = ProcessingCode.scInquiryDongleTradeDetail1K;
			break;
		case InquiryCardData:				/* �y�C�d�d����Ƭd�� */
			s = ProcessingCode.scInquiryCardData;
			break;
		case InquiryBalance:				/* �l�B�d�� */
			s = ProcessingCode.scInquiryBalance;
			break;
		case ExhibitCardLock:				/* �y�C�d�i����d */
			s = ProcessingCode.scExhibitCardLock;
			break;
		case CardLock:						/* �y�C�d��d */
			s = ProcessingCode.scCardLock;
			break;
		case EDCSignOn:						/* EDC Sign On */
			s = ProcessingCode.scEDCSignOn;
			break;
		case CashAddCredit:					/* �{���[�� */
			s = ProcessingCode.scCashAddCredit;
			break;
		case SocialCareCashAddCredit:		/* ���֥d�{���[�� */
			s = ProcessingCode.scSocialCareCashAddCredit;
			break;
		case AutoAddCredit:					/* �۰ʥ[�� */
			s = ProcessingCode.scAutoAddCredit;
			break;
		case SocialCareAutoAddCredit:		/* ���֥d�۰ʥ[�� */
			s = ProcessingCode.scSocialCareAutoAddCredit;
			break;
		case ATMAddCredit:					/* ���ĥd�[�� */
			s = ProcessingCode.scATMAddCredit;
			break;
		case SocialCareATMAddCredit:		/* ���֥d���ĥd�[�� */
			s = ProcessingCode.scSocialCareATMAddCredit;
			break;
		case XformRefundAddCredit: 			/* �l�B��m�[�� */
			s = ProcessingCode.scXformRefundAddCredit;
			break;
		case CancelAddCredit:				/* �����[�� */
			s = ProcessingCode.scCancelAddCredit;
			break;
		case Merchandise: 					/* �ʳf */
			s = ProcessingCode.scMerchandise;
			break;
		case CancelMerchandise:				/* �����ʳf */
			s = ProcessingCode.scCancelMerchandise;
			break;
		case SellCard:						/* ��d */
			s = ProcessingCode.scSellCard;
			break;
		case CancelSellCard:				/* ������d */
			s = ProcessingCode.scCancelSellCard;
			break;
		case Exhibit:						/* �i�� */
			s = ProcessingCode.scExhibit;
			break;
		case TXNAuth:						/* ����X�k���� */
			s = ProcessingCode.scTXNAuth;
			break;
		case ReturnMerchandise:				/* �h�f */
			s = ProcessingCode.scReturnMerchandise;
			break;
		case ReturnATMCard:					/* �p�W�d�h�d */
			s = ProcessingCode.scReturnATMCard;
			break;
		case XformRefundReturnCard:			/* �l�B��m�h�d */
			s = ProcessingCode.scXformRefundReturnCard;
			break;
		case CoBranderCardAutoCredit:		/* �p�W�d�۰ʥ[�ȥ\��}�� */
			s = ProcessingCode.scCoBranderCardAutoCredit;
			break;
		case CPUCashAddCredit:				/* CPU�{���[�� */
			s = ProcessingCode.scCPUCashAddCredit;
			break;
		case CPUSocialCareCashAddCredit:	/* CPU���֥d�{���[�� */
			s = ProcessingCode.scCPUSocialCareCashAddCredit;
			break;
		case CPUAutoAddCredit:				/* CPU�۰ʥ[�� */
			s = ProcessingCode.scCPUAutoAddCredit;
			break;
		case CPUSocialCareAutoAddCredit:	/* CPU���֥d�۰ʥ[�� */
			s = ProcessingCode.scCPUSocialCareAutoAddCredit;
			break;
		case CPUATMAddCredit:				/* CPU���ĥd�[�� */
			s = ProcessingCode.scCPUATMAddCredit;
			break;
		case CPUSocialCareATMAddCredit:		/* CPU���֥d���ĥd�[�� */
			s = ProcessingCode.scCPUSocialCareATMAddCredit;
			break;
		case CPUXformRefundAddCredit:		/* CPU�l�B��m�[�� */
			s = ProcessingCode.scCPUXformRefundAddCredit;
			break;
		case CPUCancelAddCredit:			/* CPU�����[�� */
			s = ProcessingCode.scCPUCancelAddCredit;
			break;
		case CPUMerchandise:				/* CPU�ʳf */
			s = ProcessingCode.scCPUMerchandise;
			break;
		case CPUCancelMerchandise:			/* CPU�����ʳf */
			s = ProcessingCode.scCPUCancelMerchandise;
			break;
		case CPUXformRefundMerchandise:		/* CPU�l�B��m�ʳf */
			s = ProcessingCode.scCPUXformRefundMerchandise;
			break;
		case CPUSellCard:					/* CPU��d */
			s = ProcessingCode.scCPUSellCard;
			break;
		case CPUCancelSellCard:				/* CPU������d */
			s = ProcessingCode.scCPUCancelSellCard;
			break;
		case CPUExhibit:					/* CPU�i�� */
			s = ProcessingCode.scCPUExhibit;
			break;
		case CPUTXNAuth:					/* CPU����X�k���� */
			s = ProcessingCode.scCPUTXNAuth;
			break;
		case CPUReturnMerchandise:			/* CPU�h�f */
			s = ProcessingCode.scCPUReturnMerchandise;
			break;
		case CPUReturnCard:					/* CPU�h�d */
			s = ProcessingCode.scCPUReturnCard;
			break;
		case CPUXformRefundReturnCard:		/* CPU�l�B��m�h�d */
			s = ProcessingCode.scCPUXformRefundReturnCard;
			break;
		case CPUCoBranderCardAutoCredit:	/* CPU�p�W�d�۰ʥ[�ȥ\��}�� */
			s = ProcessingCode.scCPUCoBranderCardAutoCredit;
			break;
		}
		s = "<" + scProcessingCode + ">" + s + "</" + scProcessingCode + ">";
		return s;
	}

	/* n 2, �y�C�ddongle�����s�� */
	public static final String scDongleVer = "T0301";
	public static String sGetDongleVersion(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}
		for (int i = 0; i < bytes.length; i ++) {
			if (bytes[i] < '0' || bytes[i] > '9') {
				return "";
			}
		}
		String s = new String(bytes);
		s = "<" + scDongleVer + ">" + s + "</" + scDongleVer + ">";
		return s;
	}
	
	/* n <= 12, TXN AMT, ������B */
	public static final String scTXNAMT = "T0400";
	public static String sGetTXNAMT(long amount) {
		if (amount < 0) {
			return "";
		}
		String s = String.valueOf(amount);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scTXNAMT + ">" + s + "</" + scTXNAMT + ">";
		return s;
	}
	
	/* n <= 12, �y�C�d���ڪ��B */
	public static final String scDebitAmount = "T0403";
	public static String sGetDebitAmount(long amount) {
		if (amount < 0) {
			return "";
		}
		String s = String.valueOf(amount);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scDebitAmount + ">" + s + "</" + scDebitAmount + ">";
		return s;
	}
	
	/* n <= 12, �y�C�d�[�ȩΰh�ڪ��B */
	public static final String scAddOrReturnAmount = "T0407";
	public static String sGetAddOrReturnAmount(long amount) {
		if (amount < 0) {
			return "";
		}
		String s = String.valueOf(amount);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scAddOrReturnAmount + ">" + s + "</" + scAddOrReturnAmount + ">";
		return s;
	}
	
	/* n <= 12, Purse Balance, �y�C�d�����l�B, 3 binary byte to 10�i��Ʀr, Signed and LSB First, ���ȥ��a�k��0, �t�ȥ��a�k��FF */
	public static final String scPurseBalance = "T0408";
	public static String sGetPurseBalance(byte[] bytes) {
		if (bytes == null || bytes.length != 3) {
			return "";
		}
		byte[] b = new byte[4];
		for (int i = 0; i < 3; i ++) {
			b[i] = bytes[i];
		}
		if (b[2] == 0) {
			b[3] = 0;	// ����
		} else {
			b[3] = (byte) 0xFF; // �t��
		}
		int i = Util.sByteToInt(b, 0, false);
		String s = String.valueOf(i);
		s = "<" + scPurseBalance + ">" + s + "</" + scPurseBalance + ">";
		return s;
	}
	
	/* n <= 12, Single auto-load transaction amount, �y�C�d�۰ʥ[�Ȫ��B */
	public static final String scSingleAutoLoadTansacAmount = "T0409";
	public static String sGetSingleAutoLoadTansacAmount(long amount) {
		if (amount < 0) {
			return "";
		}
		String s = String.valueOf(amount);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scSingleAutoLoadTansacAmount + ">" + s + "</" + scSingleAutoLoadTansacAmount + ">";
		return s;
	}
	
	/* n <= 12, Purse Balance Before TXN, �y�C�d����e�l�B */
	public static final String scPurseBalanceBeforeTXN = "T0410";
	public static String sGetPurseBalanceBeforeTXN(long amount) {
		if (amount < 0) {
			return "";
		}
		String s = String.valueOf(amount);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scPurseBalanceBeforeTXN + ">" + s + "</" + scPurseBalanceBeforeTXN + ">";
		return s;
	}
	
	/* n 14, �����e����ɶ�, yyyymmddhhnnss */
	public static final String scPurseDeliverDateTime = "T0700";
	public static String sGetPurseDeliverDateTime() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmss");
		Date now = new Date();
		String s = sdfDate.format(now);
		s = "<" + scPurseDeliverDateTime + ">" + s + "</" + scPurseDeliverDateTime + ">";
		return s;
	}
	
	/* n 6, TM Serial Number, �ݥ�����Ǹ�, ����0��6�� */
	public static final String scTMSerialNumber = "T1100";
	public static String sGetTMSerialNumber(int sn, boolean bTag) {
		if (sn < 0 || sn > 999999) {
			return "";
		}
		String s = String.format("%06d", sn);
		if (bTag) {
			s = "<" + scTMSerialNumber + ">" + s + "</" + scTMSerialNumber + ">";
		}
		return s;
	}
	
	/* n 6, ���Ⱦ�����Ǹ�, ����0��6�� */
	public static final String scCashBoxSerialNumber = "T1101";
	public static String sGetCashBoxSerialNumber(int sn, boolean bTag) {
		if (sn < 0 || sn > 999999) {
			return "";
		}
		String s = String.format("%06d", sn);
		if (bTag) {
			s = "<" + scCashBoxSerialNumber + ">" + s + "</" + scCashBoxSerialNumber + ">";
		}
		return s;
	}
	
	/* n 6, EDC����ɶ�, hhmmss, bytes came from TM TXN Date Time 14 bytes */
	public static final String scEDCTime = "T1200";
	public static String sGetEDCTime(String time) {
		if (time == null || time.length() != 6) {
			return "";
		}
		String s = "<" + scEDCTime + ">" + time + "</" + scEDCTime + ">";
		return s;
	}
	
	/* n 6, TM TXN Time, hhmmss, ���Ⱦ�����ɶ�, �Y�L���Ⱦ��s�u, ��J�PT1200�ۦP�� */
	public static final String scTMTXNTime = "T1201";
	public static String sGetTMTXNTime(String time) {
		if (time == null || time.length() != 6) {
			return "";
		}
		String s = "<" + scTMTXNTime + ">" + time + "</" + scTMTXNTime + ">";
		return s;
	}
	
	/* n 8, EDC������, yyyymmdd */
	public static final String scEDCDate = "T1300";
	public static String sGetEDCDate(String date, boolean bTag) {
		if (date == null || date.length() != 8) {
			return "";
		}
		String s = date;
		if (bTag) {
			s = "<" + scEDCDate + ">" + s + "</" + scEDCDate + ">";
		}
		return s;
	}
	
	/* n 8, TM TXN Date, yyyymmdd, ���Ⱦ�������, �Y�L���Ⱦ��s�u, ��J�PT1300�ۦP�� */
	public static final String scTMTXNDate = "T1301";
	public static String sGetTMTXNDate(String date) {
		if (date == null || date.length() != 8) {
			return "";
		}
		String s = "<" + scTMTXNDate + ">" + date + "</" + scTMTXNDate + ">";
		return s;
	}
	
	/* n 4, Card expiry date, YYMM */
	public static final String scCardExpiryDate = "T1400";
	public static String sGetCardExpiryDate(String yymm) {
		if (yymm == null || yymm.length() != 4) {
			return "";
		}
		try {
			Integer.parseInt(yymm);
		} catch (NumberFormatException e) {
			return "";
		}
		String s = "<" + scCardExpiryDate + ">" + yymm + "</" + scCardExpiryDate + ">";
		return s;
	}
	
	/* n 8, Purse Exp. date, �y�C�d���Ĵ�, YYYYMMDD */
	public static final String scPurseExpiryDate = "T1402";
	public static String sGetPurseExpiryDate(String yyyymmdd) {
		if (yyyymmdd == null || yyyymmdd.length() != 8) {
			return "";
		}
		try {
			Integer.parseInt(yyyymmdd);
		} catch (NumberFormatException e) {
			return "";
		}
		String s = "<" + scPurseExpiryDate + ">" + yyyymmdd + "</" + scPurseExpiryDate + ">";
		return s;
	}
	
	/* n 8, New Purse Exp. date, �y�C�d���Ĵ�, YYYYMMDD */
	public static final String scNewPurseExpiryDate = "T1403";
	public static String sGetNewPurseExpiryDate(String yyyymmdd) {
		if (yyyymmdd == null || yyyymmdd.length() != 8) {
			return "";
		}
		try {
			Integer.parseInt(yyyymmdd);
		} catch (NumberFormatException e) {
			return "";
		}
		String s = "<" + scNewPurseExpiryDate + ">" + yyyymmdd + "</" + scNewPurseExpiryDate + ">";
		return s;
	}
	
	/* n 14, Retrieval Reference Number */
	public static final String scRetrievalReferenceNumber = "T3700";
	public static String sGetRetrievalReferenceNumber(String date, String sn) {
		if (date == null || date.length() != 8) {
			return "";
		}
		if (sn == null || sn.length() != 6) {
			return "";
		}
		String s = "<" + scRetrievalReferenceNumber + ">" + date + sn + "</" + scRetrievalReferenceNumber + ">";
		return s;
	}
	
	/* an <= 20, ���Ⱦ�����渹, ���Ⱦ��ǰe������ߤ@�s��, �Ω󦬻Ⱦ��Ľ]���Ⱦ��PEDC��������� */
	public static final String scCashBoxNumber = "T3701";
	public static String sGetCashBoxNumber(byte[] bytes) {
		if (bytes == null || bytes.length > 20) {
			return "";
		}
		String s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scCashBoxNumber + ">" + s + "</" + scCashBoxNumber + ">";
		return s;
	}
	
	/* an 6, ������v�X */
	public static final String scAuthorTranscNumber = "T3800";
	public static String sGetAuthorTranscNumber(byte[] bytes) {
		if (bytes == null || bytes.length != 6) {
			return "";
		} 
		String s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scAuthorTranscNumber + ">" + s + "</" + scAuthorTranscNumber + ">";
		return s;
	}
	
	/* an 2, Response Code */
	public static final String scResponseCode = "T3900";
	public static String sGetResponseCode(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}
		String s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scResponseCode + ">" + s + "</" + scResponseCode + ">";
		return s;
	}
	
	/* n, API return code */
	public static final String scAPIReturnCode = "T3901";
	public static String sGetAPIReturnCode(byte[] bytes) {
		if (bytes == null) {
			return "";
		}
		String s = Util.sGetAttr_n(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scAPIReturnCode + ">" + s + "</" + scAPIReturnCode + ">";
		return s;
	}
	
	/* an 2, EZ host response code */
	public static final String scEZResponseCode = "T3902";
	public static String sGetEZResponseCode(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}
		String s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scEZResponseCode + ">" + s + "</" + scEZResponseCode + ">";
		return s;
	}
	
	/* an 2, SVCS response code */
	public static final String scSVCSResponseCode = "T3903";
	public static String sGetSVCSResponseCode(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}
		String s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scSVCSResponseCode + ">" + s + "</" + scSVCSResponseCode + ">";
		return s;
	}
	
	/* n 4, Reader SW */
	public static final String scReaderSW = "T3904";
	public static String sGetReaderSW(byte[] bytes) {
		if (bytes == null || bytes.length != 4) {
			return "";
		}
		String s = Util.sGetAttr_n(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scReaderSW + ">" + s + "</" + scReaderSW + ">";
		return s;
	}

	/* an 4, ���b�дڮt��response code */
	public static final String scPayforBillDiff = "T3911";
	public static String sGetPayforBillDiff(byte[] bytes) {
		if (bytes == null || bytes.length != 4) {
			return "";
		}
		String s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scPayforBillDiff + ">" + s + "</" + scPayforBillDiff + ">";
		return s;
	}
	
	/* b 12, New device ID, �ݥ��]�ƥN��, �榡��zzyxxx�@6��bytes�Ѷ}��12��bytes, 
	 * xxx��New SP ID(T4200), y��new device type, zz��device ID�Ǹ� */
	public static final String scNewDeviceID = "T4100";
	public static String sGetNewDeviceID(byte[] bytes) {
		if (bytes == null || bytes.length != 6) {
			return "";
		}
		String s = Util.sGetAttr_b(bytes);
		s = "<" + scNewDeviceID + ">" + s + "</" + scNewDeviceID + ">";
		return s;
	}
	
	/* b 8, Device ID, �ª�Device ID, 4��bytes�Ѷ}��8��bytes, �e2�쬰��SP ID, ��SP ID����JCCHS */
	public static final String scDeviceID = "T4101";
	public static String sGetDeviceID(byte[] bytes) {
		if (bytes == null || bytes.length != 4) {
			return "";
		}
		String s = Util.sGetAttr_b(bytes);
		s = "<" + scDeviceID + ">" + s + "</" + scDeviceID + ">";
		return s;
	}
	
	/* an 15, �ݥ��]�ƥثeIP */
	public static final String scDeviceIP = "T4102";
	public static String sGetDeviceIP() {
		String s = Util.sGetLocalIpAddress();
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scDeviceIP + ">" + s + "</" + scDeviceIP + ">";
		return s;
	}
	
	/* an <= 30, �ݥ��]�ƾ����Ǹ� */
	public static final String scDeviceSerialNumber = "T4103";
	public static String sGetDeviceSerialNumber(String sn) {
		if (sn == null || sn.length() > 30) {
			return "";
		}
		String s = sn;
		s = "<" + scDeviceSerialNumber + ">" + s + "</" + scDeviceSerialNumber + ">";
		return s;
	}
	
	/* b 8, Reader ID, Reader�����Ǹ�, 4 bytes�Ѷ}��8 bytes */
	public static final String scReaderID = "T4104";
	public static String sGetReaderID(byte[] bytes) {
		if (bytes == null || bytes.length != 4) {
			return "";
		}
		String s = Util.sGetAttr_b(bytes);
		s = "<" + scReaderID + ">" + s + "</" + scReaderID + ">";
		return s;
	}
	
	/* an 8, New SP ID, �S���ө��N��, ��JNew Service Provider ID, 
	 * Unsigned and LSB first, 3bytes��Q�i��8bytes, ����0��8�� */
	public static final String scNewSPID = "T4200";
	public static String sGetNewSPID(String id) {
		if (id == null || id.length() != 8) {
			return "";
		}
		String s = "<" + scNewSPID + ">" + id + "</" + scNewSPID + ">";
		return s;
	}
	
	/* an <= 30, New Location ID, �����N�� */
	public static final String scNewLocationID30 = "T4201";
	public static String sGetNewLocationD30(byte[] bytes) {
		if (bytes == null || bytes.length > 30) {
			return "";
		}
		String s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return "";
		}
		s = "<" + scNewLocationID30 + ">" + s + "</" + scNewLocationID30 + ">";
		return s;
	}
	
	/* an <= 30, �s���q�� */
	public static final String scPhoneNumber = "T4202";
	public static String sGetPhoneNumber(String number) {
		if (number == null || number.length() > 30) {
			return "";
		}
		String s = "<" + scPhoneNumber + ">" + number + "</" + scPhoneNumber + ">";
		return s;
	}
	
	/* an <= 30, �s���q�� 2 */
	public static final String scPhoneNumber2 = "T4203";
	public static String sGetPhoneNumber2(String number) {
		if (number == null || number.length() > 30) {
			return "";
		}
		String s = "<" + scPhoneNumber2 + ">" + number + "</" + scPhoneNumber2 + ">";
		return s;
	}
	
	/* an <= 30, �ǯu���X */
	public static final String scFaxNumber = "T4204";
	public static String sGetFaxNumber(String number) {
		if (number == null || number.length() > 30) {
			return "";
		}
		String s = "<" + scFaxNumber + ">" + number + "</" + scFaxNumber + ">";
		return s;
	}
	
	/* an <= 200, ��~�a�} */
	public static final String scBizAddress = "T4205";
	public static String sGetBizAddress(String addr) {
		if (addr == null || addr.length() > 200) {
			return "";
		}
		String s = "<" + scBizAddress + ">" + addr + "</" + scBizAddress + ">";
		return s;
	}
	
	/* n <= 30, ��~�a�}�ϽX, ZIP code, �T�X�d��: '105', ���X�d��: '10548' */
	public static final String scZipCode = "T4206";
	public static String sGetZipCode(String code) {
		if (code == null || code.length() > 30) {
			return "";
		}
		String s = "<" + scZipCode + ">" + code + "</" + scZipCode + ">";
		return s;
	}
	
	/* a <= 30, ���ĩ�/�L�ĩ����O, 'A': ���ĩ�, 'B': ���ĩ� */
	public static final String scValidStoreMark = "T4207";
	public static String sGetValidStoreMark(boolean bValid) {
		String s = bValid? "A" : "B";
		s = "<" + scValidStoreMark + ">" + s + "</" + scValidStoreMark + ">";
		return s;
	}
	
	/* n 8, �����ҥΤ��, YYYYMMDD */
	public static final String scStoreOpenDate = "T4208";
	public static String sGetStoreOpenDate(String date) {
		if (date == null || date.length() != 6) {
			return "";
		}
		String s = "<" + scStoreOpenDate + ">" + date + "</" + scStoreOpenDate + ">";
		return s;
	}
	
	/* n 8, �����������, YYYYMMDD */
	public static final String scStoreCloseDate = "T4209";
	public static String sGetStoreCloseDate(String date) {
		if (date == null || date.length() != 6) {
			return "";
		}
		String s = "<" + scStoreCloseDate + ">" + date + "</" + scStoreCloseDate + ">";
		return s;
	}
	
	/* n <= 5, ��JNew Location ID, Unsigned and LSB First, 2bytes�ର10�i�����Ʀr, 
	 * ������(byte 1)����Location ID */
	public static final String scNewLocationID5 = "T4210";
	public static String sGetNewLocationID5(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}
		byte[] b = Arrays.copyOf(bytes, 4);
		int i = Util.sByteToInt(b, 0, false);
		if (i > 99999) { // �̤j5���
			return "";
		}
		String s = String.format("%d", i);
		s = "<" + scNewLocationID5 + ">" + s + "</" + scNewLocationID5 + ">";
		return s;
	}
	
	/* 
	 * Card ������� 
	 */
	
	/* b 2, Purse Version Number (PVN), 00: Mifare, 01: level 1, 02: level 2 */
	public static final String scPurseVersionNumber = "T4800";
	public enum purse_type {
		mifare,
		level1,
		level2
	};
	public static String sGetPurseVersionNumber(purse_type e) {
		String s = "";
		switch (e) {
		case mifare:
			s = "00";
			break;
		case level1:
			s = "01";
			break;
		case level2:
			s = "02";
			break;
		}
		s = "<" + scPurseVersionNumber + ">" + s + "</" + scPurseVersionNumber + ">";
		return s;
	}
	
	/* b, Last Credit TXN Log (Card AVR data), �d���̫�@���[�ȥ������, 
	 * 50 bytes for Gen1 card and 66 bytes for Gen2 card,  */
	public static final String scLastCreditTXNLog = "T4801";
	public class LastCreditTXNLogGen1 { // 25 bytes in total
		byte MSG_type;
		byte TXN_LSB_SNum;		// LSB
		byte[] TXN_date_time = new byte[4];
		byte Subtype;
		byte[] TXN_AMT = new byte[2];
		byte[] EV = new byte[2];				
		byte[] CardPhyID = new byte[4];
		byte IssuerCode;
		byte ServiceProvider;
		byte LocationID;
		byte[] DeviceID = new byte[4];
		byte[] BankCode;
		byte[] LoyaltyCounter = new byte[2];
	};
	public static String sGetLastCreditTXNLogGen1(LastCreditTXNLogGen1 g1) {
		String s = "";
		if (g1 == null) {
			return s;
		}
		
		s += Util.sGetAttr_b(g1.MSG_type);
		s += Util.sGetAttr_b(g1.TXN_LSB_SNum);
		s += Util.sGetAttr_b(g1.TXN_date_time);
		s += Util.sGetAttr_b(g1.Subtype);
		s += Util.sGetAttr_b(g1.TXN_AMT);
		s += Util.sGetAttr_b(g1.EV);
		s += Util.sGetAttr_b(g1.CardPhyID);
		s += Util.sGetAttr_b(g1.IssuerCode);
		s += Util.sGetAttr_b(g1.ServiceProvider);
		s += Util.sGetAttr_b(g1.LocationID);		
		s += Util.sGetAttr_b(g1.DeviceID);
		s += Util.sGetAttr_b(g1.BankCode);
		s += Util.sGetAttr_b(g1.LoyaltyCounter);
		s = "<" + scLastCreditTXNLog + ">" + s + "</" + scLastCreditTXNLog + ">";
		return s;
	}
	public class LastCreditTXNLogGen2 { // 33 bytes in total
		byte PurseVersionNumber; // PVN
		byte[] TSQN = new byte[3];
		byte[] TXN_date_time = new byte[4];
		byte Subtype;
		byte[] TXN_AMT = new byte[3];
		byte[] EV = new byte[3];
		byte[] CPUServiceProvideID = new byte[3];
		byte[] LocationID = new byte[2]; // PVN=2�ɬ��s�����N��, PVN<>2�ɬ��³����N��
		byte[] DeviceID = new byte[6]; // PVN=2�ɬ��s�����N��, PVN<>2�ɬ��³����N��
		byte RFU; // PVN=2�ɩT�w��02, PVN<>2�ɩT�w��00
		byte[] CPU_EV = new byte[3]; // �����l�B(��CPU�d�p��), PVN=02�ɦ���, ��L��000000
		byte[] CPU_TSQN = new byte[3]; // ����Ǹ�(��CPU�d�p��), PVN=02�ɦ���, ��L��000000
	};
	public static String sGetLastCreditTXNLogGen2(LastCreditTXNLogGen2 g2) {
		String s = "";
		if (g2 == null) {
			return s;
		}
		
		s += Util.sGetAttr_b(g2.PurseVersionNumber);
		s += Util.sGetAttr_b(g2.TSQN);
		s += Util.sGetAttr_b(g2.TXN_date_time);
		s += Util.sGetAttr_b(g2.Subtype);
		s += Util.sGetAttr_b(g2.TXN_AMT);
		s += Util.sGetAttr_b(g2.EV);
		s += Util.sGetAttr_b(g2.CPUServiceProvideID);
		s += Util.sGetAttr_b(g2.LocationID);
		s += Util.sGetAttr_b(g2.DeviceID);
		s += Util.sGetAttr_b(g2.RFU);		
		s += Util.sGetAttr_b(g2.CPU_EV);
		s += Util.sGetAttr_b(g2.CPU_TSQN);
		s = "<" + scLastCreditTXNLog + ">" + s + "</" + scLastCreditTXNLog + ">";
		return s;
	}
	
	/* b 2, Issuer Code, 02: �y�C�d���q, 04: �� */
	public static final String scIssuerCode = "T4802";
	public enum Issuer {
		easycard,
		keelung,
		unknown
	};
	public static String sGetIssuerCode(Issuer issuer) {
		String s = "";
		switch (issuer) {
		case easycard:
			s = "02";
			break;
		case keelung:
			s = "04";
			break;
		default:
			s = "00";
			break;
		}
		s = "<" + scIssuerCode + ">" + s + "</" + scIssuerCode + ">";
		return s;
	}
	
	/* b 2, Bank Code */
	public static final String scBankCode = "T4803";
	public enum bank_code {
		easycard,		// 00, �C�C�d���q
		cathaybank,		// 31, ����@��
		taishinbank,	// 32, �x�s
		ctbcbank,		// 33, ����H�U
		fubon,			// 34, �x�_�I��
		megabank,		// 36, ���װ�ڰӻ�
		firstbank,		// 37, �Ĥ@��
		hsbc,			// 38, ���׻Ȧ�
		esunbank,		// 39, �ɤs�Ȧ�
	};
	public static String sGetBankCode(bank_code e) {
		String s = "";
		switch (e) {
		case easycard:		// 00, �C�C�d���q
			s = "00";
			break;
		case cathaybank:	// 31, ����@��
			s = "31";
			break;
		case taishinbank:	// 32, �x�s
			s = "32";
			break;
		case ctbcbank:		// 33, ����H�U
			s = "33";
			break;
		case fubon:			// 34, �x�_�I��
			s = "34";
			break;
		case megabank:		// 36, ���װ�ڰӻ�
			s = "36";
			break;
		case firstbank:		// 37, �Ĥ@��
			s = "37";
			break;
		case hsbc:			// 38, ���׻Ȧ�
			s = "38";
			break;
		case esunbank:		// 39, �ɤs�Ȧ�
			s = "39";
			break;
		}
		s = "<" + scBankCode + ">" + s + "</" + scBankCode + ">";
		return s;
	}
	
	/* b 2, Area Code */
	public static final String scAreaCode = "T4804";
	public enum area_code {
		taipei,			// 01, �x�_��
		newtaipei,		// 02, �s�_��
		keelung,		// 03, �򶩥�
		taoyuan,		// 04, ��鿤
		yilan,			// 05, �y��
		matsu,			// 06, �s����
		taipeilove,		// 0E, �x�_���R�ߤG(personal profile������03)
		newtaipeilove	// 0F, �s�x�_���R�ߤG(personal profile������03)
	};
	public static String sGetAreaCode(area_code e) {
		String s = "";
		switch (e) {
		case taipei:		// 01, �x�_��
			s = "01";
			break;
		case newtaipei:		// 02, �s�_��
			s = "02";
			break;
		case keelung:		// 03, �򶩥�
			s = "03";
			break;
		case taoyuan:		// 04, ��鿤
			s = "04";
			break;
		case yilan:			// 05, �y��
			s = "05";
			break;
		case matsu:			// 06, �s����
			s = "06";
			break;
		case taipeilove:	// 0E, �x�_���R�ߤG(personal profile������03)
			s = "0E";
			break;
		case newtaipeilove:	// 0F, �s�x�_���R�ߤG(personal profile������03)
			s = "0F";
			break;
		}
		s = "<" + scAreaCode + ">" + s + "</" + scAreaCode + ">";
		return s;
	}

	/* b 4, CPU Sub Area Code, Unsigned and LSB First */
	public static final String scCPUSubAreaCode = "T4805";
	public static String sGetCPUSubAreaCode(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}
		String s  = Util.sGetAttr_b(bytes);
		s = "<" + scCPUSubAreaCode + ">" + s + "</" + scCPUSubAreaCode + ">";
		return s;
	}
	
	/* b 8, ProfileExp. Date, ���������, UnixDateTime, Unsigned and LSB First */
	public static final String scProfileExpDate = "T4806";
	public static String sGetProfileExpDate(byte[] bytes) {
		if (bytes == null || bytes.length != 4) {
			return "";
		}
		String s  = Util.sGetAttr_b(bytes);
		s = "<" + scProfileExpDate + ">" + s + "</" + scProfileExpDate + ">";
		return s;
	}

	/* b 8, New ProfileExp. Date, �s���������, UnixDateTime, Unsigned and LSB First */
	public static final String scNewProfileExpDate = "T4807";
	public static String sGetNewProfileExpDate(byte[] bytes) {
		if (bytes == null || bytes.length != 4) {
			return "";
		}
		String s  = Util.sGetAttr_b(bytes);
		s = "<" + scNewProfileExpDate + ">" + s + "</" + scNewProfileExpDate + ">";
		return s;
	}

	/* n 6, TSQN, ���d����Ǹ�, Unsigned and LSB, Mifare: ��J2bytes�ର�Q�i���, CPU: ��J3bytes�ର�Q�i��� */
	public static final String scTSQN = "T4808";
	public static String sGetTSQN(byte[] bytes) {
		if (bytes == null || bytes.length != 3) {
			return "";
		}
		byte[] b = Arrays.copyOf(bytes, 4);
		int i = Util.sByteToInt(b, 0, false);
		String s = String.format("%06d", i);
		s = "<" + scTSQN + ">" + s + "</" + scTSQN + ">";
		return s;
	}
	
	/* b 2, TM, ����Ҧ����� (TXN Mode, �ѳ]�Ʋ���) */
	public static final String scTM = "T4809";
	public static String sGetTM(byte b) {
		String s = Util.sGetAttr_b(b);
		s = "<" + scTM + ">" + s + "</" + scTM + ">";
		return s;
	}
	
	/* b 2, TQ, ����ݩʳ]�w�� (TXN Qualifier, �ѳ]�Ʋ���) */
	public static final String scTQ = "T4810";
	public static String sGetTQ(byte b) {
		String s = Util.sGetAttr_b(b);
		s = "<" + scTQ + ">" + s + "</" + scTQ + ">";
		return s;
	}
	
	/* an 6, TXN SN. before TXN, ����e������Ǹ� */
	public static final String scTXNSNBeforeTXN = "T4811";
	public static String sGetTXNSNBeforeTXN(byte[] bytes) {
		if (bytes == null || bytes.length != 6) {
			return "";
		}
		String s = Util.sGetAttr_an(bytes);
		s = "<" + scTXNSNBeforeTXN + ">" + s + "</" + scTXNSNBeforeTXN + ">";
		return s;
	}
	
	/* b 6, CTC, �G�N�d */
	public static final String scCTC = "T4812";
	public static String sGetCTC(byte[] bytes) {
		if (bytes == null || bytes.length != 3) {
			return "";
		}
		String s = Util.sGetAttr_b(bytes);
		s = "<" + scCTC + ">" + s + "</" + scCTC + ">";
		return s;
	}
	
	/* n <= 12, Loyalty counter, �̲��d��l��ƶǰe */
	public static final String scLoyaltyCounter = "T4813";
	public static String sGetLoyaltyCounter(long counter) {
		if (counter < 0) {
			return "";
		}
		String s = String.valueOf(counter);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scLoyaltyCounter + ">" + s + "</" + scLoyaltyCounter + ">";
		return s;
	}
	
	/* n <= 12, Deposit, ��� */
	public static final String scDeposit = "T4814";
	public static String sGetDeposit(long amount) {
		if (amount < 0) {
			return "";
		}
		String s = String.valueOf(amount);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scDeposit + ">" + s + "</" + scDeposit + ">";
		return s;
	}
	
	/* n <= 5, New Refund Fee, �h�d����O, 2 bytes (Unsigned and LSB)�ର10�i��Ʀr */
	public static final String scNewRefundFee = "T4815";
	public static String sGetNewRefundFee(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}	
		byte[] b = Arrays.copyOf(bytes, 4);
		int i = Util.sByteToInt(b, 0, false);
		String s = String.format("%d", i);
		if (s.length() > 5) {
			return "";
		}
		s = "<" + scNewRefundFee + ">" + s + "</" + scNewRefundFee + ">";
		return s;
	}
	
	/* an <= 5, Broken Fee, ���d�l���O, 2 bytes (Unsigned and LSB)�ର10�i��Ʀr */
	public static final String scBrokenFee = "T4816";
	public static String sGetBrokenFee(byte[] bytes) {
		if (bytes == null || bytes.length != 2) {
			return "";
		}	
		byte[] b = Arrays.copyOf(bytes, 4);
		int i = Util.sByteToInt(b, 0, false);
		String s = String.format("%d", i);
		if (s.length() > 5) {
			return "";
		}
		s = "<" + scBrokenFee + ">" + s + "</" + scBrokenFee + ">";
		return s;
	}
	
	/* an <= 5, Customer Fee, �h�d�ӶD�O, 2 bytes (Unsigned and LSB)�ର10�i��Ʀr */
	public static final String scCustomerFee = "T4817";
	public static String sGetCustomerFee(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 2) {
			return s;
		}	
		byte[] b = Arrays.copyOf(bytes, 4);
		int i = Util.sByteToInt(b, 0, false);
		s = String.format("%d", i);
		if (s.length() > 5) {
			return "";
		}
		s = "<" + scCustomerFee + ">" + s + "</" + scCustomerFee + ">";
		return s;
	}
	
	/* n <= 12, Another EV, �̲��d��l��ƶǰe */
	public static final String scAnotherEV = "T4818";
	public static String sGetAnotherEV(long value) {
		String s = "";
		if (value < 0) {
			return s;
		}
		s = String.valueOf(value);
		if (s.length() > 12) {
			return "";
		}
		s = "<" + scAnotherEV + ">" + s + "</" + scAnotherEV + ">";
		return s;
	}
	
	/* an 2, ��d��] */
	public static final String scCardLockReson = "T4819";
	public static String sGetCardLockReson(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 2) {
			return s;
		}
		s = Util.sGetAttr_an(bytes);
		if (s.length() == 0) {
			return s;
		}
		s = "<" + scCardLockReson + ">" + s + "</" + scCardLockReson + ">";
		return s;
	}
	
	/* b 2, Spec. version number, Host�ѧO���� */
	public static final String scSpecVersionNumber = "T4820";
	public static String sGetSpecVersionNumber(byte b) {
		String s = Util.sGetAttr_b(b);
		if (s.length() == 0) {
			return s;
		}
		s = "<" + scSpecVersionNumber + ">" + s + "</" + scSpecVersionNumber + ">";
		return s;
	}
	
	/* b 8, Card Parameters, ���d�Ѽ� */
	public static final String scCardParameters = "T4821";
	public static String sGetCardParameters(byte[] cardOneDayQuota, byte[] cardOneDayQuotaDate) {
		if (cardOneDayQuota == null || cardOneDayQuota.length != 2) {
			return "";
		}
		if (cardOneDayQuotaDate == null || cardOneDayQuotaDate.length != 2) {
			return "";
		}
		String s = Util.sGetAttr_b(cardOneDayQuotaDate);
		if (s.length() == 0) {
			return "";
		}
		String s1 = Util.sGetAttr_b(cardOneDayQuotaDate);
		if (s1.length() == 0) {
			return "";
		}
		s = "<" + scCardParameters + ">" + s + s1 + "</" + scCardParameters + ">";
		return s;
	}
	
	/* 2 bytes, �p�B���O�魭�B�g�J��m */
	public static final String scCPUOneDayQuotaWriteFlag = "T4823";
	public static String sGetCPUOneDayQuotaWriteFlag(OneDayQuotaWriteForMicroPayment e) {
		String s = "";
		switch (e) {
		case NMNC: 		// T=Mifare�PT-CPU�����g�J
			s = "00";
			break;
		case WMNC: 		// T=Mifare�g�J, T-CPU���g�J
			s = "01";
			break;
		case NMWC: 		// T=Mifare���g�J, T-CPU�g�J
			s = "10";
			break;
		case WNWC:  	// T=Mifare�PT-CPU���g�J
			s = "11";
			break;
		}
		s = "<" + scCPUOneDayQuotaWriteFlag + ">" + s + "</" + scCPUOneDayQuotaWriteFlag + ">";
		return s;
	}
	
	/* n 2, CPU CPD read flag */
	public static final String scCPUCPDReadFlag = "T4824";
	public static String sGetCPUCPDReadFlag(CPDReadFlag flag) {
		String s = "";
		switch (flag) {
		case YRHostNCReader: // Ū�^Host�BReader������
			s = "00"; // ��Ū��CPD
			break;
		case NRHostYCReader: // ��Ū�^Host�BReader�n����
			s = "10"; // Ū��CPD
			break;
		case YRHostYCReader: // Ū�^Host�BReader�n����
			s = "10"; // Ū��CPD
			break;
		default: // ��Ū�^Host�BReader������
			s = "00"; // ��Ū��CPD
			break;
		}
		s = "<" + scCPUCPDReadFlag + ">" + s + "</" + scCPUCPDReadFlag + ">";
		return s;
	}
	
	/* n 2, CPU credit balance change flag */
	public static final String scCPUCreditBalanceChangeFlag = "T4825";
	public static String sGetCPUCreditBalanceChangeFlag(boolean bChange) {
		String s = "";
		if (bChange) {
			s = "01"; // ACB�w�ܧ�
		} else {
			s = "00"; // ACB���ܧ�
		}
		s = "<" + scCPUCreditBalanceChangeFlag + ">" + s + "</" + scCPUCreditBalanceChangeFlag + ">";
		return s;
	}
	
	/* n 1, Card physical ID length, 4 or 7 */
	public static final String scCardPhysicalIDLength = "T4826";
	public static String sGetCardPhysicalIDLength(int length) {
		if (length != 4 || length != 7) {
			return "";
		}
		
		String s = String.valueOf(length);		
		s = "<" + scCardPhysicalIDLength + ">" + s + "</" + scCardPhysicalIDLength + ">";
		return s;
	}
	
	/* b 10, CPU Card Parameters, 
	 * 1. Card One Day Quota, b 6, ����e�d���֭p�魭�B�B��,
	 * 2. Card One Day Quota Date, b 4, ����e�d���֭p�魭�B�B�פ�� */
	public static final String scCPUCardParameters = "T4827";
	public static String sGetCPUCardParameters(byte[] CardOneDayQuota, byte[] CardOneDayQuotaDate) {
		if (CardOneDayQuota == null || CardOneDayQuota.length != 3) {
			return "";
		}
		if (CardOneDayQuotaDate == null || CardOneDayQuotaDate.length != 2) {
			return "";
		}
		String s = "";
		s = Util.sGetAttr_b(CardOneDayQuota);
		s += Util.sGetAttr_b(CardOneDayQuotaDate);
		s = "<" + scCPUCardParameters + ">" + s + "</" + scCPUCardParameters + ">";
		return s;
	}
	
	/* b 20, Mifare setting data, 
	 * 1. Personal profile authorization, b 2, Mifare S1B1
	 * 2. RFU, b 18, �O�d */
	public static final String scMifareSettingData = "T4828";
	public static String sGetMifareSettingData(byte[] PersonalProfileAuthor, byte[] RFU) {
		if (PersonalProfileAuthor == null || PersonalProfileAuthor.length != 1) {
			return "";
		}
		if (RFU == null || RFU.length != 9) {
			return "";
		}
		String s = "";
		s = Util.sGetAttr_b(PersonalProfileAuthor);
		s += Util.sGetAttr_b(RFU);
		s = "<" + scMifareSettingData + ">" + s + "</" + scMifareSettingData + ">";
		return s;
	}
	
	/* b 32, CPU Card Setting Parameter, 
	 * 1. Micro Payment Setting, b 2, CPU SFI1 CID
	 * 2. RFU, b 30, �O�d */
	public static final String scCPUCardSettingParameter = "T4829";
	public static String sGetCPUCardSettingParameter(byte[] MicroPaymentSetting, byte[] RFU) {
		if (MicroPaymentSetting == null || MicroPaymentSetting.length != 1) {
			return "";
		}
		if (RFU == null || RFU.length != 15) {
			return "";
		}
		String s = "";
		s = Util.sGetAttr_b(MicroPaymentSetting);
		s += Util.sGetAttr_b(RFU);
		s = "<" + scCPUCardSettingParameter + ">" + s + "</" + scCPUCardSettingParameter + ">";
		return s;
	}
	
	/* b 1, Read Purse Flag, 0=���ˬd(�����P�@���d), 1=�ˬd(�ݦP�@���d) */
	public static final String scReadPurseFlag = "T4830";
	public static String sGetReadPurseFlag(boolean bCheck) {
		String s = "0";
		if (bCheck) {
			s = "1";
		}
		s = "<" + scReadPurseFlag + ">" + s + "</" + scReadPurseFlag + ">";
		return s;
	}
	
	/* b 2, SAM Key Version Numner (KVN), Key set + Key version
	 * b7: RFU 
	 * b6: Confirm key set, 0: normal, 1: backup
	 * b5: Host key set, 0: normal, 1: backup
	 * b4: SAM key set, 0: normal, 1: backup
	 * b0~3: Key version (ex. 0001b)
	 * �����@�NMAC (T6403)
	 */
	public static final String scSAMKVN = "T5301";
	public static String sGetSAMKVN(byte b) {
		String s = "";
		s = Util.sGetAttr_b(b);
		s = "<" + scSAMKVN + ">" + s + "</" + scSAMKVN + ">";
		return s;
	}
	
	/* b 6, CPU Card Key Info */
	public static final String scCPUCardKeyInfo = "T5302";
	public class CPUCardKeyInfo {
		byte adminKeyKVN;
		byte creditKeyKVN;
		byte debitKeyKVN;
	};
	public static String sGetCPUCardKeyInfo(CPUCardKeyInfo info) {
		String s = "";
		if (info == null) {
			return s;
		}
		s = Util.sGetAttr_b(info.adminKeyKVN);
		s += Util.sGetAttr_b(info.creditKeyKVN);
		s += Util.sGetAttr_b(info.debitKeyKVN);
		s = "<" + scCPUCardKeyInfo + ">" + s + "</" + scCPUCardKeyInfo + ">";
		return s;
	}
	
	/* b 2, CPU Hash Type */
	public static final String scCPUHashType = "T5303";
	public static String sGetCPUHashType(byte b) {
		String s = "";
		s = Util.sGetAttr_b(b);
		s = "<" + scCPUHashType + ">" + s + "</" + scCPUHashType + ">";
		return s;
	}
	
	/* b 2, CPU host admin KVN */
	public static final String scCPUHostAdminKVNKVN = "T5304";
	public static String sGetCPUHostAdminKVNKVN(byte b) {
		String s = "";
		s = Util.sGetAttr_b(b);
		s = "<" + scCPUHostAdminKVNKVN + ">" + s + "</" + scCPUHostAdminKVNKVN + ">";
		return s;
	}
	
	/* ??????????????????? */
	public static final String scCPUEDC = "T5306";
	/* ??????????????????? */
	
	/* b 16, RSAM */
	public static final String scRSAM = "T5307";
	public static String sGetRSAM(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 8) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scRSAM + ">" + s + "</" + scRSAM + ">";
		return s;
	}
	
	/* T5308, RHOST, M/ /  */
	public static final String scRHOST = "T5308";
	public static String sGetRHOST(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 8) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scRHOST + ">" + s + "</" + scRHOST + ">";
		return s;
	}
	
	/* b 16, SAM ID, 8 bytes Unsigned and MSB First unpack to 16 bytes */
	public static final String scSAMID = "T5361";
	public static String sGetSAMID(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 8) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scSAMID + ">" + s + "</" + scSAMID + ">";
		return s;
	}
	
	/* b 8, SAM SN, 4 bytes Unsigned and MSB First unpack to 8 bytes */
	public static final String scSAMSN = "T5362";
	public static String sGetSAMSN(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 4) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scSAMSN + ">" + s + "</" + scSAMSN + ">";
		return s;
	}
	
	/* b 16, SAM CRN, 8 bytes Unsigned and MSB First unpack to 16 bytes */
	public static final String scSAMCRN = "T5363";
	public static String sGetSAMCRN(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 8) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scSAMCRN + ">" + s + "</" + scSAMCRN + ">";
		return s;
	}
	
	/* b 156, CPU SAM Info, 78 bytes unpack to 156 bytes */
	public static final String scCPUSAMInfo = "T5364";
	public class CPUSAMInfo {
		public byte SAMVersionNumber;			// SAM Version Number
		public byte[] SAMUsageControl;			// 3 bytes, SAM Usage Control 
		public byte SAMAdminKVN;				// SAM Admin KVN
		public byte SAMIssuerKVN;				// SAM Issuer KVN
		public byte[] TagListTable;				// 40 bytes, Tag List Table
		public byte[] SAMIssuerSpecificData;	// 32 bytes, SAM Issuer Specific Data 
	};
	public static String sGetCPUSAMInfo(CPUSAMInfo info) {
		String s = "";
		if (info == null || info.SAMUsageControl == null || info.SAMUsageControl.length != 3 ||  
				info.TagListTable == null || info.TagListTable.length != 40 || 
				info.SAMIssuerSpecificData == null || info.SAMIssuerSpecificData.length != 32) {
			return s;
		}
		s = Util.sGetAttr_b(info.SAMVersionNumber);
		s += Util.sGetAttr_b(info.SAMUsageControl);
		s += Util.sGetAttr_b(info.SAMAdminKVN);
		s += Util.sGetAttr_b(info.SAMIssuerKVN);
		s += Util.sGetAttr_b(info.TagListTable);
		s += Util.sGetAttr_b(info.SAMIssuerSpecificData);
		s = "<" + scCPUSAMInfo + ">" + s + "</" + scCPUSAMInfo + ">";
		return s;
	}
	
	/* b 24, SAM Transaction Info, 12 bytes unpack to 24 bytes */
	public static final String scSAMTransactionInfo = "T5365";
	public class SAMTransactionInfo {
		public byte[] acl;	// 3 bytes, Authodrized Credit Limit
		public byte[] acb;	// 3 bytes, Authodrized Credit Balance
		public byte[] acc;	// 3 bytes, Authodrized Credit Cumulative
		public byte[] accc;	// 3 bytes, Authodrized Cancel Credit Cumulative
	}
	public static String sGetSAMTransactionInfo(SAMTransactionInfo info) {
		String s = "";
		if (info == null || info.acl == null || info.acl.length != 3 ||  
				info.acb == null || info.acb.length != 3 || 
				info.acc == null || info.acc.length != 3 ||
				info.accc == null || info.accc.length != 3) {
			return s;
		}
		s = Util.sGetAttr_b(info.acl);
		s += Util.sGetAttr_b(info.acb);
		s += Util.sGetAttr_b(info.acc);
		s += Util.sGetAttr_b(info.accc);
		s = "<" + scSAMTransactionInfo + ">" + s + "</" + scSAMTransactionInfo + ">";
		return s;
	}
	
	/* b 6, Single Credit TXN AMT Limit, 3 bytes unpack to 6 bytes */
	public static final String scSingleCreditTXNAMTLimit = "T5366";
	public static String sGetSingleCreditTXNAMTLimit(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 3) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scSingleCreditTXNAMTLimit + ">" + s + "</" + scSingleCreditTXNAMTLimit + ">";
		return s;
	}
	
	/* b 114, CPU SAM Parameter Setting Data */
	public static final String scCPUSAMParameterSettingData = "T5367";
	public class CPUSAMParameterSettingData {
		byte SAMUpdateOption;		// SAM Update Option 
		byte[] NewSAMValue;			// 40 bytes, New SAM Value 
		byte[] UpdateSAMValueMAC;	// 16 bytes, Update SAM Value MAC 
	};
	public static String sGetCPUSAMParameterSettingData(CPUSAMParameterSettingData data) {
		String s = "";
		if (data == null || data.NewSAMValue == null || data.NewSAMValue.length != 40 ||
				data.UpdateSAMValueMAC == null || data.UpdateSAMValueMAC.length != 16) {
			return s;
		}
		s = Util.sGetAttr_b(data.SAMUpdateOption);
		s += Util.sGetAttr_b(data.NewSAMValue);
		s += Util.sGetAttr_b(data.UpdateSAMValueMAC);
		s = "<" + scCPUSAMParameterSettingData + ">" + s + "</" + scCPUSAMParameterSettingData + ">";
		return s;
	}
	
	/* b 8, STC (SAM TXN Counter), 4 bytes unpack to 8 bytes */
	public static final String scSTC = "T5368";
	public static String sGetSTC(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 4) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scSTC + ">" + s + "</" + scSTC + ">";
		return s;
	}
	
	/* b 2, SAM sign on control flag */
	public static final String scSAMSignOnControlFlag = "T5369";
	public static String sGetSAMSignOnControlFlag(SAMSignOnControlFlag e) {
		String s = "";
		switch (e) {
		case NoSignOnForBoth: 	// ��iSAM�d������SignOn(���u)
			s = "00";
			break;
		case SignOnForNewOnly: 	// �uSignOn�sSAM�d(�^�OMifare��)
			s = "01";
			break;
		case SignOnForOldOnly: 	// �uSignOn��SAM�d
			s = "10";
			break;
		case SignOnForBoth: 	// ��iSAM�d���nSignOn
			s = "11";
			break;
		}
		s = "<" + scSAMSignOnControlFlag + ">" + s + "</" + scSAMSignOnControlFlag + ">";
		return s;
	}
	
	/* b 66, CPU Last Sign On Info, 33 bytes unpack to 66 bytes */
	public static final String scCPULastSignOnInfo = "T5370";
	public class CPULastSignOnInfo {
		byte[] PreviousNewDeviceID;		// 6 bytes, Unsigned and LSB First
		byte[] PreviousSTC;				// 4 bytes, SAM TXN Counter, Unsigned and MSB First
		byte[] PreviousTXNDateTime;		// 4 bytes, Unix Time
		byte PreviousCreditBalanceChangeFlag;	// 1 byte, �[�ȱ��v�B��(ACB)�ܧ�X��
		byte[] PreviousConfirmCode;		// 2 bytes, Status code 1 + Status code 2
		byte[] PreviousCACrypto;		// 16 bytes, Credit Authorization Cryptogram
	};
	public static String sGetCPULastSignOnInfo(CPULastSignOnInfo info) {
		String s = "";
		if (info == null || info.PreviousNewDeviceID == null || info.PreviousNewDeviceID.length != 6 ||  
				info.PreviousSTC == null || info.PreviousSTC.length != 4 || 
				info.PreviousTXNDateTime == null || info.PreviousTXNDateTime.length != 4 ||
				info.PreviousConfirmCode == null || info.PreviousConfirmCode.length != 2 ||
				info.PreviousCACrypto == null || info.PreviousCACrypto.length != 16) {
			return s;
		}
		s = Util.sGetAttr_b(info.PreviousNewDeviceID);
		s += Util.sGetAttr_b(info.PreviousSTC);
		s += Util.sGetAttr_b(info.PreviousTXNDateTime);
		s += Util.sGetAttr_b(info.PreviousCreditBalanceChangeFlag);
		s += Util.sGetAttr_b(info.PreviousConfirmCode);
		s += Util.sGetAttr_b(info.PreviousCACrypto);
		s = "<" + scCPULastSignOnInfo + ">" + s + "</" + scCPULastSignOnInfo + ">";
		return s;
	}
	
	/* b 16, SID (SAM ID) */
	public static final String scSID = "T5371";
	public static String sGetSID(byte[] bytes) {
		String s = "";
		if (bytes == null || bytes.length != 8) {
			return s;
		}
		s = Util.sGetAttr_b(bytes);
		s = "<" + scSID + ">" + s + "</" + scSID + ">";
		return s;
	}
	
	/* b 8, �妸���X, M/ /M */
	public static final String scBatchNumber = "T5501";
	private static int sSerialNumber = 0;
	public static String sGetBatchNumber(String date) {
		if (date == null || date.length() != 6) {
			return "";
		}
		String s = date;
		s += String.format("%02d", sSerialNumber ++);
		if (sSerialNumber >= 100) {
			sSerialNumber = 0;
		}
		s = "<" + scBatchNumber + ">" + s + "</" + scBatchNumber + ">";
		return s;
	}
	
	/* an 10, TM Location ID, �S���ө�������M/ /M */
	public static final String scTMLocationID = "T5503";
	public static String sGetTMLocationID(String id) {
		if (id == null || id.length() != 10) {
			return "";
		}
		String s = "<" + scTMLocationID + ">" + id + "</" + scTMLocationID + ">";
		return s;
	}
	
	/* an 2, TM ID, ���Ⱦ�����, M/ /M */
	public static final String scTMID = "T5504";
	public static String sGetTMID(String id) {
		if (id == null || id.length() != 2) {
			return "";
		}
		String s = "<" + scTMID + ">" + id + "</" + scTMID + ">";
		return s;
	}
	
	/* ?b 4?, TM agent number, ���ȭ��N��, M/ /M */
	public static final String scTMAgentNumber = "T5510";
	public static String sGetTMAgentNumber(String number) {
		if (number == null || number.length() != 4) {
			return "";
		}
		String s = "<" + scTMAgentNumber + ">" + number + "</" + scTMAgentNumber + ">";
		return s;
	}
	
	/* var, ������T, M/M/, ���r�ݥ��]�ƪ��A��T55880x */
	public static final String scVersionInfo = "T5588";
	public enum VersionInfo_type {
		ssl,			// SSL����
		blackList,		// �¦W�檩��
		devProgVer,		// �ݥ��]�Ƶ{������
		devParamVer,	// �ݥ��]�ưѼƪ���
		dongleProg		// Dongle�{������
	};
		/* an 2, ���A��T����, 01: SSL����, 02: �¦W�檩��, 03: �ݥ��]�Ƶ{������, 04: �ݥ��]�ưѼƪ���, 05: Dongle�{������ */
		public static final String scVersionInfo_type = "T558801";
		public static final String scVersionInfo_type_ssl = "01";
		public static final String scVersionInfo_type_backList = "02";
		public static final String scVersionInfo_type_devProgVer = "03";
		public static final String scVersionInfo_type_devParamVer = "04";
		public static final String scVersionInfo_type_dongleProg = "05";
		/* 0~30bytes, ���A��T�l����, Key����������J, �¦W���ɮ�����, �ݥ��]�Ƶ{���N��program ID, �ݥ��]�ưѼƤ���J*/
		public static final String scVersionInfo_subtype = "T558802";
		/* an 0~30, ���A��T�������e, SSL�����s��, �¦W�檩���s��, �ݥ��]�Ƶ{�������s��, Dongle�{�������s��, ��L�����s�� */
		public static final String scVersionInfo_content = "T558803";
	public class VersionInfo {
		VersionInfo_type type;
		String VersionInfo_type;
		String VersionInfo_subtype;
		String VersionInfo_content;
	};
	public String sGetVersionInfo(VersionInfo info) {
		String s = "";
		if (info == null) {
			return s;
		}
		
		switch (info.type) {
		case ssl:			// SSL����
			s += s = "<" + scVersionInfo_type + ">" + "01" + "</" + scVersionInfo_type + ">";
			if (info.VersionInfo_content == null) {
				s += s = "<" + scVersionInfo_content + ">" + "</" + scVersionInfo_content + ">";
			} else {
				s += s = "<" + scVersionInfo_content + ">" + info.VersionInfo_content + "</" + scVersionInfo_content + ">";
			}
			break;
		case blackList:	// �¦W�檩��
			s += s = "<" + scVersionInfo_type + ">" + "02" + "</" + scVersionInfo_type + ">";
			if (info.VersionInfo_subtype == null) {
				s += s = "<" + scVersionInfo_subtype + ">" + "</" + scVersionInfo_subtype + ">";
			} else {
				s += s = "<" + scVersionInfo_subtype + ">" + info.VersionInfo_subtype + "</" + scVersionInfo_subtype + ">";
			}
			if (info.VersionInfo_content == null) {
				s += s = "<" + scVersionInfo_content + ">" + "</" + scVersionInfo_content + ">";
			} else {
				s += s = "<" + scVersionInfo_content + ">" + info.VersionInfo_content + "</" + scVersionInfo_content + ">";
			}
			break;
		case devProgVer:	// �ݥ��]�Ƶ{������
			s += s = "<" + scVersionInfo_type + ">" + "03" + "</" + scVersionInfo_type + ">";
			if (info.VersionInfo_subtype == null) {
				s += s = "<" + scVersionInfo_subtype + ">" + "</" + scVersionInfo_subtype + ">";
			} else {
				s += s = "<" + scVersionInfo_subtype + ">" + info.VersionInfo_subtype + "</" + scVersionInfo_subtype + ">";
			}
			if (info.VersionInfo_content == null) {
				s += s = "<" + scVersionInfo_content + ">" + "</" + scVersionInfo_content + ">";
			} else {
				s += s = "<" + scVersionInfo_content + ">" + info.VersionInfo_content + "</" + scVersionInfo_content + ">";
			}
			break;
		case devParamVer:	// �ݥ��]�ưѼƪ���
			s += s = "<" + scVersionInfo_type + ">" + "04" + "</" + scVersionInfo_type + ">";
			if (info.VersionInfo_content == null) {
				s += s = "<" + scVersionInfo_content + ">" + "</" + scVersionInfo_content + ">";
			} else {
				s += s = "<" + scVersionInfo_content + ">" + info.VersionInfo_content + "</" + scVersionInfo_content + ">";
			}
			break;
		case dongleProg:
			s += s = "<" + scVersionInfo_type + ">" + "05" + "</" + scVersionInfo_type + ">";
			s += s = "<" + scVersionInfo_content + ">" + "???" + "</" + scVersionInfo_content + ">";
			break;
		}
		s = "<" + scVersionInfo + ">" + s + "</" + scVersionInfo + ">";
		return s;
	}
	
	/* n 6, ����`���, �Ҧ�message type=0200���[�`, �Hbatchno���p���¦ */
	public static final String scTXNCounts = "T5591";
	
	/* var, ��ƶǰe����,  */
	public static final String scDataXferControl = "T5596";
		/* n 8, �`���� */
		public static final String scDataXferControl_total = "T559601";
		/* n 8, �w�ǰe���� */
		public static final String scDataXferControl_xferred = "T559602";
		/* n 8, �w�������� */
		public static final String scDataXferControl_received = "T559603";
		/* n 8, �ǰe�Ǹ� */
		public static final String scDataXferControl_sn = "T559604";
	public class DataXferControl{
		int total; 			/* n 8, �`���� */
		int xferred;		/* n 8, �w�ǰe���� */
		int received;		/* n 8, �w�������� */		
		int sn;				/* n 8, �ǰe�Ǹ� */
	};
	public String sGetDataXferControl(DataXferControl control) {
		String s = "";
		if (control == null) {
			return "";
		}
		String total = String.format("%08d", control.total);
		s = "<" + scDataXferControl_total + ">" + total + "</" + scDataXferControl_total + ">";
		String xferred = String.format("%08d", control.xferred);
		s += "<" + scDataXferControl_xferred + ">" + xferred + "</" + scDataXferControl_xferred + ">";
		String received = String.format("%08d", control.received);
		s += "<" + scDataXferControl_received + ">" + received + "</" + scDataXferControl_received + ">";
		String sn = String.format("%08d", control.sn);
		s += "<" + scDataXferControl_sn + ">" + sn + "</" + scDataXferControl_sn + ">";
		s = "<" + scDataXferControl + ">" + s + "</" + scDataXferControl + ">";
		return s;
	}
	
	/* b 12, Reader firmware version, M/ /  */
	public static final String scReaderFWVersion = "T6000";
	public static String sGetReaderFWVersion(byte[] bytes) {
		if (bytes == null || bytes.length != 6) {
			return "";
		}
		String s = Util.sGetAttr_b(bytes);
		s = "<" + scReaderFWVersion + ">" + s + "</" + scReaderFWVersion + ">";
		return s;
	}
	
	/* b 48, Term Host Parameters, M/C/  */
	public static final String scTermHostParameters = "T6002";
	public class TermHostParameters {
		// b 2, �魭�B�X��, 1 bytes unpack to 2bytes
		OneDayQuotaFlagForMicroPayment OneDayQuotaFlag;
		// b 4, �魭�B�� (default = 3000), 2 bytes unpack to 4 bytes
		byte[] OneDayQuota;			
		// b 2,	�����B�X��, 1 bytes unpack to 2bytes, 00: ������, 01: ����
		boolean OnceQuotaFlag;		
		// b 4, �����B�� (default = 1000), 2 bytes unpack to 4 bytes
		byte[] OnceQuota;			
		// b 2, �ˬd�l�B�X��, 1 bytes unpack to 2bytes, 00: �ˬd�l�B (default), 01: ���ˬd�l�B
		boolean CheckEVFlag;			
		// b 2, �[���B�ױ��޺X��, 1 bytes unpack to 2bytes, 00: �������[���B��, 01: ����[���B��
		boolean AddQuotaFlag;		
		// b 6, �[���B��(default = 100,000), 3bytes unpack to 6bytes
		byte[] AddQuota;			
		// b 2, ���ȥ���X�k���ҺX��, 1 bytes unpack to 2bytes, 00: ������, 01: ����
		boolean CheckDeductFlag;
		// b 4, ���ȥ���X�k���Ҫ��B (default = 500), 2 bytes unpack to 4 bytes
		byte[] CheckDeductValue;	
		// b 2, ???, 1bytes unpack to 2bytes
		boolean DeductLimitFlag;
		// b 8, ???, 4bytes unpack to 8bytes
		byte[] APIVersion;			
		// b 10, 5bytes unpack to 10bytes
		byte[] RFU;					
	};
	
	public class TermHostParametersResp {
		// �魭�B�X��
		OneDayQuotaFlagForMicroPayment OneDayQuotaFlag;
		// �魭�B�� (default = 3000)
		int OneDayQuota;			
		// �����B�X��, 00: ������, 01: ����
		boolean OnceQuotaFlag;		
		// �����B�� (default = 1000)
		int OnceQuota;			
		// �ˬd�l�B�X��, 00: �ˬd�l�B (default), 01: ���ˬd�l�B
		boolean CheckEVFlag;			
		// �[���B�ױ��޺X��, 00: �������[���B��, 01: ����[���B��
		boolean AddQuotaFlag;		
		// �[���B��(default = 100,000)
		int AddQuota;			
		// ���ȥ���X�k���ҺX��, 00: ������, 01: ����
		boolean CheckDeductFlag;
		// ���ȥ���X�k���Ҫ��B (default = 500)
		int CheckDeductValue;	
		// ���ȥ���X�k���ҺX��
		boolean DeductLimitFlag;
		// 
		String APIVersion;			
		// �O�d
		byte[] RFU;					
	};
	
	public String sGetTermHostParameters(TermHostParameters params) {
		String s = "";
		if (params == null || params.OneDayQuota == null || params.OneDayQuota.length != 2 ||
				params.OnceQuota == null || params.OnceQuota.length != 2 || 
				params.AddQuota == null || params.AddQuota.length != 3 ||
				params.CheckDeductValue == null || params.CheckDeductValue.length != 2 || 
				params.APIVersion == null || params.APIVersion.length != 4) {
			return s;
		}
		switch (params.OneDayQuotaFlag) {
		case NCNA: // ���ˬd, ���֭p�魭�B
			s += "00";
			break;
		case NCYA: // ���ˬd, �֭p�魭�B
			s += "01";
			break;
		case YCNA: // �ˬd, ���֭p�魭�B
			s += "10";
			break;
		case YCYA: // �ˬd, �֭p�魭�B
			s += "11";
			break;
		}
		s += Util.sGetAttr_b(params.OneDayQuota);
		if (params.OnceQuotaFlag) {
			s += "01";
		} else {
			s += "00";
		}
		s += Util.sGetAttr_b(params.OnceQuota);			
		if (params.CheckEVFlag) {
			s += "00";
		} else {
			s += "01";
		}
		if (params.AddQuotaFlag) {
			s += "01";
		} else {
			s += "00";
		}
		s += Util.sGetAttr_b(params.AddQuota);			
		if (params.CheckDeductFlag) {
			s += "01";
		} else {
			s += "00";
		}
		s += Util.sGetAttr_b(params.CheckDeductValue);	
		if (params.DeductLimitFlag) {
			s += "01";
		} else {
			s += "00";
		}
		s += Util.sGetAttr_b(params.APIVersion);			
		s += "0000000000";
		s = "<" + scTermHostParameters + ">" + s + "</" + scTermHostParameters + ">";
		return s;
	}
	
	/* b 64, Term Reader Parameters, M/ /  */
	public static final String scTermReaderParameters = "T6003";
	public class TermReaderParameters {
		byte[] RemainderOfAddQuota;			// b 6
		byte[] deMACParameters;				// b 16
		byte[] CancelCreditQuota;			// b 6
		byte[] RFU;							// b 36
	};
	public String sGetTermReaderParameters(TermReaderParameters params) {
		String s = "";
		if (params == null || 
				params.RemainderOfAddQuota == null || params.RemainderOfAddQuota.length != 3 ||
				params.deMACParameters == null || params.deMACParameters.length != 8 || 
				params.CancelCreditQuota == null || params.CancelCreditQuota.length != 3) {
			return s;
		}
		s += Util.sGetAttr_b(params.RemainderOfAddQuota);
		s += Util.sGetAttr_b(params.deMACParameters);	
		s += Util.sGetAttr_b(params.CancelCreditQuota);
		s += "000000000000000000000000000000000000";
		s = "<" + scTermReaderParameters + ">" + s + "</" + scTermReaderParameters + ">";
		return s;
	}
	
	/* n 5, Blacklist version, M/ /  */
	public static final String scBlacklistVersion = "T6004";
	public String sGetBlacklistVersion(byte[] bytes) {
		if (bytes == null || bytes.length != 5) {
			return "";
		}
		String s = Util.sGetAttr_n(bytes);
		s = "<" + scBlacklistVersion + ">" + s + "</" + scBlacklistVersion + ">";
		return s;
	}
	
	/* b 16, S-TAC, Request�y�C�dADATA�ýX��S-TAC, M/ /  */
	public static final String scS_TAC = "T6400";
	public String sGetS_TAC(byte[] bytes) {
		if (bytes == null || bytes.length != 8) {
			return "";
		}
		String s = Util.sGetAttr_b(bytes);
		s = "<" + scS_TAC + ">" + s + "</" + scS_TAC + ">";
		return s;
	}
	
	/* b 32, SAToken, M/ /  */
	public static final String scSAToken = "T6408";
	public String sGetSAToken(byte[] bytes) {
		if (bytes == null || bytes.length != 16) {
			return "";
		}
		String s = Util.sGetAttr_b(bytes);
		s = "<" + scSAToken + ">" + s + "</" + scSAToken + ">";
		return s;
	}
	
	/* b 32, HAToken, M/ /  */
	public static final String scHAToken = "T6409";
}
