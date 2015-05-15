package CMAS;


import java.util.ArrayList;

import java.util.Properties;


import org.apache.log4j.Logger;

import CMAS.CmasDataSpec.Issuer;
import CMAS.CmasDataSpec.SubTag5588;
import CMAS.CmasDataSpec.SubTag5596;
import CMAS.CmasDataSpec.SubTag6002;
import CMAS.ConfigManager;
import Reader.PPR_Reset;
import Reader.PPR_SignOn;
import Utilities.DataFormat;
import Utilities.Util;

public class CmasKernel {

	static Logger logger = Logger.getLogger(CmasKernel.class);

	public CmasKernel(){
		logger.info("Start");
		
		logger.info("End");
	}
	
	//public CmasKernel(CmasDataSpec spec, PPR_Reset reset, Properties pApi) {
	public void readerField2CmasSpec(PPR_Reset pprReset, CmasDataSpec spec, ArrayList<Properties> cfgList, SubTag5596 _t5596) {
		// TODO Auto-generated constructor stub
		logger.info("Start");
		Properties pApi = cfgList.get(ConfigManager.ConfigOrder.EASYCARD_API.ordinal());
		
		
		spec.setT0100("0800");
		spec.setT0300("881999");
		spec.setT1100(new String(pprReset.GetReq_TMSerialNumber()));
		spec.setT1101(new String(pprReset.GetReq_TMSerialNumber()));
		spec.setT1200(pprReset.GetReq_TMTXNTime());
		spec.setT1201(pprReset.GetReq_TMTXNTime());
		
		spec.setT1300(pprReset.GetReq_TMTXNDate());
		spec.setT1301(pprReset.GetReq_TMTXNDate());
		
		spec.setT3700(pprReset.GetReq_TMTXNDate()
				+new String(pprReset.GetReq_TMSerialNumber()));
		spec.setT4100(Util.bcd2Ascii(pprReset.GetResp_NewDeviceID()));
		spec.setT4101(Util.bcd2Ascii(pprReset.GetResp_DeviceID()));
		spec.setT4102(Util.sGetLocalIpAddress());
		spec.setT4103("KOBE-PC-24");
		spec.setT4104(Util.bcd2Ascii(pprReset.GetResp_ReaderID()));
		spec.setT4200(pprReset.GetReq_NewServiceProviderIDByNewDeviceID());
		
		spec.setT4210(String.format("%05d", DataFormat.byteArrayToInt(pprReset.GetReq_NewLocationID()))); //New location ID
		spec.setT4802(String.format("%02d", Issuer.easycard.ordinal()));//Issuer Code
		spec.setT4820(String.format("%02X", pprReset.GetResp_SpecVersionNumber()));
		
		spec.setT4823(String.format("%2s", Integer.toBinaryString(pprReset.GetResp_OneDayQuotaWriteForMicroPayment())).replace(' ', '0'));
		spec.setT4824(String.format("%2s", Integer.toBinaryString(pprReset.GetResp_CPDReadFlag())).replace(' ', '0'));
		spec.setT5301(String.format("%02X", pprReset.GetResp_SAMKeyVersion()));		
		spec.setT5307(Util.bcd2Ascii(pprReset.GetResp_RSAM()));
		spec.setT5308(Util.bcd2Ascii(pprReset.GetResp_RHOST()));
		spec.setT5361(Util.bcd2Ascii(pprReset.GetResp_SAMID()));
		spec.setT5362(Util.bcd2Ascii(pprReset.GetResp_SAMSN()));
		spec.setT5363(Util.bcd2Ascii(pprReset.GetResp_SAMCRN()));
		spec.setT5364(String.format("%02X", pprReset.GetResp_SAMVersionNumber())+Util.bcd2Ascii(pprReset.GetResp_SAMUsageControl())+String.format("%02X%02X", pprReset.GetResp_SAMAdminKVN(),pprReset.GetResp_SAMIssuerKVN())+Util.bcd2Ascii(pprReset.GetResp_TagListTable())+Util.bcd2Ascii(pprReset.GetResp_SAMIssuerSpecificData()));
		
		spec.setT5365(Util.bcd2Ascii(pprReset.GetResp_ACL())+Util.bcd2Ascii(pprReset.GetResp_ACB())+Util.bcd2Ascii(pprReset.GetResp_ACC())+Util.bcd2Ascii(pprReset.GetResp_ACCC()));
		spec.setT5366(Util.bcd2Ascii(pprReset.GetResp_SingleCreditTXNAMTLimit()));
		spec.setT5368(Util.bcd2Ascii(pprReset.GetResp_STC()));
		
		spec.setT5369(String.format("%2s", Integer.toBinaryString(pprReset.GetResp_SAMSignOnControlFlag())).replace(' ', '0'));
		spec.setT5370(Util.bcd2Ascii(pprReset.GetResp_PreviousNewDeviceID())+Util.bcd2Ascii(pprReset.GetResp_PreviousSTC())+Util.bcd2Ascii(pprReset.GetResp_PreviousTXNDateTime())+String.format("%02X",(pprReset.GetResp_PreviousCreditBalanceChangeFlag())?1:0)+Util.bcd2Ascii(pprReset.GetResp_PreviousConfirmCode())+Util.bcd2Ascii(pprReset.GetResp_PreviousCACrypto()));
		spec.setT5371(Util.bcd2Ascii(pprReset.GetResp_SAMIDNew()));
		//spec.setT5501();//批次號碼
		
		byte[] d = spec.getT1300().getBytes();
		byte[] s = spec.getT1100().getBytes();
		spec.setT5501(String.format("%c%c%c%c%c%c%c%c"
				,d[2],d[3],d[4],d[5],d[6],d[7]
				,s[4],s[5]
		));
		spec.setT5503(pprReset.GetReq_TMLocationID());
		spec.setT5504(pprReset.GetReq_TMID());
		spec.setT5510(pprReset.GetReq_TMAgentNumber());
		
		
		
		
		// test ArrayList
		SubTag5588 tag = spec.new SubTag5588(); 
		tag.setT558801("01");
		tag.setT558803("5566");
		spec.setT5588s(tag);
		
		tag = spec.new SubTag5588(); 
		tag.setT558801("02");
		tag.setT558803(pApi.getProperty("BlackListVer"));
		spec.setT5588s(tag);
		
		tag = spec.new SubTag5588(); 		
		tag.setT558801("03");
		tag.setT558802(pApi.getProperty("ApiName"));
		tag.setT558803(pApi.getProperty("ApiVer"));
		spec.setT5588s(tag);
		
		
		SubTag5596 t5596 = spec.getT5596();
		t5596.setT559601(_t5596.getT559601());
		t5596.setT559602(_t5596.getT559602());
		t5596.setT559603(_t5596.getT559603());
		t5596.setT559604(_t5596.getT559604());
		//spec.setT5596(t5596);
		
		
		spec.setT6000(Util.bcd2Ascii(pprReset.GetResp_ReaderFWVersion()));
		
		
		
		SubTag6002 t6002 = spec.getT6002();
		t6002.setOneDayQuotaFlag(String.format("%2s", Integer.toBinaryString(pprReset.GetResp_OneDayQuotaFlagForMicroPayment()).replace(' ', '0')));
		t6002.setOneDayQuota(Util.bcd2Ascii(pprReset.GetResp_OneDayQuotaForMicroPayment()));
		t6002.setOnceQuotaFlag(String.format("%02d", pprReset.GetResp_OnceQuotaFlagForMicroPayment()));
		t6002.setOnceQuota(Util.bcd2Ascii(pprReset.GetResp_OnceQuotaForMicroPayment()));
		t6002.setCheckEVFlag(String.format("%02d", pprReset.GetResp_CheckEVFlagForMifareOnly()));
		t6002.setAddQuotaFlag(String.format("%02d", pprReset.GetResp_AddQuotaFlag()));
		t6002.setAddQuota(Util.bcd2Ascii(pprReset.GetResp_AddQuota()));
		t6002.setCheckDeductFlag(String.format("%02d", pprReset.GetResp_CheckDebitFlag()));
		t6002.setCheckDeductValue(Util.bcd2Ascii(pprReset.GetResp_CheckDebitValue()));
		t6002.setDeductLimitFlag(String.format("%02d", pprReset.GetResp_MerchantLimitUseForMicroPayment()));
		t6002.setApiVersion(String.format("%8s", pApi.getProperty("ApiVer")));
		t6002.setRFU("0000000000");
		
		
		spec.setT6003(Util.bcd2Ascii(pprReset.GetResp_RemainderofAddQuota())
				+Util.bcd2Ascii(pprReset.GetResp_deMACParameter())
				+Util.bcd2Ascii(pprReset.GetResp_CancelCreditQuota())
				+"000000000000000000000000000000000000"
		);
		
		spec.setT6004(pApi.getProperty("BlackListVer"));
		//spec.setT6004("12345");
		
		spec.setT6400(Util.bcd2Ascii(pprReset.GetResp_S_TAC()));
		spec.setT6408(Util.bcd2Ascii(pprReset.GetResp_SATOKEN()));
		
		logger.info("End");
	}
	
