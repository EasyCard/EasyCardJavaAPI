package com.easycard.pc.entry;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.log4j.Logger;









import com.easycard.pc.CMAS.Process;
import com.easycard.pc.CMAS.Process.ProcessException;
import com.easycard.pc.database.CmasDB;
import com.easycard.pc.database.CmasDB.CmasDBException;
import com.easycard.pc.database.DeviceInfo;
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

		case "cmasRequestTest":
			cmasRequestTest();
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
	
	public static void cmasRequestTest(){
		Process process;
		IRespCode result = null;
		try {
			String request = "<TransXML><Trans><T0100>0220</T0100><T0200>2645702268</T0200><T0211>0000000000000000</T0211><T0213>08</T0213><T0214>00</T0214><T0300>811599</T0300><T0400>100</T0400><T0408>701</T0408><T0409>500</T0409><T0410>801</T0410><T1100>001901</T1100><T1101>001901</T1101><T1200>142135</T1200><T1201>142135</T1201><T1300>20150615</T1300><T1301>20150615</T1301><T3700>20150615001901</T3700><T4100>001003922800</T4100><T4101>0430B400</T4101><T4103>354236050349481</T4103><T4104>00001818</T4104><T4107>5512</T4107><T4200>00010386</T4200><T4210>0</T4210><T4800>00</T4800><T4801>007D0000F8BF7E5540F40100860300B4000000000430B400000000000000000000000000000000000000000000</T4801><T4802>02</T4802><T4803>31</T4803><T4804>01</T4804><T4805>0000</T4805><T4808>000128</T4808><T4809>00</T4809><T4810>00</T4810><T4811>000127</T4811><T4812>000000</T4812><T4813>0</T4813><T4826>04</T4826><T5301>01</T5301><T5303>00</T5303><T5304>00</T5304><T5305>00</T5305><T5361>0000000000000000</T5361><T5362>00000000</T5362><T5363>0000000000000000</T5363><T5371>5039303031383330</T5371><T5501>15061599</T5501><T5503>0000010386</T5503><T5504>01</T5504><T5510>0001</T5510><T6403>0105E28A</T6403></Trans><Trans><T0100>0500</T0100><T0300>900000</T0300><T1100>001901</T1100><T1200>142135</T1200><T1300>20150615</T1300><T3700>20150615001901</T3700><T4100>001003922800</T4100><T4101>0430B400</T4101><T4103>354236050349481</T4103><T4104>00001818</T4104><T4107>5512</T4107><T4200>00010386</T4200><T4210>0</T4210><T5501>15061599</T5501><T5503>0000010386</T5503><T5504>01</T5504><T5510>0001</T5510><T5591>1</T5591><T5592><T559201>100</T559201><T559202>0</T559202><T559203>0</T559203></T5592><T6000>270202333100</T6000><T6004>10011</T6004></Trans></TransXML>";
			process = new Process();
			//result = process.doCmasRequestTest(request);
			//logger.info("cmasRequestTest() result:"+result.getId()+":"+result.getMsg()); 
		} catch (ProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void settingDevice() {
		
		HashMap<Integer, String> menu = new HashMap<Integer, String>();
		menu.put(1, "1.Reader Type");
		menu.put(2, "2.CMAS Type");
		
		try {
			CmasDB db = new CmasDB();
			DeviceInfo device = new DeviceInfo();
			
			Scanner scanner = new Scanner(System.in);
			/*
			while(true){
				
				scanner.nextLine()
				
				
			}*/
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
