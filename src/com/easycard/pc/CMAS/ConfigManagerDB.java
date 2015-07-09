package com.easycard.pc.CMAS;

import java.sql.SQLException;

import org.apache.log4j.Logger;



import com.easycard.pc.database.CmasDB;
import com.easycard.pc.database.CmasDB.CmasDBException;

public class ConfigManagerDB implements IConfigManager{
	static Logger logger = Logger.getLogger(ConfigManagerDB.class);
	CmasDB db = null;
	private String deviceNickName=null;
	public ConfigManagerDB(String name){
		deviceNickName = name;
	}
	
	public ConfigManagerDB(){}
	
	
	@Override
	public boolean initial() {
		// TODO Auto-generated method stub
		boolean result = true;
		logger.debug("start");
		try {
			db = new CmasDB();
			db.setDeviceNickName(deviceNickName);
			result = db.initial();
		} catch (CmasDBException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		logger.debug("end");
		return result;
	}

	@Override
	public boolean finish() {
		// TODO Auto-generated method stub
		db.getApiInfo().updateRec(db.getConnection());
		db.getDeviceInfo().updateRec(db.getConnection());
		//db.get.updateRec(db.getConnection());
		
		
		db.close();
		return true;
	}

	@Override
	public String getApiName() {
		// TODO Auto-generated method stub
		return db.getApiInfo().getApiName();
	}

	@Override
	public void setApiName(String apiName) {
		// TODO Auto-generated method stub
		db.getApiInfo().setApiName(apiName);
	}

	@Override
	public String getApiVersion() {
		// TODO Auto-generated method stub
		
		return db.getApiInfo().getApiVer();
	}

	@Override
	public void setApiVersion(String verName) {
		// TODO Auto-generated method stub
		db.getApiInfo().setApiVer(verName);
	}

	@Override
	public String getBlackListVersion() {
		// TODO Auto-generated method stub
		return db.getApiInfo().getBlackListVer();
	}

	@Override
	public void setBlackListVersion(String verName) {
		// TODO Auto-generated method stub
		db.getApiInfo().setBlackListVer(verName);
	}

	@Override
	public String getNewLocationID() {
		// TODO Auto-generated method stub	
		logger.debug("getter:"+db.getDeviceInfo().getNewLocationID());
		return db.getDeviceInfo().getNewLocationID();
	}

	@Override
	public void setNewLocationID(String id) {
		// TODO Auto-generated method stub
		db.getDeviceInfo().setNewLocationID(id);
	}

	@Override
	public String getReaderID() {
		// TODO Auto-generated method stub
		return db.getDeviceInfo().getReaderID();
	}

	@Override
	public void setReaderID(String id) {
		// TODO Auto-generated method stub
		db.getDeviceInfo().setReaderID(id);
	}

	@Override
	public String getHostUrl() {
		// TODO Auto-generated method stub
		return db.getHostInfo().getSocketUrl();
	}

	@Override
	public void setHostUrl(String url) {
		// TODO Auto-generated method stub
		db.getHostInfo().setSocketUrl(url);
	}

	@Override
	public String getHostIP() {
		// TODO Auto-generated method stub
		return db.getHostInfo().getSocketIP();
	}

	@Override
	public void setHostIP(String ip) {
		// TODO Auto-generated method stub
		db.getHostInfo().setSocketIP(ip);
	}

	@Override
	public String getHostPort() {
		// TODO Auto-generated method stub
		return String.format("%d", db.getHostInfo().getSocketPort());
	}

	@Override
	public void setHostPort(String port) {
		// TODO Auto-generated method stub
		db.getHostInfo().setSocketPort(Integer.valueOf(port));
	}

	@Override
	public String getFtpUrl() {
		// TODO Auto-generated method stub
		return db.getHostInfo().getFtpUrl();
	}

	@Override
	public void setFtpUrl(String url) {
		// TODO Auto-generated method stub
		db.getHostInfo().setFtpUrl(url);
	}

	@Override
	public String getFtpIP() {
		// TODO Auto-generated method stub
		return db.getHostInfo().getFtpIP();
	}

	@Override
	public void setFtpIP(String ip) {
		// TODO Auto-generated method stub
		db.getHostInfo().setFtpIP(ip);
	}

	@Override
	public String getFtpLoginID() {
		// TODO Auto-generated method stub
		return db.getHostInfo().getFtpID();
	}

	@Override
	public void setFtpLoginID(String id) {
		// TODO Auto-generated method stub
		db.getHostInfo().setFtpID(id);
	}

	@Override
	public String getFtpLoginPwd() {
		// TODO Auto-generated method stub
		return db.getHostInfo().getFtpPwd();
	}

	@Override
	public void setFtpLoginPwd(String pwd) {
		// TODO Auto-generated method stub
		db.getHostInfo().setFtpPwd(pwd);
	}

	@Override
	public String getTMSerialNo() {
		// TODO Auto-generated method stub
		return String.format("%d", db.getDeviceInfo().getTmSerialNo());
	}

	@Override
	public void setTMSerialNo(String no) {
		// TODO Auto-generated method stub
		db.getDeviceInfo().setTmSerialNo(Integer.valueOf(no));
	}

	@Override
	public String getBatchNo() {
		// TODO Auto-generated method stub
		return String.format("%d", db.getDeviceInfo().getBatchNo());
	}

	@Override
	public void setBatchNo(String no) {
		// TODO Auto-generated method stub
		db.getDeviceInfo().setBatchNo(Integer.valueOf(no));
	}

	

	@Override
	public String getReaderPort() {
		// TODO Auto-generated method stub
		
		return db.getDeviceInfo().getComport();
	}

	@Override
	public void setReaderPort(String port) {
		// TODO Auto-generated method stub
		db.getDeviceInfo().setComport(port);;
	}

	@Override
	public String getTMLocationID() {
		// TODO Auto-generated method stub
		return db.getDeviceInfo().getTmLocationID();
	}

	@Override
	public void setTMLocationID(String id) {
		// TODO Auto-generated method stub
		db.getDeviceInfo().setTmLocationID(id);
	}

	@Override
	public String getTMID() {
		// TODO Auto-generated method stub
		return db.getDeviceInfo().getTmID();
	}

	@Override
	public void setTMID(String id) {
		// TODO Auto-generated method stub
		db.getDeviceInfo().setTmID(id);
	}

	@Override
	public String getTMAgentNo() {
		// TODO Auto-generated method stub
		return db.getDeviceInfo().getTmAgentNo();
	}

	@Override
	public void setTMAgentNo(String no) {
		// TODO Auto-generated method stub
		db.getDeviceInfo().setTmAgentNo(no);
	}

	@Override
	public String getHostEnvironment() {
		// TODO Auto-generated method stub
		return String.format("%d", db.getHostInfo().getHostType());
	}

	@Override
	public void setHostEnvironment(String e) {
		// TODO Auto-generated method stub
		db.getHostInfo().setHostType(Integer.valueOf(e));
	}

	@Override
	public String getNewDeviceID() {
		// TODO Auto-generated method stub
		return db.getDeviceInfo().getNewDeviceID();
	}

	@Override
	public void setNewDeviceID(String id) {
		// TODO Auto-generated method stub		
		db.getDeviceInfo().setNewDeviceID(id);
	}

}
