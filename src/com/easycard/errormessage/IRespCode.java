package com.easycard.errormessage;

public interface IRespCode {
	
	public static final int ACTION_SUCCESS = 0;
	public static final int ACTION_FAIL = 1;
	public static final int ACTION_LOCK = 2;
	public static final int ACTION_SVCS = 3;
	
	public String getId() ;
	public String getMsg();
	public int getActionId();
}
