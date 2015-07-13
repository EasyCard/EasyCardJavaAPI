package com.easycard.pc.CMAS;


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

import com.easycard.pc.CMAS.CmasTag.SubTag5588;
import com.easycard.pc.CMAS.CmasTag.SubTag5595;
import com.easycard.pc.CMAS.CmasTag.SubTag5596;
import com.easycard.pc.CMAS.CmasTag.SubTag6002;
import com.easycard.utilities.Util;

public class CmasDataSpec {

	static Logger logger = Logger.getLogger(CmasDataSpec.class);
	
	public enum PCode{
		SIGNON("881999"),
		CPU_DEDUCT("811599"),
		CPU_DEDUCT_VOID("823899"),
		CPU_REFUND("851999"),
		CPU_ADD_VALUE("811799"),
		CPU_AUTOLOAD("825799"),
		CPU_ADDVALUE_VOID("811899"),
		LOCK_CARD("596100"),
		CPU_EXTENSION("811399"),
		CPU_TXN_VERIFICATION("816399"),
		SETTLEMENT("900000");
		
		
		private final String value;
		private PCode(String s){
			value = s;
		}
		
		public String toString(){
			return value;
		}
	}
	
	public enum MsgType{
		AUTH_REQ("0100"),
		AUTH_RESP("0100"),
		ADVICE_REQ("0220"),
		ADVICE_RESP("0230"),
		REVERSAL_REQ("0400"),
		REVERSAL_RESP("0410");
				
		private final String value;
		private MsgType(String s){
			value = s;
		}
		
		public String toString(){
			return value;
		}
	}
	
	public enum CmasReqField{
		_0800(new int[] {100, 300, 1100, 1101, 1200, 1201, 1300, 1301, 3700, 4100, 4101, 4102, 4103, 4104, 4200, 4210, 4802, 4820, 4823, 4824, 5301, 5307, 5308, 5361, 5362, 5363, 5364, 5365, 5366, 5368, 5369, 5370, 5371, 5501, 5503, 5504, 5510, 5588, 5596, 6000, 6002, 6003, 6004, 6400, 6408}),
		_0820(new int[] {100, 300, 1100, 1101, 1200, 1300, 3700, 4100, 4200, 4210, 4825, 5501, 5503, 5504, 5510, 6406}),
		_0100_AUTH(new int[] {100, 200, 211, 213, 214, 300, 400, 409, 410, 1100, 1101, 1200, 1201, 1300, 1301, 1400, 1402, 3700, 4100, 4101, 4102, 4103, 4104, 4200, 4210, 4800, 4801, 4802, 4803, 4804, 4805, 4806, 4813, 4826, 5301, 5304, 5361, 5362, 5363, 5371, 5501, 5503, 5504, 5510, 6000, 6001, 6004, 6400}),
		_0100_VERI(new int[] {100, 200, 211, 213, 214, 300, 400, 409, 410, 1100, 1101, 1200, 1201, 1300, 1301, 1400, 1402, 3700, 4100, 4101, 4102, 4103, 4104, 4200, 4210, 4800, 4801, 4802, 4803, 4804, 4805, 4806, 4809, 4810, 4811, 4812, 4813, 4814, 4821, 4826, 5301, 5302, 5303, 5304, 5361, 5362, 5363, 5371, 5501, 5503, 5504, 5510, 6000, 6004, 6400, 6406}),
		_0220(new int[] {100,200,211,213,214,300,400,408,409,410,1100,1101,1200,1201,1300,1301,1400,3700,4100,4101,4103,4104,4200,4210,4800,4801,4802,4803,4804,4805,4808,4809,4810,4811,4812,4813,4826,5301,5303,5304,5305,5361,5362,5363,5371,5501,5503,5504,5510,6404,6405});
		private final int[] field;
		private CmasReqField(int[] f){
			field = f;
		}
		
		public int[] getField(){
			return field;
		}
	}
	
	//CMAS Tag
	private CmasTag cmasTag = new CmasTag();
	
	
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
	
	//public CmasTag getCmasTag(){
	//	return cmasTag;
//	}
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
	
