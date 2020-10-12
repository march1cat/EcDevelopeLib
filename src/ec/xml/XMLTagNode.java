package ec.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;;

public class XMLTagNode {

	private XMLDocument belongDoc = null;
	private Node node = null;

	public XMLTagNode(Node node) {
		super();
		this.node = node;
	}

	public XMLDocument getBelongDoc() {
		return belongDoc;
	}

	public void setBelongDoc(XMLDocument belongDoc) {
		this.belongDoc = belongDoc;
	}
	
	
	public void appendChild(Element element) {
		this.node.appendChild(element);
	}
	
	public void setText(String textContent) {
		this.node.setTextContent(textContent);
	}
	
	public void setAttribute(String attrName , String attrValue) {
		((Element)node).setAttribute(attrName,attrValue);
	}
	
	
	
	
}
