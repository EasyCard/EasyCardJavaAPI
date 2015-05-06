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
	
	

	/*
	public String GetRespDescription() {
		int status = GetRespCode();
		
		switch (status) {
		case 0x9000:
			return "執行成功";
		// 以下為APDU格式錯誤-->交易終止, 請重新操作, 重新開機或報修	
		case 0x6001:
			return "CLA, INS ERROR";
		case 0x6002:
			return "p1, P2 ERROR";
		case 0x6003:
			return "LC, LE ERROR";
		case 0x6004:
			return "CHECK SUM ERROR";
		case 0x6005:
			return "DATA ERROR";
		case 0x6088:
			return "線路不良/Time Out";
		// 以下為票卡錯誤-->不正常卡, 拒絕交易
		case 0x6101:
			return "票卡不適用(AID Error)";
		case 0x6102:
			return "Issuer Code Error";
		case 0x6103:
			return "CPD Error";
		case 0x6104:
			return "未開卡支票卡/票卡狀態不符(Mifare卡)";
		case 0x6105:
			return "主要票值格式錯誤";
		case 0x6106:
			return "備份票值格式錯誤";
		case 0x6107:
			return "電子票值格式錯誤";
		case 0x6108:
			return "票卡過期";	
		case 0x6109:
			return "票卡已鎖卡";
		case 0x610A:
			return "票卡內檢核碼錯誤/或與0x640E同義";
		case 0x610B:
			return "Two FAT Error";
		case 0x610C:
			return "FAT Content Error";
		case 0x610D:
			return "卡片為預售(票卡起始日期未到)";
		case 0x610E:
			return "CTC或TSQN超出限制";
		case 0x610F:
			return "Level1 Mifare Locked(6999)";
		case 0x6111:
			return "為先正確執行CPU卡上一步驟或CPU卡驗證失敗";
		// RC531錯誤-->交易終止, 請重新操作
		case 0x6201:
			return "找不到卡片";
		case 0x6202:
			return "讀卡失敗";
		case 0x6203:
			return "寫卡失敗";
		case 0x6204:
			return "多張卡";
		case 0x6205:
			return "RC531 load key失敗"; 
		case 0x6206:
			return "RC531 Auth失敗";
		case 0x6207:
			return "CPU卡Record not found或超出範圍";
		case 0x6208:
			return "CPU卡Record不支援MAC或MAC錯誤";
		// SAM卡錯誤-->交易終止, 請重新開機
		case 0x6301:
			return "SAM認證失敗";
		case 0x6302:
			return "未先正確執行SAM上一步驟";
		case 0x6303:
			return "無SAM卡";
		case 0x6304:
			return "未先下PR_Reset/PPR_Reset";
		case 0x6305:
			return "未先下PR_SignOn/PPR_SignOn";
		case 0x6306:
			return "SAM卡加值額度(CA)不足";
		case 0x6307:
			return "舊SAM卡異常";
		case 0x6308:
			return "需再執行末端驗證以更新SAM參數";
		case 0x6309:
			return "新SAM卡異常";
		case 0x630A:
			return "新舊SAM卡皆異常";
		case 0x630B:
			return "STC超出限制";
		case 0x630C:
			return "SAM卡不支援該功能";
		// 交易錯誤-->拒絕交易
		case 0x6401:
			return "取消交易與上筆交易不符";
		case 0x6402:
			return "交易金額超過額度";
		case 0x6403:
			return "餘額不足";
		case 0x6404:
			return "卡號錯誤";
		case 0x6405:
			return "H_TAC內容認證錯誤";
		case 0x6406:
			return "API回應禁用名單/展期異動名單票卡";
		case 0x6407:
			return "非社福卡不須展期";
		case 0x6408:
			return "HOST判斷不須展期";
		case 0x6409:
			return "自動加值旗標未啟用";
		case 0x640A:
			return "票卡自動加值金額為零";
		case 0x640B:
			return "所查詢的該筆扣值Log資料不存在";
		case 0x640C:
			return "累積小額消費(購貨)金額超出日限額";
		case 0x640D:
			return "單次小額消費(購貨)金額超出次限額";
		case 0x640E:
			return "交易前餘額超出最後一次加值後餘額";
		case 0x640F:
			return "Reader累計加值金額超出額度管控限制";
		case 0x6410:
			return "票卡不適用(非普通卡)";
		case 0x6411:
			return "票卡押金不符(押金非100)";
		case 0x6412:
			return "超過通路卡交易額度(餘額>1000)";
		case 0x6413:
			return "製卡日未滿三個月";
		case 0x6414:
			return "卡片使用次數未滿五次";
		case 0x6415:
			return "需執行交易合法驗證";
		case 0x6416:
			return "自動加值旗標以啟用";
		case 0x6417:
			return "票卡已開卡";
		case 0x6418:
			return "票卡於此通路限制使用";
		case 0x6419:
			return "CPU卡票卡狀態不適用此交易";
		case 0x641A:
			return "PPR_SignOn檢核碼(EDC)錯誤";
		case 0x641B:
			return "交易欄位資料不適用此交易(CPU卡)";
		case 0x641C:
			return "交易欄位資料不適用此交易(SAM卡)";
		case 0x641D:
			return "超出離線自動加值日線次";
		case 0x641E:
			return "VAR-押碼檢查失敗";
		// 安全錯誤-->請報修
		case 0x6501:
			return "Key Locked";
		case 0x6502:
			return "Key No Initial";
		case 0x6503:
			return "RAM Error";
		case 0x6504:
			return "Flash Failed";
		case 0x6505:
			return "SAM使用次數已達上限";
		case 0x6506:
			return "SAM使用次數異常/有誤";
		case 0x6507:
			return "CPU卡TXN Key Selection Error";
		case 0x6508:
			return "CPU卡Signature Key Selection Error";
		case 0x6509:
			return "SAM卡Key Selection Error";
		case 0x65FF:
			return "線路遭到攻擊破壞";
		}
		
		return "未知的錯誤";
	}
	*/
	
}
