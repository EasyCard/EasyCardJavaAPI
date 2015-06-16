package com.easycard.pc.CMAS;




import java.util.ArrayList;
import java.util.Arrays;




import org.apache.log4j.Logger;

import com.easycard.pc.CMAS.CmasDataSpec.Issuer;
import com.easycard.pc.CMAS.CmasTag.SubTag5588;
import com.easycard.pc.CMAS.CmasTag.SubTag5596;
import com.easycard.pc.CMAS.CmasTag.SubTag6002;
import com.easycard.pc.CMAS.IConfigManager;
import com.easycard.errormessage.IRespCode;
import com.easycard.reader.ErrorResponse;
import com.easycard.reader.PPR_AuthTxnOffline;
import com.easycard.reader.PPR_AuthTxnOnline;
import com.easycard.reader.PPR_Reset;
import com.easycard.reader.PPR_SignOn;
import com.easycard.reader.PPR_TxnReqOffline;
import com.easycard.reader.PPR_TxnReqOnline;
import com.easycard.reader.ReaderRespCode;
import com.easycard.utilities.Util;

public class CmasKernel {

	static Logger logger = Logger.getLogger(CmasKernel.class);
	private final int CMAS_4801_SIZE = 90;
	private final int CMAS_4828_SIZE = 20;
	private final int CMAS_4829_SIZE = 32;
	private final int CMAS_6406_SIZE = 32;
	public final static int[] CMAS_LOCKCARD_ADVICE_FLD = {100, 200, 211, 213, 214, 300, 410, 1100, 1200, 1201, 1300, 1301, 3700, 4100, 4101, 4200, 4210, 4800, 4801, 4802, 4803, 4804, 4805, 4806, 4812, 4814, 4818, 4826, 4828, 4829, 5501, 5503, 5504, 5510};
	
	
	public CmasKernel(){
		logger.info("Start");
		
		logger.info("End");
	}
	
	//SignOn 0800
	public void readerField2CmasSpec(PPR_Reset pprReset, CmasDataSpec spec, IConfigManager configManager, SubTag5596 _t5596) {
		// TODO Auto-generated constructor stub
		logger.info("Start");
		//Properties pApi = cfgList.get(ConfigManager.ConfigOrder.EASYCARD_API.ordinal());
		//Properties pTxnInfo = cfgList.get(ConfigManager.ConfigOrder.TXN_INFO.ordinal());
		
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
		
		spec.setT4210(String.format("%05d", Util.bytes2Long(pprReset.GetReq_NewLocationID(), 0, pprReset.GetReq_NewLocationID().length, true))); //New location ID
		//spec.setT4210(String.format("%05d", DataFormat.byteArrayToInt(pprReset.GetReq_NewLocationID()))); //New location ID
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
		
		
		spec.setT5501(configManager.getBatchNo());
		
		
		spec.setT5503(pprReset.GetReq_TMLocationID());
		spec.setT5504(pprReset.GetReq_TMID());
		spec.setT5510(pprReset.GetReq_TMAgentNumber());
		
		
	
		 
		// test ArrayList
		//SubTag5588 tag = spec.getCmasTag().new SubTag5588();
		SubTag5588 tag = spec.getSubTag5588Instance();
		tag.setT558801("01");
		tag.setT558803("5566");
		spec.setT5588s(tag);
		
		tag = spec.getSubTag5588Instance();
		tag.setT558801("02");
		tag.setT558803(configManager.getBlackListVersion());
		spec.setT5588s(tag);
		
		tag = spec.getSubTag5588Instance();	
		tag.setT558801("03");
		tag.setT558802(configManager.getApiName());
		tag.setT558803(configManager.getApiVersion());
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
		t6002.setApiVersion(String.format("%8s", configManager.getApiVersion()));
		t6002.setRFU("0000000000");
		
		
		spec.setT6003(Util.bcd2Ascii(pprReset.GetResp_RemainderofAddQuota())
				+Util.bcd2Ascii(pprReset.GetResp_deMACParameter())
				+Util.bcd2Ascii(pprReset.GetResp_CancelCreditQuota())
				+"000000000000000000000000000000000000"
		);
		
		spec.setT6004(configManager.getBlackListVersion());
		//spec.setT6004("12345");
		
		spec.setT6400(Util.bcd2Ascii(pprReset.GetResp_S_TAC()));
		spec.setT6408(Util.bcd2Ascii(pprReset.GetResp_SATOKEN()));
		
		logger.info("End");
	}
	
	//SignOn 0820
	public void readerField2CmasSpec(PPR_SignOn pprSignOn, CmasDataSpec specAdv, CmasDataSpec specResetResp, IConfigManager configManager)
	{
		//signon advice
		//Properties txnInfo = cfgList.get(ConfigManager.ConfigOrder.TXN_INFO.ordinal());
		
		specAdv.setT0100("0820");
		specAdv.setT0300("881999");
		
		logger.debug("getTM_Serial_Number:"+configManager.getTMSerialNo());
		specAdv.setT1100(configManager.getTMSerialNo());
		specAdv.setT1101(configManager.getTMSerialNo());
		
		
		int unixTimeStamp = (int) (System.currentTimeMillis() / 1000L);
		specAdv.setT1200(Util.sGetTime(unixTimeStamp));
		specAdv.setT1300(Util.sGetDate(unixTimeStamp));
		
		specAdv.setT3700(specAdv.getT1300() + specAdv.getT1100());
		specAdv.setT4100(specResetResp.getT4100());
		specAdv.setT4200(specResetResp.getT4200());
		specAdv.setT4210(specResetResp.getT4210());
		specAdv.setT4825(String.format("%02X", pprSignOn.GetResp_CreditBalanceChangeFlag()));
		
		/*
		byte[] d = specAdv.getT1300().getBytes();
		byte[] s = specAdv.getT1100().getBytes();
		specAdv.setT5501(String.format("%c%c%c%c%c%c%c%c"
				,d[2],d[3],d[4],d[5],d[6],d[7]
				,s[4],s[5]
		));*/
		specAdv.setT5501(configManager.getBatchNo());
		
		
		specAdv.setT5503(specResetResp.getT5503());
		specAdv.setT5504(specResetResp.getT5504());
		specAdv.setT5510(specResetResp.getT5510());
		
		if(specAdv.getT4825().equalsIgnoreCase("01"))
		{
			specAdv.setT6406(Util.bcd2Ascii(pprSignOn.GetResp_CACrypto()));
		}
		
	}

