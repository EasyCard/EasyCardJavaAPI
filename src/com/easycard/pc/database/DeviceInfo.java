package com.easycard.pc.database;


import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;



public class DeviceInfo implements ICmasTable{

	static Logger logger = Logger.getLogger(DeviceInfo.class);
	private boolean tbUpdated = false; //table updated?
	
	private class DBFields{
		
		//public int id=intDefaultValue;
		
		//User Define field
		public String nickName=strDefaultValue;//primary KEY		
		public String updateDateTime=strDefaultValue;//signOn update
		public String comport=strDefaultValue;
		public int hostType=intDefaultValue;
		public String tmID=strDefaultValue;
		public String tmLocationID=strDefaultValue;
		public String tmAgentNo=strDefaultValue;
		
		
		public String newDeviceID=strDefaultValue;	
		
		
		public String readerID=strDefaultValue;//signOn update		
		public String newLocationID=strDefaultValue;//signOn update
		
		public int tmSerialNo=intDefaultValue;
		public int batchNo=intDefaultValue;
		
		public String apiParaVer=strDefaultValue;
		
		public int adviceLimit=intDefaultValue;//TM26
		public int adviceLimitLock=intDefaultValue;//TM27
		public String txnSwitch=strDefaultValue;//TM30
		
		public int cashAddUnit = intDefaultValue;//TM31
		public int autoloadAmtLimit = intDefaultValue;//TM77
		
		
	}
	public static final String TABLE_NAME = "device_info";
	private DBFields dbFields = new DBFields();
	//------------------------------------------------------------
	
	public boolean selectTable(Connection con, String nickNameKey){
		boolean result = true;
		String sql = null;
		if(nickNameKey!=null) sql = String.format("SELECT * FROM %s", TABLE_NAME);
		else sql = String.format("SELECT * FROM %s", TABLE_NAME);
		
		PreparedStatement pst = null; 
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);			
			rs = pst.executeQuery();
			
					
			Field[] fields = dbFields.getClass().getDeclaredFields();
			int cnt = fields.length - 1;
			if(rs.next()){
				
				
				for(int i=0; i<cnt; i++){
					try {
						if(fields[i].getType().equals(int.class)){
							
							//logger.debug("name:"+fields[i].getName()+", value:"+rs.getInt(fields[i].getName()));
							fields[i].setInt(dbFields, rs.getInt(fields[i].getName()));
							
						} else if(fields[i].getType().equals(String.class)) {
							
							//logger.debug("name:"+fields[i].getName()+", value:"+rs.getString(fields[i].getName()));
							fields[i].set(dbFields, rs.getString(fields[i].getName()));
						}
					} catch (IllegalArgumentException
							| IllegalAccessException e) {
						// TODO Auto-generated catch block
						logger.error(e.getMessage());
						e.printStackTrace();
					}
				
				}
			} else {
				logger.error("device_info Table got NULL data. nickName:"+nickNameKey);
				result = false;
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
		}
		
