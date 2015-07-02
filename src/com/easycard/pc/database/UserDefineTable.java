package com.easycard.pc.database;


import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;



public class UserDefineTable implements ICmasTable{

	static Logger logger = Logger.getLogger(UserDefineTable.class);	
	private class DBFields{
		//public int id=intDefaultValue;//autoincrement
		public String nickName=strDefaultValue;//primary KEY		
		public String updateDateTime=strDefaultValue;
		public String readerComport=strDefaultValue;
		public int hostType=intDefaultValue;
		public String tmID=strDefaultValue;
		public String tmLocationID=strDefaultValue;
		public String tmAgentNo=strDefaultValue;
		
		DBFields(){}
	}
	public static final String TABLE_NAME = "user_define";
	private DBFields dbFields = new DBFields();
	//------------------------------
	public boolean selectTable(Connection con, String nickNameKey){
		boolean result = true;
		String sql = String.format("SELECT * FROM %s", TABLE_NAME);
		
		PreparedStatement pst = null; 
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql);
			//pst.setString(1, "R1");
			rs = pst.executeQuery();
			
					
			Field[] fields = dbFields.getClass().getDeclaredFields();
			int cnt = fields.length - 1;
			if(rs.next()){
				logger.debug("got data");
				
				for(int i=0; i<cnt; i++){
					try {
						if(fields[i].getType().equals(int.class)){
							logger.debug("int type");
							logger.debug("name:"+fields[i].getName()+", value:"+rs.getInt(fields[i].getName()));
							fields[i].setInt(dbFields, rs.getInt(fields[i].getName()));
							
						} else if(fields[i].getType().equals(String.class)) {
							logger.debug("string type");
							logger.debug("name:"+fields[i].getName()+", value:"+rs.getString(fields[i].getName()));
							fields[i].set(dbFields, rs.getString(fields[i].getName()));
						}
					} catch (IllegalArgumentException
							| IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
			} else {
				logger.error("UserDefine Table got NULL data. nickName:"+nickNameKey);
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
    	//logger.debug("total fields cnt:"+fields.length);
    	
    	//declared field type
    	int cnt = fields.length-1;
    	for(int i=0; i<cnt; i++){
    		//logger.debug("name:"+fields[i].getName());
    		if(fields[i].getType().equals(String.class)){
    			sql+=fields[i].getName()+" TEXT";
    			
    		} else if(fields[i].getType().equals(int.class)){
    			sql+=fields[i].getName()+" INTEGER";    			    		
    		}    		
    		if(fields[i].getName().equalsIgnoreCase("nickName")) sql+=" PRIMARY KEY ";
    		//if(fields[i].getName().equalsIgnoreCase("id")) sql+=" AUTOINCREMENT ";
    		
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
			setReaderComport("COM6");
			setTmAgentNo("1234");
			setTmID("12");
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
		Field[] fields = dbFields.getClass().getDeclaredFields();
    	String sql = String.format("INSERT INTO %s (", TABLE_NAME);
    	String values=" VALUES(";
    	int cnt = fields.length-1;//last field was not dbField
    	for(int i=0; i<cnt; i++){
    		try {
				if((fields[i].getType().equals(int.class) && fields[i].getInt(dbFields)==intDefaultValue) ||
				   (fields[i].getType().equals(String.class) && fields[i].get(dbFields)==strDefaultValue))
					continue;//value was defaultValue, not to process it
				//System.out.println("name:"+fields[i].getName());
				sql+=fields[i].getName();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    		
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
		
		String sql = String.format("UPDATE %s SET ", TABLE_NAME);
    	Field[] fields = dbFields.getClass().getDeclaredFields();
    	
    	int cnt = fields.length -1;
    	for(int i=0; i<cnt; i++){
    		try {
				if((fields[i].getType().equals(int.class) && fields[i].getInt(dbFields)==intDefaultValue) ||
				   (fields[i].getType().equals(String.class) && fields[i].get(dbFields)==strDefaultValue))
						continue;
				if(i==cnt-1) sql+=fields[i].getName()+"=? ";
	    		else sql+=fields[i].getName()+"=?,";
				
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

    		pst.setString(idx++, getNickName()); //for WHERE nickName=?
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
		this.dbFields.nickName = nickName;
	}
	
	
	public String getReaderComport() {
		return dbFields.readerComport;
	}
	public void setReaderComport(String readerComport) {
		this.dbFields.readerComport = readerComport;
	}
	
	public String getTmID() {
		return dbFields.tmID;
	}
	public void setTmID(String tmID) {
		this.dbFields.tmID = tmID;
	}
	public String getTmLocationID() {
		return dbFields.tmLocationID;
	}
	public void setTmLocationID(String tmLocationID) {
		this.dbFields.tmLocationID = tmLocationID;
	}
	public String getTmAgentNo() {
		return dbFields.tmAgentNo;
	}
	public void setTmAgentNo(String tmAgentNo) {
		this.dbFields.tmAgentNo = tmAgentNo;
	}

	public int getHostType() {
		return dbFields.hostType;
	}

	public void setHostType(int hostType) {
		this.dbFields.hostType = hostType;
	}

	public String getUpdateDateTime() {
		return dbFields.updateDateTime;
	}

	public void setUpdateDateTime(String updateDateTime) {
		this.dbFields.updateDateTime = updateDateTime;
	}
	
	/*
	public int getID() {
		return dbFields.id;
	}
*/
	//public void setHostType(int hostType) {
	//	this.dbFields.hostType = hostType;
	//}
}