	/*
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
	
	public class CmasTag{
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
		}
	*/

	public String getT0100() {
		return cmasTag.t0100;
	}
	public void setT0100(String t0100) {
		cmasTag.t0100 = t0100;
	}
	public String getT0200() {
		return cmasTag.t0200;
	}
	public void setT0200(String t0200) {
		cmasTag.t0200 = t0200;
	}
	public String getT0211() {
		return cmasTag.t0211;
	}
	public void setT0211(String t0211) {
		cmasTag.t0211 = t0211;
	}
	public String getT0212() {
		return cmasTag.t0212;
	}
	public void setT0212(String t0212) {
		cmasTag.t0212 = t0212;
	}
	public String getT0213() {
		return cmasTag.t0213;
	}
	public void setT0213(String t0213) {
		cmasTag.t0213 = t0213;
	}
	public String getT0214() {
		return cmasTag.t0214;
	}
	public void setT0214(String t0214) {
		cmasTag.t0214 = t0214;
	}
	public String getT0300() {
		return cmasTag.t0300;
	}
	public void setT0300(String t0300) {
		cmasTag.t0300 = t0300;
	}
	public String getT0301() {
		return cmasTag.t0301;
	}
	public void setT0301(String t0301) {
		cmasTag.t0301 = t0301;
	}
	public String getT0400() {
		return cmasTag.t0400;
	}
	public void setT0400(String t0400) {
		cmasTag.t0400 = t0400;
	}
	public String getT0403() {
		return cmasTag.t0403;
	}
	public void setT0403(String t0403) {
		cmasTag.t0403 = t0403;
	}
	public String getT0407() {
		return cmasTag.t0407;
	}
	public void setT0407(String t0407) {
		cmasTag.t0407 = t0407;
	}
	public String getT0408() {
		return cmasTag.t0408;
	}
	public void setT0408(String t0408) {
		cmasTag.t0408 = t0408;
	}
	public String getT0409() {
		return cmasTag.t0409;
	}
	public void setT0409(String t0409) {
		cmasTag.t0409 = t0409;
	}
	public String getT0410() {
		return cmasTag.t0410;
	}
	public void setT0410(String t0410) {
		cmasTag.t0410 = t0410;
	}
	public String getT0700() {
		return cmasTag.t0700;
	}
	public void setT0700(String t0700) {
		cmasTag.t0700 = t0700;
	}
	public String getT1100() {
		return cmasTag.t1100;
	}
	public void setT1100(String t1100) {
		cmasTag.t1100 = t1100;
	}
	public String getT1101() {
		return cmasTag.t1101;
	}
	public void setT1101(String t1101) {
		cmasTag.t1101 = t1101;
	}
	public String getT1200() {
		return cmasTag.t1200;
	}
	public void setT1200(String t1200) {
		cmasTag.t1200 = t1200;
	}
	public String getT1201() {
		return cmasTag.t1201;
	}
	public void setT1201(String t1201) {
		cmasTag.t1201 = t1201;
	}
	public String getT1300() {
		return cmasTag.t1300;
	}
	public void setT1300(String t1300) {
		cmasTag.t1300 = t1300;
	}
	/**
	 * @return cmasTag.the t1301
	 */
	public String getT1301() {
		return cmasTag.t1301;
	}
	/**
	 * @param t1301 the t1301 to set
	 */
	public void setT1301(String t1301) {
		cmasTag.t1301 = t1301;
	}
	public String getT1400() {
		return cmasTag.t1400;
	}
	public void setT1400(String t1400) {
		cmasTag.t1400 = t1400;
	}
	public String getT1402() {
		return cmasTag.t1402;
	}
	public void setT1402(String t1402) {
		cmasTag.t1402 = t1402;
	}
	public String getT1403() {
		return cmasTag.t1403;
	}
	public void setT1403(String t1403) {
		cmasTag.t1403 = t1403;
	}
	public String getT3700() {
		return cmasTag.t3700;
	}
	public void setT3700(String t3700) {
		cmasTag.t3700 = t3700;
	}
	public String getT3701() {
		return cmasTag.t3701;
	}
	public void setT3701(String t3701) {
		cmasTag.t3701 = t3701;
	}
	public String getT3800() {
		return cmasTag.t3800;
	}
	public void setT3800(String t3800) {
		cmasTag.t3800 = t3800;
	}
	public String getT3900() {
		return cmasTag.t3900;
	}
	public void setT3900(String t3900) {
		cmasTag.t3900 = t3900;
	}
	public String getT3901() {
		return cmasTag.t3901;
	}
	public void setT3901(String t3901) {
		cmasTag.t3901 = t3901;
	}
	public String getT3902() {
		return cmasTag.t3902;
	}
	public void setT3902(String t3902) {
		cmasTag.t3902 = t3902;
	}
	public String getT3903() {
		return cmasTag.t3903;
	}
	public void setT3903(String t3903) {
		cmasTag.t3903 = t3903;
	}
	public String getT3904() {
		return cmasTag.t3904;
	}
	public void setT3904(String t3904) {
		cmasTag.t3904 = t3904;
	}
	public String getT3911() {
		return cmasTag.t3911;
	}
	public void setT3911(String t3911) {
		cmasTag.t3911 = t3911;
	}
	public String getT4100() {
		return cmasTag.t4100;
	}
	public void setT4100(String t4100) {
		cmasTag.t4100 = t4100;
	}
	public String getT4101() {
		return cmasTag.t4101;
	}
	public void setT4101(String t4101) {
		cmasTag.t4101 = t4101;
	}
	public String getT4102() {
		return cmasTag.t4102;
	}
	public void setT4102(String t4102) {
		cmasTag.t4102 = t4102;
	}
	public String getT4103() {
		return cmasTag.t4103;
	}
	public void setT4103(String t4103) {
		cmasTag.t4103 = t4103;
	}
	public String getT4104() {
		return cmasTag.t4104;
	}
	public void setT4104(String t4104) {
		cmasTag.t4104 = t4104;
	}
	public String getT4200() {
		return cmasTag.t4200;
	}
	public void setT4200(String t4200) {
		cmasTag.t4200 = t4200;
	}
	public String getT4201() {
		return cmasTag.t4201;
	}
	public void setT4201(String t4201) {
		cmasTag.t4201 = t4201;
	}
	public String getT4202() {
		return cmasTag.t4202;
	}
	public void setT4202(String t4202) {
		cmasTag.t4202 = t4202;
	}
	public String getT4203() {
		return cmasTag.t4203;
	}
	public void setT4203(String t4203) {
		cmasTag.t4203 = t4203;
	}
	public String getT4204() {
		return cmasTag.t4204;
	}
	public void setT4204(String t4204) {
		cmasTag.t4204 = t4204;
	}
	public String getT4205() {
		return cmasTag.t4205;
	}
	public void setT4205(String t4205) {
		cmasTag.t4205 = t4205;
	}
	public String getT4206() {
		return cmasTag.t4206;
	}
	public void setT4206(String t4206) {
		cmasTag.t4206 = t4206;
	}
	public String getT4207() {
		return cmasTag.t4207;
	}
	public void setT4207(String t4207) {
		cmasTag.t4207 = t4207;
	}
	public String getT4208() {
		return cmasTag.t4208;
	}
	public void setT4208(String t4208) {
		cmasTag.t4208 = t4208;
	}
	public String getT4209() {
		return cmasTag.t4209;
	}
	public void setT4209(String t4209) {
		cmasTag.t4209 = t4209;
	}
	public String getT4210() {
		return cmasTag.t4210;
	}
	public void setT4210(String t4210) {
		cmasTag.t4210 = t4210;
	}
	public String getT4800() {		
		return cmasTag.t4800;
	}
	public void setT4800(String t4800) {
		cmasTag.t4800 = t4800;
	}
	public String getT4801() {
		return cmasTag.t4801;
	}
	public void setT4801(String t4801) {
		cmasTag.t4801 = t4801;
	}
	
