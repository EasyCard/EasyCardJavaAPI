package com.easycard.pc.database;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.easycard.pc.CMAS.ConfigManager;

public class TxnInfo implements ICmasTable{

	static Logger logger = Logger.getLogger(HostInfo.class);
	
	private class DBFields{
		
		public String newDeviceID="";		
		public String tmSerialNo="";
		public String batchNo="";
			
		
		DBFields(){}
	}
	public static final String TABLE_NAME = "txn_info";
	private DBFields dbFields = new DBFields();
	//------------------------------

	
	@Override
	public boolean createTable(Connection con) {
		// TODO Auto-generated method stub
		boolean result = true;
		String sql = String.format("CREATE TABLE IF NOT EXISTS %s (", TABLE_NAME);
    	Field[] fields = dbFields.getClass().getDeclaredFields();
    	logger.debug("total fields cnt:"+fields.length);
    	
    	//declared field type
    	int cnt = fields.length-1;
    	for(int i=0; i<cnt; i++){
    		logger.debug("name:"+fields[i].getName());
    		if(fields[i].getType().equals(String.class)){
    			sql+=fields[i].getName()+" text";
    		} else if(fields[i].getType().equals(int.class)){
    			sql+=fields[i].getName()+" integer";
    			if(fields[i].getName().equalsIgnoreCase("newDeviceID")) sql+=" primary key";    			
    		}    		
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
		Field[] fields = dbFields.getClass().getDeclaredFields();
    	String sql = String.format("INSERT INTO %s (", TABLE_NAME);
    	String values=" values(";
    	int cnt = fields.length-1;//last field was not dbField
    	for(int i=0; i<cnt; i++){
    		//System.out.println("name:"+fields[i].getName());
    		sql+=fields[i].getName();    		
    		values+="?";
    		if(i!=cnt-1) {
    			values+=",";
    			sql+=",";
    		}
    		
    	}
    	values += ")";
    	sql+=")";
    	sql+=values;
    	
    	System.out.println("Insert SQL cmd:"+sql);
    	
    	//String sql = "insert into test (id,name) values(?,?)";
    	
        
    	PreparedStatement pst = null;
        try {
			pst = con.prepareStatement(sql);
			int idx = 1 ; 
	        for(int i=0; i<cnt; i++){
				try {
					if(fields[i].getType().equals(String.class)){
						String value = (String) fields[i].get(dbFields);
						//System.out.println("value:"+value);
						pst.setString(idx++, value);
							
					} else if(fields[i].getType().equals(int.class)){
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
		
		String sql = String.format("UPDATE %s SET ", TABLE_NAME);
    	Field[] fields = dbFields.getClass().getDeclaredFields();
    	
    	int cnt = fields.length -1;
    	for(int i=0; i<cnt; i++){
    		if(i==cnt-1) sql+=fields[i].getName()+"=? ";
    		else sql+=fields[i].getName()+"=?,";
    	}
    	
    	sql += " WHERE newDeviceID=?";
    	logger.debug("sql cmd:"+sql);
    	
    	try {
    		PreparedStatement pst = null;
    		pst = con.prepareStatement(sql);
        
    		int idx = 1 ; 
    		for(int i=0; i<cnt; i++){
			
				if(fields[i].getType().equals(String.class)){
					String value = (String) fields[i].get(dbFields);
					//System.out.println("value:"+value);
					pst.setString(idx++, value);
					
				} else if(fields[i].getType().equals(int.class)){
					int value = (int) fields[i].get(dbFields);
					pst.setInt(idx++, value);
	    		}    
			 
    		}

    		pst.setString(idx++, getNewDeviceID());
  			pst.executeUpdate();
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
		String sql = String.format("DELETE FROM %s WHERE newDeviceID=?", TABLE_NAME);
		
		
		//String sql = "DELETE FROM host_info WHERE hostType=?";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, getNewDeviceID());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

	public String getNewDeviceID() {
		return dbFields.newDeviceID;
	}
	public void setNewDeviceID(String newDeviceID) {
		this.dbFields.newDeviceID = newDeviceID;
	}
	public String getBatchNo() {
		return dbFields.batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.dbFields.batchNo = batchNo;
	}
	public String TMSerialNo() {
		return dbFields.tmSerialNo;
	}
	public void setBlackListVer(String tmSerialNo) {
		this.dbFields.tmSerialNo = tmSerialNo;
	}
}
