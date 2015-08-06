package com.easycard.pc.CMAS;

import java.sql.Connection;
import java.util.List;

import org.apache.log4j.Logger;

import com.easycard.pc.communication.socket.SSL;
import com.easycard.pc.database.BatchDetail;

public class SendAdvice extends Thread{

	private boolean isRunning = true;
	private List<BatchDetail.DBFields> adviceSet = null;
	private SSL ssl = null;
	private Connection con = null;
	static Logger logger = Logger.getLogger(SendAdvice.class);
	
	public SendAdvice(List<BatchDetail.DBFields> adviceSet, SSL ssl, Connection con){
		this.adviceSet = adviceSet;
		this.ssl = ssl;
		this.con = con;
	}
	
	public void stopSendAdvice(){
		this.isRunning = false;
		ssl.disconnect();
	}
	
	private boolean getIsRunning(){
		return this.isRunning;
	}
	
	public static void immediatelySend(SSL ssl, BatchDetail bd, Connection con, String advice){
		String cmasResponse = null;
		if((cmasResponse = ssl.sendRequest(advice)) != null){
			CmasDataSpec spec = new CmasDataSpec(cmasResponse);
			if(spec.getT3900().equalsIgnoreCase("00") == false){
				logger.error("WTF... why advice response t3900("+spec.getT3900()+") was not 00");
			}
			//BatchDetail bd = configManager.getBatchDetail();
			bd.setAdviceResp(cmasResponse);
			bd.updateRec(con);
		}
	}
	
	public void run() {
		
		
		String resp = null;
		BatchDetail.DBFields fields = null;
		BatchDetail bd = new BatchDetail();
		
		
			
		try {
			ssl.join();
			
				
			for(int i=0; i<adviceSet.size(); i++){
				
						
				fields = adviceSet.get(i);												
				resp = ssl.sendRequest(fields.adviceReq);
				CmasDataSpec spec = new CmasDataSpec(resp);
				if(spec.getT3900().equalsIgnoreCase("00") == false){
					logger.error("WTF... why advice response t3900("+spec.getT3900()+") was not 00");
				}
				
				logger.debug("sendAdvice Req:"+fields.adviceReq);
				logger.debug("sendAdvice Resp:"+resp);
				bd.setNewDeviceIDMixBatchNo(fields.newDeviceIDMixBatchNo);
				bd.setRRN(fields.rrn);
				bd.setAdviceResp(resp);			
				bd.updateRec(con);
					
				if(getIsRunning()==false) {
					logger.debug("main process canceled to send advice");
					break;
				}
			
			}
		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.isRunning = false;
				logger.error("SSL join fail");				
		}	
		
	}
}