	public void readerField2CmasSpec(PPR_SignOn pprSignOn, CmasDataSpec specAdv, CmasDataSpec specResetResp, ArrayList<Properties> cfgList)
	{
		//signon advice
		Properties txnInfo = cfgList.get(ConfigManager.ConfigOrder.TXN_INFO.ordinal());
		
		specAdv.setT0100("0820");
		specAdv.setT0300("881999");
		
		logger.debug("getTM_Serial_Number:"+txnInfo.getProperty("TM_Serial_Number"));
		specAdv.setT1100(txnInfo.getProperty("TM_Serial_Number"));
		specAdv.setT1101(txnInfo.getProperty("TM_Serial_Number"));
		
		
		int unixTimeStamp = (int) (System.currentTimeMillis() / 1000L);
		specAdv.setT1200(Util.sGetTime(unixTimeStamp));
		specAdv.setT1300(Util.sGetDate(unixTimeStamp));
		
		specAdv.setT3700(specAdv.getT1300() + specAdv.getT1100());
		specAdv.setT4100(specResetResp.getT4100());
		specAdv.setT4200(specResetResp.getT4200());
		specAdv.setT4210(specResetResp.getT4210());
		specAdv.setT4825(String.format("%02X", pprSignOn.GetResp_CreditBalanceChangeFlag()));
		
		byte[] d = specAdv.getT1300().getBytes();
		byte[] s = specAdv.getT1100().getBytes();
		specAdv.setT5501(String.format("%c%c%c%c%c%c%c%c"
				,d[2],d[3],d[4],d[5],d[6],d[7]
				,s[4],s[5]
		));
		
		specAdv.setT5503(specResetResp.getT5503());
		specAdv.setT5504(specResetResp.getT5504());
		specAdv.setT5510(specResetResp.getT5510());
		
		if(specAdv.getT4825().equalsIgnoreCase("01"))
		{
			specAdv.setT6406(Util.bcd2Ascii(pprSignOn.GetResp_CACrypto()));
		}
		
	}

	
	
