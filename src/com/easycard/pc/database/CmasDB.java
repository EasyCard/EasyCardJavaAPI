package com.easycard.pc.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;









import java.sql.Statement;

import com.easycard.errormessage.ApiRespCode;
import com.easycard.errormessage.IRespCode;
import com.easycard.pc.CMAS.ConfigManager;
import com.easycard.pc.CMAS.IConfigManager;
import com.easycard.utilities.Util;

public class CmasDB extends BaseSQLite implements IConfigManager{

	public static final String DB_NAME = "config/CMAS.db";
	public static final int DB_VERSION = 15;
	
	//private boolean init = false;
	private String deviceNickName=null;
	private Connection cn= null;
	
	private HostInfo hostInfo = null;
	private DeviceInfo deviceInfo = null;
	private ApiInfo apiInfo = null;
	private TxnBatch txnBatch = null;
	private BatchDetail batchDetail = null;
	
	public class CmasDBException extends Exception {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public CmasDBException(String message) {
	        super(message);
	    }
	}
	
	public CmasDB(String deviceNickName) throws SQLException,CmasDBException{
		this.deviceNickName = deviceNickName;
		getDBConnection();
	}
	
	public CmasDB() throws SQLException,CmasDBException{
		getDBConnection();
	}
	
	private void getDBConnection() throws SQLException,CmasDBException{
		File dbFile = new File(DB_NAME);		
		if(!dbFile.exists()) {
			logger.debug("DB not exists");
			throw new CmasDBException("DB not exists");
			//this.init = true;//db not exists
		}
		
		cn = getConnection(DB_NAME);
	}
	
	
	
