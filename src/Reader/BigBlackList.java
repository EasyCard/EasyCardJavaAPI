package Reader;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;



import org.apache.log4j.Logger;

import Utilities.Util;

public class BigBlackList {

	
	class BlackListException extends Exception {
		private static final long serialVersionUID = 1L;

		public BlackListException(String msg){
			super(msg);
		}
		
	}
	
	static Logger logger = Logger.getLogger(BigBlackList.class);
	//private byte[] fileName = new byte[20];
	static public final int  FILE_NAME_SIZE = 20; 
	static public final int  FILE_HEAD_SIZE = 25;
	static public final int  FILE_END_SIZE = 32;
	static public final int  FILE_BODY_UNIT_SIZE = 9;
	
	
	private byte[] fileHead = null;// = new byte[25];
	private byte[] fileBody = null;
	private byte[] fileEnd = null;//new byte[32];
	
	//private final String FILE_NAME_SIZE_ERROR = "File Name must be 20 bytes";
	//private final String FILE_NAME_BLC_ERROR = "File Name 1~3bytes was not BLC";
	//private final String FILE_NAME_VER_ERROR = "File Name Version 4~8bytes was from 0 to 65535 ";
	//private final String PARSE_FILE_ERROR = "Parse Black List File Error";
	
	public BigBlackList(){}
	/*
	public BigBlackList(String name) throws BlackListException {
	
		try{
			PropertyConfigurator.configure(BigBlackList.class.getResourceAsStream("/config/log4j.properties"));
			
				
			this.checkFileName(name);
								
			//parse Black List File
			if(!this.paraseBlackList(name)) throw new BlackListException(PARSE_FILE_ERROR);
					
		} catch (Exception e) {
			logger.error("EZBlackList exception:"+e.getMessage());
			throw e;
		}
	}
	*/
	
	static boolean checkFileNameOK(String name) {
		
		byte[] b = null;
		byte[] t = null;
		
		logger.info("start");
		//check FileName size
		if(name.length() != FILE_NAME_SIZE){
			logger.error("Big File Size was Wrong:"+name.length());
			return false;			
		}
		b = name.getBytes();
		
		//check BLC
		if(b[0] != (byte)'B' || b[1] != 'L' || b[2] != 'C'){
			logger.error("Black List File 1~3bytes must be BLC");
			return false;			
		}
		//check version
		t = Arrays.copyOfRange(b, 3, 8);
		int version = Integer.valueOf(new String(t));
		if(version > 65535 || version < 0){
			logger.error("Black List Version duration was 0~65535:"+version);
			return false;
		}
		
		logger.info("end");
		return true;
	}
	
	
	
	public boolean configure(InputStream input){
		//openFile
		//外部檔案
		int cnt = 0;
		
		try {
			//logger.debug("file Size:"+input.available());
			if((input.available()-FILE_HEAD_SIZE-FILE_END_SIZE)%FILE_BODY_UNIT_SIZE !=0){
				logger.error("File size something wrong");
				return false;
			}
			cnt = (input.available()-FILE_HEAD_SIZE-FILE_END_SIZE)/FILE_BODY_UNIT_SIZE;
			fileHead = new byte[FILE_HEAD_SIZE];
			fileEnd = new byte[FILE_END_SIZE];
			fileBody = new byte[cnt*FILE_BODY_UNIT_SIZE];
			input.read(fileHead, 0, fileHead.length);
			input.read(fileBody, 0, fileBody.length);
			input.read(fileEnd, 0, fileEnd.length);
			
			/*
			String log="";
			for(int i=0; i<fileEnd.length; i++){
				log += String.format("(%02x)", fileEnd[i]);
			}
			logger.debug("FileEnd:"+log);
			*/
			if(getBlaclListCnt() != cnt){
				logger.error("File Size and File_End_Cnt not the same");
				return false;
			}			
			input.close();
		} catch (IOException e) {
				// TODO Auto-generated catch block
			
			e.printStackTrace();
			return false;
		}
			
		return true;		
	}
	
	public int getBlaclListCnt(){
		
		if(fileEnd == null){
			logger.error("Please run configure() first "); 
			return 0;
		}
		int cnt = 0;
		try {
			byte[] b = Arrays.copyOfRange(fileEnd, 21, 29);
			
			cnt = Integer.valueOf(new String(b));
			logger.info("getBlaclListCnt:"+cnt);
		} catch(Exception e) {
			
			logger.error("getBlaclListCnt exception:"+e.getMessage());
		}
		
		return cnt;
	}
	
	static public String getFileNameVersion(String name){
		
		if(name.length() != FILE_NAME_SIZE){
			logger.error("File name size error:"+name.length());
			return null;
		}
		String version = null;
		byte[] fileName = name.getBytes();
		byte[] b = null;
		try{
			b = Arrays.copyOfRange(fileName, 3, 8);
			version = new String(b);
			logger.info("getter:"+version);
		} catch(Exception e) {
			logger.error("getVersion exception:" + e.getMessage());
		}
		return version;
	}
	
	static String getFileNameDate(String name){
		
		if(name.length() != FILE_NAME_SIZE){
			logger.error("File name size error:"+name.length());
			return null;
		}		
		String date = null;
		byte[] fileName = name.getBytes();
		byte[] b = null;
		try{
			b = Arrays.copyOfRange(fileName, 10, 16);
			date = new String(b);
			logger.info("getter:"+date);
		} catch(Exception e) {
			logger.error("getDate exception:" + e.getMessage());
		}
		return date;
	}
	
	public String getFileHeadDate(){
		if(fileHead == null){
			logger.error("Please run configure() first "); 
			return null;
		}
		
		String date = null;
		byte[] b = null;
		try{
			b = Arrays.copyOfRange(fileHead, 4, 12);
			date = new String(b);
			logger.info("getter:"+date);
		} catch(Exception e) {
			logger.error("getDate exception:" + e.getMessage());
		}
		return date;
	}
	
	public boolean checkCardID(byte[] cardIDNeededReverseFirst){
		
		boolean result = false;
		try{
			
			long l = Util.bytes2Long(cardIDNeededReverseFirst, 0, cardIDNeededReverseFirst.length, true);
			logger.debug("cardID long value:"+l);
			result = this.checkCardID(l);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("checkCardID exception:"+e.getMessage());
		}
		
		return result;
	}
	
	public boolean checkCardID(long cardid){
		
		if(fileBody == null){
			logger.error("Please run configure() first "); 
			return false;
		}
		
		boolean result = false;
		int cnt=this.getBlaclListCnt();
		int i = 0;
		int M = cnt-1;
		int m = 0;
		while(m<=M){
			
			i = (M+m)/2;
			//logger.debug("i:"+i+", M:"+M+", m:"+m);
			byte[] d = Arrays.copyOfRange(this.fileBody, (i)*FILE_BODY_UNIT_SIZE, (i+1)*FILE_BODY_UNIT_SIZE);
			//System.out.println(String.format("(%02X)(%02X)(%02X)(%02X)(%02X)(%02X)(%02X)(%02X)", d[0], d[1], d[2], d[3], d[4], d[5], d[6], d[7]));
			
			long l = Util.bytes2Long(d, 0, 8, true);
			//System.out.println("l:"+l+",cardID:"+id);
			if(cardid > l){
				m = i + 1;
			} else if(cardid < l) {
				M = i - 1;
			} else {
				logger.info("BlackList CardID got it, index:"+i+",value:"+l); 
				result = true;
				break;
			}
		}
		return result;
	}
	
}