	//PPR_TxnReqOffline advice 0220
	public void readerField2CmasSpec(PPR_TxnReqOffline pprTxnReqOffline, PPR_AuthTxnOffline pprAuthTxnOffline, CmasDataSpec deductAdvice, IConfigManager configManager){
		// deduct advice
		byte[] b = null;
		

		deductAdvice.setT0100("0220");
		
		//t0200
		b = Arrays.copyOfRange(pprTxnReqOffline.getRespCardPhysicalID(), 0, pprTxnReqOffline.getRespCardPhysicalIDLength());
		String t0200 = Util.IntelFormat2Decimal(b, 0, pprTxnReqOffline.getRespCardPhysicalIDLength());
		deductAdvice.setT0200(t0200);
		
		//t0211
		if(pprTxnReqOffline.getRespPurseVersionNumber() != 0x00){
			
			deductAdvice.setT0211(Util.bcd2Ascii(pprTxnReqOffline.getRespPID()));
		}
		//t0213 cardType
		deductAdvice.setT0213(String.format("%02X", pprTxnReqOffline.getRespCardType()));
		
		//t0214 
		deductAdvice.setT0214(String.format("%02X", pprTxnReqOffline.getRespPersonalProfile()));
		
		//t0300
		deductAdvice.setT0300(CmasDataSpec.PCode.CPU_DEDUCT.toString());
		
		//t0400
		b = pprTxnReqOffline.getRespTxnAmt();
		String t0400 = Util.IntelFormat2Decimal(b, 0, pprTxnReqOffline.getRespTxnAmt().length);
		deductAdvice.setT0400(t0400);
				
			
		//t0408 Purse Balance
		b = pprAuthTxnOffline.getRespPurseBalance();
		String t0408 = Util.IntelFormat2Decimal(b, 0, pprAuthTxnOffline.getRespPurseBalance().length);
		deductAdvice.setT0408(t0408);
		
		//t0409
		b = pprTxnReqOffline.getRespSingleAutoLoadTxnAmt();
		String t0409 = Util.IntelFormat2Decimal(b, 0, b.length);
		deductAdvice.setT0409(t0409);
		
		//t0410 Purse Balance Before TXN
		b = pprTxnReqOffline.getRespPurseBalanceBeforeTxn();
		String t0410 = Util.IntelFormat2Decimal(b, 0, b.length);
		deductAdvice.setT0410(t0410);
		
		//t1100,t1101
		deductAdvice.setT1100(configManager.getTMSerialNo());
		deductAdvice.setT1101(configManager.getTMSerialNo());
		
		//t1200, t1201, t1300, t1301
		b = pprTxnReqOffline.getReqTMTXNDateTime().getBytes();

		String time = new String(Arrays.copyOfRange(b, 8, 14)); 
		String date = new String(Arrays.copyOfRange(b, 0, 8));
		deductAdvice.setT1200(time);
		deductAdvice.setT1201(time);
		deductAdvice.setT1300(date);
		deductAdvice.setT1301(date);
		
		//t1400		
		int unixTimeStamp = (int)Util.bytes2Long(pprTxnReqOffline.getRespPurseExpDate(), 0, pprTxnReqOffline.getRespPurseExpDate().length, true);		
		deductAdvice.setT1400(Util.getDateTime(unixTimeStamp, Util._YYMM));
		
		
		//t3700
		deductAdvice.setT3700(deductAdvice.getT1300() + deductAdvice.getT1100());
		
		//t4100
		deductAdvice.setT4100(Util.bcd2Ascii(pprTxnReqOffline.getRespNewDeviceID()));
		

		//t4101
		deductAdvice.setT4101(Util.bcd2Ascii(pprTxnReqOffline.getRespDeviceID()));
				
		//t4103
		deductAdvice.setT4103(Util.getMACAddress());
		
		//t4104
		deductAdvice.setT4104(configManager.getReaderID());
		
		
		//t4200
		deductAdvice.setT4200(String.format("%08d", Util.bytes2Long(pprTxnReqOffline.getRespNewServiceProviderID(), 0, pprTxnReqOffline.getRespNewServiceProviderID().length, true)));
		
		//t4210
		deductAdvice.setT4210(configManager.getNewLocationID());
		
		//t4800
		deductAdvice.setT4800(String.format("%02X", pprTxnReqOffline.getRespPurseVersionNumber()));
		
		//t4801
		byte[] t4801 = new byte[CMAS_4801_SIZE/2];
		System.arraycopy(pprTxnReqOffline.getRespLastCreditTxnLog(), 0, t4801, 0, pprTxnReqOffline.getRespLastCreditTxnLog().length);		
		deductAdvice.setT4801(Util.bcd2Ascii(t4801));
		
		//t4802
		deductAdvice.setT4802(String.format("%02X", pprTxnReqOffline.getRespIssuerCode()));
		
		//t4803
		deductAdvice.setT4803(String.format("%02X", pprTxnReqOffline.getRespBankCode()));
		
			
		//t4804
		deductAdvice.setT4804(String.format("%02X", pprTxnReqOffline.getRespAreaCode()));
			
		//t4805
		if(pprTxnReqOffline.getRespPurseVersionNumber() != 0x00){
			
			deductAdvice.setT4805(Util.bcd2Ascii(pprTxnReqOffline.getRespSubAreaCode()));
		}
		
		
		//t4808		
		deductAdvice.setT4808(Util.IntelFormat2Decimal(pprAuthTxnOffline.getRespTSQN(), 0, pprAuthTxnOffline.getRespTSQN().length));
		
		if(pprTxnReqOffline.getRespPurseVersionNumber() != 0x00){
			//t4809
			deductAdvice.setT4809(String.format("%02X", pprTxnReqOffline.getRespTxnMode()));
		
			//t4810
			deductAdvice.setT4810(String.format("%02X", pprTxnReqOffline.getRespTxnQualifier()));
		}
		
		
		//t4811		
		deductAdvice.setT4811(String.format("%06d", Util.bytes2Long(pprTxnReqOffline.getRespTxnSNBeforeTxn(), 0, pprTxnReqOffline.getRespTxnSNBeforeTxn().length, true)));
		
		if(pprTxnReqOffline.getRespPurseVersionNumber() != 0x00){
			//t4812
			deductAdvice.setT4812(Util.bcd2Ascii(pprTxnReqOffline.getRespCTC()));
		}					
		//t4813
		b = Arrays.copyOfRange(pprTxnReqOffline.getRespCPDSAMID(), 0, 2);		
		deductAdvice.setT4813(String.format("%06d", Util.bytes2Long(b, 0, b.length)));
		
		//t4826
		deductAdvice.setT4826(String.format("%02X", pprTxnReqOffline.getRespCardPhysicalIDLength()));
		
		//t5301
		deductAdvice.setT5301(String.format("%02X", pprTxnReqOffline.getRespCPDKVN_SAMKVN()));
		
		
		if(pprTxnReqOffline.getRespPurseVersionNumber() != 0x00){
			//t5303
			deductAdvice.setT5303(String.format("%02X", pprAuthTxnOffline.getRespMAC()[0]));
	
			//t5304
			deductAdvice.setT5304(String.format("%02X", pprAuthTxnOffline.getRespMAC()[1]));
				
			//t5305
			deductAdvice.setT5305(String.format("%02X", pprTxnReqOffline.getRespSignKeyKVN()));
		}
		
	
		if(pprTxnReqOffline.getRespPurseVersionNumber() == 0x00){
			//t5361
			b = pprTxnReqOffline.getRespCPDSAMID();
			byte[] c = Arrays.copyOfRange(b, 2, 10);
			deductAdvice.setT5361(Util.bcd2Ascii(c));
				
			//t5362
			c = Arrays.copyOfRange(b, 10, 14);
			deductAdvice.setT5362(Util.bcd2Ascii(c));
				
			//t5363
			deductAdvice.setT5363(Util.bcd2Ascii(pprTxnReqOffline.getRespCPDRAN_SAMCRN()));
		}
		
		if(pprTxnReqOffline.getRespPurseVersionNumber() != 0x00){
			//t5371
			deductAdvice.setT5371(Util.bcd2Ascii(pprTxnReqOffline.getRespSIDSTAC()));
		}
		//t5501
		deductAdvice.setT5501(configManager.getBatchNo());
			
		//t5503
		deductAdvice.setT5503(pprTxnReqOffline.getReqTMLocationID());
			
		//t5504
		deductAdvice.setT5504(pprTxnReqOffline.getReqTMID());
			
		//t5510
		deductAdvice.setT5510(pprTxnReqOffline.getReqTMAgentNumber());
			
				
		if(pprTxnReqOffline.getRespPurseVersionNumber() != 0x00){
			//t6404
			b = Arrays.copyOfRange(pprAuthTxnOffline.getRespMAC(), 2, pprAuthTxnOffline.getRespMAC().length);			
			deductAdvice.setT6404(Util.bcd2Ascii(b));
				
			//t6405
			b = pprAuthTxnOffline.getRespSIGN();
			deductAdvice.setT6405(Util.bcd2Ascii(b));
		}	
	}
	
	
	//PPR_TxnReqOnline advice 0220
	public void readerField2CmasSpec(PPR_TxnReqOnline pprTxnReqOnline, PPR_AuthTxnOnline pprAuthTxnOnline, CmasDataSpec advice, IConfigManager configManager){
			// deduct advice
			byte[] b = null;
			
			logger.info("start");
			advice.setT0100("0220");
			
			//t0200
			b = Arrays.copyOfRange(pprTxnReqOnline.getRespCardPhysicalID(), 0, pprTxnReqOnline.getRespCardPhysicalIDLength());
			String t0200 = Util.IntelFormat2Decimal(b, 0, pprTxnReqOnline.getRespCardPhysicalIDLength());
			advice.setT0200(t0200);
			
			if(pprTxnReqOnline.getRespPurseVersionNumber() != 0x00){//CPU card
				logger.debug("PurseVersionNo. != 0x00");
				//t0211
				advice.setT0211(Util.bcd2Ascii(pprTxnReqOnline.getRespPID()));
			}
			//t0213 cardType
			advice.setT0213(String.format("%02X", pprTxnReqOnline.getRespCardType()));
			
			//t0214 
			advice.setT0214(String.format("%02X", pprTxnReqOnline.getRespPersonalProfile()));
			
			//t0300
			advice.setT0300(CmasDataSpec.PCode.CPU_DEDUCT.toString());
			
			//t0400
			b = pprTxnReqOnline.getRespTxnAmt();
			String t0400 = Util.IntelFormat2Decimal(b, 0, pprTxnReqOnline.getRespTxnAmt().length);
			advice.setT0400(t0400);
					
			
			//t0408 Purse Balance
			b = pprAuthTxnOnline.getRespPurseBalance();
			String t0408 = Util.IntelFormat2Decimal(b, 0, pprAuthTxnOnline.getRespPurseBalance().length);
			advice.setT0408(t0408);
			
			//t0409
			b = pprTxnReqOnline.getRespSingleAutoLoadTxnAmt();
			String t0409 = Util.IntelFormat2Decimal(b, 0, b.length);
			advice.setT0409(t0409);
			
			//t0410 Purse Balance Before TXN
			b = pprTxnReqOnline.getRespPurseBalanceBeforeTxn();
			String t0410 = Util.IntelFormat2Decimal(b, 0, b.length);
			advice.setT0410(t0410);
			
			//t1100,t1101
			advice.setT1100(configManager.getTMSerialNo());
			advice.setT1101(configManager.getTMSerialNo());
			
			//t1200, t1201, t1300, t1301
			b = pprTxnReqOnline.getReqTMTXNDateTime().getBytes();

			String time = new String(Arrays.copyOfRange(b, 8, 14)); 
			String date = new String(Arrays.copyOfRange(b, 0, 8));
			advice.setT1200(time);
			advice.setT1201(time);
			advice.setT1300(date);
			advice.setT1301(date);
			
			//t1400		
			int unixTimeStamp = (int)Util.bytes2Long(pprTxnReqOnline.getRespPurseExpDate(), 0, pprTxnReqOnline.getRespPurseExpDate().length, true);		
			advice.setT1400(Util.getDateTime(unixTimeStamp, Util._YYMM));
			
			
			//t3700
			advice.setT3700(advice.getT1300() + advice.getT1100());
			
			//t4100
			advice.setT4100(Util.bcd2Ascii(pprTxnReqOnline.getRespNewDeviceID()));
			
			//t4101
			advice.setT4101(Util.bcd2Ascii(pprTxnReqOnline.getRespDeviceID()));
					
			//t4103
			advice.setT4103(Util.getMACAddress());
			
			//t4104
			advice.setT4104(configManager.getReaderID());
			
			
			//t4200
			advice.setT4200(String.format("%08d", Util.bytes2Long(pprTxnReqOnline.getRespNewServiceProviderID(), 0, pprTxnReqOnline.getRespNewServiceProviderID().length, true)));
			
			//t4210
			advice.setT4210(configManager.getNewLocationID());
			
			//t4800
			advice.setT4800(String.format("%02X", pprTxnReqOnline.getRespPurseVersionNumber()));
			
			//t4801
			byte[] t4801 = new byte[CMAS_4801_SIZE/2];
			System.arraycopy(pprTxnReqOnline.getRespLastCreditTxnLog(), 0, t4801, 0, pprTxnReqOnline.getRespLastCreditTxnLog().length);		
			advice.setT4801(Util.bcd2Ascii(t4801));
			
			//t4802
			advice.setT4802(String.format("%02X", pprTxnReqOnline.getRespIssuerCode()));
			
			//t4803
			advice.setT4803(String.format("%02X", pprTxnReqOnline.getRespBankCode()));
			
			
			//t4804
			advice.setT4804(String.format("%02X", pprTxnReqOnline.getRespAreaCode()));
								
			//t4805
			advice.setT4805(Util.bcd2Ascii(pprTxnReqOnline.getRespSubAreaCode()));
								
			//t4808									 pprAuthTxnOnine
			advice.setT4808(Util.IntelFormat2Decimal(pprAuthTxnOnline.getRespTSQN(), 0, pprAuthTxnOnline.getRespTSQN().length));
			
			//t4809
			advice.setT4809(String.format("%02X", pprTxnReqOnline.getRespTxnMode()));
			
			//t4810
			advice.setT4810(String.format("%02X", pprTxnReqOnline.getRespTxnQualifier()));
			
			//t4811		
			advice.setT4811(String.format("%06d", Util.bytes2Long(pprTxnReqOnline.getRespTxnSNBeforeTxn(), 0, pprTxnReqOnline.getRespTxnSNBeforeTxn().length, true)));
			
					
			//t4812
			advice.setT4812(Util.bcd2Ascii(pprTxnReqOnline.getRespCTC()));
									
			//t4813
			b = Arrays.copyOfRange(pprTxnReqOnline.getRespSamID(), 0, 2);		
			advice.setT4813(String.format("%06d", Util.bytes2Long(b, 0, b.length)));
			
			//t4826
			advice.setT4826(String.format("%02X", pprTxnReqOnline.getRespCardPhysicalIDLength()));
			
			//t5301
			advice.setT5301(String.format("%02X", pprTxnReqOnline.getRespSamKVN()));
			
			
			if(pprTxnReqOnline.getRespPurseVersionNumber() != 0x00){//CPU card
				//t5303
				advice.setT5303(String.format("%02X", pprAuthTxnOnline.getRespMAC()[0]));

				//t5304
				advice.setT5304(String.format("%02X", pprAuthTxnOnline.getRespMAC()[1]));
				
				//t5305
				advice.setT5305(String.format("%02X", pprTxnReqOnline.getRespSignatureKeyKVN()));
			} 
			/*else {
				advice.setT5303("0000");
				advice.setT5304("0000");
				advice.setT5305("0000");
			}*/
				
			//t5361						
			advice.setT5361(Util.bcd2Ascii(pprTxnReqOnline.getRespSamID()));
				
			//t5362			
			advice.setT5362(Util.bcd2Ascii(pprTxnReqOnline.getRespSamSN()));
				
			//t5363
			advice.setT5363(Util.bcd2Ascii(pprTxnReqOnline.getRespSamCRN()));
				
			//t5371
			advice.setT5371(Util.bcd2Ascii(pprTxnReqOnline.getRespSTAC()));
				
			//t5501
			advice.setT5501(configManager.getBatchNo());
				
			//t5503
			advice.setT5503(pprTxnReqOnline.getReqTMLocationID());
				
			//t5504
			advice.setT5504(pprTxnReqOnline.getReqTMID());
				
			//t5510
			advice.setT5510(pprTxnReqOnline.getReqTMAgentNumber());
				
			//t6404
			b = Arrays.copyOfRange(pprAuthTxnOnline.getRespMAC(), 2, pprAuthTxnOnline.getRespMAC().length);			
			advice.setT6404(Util.bcd2Ascii(b));
				
			//t6405
			b = pprAuthTxnOnline.getRespSIGN();
			advice.setT6405(Util.bcd2Ascii(b));
				
			//t6406
			b = new byte[CMAS_6406_SIZE/2];
			System.arraycopy(pprTxnReqOnline.getRespTxnCrypto(), 0, b, 0, pprTxnReqOnline.getRespTxnCrypto().length);
			//b = pprTxnReqOnline.getRespTxnCrypto();
			advice.setT6406(Util.bcd2Ascii(b));	
		}
		
	
	//3.1帳務授權交易
	public void readerField2CmasSpec(PPR_TxnReqOnline pprTxnReqOnline, CmasDataSpec spec, IConfigManager configManager){
		byte[] b = null;
		spec.setT0100("0100");
		
		//t0200
		b = Arrays.copyOfRange(pprTxnReqOnline.getRespCardPhysicalID(), 0, pprTxnReqOnline.getRespCardPhysicalIDLength());
		String t0200 = Util.IntelFormat2Decimal(b, 0, pprTxnReqOnline.getRespCardPhysicalIDLength());				
		spec.setT0200(t0200);
		
		//t0211
		spec.setT0211(Util.bcd2Ascii(pprTxnReqOnline.getRespPID()));
				
		//t0213 cardType
		spec.setT0213(String.format("%02X", pprTxnReqOnline.getRespCardType()));
				
		//t0214 
		spec.setT0214(String.format("%02X", pprTxnReqOnline.getRespPersonalProfile()));
				
		//t0300
		//setttingUp from caller
				
		//t0400
		b = pprTxnReqOnline.getRespTxnAmt();
		String t0400 = Util.IntelFormat2Decimal(b, 0, pprTxnReqOnline.getRespTxnAmt().length);
		spec.setT0400(t0400);
						
				
		//t0409
		b = pprTxnReqOnline.getRespSingleAutoLoadTxnAmt();
		String t0409 = Util.IntelFormat2Decimal(b, 0, b.length);
		spec.setT0409(t0409);
				
		//t0410 Purse Balance Before TXN
		b = pprTxnReqOnline.getRespPurseBalanceBeforeTxn();
		String t0410 = Util.IntelFormat2Decimal(b, 0, b.length);
		spec.setT0410(t0410);
				
		//t1100,t1101
		spec.setT1100(configManager.getTMSerialNo());
		spec.setT1101(configManager.getTMSerialNo());
				
		//t1200, t1201, t1300, t1301
		b = pprTxnReqOnline.getReqTMTXNDateTime().getBytes();
		String time = new String(Arrays.copyOfRange(b, 8, 14)); 
		String date = new String(Arrays.copyOfRange(b, 0, 8));
		spec.setT1200(time);
		spec.setT1201(time);
		spec.setT1300(date);
		spec.setT1301(date);
				
		//t1400,t1402
		b = pprTxnReqOnline.getRespPurseExpDate();
		int unixTimeStamp = (int)Util.bytes2Long(b, 0, b.length, true);		
		spec.setT1400(Util.getDateTime(unixTimeStamp, Util._YYMM));
		spec.setT1402(Util.sGetDate(unixTimeStamp));		
		
		
		
		//t3700
		spec.setT3700(spec.getT1300() + spec.getT1100());
				
		//t4100
		spec.setT4100(Util.bcd2Ascii(pprTxnReqOnline.getRespNewDeviceID()));
				
		//t4101
		spec.setT4101(Util.bcd2Ascii(pprTxnReqOnline.getRespDeviceID()));
			
		//t4102
		spec.setT4102(Util.sGetLocalIpAddress());
		
		//t4103
		spec.setT4103(Util.getMACAddress());
				
		//t4104
		spec.setT4104(configManager.getReaderID());
				
				
		//t4200
		b = pprTxnReqOnline.getRespNewServiceProviderID();
		spec.setT4200(String.format("%08d", Util.bytes2Long(b, 0, b.length,true)));
				
		//t4210
		spec.setT4210(configManager.getNewLocationID());
				
		//t4800
		spec.setT4800(String.format("%02X", pprTxnReqOnline.getRespPurseVersionNumber()));
				
		//t4801
		byte[] t4801 = new byte[CMAS_4801_SIZE/2];
		System.arraycopy(pprTxnReqOnline.getRespLastCreditTxnLog(), 0, t4801, 0, pprTxnReqOnline.getRespLastCreditTxnLog().length);		
		spec.setT4801(Util.bcd2Ascii(t4801));
				
		//t4802
		spec.setT4802(String.format("%02X", pprTxnReqOnline.getRespIssuerCode()));
				
		//t4803
		spec.setT4803(String.format("%02X", pprTxnReqOnline.getRespBankCode()));
				
				
		//t4804
		spec.setT4804(String.format("%02X", pprTxnReqOnline.getRespAreaCode()));
									
		//t4805
		spec.setT4805(Util.bcd2Ascii(pprTxnReqOnline.getRespSubAreaCode()));
		
		//t4806
		spec.setT4806(Util.bcd2Ascii(pprTxnReqOnline.getRespProfileExpDate()));
				
								
		//t4813		
		spec.setT4813(String.format("%06d", Util.bytes2Long(pprTxnReqOnline.getRespLoyaltyCounter(), 0, pprTxnReqOnline.getRespLoyaltyCounter().length)));
				
		//t4826
		spec.setT4826(String.format("%02X", pprTxnReqOnline.getRespCardPhysicalIDLength()));
				
		//t5301
		spec.setT5301(String.format("%02X", pprTxnReqOnline.getRespSamKVN()));
				
			

		//t5304
		spec.setT5304(String.format("%02X", pprTxnReqOnline.getRespHostAdminKeyKVN()));
					
		//t5305
		//deductAdvice.setT5305(String.format("%02X", pprTxnReqOffline.getRespSignKeyKVN()));

					
		//t5361		
		spec.setT5361(Util.bcd2Ascii(pprTxnReqOnline.getRespSamID()));
					
		//t5362		
		spec.setT5362(Util.bcd2Ascii(pprTxnReqOnline.getRespSamSN()));
					
		//t5363
		spec.setT5363(Util.bcd2Ascii(pprTxnReqOnline.getRespSamCRN()));
					
					
		//t5371
		spec.setT5371(Util.bcd2Ascii(pprTxnReqOnline.getRespSID()));
					
		//t5501			
		spec.setT5501(configManager.getBatchNo());
					
		//t5503
		spec.setT5503(pprTxnReqOnline.getReqTMLocationID());
					
		//t5504
		spec.setT5504(pprTxnReqOnline.getReqTMID());
					
		//t5510
		spec.setT5510(pprTxnReqOnline.getReqTMAgentNumber());
			
	
		//t6000
		spec.setT6000(Util.bcd2Ascii(pprTxnReqOnline.getRespReaderFWVersion()));
		
		//t6001
		spec.setT6001(Util.bcd2Ascii(pprTxnReqOnline.getRespReaderAVRData()));
		
		//t6004
		spec.setT6004(configManager.getBlackListVersion());
		
		//t6400	
		spec.setT6400(Util.bcd2Ascii(pprTxnReqOnline.getRespSTAC()));
	
	}
	
	
	
	
	