	public enum Issuer {
		easycard,
		keelung,
		unknown
	};
	public String getT4802() {
		return cmasTag.t4802;
	}
	public void setT4802(String t4802) {
		cmasTag.t4802 = t4802;
	}
	public String getT4803() {
		return cmasTag.t4803;
	}
	public void setT4803(String t4803) {
		cmasTag.t4803 = t4803;
	}
	public String getT4804() {
		return cmasTag.t4804;
	}
	public void setT4804(String t4804) {
		cmasTag.t4804 = t4804;
	}
	public String getT4805() {
		return cmasTag.t4805;
	}
	public void setT4805(String t4805) {
		cmasTag.t4805 = t4805;
	}
	public String getT4806() {
		return cmasTag.t4806;
	}
	public void setT4806(String t4806) {
		cmasTag.t4806 = t4806;
	}
	public String getT4807() {
		return cmasTag.t4807;
	}
	public void setT4807(String t4807) {
		cmasTag.t4807 = t4807;
	}
	public String getT4808() {
		return cmasTag.t4808;
	}
	public void setT4808(String t4808) {
		cmasTag.t4808 = t4808;
	}
	public String getT4809() {
		return cmasTag.t4809;
	}
	public void setT4809(String t4809) {
		cmasTag.t4809 = t4809;
	}
	public String getT4810() {
		return cmasTag.t4810;
	}
	public void setT4810(String t4810) {
		cmasTag.t4810 = t4810;
	}
	public String getT4811() {
		return cmasTag.t4811;
	}
	public void setT4811(String t4811) {
		cmasTag.t4811 = t4811;
	}
	public String getT4812() {
		return cmasTag.t4812;
	}
	public void setT4812(String t4812) {
		cmasTag.t4812 = t4812;
	}
	public String getT4813() {
		return cmasTag.t4813;
	}
	public void setT4813(String t4813) {
		cmasTag.t4813 = t4813;
	}
	public String getT4814() {
		return cmasTag.t4814;
	}
	public void setT4814(String t4814) {
		cmasTag.t4814 = t4814;
	}
	public String getT4815() {
		return cmasTag.t4815;
	}
	public void setT4815(String t4815) {
		cmasTag.t4815 = t4815;
	}
	public String getT4816() {
		return cmasTag.t4816;
	}
	public void setT4816(String t4816) {
		cmasTag.t4816 = t4816;
	}
	public String getT4817() {
		return cmasTag.t4817;
	}
	public void setT4817(String t4817) {
		cmasTag.t4817 = t4817;
	}
	public String getT4818() {
		return cmasTag.t4818;
	}
	public void setT4818(String t4818) {
		cmasTag.t4818 = t4818;
	}
	public String getT4819() {
		return cmasTag.t4819;
	}
	public void setT4819(String t4819) {
		cmasTag.t4819 = t4819;
	}
	public String getT4820() {
		return cmasTag.t4820;
	}
	public void setT4820(String t4820) {
		cmasTag.t4820 = t4820;
	}
	public String getT4821() {
		return cmasTag.t4821;
	}
	public void setT4821(String t4821) {
		cmasTag.t4821 = t4821;
	}
	public String getT4823() {
		return cmasTag.t4823;
	}
	public void setT4823(String t4823) {
		cmasTag.t4823 = t4823;
	}
	public String getT4824() {
		return cmasTag.t4824;
	}
	public void setT4824(String t4824) {
		cmasTag.t4824 = t4824;
	}
	public String getT4825() {
		return cmasTag.t4825;
	}
	public void setT4825(String t4825) {
		cmasTag.t4825 = t4825;
	}
	public String getT4826() {
		return cmasTag.t4826;
	}
	public void setT4826(String t4826) {
		cmasTag.t4826 = t4826;
	}
	public String getT4827() {
		return cmasTag.t4827;
	}
	public void setT4827(String t4827) {
		cmasTag.t4827 = t4827;
	}
	public String getT4828() {
		return cmasTag.t4828;
	}
	public void setT4828(String t4828) {
		cmasTag.t4828 = t4828;
	}
	public String getT4829() {
		return cmasTag.t4829;
	}
	public void setT4829(String t4829) {
		cmasTag.t4829 = t4829;
	}
	public String getT4830() {
		return cmasTag.t4830;
	}
	public void setT4830(String t4830) {
		cmasTag.t4830 = t4830;
	}
	public String getT5301() {
		return cmasTag.t5301;
	}
	public void setT5301(String t5301) {
		cmasTag.t5301 = t5301;
	}
	public String getT5302() {
		return cmasTag.t5302;
	}
	public void setT5302(String t5302) {
		cmasTag.t5302 = t5302;
	}
	public String getT5303() {
		return cmasTag.t5303;
	}
	public void setT5303(String t5303) {
		cmasTag.t5303 = t5303;
	}
	public String getT5304() {
		return cmasTag.t5304;
	}
	public void setT5304(String t5304) {
		cmasTag.t5304 = t5304;
	}
	public String getT5305() {
		return cmasTag.t5305;
	}
	public void setT5305(String t5305) {
		cmasTag.t5305 = t5305;
	}
	public String getT5306() {
		return cmasTag.t5306;
	}
	public void setT5306(String t5306) {
		cmasTag.t5306 = t5306;
	}
	public String getT5307() {
		return cmasTag.t5307;
	}
	public void setT5307(String t5307) {
		cmasTag.t5307 = t5307;
	}
	public String getT5308() {
		return cmasTag.t5308;
	}
	public void setT5308(String t5308) {
		cmasTag.t5308 = t5308;
	}
	public String getT5361() {
		return cmasTag.t5361;
	}
	public void setT5361(String t5361) {
		cmasTag.t5361 = t5361;
	}
	public String getT5362() {
		return cmasTag.t5362;
	}
	public void setT5362(String t5362) {
		cmasTag.t5362 = t5362;
	}
	public String getT5363() {
		return cmasTag.t5363;
	}
	public void setT5363(String t5363) {
		cmasTag.t5363 = t5363;
	}
	public String getT5364() {
		return cmasTag.t5364;
	}
	public void setT5364(String t5364) {
		cmasTag.t5364 = t5364;
	}
	public String getT5365() {
		return cmasTag.t5365;
	}
	public void setT5365(String t5365) {
		cmasTag.t5365 = t5365;
	}
	public String getT5366() {
		return cmasTag.t5366;
	}
	public void setT5366(String t5366) {
		cmasTag.t5366 = t5366;
	}
	public String getT5367() {
		return cmasTag.t5367;
	}
	public void setT5367(String t5367) {
		cmasTag.t5367 = t5367;
	}
	public String getT5368() {
		return cmasTag.t5368;
	}
	public void setT5368(String t5368) {
		cmasTag.t5368 = t5368;
	}
	public String getT5369() {
		return cmasTag.t5369;
	}
	public void setT5369(String t5369) {
		cmasTag.t5369 = t5369;
	}
	public String getT5370() {
		return cmasTag.t5370;
	}
	public void setT5370(String t5370) {
		cmasTag.t5370 = t5370;
	}
	public String getT5371() {
		return cmasTag.t5371;
	}
	public void setT5371(String t5371) {
		cmasTag.t5371 = t5371;
	}
	public String getT5501() {
		return cmasTag.t5501;
	}
	public void setT5501(String t5501) {
		cmasTag.t5501 = t5501;
	}
	public String getT5503() {
		return cmasTag.t5503;
	}
	public void setT5503(String t5503) {
		cmasTag.t5503 = t5503;
	}
	public String getT5504() {
		return cmasTag.t5504;
	}
	public void setT5504(String t5504) {
		cmasTag.t5504 = t5504;
	}
	public String getT5509() {
		return cmasTag.t5509;
	}
	public void setT5509(String t5509) {
		cmasTag.t5509 = t5509;
	}
	public String getT5510() {
		return cmasTag.t5510;
	}
	public void setT5510(String t5510) {
		cmasTag.t5510 = t5510;
	}
	public String getT5548() {
		return cmasTag.t5548;
	}
	public void setT5548(String t5548) {
		cmasTag.t5548 = t5548;
	}
	public String getT5550() {
		return cmasTag.t5550;
	}
	public void setT5550(String t5550) {
		cmasTag.t5550 = t5550;
	}
	public String getT5581() {
		return cmasTag.t5581;
	}
	public void setT5581(String t5581) {
		cmasTag.t5581 = t5581;
	}
	public String getT5582() {
		return cmasTag.t5582;
	}
	public void setT5582(String t5582) {
		cmasTag.t5582 = t5582;
	}
	public String getT5583() {
		return cmasTag.t5583;
	}
	public void setT5583(String t5583) {
		cmasTag.t5583 = t5583;
	}
	
