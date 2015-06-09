package CMAS;

import java.util.ArrayList;

//因為需要使用到reflect的功能，所以所有的成員宣告成public
//但這樣容易在外面直接更改變數值
//因此在CmasDataSpec中的CmasTag宣告成private
//再利用public 的function去存取CmasTag的成員
public class CmasTag {

	public class SubTag5588{
		private String t558801="";
		private String t558802="";
		private String t558803="";
		
		public String getT558801() {
			return t558801;
		}
		public void setT558801(String t558801) {
			this.t558801 = t558801;
		}
		public String getT558802() {
			return t558802;
		}
		public void setT558802(String t558802) {
			this.t558802 = t558802;
		}
		public String getT558803() {
			return t558803;
		}
		public void setT558803(String t558803) {
			this.t558803 = t558803;
		}
	}
	
	public class SubTag5595{
		private String t559501="";
		private String t559502="";
		private String t559503="";
		private String t559504="";
		private String t559505="";
		public String getT559501() {
			return t559501;
		}
		public void setT559501(String t559501) {
			this.t559501 = t559501;
		}
		public String getT559502() {
			return t559502;
		}
		public void setT559502(String t559502) {
			this.t559502 = t559502;
		}
		public String getT559503() {
			return t559503;
		}
		public void setT559503(String t559503) {
			this.t559503 = t559503;
		}
		public String getT559504() {
			return t559504;
		}
		public void setT559504(String t559504) {
			this.t559504 = t559504;
		}
		public String getT559505() {
			return t559505;
		}
		public void setT559505(String t559505) {
			this.t559505 = t559505;
		}
	}
	
	public class SubTag5596{
		private String t559601="00000000";
		private String t559602="00000000";
		private String t559603="00000000";
		private String t559604="00000000";
		public String getT559601() {
			return t559601;
		}
		public void setT559601(String t559601) {
			this.t559601 = t559601;
		}
		public String getT559602() {
			return t559602;
		}
		public void setT559602(String t559602) {
			this.t559602 = t559602;
		}
		public String getT559603() {
			return t559603;
		}
		public void setT559603(String t559603) {
			this.t559603 = t559603;
		}
		public String getT559604() {
			return t559604;
		}
		public void setT559604(String t559604) {
			this.t559604 = t559604;
		}		
	}
	
	public class SubTag6002{
		private String oneDayQuotaFlag="";
		private String oneDayQuota="";
		private String onceQuotaFlag="";
		private String onceQuota="";
		private String checkEVFlag="";
		private String addQuotaFlag="";
		private String addQuota="";
		private String checkDeductFlag="";// reader spec. was 'checkDebigFlag'
		private String checkDeductValue="";
		private String deductLimitFlag="";//reader spec. was 'Merchant Limit Use For Micro Payment'
		private String apiVersion="";
		private String RFU="";
		
		public String getOneDayQuotaFlag() {
			
			//logger.info("getter:"+oneDayQuotaFlag);
			return oneDayQuotaFlag;
		}
		public void setOneDayQuotaFlag(String oneDayQuotaFlag) {
			this.oneDayQuotaFlag = oneDayQuotaFlag;
		}
		public String getOneDayQuota() {
			return oneDayQuota;
		}
		public void setOneDayQuota(String oneDayQuota) {
			this.oneDayQuota = oneDayQuota;
		}
		public String getOnceQuotaFlag() {
			return onceQuotaFlag;
		}
		public void setOnceQuotaFlag(String onceQuotaFlag) {
			this.onceQuotaFlag = onceQuotaFlag;
		}
		public String getOnceQuota() {
			return onceQuota;
		}
		public void setOnceQuota(String onceQuota) {
			this.onceQuota = onceQuota;
		}
		public String getCheckEVFlag() {
			return checkEVFlag;
		}
		public void setCheckEVFlag(String checkEVFlag) {
			this.checkEVFlag = checkEVFlag;
		}
		public String getAddQuotaFlag() {
			return addQuotaFlag;
		}
		public void setAddQuotaFlag(String addQuotaFlag) {
			this.addQuotaFlag = addQuotaFlag;
		}
		public String getAddQuota() {
			return addQuota;
		}
		public void setAddQuota(String addQuota) {
			this.addQuota = addQuota;
		}
		public String getCheckDeductFlag() {
			return checkDeductFlag;
		}
		public void setCheckDeductFlag(String checkDeductFlag) {
			this.checkDeductFlag = checkDeductFlag;
		}
		public String getCheckDeductValue() {
			return checkDeductValue;
		}
		public void setCheckDeductValue(String checkDeductValue) {
			this.checkDeductValue = checkDeductValue;
		}
		public String getDeductLimitFlag() {
			return deductLimitFlag;
		}
		public void setDeductLimitFlag(String deductLimitFlag) {
			this.deductLimitFlag = deductLimitFlag;
		}
		public String getApiVersion() {
			return apiVersion;
		}
		public void setApiVersion(String apiVersion) {
			this.apiVersion = apiVersion;
		}
		public String getRFU() {
			return RFU;
		}
		public void setRFU(String rFU) {
			RFU = rFU;
		}
	}
	
