package com.easycard.pc.communication.socket;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;



public class MyX509TrustManager implements X509TrustManager{
	
	static Logger logger = Logger.getLogger(MyX509TrustManager.class);
	private String serverCAFileName = null;
	//private String clientCAFileName = null;
	
	public MyX509TrustManager(String serverCAName, String clientCAName)
	{
		serverCAFileName = serverCAName;
		//clientCAFileName = clientCAName;
	}
	
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		
		// TODO Auto-generated method stub
		if(serverCAFileName == null){
			logger.info("Pass Server Cert. Check");
		} else {
			/*check Server Cert. with CA File by kobe , but not yet test, because no CA File*/
			FileInputStream inStream = null;
			try {
				inStream = new FileInputStream(serverCAFileName);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				logger.error(e1.getMessage());
			}
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate CACert = (X509Certificate)cf.generateCertificate(inStream);
			try {
				inStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String CASubjectdn	= CACert.getSubjectDN().getName();
			String CASIssuerdn	= CACert.getIssuerDN().getName() ;
			
			logger.debug("CAcert SubjectName:" + CASubjectdn) ;
			logger.debug("CAcert IssuerName:" + CASIssuerdn) ;
			logger.debug("waitint for authed.cert Type:" + CACert.getType()) ;
			
			for (X509Certificate cert : chain) {
                // Verifing by public key
            	if(CASubjectdn.equalsIgnoreCase(cert.getSubjectDN().getName())){
            		cert.checkValidity();
            		try {
						cert.verify(CACert.getPublicKey());
					} catch (InvalidKeyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error(e.getMessage());
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error(e.getMessage());
					} catch (NoSuchProviderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error(e.getMessage());
					} catch (SignatureException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error(e.getMessage());
					}
            	}							
            }					
		}
	}
	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
