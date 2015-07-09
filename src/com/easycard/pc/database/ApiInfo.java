package com.easycard.pc.database;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.easycard.pc.CMAS.ConfigManager;

public class ApiInfo implements ICmasTable{

	static Logger logger = Logger.getLogger(HostInfo.class);
	private boolean tbUpdated = false;
	
	private class DBFields{
		
		public String apiName="";//primary key
		public String apiVer="";
		public String blackListVer="";
		public int nowDBVersion=1;	
		
		DBFields(){}
	}
	public static final String TABLE_NAME = "api_info";
	private DBFields dbFields = new DBFields();
	//------------------------------

	public boolean selectTable(Connection con){
		boolean result = true;
		 String sql = String.format("SELECT * FROM %s", TABLE_NAME);
	     PreparedStatement pst = null;
	     
	    
	     ResultSet rs = null;
	     try {
			//stat = con.createStatement();
	    	pst = con.prepareStatement(sql);
	    	//pst.setString(1, "EasyCardApi");
		    //pst.setString(1, keyApiName);
			rs = pst.executeQuery();
		    //rs.last();//move to last pointer, sqlite not support
		    
		    //int cnt = rs.getRow();
			//if(cnt == 0) return false;//no any data existed
			//logger.debug("ApiTable Cnt:"+cnt);
			
			if(rs.next()){
				this.setApiName(rs.getString("apiName"));
				this.setApiVer(rs.getString("apiVer"));
				this.setBlackListVer(rs.getString("blackListVer"));
				this.setNowDBVersion(rs.getInt("nowDBVersion"));
				
				logger.debug("apiName:"+rs.getString("apiName"));
		    	logger.debug("apiVer:"+rs.getString("apiVer"));
		    	logger.debug("blackListVer:"+rs.getString("blackListVer"));
		    	logger.debug("nowDBVersion:"+rs.getInt("nowDBVersion"));
			} else {
				logger.error("ApiInfo no data");
				return false;			
			}
	     } catch (SQLException e) {
			// TODO Auto-generated catch block
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
    	logger.debug("total fields cnt:"+fields.length);
    	
    	//declared field type
    	int cnt = fields.length-1;
    	for(int i=0; i<cnt; i++){
    		logger.debug("name:"+fields[i].getName());
    		if(fields[i].getType().equals(String.class)){
    			sql+=fields[i].getName()+" TEXT";
    		} else if(fields[i].getType().equals(int.class)){
    			sql+=fields[i].getName()+" INTEGER";
    			if(fields[i].getName().equalsIgnoreCase("apiName")) sql+=" PRIMARY KEY";    			
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
			
			//setting Default value
			Properties p = new Properties();

    		try {
    			
	    		p.load(HostInfo.class.getResourceAsStream("/"+ConfigManager.EASYCARD_API_FILE));
	    			
	    		setApiName(p.getProperty("ApiName"));
	    		setApiVer(p.getProperty("ApiVer"));
	    		setBlackListVer(p.getProperty("BlackListVer"));
	    		setNowDBVersion(Integer.valueOf(p.getProperty("DBVersion")));
	    			
	    		insertRec(con);
    		
    			
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    			result = false;
    		}
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
    	
    	sql += " WHERE apiName=?";
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

    		pst.setString(idx++, getApiName());
  			pst.executeUpdate();
  			this.tbUpdated = true;
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
		String sql = String.format("DELETE FROM %s WHERE apiName=?", TABLE_NAME);
		
		
		//String sql = "DELETE FROM host_info WHERE hostType=?";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, getApiName());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

	public String getApiName() {
		return dbFields.apiName;
	}
	public void setApiName(String apiName) {
		this.tbUpdated = true;
		this.dbFields.apiName = apiName;
	}
	public String getApiVer() {
		return dbFields.apiVer;
	}
	public void setApiVer(String apiVer) {
		this.tbUpdated = true;
		this.dbFields.apiVer = apiVer;
	}
	public String getBlackListVer() {
		return dbFields.blackListVer;
	}
	public void setBlackListVer(String blackListVer) {
		this.tbUpdated = true;
		this.dbFields.blackListVer = blackListVer;
	}
	
	public int getNowDBVersion() {
		return dbFields.nowDBVersion;
	}
	public void setNowDBVersion(int nowDBVersion) {
		this.tbUpdated = true;
		this.dbFields.nowDBVersion = nowDBVersion;
	}
	
	public boolean getTbUpdated(){
		return this.tbUpdated;
	}
}
