package CMAS;

public class ProcessingCode {
	public enum code {
		ParamsDownload, 				/* �ѼƤU��, TMS system�ѼƤU�� */ 
		SignOnTMSSystem, 				/* ���ݳ]�ƹ�TMS system�i��sign on */ 
		StatusReport,					/* ���ݳ]�ƹ�TMS system�^���ݥ��]�ƪ��A */
		Settlement,						/* ���b */
		InquiryCardTradeDetail6, 		/* �y�C�d�d��������Ӭd��, 6�� */
		InquiryDongleTradeDetail1K,		/* �y�C�dDongle������Ӭd��, 1000�� */
		InquiryCardData,				/* �y�C�d�d����Ƭd�� */
		InquiryBalance,					/* �l�B�d�� */
		ExhibitCardLock,				/* �y�C�d�i����d */
		CardLock,						/* �y�C�d��d */
		EDCSignOn,						/* EDC Sign On */
		CashAddCredit,					/* �{���[�� */
		SocialCareCashAddCredit,		/* ���֥d�{���[�� */
		AutoAddCredit,					/* �۰ʥ[�� */
		SocialCareAutoAddCredit,		/* ���֥d�۰ʥ[�� */
		ATMAddCredit,					/* ���ĥd�[�� */
		SocialCareATMAddCredit,			/* ���֥d���ĥd�[�� */
		XformRefundAddCredit, 			/* �l�B��m�[�� */
		CancelAddCredit,				/* �����[�� */
		Merchandise, 					/* �ʳf */
		CancelMerchandise,				/* �����ʳf */
		SellCard,						/* ��d */
		CancelSellCard,					/* ������d */
		Exhibit,						/* �i�� */
		TXNAuth,						/* ����X�k���� */
		ReturnMerchandise,				/* �h�f */
		ReturnATMCard,					/* �p�W�d�h�d */
		XformRefundReturnCard,			/* �l�B��m�h�d */
		CoBranderCardAutoCredit,		/* �p�W�d�۰ʥ[�ȥ\��}�� */
		CPUCashAddCredit,				/* CPU�{���[�� */
		CPUSocialCareCashAddCredit,		/* CPU���֥d�{���[�� */
		CPUAutoAddCredit,				/* CPU�۰ʥ[�� */
		CPUSocialCareAutoAddCredit,		/* CPU���֥d�۰ʥ[�� */
		CPUATMAddCredit,				/* CPU���ĥd�[�� */
		CPUSocialCareATMAddCredit,		/* CPU���֥d���ĥd�[�� */
		CPUXformRefundAddCredit,		/* CPU�l�B��m�[�� */
		CPUCancelAddCredit,				/* CPU�����[�� */
		CPUMerchandise,					/* CPU�ʳf */
		CPUCancelMerchandise,			/* CPU�����ʳf */
		CPUXformRefundMerchandise,		/* CPU�l�B��m�ʳf */
		CPUSellCard,					/* CPU��d */
		CPUCancelSellCard,				/* CPU������d */
		CPUExhibit,						/* CPU�i�� */
		CPUTXNAuth,						/* CPU����X�k���� */
		CPUReturnMerchandise,			/* CPU�h�f */
		CPUReturnCard,					/* CPU�h�d */
		CPUXformRefundReturnCard,		/* CPU�l�B��m�h�d */
		CPUCoBranderCardAutoCredit		/* CPU�p�W�d�۰ʥ[�ȥ\��}�� */ 	
	};
	
	/* �ѼƤU��, TMS system�ѼƤU�� */
	public static final String scParamsDownload = "920700";
	
	/* ���ݳ]�ƹ�TMS system�i��sign on */
	public static final String scSignOnTMSSystem = "940007";
	
	/* ���ݳ]�ƹ�TMS system�^���ݥ��]�ƪ��A */ 
	public static final String scStatusReport = "950007";
	
	/* ���b */
	public static final String scSettlement = "900000";
	
	/* �y�C�d�d��������Ӭd��, 6�� */
	public static final String scInquiryCardTradeDetail6 = "216000";
	