	public boolean initial(){
		boolean result = true;
		
		
		deviceInfo = new DeviceInfo();
		apiInfo = new ApiInfo();
		hostInfo = new HostInfo();		
		txnBatch = new TxnBatch();
		batchDetail = new BatchDetail();
		
		
		
		
		//select api_info table
		apiInfo.selectTable(cn);
		//upgrade DB
		onUpgrade(apiInfo.getNowDBVersion(), DB_VERSION);
		
		if(deviceInfo.selectTable(cn, this.getDeviceNickName()) == false) {
			logger.debug("DB device_info table selected fail by nickName:"+this.getDeviceNickName());
			return false;
		}
		//select host_info table
		hostInfo.selectTable(cn, deviceInfo.getHostType());
		
		if(deviceInfo.getNewDeviceID() != null){
			this.selectTxnBatchRec();
			/*String txnBatchKey = deviceInfo.getNewDeviceID()+deviceInfo.getBatchNo();
			logger.debug("txnBatchKey:"+txnBatchKey);
			if(txnBatch.selectTable(cn, txnBatchKey)==false){
				txnBatch.initDefault();
				txnBatch.setNewDeviceIDMixBatchNo(txnBatchKey);
				txnBatch.insertRec(cn);
			}*/
		} else logger.debug("newDeviceID is NULL, not to select txn_batch table");
		
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
	
	public void selectTxnBatchRec(){
		
		String txnBatchKey = deviceInfo.getNewDeviceID()+deviceInfo.getBatchNo();
		logger.debug("select txn_batch by txnBatchKey:"+txnBatchKey);
		if(txnBatch.selectTable(cn, txnBatchKey)==false){
			logger.debug("txnb_batch record not existed, insert one new record by key:"+txnBatchKey);
			txnBatch.initDefault();
			txnBatch.setNewDeviceIDMixBatchNo(txnBatchKey);
			txnBatch.insertRec(cn);
		}
	}
	
	
	public Connection getConnection(){
		return cn;
	}
	
	
	private void exeSQLCommand(String sql){
		
	    Statement stat = null;
	    try {
			stat = cn.createStatement();
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("SQLException:"+e.getMessage());
		}
	     
	}
	
	private void onUpgrade(int oldVersion, int newVersion){
	
		
		logger.debug("oldVersion:"+oldVersion+",newVersion:"+newVersion);
		if(newVersion > oldVersion){
			logger.debug("Upgrade DB now");
			
			
			//this.exeSQLCommand(String.format("UPDATE api_info set blackListType='BIG';"));
			//this.exeSQLCommand(String.format("ALTER TABLE batch_detail ADD COLUMN t3900 TEXT DEFAULT null;"));
			
			this.exeSQLCommand(String.format("UPDATE batch_detail SET t3900='00' WHERE adviceResp != 'NULL'"));
			
			//this.exeSQLCommand(String.format("ALTER TABLE device_info ADD COLUMN apiParaVer TEXT DEFAULT %s;","0000"));
			//this.exeSQLCommand(String.format("ALTER TABLE device_info ADD COLUMN txnSwitch TEXT DEFAULT %s;","000000000"));
			
			//update dbVersion
			apiInfo.setNowDBVersion(newVersion);
			apiInfo.updateRec(cn);
			
			
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
	
	public TxnBatch getTxnBatch(){
		
		return this.txnBatch;
	}

	public BatchDetail getBatchDetail(){
		return this.batchDetail;
	}
	

	@Override
	public boolean finish() {
		// TODO Auto-generated method stub
		if(deviceInfo.getTbUpdated()) deviceInfo.updateRec(cn);
		if(apiInfo.getTbUpdated()) apiInfo.updateRec(cn);
		if(hostInfo.getTbUpdated()) hostInfo.updateRec(cn);
		if(txnBatch.getTbUpdated()) txnBatch.updateRec(cn);
		
		return true;
	}

	@Override
	public String getApiName() {
		// TODO Auto-generated method stub
		return this.apiInfo.getApiName();
	}

	@Override
	public void setApiName(String apiName) {
		// TODO Auto-generated method stub
		this.apiInfo.setApiName(apiName);
	}

	@Override
	public String getApiVersion() {
		// TODO Auto-generated method stub
		return this.apiInfo.getApiVer();
	}

	@Override
	public void setApiVersion(String verName) {
		// TODO Auto-generated method stub
		this.apiInfo.setApiVer(verName);
	}

	@Override
	public String getBlackListVersion() {
		// TODO Auto-generated method stub
		return this.apiInfo.getBlackListVer();
	}

	@Override
	public void setBlackListVersion(String verName) {
		// TODO Auto-generated method stub
		this.apiInfo.setBlackListVer(verName);
	}

	@Override
	public String getNewLocationID() {
		// TODO Auto-generated method stub
		return this.deviceInfo.getNewLocationID();
	}

	@Override
	public void setNewLocationID(String id) {
		// TODO Auto-generated method stub
		this.deviceInfo.setNewLocationID(id);
	}

	@Override
	public String getReaderID() {
		// TODO Auto-generated method stub
		return this.deviceInfo.getReaderID();
	}

	@Override
	public void setReaderID(String id) {
		// TODO Auto-generated method stub
		this.deviceInfo.setReaderID(id);
	}

	@Override
	public String getHostUrl() {
		// TODO Auto-generated method stub
		return this.hostInfo.getSocketUrl();
	}

	@Override
	public void setHostUrl(String url) {
		// TODO Auto-generated method stub
		this.hostInfo.setSocketUrl(url);
	}

	@Override
	public String getHostIP() {
		// TODO Auto-generated method stub
		return this.hostInfo.getSocketIP();
	}

	@Override
	public void setHostIP(String ip) {
		// TODO Auto-generated method stub
		this.hostInfo.setSocketIP(ip);
	}

	@Override
	public String getHostPort() {
		// TODO Auto-generated method stub
		return String.format("%d", this.hostInfo.getSocketPort());
	}

	@Override
	public void setHostPort(String port) {
		// TODO Auto-generated method stub
		this.hostInfo.setSocketPort(Integer.valueOf(port));
	}

	@Override
	public String getFtpUrl() {
		// TODO Auto-generated method stub
		return this.hostInfo.getFtpUrl();
	}

	@Override
	public void setFtpUrl(String url) {
		// TODO Auto-generated method stub
		this.hostInfo.setFtpUrl(url);
	}

	@Override
	public String getFtpIP() {
		// TODO Auto-generated method stub
		return this.hostInfo.getFtpIP();
	}

	@Override
	public void setFtpIP(String ip) {
		// TODO Auto-generated method stub
		this.hostInfo.setFtpIP(ip);
	}
	
	@Override
	public int getFtpPort() {
		// TODO Auto-generated method stub
		return this.hostInfo.getFtpPort();
	}

	@Override
	public void setFtpPort(int port) {
		// TODO Auto-generated method stub
		this.hostInfo.setFtpPort(port);
	}

	@Override
	public String getFtpLoginID() {
		// TODO Auto-generated method stub
		return this.hostInfo.getFtpID();
	}

	@Override
	public void setFtpLoginID(String id) {
		// TODO Auto-generated method stub
		this.hostInfo.setFtpID(id);
	}

	@Override
	public String getFtpLoginPwd() {
		// TODO Auto-generated method stub
		return this.hostInfo.getFtpPwd();
	}

	@Override
	public void setFtpLoginPwd(String pwd) {
		// TODO Auto-generated method stub
		this.hostInfo.setFtpPwd(pwd);
	}

	@Override
	public String getTMSerialNo() {
		// TODO Auto-generated method stub
		return String.format("%d", this.deviceInfo.getTmSerialNo());
	}

	@Override
	public void setTMSerialNo(String no) {
		//api send SN = 100
		//host response SN=101
		//api update SN=101, sned again
		//host response SN=101
		//txn finish, SN++, so SN=102
		// TODO Auto-generated method stub
		this.deviceInfo.setTmSerialNo(Integer.valueOf(no));
	}

	@Override
	public String getBatchNo() {
		// TODO Auto-generated method stub
		if(this.deviceInfo.getBatchNo()==ICmasTable.intDefaultValue){
			//init dafault value			
			String date = Util.sGetDateShort();
			setBatchNo(date + "01");
			logger.debug("initial BatchNo to :"+date + "01");
		}
		return String.format("%d", this.deviceInfo.getBatchNo());
	}

	@Override
	public void setBatchNo(String no) {
		// TODO Auto-generated method stub
		this.deviceInfo.setBatchNo(Integer.valueOf(no));
	}

	

	@Override
	public String getReaderPort() {
		// TODO Auto-generated method stub
		
		return this.deviceInfo.getComport();
	}

	@Override
	public void setReaderPort(String port) {
		// TODO Auto-generated method stub
		this.deviceInfo.setComport(port);;
	}

	@Override
	public String getTMLocationID() {
		// TODO Auto-generated method stub
		return this.deviceInfo.getTmLocationID();
	}

	@Override
	public void setTMLocationID(String id) {
		// TODO Auto-generated method stub
		this.deviceInfo.setTmLocationID(id);
	}

	@Override
	public String getTMID() {
		// TODO Auto-generated method stub
		return this.deviceInfo.getTmID();
	}

	@Override
	public void setTMID(String id) {
		// TODO Auto-generated method stub
		this.deviceInfo.setTmID(id);
	}

	@Override
	public String getTMAgentNo() {
		// TODO Auto-generated method stub
		return this.deviceInfo.getTmAgentNo();
	}

	@Override
	public void setTMAgentNo(String no) {
		// TODO Auto-generated method stub
		this.deviceInfo.setTmAgentNo(no);
	}

	@Override
	public String getHostEnvironment() {
		// TODO Auto-generated method stub
		return String.format("%d", this.hostInfo.getHostType());
	}

	@Override
	public void setHostEnvironment(String e) {
		// TODO Auto-generated method stub
		this.hostInfo.setHostType(Integer.valueOf(e));
	}

	@Override
	public String getNewDeviceID() {
		// TODO Auto-generated method stub
		return this.deviceInfo.getNewDeviceID();
	}

	@Override
	public void setNewDeviceID(String id) {
		// TODO Auto-generated method stub		
		this.deviceInfo.setNewDeviceID(id);
	}

	@Override
	public String getApiParaVer() {
		// TODO Auto-generated method stub
		return this.deviceInfo.getApiParaVer();
	}

	@Override
	public void setApiParaVer(String ver) {
		// TODO Auto-generated method stub
		this.deviceInfo.setApiParaVer(ver);
	}

	@Override
	public int getAdviceLimit() {
		// TODO Auto-generated method stub
		return this.deviceInfo.getAdviceLimit();
	}

	@Override
	public void setAdviceLimit(int limit) {
		// TODO Auto-generated method stub
		this.deviceInfo.setAdviceLimit(limit);
	}

	@Override
	public int getAdviceLimitLock() {
		// TODO Auto-generated method stub
		return this.deviceInfo.getAdviceLimitLock();
	}

	@Override
	public void setAdviceLimitLock(int limit) {
		// TODO Auto-generated method stub
		this.deviceInfo.setAdviceLimitLock(limit);
	}

	
	public boolean getCashAddON() {
		// TODO Auto-generated method stub
		byte []b = this.deviceInfo.getTxnSwitch().getBytes();
		if(b.length < 1) {
			logger.error("getCashAddON len <1");
			return false;
		}
		return (b[0]=='1')?true:false;
	}
	
	public boolean getAutoloadON() {
		byte []b = this.deviceInfo.getTxnSwitch().getBytes();
		if(b.length < 2) {
			logger.error("getAutoloadON len <2");
			return false;
		}
		return (b[1]=='1')?true:false;
	}

	
	public boolean getBalanceTransformON() {
		// TODO Auto-generated method stub
		byte []b = this.deviceInfo.getTxnSwitch().getBytes();
		if(b.length < 3) {
			logger.error("getBalanceTransformON len <3");
			return false;
		}
		return (b[2]=='1')?true:false;
	}

	

	
	public boolean getDeductON() {
		// TODO Auto-generated method stub
		byte []b = this.deviceInfo.getTxnSwitch().getBytes();
		if(b.length < 4) {
			logger.error("getDeductON len <4");
			return false;
		}
		return (b[3]=='1')?true:false;
	}

	public boolean getBalanceTransformDeductON() {
		// TODO Auto-generated method stub
		byte []b = this.deviceInfo.getTxnSwitch().getBytes();
		if(b.length < 5) {
			logger.error("getDeductON len <5");
			return false;
		}
		return (b[4]=='1')?true:false;
	}

	

	
	public boolean getVoidON() {
		// TODO Auto-generated method stub
		byte []b = this.deviceInfo.getTxnSwitch().getBytes();
		if(b.length < 6) {
			logger.error("getDeductON len <6");
			return false;
		}
		return (b[5]=='1')?true:false;
	}

	
	
	public boolean getRefundON() {
		// TODO Auto-generated method stub
		byte []b = this.deviceInfo.getTxnSwitch().getBytes();
		if(b.length < 7) {
			logger.error("getDeductON len <7");
			return false;
		}
		return (b[6]=='1')?true:false;
	}

	

	
	public boolean getExtensionON() {
		// TODO Auto-generated method stub
		byte []b = this.deviceInfo.getTxnSwitch().getBytes();
		if(b.length < 8) {
			logger.error("getDeductON len <8");
			return false;
		}
		return (b[7]=='1')?true:false;
	}

	
	

	
	public boolean getAutoloadenableON() {
		// TODO Auto-generated method stub
		byte []b = this.deviceInfo.getTxnSwitch().getBytes();
		if(b.length < 9) {
			logger.error("getDeductON len <9");
			return false;
		}
		return (b[8]=='1')?true:false;
	}

	

	@Override
	public String getTxnSwitch() {
		// TODO Auto-generated method stub
		return this.deviceInfo.getTxnSwitch();
	}

	@Override
	public void setTxnSwitch(String s) {
		// TODO Auto-generated method stub
		this.deviceInfo.setTxnSwitch(s);
	}

	@Override
	public int getCashAddUnit() {
		// TODO Auto-generated method stub
		return this.deviceInfo.getCashAddUnit();
	}

	@Override
	public void setCashAddUnit(int unit) {
		// TODO Auto-generated method stub
		this.deviceInfo.setCashAddUnit(unit);
	}

	@Override
	public int getAutoloadAmtLimit() {
		// TODO Auto-generated method stub
		return this.deviceInfo.getAutoloadAmtLimit();
	}

	@Override
	public void setAutoloadAmtLimit(int limit) {
		// TODO Auto-generated method stub
		this.deviceInfo.setAutoloadAmtLimit(limit);
	}

	@Override
	public String getBlackListType() {
		// TODO Auto-generated method stub
		return this.apiInfo.getBlackListType();
	}

	@Override
	public void setBlackListType(String type) {
		// TODO Auto-generated method stub
		this.apiInfo.setBlackListType(type);
	}	
}
