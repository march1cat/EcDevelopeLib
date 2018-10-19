package ec.xpath;

import java.io.InputStream;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

public class XPathManager {
	
	private Document XML_DOC = null;
	private XPath xpath = null;

	public XPathManager() {
		XPathFactory xpath_fac = XPathFactory.newInstance();
		xpath = xpath_fac.newXPath();
	}
	
	public void ParsingXML(String XML_URI) throws Exception{
		DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
		DBF.setNamespaceAware(true);
		DocumentBuilder DBuilder = DBF.newDocumentBuilder();
		XML_DOC = DBuilder.parse(XML_URI);
	}
  
  public void ParsingXML(InputStream XML_URI) throws Exception{
    DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
    DBF.setNamespaceAware(true);
    DocumentBuilder DBuilder = DBF.newDocumentBuilder();
    XML_DOC = DBuilder.parse(XML_URI);
  }
  
  
  public void ParsingXMLContent(String XmlContent) throws Exception{
    DocumentBuilderFactory DBF = DocumentBuilderFactory.newInstance();
    DBF.setNamespaceAware(true);
    DocumentBuilder DBuilder = DBF.newDocumentBuilder();
    XML_DOC = DBuilder.parse(new InputSource(new StringReader(XmlContent))); 
  }
  
  
	public NodeList XSearch(String XPath_Expression) throws Exception{
		if(XML_DOC == null) throw new Exception("XMLExcpeiton"); 
		XPathExpression xpath_exp = xpath.compile(XPath_Expression);
		Object result = xpath_exp.evaluate(XML_DOC, XPathConstants.NODESET);
		return (NodeList) result;
	}
}
