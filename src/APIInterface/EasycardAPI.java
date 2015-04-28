package APIInterface;

import org.apache.log4j.Logger;


import CMAS.Process;
import ErrorMessage.IRespCode;





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
		
	    cmasSignOn(); 
	    //logger.info("End");
	}

/*
	private static boolean apiInit(){
		
		try{
						
			//PropertyConfigurator.configure("lib//log4j.properties");
			
		}catch(Exception e){
			
			logger.debug("init fail:"+e.getMessage());
			return false;
		}
		
		return true;
	}
	*/

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
	

	public static void readCardNumber(){	
		try{
		
			logger.info("Start");		
			//Process process = new Process(rs);
			logger.info("End");
		
	
		}catch(Exception e){
			logger.error(e.getMessage());
		}
	}
}