	public void readerField2CmasSpec(PPR_TxnReqOffline pprTxnReqOffline, CmasDataSpec spec, IConfigManager configManager){
		//Reader Response 6415, goOnline
		byte[] b = null;
		spec.setT0100("0100");

		//t0200
		b = Arrays.copyOfRange(pprTxnReqOffline.getRespCardPhysicalID(), 0, pprTxnReqOffline.getRespCardPhysicalIDLength());
		String t0200 = Util.IntelFormat2Decimal(b, 0, pprTxnReqOffline.getRespCardPhysicalIDLength());				
		spec.setT0200(t0200);
		
		if(pprTxnReqOffline.getRespPurseVersionNumber() != 0x00){
			//t0211
			spec.setT0211(Util.bcd2Ascii(pprTxnReqOffline.getRespPID()));
		}
		
		//t0213 cardType
		spec.setT0213(String.format("%02X", pprTxnReqOffline.getRespCardType()));
				
		//t0214 
		spec.setT0214(String.format("%02X", pprTxnReqOffline.getRespPersonalProfile()));
				
		//t0300
		//setttingUp from caller
				
		//t0400
		b = pprTxnReqOffline.getRespTxnAmt();
		String t0400 = Util.IntelFormat2Decimal(b, 0, pprTxnReqOffline.getRespTxnAmt().length);
		spec.setT0400(t0400);
						
			
		//t0409
		b = pprTxnReqOffline.getRespSingleAutoLoadTxnAmt();
		String t0409 = Util.IntelFormat2Decimal(b, 0, b.length);
		spec.setT0409(t0409);
				
		//t0410 Purse Balance Before TXN
		b = pprTxnReqOffline.getRespPurseBalanceBeforeTxn();
		String t0410 = Util.IntelFormat2Decimal(b, 0, b.length);
		spec.setT0410(t0410);
				
		//t1100,t1101
		spec.setT1100(configManager.getTMSerialNo());
		spec.setT1101(configManager.getTMSerialNo());
				
		//t1200, t1201, t1300, t1301
		b = pprTxnReqOffline.getReqTMTXNDateTime().getBytes();
		String time = new String(Arrays.copyOfRange(b, 8, 14)); 
		String date = new String(Arrays.copyOfRange(b, 0, 8));
		spec.setT1200(time);
		spec.setT1201(time);
		spec.setT1300(date);
		spec.setT1301(date);


		//t1400,t1402
		b = pprTxnReqOffline.getRespPurseExpDate();
		int unixTimeStamp = (int)Util.bytes2Long(b, 0, b.length, true);		
		spec.setT1400(Util.getDateTime(unixTimeStamp, Util._YYMM));
		spec.setT1402(Util.sGetDate(unixTimeStamp));		
		
		
		
		//t3700
		spec.setT3700(spec.getT1300() + spec.getT1100());
				
		//t4100
		spec.setT4100(Util.bcd2Ascii(pprTxnReqOffline.getRespNewDeviceID()));
				
		//t4101
		spec.setT4101(Util.bcd2Ascii(pprTxnReqOffline.getRespDeviceID()));
			
		//t4102
		spec.setT4102(Util.sGetLocalIpAddress());
		
		//t4103
		spec.setT4103(Util.getMACAddress());
				
		//t4104
		spec.setT4104(configManager.getReaderID());
				
				
		//t4200
		b = pprTxnReqOffline.getRespNewServiceProviderID();
		spec.setT4200(String.format("%08d", Util.bytes2Long(b, 0, b.length,true)));
				
		//t4210
		spec.setT4210(configManager.getNewLocationID());
				
		//t4800
		spec.setT4800(String.format("%02X", pprTxnReqOffline.getRespPurseVersionNumber()));
				
		//t4801
		byte[] t4801 = new byte[CMAS_4801_SIZE/2];
		System.arraycopy(pprTxnReqOffline.getRespLastCreditTxnLog(), 0, t4801, 0, pprTxnReqOffline.getRespLastCreditTxnLog().length);		
		spec.setT4801(Util.bcd2Ascii(t4801));
				
		//t4802
		spec.setT4802(String.format("%02X", pprTxnReqOffline.getRespIssuerCode()));
				
		//t4803
		spec.setT4803(String.format("%02X", pprTxnReqOffline.getRespBankCode()));
				
				
		//t4804
		spec.setT4804(String.format("%02X", pprTxnReqOffline.getRespAreaCode()));
									
		//t4805
		spec.setT4805(Util.bcd2Ascii(pprTxnReqOffline.getRespSubAreaCode()));
		
		
		
		if(pprTxnReqOffline.getRespPurseVersionNumber() != 0x00){
			//t4806
			spec.setT4806(Util.bcd2Ascii(pprTxnReqOffline.getRespProfileExpDate()));
		
			//t4809
			spec.setT4809(String.format("%02X", pprTxnReqOffline.getRespTxnMode()));
		
			//t4810
			spec.setT4810(String.format("%02X", pprTxnReqOffline.getRespTxnQualifier()));
		
		}
			
		//t4811
		long l = Util.bytes2Long(pprTxnReqOffline.getRespTxnSNBeforeTxn(), 0, pprTxnReqOffline.getRespTxnSNBeforeTxn().length, true);
		spec.setT4811(String.format("%06d", l));
		
		//t4812
		spec.setT4812(Util.bcd2Ascii(pprTxnReqOffline.getRespCTC()));
		
		//t4813		
		b = Arrays.copyOfRange(pprTxnReqOffline.getRespCPDSAMID(), 0, 2);		
		spec.setT4813(String.format("%06d", Util.bytes2Long(b, 0, b.length)));
			
		//t4814
		spec.setT4814(Util.IntelFormat2Decimal(pprTxnReqOffline.getRespDeposit(), 0, pprTxnReqOffline.getRespDeposit().length));
		
		
		//t4821
		if(pprTxnReqOffline.getRespPurseVersionNumber() != 0x00){
			b = Arrays.copyOfRange(pprTxnReqOffline.getRespCardOneDayQuota(), 0, 2);			
			spec.setT4821(Util.bcd2Ascii(b) + Util.bcd2Ascii(pprTxnReqOffline.getRespCardOneDayQuotaDate()));		
		}

		
		//t4826
		spec.setT4826(String.format("%02X", pprTxnReqOffline.getRespCardPhysicalIDLength()));
				
		//t5301
		spec.setT5301(String.format("%02X", pprTxnReqOffline.getRespCPDKVN_SAMKVN()));
				
			
		//t5302,t5303
		if(pprTxnReqOffline.getRespPurseVersionNumber() != 0x00){
			spec.setT5302(String.format("%02X%02X%02X", pprTxnReqOffline.getRespCPUAdminKeyKVN(), pprTxnReqOffline.getRespCreditKeyKVN(), pprTxnReqOffline.getRespCPUIssuerKeyKVN()));
			spec.setT5303(String.format("%02X", pprTxnReqOffline.getRespMAC()[0]));
		}

		
		
		//t5304
		spec.setT5304(String.format("%02X", pprTxnReqOffline.getRespBankCode()));
					
						
		//t5361
		b = pprTxnReqOffline.getRespCPDSAMID();
		byte[] c = Arrays.copyOfRange(b, 2, 10);
		spec.setT5361(Util.bcd2Ascii(c));
					
		//t5362
		c = Arrays.copyOfRange(b, 10, 14);
		spec.setT5362(Util.bcd2Ascii(c));
					
		//t5363
		spec.setT5363(Util.bcd2Ascii(pprTxnReqOffline.getRespCPDRAN_SAMCRN()));
					
				
		//t5371
		spec.setT5371(Util.bcd2Ascii(pprTxnReqOffline.getRespSIDSTAC()));
					
		//t5501			
		spec.setT5501(configManager.getBatchNo());
					
		//t5503
		spec.setT5503(pprTxnReqOffline.getReqTMLocationID());
					
		//t5504
		spec.setT5504(pprTxnReqOffline.getReqTMID());
					
		
		//t5510
		spec.setT5510(pprTxnReqOffline.getReqTMAgentNumber());
			
	
		//t6000
		spec.setT6000(Util.bcd2Ascii(pprTxnReqOffline.getRespReaderFWVersion()));
		
		
		//t6004
		spec.setT6004(configManager.getBlackListVersion());
		
		if(pprTxnReqOffline.getRespPurseVersionNumber() == 0x00){
			//t6400
			spec.setT6400(Util.bcd2Ascii(pprTxnReqOffline.getRespSIDSTAC()));
			
		}
		
		//t6406
		if(pprTxnReqOffline.getRespPurseVersionNumber() != 0x00){
			spec.setT6406(Util.bcd2Ascii(pprTxnReqOffline.getRespSVCrypto()));
		}
	}
	