	public int cmasSpec2ReaderField(CmasDataSpec spec, PPR_SignOn pprSignon, ArrayList<Properties> cfgList)
	{
		int result = 0;
		
		pprSignon.SetReq_H_TAC(spec.getT6401());
		pprSignon.SetReq_HAToken(spec.getT6409());
		
		String t5367_1=null;
		String t5367_2=null;
		String t5367_3=null;
		if(spec.getT5367() == "")
		{
			t5367_1 = "00";
			t5367_2 = "00000000000000000000000000000000000000000000000000000000000000000000000000000000";
			t5367_3 = "00000000000000000000000000000000";
		}
		else
		{
			t5367_1 = spec.getT5367().substring(0, 1);
			t5367_1 = spec.getT5367().substring(2, 81);
			t5367_1 = spec.getT5367().substring(82, 113);
		}
		pprSignon.SetReq_SAMUpdateOption(t5367_1);
		pprSignon.SetReq_NewSAMValue(t5367_2);
		pprSignon.SetReq_UpdateSAMValueMAC(t5367_3);
		
		pprSignon.SetReq_CPDReadFlag(spec.getT4824());
		pprSignon.SetReq_OneDayQuotaWriteForMicroPayment(spec.getT4823());
		pprSignon.SetReq_SAMSignOnControlFlag(spec.getT5369());
		pprSignon.SetReq_CheckEVFlagForMifareOnly(spec.getT6002().getCheckEVFlag());
		pprSignon.SetReq_MerchantLimitUseForMicroPayment(spec.getT6002().getDeductLimitFlag());
		
		pprSignon.SetReq_OneDayQuotaFlagForMicroPayment(spec.getT6002().getOneDayQuotaFlag());
		pprSignon.SetReq_OnceQuotaFlagForMicroPayment(spec.getT6002().getOnceQuotaFlag());
		pprSignon.SetReq_CheckDebitFlag(spec.getT6002().getCheckDeductFlag());
		
		pprSignon.SetReq_OneDayQuotaForMicroPayment(spec.getT6002().getOneDayQuota());
		pprSignon.SetReq_OnceQuotaForMicroPayment(spec.getT6002().getOnceQuota());
		pprSignon.SetReq_CheckDebitValue(spec.getT6002().getCheckDeductValue());
		pprSignon.SetReq_AddQuotaFlag(spec.getT6002().getAddQuotaFlag());
		pprSignon.SetReq_AddQuota(spec.getT6002().getAddQuota());
		
		pprSignon.SetReq_EDC(spec.getT5303()+spec.getT5306());
		
		
		
		return result;
		
	}
	
