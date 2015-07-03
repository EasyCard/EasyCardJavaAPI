package com.easycard.pc.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;





import com.easycard.errormessage.ApiRespCode;
import com.easycard.errormessage.IRespCode;
import com.easycard.pc.CMAS.ConfigManager;

public class CmasDB extends BaseSQLite{

	public static final String DB_NAME = "config/CMAS.db";
	public static final int DB_VERSION = 2;
	
	
	private Connection cn= null;
	private HostInfo hostInfo = null;
	private DeviceInfo deviceInfo = null;
	private ApiInfo apiInfo = null;
	private TxnInfo txnInfo = null;
	//private UserDefineTable userDefine = null;
	private boolean init = false;
	private String deviceNickName=null;
	
	@SuppressWarnings("serial")
	public class CmasDBException extends Exception{

		public CmasDBException(String msg){
			 super(msg);
		 }		 
	 }
	
	public boolean initial(){
		boolean result = true;
		
		//userDefine = new UserDefineTable();
		deviceInfo = new DeviceInfo();
		apiInfo = new ApiInfo();
		hostInfo = new HostInfo();		
		txnInfo = new TxnInfo();
		
		
		if(init){
			logger.debug("CMAS DB not exists, create & init it");
			if(createNeededTable() == false) return false;
			
			return true;
		}
		
		
		//upgrade DB
		apiInfo.selectTable(cn);
		onUpgrade(apiInfo.getNowDBVersion(), DB_VERSION);
		
		if(deviceInfo.selectTable(cn, this.getDeviceNickName()) == false) return false;
		hostInfo.selectTable(cn, deviceInfo.getHostType());
		
		//debug
		logger.debug("========== UserDefineInfo ================");
		logger.debug("HostType:"+deviceInfo.getHostType());
		logger.debug("NickName:"+deviceInfo.getNickName());
		logger.debug("ReaderComport:"+deviceInfo.getComport());
		logger.debug("TmAgentNo:"+deviceInfo.getTmAgentNo());
		logger.debug("TmID:"+deviceInfo.getTmID());
		logger.debug("TmLocationID:"+deviceInfo.getTmLocationID());
		logger.debug("UpdateDateTime:"+deviceInfo.getUpdateDateTime());
		logger.debug("BatchNo:"+deviceInfo.getBatchNo());

		logger.debug("NewDeviceID:"+deviceInfo.getNewDeviceID());
		logger.debug("NewLocationID:"+deviceInfo.getNewLocationID());
		logger.debug("ReaderID"+deviceInfo.getReaderID());

		
		
		logger.debug("========== HostInfo ================");
		logger.debug("FtpID:"+hostInfo.getFtpID());
		logger.debug("FtpIP:"+hostInfo.getFtpIP());
		logger.debug("FtpPort:"+hostInfo.getFtpPort());
		logger.debug("FtpPwd:"+hostInfo.getFtpPwd());
		logger.debug("FtpUrl:"+hostInfo.getFtpUrl());
		logger.debug("HostName:"+hostInfo.getHostName());
		logger.debug("HostType:"+hostInfo.getHostType());
		logger.debug("SocketIP:"+hostInfo.getSocketIP());
		logger.debug("SocketPort:"+hostInfo.getSocketPort());
		logger.debug("SocketUrl:"+hostInfo.getSocketUrl());
		
		
		
		
		return result;
	}
	
	public CmasDB() throws SQLException{
		File dbFile = new File(DB_NAME);		
		if(!dbFile.exists()) this.init = true;//db not exists
		else logger.debug("DB already exists");
		cn = getConnection(DB_NAME);
	}
	
	public Connection getConnection(){
		return cn;
	}
	
	private boolean createNeededTable(){
		
		logger.debug("start");
		
		if(deviceInfo.createTable(cn) == false) return false;
		//if(userDefine.createTable(cn) == false) return false;
		if(apiInfo.createTable(cn) == false) return false;
		if(hostInfo.createTable(cn) == false) return false;
				
		if(txnInfo.createTable(cn) == false) return false;
		
		
		logger.debug("end");
		return true;
		
	}
	
	private void onUpgrade(int oldVersion, int newVersion){
	
		logger.debug("Upgrade DB");
		logger.debug("oldVersion:"+oldVersion+",newVersion:"+newVersion);
		if(newVersion > oldVersion){
			
			//update deviceTable
			deviceInfo.setTmSerialNo(1);
			deviceInfo.setBatchNo(1);
			deviceInfo.setNickName("R1");
			deviceInfo.updateRec(cn);
			
			apiInfo.setNowDBVersion(newVersion);
			apiInfo.updateRec(cn);
			
			//update dbVersion
			//apiInfo.setNowDBVersion(DB_VERSION);
			//apiInfo.updateRec(cn);
			//sample code, add a new column into table
			//db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
		}
	}
	
	public void close(){
		try {
			cn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getDeviceNickName(){
		return this.deviceNickName;
	}
	
	public String setDeviceNickName(String name){
		return this.deviceNickName = name;
	}
	
	public ApiInfo getApiInfo(){
		return this.apiInfo;
	}
	
	public HostInfo getHostInfo(){
		return this.hostInfo;
	}
	
	public DeviceInfo getDeviceInfo(){
		
		return this.deviceInfo;
	}
	
	public TxnInfo getTxnInfo(){
		return this.txnInfo;
	}
}
