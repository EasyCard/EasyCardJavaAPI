package Reader;


import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

public abstract class BaseResponseAutoParser {

	static Logger logger = Logger.getLogger(BaseResponseAutoParser.class);
    //欄位及長度	
    protected abstract LinkedHashMap<String, Integer> getFields();
    //index of byte array
    private int index = 0;

    public void parse(byte[] input){

        try{
        	LinkedHashMap<String, Integer> maps = this.getFields();
        	if(maps == null){
        		logger.error("maybe dataLen was wrong, maps was NULL");
        		return;
        	}
        	
        	
            for(java.util.Map.Entry<String, Integer> entry : maps.entrySet()){
                Field field = this.getClass().getField(entry.getKey());
                Class<?> clazz = field.getType();                
                int length = entry.getValue();           
                Object value = translate(input, clazz, length);
                field.set(this, value);
            }
        }catch (Exception e){
            //略
        	e.printStackTrace();
        	logger.error(e.getMessage());
        }

    }

    protected Object translate(byte[] input, Class<?> clazz, int length){
        Object result = null;
        //if(clazz.equals(int.class)){      
        //    result = translateToInt(input);
        if(clazz.equals(String.class)){        	
            result = translateToString(input, length);
        }else if(clazz.equals(byte[].class)){        	
        	result = translateToByteArray(input, length);
        }else if(clazz.equals(byte.class)){        	
            result = translateToByte(input);
        }else{
        	logger.error("Unknowen Class Type:"+clazz.getName());        	
            
        }

        return result;
    }

    private byte translateToByte(byte[] input){
    	
    	byte result = 0x00;
    	result = input[index++];
    	
    	
    	return result;
    }
    
    /*
    //byte to int
    private int translateToInt(byte[] input){
        final int LENGTH = 4;
        byte[] cell = Arrays.copyOfRange(input, index, index+LENGTH);
        index += LENGTH;
        
        //System.out.println("len:"+cell.length);
        System.out.println("value:"+String.format("%02x,%02x,%02x,%02x", cell[0],cell[1],cell[2],cell[3]));
        
        
        int result = 0;
        result += (cell[0] << 24) & 0xFF000000;
        result += (cell[1] << 16) & 0x00FF0000;
        result += (cell[2] << 8) & 0x0000FF00;
		result += cell[3] & 0x000000FF;
		
		System.out.println("index:"+index);
        return result;
    }
*/
    
    //byte to String
    private String translateToString(byte[] input, int length){
        
        String result = null;
        byte[] b = Arrays.copyOfRange(input, index, index+length);
        
        result = new String(b);
		index += length;
        return result;
    }
    
  //byte array
    private byte[] translateToByteArray(byte[] input, int length){
        
        byte[] result = new byte[length];
        System.arraycopy(input, index, result, 0, length);
        
        /*
        for (byte b : result) {
			System.out.println(String.format("%02X", b));
		}
        */
        index+=length;
        return result;
    }

}
