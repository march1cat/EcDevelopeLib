package ec.file.doc.pdf2;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;

import ec.system.Basis;

public class ListElement extends Basis{

	private List comp = new List(List.ORDERED);  
	private DocFontStyle style = null;
	private float marginBetweenItems = 30;
	public ListElement() {
		
	}
	
	public void addText(String text) {
		comp.add(new ListItem(marginBetweenItems,new Chunk(text,style.getFontContent())));
	}
	
	
	public void setMarginBetweenItems(float marginBetweenItems) {
		this.marginBetweenItems = marginBetweenItems;
	}

	public List getComponent() {
		return comp;
	}
	
	public void setStyle(DocFontStyle style) {
		this.style = style;
	}
}
