package com.easycard.reader;
//import EasycardAPI;



import org.apache.log4j.Logger;

import com.easycard.errormessage.IRespCode;
import com.easycard.errormessage.ApiRespCode;
import com.easycard.reader.ReaderRespCode;




public class EZReader{
	
	static Logger logger = Logger.getLogger(EZReader.class);

    private IRecvSender recvSender=null;
    
    
 	
    public EZReader(IRecvSender rs){
    	logger.info("Start");
    	
    	recvSender = rs;
    	logger.info("End");
    }
    
    public IRespCode exeCommand(APDU command)
    {
    	logger.info("Start");
    	
    	IRespCode result;
    	int statusCode;
    	
    	byte[] sendBuffer;
    	byte[] recvBuffer = null;
    	
    	try{    	
    		sendBuffer = command.GetRequest();    	
    		
    		
    		recvBuffer = recvSender.sendRecv(sendBuffer);
    		if(recvBuffer==null) {
    			logger.error("recvBuffer is NULL");
    			return ApiRespCode.READER_NO_RESPONSE;
    		}
    		
    		if(command.SetRespond(recvBuffer)==false)
    		{
    			logger.error("command: "+command.getClass().getName()+",setRespond fail");
    			return ApiRespCode.ERROR;
    		}
    		
    		
    		
    		if((statusCode = command.GetRespCode()) != 0x9000)
    		{
    			String hexCode = String.format("%04X", statusCode);
    			logger.error("APDU respCode:"+hexCode);
    			result = ApiRespCode.fromCode(hexCode, ReaderRespCode.values());
    		}    		
    		else {
    			//for debug
        		command.debugResponseData();
    			result = ReaderRespCode._9000;
    		}
    		
    		
    	}
    	catch(Exception e)
    	{
    		logger.error("Exception: "+ e.getMessage());
    		return ReaderRespCode.UNKNOWN_ERROR_CODE;
    	}
    	
		return result;    	
    }
    
    //執行reader finish 一些動作。包括：close Port...等
    public boolean finish(){
    	logger.info("start");
    	return recvSender.finish();    
    }
    
    //確認是否要進入reTry機制
    static public boolean reTryEnabled(IRespCode code){
    	
    	if(code == ReaderRespCode._6088 || 
    		code == ReaderRespCode._9970 || 
    		code == ReaderRespCode._9969 || 
    		code == ReaderRespCode._9968){
    		return true;
    	}
    	return false;
    }
}












