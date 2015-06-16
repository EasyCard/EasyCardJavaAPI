package com.easycard.reader;



import org.apache.log4j.Logger;

import com.easycard.utilities.Util;

public class PPR_ReadCardNumber extends APDU{

	static Logger logger = Logger.getLogger(PPR_ReadCardNumber.class);
	private final int reqDataLength = 4;
	private final int respDataLength = 9;
	
	//in
	private byte []txnAmt = new byte[3];
	private byte LCDControlFalg;
	
	
	
	public PPR_ReadCardNumber(){
		
		
		
		int scReqInfoLength = reqDataLength + scReqInfoMinLength;
		
		
		
		logger.info("Start");
		Req_NAD = 0;
		Req_PCB = 0; 
		Req_LEN = (byte) scReqInfoLength;
		
		Req_CLA = (byte) 0x80;
		Req_INS = 0x51;			
		Req_P1 = 0x02;
		Req_P2 = 0x02;
			
		Req_Lc = (byte) reqDataLength;
		Req_Le = (byte) respDataLength;
		
		/*
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
		*/
		logger.info("End");
	}
	
	
	
	@Override
	public byte[] GetRequest() {
		// TODO Auto-generated method stub
		
		int reqTotalLength = reqDataLength + scReqMinLength;
		int i=0;
		byte [] request = new byte[reqTotalLength];
		
		logger.info("Start");
		request[0] = Req_NAD;
		request[1] = Req_PCB;
		request[2] = Req_LEN;
		request[3] = Req_CLA;
		request[4] = Req_INS;
		request[5] = Req_P1;
		request[6] = Req_P2;
		request[7] = Req_Lc;
		i+=8;
		//TXN_AMT(3)
		System.arraycopy(this.txnAmt, 0, request, i, txnAmt.length);
		i+=txnAmt.length;
		
		//LCD Control Flag
		request[i] = this.LCDControlFalg;
		
		//Le
		request[reqTotalLength - 2] = Req_Le;
		request[reqTotalLength - 1] = this.getEDC(request, reqTotalLength); // EDC
		
		logger.debug("request:"+Util.hex2StringLog(request));
		logger.info("End");
		return request;
	}

	@Override
	public boolean SetRequestData(byte[] bytes) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int GetReqRespLength() {
		// TODO Auto-generated method stub
		logger.info("getter:"+this.reqDataLength);
		return this.reqDataLength;
	}

	@Override
	public void debugResponseData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] GetRespond() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean SetRespond(byte[] bytes) {
		// TODO Auto-generated method stub
		return false;
	}



	/**
	 * @return the txnAmt
	 */
	public byte [] getTxnAmt() {
		logger.info("getter:"+Util.hex2StringLog(this.txnAmt));
		return txnAmt;
	}



	/**
	 * @param txnAmt the txnAmt to set
	 */
	public boolean setTxnAmt(int amt) {
		if (amt > 0x00FFFFFF) {
			logger.error("amt:"+amt+", too large");
			return false;
		}
		logger.info("setter:"+amt);
		this.txnAmt[2] = (byte) ((amt & 0x00FF0000) >> 16);
		this.txnAmt[1] = (byte) ((amt & 0x0000FF00) >> 8);
		this.txnAmt[0] = (byte) (amt & 0x000000FF);
		
		
		return true;
	}



	/**
	 * @return the lCDControlFalg
	 */
	public byte getLCDControlFalg() {
		logger.info("getter:"+this.LCDControlFalg);
		return LCDControlFalg;
	}



	/**
	 * @param lCDControlFalg the lCDControlFalg to set
	 */
	public void setLCDControlFalg(byte lCDControlFalg) {
		logger.info("setter:"+lCDControlFalg);
		LCDControlFalg = lCDControlFalg;
	}
	

}