		return result;
	} 
	
	
	@Override
	public boolean createTable(Connection con) {
		// TODO Auto-generated method stub
		boolean result = true;
		String sql = String.format("CREATE TABLE IF NOT EXISTS %s (", TABLE_NAME);
    	Field[] fields = dbFields.getClass().getDeclaredFields();
    	
    	
    	//declared field type
    	int cnt = fields.length-1;
    	for(int i=0; i<cnt; i++){
    		//logger.debug("name:"+fields[i].getName());
    		if(fields[i].getType().equals(String.class)){
    			sql+=fields[i].getName()+" TEXT";    			
    		} else if(fields[i].getType().equals(int.class)){
    			sql+=fields[i].getName()+" INTEGER";
    			    			
    		}    		
    		if(fields[i].getName().equalsIgnoreCase("nickName")) sql+=" PRIMARY KEY";
    		
    		if(i!=cnt-1) sql+=",";    		
    	}
    	sql+=");";
    	logger.debug("Create SQL cmd:"+sql);
    	
    	//String sql = "DROP TABLE IF EXISTS test ;create table test (id integer, name string); ";
        Statement stat = null;
        try {
        	//create Table
			stat = con.createStatement();
			stat.executeUpdate(sql);
			
			setHostType(1);
			setNickName("R1");
			setComport("COM6");
			setTmAgentNo("1234");
			setTmID("00");
			setTmLocationID("100001");
			setUpdateDateTime("201507020946");
			insertRec(con);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
        logger.debug("end");
        return result;
	}

	@Override
	public boolean insertRec(Connection con) {
		// TODO Auto-generated method stub
		logger.debug("start");
		boolean result = true;
		boolean _1stFieldExisted=false;
		Field[] fields = dbFields.getClass().getDeclaredFields();
    	String sql = String.format("INSERT INTO %s (", TABLE_NAME);
    	String values=" VALUES(";
    	int cnt = fields.length-1;//last field was not dbField
    	for(int i=0; i<cnt; i++){    		    	
    		try {
				if((fields[i].getType().equals(int.class) && fields[i].getInt(dbFields)==intDefaultValue) ||
				   (fields[i].getType().equals(String.class) && fields[i].get(dbFields)==strDefaultValue))
					continue;//value was defaultValue, not to process it
				if(_1stFieldExisted==true){
					values+=",";
	    			sql+=",";
				} 
				sql+=fields[i].getName();
				values+="?";
	    		
	    		_1stFieldExisted = true;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e.getMessage());
			}
    	}
    	values += ")";
    	sql+=")";
    	sql+=values;
    	
    	System.out.println("Insert SQL cmd:"+sql);
    	
    	PreparedStatement pst = null;
        try {
			pst = con.prepareStatement(sql);
			int idx = 1 ; 
	        for(int i=0; i<cnt; i++){
	        	try {
					if(fields[i].getType().equals(String.class) && fields[i].get(dbFields)!=strDefaultValue){
						
						String value = (String) fields[i].get(dbFields);
						//System.out.println("value:"+value);
						pst.setString(idx++, value);
							
					} else if(fields[i].getType().equals(int.class) && fields[i].getInt(dbFields) != intDefaultValue){
						int value = (int) fields[i].get(dbFields);
						pst.setInt(idx++, value);
			    	}    
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = false;
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result = false;
				}     		
	    	}
	                       
	        
	        pst.executeUpdate();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			result = false;
		}
		
		logger.debug("end");
		return result;
	}

	@Override
	public boolean updateRec(Connection con) {
		// TODO Auto-generated method stub
		boolean result = true;
		boolean _1stFieldExisted=false;
		String sql = String.format("UPDATE %s SET ", TABLE_NAME);
    	Field[] fields = dbFields.getClass().getDeclaredFields();
    	
    	int cnt = fields.length -1;
    	for(int i=0; i<cnt; i++){
    		try {
				if((fields[i].getType().equals(int.class) && fields[i].getInt(dbFields)==intDefaultValue) ||
				   (fields[i].getType().equals(String.class) && fields[i].get(dbFields)==strDefaultValue))
						continue;
				
				if(_1stFieldExisted==true) sql+=",";
				sql+=fields[i].getName()+"=?";
	    		
				_1stFieldExisted = true;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//value was defaultValue, not to process it
    		
    	}
    	
    	sql += " WHERE nickName=?";
    	logger.debug("sql cmd:"+sql);
    	
    	try {
    		PreparedStatement pst = null;
    		pst = con.prepareStatement(sql);
        
    		int idx = 1 ; 
    		for(int i=0; i<cnt; i++){
			
    			if(fields[i].getType().equals(String.class) && fields[i].get(dbFields)!=strDefaultValue){
					String value = (String) fields[i].get(dbFields);
					//System.out.println("value:"+value);
					pst.setString(idx++, value);
					
				} else if(fields[i].getType().equals(int.class) && fields[i].getInt(dbFields)!=intDefaultValue){
					int value = (int) fields[i].get(dbFields);
					pst.setInt(idx++, value);
	    		}   
			 
    		}

    		pst.setString(idx++, getNickName());//WHERE nickName=?
  			pst.executeUpdate();
  			this.tbUpdated = false;
    	} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}  catch (SQLException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  			result = false;
  		}
		
    	
		return result;
	}

	@Override
	public boolean deleteRec(Connection con) {
		// TODO Auto-generated method stub
		boolean result = true;
		String sql = String.format("DELETE FROM %s WHERE nickName=?", TABLE_NAME);
		
		
		//String sql = "DELETE FROM host_info WHERE hostType=?";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, getNickName());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

	public String getNickName() {
		return dbFields.nickName;
	}
	public void setNickName(String nickName) {
		this.tbUpdated = true;
		this.dbFields.nickName = nickName;
	}
	
	
	public String getComport() {
		
		
		return dbFields.comport;
	}
	public void setComport(String comport) {
		this.tbUpdated = true;
		this.dbFields.comport = comport;
	}
	
	public String getTmID() {
		return dbFields.tmID;
	}
	public void setTmID(String tmID) {
		this.tbUpdated = true;
		this.dbFields.tmID = tmID;
	}
	public String getTmLocationID() {
		return dbFields.tmLocationID;
	}
	public void setTmLocationID(String tmLocationID) {
		this.tbUpdated = true;
		this.dbFields.tmLocationID = tmLocationID;
	}
	public String getTmAgentNo() {
		return dbFields.tmAgentNo;
	}
	public void setTmAgentNo(String tmAgentNo) {
		this.tbUpdated = true;
		this.dbFields.tmAgentNo = tmAgentNo;
	}

	public int getHostType() {
		return dbFields.hostType;
	}

	public void setHostType(int hostType) {
		this.tbUpdated = true;
		this.dbFields.hostType = hostType;
	}

	public String getUpdateDateTime() {
		return dbFields.updateDateTime;
	}

	public void setUpdateDateTime(String updateDateTime) {
		this.tbUpdated = true;
		this.dbFields.updateDateTime = updateDateTime;
	}
	
	
	public String getNewDeviceID() {
		//logger.debug("newDeviceID:"+dbFields.newDeviceID);
		return dbFields.newDeviceID;
	}
	public void setNewDeviceID(String newDeviceID) {
		this.tbUpdated = true;
		this.dbFields.newDeviceID = newDeviceID;
	}
	
	
	public String getReaderID() {
		return dbFields.readerID;
	}
	public void setReaderID(String readerID) {
		this.tbUpdated = true;
		this.dbFields.readerID = readerID;
	}
	public String getNewLocationID() {
		return dbFields.newLocationID;
	}
	public void setNewLocationID(String newLocationID) {
		this.tbUpdated = true;
		this.dbFields.newLocationID = newLocationID;
	}
	
	public int getBatchNo() {
		return dbFields.batchNo;
	}

	public void setBatchNo(int batchNo) {
		this.tbUpdated = true;
		this.dbFields.batchNo = batchNo;
	}

	public int getTmSerialNo() {
		logger.debug("getter:"+dbFields.tmSerialNo);
		return dbFields.tmSerialNo;
	}

	public void setTmSerialNo(int tmSerialNo) {
		logger.debug("setter:"+tmSerialNo);
		this.tbUpdated = true;
		this.dbFields.tmSerialNo = tmSerialNo;
	}
	
	public String getApiParaVer() {
		return dbFields.apiParaVer;
	}

	public void setApiParaVer(String apiParaVer) {
		logger.debug("setter:"+apiParaVer);
		this.tbUpdated = true;
		this.dbFields.apiParaVer = apiParaVer;
	}
	
	public int getAdviceLimit() {
		return dbFields.adviceLimit;
	}

	public void setAdviceLimit(int limit) {
		this.tbUpdated = true;
		this.dbFields.adviceLimit = limit;
	}
	
	public int getAdviceLimitLock() {
		return dbFields.adviceLimitLock;
	}

	public void setAdviceLimitLock(int limit) {
		this.tbUpdated = true;
		this.dbFields.adviceLimitLock = limit;
	}
	
	public String getTxnSwitch() {
		return dbFields.txnSwitch;
	}

	public void setTxnSwitch(String txnSwitch) {
		logger.debug("setter:"+txnSwitch);
		this.tbUpdated = true;
		this.dbFields.txnSwitch = txnSwitch;
	}
	
	
	public int getCashAddUnit() {
		return dbFields.cashAddUnit;
	}

	public void setCashAddUnit(int unit) {
		this.tbUpdated = true;
		this.dbFields.cashAddUnit = unit;
	}
	
	public int getAutoloadAmtLimit() {
		return dbFields.autoloadAmtLimit;
	}

	public void setAutoloadAmtLimit(int limit) {
		this.tbUpdated = true;
		this.dbFields.autoloadAmtLimit = limit;
	}
	
	public boolean getTbUpdated(){
		return this.tbUpdated;
	}
}
