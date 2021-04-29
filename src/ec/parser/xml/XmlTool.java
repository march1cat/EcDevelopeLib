package ec.parser.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import ec.system.Basis;

public class XmlTool extends Basis {

	public static XMLDocument parseXMLString(String content) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();  
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new InputSource(new StringReader(content)));
		return new XMLDocument(doc);
	}
	
	public static void parseXMLFile(String uri){
		
	}
	
	public static List<XMLTagNode> searchByTagName(XMLDocument xml , String tagName) throws Exception {
		List<XMLTagNode> nodes = new ArrayList<>();
		NodeList nList =  xml.searchNodesByName(tagName);
		for(int i = 0 ;i < nList.getLength();i++) {
			Node n = nList.item(i);
			XMLTagNode xmlNode = new XMLTagNode(n);
			xmlNode.setBelongDoc(xml);
			nodes.add(xmlNode);
		}
		if(nodes.size() > 0) return nodes;
		else throw new Exception("Node(" + tagName + ") not found!!");
	}
	
	public static XMLTagNode createNode(XMLTagNode node , String tagName) {
		Element element = node.getBelongDoc().createElement(tagName);
		node.appendChild(element);
		XMLTagNode tagNode =  new XMLTagNode(element);
		tagNode.setBelongDoc(node.getBelongDoc());
		return tagNode;
	}
	
	public static String exportAsString(XMLDocument xmlDoc) throws IOException {
		OutputFormat format  = new OutputFormat (xmlDoc.getDoc());
		StringWriter stringOut = new StringWriter ();    
		XMLSerializer serial   = new XMLSerializer (stringOut,  format);
		serial.serialize(xmlDoc.getDoc());
		return stringOut.toString();
	}
	
	
	
}
