package com.easycard.pc.CMAS;

import com.easycard.errormessage.IRespCode;


public enum CmasRespCode implements IRespCode{
	_00("00","00訊息處理成功",1),
	_03("03","不合法的端末設備",1),
	_04("04","卡片禁用",1),
	_05("05","卡片驗證碼錯誤",1),
	_06("06","SVCS拒絕交易",1),
	_12("12","交易不合法",1),
	_13("13","金額不合法",1),
	_14("14","卡號不合法",1),
	_15("15","無此發卡業者",1),
	_19("19","交易重複",1),
	_25("25","無法取得資料",1),
	_30("30","訊息格式檢察錯誤",1),
	_41("41","遺失卡",1),
	_51("51","額度不足",1),
	_54("54","卡片過期",1),
	_55("55","無須展期",1),
	_57("57","持卡人交易不被允許",1),
	_58("58","端末設備交易不被允許",1),
	_59("59","端末設備不合法",1),
	_61("61","超過金額上限",1),
	_64("64","安全檢查錯誤",1),
	_65("65","超過次數上限",1),
	_76("76","無法找到原始交易",1),
	_77("77","交易內容不一致",1),
	_91("91","後端主機不在服務狀態",1),
	_92("92","SVCS or STANprocess timeout ",1),
	_96("96","系統發生異常或資料內容有誤",1),
	_97("97","Call DES error",1),
	_98("98","悠遊卡扣款失敗但auto-load成功",1),
	_99("99","讀寫檔案錯誤",1);
	
	
	private final String id;
	private final String msg;
	private final int actionId;  
	  

	CmasRespCode(String id, String msg, int actionId)
	{
		this.id = id;
		this.msg = msg;
		this.actionId = actionId;
	}
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public String getMsg() {
		// TODO Auto-generated method stub
		return this.msg;
	}

	@Override
	public int getActionId() {
		// TODO Auto-generated method stub
		return this.actionId;
	}
	
}
