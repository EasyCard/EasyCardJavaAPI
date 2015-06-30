package com.easycard.pc.database;

import java.sql.Connection;
public interface ICmasTable {

	public boolean createTable(Connection con);
	public boolean insertRec(Connection con);
	public boolean updateRec(Connection con);
	public boolean deleteRec(Connection con);
	//public void createTable(Connection con);
	
}
