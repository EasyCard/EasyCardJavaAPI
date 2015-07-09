package com.easycard.pc.database;

import java.sql.Connection;
public interface ICmasTable {

	public final String strDefaultValue = null;
	public final int intDefaultValue = -1;
	
	
	public boolean createTable(Connection con);
	public boolean insertRec(Connection con);
	public boolean updateRec(Connection con);
	public boolean deleteRec(Connection con);
	
	
}