	public ArrayList<SubTag5588> getT5588s() {
		return cmasTag.t5588s;
	}
	
	public void setT5588s(SubTag5588 t5588) {
		cmasTag.t5588s.add(t5588);
	}
	
	public String getT5589() {
		return cmasTag.t5589;
	}
	public void setT5589(String t5589) {
		cmasTag.t5589 = t5589;
	}
	public String getT5590() {
		return cmasTag.t5590;
	}
	public void setT5590(String t5590) {
		cmasTag.t5590 = t5590;
	}
	public String getT5591() {
		return cmasTag.t5591;
	}
	public void setT5591(String t5591) {
		cmasTag.t5591 = t5591;
	}
	public String getT5592() {
		return cmasTag.t5592;
	}
	public void setT5592(String t5592) {
		cmasTag.t5592 = t5592;
	}
	public String getT5593() {
		return cmasTag.t5593;
	}
	public void setT5593(String t5593) {
		cmasTag.t5593 = t5593;
	}
	public String getT5594() {
		return cmasTag.t5594;
	}
	public void setT5594(String t5594) {
		cmasTag.t5594 = t5594;
	}
	public ArrayList<SubTag5595> getT5595s() {
		return cmasTag.t5595s;
	}
	public void setT5595(SubTag5595 t5595) {
		cmasTag.t5595s.add(t5595);
	}
	public SubTag5596 getT5596() {
		return cmasTag.t5596;
	}
	public void setT5596(SubTag5596 t5596) {
		cmasTag.t5596 = t5596;
	}
	public String getT5597() {
		return cmasTag.t5597;
	}
	public void setT5597(String t5597) {
		cmasTag.t5597 = t5597;
	}
	public String getT5599() {
		return cmasTag.t5599;
	}
	public void setT5599(String t5599) {
		cmasTag.t5599 = t5599;
	}
	public String getT6000() {
		return cmasTag.t6000;
	}
	public void setT6000(String t6000) {
		cmasTag.t6000 = t6000;
	}
	public String getT6001() {
		return cmasTag.t6001;
	}
	public void setT6001(String t6001) {
		cmasTag.t6001 = t6001;
	}
	public SubTag6002 getT6002() {
		return cmasTag.t6002;
	}
	
