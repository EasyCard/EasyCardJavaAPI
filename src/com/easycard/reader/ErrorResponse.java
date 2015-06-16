package com.easycard.reader;

import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

public class ErrorResponse extends BaseResponseAutoParser{

	static Logger logger = Logger.getLogger(ErrorResponse.class);
	private final int ERROR1_LEN = 120 + 2;//640E(餘額異常)、610F(二代餘額異常)、6418(通路限制)
	private final int ERROR2_LEN = 40 + 2;//6103(CPD檢查異常)
	public final static int err640E = 0x640E;
	public final static int err610F = 0x610F;
	public final static int err6418 = 0x6418;
	public final static int err6103 = 0x6103;
	
	private int dataBodyLen;
	
	public byte purseVersionNumber;
	public byte purseUsageControl;
	public byte[] singleAutoLoadTxnAmt=null;
	public byte[] PID=null;
	public byte cpuAdminKeyKVN;
	
	
	public byte[] subAreaCode=null;
	public byte[] purseExpDate=null;
	public byte[] purseBalanceBeforeTxn=null;
	public byte[] TxnSNBeforeTxn=null;
	
	public byte cardType;
	public byte personalProfile;
	public byte[] profileExpDate=null;
	public byte areaCode;
	public byte[] cardPhysicalID=null;
	
	public byte cardPhysicalIDLength;
	
	public byte[] deviceID=null;
	
	public byte[] newDeviceID=null;
	public byte serviceProviderID;
	public byte[] newServiceProviderID=null;
	public byte locationID;
	public byte[] newLocationID=null;
	
	public byte[] deposit=null;
	public byte issuerCode;
	public byte bankCode;
	
	
	public byte[] LastCreditTxnLog=null;
	public byte msgType;
	public byte subType;
	
	
	public byte[] statusCode=null;
	
	//120bytes additional fields
	public byte[] loyaltyCounter = null;
	public byte[] anotherEV = null;
	public byte mifareSettingParameter;
	public byte CPUSettingParameter;
	
	//40bytes additional fields
	public byte[] CTC = null;
	
	public ErrorResponse(int len){
		dataBodyLen = len;
	}
	
	public int getERROR1_LEN() {
		return ERROR1_LEN;
	}


	public int getERROR2_LEN() {
		return ERROR2_LEN;
	}


	
	public int getDataBodyLen() {
		return dataBodyLen;
	}


	public byte getPurseVersionNumber() {
		return purseVersionNumber;
	}


	public byte getPurseUsageControl() {
		return purseUsageControl;
	}


	public byte[] getSingleAutoLoadTxnAmt() {
		return singleAutoLoadTxnAmt;
	}


	public byte[] getPID() {
		return PID;
	}


	public byte getCpuAdminKeyKVN() {
		return cpuAdminKeyKVN;
	}


	public byte[] getSubAreaCode() {
		return subAreaCode;
	}


	public byte[] getPurseExpDate() {
		return purseExpDate;
	}


	public byte[] getPurseBalanceBeforeTxn() {
		return purseBalanceBeforeTxn;
	}


	public byte[] getTxnSNBeforeTxn() {
		return TxnSNBeforeTxn;
	}


	public byte getCardType() {
		return cardType;
	}


	public byte getPersonalProfile() {
		return personalProfile;
	}


	public byte[] getProfileExpDate() {
		return profileExpDate;
	}


	public byte getAreaCode() {
		return areaCode;
	}


	public byte[] getCardPhysicalID() {
		return cardPhysicalID;
	}


	public byte getCardPhysicalIDLength() {
		return cardPhysicalIDLength;
	}


	public byte[] getDeviceID() {
		return deviceID;
	}


	public byte[] getNewDeviceID() {
		return newDeviceID;
	}


	public byte getServiceProviderID() {
		return serviceProviderID;
	}


	public byte[] getNewServiceProviderID() {
		return newServiceProviderID;
	}


	public byte getLocationID() {
		return locationID;
	}


	public byte[] getNewLocationID() {
		return newLocationID;
	}


	public byte[] getDeposit() {
		return deposit;
	}


	public byte getIssuerCode() {
		return issuerCode;
	}


	public byte getBankCode() {
		return bankCode;
	}

	
	public byte[] getLastCreditTxnLog() {
		return LastCreditTxnLog;
	}

	public byte getMsgType() {
		return msgType;
	}

	public byte getSubType() {
		return subType;
	}

	public byte[] getStatusCode() {
		return statusCode;
	}


	//640E, 610F, 6418
	public byte[] getLoyaltyCounter() {
		return loyaltyCounter;
	}


	public byte[] getAnotherEV() {
		return anotherEV;
	}


	public byte getMifareSettingParameter() {
		return mifareSettingParameter;
	}


	public byte getCPUSettingParameter() {
		return CPUSettingParameter;
	}


	//6103
	public byte[] getCTC() {
		return CTC;
	}
	
	@Override
	protected LinkedHashMap<String, Integer> getFields() {
		// TODO Auto-generated method stub
		LinkedHashMap<String, Integer> maps = new LinkedHashMap<String, Integer>();
		
		if(dataBodyLen == ERROR1_LEN) {
			
			maps.put("purseVersionNumber",1);
			maps.put("purseUsageControl",1);
			maps.put("singleAutoLoadTxnAmt",3);
			maps.put("PID",8);
			maps.put("cpuAdminKeyKVN",1);
			
			
			maps.put("subAreaCode",2);
			maps.put("purseExpDate",4);
			maps.put("purseBalanceBeforeTxn",3);
			maps.put("TxnSNBeforeTxn",3);				
			maps.put("cardType",1);
			
			maps.put("personalProfile",1);
			maps.put("profileExpDate",4);
			maps.put("areaCode",1);
			maps.put("cardPhysicalID",7);
			maps.put("cardPhysicalIDLength",1);
			
			
			maps.put("deviceID",4);
			maps.put("newDeviceID",6);
			maps.put("serviceProviderID",1);
			maps.put("newServiceProviderID",3);
			maps.put("locationID",1);
			
			maps.put("newLocationID",2);				
			maps.put("deposit",3);
			maps.put("issuerCode",1);
			maps.put("bankCode",1);
			maps.put("loyaltyCounter",2);
			
			
			maps.put("LastCreditTxnLog",33);
			maps.put("msgType",1);
			maps.put("subType",1);
			maps.put("anotherEV",3);				
			maps.put("mifareSettingParameter",1);
			
			maps.put("CPUSettingParameter",1);
			maps.put("statusCode",2);
			
		} else if(dataBodyLen == ERROR2_LEN) {
			
			maps.put("purseVersionNumber",1);				
			maps.put("PID",8);				
			maps.put("CTC",3);				
			maps.put("cardType",1);
			maps.put("personalProfile",1);
			
			
			maps.put("cardPhysicalID",7);				
			maps.put("cardPhysicalIDLength",1);
			maps.put("deviceID",4);				
			maps.put("newDeviceID",6);
			maps.put("serviceProviderID",1);
			
			maps.put("newServiceProviderID",3);
			maps.put("locationID",1);
			maps.put("newLocationID",2);
			maps.put("issuerCode",1);
			maps.put("statusCode",2);
		} else {
			logger.error("Unknowen dataBody Len:"+dataBodyLen);
			return null;
		}
		
		return maps;
	}

	static public boolean isErrorResponse(int respCode){
		boolean result = false;
		switch(respCode){
			case err640E:
			case err610F:
			case err6418:
			case err6103:
				logger.warn("ErrorCode got it:"+String.format("%04X", respCode));
				result = true;
			default:
				break;
		}
		
		return result;
	}
	
}
