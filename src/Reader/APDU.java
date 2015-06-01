package Reader;

import java.util.Arrays;

import org.apache.log4j.Logger;

//so Smart...
public abstract class APDU {
	
	
	static Logger logger = Logger.getLogger(APDU.class);
	protected boolean mReqDirty = true;
	
	
	protected static final int scReqMinLength_NoData = 9;
	protected static final int scReqMinLength = 10;
	protected static final int scReqInfoMinLength = 6;
	protected static final int scReqDataOffset = 8;
	
	/* Resquest Parameters and Methods */
	/* Prolog */
	protected byte Req_NAD = 0; 		// Node Address, 0x00
	protected byte Req_PCB = 0; 		// Protocol Control Byte, 0x00 
	protected byte Req_LEN = 0;			// Length of Information Section in bytes
	/* Information-Header */
	protected byte Req_CLA = 0;			// Classification code
	protected byte Req_INS = 0;			// Instruction code
	protected byte Req_P1 = 0;			// Parameter 1
	protected byte Req_P2 = 0;			// Parameter 2
	/* Information-Body */	
	protected byte Req_Lc = 0; 			// Length of command data field
	// protected byte[] Req_Data = null;	// Data contents
	protected byte Req_Le = 0; 			// Length of expected data responded
	/* Epilog */
	protected byte Req_EDC = 0;			// Error Detection Code
	
	public abstract byte[] GetRequest();
	public abstract boolean SetRequestData(byte[] bytes);
	public abstract int GetReqRespLength();
	public abstract void debugResponseData();
	
	protected static final int scRespMinLength = 6;
	protected static final int scRespDataOffset = 3;
	
	/* Respond Parameters and Methods */
	/* Prolog */ 
	protected byte Resp_NAD; 			// Node Address, 0x00
	protected byte Resp_PCB; 			// Protocol Control Byte, 0x00 
	protected byte Resp_LEN;			// Length of Information Section in bytes
	/* Information-Body */	
	protected byte[] Resp_Data;			// Data contents
	/* Information-Trailer */
	protected byte Resp_SW1;			// Status Code 1
	protected byte Resp_SW2;			// Status Code 2
	/* Epilog */
	protected byte Resp_EDC;			// Error Detection Code
	
	
	
	public abstract byte[] GetRespond();
	public abstract boolean SetRespond(byte[] bytes);
	
	protected byte getEDC(byte[] bytes, int length) {
		if (bytes == null || length < 2) {
			return 0x00;
		}
		
		byte sum = 0;
		for (int i = 0; i < length - 1; i ++) {
			sum ^= bytes[i]; 
		}
		
		return sum;
	}
	
	public byte ClearBit(byte b, int pos) {
		if (pos < 0 || pos > 7) {
			return b;
		}
		return (byte) (b & ~(1 << pos));
		
	}
	
	public byte SetBit(byte b, int pos) {
		if (pos < 0 || pos > 7) {
			return b;
		}
		return (byte) (b | (1 << pos));
	}
	
	public boolean IsBitSet(byte b, int pos) {
		if (pos < 0 || pos > 7) {
			return false;
		}
		return (b & (1 << pos)) > 0? true : false;
	}
	
	public boolean IsRespOk(byte SW1, byte SW2) {
		if (SW1 == 0x90 && SW2 == 0x00) {
			return true;
		}
		return false;
	}
	
	public int GetRespCode() {
		return ((Resp_SW1 << 8) + Resp_SW2) & 0x0000FFFF;
	}

	protected boolean checkResponseFormat(byte[] respData, int expectedRespDataLen) {
		int length = respData.length;
		int totalRespLen = expectedRespDataLen+scRespMinLength;
		int headLen = respData[2] & 0x000000FF;
		if (totalRespLen != length) {
			// invalid respond format... 
			logger.error("check total len Wrong!, totalLen should be:"+totalRespLen+", responseed Data array len:"+length);
			return false;
		}
		
		if (respData[2] != (byte) (expectedRespDataLen + 2)) { // Data + SW1 + SW2
			// invalid data format...
			logger.error("check Head len Wrong!, Head len:"+(int)(expectedRespDataLen+2)+", responseed head len:"+headLen);
			return false;
		}
		
		byte sum = getEDC(respData, length);
		if (sum != respData[totalRespLen - 1]) {
			// check sum error...
			logger.error("resp Data check Sum error");
			return false;
		}
				
		
		
		Resp_SW1 = respData[length - 3];
		Resp_SW2 = respData[length - 2];
		logger.debug("statusCode:"+String.format("%02x%02x", Resp_SW1,Resp_SW2));
		
		return true;
	}
	
}
