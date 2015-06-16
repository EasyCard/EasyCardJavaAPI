package com.easycard.utilities;



public class DataFormat {
   public DataFormat(){
	  
  }
   
public static byte booleam2byte(Boolean vIn)
{
	byte vOut = (byte)(vIn?1:0);
	return vOut;
}
   public static void putInt(int value, byte[] array, int offset) {
     array[offset]   = (byte)(0xff & (value >> 24));
     array[offset+1] = (byte)(0xff & (value >> 16));
     array[offset+2] = (byte)(0xff & (value >> 8));
     array[offset+3] = (byte)(0xff & value);
   }

   public static int getInt(byte[] array, int offset) {
     return
       ((array[offset]   & 0xff) << 24) |
       ((array[offset+1] & 0xff) << 16) |
       ((array[offset+2] & 0xff) << 8) |
        (array[offset+3] & 0xff);
   }

   public static void putLong(long value, byte[] array, int offset) {
     array[offset]   = (byte)(0xff & (value >> 56));
     array[offset+1] = (byte)(0xff & (value >> 48));
     array[offset+2] = (byte)(0xff & (value >> 40));
     array[offset+3] = (byte)(0xff & (value >> 32));
     array[offset+4] = (byte)(0xff & (value >> 24));
     array[offset+5] = (byte)(0xff & (value >> 16));
     array[offset+6] = (byte)(0xff & (value >> 8));
     array[offset+7] = (byte)(0xff & value);
   }

   public static long getLong7Byte(byte[] array, int offset) {
     return
    
       ((long)(array[offset+0] & 0xff) << 48) |
       ((long)(array[offset+1] & 0xff) << 40) |
       ((long)(array[offset+2] & 0xff) << 32) |
       ((long)(array[offset+3] & 0xff) << 24) |
       ((long)(array[offset+4] & 0xff) << 16) |
       ((long)(array[offset+5] & 0xff) << 8) |
       ((long)(array[offset+6] & 0xff));
   }
   public static long getLong(byte[] array, int offset) {
     return
       ((long)(array[offset] &0xff)		  <<56)	|  
       ((long)(array[offset+0] & 0xff) << 48) |
       ((long)(array[offset+1] & 0xff) << 40) |
       ((long)(array[offset+2] & 0xff) << 32) |
       ((long)(array[offset+3] & 0xff) << 24) |
       ((long)(array[offset+4] & 0xff) << 16) |
       ((long)(array[offset+5] & 0xff) << 8) |
       ((long)(array[offset+6] & 0xff));
   }
   //Byte[] to byte[]
   byte[] BytetoPrimitives(Byte[] oBytes)
   {

   byte[] bytes = new byte[oBytes.length];
   for(int i = 0; i < oBytes.length; i++){
       bytes[i] = oBytes[i];
   }
   return bytes;

   }



   //byte[] to Byte[]
   Byte[] bytetoObjects(byte[] bytesPrim) {

   Byte[] bytes = new Byte[bytesPrim.length];
   int i = 0;
   for (byte b : bytesPrim) bytes[i++] = b; //Autoboxing
   return bytes;

}
	public  static String convertToHexString(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
		    int halfbyte = (data[i] >>> 4) & 0x0F;
		    int two_halfs = 0;
		    do {
		        if ((0 <= halfbyte) && (halfbyte <= 9))
		            buf.append((char) ('0' + halfbyte));
		        else
		            buf.append((char) ('a' + (halfbyte - 10)));
		            halfbyte = data[i] & 0x0F;
		        } while(two_halfs++ < 1);
		    }
		return buf.toString();
		}
	
	
	/*
	 * long number to bcd byte array e.g. 123 --> (0000) 0001 0010 0011
	 * e.g. 12 ---> 0001 0010
	 */
	public static byte[] DecToBCDArray(long num) {
		int digits = 0;
 
		long temp = num;
		while (temp != 0) {
			digits++;
			temp /= 10;
		}
 
		int byteLen = digits % 2 == 0 ? digits / 2 : (digits + 1) / 2;
		boolean isOdd = digits % 2 != 0;
 
		byte bcd[] = new byte[byteLen];
 
		for (int i = 0; i < digits; i++) {
			byte tmp = (byte) (num % 10);
 
			if (i == digits - 1 && isOdd)
				bcd[i / 2] = tmp;
			else if (i % 2 == 0)
				bcd[i / 2] = tmp;
			else {
				byte foo = (byte) (tmp << 4);
				bcd[i / 2] |= foo;
			}
 
			num /= 10;
		}
 
		for (int i = 0; i < byteLen / 2; i++) {
			byte tmp = bcd[i];
			bcd[i] = bcd[byteLen - i - 1];
			bcd[byteLen - i - 1] = tmp;
		}
 
		return bcd;
	}
 
	
	
	//////////////////////////////////////////////////////////////
	public int GetBytetoInt(int byteValue)
	{
	byte intValue=0;   
	int temp = byteValue % 256;   
	if ( byteValue < 0) {   
	intValue =  (byte)(temp < -128 ? 256 + temp : temp);   
	}   
	else {   
	intValue =  (byte)(temp > 127 ? temp - 256 : temp);   
	}  
	return intValue;
	}
	
	public static int byteArrayToInt(byte[] b) 
	{
		int value = 0;
		int shift = 0;
		int len = b.length;
		for (int i = 0; i < len; i++) {
			shift = (len - 1 - i) * 8;
			value += (b[i] & 0xFF) << shift;
		}
		return value;
	}
	
	public static byte[] intToByteArray(int a)
	{
	byte[] ret = new byte[4];
	ret[0] = (byte) (a & 0xFF);   
	ret[1] = (byte) ((a >> 8) & 0xFF);   
	ret[2] = (byte) ((a >> 16) & 0xFF);   
	ret[3] = (byte) ((a >> 24) & 0xFF);
	return ret;
	}
	/*
	 * 
	 * 
	 */
	public static String AsciiToString(byte[] data) {
	    StringBuilder sb = new StringBuilder(data.length);
	    for (int i = 0; i < data.length; ++ i) {
	        if (data[i] < 0) throw new IllegalArgumentException();
	        sb.append((char) data[i]);
	    }
	    return sb.toString();
	}
	public static String AsciiToBinary(String asciiString){  
	
	byte[] bytes = asciiString.getBytes();  
	StringBuilder binary = new StringBuilder();  
	for (byte b : bytes)  
	{  
	int val = b;  
	for (int i = 0; i < 8; i++)  
	{  
	binary.append((val & 128) == 0 ? 0 : 1);  
	val <<= 1;  
	}  
	// binary.append(' ');  
	}  
	return binary.toString();  
	}  
	
	public static String bytesToHex(byte[] bytes) {
	    final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

	
}
