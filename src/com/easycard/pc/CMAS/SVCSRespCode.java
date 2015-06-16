package com.easycard.pc.CMAS;

import com.easycard.errormessage.IRespCode;



public enum SVCSRespCode implements IRespCode{

	_00("00","00交易成功",1),
	_04("04","04卡片禁用",1),
	_05("05","05卡片驗證碼錯誤",1),
	_12("12","12不合法交易",1),
	_13("13","13不合法交易金額",1),
	_14("14","14卡號不存在",1),
	_15("15","15不合法交易",1),
	_19("19","19交易重複",1),
	_25("25","25黑名單掛失卡",1),
	_28("28","28檔案無法查詢",1),
	_30("30","30格式檢查錯誤",1),
	_41("41","41遺失卡",1),
	_51("51","51額度不足",1),
	_54("54","54卡片過期",1),
	_57("57","57持卡人交易不被允許",1),
	_58("58","58端末機交易不被允許",1),
	_76("76","76找不到原始交易",1),
	_77("77","77與原交易內容不一致",1),
	_91("91","91Unable to forward to issueSwitch cus",1),
	_92("92","92Unable to route of this transaction",1),
	_93("93","93交易不被允許",1),
	_96("96","96系統發生異常",1),
	_N0("N0","N0Issus進入代行授權模式",1),
	_N3("N3","N3",1),
	_N4("N4","N4超過發生行代行授權條件",1),
	_Z3("Z3","Z3",1),
	_Q1("Q1","Q1信用卡授權失敗",1),
	_A0("A0","A0SVCS主機系統錯誤",1),
	_C0("C0","C0卡號不存在",1);
	
	private final String id;
	private final String msg;
	private final int actionId;  
	  
	
	SVCSRespCode(String id, String msg, int actionId)
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
