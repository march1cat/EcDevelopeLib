package ec.file.doc.pdf2;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import ec.system.Basis;

public class TableElement extends Basis{

	private int colNum = 0;
	private PdfPTable component = null;
	private DocFontStyle style = null;
	
	
	public TableElement(int colNum) {
		this.colNum = colNum;
		component = new PdfPTable(colNum);
		component.setWidthPercentage(90);
	}
	
	
	public void addColText(String text) {
		Paragraph para = new Paragraph(text,style.getFontContent());
		PdfPCell cell = new PdfPCell(para);
		component.addCell(cell);
	} 
	
	public void addColText(String text,int colspan) {
		Paragraph para = new Paragraph(text,style.getFontContent());
		PdfPCell cell = new PdfPCell(para);
		cell.setColspan(colspan);
		component.addCell(cell);
	} 
	

	public DocFontStyle getStyle() {
		return style;
	}


	public void setStyle(DocFontStyle style) {
		this.style = style;
	}


	public PdfPTable getComponent() {
		return component;
	}
	
	
	
}