	public String packRequeset(int[] field, CmasDataSpec spec)
	{
		String result="<TransXML><Trans>";
		
		for(int tag:field)
		{
			result += tagGenerater(tag, spec);
		}
		result += "</Trans></TransXML>";
		logger.info("pack CMAS data:"+result);
		
		
		return result;
	}
	
	private String tagGenerater(int tag, CmasDataSpec spec){
		String result = "";
		String start = "<T"+String.format("%04d", tag)+">";
		String end = "</T"+String.format("%04d", tag)+">";
		switch(tag)
		{
			case 100:
				result = start + spec.getT0100() + end ;
				break;
			
			case 200:
				result = start + spec.getT0200() + end ;
				break;
				
			case 300:
				result = start + spec.getT0300() + end ;
				break;
			case 1100:
				result = start + spec.getT1100() + end ;
				break;
			case 1101:
				result = start + spec.getT1101() + end ;
				break;
			case 1200:
				result = start + spec.getT1200() + end ;
				break;
			case 1201:
				result = start + spec.getT1201() + end ;
				break;	
				
			case 1300:
				result = start + spec.getT1300() + end ;
				break;
			case 1301:
				result = start + spec.getT1301() + end ;
				break;
			case 3700:
				result = start + spec.getT3700() + end ;
				break;	
			case 4100:
				result = start + spec.getT4100() + end ;
				break;
			case 4101:
				result = start + spec.getT4101() + end ;
				break;
			case 4102:
				result = start + spec.getT4102() + end ;
				break;
			case 4103:
				result = start + spec.getT4103() + end ;
				break;
			case 4104:
				result = start + spec.getT4104() + end ;
				break;
			case 4200:
				result = start + spec.getT4200() + end ;
				break;
			case 4201:
				result = start + spec.getT4201() + end ;
				break;
			case 4210:
				result = start + spec.getT4210() + end ;
				break;
			
			case 4802:
				result = start + spec.getT4802() + end ;
				break;
			case 4820:
				result = start + spec.getT4820() + end ;
				break;
			case 4823:
				result = start + spec.getT4823() + end ;
				break;	
			case 4824:
				result = start + spec.getT4824() + end ;
				break;
			case 4825:
				result = start + spec.getT4825() + end ;
				break;
			
			case 5301:
				result = start + spec.getT5301() + end ;
				break;
			case 5307:
				result = start + spec.getT5307() + end ;
				break;
			case 5308:
				result = start + spec.getT5308() + end ;
				break;
			case 5361:
				result = start + spec.getT5361() + end ;
				break;
			case 5362:
				result = start + spec.getT5362() + end ;
				break;
			case 5363:
				result = start + spec.getT5363() + end ;
				break;
			case 5364:
				result = start + spec.getT5364() + end ;
				break;
			case 5365:
				result = start + spec.getT5365() + end ;
				break;
			case 5366:
				result = start + spec.getT5366() + end ;
				break;
			case 5368:
				result = start + spec.getT5368() + end ;
				break;
			case 5369:
				result = start + spec.getT5369() + end ;
				break;
			case 5370:
				result = start + spec.getT5370() + end ;
				break;
			case 5371:
				result = start + spec.getT5371() + end ;
				break;	
			case 5501:
				result = start + spec.getT5501() + end ;
				break;	
			case 5503:
				result = start + spec.getT5503() + end;
				break;	
			case 5504:
				result = start + spec.getT5504() + end;
				break;	
			case 5510:
				result = start + spec.getT5510() + end;
				break;	
			case 5588:
				
				ArrayList<SubTag5588> t5588s = spec.getT5588s();
				for(SubTag5588 t5588:t5588s)
				{
					result+=start;
					if(t5588.getT558801()!="") result+="<T558801>"+t5588.getT558801()+"</T558801>";
					if(t5588.getT558802()!="") result+="<T558802>"+t5588.getT558802()+"</T558802>";
					if(t5588.getT558803()!="") result+="<T558803>"+t5588.getT558803()+"</T558803>";					
					result+=end;
				}
				
				/*
				SubTag5588 []t5588s = spec.getT5588(); 

				for(SubTag5588 t5588:t5588s)
				{
					result+=start;
					if(t5588.getT558801()!="") result+="<T558801>"+t5588.getT558801()+"</T558801>";
					if(t5588.getT558802()!="") result+="<T558802>"+t5588.getT558802()+"</T558802>";
					if(t5588.getT558803()!="") result+="<T558803>"+t5588.getT558803()+"</T558803>";					
					result+=end;
				}
					*/			
				break;	
			
			case 5596:
				SubTag5596 t5596 = spec.getT5596();
				result  = start + "<T559601>"+t5596.getT559601()+"</T559601>";
				result += "<T559602>"+t5596.getT559601()+"</T559602>";
				result += "<T559603>"+t5596.getT559601()+"</T559603>";
				result += "<T559604>"+t5596.getT559601()+"</T559604>" + end;
				
				break;
				
			case 6000:
				result = start + spec.getT6000() + end;
				break;	
			
			case 6002:
				SubTag6002 t6002 = spec.getT6002();
				result = start 
						+ t6002.getOneDayQuotaFlag()
						+ t6002.getOneDayQuota()
						+ t6002.getOnceQuotaFlag()
						+ t6002.getOnceQuota()
						+ t6002.getCheckEVFlag()
						+ t6002.getAddQuotaFlag()
						+ t6002.getAddQuota() 
						+ t6002.getCheckDeductFlag()
						+ t6002.getCheckDeductValue()
						+ t6002.getDeductLimitFlag()
						+ t6002.getApiVersion()
						+ t6002.getRFU()
						+ end;
				
				//result = start + spec.getT6002() + end;
				break;	
				
			case 6003:
				result = start + spec.getT6003() + end;
				break;	
				
			case 6004:
				result = start + spec.getT6004() + end;
				break;	
				
			case 6400:
				result = start + spec.getT6400() + end;
				break;	
				
			case 6406:
				if(spec.getT4825().equalsIgnoreCase("01"))
					result = start + spec.getT6406() + end;
				else
					result = "";
				break;	
				
				
			case 6408:
				result = start + spec.getT6408() + end;
				break;
				
			default:
				logger.error("oh!oh!, unKnowen tag to generate:"+tag);	
				break;
		}
		
		return result;
	}
	
	
	
}