	/*
	public void setT6002(SubTag6002 t6002) {
		cmasTag.t6002 = t6002;
	}
	*/
	public String getT6003() {
		return cmasTag.t6003;
	}
	public void setT6003(String t6003) {
		cmasTag.t6003 = t6003;
	}
	public String getT6004() {
		return cmasTag.t6004;
	}
	public void setT6004(String t6004) {
		cmasTag.t6004 = t6004;
	}
	public String getT6400() {
		return cmasTag.t6400;
	}
	public void setT6400(String t6400) {
		cmasTag.t6400 = t6400;
	}
	public String getT6401() {		
		return cmasTag.t6401;
	}
	public void setT6401(String t6401) {
		cmasTag.t6401 = t6401;
	}
	public String getT6402() {
		return cmasTag.t6402;
	}
	public void setT6402(String t6402) {
		cmasTag.t6402 = t6402;
	}
	public String getT6403() {
		return cmasTag.t6403;
	}
	public void setT6403(String t6403) {
		cmasTag.t6403 = t6403;
	}
	public String getT6404() {
		return cmasTag.t6404;
	}
	public void setT6404(String t6404) {
		cmasTag.t6404 = t6404;
	}
	public String getT6405() {
		return cmasTag.t6405;
	}
	public void setT6405(String t6405) {
		cmasTag.t6405 = t6405;
	}
	public String getT6406() {
		return cmasTag.t6406;
	}
	public void setT6406(String t6406) {
		cmasTag.t6406 = t6406;
	}
	public String getT6407() {
		return cmasTag.t6407;
	}
	public void setT6407(String t6407) {
		cmasTag.t6407 = t6407;
	}
	public String getT6408() {
		return cmasTag.t6408;
	}
	public void setT6408(String t6408) {
		cmasTag.t6408 = t6408;
	}
	public String getT6409() {		
		return cmasTag.t6409;
	}
	public void setT6409(String t6409) {
		cmasTag.t6409 = t6409;
	}
	
