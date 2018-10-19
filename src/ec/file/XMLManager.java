package ec.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLManager {

	private String filePath;

	public XMLManager(String filePath) {
		this.filePath = filePath;
	}

	public NodeList readXML(String fileName, String tagName) {
		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dombuilder = domfac.newDocumentBuilder();
			InputStream is = new FileInputStream(filePath + fileName + ".xml");
			Document doc = dombuilder.parse(is);
			NodeList list = doc.getElementsByTagName(tagName);
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	

	public void createXMLFile(String fileName, String data,String mainNode) {
		StringBuffer xmlData = new StringBuffer("");
		xmlData.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		xmlData.append("\n");
		xmlData.append("<"+mainNode+">\n");
		xmlData.append(data);
		xmlData.append("</"+mainNode+">\n");
		try {
			OutputStream os = new FileOutputStream(new File(filePath + fileName
					+ ".xml"));
			BufferedWriter brw = new BufferedWriter(new OutputStreamWriter(os));
			brw.write(xmlData.toString());
			brw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

	public static String getNodeItem(Node node, String itemName) {
		String nodeItem = node.getAttributes().getNamedItem(itemName)
				.getNodeValue();
		return nodeItem;
	}

}
