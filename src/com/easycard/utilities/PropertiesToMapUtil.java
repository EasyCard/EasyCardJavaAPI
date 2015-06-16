package com.easycard.utilities;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
 


public class PropertiesToMapUtil {

 
    /**
     * 根據java標準properties文件讀取資料(文件编码格式採用UTF-8格式！)，并赋值为一个 HashMap<String,String>
     * @param path
     * @param map
     * @return
     * @throws Exception
     */
    public final static Map<String,String> fileToMap(String path,Map<String,String> map) throws Exception{
        if(map == null){
            map = new HashMap<String,String>();
        }
        FileInputStream isr = null;
        Reader r = null;
        try {
            isr = new FileInputStream(path);
            r = new InputStreamReader(isr, "utf-8");
            Properties props = new Properties();
            props.load(r);
            Set<Entry<Object, Object>> entrySet = props.entrySet();
            for (Entry<Object, Object> entry : entrySet) {
                if (!entry.getKey().toString().startsWith("#")) {
                    map.put(((String) entry.getKey()).trim(), ((String) entry
                            .getValue()).trim());
                }
            }
            return map;
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (IOException e) {
              
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e2) {
               
                }
            }
        }
    }
     
     
    /**
     * 根據java標準properties文件讀取資料，并赋值为一个 HashMap<String,String>
     * @param path
     * @param map
     * @return
     * @throws Exception
     */
    public final static Map<String,String> fileToMap(String path,Map<String,String> map,String encoding) throws Exception{
        if(map == null){
            map = new HashMap<String,String>();
        }
        FileInputStream isr = null;
        Reader r = null;
        try {
            isr = new FileInputStream(path);
            r = new InputStreamReader(isr, encoding);
            Properties props = new Properties();
            props.load(r);
            Set<Entry<Object, Object>> entrySet = props.entrySet();
            for (Entry<Object, Object> entry : entrySet) {
                if (!entry.getKey().toString().startsWith("#")) {
                    map.put(((String) entry.getKey()).trim(), ((String) entry
                            .getValue()).trim());
                }
            }
            return map;
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                  
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e2) {
                
                }
            }
        }
    }
}