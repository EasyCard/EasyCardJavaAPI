package CMAS;


import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class CmasDataSpec {

	static Logger logger = Logger.getLogger(CmasDataSpec.class);
	
	public CmasDataSpec(){/*Empty dataSpec*/}
	public CmasDataSpec(String cmasResp){
		/*parse hostCMAS Response data to cmasDataSpec*/
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();	       
		DocumentBuilder db = null;
		try {
				
			db = dbf.newDocumentBuilder();
		
	        InputSource is = new InputSource();
	        is.setCharacterStream(new StringReader(cmasResp));

	        Document doc = null;
	        doc = db.parse(is);
			        
	        NodeList root = doc.getElementsByTagName("Trans");
	        	   
	        getTag(root.item(0));
	        	   
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        
	}
	private void getTag(Node node) {
	    // do something with the current node instead of System.out
	    System.out.println(node.getNodeName());

	    NodeList nodeList = node.getChildNodes();
	    for (int i = 0; i < nodeList.getLength(); i++) {
	        Node currentNode = nodeList.item(i);
	        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
	            //calls this method for all the children which is Element
	        	logger.debug("tag:"+Integer.valueOf(currentNode.getNodeName().replaceAll("T", "0")));
	        	//getTag(currentNode);
	        	//tagParser(Integer.valueOf(currentNode.getNodeName().replaceAll("T", "0")), currentNode.getTextContent());
	        	tagParser(currentNode);
	        }
	    }
	}
	
	
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
			
			logger.info("getter:"+oneDayQuotaFlag);
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
	
	
	private String t0100="";
	private String t0200="";
	private String t0211="";
	private String t0212="";
	private String t0213="";
	private String t0214="";
	private String t0300="";
	private String t0301="";
	private String t0400="";
	private String t0403="";
	private String t0407="";
	private String t0408="";
	private String t0409="";
	private String t0410="";
	private String t0700="";
	private String t1100="";
	private String t1101="";
	private String t1200="";
	private String t1201="";
	private String t1300="";
	private String t1301="";
	private String t1400="";
	private String t1402="";
	private String t1403="";
	private String t3700="";
	private String t3701="";
	private String t3800="";	
	private String t3900="";
	private String t3901="";
	private String t3902="";
	private String t3903="";
	
	private String t3904="";
	private String t3911="";
	private String t4100="";
	private String t4101="";
	private String t4102="";
	private String t4103="";
	private String t4104="";
	private String t4200="";
	private String t4201="";
	private String t4202="";
	private String t4203="";
	
	private String t4204="";
	private String t4205="";
	private String t4206="";
	private String t4207="";
	private String t4208="";
	private String t4209="";
	private String t4210="";
	private String t4800="";
	private String t4801="";
	private String t4802="";
	private String t4803="";
	private String t4804="";
	private String t4805="";
	private String t4806="";
	private String t4807="";
	private String t4808="";
	
	private String t4809="";
	private String t4810="";
	
	private String t4811="";
	private String t4812="";
	private String t4813="";
	private String t4814="";
	private String t4815="";
	private String t4816="";
	private String t4817="";
	private String t4818="";
	private String t4819="";
	
	private String t4820="";
	private String t4821="";
	private String t4823="";
	private String t4824="";
	private String t4825="";
	private String t4826="";
	private String t4827="";
	private String t4828="";
	private String t4829="";	
	private String t4830="";
	
	private String t5301="";
	private String t5302="";
	private String t5303="";
	private String t5304="";
	private String t5305="";
	private String t5306="";
	private String t5307="";
	private String t5308="";
	private String t5361="";
	private String t5362="";
	
	private String t5363="";
	private String t5364="";
	private String t5365="";
	private String t5366="";
	private String t5367="00"+"00000000000000000000000000000000000000000000000000000000000000000000000000000000"+"00000000000000000000000000000000"; //default value was '00', because maybe no needed t oupdate sam, host would not response t5367, that's a cmas bug. waitting 環友 to fix it
	private String t5368="";
	private String t5369="";
	
	private String t5370="";
	private String t5371="";
	
	private String t5501="";
	private String t5503="";
	private String t5504="";
	private String t5509="";
	
	private String t5510="";
	private String t5548="";
	private String t5550="";
	private String t5581="";
	private String t5582="";
	private String t5583="";
	//public String t5588="";
	//private SubTag5588 []tag5588;
	//private SubTag5588 []subTag5588 = new SubTag5588[5];	//01 : SSL版本
														//02 : 黑名單版本
														//03 : 端末設備程式版本
														//04 : 端末設備參數版本
														//05 : Dongle程式版本

	
	private ArrayList<SubTag5588> t5588s = new ArrayList<SubTag5588>();
	
	private String t5589="";
	private String t5590="";
	private String t5591="";
	private String t5592="";
	private String t5593="";
	private String t5594="";

	private ArrayList<SubTag5595> t5595s = new ArrayList<SubTag5595>();
	
	private SubTag5596 t5596 = new SubTag5596();
	
	private String t5597="";
	private String t5599="";
	
	
	private String t6000="";
	private String t6001="";
	//private String t6002="";
	private SubTag6002 t6002 = new SubTag6002();
	private String t6003="";
	private String t6004="";
	
	private String t6400="";
	private String t6401="";
	private String t6402="";
	private String t6403="";
	private String t6404="";
	private String t6405="";
	private String t6406="";
	private String t6407="";
	private String t6408="";
	private String t6409="";
	
	
	
	public String getT0100() {
		return t0100;
	}
	public void setT0100(String t0100) {
		this.t0100 = t0100;
	}
	public String getT0200() {
		return t0200;
	}
	public void setT0200(String t0200) {
		this.t0200 = t0200;
	}
	public String getT0211() {
		return t0211;
	}
	public void setT0211(String t0211) {
		this.t0211 = t0211;
	}
	public String getT0212() {
		return t0212;
	}
	public void setT0212(String t0212) {
		this.t0212 = t0212;
	}
	public String getT0213() {
		return t0213;
	}
	public void setT0213(String t0213) {
		this.t0213 = t0213;
	}
	public String getT0214() {
		return t0214;
	}
	public void setT0214(String t0214) {
		this.t0214 = t0214;
	}
	public String getT0300() {
		return t0300;
	}
	public void setT0300(String t0300) {
		this.t0300 = t0300;
	}
	public String getT0301() {
		return t0301;
	}
	public void setT0301(String t0301) {
		this.t0301 = t0301;
	}
	public String getT0400() {
		return t0400;
	}
	public void setT0400(String t0400) {
		this.t0400 = t0400;
	}
	public String getT0403() {
		return t0403;
	}
	public void setT0403(String t0403) {
		this.t0403 = t0403;
	}
	public String getT0407() {
		return t0407;
	}
	public void setT0407(String t0407) {
		this.t0407 = t0407;
	}
	public String getT0408() {
		return t0408;
	}
	public void setT0408(String t0408) {
		this.t0408 = t0408;
	}
	public String getT0409() {
		return t0409;
	}
	public void setT0409(String t0409) {
		this.t0409 = t0409;
	}
	public String getT0410() {
		return t0410;
	}
	public void setT0410(String t0410) {
		this.t0410 = t0410;
	}
	public String getT0700() {
		return t0700;
	}
	public void setT0700(String t0700) {
		this.t0700 = t0700;
	}
	public String getT1100() {
		return t1100;
	}
	public void setT1100(String t1100) {
		this.t1100 = t1100;
	}
	public String getT1101() {
		return t1101;
	}
	public void setT1101(String t1101) {
		this.t1101 = t1101;
	}
	public String getT1200() {
		return t1200;
	}
	public void setT1200(String t1200) {
		this.t1200 = t1200;
	}
	public String getT1201() {
		return t1201;
	}
	public void setT1201(String t1201) {
		this.t1201 = t1201;
	}
	public String getT1300() {
		return t1300;
	}
	public void setT1300(String t1300) {
		this.t1300 = t1300;
	}
	/**
	 * @return the t1301
	 */
	public String getT1301() {
		return t1301;
	}
	/**
	 * @param t1301 the t1301 to set
	 */
	public void setT1301(String t1301) {
		this.t1301 = t1301;
	}
	public String getT1400() {
		return t1400;
	}
	public void setT1400(String t1400) {
		this.t1400 = t1400;
	}
	public String getT1402() {
		return t1402;
	}
	public void setT1402(String t1402) {
		this.t1402 = t1402;
	}
	public String getT1403() {
		return t1403;
	}
	public void setT1403(String t1403) {
		this.t1403 = t1403;
	}
	public String getT3700() {
		return t3700;
	}
	public void setT3700(String t3700) {
		this.t3700 = t3700;
	}
	public String getT3701() {
		return t3701;
	}
	public void setT3701(String t3701) {
		this.t3701 = t3701;
	}
	public String getT3800() {
		return t3800;
	}
	public void setT3800(String t3800) {
		this.t3800 = t3800;
	}
	public String getT3900() {
		return t3900;
	}
	public void setT3900(String t3900) {
		this.t3900 = t3900;
	}
	public String getT3901() {
		return t3901;
	}
	public void setT3901(String t3901) {
		this.t3901 = t3901;
	}
	public String getT3902() {
		return t3902;
	}
	public void setT3902(String t3902) {
		this.t3902 = t3902;
	}
	public String getT3903() {
		return t3903;
	}
	public void setT3903(String t3903) {
		this.t3903 = t3903;
	}
	public String getT3904() {
		return t3904;
	}
	public void setT3904(String t3904) {
		this.t3904 = t3904;
	}
	public String getT3911() {
		return t3911;
	}
	public void setT3911(String t3911) {
		this.t3911 = t3911;
	}
	public String getT4100() {
		return t4100;
	}
	public void setT4100(String t4100) {
		this.t4100 = t4100;
	}
	public String getT4101() {
		return t4101;
	}
	public void setT4101(String t4101) {
		this.t4101 = t4101;
	}
	public String getT4102() {
		return t4102;
	}
	public void setT4102(String t4102) {
		this.t4102 = t4102;
	}
	public String getT4103() {
		return t4103;
	}
	public void setT4103(String t4103) {
		this.t4103 = t4103;
	}
	public String getT4104() {
		return t4104;
	}
	public void setT4104(String t4104) {
		this.t4104 = t4104;
	}
	public String getT4200() {
		return t4200;
	}
	public void setT4200(String t4200) {
		this.t4200 = t4200;
	}
	public String getT4201() {
		return t4201;
	}
	public void setT4201(String t4201) {
		this.t4201 = t4201;
	}
	public String getT4202() {
		return t4202;
	}
	public void setT4202(String t4202) {
		this.t4202 = t4202;
	}
	public String getT4203() {
		return t4203;
	}
	public void setT4203(String t4203) {
		this.t4203 = t4203;
	}
	public String getT4204() {
		return t4204;
	}
	public void setT4204(String t4204) {
		this.t4204 = t4204;
	}
	public String getT4205() {
		return t4205;
	}
	public void setT4205(String t4205) {
		this.t4205 = t4205;
	}
	public String getT4206() {
		return t4206;
	}
	public void setT4206(String t4206) {
		this.t4206 = t4206;
	}
	public String getT4207() {
		return t4207;
	}
	public void setT4207(String t4207) {
		this.t4207 = t4207;
	}
	public String getT4208() {
		return t4208;
	}
	public void setT4208(String t4208) {
		this.t4208 = t4208;
	}
	public String getT4209() {
		return t4209;
	}
	public void setT4209(String t4209) {
		this.t4209 = t4209;
	}
	public String getT4210() {
		return t4210;
	}
	public void setT4210(String t4210) {
		this.t4210 = t4210;
	}
	public String getT4800() {
		return t4800;
	}
	public void setT4800(String t4800) {
		this.t4800 = t4800;
	}
	public String getT4801() {
		return t4801;
	}
	public void setT4801(String t4801) {
		this.t4801 = t4801;
	}
	
	public enum Issuer {
		easycard,
		keelung,
		unknown
	};
	public String getT4802() {
		return t4802;
	}
	public void setT4802(String t4802) {
		this.t4802 = t4802;
	}
	public String getT4803() {
		return t4803;
	}
	public void setT4803(String t4803) {
		this.t4803 = t4803;
	}
	public String getT4804() {
		return t4804;
	}
	public void setT4804(String t4804) {
		this.t4804 = t4804;
	}
	public String getT4805() {
		return t4805;
	}
	public void setT4805(String t4805) {
		this.t4805 = t4805;
	}
	public String getT4806() {
		return t4806;
	}
	public void setT4806(String t4806) {
		this.t4806 = t4806;
	}
	public String getT4807() {
		return t4807;
	}
	public void setT4807(String t4807) {
		this.t4807 = t4807;
	}
	public String getT4808() {
		return t4808;
	}
	public void setT4808(String t4808) {
		this.t4808 = t4808;
	}
	public String getT4809() {
		return t4809;
	}
	public void setT4809(String t4809) {
		this.t4809 = t4809;
	}
	public String getT4810() {
		return t4810;
	}
	public void setT4810(String t4810) {
		this.t4810 = t4810;
	}
	public String getT4811() {
		return t4811;
	}
	public void setT4811(String t4811) {
		this.t4811 = t4811;
	}
	public String getT4812() {
		return t4812;
	}
	public void setT4812(String t4812) {
		this.t4812 = t4812;
	}
	public String getT4813() {
		return t4813;
	}
	public void setT4813(String t4813) {
		this.t4813 = t4813;
	}
	public String getT4814() {
		return t4814;
	}
	public void setT4814(String t4814) {
		this.t4814 = t4814;
	}
	public String getT4815() {
		return t4815;
	}
	public void setT4815(String t4815) {
		this.t4815 = t4815;
	}
	public String getT4816() {
		return t4816;
	}
	public void setT4816(String t4816) {
		this.t4816 = t4816;
	}
	public String getT4817() {
		return t4817;
	}
	public void setT4817(String t4817) {
		this.t4817 = t4817;
	}
	public String getT4818() {
		return t4818;
	}
	public void setT4818(String t4818) {
		this.t4818 = t4818;
	}
	public String getT4819() {
		return t4819;
	}
	public void setT4819(String t4819) {
		this.t4819 = t4819;
	}
	public String getT4820() {
		return t4820;
	}
	public void setT4820(String t4820) {
		this.t4820 = t4820;
	}
	public String getT4821() {
		return t4821;
	}
	public void setT4821(String t4821) {
		this.t4821 = t4821;
	}
	public String getT4823() {
		return t4823;
	}
	public void setT4823(String t4823) {
		this.t4823 = t4823;
	}
	public String getT4824() {
		return t4824;
	}
	public void setT4824(String t4824) {
		this.t4824 = t4824;
	}
	public String getT4825() {
		return t4825;
	}
	public void setT4825(String t4825) {
		this.t4825 = t4825;
	}
	public String getT4826() {
		return t4826;
	}
	public void setT4826(String t4826) {
		this.t4826 = t4826;
	}
	public String getT4827() {
		return t4827;
	}
	public void setT4827(String t4827) {
		this.t4827 = t4827;
	}
	public String getT4828() {
		return t4828;
	}
	public void setT4828(String t4828) {
		this.t4828 = t4828;
	}
	public String getT4829() {
		return t4829;
	}
	public void setT4829(String t4829) {
		this.t4829 = t4829;
	}
	public String getT4830() {
		return t4830;
	}
	public void setT4830(String t4830) {
		this.t4830 = t4830;
	}
	public String getT5301() {
		return t5301;
	}
	public void setT5301(String t5301) {
		this.t5301 = t5301;
	}
	public String getT5302() {
		return t5302;
	}
	public void setT5302(String t5302) {
		this.t5302 = t5302;
	}
	public String getT5303() {
		return t5303;
	}
	public void setT5303(String t5303) {
		this.t5303 = t5303;
	}
	public String getT5304() {
		return t5304;
	}
	public void setT5304(String t5304) {
		this.t5304 = t5304;
	}
	public String getT5305() {
		return t5305;
	}
	public void setT5305(String t5305) {
		this.t5305 = t5305;
	}
	public String getT5306() {
		return t5306;
	}
	public void setT5306(String t5306) {
		this.t5306 = t5306;
	}
	public String getT5307() {
		return t5307;
	}
	public void setT5307(String t5307) {
		this.t5307 = t5307;
	}
	public String getT5308() {
		return t5308;
	}
	public void setT5308(String t5308) {
		this.t5308 = t5308;
	}
	public String getT5361() {
		return t5361;
	}
	public void setT5361(String t5361) {
		this.t5361 = t5361;
	}
	public String getT5362() {
		return t5362;
	}
	public void setT5362(String t5362) {
		this.t5362 = t5362;
	}
	public String getT5363() {
		return t5363;
	}
	public void setT5363(String t5363) {
		this.t5363 = t5363;
	}
	public String getT5364() {
		return t5364;
	}
	public void setT5364(String t5364) {
		this.t5364 = t5364;
	}
	public String getT5365() {
		return t5365;
	}
	public void setT5365(String t5365) {
		this.t5365 = t5365;
	}
	public String getT5366() {
		return t5366;
	}
	public void setT5366(String t5366) {
		this.t5366 = t5366;
	}
	public String getT5367() {
		return t5367;
	}
	public void setT5367(String t5367) {
		this.t5367 = t5367;
	}
	public String getT5368() {
		return t5368;
	}
	public void setT5368(String t5368) {
		this.t5368 = t5368;
	}
	public String getT5369() {
		return t5369;
	}
	public void setT5369(String t5369) {
		this.t5369 = t5369;
	}
	public String getT5370() {
		return t5370;
	}
	public void setT5370(String t5370) {
		this.t5370 = t5370;
	}
	public String getT5371() {
		return t5371;
	}
	public void setT5371(String t5371) {
		this.t5371 = t5371;
	}
	public String getT5501() {
		return t5501;
	}
	public void setT5501(String t5501) {
		this.t5501 = t5501;
	}
	public String getT5503() {
		return t5503;
	}
	public void setT5503(String t5503) {
		this.t5503 = t5503;
	}
	public String getT5504() {
		return t5504;
	}
	public void setT5504(String t5504) {
		this.t5504 = t5504;
	}
	public String getT5509() {
		return t5509;
	}
	public void setT5509(String t5509) {
		this.t5509 = t5509;
	}
	public String getT5510() {
		return t5510;
	}
	public void setT5510(String t5510) {
		this.t5510 = t5510;
	}
	public String getT5548() {
		return t5548;
	}
	public void setT5548(String t5548) {
		this.t5548 = t5548;
	}
	public String getT5550() {
		return t5550;
	}
	public void setT5550(String t5550) {
		this.t5550 = t5550;
	}
	public String getT5581() {
		return t5581;
	}
	public void setT5581(String t5581) {
		this.t5581 = t5581;
	}
	public String getT5582() {
		return t5582;
	}
	public void setT5582(String t5582) {
		this.t5582 = t5582;
	}
	public String getT5583() {
		return t5583;
	}
	public void setT5583(String t5583) {
		this.t5583 = t5583;
	}
	
	public ArrayList<SubTag5588> getT5588s() {
		return t5588s;
	}
	
	public void setT5588s(SubTag5588 t5588) {
		this.t5588s.add(t5588);
	}
	
	public String getT5589() {
		return t5589;
	}
	public void setT5589(String t5589) {
		this.t5589 = t5589;
	}
	public String getT5590() {
		return t5590;
	}
	public void setT5590(String t5590) {
		this.t5590 = t5590;
	}
	public String getT5591() {
		return t5591;
	}
	public void setT5591(String t5591) {
		this.t5591 = t5591;
	}
	public String getT5592() {
		return t5592;
	}
	public void setT5592(String t5592) {
		this.t5592 = t5592;
	}
	public String getT5593() {
		return t5593;
	}
	public void setT5593(String t5593) {
		this.t5593 = t5593;
	}
	public String getT5594() {
		return t5594;
	}
	public void setT5594(String t5594) {
		this.t5594 = t5594;
	}
	public ArrayList<SubTag5595> getT5595s() {
		return t5595s;
	}
	public void setT5595(SubTag5595 t5595) {
		this.t5595s.add(t5595);
	}
	public SubTag5596 getT5596() {
		return t5596;
	}
	public void setT5596(SubTag5596 t5596) {
		this.t5596 = t5596;
	}
	public String getT5597() {
		return t5597;
	}
	public void setT5597(String t5597) {
		this.t5597 = t5597;
	}
	public String getT5599() {
		return t5599;
	}
	public void setT5599(String t5599) {
		this.t5599 = t5599;
	}
	public String getT6000() {
		return t6000;
	}
	public void setT6000(String t6000) {
		this.t6000 = t6000;
	}
	public String getT6001() {
		return t6001;
	}
	public void setT6001(String t6001) {
		this.t6001 = t6001;
	}
	public SubTag6002 getT6002() {
		return t6002;
	}
	
	/*
	public void setT6002(SubTag6002 t6002) {
		this.t6002 = t6002;
	}
	*/
	public String getT6003() {
		return t6003;
	}
	public void setT6003(String t6003) {
		this.t6003 = t6003;
	}
	public String getT6004() {
		return t6004;
	}
	public void setT6004(String t6004) {
		this.t6004 = t6004;
	}
	public String getT6400() {
		return t6400;
	}
	public void setT6400(String t6400) {
		this.t6400 = t6400;
	}
	public String getT6401() {
		logger.info("getter:"+t6401);
		return t6401;
	}
	public void setT6401(String t6401) {
		this.t6401 = t6401;
	}
	public String getT6402() {
		return t6402;
	}
	public void setT6402(String t6402) {
		this.t6402 = t6402;
	}
	public String getT6403() {
		return t6403;
	}
	public void setT6403(String t6403) {
		this.t6403 = t6403;
	}
	public String getT6404() {
		return t6404;
	}
	public void setT6404(String t6404) {
		this.t6404 = t6404;
	}
	public String getT6405() {
		return t6405;
	}
	public void setT6405(String t6405) {
		this.t6405 = t6405;
	}
	public String getT6406() {
		return t6406;
	}
	public void setT6406(String t6406) {
		this.t6406 = t6406;
	}
	public String getT6407() {
		return t6407;
	}
	public void setT6407(String t6407) {
		this.t6407 = t6407;
	}
	public String getT6408() {
		return t6408;
	}
	public void setT6408(String t6408) {
		this.t6408 = t6408;
	}
	public String getT6409() {
		logger.info("getter:"+t6409);
		return t6409;
	}
	public void setT6409(String t6409) {
		this.t6409 = t6409;
	}
	
	//private void tagParser(int tag, String text){
	private void tagParser(Node node){
		
		int tag = Integer.valueOf(node.getNodeName().replaceAll("T", "0"));
		String text = node.getTextContent();
		//String result = "";
		//String start = "<T"+String.format("%04d", tag)+">";
		//String end = "</T"+String.format("%04d", tag)+">";
		switch(tag)
		{
			case 100:
				setT0100(text);
				break;
			
			case 200:
				setT0200(text);
				break;
				
			case 300:
				setT0300(text);
				break;
				
			case 400:
				setT0400(text);
				break;
			
			case 403:
				setT0403(text);
				break;
			
			case 407:
				setT0403(text);
				break;
			
			case 408:
				setT0408(text);
				break;
				
			case 409:
				setT0409(text);
				break;	
				
			case 410:
				setT0410(text);
				break;	
				
			case 1100:
				setT1100(text);
				break;
			case 1101:
				setT1101(text);
				break;
			case 1200:
				setT1200(text);
				break;
			case 1201:
				setT1201(text);
				break;	
				
			case 1300:
				setT1300(text);
				break;
			case 1301:
				setT1301(text);
				break;
			case 3700:
				setT3700(text);
				break;	
				
			case 3800:
				setT3800(text);
				break;
				
				
			case 3900:
				setT3900(text);
				break;		
		
			case 4100:
				setT4100(text);
				break;
			case 4101:
				setT4101(text);
				break;
			case 4102:
				setT4102(text);
				break;
			case 4103:
				setT4103(text);
				break;
			case 4104:
				setT4104(text);
				break;
			case 4200:
				setT4200(text);
				break;
			case 4201:
				setT4201(text);
				break;
			case 4210:
				setT4210(text);
				break;
			
			case 4802:
				setT4802(text);
				break;
	
			case 4813:
				setT4813(text);				
				break;
				
			case 4814:
				setT4814(text);				
				break;
				
			case 4815:
				setT4815(text);				
				break;				
				
			case 4816:
				setT4816(text);				
				break;
				
			case 4817:
				setT4817(text);				
				break;		
				
			case 4820:
				setT4820(text);
				break;
			case 4823:
				setT4823(text);
				break;	
			case 4824:
				setT4824(text);
				break;
			case 5301:
				setT5301(text);
				break;
			
			case 5303:
				setT5303(text);
				break;
			
			case 5306:
				setT5306(text);
				break;	
				
				
			case 5307:
				setT5307(text);
				break;
			case 5308:
				setT5308(text);
				break;
			case 5361:
				setT5361(text);
				break;
			case 5362:
				setT5362(text);
				break;
			case 5363:
				setT5363(text);
				break;
			case 5364:
				setT5364(text);
				break;
			case 5365:
				setT5365(text);
				break;
			case 5366:
				setT5366(text);
				break;
			case 5368:
				setT5368(text);
				break;
			case 5369:
				setT5369(text);
				break;
			case 5370:
				setT5370(text);
				break;
			case 5371:
				setT5371(text);
				break;	
			case 5501:
				setT5501(text);
				break;	
			case 5503:
				setT5503(text);
				break;	
			case 5504:
				setT5504(text);
				break;	
			case 5510:
				setT5510(text);
				break;	
				
			
			case 5588:
				SubTag5588 t5588 = new SubTag5588();
				NodeList childs5588 = node.getChildNodes();
				Node child5588=null;
				for(int i=0; i<childs5588.getLength(); i++)
				{
					child5588 = childs5588.item(i);
					if(child5588.getNodeType() == Node.ELEMENT_NODE)
					{
						if(child5588.getNodeName().equalsIgnoreCase("T558801")) t5588.setT558801(child5588.getTextContent());
						if(child5588.getNodeName().equalsIgnoreCase("T558802")) t5588.setT558802(child5588.getTextContent());
						if(child5588.getNodeName().equalsIgnoreCase("T558803")) t5588.setT558803(child5588.getTextContent());
					}
				}				
				t5588s.add(t5588);				
				break;	
				
			case 5591:
				setT5591(text);
				break;	
				
		
			case 5595:
				SubTag5595 t5595 = new SubTag5595();
				NodeList childs5595 = node.getChildNodes();
				Node child5595=null;
				for(int i=0; i<childs5595.getLength(); i++)
				{
					child5595 = childs5595.item(i);
					if(child5595.getNodeType() == Node.ELEMENT_NODE)
					{
						logger.debug("5595 value:"+child5595.getTextContent());
						if(child5595.getNodeName().equalsIgnoreCase("T559501")) t5595.setT559501(child5595.getTextContent());
						if(child5595.getNodeName().equalsIgnoreCase("T559502")) t5595.setT559502(child5595.getTextContent());
						if(child5595.getNodeName().equalsIgnoreCase("T559503")) t5595.setT559503(child5595.getTextContent());
						if(child5595.getNodeName().equalsIgnoreCase("T559504")) t5595.setT559504(child5595.getTextContent());
						if(child5595.getNodeName().equalsIgnoreCase("T559505")) t5595.setT559505(child5595.getTextContent());
					}
				}				
				setT5595(t5595);				
				break;	
				
				
			
			case 5596:
				
				NodeList childs5596 = node.getChildNodes();
				Node child5596=null;
				for(int i=0; i<childs5596.getLength(); i++)
				{
					child5596 = childs5596.item(i);
					if(child5596.getNodeType() == Node.ELEMENT_NODE)
					{
						if(child5596.getNodeName().equalsIgnoreCase("T559601")) t5596.setT559601(child5596.getTextContent());
						if(child5596.getNodeName().equalsIgnoreCase("T559602")) t5596.setT559602(child5596.getTextContent());
						if(child5596.getNodeName().equalsIgnoreCase("T559603")) t5596.setT559603(child5596.getTextContent());
						if(child5596.getNodeName().equalsIgnoreCase("T559604")) t5596.setT559604(child5596.getTextContent());
					}
				}
				
				
				
				break;
				
			case 6000:
				setT6000(text);
				break;	
			
			case 6002:
				SubTag6002 t6002 = getT6002();
				
				logger.debug("6002_data:"+text);
				
				logger.debug("setOneDayQuotaFlag:"+text.substring(0, 2));
				t6002.setOneDayQuotaFlag(text.substring(0, 2));//2
				t6002.setOneDayQuota(text.substring(2, 6));//4
				t6002.setOnceQuotaFlag(text.substring(6, 8));//2
				t6002.setOnceQuota(text.substring(8, 12));//4
				t6002.setCheckDeductValue(text.substring(12, 14));//2
				t6002.setAddQuotaFlag(text.substring(14, 16));//2
				t6002.setAddQuota(text.substring(16, 22));//6
				t6002.setCheckDeductFlag(text.substring(22,24));//2
				t6002.setCheckDeductValue(text.substring(24, 28));//4
				t6002.setDeductLimitFlag(text.substring(28, 30));//2
				t6002.setApiVersion(text.substring(30, 38));//8
				t6002.setRFU(text.substring(38, 48));//10
				
				
				break;	
				
			case 6003:
				setT6003(text);
				break;	
				
			case 6004:
				setT6004(text);
				break;	
				
			case 6400:
				setT6400(text);
				break;	
			
			case 6401:
				
				logger.debug("CMAS Response 6401:"+text);
				setT6401(text);
				break;		
				
			case 6408:
				setT6408(text);
				break;
				
			case 6409:
				
				logger.debug("CMAS Response 6409:"+text);
				setT6409(text);
				break;
					
			default:
				logger.error("oh!oh!, unKnowen tag to generate:"+tag);	
				break;
		}
		
		
	}
	
	/*

	private int GetValueCount(String tag) {
		if (mResp == null) {
			return 0;
		}
		
		String startTag = "<" + tag + ">";
		String endTag = "</" + tag + ">";
		int count = 0;
		int start = 0;
		int end = 0;
		while (true) {
			start = mResp.indexOf(startTag, end);
			if (start == -1) {
				return count;
			}
			end = mResp.indexOf(endTag, start);
			if (end == -1) {
				return count;
			}
			count ++;
		}
	}
	
	private String GetValue(String tag) {
		String startTag = "<" + tag + ">";
		String endTag = "</" + tag + ">";
		int start = mResp.indexOf(startTag);
		if (start == -1) {
			return "";
		}
		int end = mResp.indexOf(endTag, start);
		if (end == -1) {
			return "";
		}
		return mResp.substring(start + startTag.length(), end);	
	}
	
	private String GetValue(String tag, String subTag, int index) {
		if (mResp == null) {
			return "";
		}
		
		String startTag = "<" + tag + ">";
		String endTag = "</" + tag + ">";
		String subStartTag = "<" + subTag + ">";
		String subEndTag = "</" + subTag + ">";
		int count = 0;
		int start = 0;
		int end = 0;
		while (true) {
			start = mResp.indexOf(startTag, end);
			if (start == -1) {
				return "";
			}
			end = mResp.indexOf(endTag, start);
			if (end == -1) {
				return "";
			}
			if (count == index) {
				break;
			}
			count ++;
		}
		
		start = mResp.indexOf(subStartTag, start);
		if (start == -1) {
			return "";
		}
		if (start > end) {
			// 在Tag範圍之外, 代表index不對!
			return "";
		}
		end = mResp.indexOf(subEndTag, start);
		if (end == -1) {
			return "";
		}
		return mResp.substring(start + subStartTag.length(), end);
	}
	*/
}

