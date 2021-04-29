package ec.parser.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ec.system.Basis;

public class XMLDocument extends Basis {

	private Document doc = null;
	
	public XMLDocument(Document doc) {
		this.doc = doc;
	}
	
	public Element createElement(String tagName) {
		return this.doc.createElement(tagName);
	}
	
	
	protected NodeList searchNodesByName(String tagName) {
		NodeList nList =  this.doc.getElementsByTagName(tagName);
		return nList;
	}

	public Document getDoc() {
		return doc;
	}
	
	
	
	
	
	
	
}
