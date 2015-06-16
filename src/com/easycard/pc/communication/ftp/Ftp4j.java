package com.easycard.pc.communication.ftp;

import java.io.IOException;



import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;


import org.apache.log4j.Logger;

import com.easycard.pc.communication.socket.MyX509TrustManager;




//reference @ http://www.sauronsoftware.it/projects/ftp4j/index.php by kobe
public class Ftp4j {
	 
	static Logger logger = Logger.getLogger(Ftp4j.class);
	private FTPDataTransferListener ftpListener = null;
	private FTPClient client = null;
	
	
	private String caFilePath = null;
	private String clientCertFilePath = null;
	
	public Ftp4j(FTPDataTransferListener ftpListener){
		if(ftpListener == null) {
			this.ftpListener = new Ftp4jListener();
		} else this.ftpListener = ftpListener;
	}
	
	public String getCaFilePath() {
		return caFilePath;
	}

	public void setCaFilePath(String caFilePath) {
		this.caFilePath = caFilePath;
	}

	public String getClientCertFilePath() {
		return clientCertFilePath;
	}

	public void setClientCertFilePath(String clientCertFilePath) {
		this.clientCertFilePath = clientCertFilePath;
	}

	public boolean connect2Server(String host, 
							int port, 
							String id, 
							String pwd,
							boolean eanbleSSL)
	{
		
		boolean result = false;
		this.client = new FTPClient();
		client.setPassive(true); // Passive mode
		if(eanbleSSL)
		{
			logger.info("Enable FTPS");
			SSLContext sslContext = null;
			try {
				
				TrustManager[] trustManager = new TrustManager [] {
						(TrustManager) new MyX509TrustManager(this.caFilePath,this.clientCertFilePath)				
				};
				sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null, trustManager, new SecureRandom());
				
			} catch (NoSuchAlgorithmException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			} catch (KeyManagementException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
			SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
			client.setSSLSocketFactory(sslSocketFactory);
			client.setSecurity(FTPClient.SECURITY_FTPS);
		}
		
		try {
			logger.debug("FTP Host:"+host+", FTP port:"+port);
			client.connect(host, port);
			client.login(id, pwd);			
			result = true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public String getCurrentDIR()
	{
		String result = null;
		try {
			if(client != null)
				result = client.currentDirectory();
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean download(String serverPath, String localPath) {
		
		boolean result = false;
		
		try {			
			if(client==null) {
				logger.error("ftpClient object was NULL");
				return false;
			}		
			//logger.debug("CurreDIR:"+client.currentDirectory());
			
			logger.info("ftp download Remote:"+serverPath + " to local:"+ localPath);
			client.download(serverPath, new java.io.File(localPath), ftpListener);
			result = true;
			
		} catch (FTPDataTransferException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (FTPAbortedException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public void disconnect()
	{
		
		try {
			this.client.disconnect(true);
		
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		return;
	}
}