	/* �y�C�dDongle������Ӭd��, 1000�� */
	public static final String scInquiryDongleTradeDetail1K = "214100";
	
	/* �y�C�d�d����Ƭd�� */
	public static final String scInquiryCardData = "296000";
	
	/* �l�B�d�� */
	public static final String scInquiryBalance = "200000";
	
	/* �y�C�d�i����d */
	public static final String scExhibitCardLock = "596000";
	
	/* �y�C�d��d */
	public static final String scCardLock = "596100";
	
	/* EDC Sign On */
	public static final String scEDCSignOn = "881999";
	
	/* �{���[�� */
	public static final String scCashAddCredit = "810799";
	
	/* ���֥d�{���[�� */
	public static final String scSocialCareCashAddCredit = "840799";
	
	/* �۰ʥ[�� */
	public static final String scAutoAddCredit = "824799";
	
	/* ���֥d�۰ʥ[�� */
	public static final String scSocialCareAutoAddCredit = "844799";
	
	/* ���ĥd�[�� */
	public static final String scATMAddCredit = "836799";
	
	/* ���֥d���ĥd�[�� */
	public static final String scSocialCareATMAddCredit = "846799";
	
	/* �l�B��m�[�� */
	public static final String scXformRefundAddCredit = "838799";
	
	/* �����[�� */
	public static final String scCancelAddCredit = "810899";
	
	/* �ʳf */
	public static final String scMerchandise = "810599";
	
	/* �����ʳf */
	public static final String scCancelMerchandise = "822899";
	
	/* ��d */
	public static final String scSellCard = "850799";
	
	/* ������d */
	public static final String scCancelSellCard = "850899";
	
	/* �i�� */
	public static final String scExhibit = "810399";
	
	/* ����X�k���� */
	public static final String scTXNAuth = "815399";
	
	/* �h�f */
	public static final String scReturnMerchandise = "850999";
	
	/* �p�W�d�h�d */
	public static final String scReturnATMCard = "860799";
	
	/* �l�B��m�h�d */
	public static final String scXformRefundReturnCard = "862799";
	
	/* �p�W�d�۰ʥ[�ȥ\��}�� */
	public static final String scCoBranderCardAutoCredit = "813799";
	
	/* CPU�{���[�� */
	public static final String scCPUCashAddCredit = "811799";
	
	/* CPU���֥d�{���[�� */
	public static final String scCPUSocialCareCashAddCredit = "841799";
	
	/* CPU�۰ʥ[�� */
	public static final String scCPUAutoAddCredit = "825799";
	
	/* CPU���֥d�۰ʥ[�� */
	public static final String scCPUSocialCareAutoAddCredit = "845799";
	
	/* CPU���ĥd�[�� */
	public static final String scCPUATMAddCredit = "837799";
	
	/* CPU���֥d���ĥd�[�� */
	public static final String scCPUSocialCareATMAddCredit = "847799";
	
	/* CPU�l�B��m�[�� */
	public static final String scCPUXformRefundAddCredit = "839799";
	
	/* CPU�����[�� */
	public static final String scCPUCancelAddCredit = "811899";
	
	/* CPU�ʳf */
	public static final String scCPUMerchandise = "811599";
	
	/* CPU�����ʳf */
	public static final String scCPUCancelMerchandise = "823899";
	
	/* CPU�l�B��m�ʳf */
	public static final String scCPUXformRefundMerchandise = "813599";
	
	/* CPU��d */
	public static final String scCPUSellCard = "851799";
	
	/* CPU������d */
	public static final String scCPUCancelSellCard = "851899";
	
	/* CPU�i�� */
	public static final String scCPUExhibit = "811399";
	
	/* CPU����X�k���� */
	public static final String scCPUTXNAuth = "816399";

	/* CPU�h�f */
	public static final String scCPUReturnMerchandise = "851999";
	
	/* CPU�h�d */
	public static final String scCPUReturnCard = "864799";
	
	/* CPU�l�B��m�h�d */
	public static final String scCPUXformRefundReturnCard = "863799";
	
	/* CPU�p�W�d�۰ʥ[�ȥ\��}�� */
	public static final String scCPUCoBranderCardAutoCredit = "814799";
}
