package Utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

public class Util {
	public static String sGetHexString(byte[] data) {
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < data.length; i ++) {
	    	if (i > 0) {
	    		sb.append(", ");
	    	}
	        sb.append(String.format("%02X", data[i]));
	    }
	    return sb.toString();
	}
	
	public static String sGetHexString(byte[] data, int length) {
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < length; i ++) {
	    	if (i > 0) {
	    		sb.append(", ");
	    	}
	        sb.append(String.format("%02X", data[i]));
	    }
	    return sb.toString();
	}
	
	public static String sGetHexString(byte[] data, int offset, int length) {
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < length; i ++) {
	    	if (i > 0) {
	    		sb.append(", ");
	    	}
	        sb.append(String.format("%02X", data[offset + i]));
	    }
	    return sb.toString();
	}
	
	public static String sBytesToString(byte[] bytes, int offset, int byteCount) {
		if (offset + byteCount > bytes.length) {
			return "";
		}
		String str = "";
		for (int i = 0; i < byteCount; i ++) {
			str += (char)bytes[offset + i];
		}
		return str;
	}
	
	public static long sByteToLong(byte[] bytes, int offset, boolean bMSB) {
		if (bytes == null || bytes.length < (offset + 8)) {
			return 0l;
		}
		
		long l = 0;
		if (bMSB) {
			l += ((long) bytes[offset] << 56) & 0xFF00000000000000l;
			l += ((long) bytes[offset + 1] << 48) & 0x00FF000000000000l;
			l += ((long) bytes[offset + 2] << 40) & 0x0000FF0000000000l;
			l += ((long) bytes[offset + 3] << 32) & 0x000000FF00000000l;
			l += ((long) bytes[offset + 4] << 24) & 0x00000000FF000000l;
			l += ((long) bytes[offset + 5] << 16) & 0x0000000000FF0000l;
			l += ((long) bytes[offset + 6] << 8) & 0x000000000000FF00l;
			l += (long) bytes[offset + 7] & 0x00000000000000FFl;
		} else {
			l += ((long) bytes[offset + 7] << 56) & 0xFF00000000000000l;
			l += ((long) bytes[offset + 6] << 48) & 0x00FF000000000000l;
			l += ((long) bytes[offset + 5] << 40) & 0x0000FF0000000000l;
			l += ((long) bytes[offset + 4] << 32) & 0x000000FF00000000l;
			l += ((long) bytes[offset + 3] << 24) & 0x00000000FF000000l;
			l += ((long) bytes[offset + 2] << 16) & 0x0000000000FF0000l;
			l += ((long) bytes[offset + 1] << 8) & 0x000000000000FF00l;
			l += (long) bytes[offset] & 0x00000000000000FFl;
		}
		return l;
	}
	
	public static long sByteToLong(byte[] bytes, int offset, int len, boolean bMSB) {
		if (bytes == null || len <= 0 || len > 16 || bytes.length < (offset + len)) {
			return 0;
		}
		
		for (int i = offset; i < offset + len; i ++) {
			if (bytes[i] >= '0' && bytes[i] <= '9') {
				bytes[i] = (byte) (bytes[i] - '0');
			} else if (bytes[i] >= 'A' && bytes[i] <= 'F') {
				bytes[i] = (byte) (bytes[i] - 0x37);
			} else if (bytes[i] >= 'a' && bytes[i] <= 'f') {
				bytes[i] = (byte) (bytes[i] - 0x57);
			} else {
				return 0;
			}
		}
		
		byte[] b = null;
		if (0 != bytes.length % 2) {
			b = new byte[bytes.length + 1];
		}
		for (int i = 1; i < b.length; i ++) {
			b[i] = bytes[i + offset - 1];
		}
		
		long value = 0;
		int j = 0;
		if (bMSB) {
			for (int i = b.length - 1; i >= 0; i -= 2, j += 2) {
				value += (b[i] & 0x0F) << (j * 4);
				value += (b[i - 1] & 0x0F) << ((j + 1) * 4);	
			}
		} else {
			for (int i = 0; i < b.length; i += 2) {
				value += (b[i + 1] & 0x0F) << (j * 4);
				j ++;
				if (i == 0 && b[i] == 0) {
				} else {
					value += (b[i] & 0x0F) << (j * 4);
					j ++;
				}
			}
		}
		return value;
	}
	
	public static int sByteToInt(byte[] bytes, int offset, boolean bMSB) {
		if (bytes == null || bytes.length < (offset + 4)) {
			return 0;
		}
		
		int i = 0;
		if (bMSB) {
			i += (bytes[offset] << 24) & 0xFF000000;
			i += (bytes[offset + 1] << 16) & 0x00FF0000;
			i += (bytes[offset + 2] << 8) & 0x0000FF00;
			i += bytes[offset + 3] & 0x000000FF;
		} else {
			i += (bytes[offset + 3] << 24) & 0xFF000000;
			i += (bytes[offset + 2] << 16) & 0x00FF0000;
			i += (bytes[offset + 1] << 8) & 0x0000FF00;
			i += bytes[offset] & 0x000000FF;
		}
		return i;
	}
	
	public static int sByteToInt(byte[] bytes, int offset, int len, boolean bMSB) {
		if (bytes == null || len <= 0 || len > 8 || bytes.length < (offset + len)) {
			return 0;
		}
		
		for (int i = offset; i < offset + len; i ++) {
			if (bytes[i] >= '0' && bytes[i] <= '9') {
				bytes[i] = (byte) (bytes[i] - '0');
			} else if (bytes[i] >= 'A' && bytes[i] <= 'F') {
				bytes[i] = (byte) (bytes[i] - 0x37);
			} else if (bytes[i] >= 'a' && bytes[i] <= 'f') {
				bytes[i] = (byte) (bytes[i] - 0x57);
			} else {
				return 0;
			}
		}
		
		byte[] b = null;
		if (0 != bytes.length % 2) {
			b = new byte[bytes.length + 1];
			for (int i = 1; i < b.length; i ++) {
				b[i] = bytes[i + offset - 1];
			}
		} else {
			b = Arrays.copyOf(bytes, bytes.length);
		}
		
		int value = 0;
		int j = 0;
		if (bMSB) {
			for (int i = b.length - 1; i >= 0; i -= 2, j += 2) {
				value += (b[i] & 0x0F) << (j * 4);
				value += (b[i - 1] & 0x0F) << ((j + 1) * 4);	
			}
		} else {
			for (int i = 0; i < b.length; i += 2) {
				value += (b[i + 1] & 0x0F) << (j * 4);
				j ++;
				if (i == 0 && b[i] == 0) {
				} else {
					value += (b[i] & 0x0F) << (j * 4);
					j ++;
				}
			}
		}
		return value;
	}
	
	public static String sGetLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
	                    return inetAddress.getHostAddress();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        ex.printStackTrace();
	    }
	    return null;
	}
	
	// alpha characters, 1 data element = 1 byte
	public static String sGetAttr_a(byte[] bytes) {
		for (int i = 0; i < bytes.length; i ++) {
			if (bytes[i] < 'a' || bytes[i] > 'z') {
				return "";
			}
			if (bytes[i] < 'A' || bytes[i] > 'Z') {
				return "";
			}
		}
		return new String(bytes);
	}
			
	// alpha-number characters, 1 data element = 1 byte
	public static String sGetAttr_an(byte[] bytes) {
		for (int i = 0; i < bytes.length; i ++) {
			if (bytes[i] < 'a' || bytes[i] > 'z') {
				return "";
			}
			if (bytes[i] < 'A' || bytes[i] > 'Z') {
				return "";
			}
			if (bytes[i] < '0' || bytes[i] > '9') {
				return "";
			}
		}
		return new String(bytes);
	}
			
	// alpha-number and special characters(all characters), 1 data element = 1 byte
	public static String sGetAttr_ans(byte[] bytes) {
		for (int i = 0; i < bytes.length; i ++) {
			if (bytes[i] < ' ' || bytes[i] > '~') {
				return "";
			}
		}
		return new String(bytes);
	}
			
	// binary data, �N���@��byte unpack��00-FF��2bytes ASCII�r��, 1 data element = 2 bytes
	public static String sGetAttr_b(byte[] bytes) {
		String s = "";
		for (int i = 0; i < bytes.length; i ++) {
			byte n1 = (byte) ((bytes[i] & 0xF0) >> 4);
			byte n2 = (byte) (bytes[i] & 0x0F);
			if (n1 < 0x0A) {
				s += (char) ('0' + n1);
			} else {
				s += (char) ('A' + (n1 - 10));
			}
			if (n2 < 0x0A) {
				s += (char) ('0' + n2);
			} else {
				s += (char) ('A' + (n2 - 10));
			}
		}
		return s;
	}
	
	// binary data, �N���@��byte unpack��00-FF��2bytes ASCII�r��, 1 data element = 2 bytes
	public static String sGetAttr_b(byte b) {
		String s = "";
		char c1 = (char) ('0' + ((b & 0xF0) >> 4));
		char c2 = (char) ('0' + (b & 0x0F));
		s += c1;
		s += c2;
		return s;
	}
			
	// numeric data, each data element represents 4 bits BCD, 1 data element = 1 byte
	public static String sGetAttr_n(byte[] bytes) {
		for (int i = 0; i < bytes.length; i ++) {
			if (bytes[i] < '0' || bytes[i] > '9') {
				return "";
			}
		}
		return new String(bytes);
	}

	
	
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat DATE_FORMAT_SHORT = new SimpleDateFormat("yyMMdd");
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HHmmss");
	public static final SimpleDateFormat _YYMM = new SimpleDateFormat("yyMM");
	/*
	public static String sGetDateTime() {
		return DATE_TIME_FORMAT.format(Calendar.getInstance().getTime());
	}
	*/
	
	public static String getDateTime(int unixTimeStamp, SimpleDateFormat format){
		Date date = new Date((long)unixTimeStamp * 1000);
		return format.format(date);
	}
	
	public static String sGetDateTime(int unixTimeStamp) {
		Date date = new Date((long)unixTimeStamp * 1000);
		return DATE_TIME_FORMAT.format(date);
	}
	
	public static String sGetDate() {
		return DATE_FORMAT.format(Calendar.getInstance().getTime());
	}
	
	public static String sGetDate(int unixTimeStamp) {
		Date date = new Date((long)unixTimeStamp * 1000);
		return DATE_FORMAT.format(date);
	}
	
	public static String sGetDateShort() {
		return DATE_FORMAT_SHORT.format(Calendar.getInstance().getTime());
	}
	
	public static String sGetDateShort(int unixTimeStamp) {
		Date date = new Date((long)unixTimeStamp * 1000);
		return DATE_FORMAT_SHORT.format(date);
	}

	public static String sGetTime() {
		return TIME_FORMAT.format(Calendar.getInstance().getTime());
	}
	
	public static String sGetTime(int unixTimeStamp) {
		Date date = new Date((long)unixTimeStamp * 1000);
		return TIME_FORMAT.format(date);
	}
	
	public static boolean sDumpElementsToFile(String tag, String contents, String path) {
		String fn = path + tag + DATE_TIME_FORMAT.format(Calendar.getInstance().getTime()) + ".xml";
		
		File fxml = new File(fn);
		BufferedWriter writer = null;
		try {
			fxml.createNewFile();
		    writer = new BufferedWriter(new FileWriter(fxml));
		    writer.write(contents);
		}
		catch (IOException e) {	
			return false;
		}
		finally {
		    try {
		        if (writer != null) {
		        	writer.close( );
		        }
		    }
		    catch (IOException e) {
		    }
		}
		
		return true;
	}
	public static String hex2StringLog(byte[] data)
	{
		StringBuilder sb = new StringBuilder();
		   
		for(byte b: data)
		      sb.append("("+String.format("%02x", b & 0xff)+")");
		   
		return sb.toString();	
	}
	
	public static byte[] ascii2Bcd(String s) {
		if (s == null || s.length() == 0 || s.length() % 2 != 0) {
			return null;
		}
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < s.length(); i += 2) {
			byte b1 = c2b(s.charAt(i));
			byte b2 = c2b(s.charAt(i + 1));
			b[i / 2] = (byte) ((b1 << 4) & 0xF0);
			b[i / 2] += (byte) (b2 & 0x0F);
		}
		return b;
	}
	
	private static byte c2b(char c) {
		if (c >= '0' && c <= '9') {
			return (byte) (c - '0');
		} else if (c >= 'A' && c <= 'F') {
			return (byte) (c - 0x37);
		} else if (c >= 'a' && c <= 'f') {
			return (byte) (c - 0x57);
		} else {
			return 0;
		}
	}
	

	public static String bcd2Ascii(byte bcd) {
		
		return String.format("%02X", bcd);
	}
	
	public static String bcd2Ascii(byte[] bcd) {

		StringBuffer sb = new StringBuffer();	
		for (int i = 0; i < bcd.length; i++) {		
			sb.append(bcd2Ascii(bcd[i]));
		}
		return sb.toString();
	}

	public static byte[] paddingChar(String s, boolean toRight, byte c, int totalDigits){
		byte []b = s.getBytes();
		return paddingChar(b, toRight, c, totalDigits);		
	}
	
	public static byte[] paddingChar(byte[] data, boolean toRight, byte c, int totalDigits){
				
		int dataLen = data.length;
		byte result[] = new byte[totalDigits];
		Arrays.fill(result, c);
		
		//123xxx
		if(toRight) System.arraycopy(data, 0, result, 0, (totalDigits-dataLen)>0?dataLen:totalDigits);	 
		else System.arraycopy(data, 0, result, (totalDigits-dataLen)>0?(totalDigits-dataLen):0, (totalDigits-dataLen)>0?dataLen:totalDigits);
		
		return result;
		
	}
	
	public static byte[] int2ByteArray(int i){
		
		byte[] bytes = BigInteger.valueOf(i).toByteArray();
				
		return bytes;
	}
	
	public static void arrayReverse(byte[] data){

		try{
			int maxIndex = data.length-1;		
			byte temp = 0x00;
			for(int i=0; i<(data.length/2); i++){
				temp = data[i];
				data[i] = data[maxIndex-i];
				data[maxIndex-i] = temp;			
			}		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getMACAddress(){
		InetAddress ip = null;
		NetworkInterface network = null;
		byte[] mac = null;
		String sMAC = null;
		try {
			ip = InetAddress.getLocalHost();			
			network = NetworkInterface.getByInetAddress(ip);
			mac = network.getHardwareAddress();
			sMAC = bcd2Ascii(mac);
			
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
		return sMAC;		
	}
	
	public static long bytes2Long(byte[] array, int offset, int digits) {
	    
		long l=0;
		int j = digits - 1;
		for(int i=0; i<digits; i++){
			l += ((long)(array[offset+i] & 0xff) << (((j-i)*8)));			
		}
		
		return l;  
	}
	
	public static String IntelFormat2Decimal(byte[] array, int offset, int digits){
		
		arrayReverse(array);
		long l = bytes2Long(array, offset, digits);
		return String.valueOf(l);
	}
	
}
