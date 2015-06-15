package Reader;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;

import org.apache.log4j.Logger;


import Utilities.Util;


import org.apache.commons.lang.ArrayUtils;

public class ApduRecvSender implements IRecvSender{

	
	private SerialPort mPort = null;
	private String portName = "COM1";
	//private boolean initOk = false;
	static Logger logger = Logger.getLogger(ApduRecvSender.class);
	
	public ApduRecvSender(){
		//todo...
		
	}
	
	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		
		logger.info("setter:"+portName);
		this.portName = portName;		
	}



	private boolean openPort(String portName) {
		
		logger.info("Start, comportname:"+portName+this.portName);
		String[] portNames = SerialPortList.getPortNames();
		if (portNames == null) {
			logger.error("no any comport existed");
			return false;
		}
		int i = 0;
		for (; i < portNames.length; i ++) {
			if (portNames[i].equals(portName)) {
				break;
			}
		}
		if (i >= portNames.length) {
			logger.error("portName("+portName+") not exist");
			return false;
		}
		if(mPort != null)
		{
			//duplicated Open the same portName
			if(mPort.getPortName().equalsIgnoreCase(portName) && 
			   mPort.isOpened())
				return true;//already opend ,return true
			else
				closePort();
		}
		
		
		mPort = new SerialPort(portName);
		if (mPort == null) {
			logger.error("new SerialPort obj fail");
			return false;
		}
		
		try {
			if (!mPort.openPort()) {
				logger.error("openport fail");
				return false;
			}
			
			
			if (!mPort.setParams(SerialPort.BAUDRATE_115200, 
			        		SerialPort.DATABITS_8,
			        		SerialPort.STOPBITS_1,
			        		SerialPort.PARITY_NONE)) {
				closePort();
				logger.error("setParams fail");
				return false;
			}
		} catch (SerialPortException e) {
			closePort();
			e.printStackTrace();
			logger.error("openPort SerialPortException: "+e.getMessage());
			return false;
		}
		
		logger.info("End");
		return true;
	}
	
	private boolean closePort() {
		logger.info("Start");
		if (mPort == null || !mPort.isOpened()) {
			mPort = null;
			logger.error("mPort obj null or opened fail");
			return false;
		}
		
		try {
			mPort.closePort();
		} catch (SerialPortException e) {
			e.printStackTrace();
			logger.error("closet Port SerialPortException:"+e.getMessage());
		}
		mPort = null;
		
		logger.info("End");
		return true;
	}
	
	private boolean write(byte[] bytes) {
		
		logger.info("Start");
		if (mPort == null || !mPort.isOpened() || 
			bytes == null || bytes.length <= 0) {
			logger.error("mPort obj something wrong");
			return false;
		}
		
		try {
			return mPort.writeBytes(bytes);
		} catch (SerialPortException e) {
			e.printStackTrace();
			logger.error("write SerialPortException:"+e.getMessage());
		}
		
		logger.info("End");
		return false;
	}
	
	private byte[] read(int byteCount, int timeOut) {
		
		logger.info("Start");
		if (mPort == null || !mPort.isOpened()) {
			logger.error("mPort obj something wrong");
			return null;
		}
	
		try {
			return mPort.readBytes(byteCount, timeOut);
		} catch (SerialPortException e) {
			logger.error("read SerialPortException:"+e.getMessage());
			e.printStackTrace();
			
		} catch (SerialPortTimeoutException e)
		{
			logger.error("read SerialPortTimeoutException:"+e.getMessage());
			e.printStackTrace();
			
		}
		
		logger.info("End");
		return null;
	}
	
	
	
	public byte[] sendRecv(byte[] sendBuffer) {
		// TODO Auto-generated method stub
		
		logger.info("Start");
				
    	int totalRecvLen;
    	
		
    	byte[] recvDataBody;
    	byte[] recvLen;
    	byte[] recvBuffer;
    	
    	
    	try {    		
    		
    		
    		
	    	if(!openPort(portName))
			{
			    logger.error("openPort:("+portName+") fail");
			    return null;
			    	//return RespCode.COMPORT_OPEN_FAIL.getId();
			}
    		
    		
    		logger.debug("Comport Send >>>:"+Util.hex2StringLog(sendBuffer));
    		boolean ret= write(sendBuffer);//Write data to port
    		if(ret == false){
    			logger.error("portName:"+portName+", Write apdu data fail");
    			return null;
    		}
	
    		recvLen = read(3,60000);//60sec to read data len 3bytes
    		
    		logger.debug("read from 1~3 bytes dataLen:"+Util.hex2StringLog(recvLen));
    		if(recvLen==null) {
    			logger.error("null len");
    			return null;
    		}
    		    		
    		totalRecvLen = (recvLen[2]&0xFF) + ((recvLen[1] << 8)&0xFF00) + ((recvLen[0] << 16)&0xFF0000);
    		logger.info("read body len:"+totalRecvLen);
    		
    		//expectedRecvLen = DataFormat.byteArrayToInt(recvLen);
    		recvDataBody = read(totalRecvLen+1, 60000);//60sec to read dataBody and LRC
    		
    		recvBuffer = ArrayUtils.addAll(recvLen, recvDataBody);
    		
    	
    		logger.debug("Comport Recv <<<len("+recvBuffer.length+"):"+Util.hex2StringLog(recvBuffer));
			
			
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
    	
    	logger.info("End");
    	return recvBuffer;
    }

	@Override
	public boolean finish() {
		// TODO Auto-generated method stub
		return closePort();
		
	}

}
