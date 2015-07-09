package com.easycard.pc.database;


import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;



public class TxnBatch implements ICmasTable{

	static Logger logger = Logger.getLogger(TxnBatch.class);
	private boolean tbUpdated = false; //table updated?
	private class DBFields{
		
		//public int id=intDefaultValue;
		
		//User Define field
		public String newDeviceIDMixBatchNo=strDefaultValue;//primary KEY		
		public int closed=intDefaultValue;
		public int totalCnt=intDefaultValue;
		public int totalAmt=intDefaultValue;		
		public int deductCnt=intDefaultValue;
		public int deductAmt=intDefaultValue;
		public int cashAddCnt=intDefaultValue;
		public int cashAddAmt=intDefaultValue;
		public int refundCnt=intDefaultValue;
		public int refundAmt=intDefaultValue;
		public int voidDeductCnt=intDefaultValue;
		public int voidDeductAmt=intDefaultValue;
		public int voidCashAddCnt=intDefaultValue;
		public int voidCashAddAmt=intDefaultValue;
		public int autoloadCnt=intDefaultValue;
		public int autoloadAmt=intDefaultValue;
	
		
		


		DBFields(){}
	}
	public static final String TABLE_NAME = "txn_batch";
	private DBFields dbFields = new DBFields();
	//------------------------------------------------------------
	
	public boolean selectTable(Connection con, String newDeviceIDMixBatchNo){
		boolean result = true;
		String sql = null;
		if(newDeviceIDMixBatchNo!=null) sql = String.format("SELECT * FROM %s", TABLE_NAME);
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
				logger.error("UserDefine Table got NULL data. newDeviceIDMixBatchNo:"+newDeviceIDMixBatchNo);
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
    		if(fields[i].getName().equalsIgnoreCase("newDeviceIDMixBatchNo")) sql+=" PRIMARY KEY";
    		
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
    	
    	sql += " WHERE newDeviceIDMixBatchNo=?";
    	logger.debug("sql cmd:"+sql);
    	
    	try {
    		PreparedStatement pst = null;
    		pst = con.prepareStatement(sql);
        
    		int idx = 1 ; 
    		for(int i=0; i<cnt; i++){
			
    			if(fields[i].getType().equals(String.class) && 
    			   fields[i].get(dbFields)!=strDefaultValue){
					String value = (String) fields[i].get(dbFields);
					//System.out.println("value:"+value);
					pst.setString(idx++, value);
					
				} else if(fields[i].getType().equals(int.class) && 
						  fields[i].getInt(dbFields)!=intDefaultValue){
					int value = (int) fields[i].get(dbFields);
					pst.setInt(idx++, value);
	    		}   
			 
    		}

    		pst.setString(idx++, getNewDeviceIDMixBatchNo());//WHERE nickName=?
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
			pst.setString(1, getNewDeviceIDMixBatchNo());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

	

	public String getNewDeviceIDMixBatchNo() {
		return this.dbFields.newDeviceIDMixBatchNo;
	}


	public void setNewDeviceIDMixBatchNo(String newDeviceIDMixBatchNo) {
		this.dbFields.newDeviceIDMixBatchNo = newDeviceIDMixBatchNo;
	}


	public int getClosed() {
		return this.dbFields.closed;
	}


	public void setClosed(int closed) {
		this.tbUpdated = true;
		this.dbFields.closed = closed;
	}


	public int getTotalCnt() {
		return this.dbFields.totalCnt;
	}


	public void setTotalCnt(int totalCnt) {
		this.tbUpdated = true;
		this.dbFields.totalCnt = totalCnt;
	}


	public int getTotalAmt() {
		return this.dbFields.totalAmt;
	}


	public void setTotalAmt(int totalAmt) {
		this.tbUpdated = true;
		this.dbFields.totalAmt = totalAmt;
	}


	public int getDeductCnt() {
		return this.dbFields.deductCnt;
	}


	public void setDeductCnt(int deductCnt) {
		this.tbUpdated = true;
		this.dbFields.deductCnt = deductCnt;
	}


	public int getDeductAmt() {
		return this.dbFields.deductAmt;
	}


	public void setDeductAmt(int deductAmt) {
		this.tbUpdated = true;
		this.dbFields.deductAmt = deductAmt;
	}


	public int getCashAddCnt() {
		return this.dbFields.cashAddCnt;
	}


	public void setCashAddCnt(int cashAddCnt) {
		this.tbUpdated = true;
		this.dbFields.cashAddCnt = cashAddCnt;
	}


	public int getCashAddAmt() {
		return this.dbFields.cashAddAmt;
	}


	public void setCashAddAmt(int cashAddAmt) {
		this.tbUpdated = true;
		this.dbFields.cashAddAmt = cashAddAmt;
	}


	public int getRefundCnt() {
		return this.dbFields.refundCnt;
	}


	public void setRefundCnt(int refundCnt) {
		this.tbUpdated = true;
		this.dbFields.refundCnt = refundCnt;
	}


	public int getRefundAmt() {
		return this.dbFields.refundAmt;
	}


	public void setRefundAmt(int refundAmt) {
		this.tbUpdated = true;
		this.dbFields.refundAmt = refundAmt;
	}


	public int getVoidDeductCnt() {
		return this.dbFields.voidDeductCnt;
	}


	public void setVoidDeductCnt(int voidDeductCnt) {
		this.tbUpdated = true;
		this.dbFields.voidDeductCnt = voidDeductCnt;
	}


	public int getVoidDeductAmt() {
		return this.dbFields.voidDeductAmt;
	}


	public void setVoidDeductAmt(int voidDeductAmt) {
		this.tbUpdated = true;
		this.dbFields.voidDeductAmt = voidDeductAmt;
	}


	public int getVoidCashAddCnt() {
		return this.dbFields.voidCashAddCnt;
	}


	public void setVoidCashAddCnt(int voidCashAddCnt) {
		this.tbUpdated = true;
		this.dbFields.voidCashAddCnt = voidCashAddCnt;
	}


	public int getVoidCashAddAmt() {
		return this.dbFields.voidCashAddAmt;
	}


	public void setVoidCashAddAmt(int voidCashAddAmt) {
		this.tbUpdated = true;
		this.dbFields.voidCashAddAmt = voidCashAddAmt;
	}


	public int getAutoloadCnt() {
		return this.dbFields.autoloadCnt;
	}


	public void setAutoloadCnt(int autoloadCnt) {
		this.tbUpdated = true;
		this.dbFields.autoloadCnt = autoloadCnt;
	}


	public int getAutoloadAmt() {
		return this.dbFields.autoloadAmt;
	}


	public void setAutoloadAmt(int autoloadAmt) {
		this.tbUpdated = true;
		this.dbFields.autoloadAmt = autoloadAmt;
	}
	
	public void initDefault(){
		setTotalCnt(0);
		setTotalAmt(0);
		setDeductCnt(0);
		setDeductAmt(0);
		setCashAddAmt(0);
		setCashAddCnt(0);
		setRefundCnt(0);
		setRefundAmt(0);
		setVoidDeductAmt(0);
		setVoidDeductCnt(0);
		setVoidCashAddCnt(0);
		setVoidCashAddAmt(0);
		setAutoloadCnt(0);
		setAutoloadAmt(0);
		setClosed(0);
	}
	
	public boolean getTbUpdated(){
		return this.tbUpdated;
	}

}