	// ********************* LockAdvice ****************************
	//Reader Response ErrorCode 640E, 610F, 6418, 6103 , pack advice
	public boolean readerErrorCode2CmasLockAdvice(ErrorResponse errorRespFld, CmasDataSpec lockAdvice, IConfigManager configManager, IRespCode respCode){
		
		boolean result = true;
		try{
			
			byte[] b = null;
			lockAdvice.setT0100("0320");
			
			//t0200
			b = Arrays.copyOfRange(errorRespFld.getCardPhysicalID(), 0, errorRespFld.getCardPhysicalIDLength());
			String t0200 = Util.IntelFormat2Decimal(b, 0, errorRespFld.getCardPhysicalIDLength());
			lockAdvice.setT0200(t0200);
					
			//t0211
			lockAdvice.setT0211(Util.bcd2Ascii(errorRespFld.getPID()));
					
			//t0213 cardType
			lockAdvice.setT0213(String.format("%02X", errorRespFld.getCardType()));
					
			//t0214 
			lockAdvice.setT0214(String.format("%02X", errorRespFld.getPersonalProfile()));
					
			//t0300
			lockAdvice.setT0300("596100");
			
			lockAdvice.setT0410(String.format("%d", Util.bytes2Long(errorRespFld.getPurseBalanceBeforeTxn(), 0, errorRespFld.getPurseBalanceBeforeTxn().length, true)));
					
			//t1100,t1101
			lockAdvice.setT1100(configManager.getTMSerialNo());
			lockAdvice.setT1101(configManager.getTMSerialNo());
			
			lockAdvice.setT1200(Util.sGetTime());
			lockAdvice.setT1201(Util.sGetTime());
			
			lockAdvice.setT1300(Util.sGetDate());
			lockAdvice.setT1301(Util.sGetDate());
			
			//t3700
			lockAdvice.setT3700(lockAdvice.getT1300() + lockAdvice.getT1100());
				
			//t4100
			lockAdvice.setT4100(Util.bcd2Ascii(errorRespFld.getNewDeviceID()));
					
			//t4101
			lockAdvice.setT4101(Util.bcd2Ascii(errorRespFld.getDeviceID()));
			
			//t4200
			b = errorRespFld.getNewServiceProviderID();
			//Util.arrayReverse(b);
			lockAdvice.setT4200(String.format("%08d", Util.bytes2Long(b, 0, b.length,true)));
					
			//t4210
			lockAdvice.setT4210(configManager.getNewLocationID());
					
			//t4800
			lockAdvice.setT4800(String.format("%02X", errorRespFld.getPurseVersionNumber()));
					
			if(respCode != ReaderRespCode._6103){
				//t4801		
				byte[] t4801 = new byte[CMAS_4801_SIZE/2];
				System.arraycopy(errorRespFld.getLastCreditTxnLog(), 0, t4801, 0, errorRespFld.getLastCreditTxnLog().length);		
				lockAdvice.setT4801(Util.bcd2Ascii(t4801));
			
				//t4803
				lockAdvice.setT4803(String.format("%02X", errorRespFld.getBankCode()));
				
				
				//t4804
				lockAdvice.setT4804(String.format("%02X", errorRespFld.getAreaCode()));
									
				//t4805
				lockAdvice.setT4805(Util.bcd2Ascii(errorRespFld.getSubAreaCode()));
				
				//t4806
				lockAdvice.setT4806(Util.bcd2Ascii(errorRespFld.getProfileExpDate()));
				
			
				//t4814
				//b = Arrays.copyOfRange(errorRespFld.getDeposit(), 0, errorRespFld.getDeposit().length);
				String t4814 = Util.IntelFormat2Decimal(errorRespFld.getDeposit(), 0, errorRespFld.getDeposit().length);
				lockAdvice.setT4814(t4814);
				
				//t4818
				lockAdvice.setT4818(Util.bcd2Ascii(errorRespFld.getAnotherEV()));
				
				//t4828		
				byte[] t4828 = new byte[CMAS_4828_SIZE/2];
				t4828[0] = errorRespFld.getMifareSettingParameter();				
				lockAdvice.setT4828(Util.bcd2Ascii(t4828));
				
				//t4829		
				byte[] t4829 = new byte[CMAS_4829_SIZE/2];
				t4829[0] = errorRespFld.getCPUSettingParameter();				
				lockAdvice.setT4829(Util.bcd2Ascii(t4829));			
			} else {
				
				//t4801		
				byte[] t4801 = new byte[CMAS_4801_SIZE/2];
				lockAdvice.setT4801(Util.bcd2Ascii(t4801));
			
				//t4803
				lockAdvice.setT4803("00");
				
				
				//t4804
				lockAdvice.setT4804("00");
									
				//t4805
				lockAdvice.setT4805("0000");
				
				//t4806
				lockAdvice.setT4806("00000000");
				
			
				//t4814
				//b = Arrays.copyOfRange(errorRespFld.getDeposit(), 0, errorRespFld.getDeposit().length);
				//String t4814 = Util.IntelFormat2Decimal(b, 0, errorRespFld.getDeposit().length);
				lockAdvice.setT4814("000000");
				
				//t4818
				lockAdvice.setT4818("000000");
				
				//t4828		
				byte[] t4828 = new byte[CMAS_4828_SIZE/2];
				//t4828[0] = errorRespFld.getMifareSettingParameter();				
				lockAdvice.setT4828(Util.bcd2Ascii(t4828));
				
				//t4829		
				byte[] t4829 = new byte[CMAS_4829_SIZE/2];
				//t4829[0] = errorRespFld.getCPUSettingParameter();				
				lockAdvice.setT4829(Util.bcd2Ascii(t4829));	
			}
			
			if(respCode == ReaderRespCode._6103){
				//t4812
				lockAdvice.setT4812(Util.bcd2Ascii(errorRespFld.getCTC()));
			} else lockAdvice.setT4812("0000");
			//t4802
			lockAdvice.setT4802(String.format("%02X", errorRespFld.getIssuerCode()));
					
			lockAdvice.setT4826(String.format("%02X", errorRespFld.getCardPhysicalIDLength()));
			
			//t5501
			lockAdvice.setT5501(configManager.getBatchNo());
						
			//t5503
			lockAdvice.setT5503(configManager.getTMLocationID());
						
			//t5504
			lockAdvice.setT5504(configManager.getTMID());
						
			//t5510
			lockAdvice.setT5510(configManager.getTMAgentNo());
		} catch (Exception e) {
			result = false;
			logger.error(e.getMessage());
			e.printStackTrace();
		}	
		return result;
	}
	
	
	public boolean blackList2CmasLockAdvice(PPR_TxnReqOffline readerFld, CmasDataSpec lockAdvice, IConfigManager configManager){
		boolean result = true;
		try{
			
			byte[] b = null;
			lockAdvice.setT0100("0320");
			
			//t0200			
			String t0200 = Util.IntelFormat2Decimal(readerFld.getRespCardPhysicalID(), 0, readerFld.getRespCardPhysicalIDLength());
			lockAdvice.setT0200(t0200);
					
			//t0211
			lockAdvice.setT0211(Util.bcd2Ascii(readerFld.getRespPID()));
					
			//t0213 cardType
			lockAdvice.setT0213(String.format("%02X", readerFld.getRespCardType()));
					
			//t0214 
			lockAdvice.setT0214(String.format("%02X", readerFld.getRespPersonalProfile()));
					
			//t0300
			lockAdvice.setT0300("596100");
			
			lockAdvice.setT0410(String.format("%d", Util.bytes2Long(readerFld.getRespPurseBalanceBeforeTxn(), 0, readerFld.getRespPurseBalanceBeforeTxn().length, true)));
					
			//t1100,t1101
			lockAdvice.setT1100(configManager.getTMSerialNo());
			lockAdvice.setT1101(configManager.getTMSerialNo());
			
			lockAdvice.setT1200(Util.sGetTime());
			lockAdvice.setT1201(Util.sGetTime());
			
			lockAdvice.setT1300(Util.sGetDate());
			lockAdvice.setT1301(Util.sGetDate());
			
			//t3700
			lockAdvice.setT3700(lockAdvice.getT1300() + lockAdvice.getT1100());
				
			//t4100
			lockAdvice.setT4100(Util.bcd2Ascii(readerFld.getRespNewDeviceID()));
					
			//t4101
			lockAdvice.setT4101(Util.bcd2Ascii(readerFld.getRespDeviceID()));
			
			//t4200
			b = readerFld.getRespNewServiceProviderID();
			lockAdvice.setT4200(String.format("%08d", Util.bytes2Long(b, 0, b.length,true)));
					
			//t4210
			lockAdvice.setT4210(configManager.getNewLocationID());
					
			//t4800
			lockAdvice.setT4800(String.format("%02X", readerFld.getRespPurseVersionNumber()));
					
			//if(respCode != ReaderRespCode._6103){
			//t4801		
			byte[] t4801 = new byte[CMAS_4801_SIZE/2];
			System.arraycopy(readerFld.getRespLastCreditTxnLog(), 0, t4801, 0, readerFld.getRespLastCreditTxnLog().length);		
			lockAdvice.setT4801(Util.bcd2Ascii(t4801));
			
			//t4803
			lockAdvice.setT4803(String.format("%02X", readerFld.getRespBankCode()));
				
				
			//t4804
			lockAdvice.setT4804(String.format("%02X", readerFld.getRespAreaCode()));
									
			//t4805
			lockAdvice.setT4805(Util.bcd2Ascii(readerFld.getRespSubAreaCode()));
				
			//t4806
			lockAdvice.setT4806(Util.bcd2Ascii(readerFld.getRespProfileExpDate()));
				
			
			//t4814			
			String t4814 = Util.IntelFormat2Decimal(readerFld.getRespDeposit(), 0, readerFld.getRespDeposit().length);
			lockAdvice.setT4814(t4814);
				
			//t4818-無此欄位
			lockAdvice.setT4818("000000");
				
			//t4828-無此欄位		
			byte[] t4828 = new byte[CMAS_4828_SIZE/2];						
			lockAdvice.setT4828(Util.bcd2Ascii(t4828));
				
			//t4829		
			byte[] t4829 = new byte[CMAS_4829_SIZE/2];							
			lockAdvice.setT4829(Util.bcd2Ascii(t4829));			
			
			//T4812
			lockAdvice.setT4812(Util.bcd2Ascii(readerFld.getRespCTC()));
			
			//t4802
			lockAdvice.setT4802(String.format("%02X", readerFld.getRespIssuerCode()));
					
			lockAdvice.setT4826(String.format("%02X", readerFld.getRespCardPhysicalIDLength()));
			
			//t5501
			lockAdvice.setT5501(configManager.getBatchNo());
						
			//t5503
			lockAdvice.setT5503(configManager.getTMLocationID());
						
			//t5504
			lockAdvice.setT5504(configManager.getTMID());
						
			//t5510
			lockAdvice.setT5510(configManager.getTMAgentNo());
		} catch (Exception e) {
			result = false;
			logger.error(e.getMessage());
			e.printStackTrace();
		}	
		return result;	
	}
	