	//保護cmasTag的private
	public SubTag5588 getSubTag5588Instance(){
		return cmasTag.new SubTag5588(); 
	}
	
	public SubTag5595 getSubTag5595Instance(){
		return cmasTag.new SubTag5595(); 
	}
	
	public SubTag5596 getSubTag5596Instance(){
		return cmasTag.new SubTag5596(); 
	}
	
	public SubTag6002 getSubTag6002Instance(){
		return cmasTag.new SubTag6002(); 
	}
	
	public Object getTagValue(int tag){
		return cmasTag.getTagValue(tag);
	}
	
	
	public void setTagValue(int tag, Object value){
		cmasTag.setTagValue(tag, value);
	}
	
	
	// auto Setter
	private void tagParser(Node node){
		
		int tag = Integer.valueOf(node.getNodeName().replaceAll("T", "0"));
		String text = node.getTextContent();
		
		switch(tag)
		{
		/*
			case 100:
				setT0100(text);
				break;
			
			case 200:
				setT0200(text);
				break;
				
			case 211:
				setT0211(text);
				break;	
			
			case 213:
				setT0213(text);
				break;	
			
			case 214:
				setT0214(text);
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
				
			case 1402:
				setT1402(text);
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
				
			case 3903:
				setT3903(text);
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
			
			case 4800:
				setT4800(text);
				break;	
				
			case 4802:
				setT4802(text);
				break;
	
			case 4803:
				setT4803(text);
				break;
				
			case 4804:
				setT4804(text);
				break;	
				
			case 4805:
				setT4805(text);
				break;
				
			case 4806:
				setT4806(text);
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
				
			case 5304:
				setT5304(text);
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
				*/
			
			case 5588:
				SubTag5588 t5588 = cmasTag.new SubTag5588();
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
				//t5588s.add(t5588);				
				getT5588s().add(t5588);
				break;	
				
				/*
			case 5591:
				setT5591(text);
				break;	
				*/
		
			case 5595:
				SubTag5595 t5595 = cmasTag.new SubTag5595();
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
				
				if(t5595.getT559502().equalsIgnoreCase("TM03")){//公司分號
					String tm03Value = t5595.getT559503().replaceAll("\\s+", ""); //remove space
					if(tm03Value.length()%2 !=0){
						tm03Value = new String(Util.paddingChar(tm03Value, false, (byte)0x30, tm03Value.length()+1));
						logger.debug("TM03 Value:"+tm03Value);
						t5595.setT559503(tm03Value);
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
						if(child5596.getNodeName().equalsIgnoreCase("T559601")) cmasTag.t5596.setT559601(child5596.getTextContent());
						if(child5596.getNodeName().equalsIgnoreCase("T559602")) cmasTag.t5596.setT559602(child5596.getTextContent());
						if(child5596.getNodeName().equalsIgnoreCase("T559603")) cmasTag.t5596.setT559603(child5596.getTextContent());
						if(child5596.getNodeName().equalsIgnoreCase("T559604")) cmasTag.t5596.setT559604(child5596.getTextContent());
					}
				}
				
				
				
				break;
				
			/*	
			case 6000:
				setT6000(text);
				break;	
			*/
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
				
				/*
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
					*/
			default:
				cmasTag.setTagValue(tag, text);
				
				//logger.error("oh!oh!, unKnowen tag to generate:"+tag);	
				break;
		}
		
		
	}
}

