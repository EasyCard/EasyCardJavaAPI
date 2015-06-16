package com.easycard.utilities;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
 
public class XMLReader {
	 DocumentBuilderFactory dbFactory;
	 DocumentBuilder dBuilder;
	 public Document doc ;
	 NodeList nList;
	 public String XMLFileName;
	 File fXmlFile;
	

	public String GetNodeValue(String NodeName,String ElementName)
	 { 
		 String elementContentstr;
		 try{
		 doc.getDocumentElement().normalize();
		 Element eElement =  (Element) doc.getElementsByTagName(NodeName).item(0);
		 elementContentstr= eElement.getElementsByTagName(ElementName).item(0).getTextContent();
		 System.out.println(NodeName+" = " + elementContentstr);
		 }catch(Exception e){
			 
			 return null;
		 }
	     return elementContentstr;
	  }
	 
	 public String GetNodeValue(String Node1Name,String Node2Name,String ElementName)
	 { 
		 String elementContentstr;
		 try{
		 doc.getDocumentElement().normalize();
		 Element eElement1 =  (Element) doc.getElementsByTagName(Node1Name).item(0);
		 
		 Element eElement2 =(Element) eElement1.getElementsByTagName(Node1Name).item(0);
		 elementContentstr= eElement2.getElementsByTagName(ElementName).item(0).getTextContent();
		 System.out.println(Node1Name+ "-" +Node2Name+" = " + elementContentstr);
		 }catch(Exception e){
			 
			 return null;
		 }
	     return elementContentstr;
	  }
	 
	 public String SetNodeValue(String NodeName,String ElementName,String Value)
	 {
	    	 doc.getDocumentElement().normalize();
	    	 Element eElement =  (Element) doc.getElementsByTagName(NodeName).item(0);
	    	
	    	 eElement.getElementsByTagName(ElementName).item(0).setTextContent(Value);
	    	 
	    	 System.out.println(ElementName+" = " +  eElement.getElementsByTagName(ElementName).item(0).getTextContent());
	    	 SavetoXML();
	    	 return eElement.getElementsByTagName(ElementName).item(0).getTextContent();
	 }
	 public void SavetoXML()
	 {
	    	Element e = null;
	    	 try {
	             Transformer tr = TransformerFactory.newInstance().newTransformer();
	             tr.setOutputProperty(OutputKeys.INDENT, "yes");
	             tr.setOutputProperty(OutputKeys.METHOD, "xml");
	             tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        

	             // send DOM to file
	             tr.transform(new DOMSource( doc), 
	                                  new StreamResult(new FileOutputStream(XMLFileName)));

	         } catch (TransformerException te) {
	             System.out.println(te.getMessage());
	         } catch (IOException ioe) {
	             System.out.println(ioe.getMessage());
	         }
	    	 
	 }

	 public void LoadXMLFile(String filename) throws ParserConfigurationException
	 {
		 
		  XMLFileName=filename;	
		  fXmlFile = new File( XMLFileName);
		  dbFactory = DocumentBuilderFactory.newInstance();
		  dBuilder = dbFactory.newDocumentBuilder();
		  
 		try {
			doc = dBuilder.parse(fXmlFile);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }
	 public  XMLReader(String filename) 
	 {
		try {
			this.LoadXMLFile(filename);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
 
}




