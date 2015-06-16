package com.easycard.pc.communication.ftp;
import org.apache.log4j.Logger;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class Ftp4jListener implements FTPDataTransferListener{

	static Logger logger = Logger.getLogger(Ftp4jListener.class);
	@Override
	public void aborted() {
		// TODO Auto-generated method stub
		logger.error("ftpListener aborted");
	}

	@Override
	public void completed() {
		// TODO Auto-generated method stub
		logger.info("ftpListener completed");
	}

	@Override
	public void failed() {
		// TODO Auto-generated method stub
		logger.error("ftpListener failed");		
	}

	@Override
	public void started() {
		// TODO Auto-generated method stub		
		logger.info("ftpListener started");		
	}

	@Override
	public void transferred(int arg0) {
		// TODO Auto-generated method stub
		logger.info("ftpListener transferred");		
		//		System.out.println("Listen-transferred");
	}
	

}
