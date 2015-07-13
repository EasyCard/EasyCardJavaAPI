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
	public static final int DB_VERSION = 6;
	
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
			this.exeSQLCommand("ALTER TABLE batch_detail ADD COLUMN msgType TEXT;");
			
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
	
	

	
}
