package com.easycard.pc.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



import com.easycard.pc.CMAS.ConfigManager;

public class CmasDB extends BaseSQLite{

	public static final String DB_NAME = "config/CMAS.db";
	public static final int DB_VERSION = 2;
	private Connection cn= null;
	private HostInfo hostInfo = null;
	private DeviceInfo deviceInfo = null;
	private ApiInfo apiInfo = null;
	private TxnInfo txnInfo = null;
	
	@SuppressWarnings("serial")
	public class CmasDBException extends Exception{

		public CmasDBException(String msg){
			 super(msg);
		 }		 
	 }
	
	
	
	public CmasDB() throws SQLException,CmasDBException{
		File dbFile = new File(DB_NAME);
		boolean init = false;
		if(!dbFile.exists()) init = true;//db not exists
		else logger.debug("DB already exists");
		cn = getConnection(DB_NAME);
		
		hostInfo = new HostInfo();
		deviceInfo = new DeviceInfo();
		apiInfo = new ApiInfo();
		txnInfo = new TxnInfo();
		
		
		if(init){
			logger.debug("CMAS DB not exists, create & init it");
			if(initialNeededTable() == false) 
				throw new CmasDBException("Initial DB needed table fail!!!");
		}
		
		//upgrade
		onUpgrade(apiInfo.getNowDBVersion(), DB_VERSION);
	}
	
	public Connection getConnection(){
		return cn;
	}
	
	public boolean initialNeededTable(){
		
		logger.debug("start");
		
		if(hostInfo.createTable(cn) == false) return false;
		if(deviceInfo.createTable(cn) == false) return false;
		if(apiInfo.createTable(cn) == false) return false;
		if(txnInfo.createTable(cn) == false) return false;
		
		logger.debug("end");
		return true;
		
	}
	
	private void onUpgrade(int oldVersion, int newVersion){
	
		logger.debug("Upgrade DB");
		logger.debug("oldVersion:"+oldVersion+",newVersion:"+newVersion);
		if(newVersion > oldVersion){
			
			if(txnInfo.createTable(cn) == false) {
				logger.error("upgrade fail!");
				return;
			}
			
			apiInfo.selectTable(cn, "EasyCardApi");
			
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
	
	
}