		public String t0100="";
		public String t0200="";
		public String t0211="";
		public String t0212="";
		public String t0213="";
		public String t0214="";
		public String t0300="";
		public String t0301="";
		public String t0400="";
		public String t0403="";
		public String t0407="";
		public String t0408="";
		public String t0409="";
		public String t0410="";
		public String t0700="";
		public String t1100="";
		public String t1101="";
		public String t1200="";
		public String t1201="";
		public String t1300="";
		public String t1301="";
		public String t1400="";
		public String t1402="";
		public String t1403="";
		public String t3700="";
		public String t3701="";
		public String t3800="";	
		public String t3900="";
		public String t3901="";
		public String t3902="";
		public String t3903="";
		
		public String t3904="";
		public String t3911="";
		public String t4100="";
		public String t4101="";
		public String t4102="";
		public String t4103="";
		public String t4104="";
		public String t4200="";
		public String t4201="";
		public String t4202="";
		public String t4203="";
		
		public String t4204="";
		public String t4205="";
		public String t4206="";
		public String t4207="";
		public String t4208="";
		public String t4209="";
		public String t4210="";
		public String t4800="";
		public String t4801="";
		public String t4802="";
		public String t4803="";
		public String t4804="";
		public String t4805="";
		public String t4806="";
		public String t4807="";
		public String t4808="";
		
		public String t4809="";
		public String t4810="";
		
		public String t4811="";
		public String t4812="";
		public String t4813="";
		public String t4814="";
		public String t4815="";
		public String t4816="";
		public String t4817="";
		public String t4818="";
		public String t4819="";
		
		public String t4820="";
		public String t4821="";
		public String t4823="";
		public String t4824="";
		public String t4825="";
		public String t4826="";
		public String t4827="";
		public String t4828="";
		public String t4829="";	
		public String t4830="";
		
		public String t5301="";
		public String t5302="";
		public String t5303="";
		public String t5304="";
		public String t5305="";
		public String t5306="";
		public String t5307="";
		public String t5308="";
		public String t5361="";
		public String t5362="";
		
		public String t5363="";
		public String t5364="";
		public String t5365="";
		public String t5366="";
		public String t5367="00"+"00000000000000000000000000000000000000000000000000000000000000000000000000000000"+"00000000000000000000000000000000"; //default value was '00', because maybe no needed to update sam, host would not response t5367, that's a cmas bug. waitting 環友 to fix it
		public String t5368="";
		public String t5369="";
		
		public String t5370="";
		public String t5371="";
		
		public String t5501="";
		public String t5503="";
		public String t5504="";
		public String t5509="";
		
		public String t5510="";
		public String t5548="";
		public String t5550="";
		public String t5581="";
		public String t5582="";
		public String t5583="";
		
		
		public ArrayList<SubTag5588> t5588s = new ArrayList<SubTag5588>();
		
		public String t5589="";
		public String t5590="";
		public String t5591="";
		public String t5592="";
		public String t5593="";
		public String t5594="";

		public ArrayList<SubTag5595> t5595s = new ArrayList<SubTag5595>();
		
		public SubTag5596 t5596 = new SubTag5596();
		
		public String t5597="";
		public String t5599="";
		
		
		public String t6000="";
		public String t6001="";
		//public String t6002="";
		public SubTag6002 t6002 = new SubTag6002();
		public String t6003="";
		public String t6004="";
		
		public String t6400="";
		public String t6401="";
		public String t6402="";
		public String t6403="";
		public String t6404="";
		public String t6405="";
		public String t6406="";
		public String t6407="";
		public String t6408="";
		public String t6409="";
	
		
		
		public Object getTagValue(int tag){
			String tagName = String.format("t%04d", tag);
			Object tagValue = null;
			try {			
				tagValue  = this.getClass().getField(tagName).get(this);					
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return tagValue;
		}
		
		
		public void setTagValue(int tag, Object value){
			String tagName = String.format("t%04d", tag);
			
			try {			
				this.getClass().getField(tagName).set(this, value);					
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		
}
