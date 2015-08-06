package com.easycard.pc.database;


import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;



public class BatchDetail implements ICmasTable{

	static Logger logger = Logger.getLogger(BatchDetail.class);
	private boolean tbUpdated = false; //table updated?
	
	public class DBFields{
	
		public String newDeviceIDMixBatchNo=strDefaultValue;//primary KEY
		public String msgType = strDefaultValue;
		public String rrn = strDefaultValue;
		//public String txnDateTime=strDefaultValue;
		//public int tmSerialNo=intDefaultValue;
		public int txnAmt=intDefaultValue;
		//public int txnType=intDefaultValue;
		public String pCode=strDefaultValue;
		public String adviceReq=strDefaultValue;
		public String adviceResp=strDefaultValue;
		public String t3900 = strDefaultValue;
		
		
	}
	public static final String TABLE_NAME = "batch_detail";
	private DBFields dbFields = new DBFields();
	//------------------------------------------------------------
	
	public List<BatchDetail.DBFields> selectUnUploadAdvice(Connection con, String newDeviceIDMixBatchNo){
		List<BatchDetail.DBFields> adviceSet = new ArrayList<BatchDetail.DBFields>();
		String sql = null;
		PreparedStatement pst = null; 
		ResultSet rs = null;
		
		logger.debug("select batch_detail key was:"+newDeviceIDMixBatchNo);
		if(newDeviceIDMixBatchNo!=null) sql = String.format("SELECT * FROM %s WHERE newDeviceIDMixBatchNo=? and adviceResp IS NULL", TABLE_NAME);
		else sql = String.format("SELECT * FROM %s WHERE newDeviceIDMixBatchNo=? and adviceResp IS NULL", TABLE_NAME);
		
		
		
		try {
			pst = con.prepareStatement(sql);			
			pst.setString(1, newDeviceIDMixBatchNo);
			rs = pst.executeQuery();
		
			//logger.debug("advices count was:"+rs.);
			while(rs.next()){
				DBFields record = new DBFields();
				Field[] fields = record.getClass().getDeclaredFields();
				int cnt = fields.length - 1;
				for(int i=0; i<cnt; i++){
					try {
						if(fields[i].getType().equals(int.class)){
							
							//logger.debug("name:"+fields[i].getName()+", value:"+rs.getInt(fields[i].getName()));
							fields[i].setInt(record, rs.getInt(fields[i].getName()));
							
						} else if(fields[i].getType().equals(String.class)) {
							
							//logger.debug("name:"+fields[i].getName()+", value:"+rs.getString(fields[i].getName()));
							fields[i].set(record, rs.getString(fields[i].getName()));
						}
					} catch (IllegalArgumentException
							| IllegalAccessException e) {
						// TODO Auto-generated catch block
						logger.error(e.getMessage());
						e.printStackTrace();
					}
				}
				adviceSet.add(record);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("SQLException:"+e.getMessage());
			e.printStackTrace();
		}
		
		
		return adviceSet;
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
    		//if(fields[i].getName().equalsIgnoreCase("nickName")) sql+=" PRIMARY KEY";
    		
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
    	
    	sql += " WHERE newDeviceIDMixBatchNo=? and rrn=?";
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

    		pst.setString(idx++, getNewDeviceIDMixBatchNo());//WHERE newDeviceIDMixBatchNo=?
    		pst.setString(idx++, getRRN());//WHERE rrn=?
  			pst.executeUpdate();
  			this.tbUpdated = false;
    	} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
    		logger.error("IllegalArgumentException:"+e.getMessage());
			e.printStackTrace();
			result = false;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			logger.error("IllegalAccessException:"+e.getMessage());
			e.printStackTrace();
			result = false;
		}  catch (SQLException e) {
  			// TODO Auto-generated catch block
			logger.error("SQLException:"+e.getMessage());
  			e.printStackTrace();
  			result = false;
  		}
		
    	
		return result;
	}

	@Override
	public boolean deleteRec(Connection con) {
		// TODO Auto-generated method stub
		boolean result = true;
		String sql = String.format("DELETE FROM %s WHERE newDeviceIDMixBatchNo=? AND rrn=? AND msgType=?", TABLE_NAME);
		
		
		//String sql = "DELETE FROM host_info WHERE hostType=?";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
			logger.debug("delete Record from batch_detail table, sqlCommand:"+sql+", NewDeviceIDMixBatchNo:"+getNewDeviceIDMixBatchNo()+",getRRN():"+getRRN()+",getMsgType:"+getMsgType());
			pst.setString(1, getNewDeviceIDMixBatchNo());
			pst.setString(2, getRRN());
			pst.setString(3, getMsgType());
			pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("SQLException:"+e.getMessage());
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

	public String getNewDeviceIDMixBatchNo() {
		return this.dbFields.newDeviceIDMixBatchNo;
	}
	public void setNewDeviceIDMixBatchNo(String newDeviceIDMixBatchNo) {
		this.tbUpdated = true;
		this.dbFields.newDeviceIDMixBatchNo = newDeviceIDMixBatchNo;
	}
	
	/*
	public String getTxnDateTime() {
		return this.dbFields.txnDateTime;
	}
	public void setTxnDateTime(String txnDateTime) {
		this.tbUpdated = true;
		this.dbFields.txnDateTime = txnDateTime;
	}*/
	public int getTxnAmt() {
		return this.dbFields.txnAmt;
	}
	public void setTxnAmt(int txnAmt) {
		this.tbUpdated = true;
		this.dbFields.txnAmt = txnAmt;
	}
	public String getPCode() {
		return this.dbFields.pCode;
	}
	public void setPCode(String pCode) {
		this.tbUpdated = true;
		this.dbFields.pCode = pCode;
	}
	public String getAdviceReq() {
		return this.dbFields.adviceReq;
	}
	public void setAdviceReq(String adviceReq) {
		this.tbUpdated = true;
		this.dbFields.adviceReq = adviceReq;
	}
	public String getAdviceResp() {
		return this.dbFields.adviceResp;
	}
	public void setAdviceResp(String adviceResp) {
		this.tbUpdated = true;
		this.dbFields.adviceResp = adviceResp;
	}

	public String getRRN() {
		return dbFields.rrn;
	}

	public void setRRN(String rrn) {
		this.tbUpdated = true;
		this.dbFields.rrn = rrn;
	}
	
	public boolean getTbUpdated(){
		return this.tbUpdated;
	}
	
	public String getMsgType() {
		return dbFields.msgType;
	}

	public void setMsgType(String msgType) {
		this.tbUpdated = true;
		this.dbFields.msgType = msgType;
	}
	
	public String getT3900() {
		return dbFields.t3900;
	}

	public void setT3900(String t3900) {
		this.tbUpdated = true;
		this.dbFields.t3900 = t3900;
	}
	
}
