package ec.file.doc.pdf2;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

import ec.system.Basis;

public class DocFontStyle extends Basis {
	
	private BaseFont bfChinese = null;
	
	//Index Font Setting
	private int indexFontSize = 32;
	private Font fontIndex = null;
	private BaseColor colorIndex = PDFBaseColor(0,0,0);
	
	//Chapter Font Setting
	private int chapterFontSize = 32;
	private Font fontCht = null;
	private BaseColor colorCht = PDFBaseColor(0,0,0);
	
	//Content Font Setting
	private int contentFontSize = 18;
	private Font fontContent = null;
	private BaseColor colorContent = PDFBaseColor(0,0,0);
	
	
	private boolean isImplemented = false;
	
	
	public void implement() {
		try {
			bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
			fontCht = new Font(bfChinese,chapterFontSize,Font.NORMAL,colorCht);
			fontContent = new Font(bfChinese,contentFontSize,Font.NORMAL,colorContent);
			fontIndex = new Font(bfChinese,indexFontSize,Font.NORMAL,colorIndex);
		} catch (Exception e) {
			this.exportExceptionText(e);
		}
		isImplemented = true;
	}
	
	
	public Font getFontCht() {
		return fontCht;
	}

	public void setChapterFontSize(int chapterFontSize) {
		this.chapterFontSize = chapterFontSize;
	}
	
	public void setChapterFontColor(int red,int green,int blue) {
		colorCht = PDFBaseColor(red,green,blue);
	}
	
	
	public Font getFontContent() {
		return fontContent;
	}


	public void setContentFontSize(int contentFontSize) {
		this.contentFontSize = contentFontSize;
	}

	
	public void setContentFontColor(int red,int green,int blue) {
		colorContent = PDFBaseColor(red,green,blue);
	}
	

	public Font getFontIndex() {
		return fontIndex;
	}


	public void setIndexFontSize(int indexFontSize) {
		this.indexFontSize = indexFontSize;
	}


	public void setColorIndex(int red,int green,int blue) {
		this.colorIndex = PDFBaseColor(red,green,blue);
	}

	public boolean isImplemented() {
		return isImplemented;
	}


	private static BaseColor PDFBaseColor(int red,int green,int blue){
		BaseColor color = new BaseColor(red, green, blue);
		return color;
	}
}
