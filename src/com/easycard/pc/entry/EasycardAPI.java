package com.easycard.pc.entry;

import org.apache.log4j.Logger;




import com.easycard.pc.CMAS.Process;
import com.easycard.pc.CMAS.Process.ProcessException;
import com.easycard.errormessage.IRespCode;





public class EasycardAPI {
	static Logger logger = Logger.getLogger(EasycardAPI.class);
	
	
	
	
	public EasycardAPI() //throws FileNotFoundException, IOException
	{
		//logger.info("Start");
		//apiInit();
		//logger.info("End");
	}

	public static void main(String args[]) {	
		//apiInit();
		//logger.info("********** App Start **********");	
		String cmd = args[0];
		
		switch(cmd){
		
		case "signon":
			cmasSignOn();
			break;
			
		case "signonQuery":
			signQuery();
			break;
			
		case "deduct":
			int amt = Integer.valueOf(args[1]);
			boolean autoload = args[2].equalsIgnoreCase("1")?true:false;
			deduct(amt, autoload);
			break;
			
		case "readCardBasicData":
			readCardBasicData();
			break;
		
		case "autoload":
			autoload(Integer.valueOf(args[1]));
			break;

		default:
			System.out.println("Unknoewn command:"+cmd);
			break;
			
		}
	    /*
	     * 1. signon ready*/
	     //
	     
	     
	     
	    //logger.info("End");
	}



	public static void cmasSignOn()
	{	
		try{
		
			//logger.info("Start");
			
			IRespCode result;
			Process process = new Process();
			result = process.doSignon();
			
			logger.info("Do_Signon() result:"+result.getId()+":"+result.getMsg()); 
			
			logger.info("End");
		}catch(Exception e){
			e.getStackTrace();		
			logger.error(e.getMessage());	
		}
	}
	

	public static void signQuery(){	
		try{
		
			logger.info("Start");		
			Process process = new Process();
			process.doSignOnQuery();
			logger.info("End");
		
	
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}
	
	public static void deduct(int amt, boolean autoload){	
		try{
		
			logger.info("Start");		
			Process process = new Process();
			IRespCode result = process.doDeduct(amt, autoload);
			logger.info("Txn Result:"+result.getId()+"_"+result.getMsg());
			
			logger.info("End");
		
	
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}

	public static void readCardBasicData(){
		
		Process process;
		try {
			process = new Process();
			process.doReadCardBasicData();
		} catch (ProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void autoload(int amt){
		Process process;
		IRespCode result = null;
		try {
			process = new Process();
			result = process.doAutoload(amt);
			logger.info("autoload() result:"+result.getId()+":"+result.getMsg()); 
		} catch (ProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