	// ********************* LockAdvice End****************************
	public int cmasSpec2ReaderField(CmasDataSpec spec, PPR_SignOn pprSignon, IConfigManager configManager)
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
	
	public void cmasSpec2ReaderField(CmasDataSpec spec, PPR_AuthTxnOffline pprAuthTxnOffline, IConfigManager configManager){
		
		try{
			
			
			if(spec.getT4800().equalsIgnoreCase("00")){
				byte[] b = new byte[PPR_AuthTxnOffline.lReqHVCrypto];
				logger.debug("t6401:"+spec.getT6401());
				byte[] c = Util.ascii2Bcd(spec.getT6401());	
				
				System.arraycopy(c, 0, b, 0, c.length);		
				
				logger.debug("b[]:"+Util.hex2StringLog(b));
				pprAuthTxnOffline.setReqHVCrypto(b);
			} else {
				logger.debug("t6407:"+spec.getT6407());
				byte[] c = Util.ascii2Bcd(spec.getT6407());	
				pprAuthTxnOffline.setReqHVCrypto(c);
				
			}
		} catch(Exception e) {
			logger.error("Exception:"+e.getMessage());
			e.printStackTrace();
		}
	}
	

	public int cmasSpec2ReaderField(CmasDataSpec spec, PPR_AuthTxnOnline pprAuthTxnOnline, IConfigManager configManager){
		
		logger.info("start");
		int result = 0;
		try {
			
			
			//for展期交易
			if(spec.getT0300().equalsIgnoreCase(CmasDataSpec.PCode.CPU_EXTENSION.toString()))
			{
				//todo...
			}
			
			logger.debug("T4800:["+spec.getT4800()+"]");
			if(spec.getT4800().equalsIgnoreCase("00") == false){
				logger.debug("T4800 != 00");
				byte[] txnToken = Util.ascii2Bcd(spec.getT6409());
				pprAuthTxnOnline.setReqTxnToken(txnToken);
			} else logger.debug("T4800 == 00");
			
			
			if(spec.getT4800().equalsIgnoreCase("02") == false){
				logger.debug("T4800 != 02");
				byte[] htac = Util.ascii2Bcd(spec.getT6401());
				pprAuthTxnOnline.setReqHTAC(htac);
			} else logger.debug("T4800 == 02");
		} catch(Exception e) {
			logger.error("0110 to PPR_AuthTxnOnline Exception:"+e.getMessage());
			
		}
		
		logger.info("end");
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
		logger.info("Pack CMAS data:"+result);
		
		
		return result;
	}
	
