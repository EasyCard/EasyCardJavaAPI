package ErrorMessage;

import java.util.HashMap;
import java.util.Map;



public enum ApiRespCode  implements IRespCode{
	
	/**/
	SUCCESS("0", "Success", 0),
	ERROR("-1", "Something Error", 1),
	ESCAPE("-2", "Escape", 1),	
	LRC_NOT_MATCH("-3", "Reader'LRC maybe does not match with API", 1),
	COMPORT_OPEN_FAIL("-4", "Reader Open Comport fail", 1),
	SSL_CONNECT_FAIL("-5", "SSL Connection Fail", 1),
	READER_NO_RESPONSE("-6", "Reader no response", 1),
	HOST_NO_RESPONSE("-7", "Host no response", 1),
	CARDID_IN_BLACKLIST("-8", "此卡，卡號不合法", 1),
	AUTOLOAD_YES_OR_NO("-9", "餘額不足，是否允許自動加值？", 1),
	NO_RECV_RESPONSE_FROM_HOST("-10", "無接收到主機資料", 1),
	UNKNOWN_ERROR_CODE("XXX","Unknowen ErrCode", 1);
	

	  
	private String id; 
	private final String msg;
	private final int actionId;
	private static Map<String, IRespCode> codeToEnumMap;

	  
	  
	ApiRespCode(String id, String msg, int actionId) {
	    this.id = id;
	    this.msg = msg;
	    this.actionId = actionId;
	  }

	  public String getId() {
	    return this.id;
	  }

	  public String getMsg() {
	    return this.msg;
	  }
	  
	  public int getActionId() {
		    return this.actionId; 
	  }
	  
	  private void setId(String id)
	  {
		  this.id = id;
	  }
	  
	  public static IRespCode fromCode(String code, IRespCode[] rc) {
            // Keep a hashmap of mapping between code and corresponding enum as a cache.  We need to initialize it only once
            if (codeToEnumMap == null) {
                codeToEnumMap = new HashMap<String, IRespCode>();
                for (IRespCode aEnum : rc) {
                    codeToEnumMap.put(aEnum.getId(), aEnum);
                }
            }

            IRespCode enumForGivenCode = codeToEnumMap.get(code);
            if (enumForGivenCode == null) {
                //enumForGivenCode = UNKNOWN_ERROR_CODE;
            	UNKNOWN_ERROR_CODE.setId(code);
            	enumForGivenCode = UNKNOWN_ERROR_CODE;
            }

            return enumForGivenCode;      
	  }
	
}
