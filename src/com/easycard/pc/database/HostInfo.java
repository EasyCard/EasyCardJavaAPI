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

public class HostInfo implements ICmasTable{

	static Logger logger = Logger.getLogger(HostInfo.class);
	
	private class DBFields{
		
		public int hostType=intDefaultValue; //primary KEY
		public String hostName=strDefaultValue;		
		public String socketUrl=strDefaultValue;
		public String socketIP=strDefaultValue;
		public int socketPort=intDefaultValue;
		public String ftpUrl=strDefaultValue;
		public String ftpIP=strDefaultValue;
		public int ftpPort=intDefaultValue;
		public String ftpID=strDefaultValue;
		public String ftpPwd=strDefaultValue;
		DBFields(){}
	}
	public static final String TABLE_NAME = "host_info";
	private DBFields dbFields = new DBFields();
	//------------------------------
	public boolean selectTable(Connection con, int hostType){
		boolean result = false;
		String sql = String.format("SELECT * FROM %s WHERE hostType=?", TABLE_NAME);
		ResultSet rs = null;
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, hostType);
			rs = pst.executeQuery();
			Field[] fields = dbFields.getClass().getDeclaredFields();
			int cnt = fields.length - 1;
			if(rs.next()){
				for(int i=0; i<cnt; i++){
					try {
						if(fields[i].getType().equals(int.class)){
							
							fields[i].setInt(dbFields, rs.getInt(fields[i].getName()));
							
						} else if(fields[i].getType().equals(String.class)) {
							fields[i].set(dbFields, rs.getString(fields[i].getName()));
						}
					} catch (IllegalArgumentException
							| IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
			
			/*
				this.setHostType(rs.getInt("hostType"));
				this.setHostName(rs.getString("hostName"));
				this.setSocketUrl(rs.getString("socketUrl"));
				this.setSocketIP(rs.getString("socketIP"));
				
				this.setSocketPort(rs.getInt("socketPort"));
				this.setFtpUrl(rs.getString("ftpUrl"));
				this.setFtpIP(rs.getString("ftpIP"));
				this.setFtpPort(rs.getInt("ftpPort"));
				}*/
			} else {
				logger.error("HostInfo Table got NULL data:"+hostType);
				result = false;
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
    			sql+=fields[i].getName()+" text";
    		} else if(fields[i].getType().equals(int.class)){
    			sql+=fields[i].getName()+" integer";
    			if(fields[i].getName().equalsIgnoreCase("hostType")) sql+=" primary key";    			
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
            String[] file = {ConfigManager.HOST_DEVE_INFO_FILE, ConfigManager.HOST_TEST_INFO_FILE, ConfigManager.HOST_PROD_INFO_FILE};
    		try {
    			for(int i=0; i<file.length; i++){
	    			p.load(HostInfo.class.getResourceAsStream("/"+file[i]));
	    			setHostType(i);
	    			setSocketUrl(p.getProperty("HostUrl"));
	    			setSocketIP(p.getProperty("HostIP"));
	    			setSocketPort(Integer.valueOf(p.getProperty("HostPort")));
	    			setFtpUrl(p.getProperty("FtpUrl"));
	    			setFtpIP(p.getProperty("FtpIP"));
	    			setFtpPort(Integer.valueOf(p.getProperty("FtpPort")));
	    			setFtpID(p.getProperty("FtpLoginId"));
	    			setFtpPwd(p.getProperty("FtpLoginPwd"));	
	    			
	    			insertRec(con);
    			}
    			
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
        
		
		/*
		//String sql = "DROP TABLE IF EXISTS ? ;create table ? (id integer, name string); ";
		String sql = "CREATE TABLE host_info IF NOT EXISTS ("
				+ "hostType integer,"
				+ "url string, "
				+ "ip string, "
				+ "port integer, "
				+ "ftpUrl string, "
				+ "ftpIP string, "
				+ "ftpPort integer, "
				+ "ftpLoginID string, "
				+ "ftpLoginPwd string"
				+ ");";
		

		//建表時，用PreparedStatement需要花5 秒.怪...
		//PreparedStatement pst = null;
		//pst = con.prepareStatement(sql);
    	//pst.executeUpdate();
        try {
        	
        	Statement stat = null;
            stat = con.createStatement();
            stat.executeUpdate(sql);
            
            Properties p = new Properties();
            String[] file = {ConfigManager.HOST_DEVE_INFO_FILE, ConfigManager.HOST_TEST_INFO_FILE, ConfigManager.HOST_PROD_INFO_FILE};
    		try {
    			for(int i=0; i<file.length; i++){
	    			p.load(HostInfo.class.getResourceAsStream("/"+file[i]));
	    			setHostType(i);
	    			setSocketUrl(p.getProperty("HostUrl"));
	    			setSocketIP(p.getProperty("HostIP"));
	    			setSocketPort(Integer.valueOf(p.getProperty("HostPort")));
	    			setFtpUrl(p.getProperty("FtpUrl"));
	    			setFtpIP(p.getProperty("FtpIP"));
	    			setFtpPort(Integer.valueOf(p.getProperty("FtpPort")));
	    			setFtpID(p.getProperty("FtpLoginId"));
	    			setFtpPwd(p.getProperty("FtpLoginPwd"));	
	    			
	    			insertRec(con);
    			}
    			
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
		} catch (SQLException e) {
			logger.error("SQLException errCode:"+e.getMessage());
			// TODO Auto-generated catch block
			if(e.getErrorCode()!=0)//0:table already exists
				e.printStackTrace();
		}
       */
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
        
		
		/*
		String sql = "INSERT INTO host_info (hostType,url,ip,port,ftpUrl,ftpIP,ftpPort,ftpLoginID,ftpLoginPwd) values(?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
			 int idx = 1 ; 
		     pst.setInt(idx++, getHostType());
		     pst.setString(idx++, getSocketUrl());
		     pst.setString(idx++, getSocketIP());
		     pst.setInt(idx++, getSocketPort());
		     pst.setString(idx++, getFtpUrl());
		     pst.setString(idx++, getFtpIP());
		     pst.setInt(idx++, getFtpPort());
		     pst.setString(idx++, getFtpID());
		     pst.setString(idx++, getFtpPwd());
		     pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
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
    	
    	sql += " WHERE hostType=?";
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

    		pst.setInt(idx++, getHostType());
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
		
    	
    	
		/*
		int i=0;
		Field[] fs = fields.getClass().getDeclaredFields();
		String sql = "UPDATE FROM host_info SET ";
		String sql = "update test set name = ? where id = ?";
		for(Field field:fs){
			if(field.getType().equals(String.class)){
				field.get(this);
			}
		}
		
		String sql = "UPDATE FROM host_info SET (hostType,url,ip,port,ftpUrl,ftpIP,ftpPort,ftpLoginID,ftpLoginPwd) values(?,?,?,?,?,?,?,?,?) WHERE hostType=?";
		*/
		
		return result;
	}

	@Override
	public boolean deleteRec(Connection con) {
		// TODO Auto-generated method stub
		boolean result = true;
		String sql = String.format("DELETE FROM %s WHERE hostType=?", TABLE_NAME);
		
		
		//String sql = "DELETE FROM host_info WHERE hostType=?";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(sql);
			pst.setInt(1, getHostType());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

	public String getHostName() {
		return dbFields.hostName;
	}

	public void setHostName(String hostName) {
		this.dbFields.hostName = hostName;
	}

	public int getHostType() {
		return dbFields.hostType;
	}

	public void setHostType(int hostType) {
		this.dbFields.hostType = hostType;
	}

	public String getSocketUrl() {
		return dbFields.socketUrl;
	}

	public void setSocketUrl(String socketUrl) {
		this.dbFields.socketUrl = socketUrl;
	}

	public String getSocketIP() {
		return dbFields.socketIP;
	}

	public void setSocketIP(String socketIP) {
		this.dbFields.socketIP = socketIP;
	}

	public int getSocketPort() {
		return dbFields.socketPort;
	}

	public void setSocketPort(int port) {
		this.dbFields.socketPort = port;
	}

	public String getFtpUrl() {
		return dbFields.ftpUrl;
	}

	public void setFtpUrl(String ftpUrl) {
		this.dbFields.ftpUrl = ftpUrl;
	}

	public String getFtpIP() {
		return dbFields.ftpIP;
	}

	public void setFtpIP(String ftpIp) {
		this.dbFields.ftpIP = ftpIp;
	}

	public int getFtpPort() {
		return dbFields.ftpPort;
	}

	public void setFtpPort(int ftpPort) {
		this.dbFields.ftpPort = ftpPort;
	}

	public String getFtpID() {
		return dbFields.ftpID;
	}

	public void setFtpID(String ftpID) {
		this.dbFields.ftpID = ftpID;
	}

	public String getFtpPwd() {
		return dbFields.ftpPwd;
	}

	public void setFtpPwd(String ftpPwd) {
		this.dbFields.ftpPwd = ftpPwd;
	}

}
