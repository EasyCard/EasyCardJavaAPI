package com.easycard.pc.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import org.apache.log4j.Logger;

public abstract class BaseSQLite {

	static Logger logger = Logger.getLogger(BaseSQLite.class);
	
	public Connection getConnection(String dbName) throws SQLException{
		Connection cn = null;
		logger.debug("1");
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			//throw new ECCDatabaseProblemException(e1.getMessage());			
		}
	  
		logger.debug("1");
			String connectionString = "jdbc:sqlite:"+dbName;
		
			try {
				logger.debug("1");
				cn = DriverManager.getConnection(connectionString);
				logger.debug("1");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//throw new ECCDatabaseProblemException(e.getMessage());
				
			}
			logger.debug("1");	
			 return cn; 
	}
	
	/*
	public Connection getConnection(String dbName) throws SQLException
    {
		logger.debug("start");
		Connection cn = null;
		SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.enableRecursiveTriggers(true);
    
            
        SQLiteDataSource ds = new SQLiteDataSource(config); 
        ds.setUrl("jdbc:sqlite:"+dbName);
        //ds.setUrl("jdbc:sqlite:config/CMAS.db");
        cn = ds.getConnection();
        logger.debug("end");
        return cn; 
    }
	*/
	public void exeSqlStatement (Connection con, String sqlStatement) throws SQLException{
		Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sqlStatement);
	}
	
	/*
	//create Table
    public void createTable(Connection con, String sqlStatement)throws SQLException{
        //String sql = "DROP TABLE IF EXISTS test ;create table test (id integer, name string); ";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sqlStatement);
        
    }
    //drop table
    public void dropTable(Connection con, String sqlStatement)throws SQLException{
        //String sql = "drop table test ";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sqlStatement);
    }
    
    //新增
    public void insert(Connection con,int id,String name)throws SQLException{
        String sql = "insert into test (id,name) values(?,?)";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        int idx = 1 ; 
        pst.setInt(idx++, id);
        pst.setString(idx++, name);
        pst.executeUpdate();
        
    }
    //修改
    public void update(Connection con,int id,String name)throws SQLException{
        String sql = "update test set name = ? where id = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        int idx = 1 ; 
        pst.setString(idx++, name);
        pst.setInt(idx++, id);
        pst.executeUpdate();
    }
    //刪除
    public void delete(Connection con,int id)throws SQLException{
        String sql = "delete from test where id = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        int idx = 1 ; 
        pst.setInt(idx++, id);
        pst.executeUpdate();
    }
    
    public void selectAll(Connection con)throws SQLException{
        String sql = "select * from test";
        Statement stat = null;
        
        ResultSet rs = null;
        stat = con.createStatement();
        rs = stat.executeQuery(sql);
        while(rs.next())
        {
            System.out.println(rs.getInt("id")+"\t"+rs.getString("name"));
        }
    }
    */
}