	private String tagGenerater(int tag, CmasDataSpec spec){
		String result = "";		
		String tagName = String.format("T%04d", tag);
		String start = "<"+tagName+">";
		String end = "</"+tagName+">";
		
		
		
		switch(tag)
		{
		/*
			case 100:
				result = start + spec.getT0100() + end ;
				break;
			
			case 200:
				result = start + spec.getT0200() + end ;
				break;
				
			case 211:
				result = start + spec.getT0211() + end ;
				break;	
			
			case 213:
				result = start + spec.getT0213() + end ;
				break;	
				
			case 214:
				result = start + spec.getT0214() + end ;
				break;
				
			case 300:
				result = start + spec.getT0300() + end ;
				break;
		
			case 400:
				result = start + spec.getT0400() + end ;
				break;	
				
			case 408:
				result = start + spec.getT0408() + end ;
				break;	
				
			case 409:
				result = start + spec.getT0409() + end ;
				break;
				
			case 410:
				result = start + spec.getT0410() + end ;
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
				
			case 1400:
				result = start + spec.getT1400() + end ;
				break;
				
			case 1402:
				result = start + spec.getT1402() + end ;
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
			case 4800:
				result = start + spec.getT4800() + end ;
				break;	
				
			case 4801:
				result = start + spec.getT4801() + end ;
				break;
				
			case 4802:
				result = start + spec.getT4802() + end ;
				break;
			
			case 4803:
				result = start + spec.getT4803() + end ;
				break;	
			
			case 4804:
				result = start + spec.getT4804() + end ;
				break;	
				
			case 4805:
				result = start + spec.getT4805() + end ;
				break;	
			
			case 4806:
				result = start + spec.getT4806() + end ;
				break;	
				
			case 4808:
				result = start + spec.getT4808() + end ;
				break;	
				
			case 4809:
				result = start + spec.getT4809() + end ;
				break;	
				
			case 4810:
				result = start + spec.getT4810() + end ;
				break;	
				
			case 4811:
				result = start + spec.getT4811() + end ;
				break;	
			
			case 4812:
				result = start + spec.getT4812() + end ;
				break;	
				
			case 4813:
				result = start + spec.getT4813() + end ;
				break;	
			
			case 4814:
				result = start + spec.getT4814() + end ;
				break;	
			
			case 4817:
				result = start + spec.getT4817() + end ;
				break;	
			
			case 4818:
				result = start + spec.getT4818() + end ;
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
			
			case 4826:
				result = start + spec.getT4826() + end ;
				break;	
			
			case 4828:
				result = start + spec.getT4828() + end ;
				break;	
				
			case 4829:
				result = start + spec.getT4829() + end ;
				break;
				
			case 5301:
				result = start + spec.getT5301() + end ;
				break;
				
			case 5303:
				result = start + spec.getT5303() + end ;
				break;
				
			case 5304:
				result = start + spec.getT5304() + end ;
				break;
				
			case 5305:
				result = start + spec.getT5305() + end ;
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
				
				*/
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
						
				break;	
			
			case 5596:
				SubTag5596 t5596 = spec.getT5596();
				result  = start + "<T559601>"+t5596.getT559601()+"</T559601>";
				result += "<T559602>"+t5596.getT559601()+"</T559602>";
				result += "<T559603>"+t5596.getT559601()+"</T559603>";
				result += "<T559604>"+t5596.getT559601()+"</T559604>" + end;
				
				break;
				
				/*
			case 6001:
				result = start + spec.getT6001() + end ;
				break;				
				
			case 6000:
				result = start + spec.getT6000() + end;
				break;	
			*/
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
				
				/*
			case 6003:
				result = start + spec.getT6003() + end;
				break;	
				
			case 6004:
				result = start + spec.getT6004() + end;
				break;	
				
			case 6400:
				result = start + spec.getT6400() + end;
				break;	
				
			case 6404:
				result = start + spec.getT6404() + end ;
				break;
				
			case 6405:
				result = start + spec.getT6405() + end ;
				break;
				
			case 6406:
				//if(spec.getT4825().equalsIgnoreCase("01"))
					result = start + spec.getT6406() + end;
				//else
				//	result = "";
				break;	
				
				
			case 6408:
				result = start + spec.getT6408() + end;
				break;*/
				
			default:
				//if tagValue was null or "", did not needed to pack the tagname 
				Object tagValue = spec.getTagValue(tag);
				if(tag == 211) logger.debug("0211 value:"+tagValue);
				if(tagValue ==null || tagValue=="")
					return result;
				
				result = start + tagValue + end;
				
				//logger.error("oh!oh!, unKnowen tag to generate:"+tag);	
				break;
		}
		
		return result;
	}
	
	
	
}
