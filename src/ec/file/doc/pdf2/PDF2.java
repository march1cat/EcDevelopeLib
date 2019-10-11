package ec.file.doc.pdf2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import ec.system.Basis;

/*
 * New Version for PDF generator 2019/05/21 use itext v5
 * 
 * */
public class PDF2 extends Basis {
	
	private Document doc = null;
	private DocFontStyle style = new DocFontStyle();
	
	private String saveTo = null;
	
	private List<Element> indexes = null;
	private List<Element> contents = null;
	
	private int indexSEQ = 0;
	private int chapterNum = 1;
	private Image cover = null;
	
	public PDF2(String saveTo) {
		this.saveTo = saveTo;
	}
	
	
	public void addChapter(String chtTitle) {
		if(!style.isImplemented()) style.implement();
		String linkID = getISEQ();
		if(true) {
			Chunk chunk = new Chunk(chapterNum + " . " + chtTitle,style.getFontIndex());
			chunk.setLocalGoto(linkID);  
			if(indexes == null) {
				indexes = new ArrayList<Element>();
				indexes.add(new Paragraph("目錄",style.getFontCht()));
			}
			indexes.add(new Paragraph(chunk));
		}
		
		
		if(true) {
			Chunk chunk = new Chunk(chtTitle,style.getFontCht());
			chunk.setLocalDestination(linkID);
			Chapter chapter = new Chapter(new Paragraph(chunk),chapterNum++);    
			if(contents == null) contents = new ArrayList<Element>();
			contents.add(chapter);
		}
	}
	
	
	public void addText(String contet) {
		if(contents == null) contents = new ArrayList<Element>();
		if(!style.isImplemented()) style.implement();
		contents.add(new Paragraph(contet,style.getFontContent()));
	}
	
	public void addImage(String imgUri) throws Exception {
		if(contents == null) contents = new ArrayList<Element>();
		File img = new File(imgUri);
		if(img.exists()){
			Image image = Image.getInstance(imgUri);
			image.setAlignment(Image.ALIGN_CENTER);
			contents.add(image);
		} else throw new Exception("Image File doesn't exit,Uri = " + imgUri);
	}
	
	public TableElement addTable(int colAmount) {
		if(contents == null) contents = new ArrayList<Element>();
		TableElement t = new TableElement(colAmount);
		t.setStyle(this.style);
		contents.add(t.getComponent());
		return t;
	}
	
	public ListElement addList() {
		if(contents == null) contents = new ArrayList<Element>();
		ListElement list = new ListElement();
		list.setStyle(this.style);
		contents.add(list.getComponent());
		return list;
	}
	
	public void setCover(String imgUri) throws Exception {
		File img = new File(imgUri);
		if(img.exists()){
			cover = Image.getInstance(imgUri);
			cover.setAlignment(Image.ALIGN_CENTER);
		} else throw new Exception("Image File doesn't exit,Uri = " + imgUri);
	}
	
	public void insertChangePage() {
		if(contents == null) contents = new ArrayList<Element>();
		contents.add(new NewPageElement());
	}
	
	public void addMarginTB(int tbsize) {
		Paragraph spacer = new Paragraph("");
		spacer.setSpacingAfter(tbsize);
		if(contents == null) contents = new ArrayList<Element>();
		contents.add(spacer);
	}
	
	public void save() throws Exception {
		if(isListWithContent(contents)) {
			if(true) {
				File f = new File(saveTo);
				if(f.exists()) f.delete();
				doc = new Document();
				PdfWriter.getInstance(doc, new FileOutputStream(saveTo));
				doc.open();
			}
			
			
			if(cover != null) {
				cover.scaleAbsolute(doc.getPageSize().getWidth() - 40, doc.getPageSize().getHeight() - 40);
				doc.add(cover);
			}
			
			if(isListWithContent(indexes)) {
				for(Element i : indexes) {
					doc.add(i);
				}
				doc.newPage();
			}
			if(isListWithContent(contents)) {
				for(Element i : contents) {
					if(i instanceof NewPageElement) {
						doc.newPage();
					} else if(i instanceof Image) {
						((Image)i).scaleAbsolute(doc.getPageSize().getWidth() - 40, doc.getPageSize().getHeight() - 40);
						doc.add(i);
					} else {
						doc.add(i);
					}
				}
			}
			
			doc.close();
		}
	}
	
	private String getISEQ() {
		indexSEQ = indexSEQ + 1;
		String p = "00000000" + indexSEQ;
		return p .substring(p.length() - 4);
	}


	public String getSaveTo() {
		return saveTo;
	}


	public DocFontStyle getFontStyle() {
		return style;
	}
	
	
	
}
