package ec.file.doc;
//Table每一個ROW的欄位總數必須跟欄位CELL colspan總數符合

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ec.system.Basis;

public class PDF extends Basis{
	
	private Document doc = null;
	public String dir_filename;
	private ArrayList<Element> pdf_item_ar = new ArrayList<Element>();
	private HashMap<String,PdfPTable> innerTableMap = new HashMap<String,PdfPTable>();
	
	private BaseFont bfChinese;
	
	
	public final static int ALIGN_RIGHT =  Paragraph.ALIGN_RIGHT;
	public final static int ALIGN_CENTER =  Paragraph.ALIGN_CENTER;
	public final static int ALIGN_LEFT =  Paragraph.ALIGN_LEFT;
	public final static int ALIGN_TOP = Paragraph.ALIGN_TOP;
	public final static int ALIGN_MIDDLE = Paragraph.ALIGN_MIDDLE;
	public final static int ALIGN_BOTTOM = Paragraph.ALIGN_BOTTOM;
	
	public static int default_FontSize = 12;
	public static BaseColor default_FontColor = PDFBaseColor(0,0,0);
	public static int default_FontType = Font.NORMAL;
	public static int default_FontAlign = ALIGN_LEFT;
	
	public static int default_Distance_Between_Items = 5;	
	public static String newLineChar = "\r\t";
	
	public PDF(String output_filename_With_Path) throws DocumentException, IOException{
		dir_filename = output_filename_With_Path;
		
		doc = new Document();
		PdfWriter.getInstance(doc, new FileOutputStream(output_filename_With_Path));
		doc.open();
		iniChineseFont();
	}
	
	public PDF(String output_filename_With_Path,String fontStyleFilePath) throws DocumentException, IOException{
		dir_filename = output_filename_With_Path;
		
		doc = new Document();
		PdfWriter.getInstance(doc, new FileOutputStream(output_filename_With_Path));
		doc.open();
		iniChineseFont(fontStyleFilePath);
	}
	
	public PDF(String output_filename_With_Path,Rectangle PageSizeWithRotate) throws DocumentException, IOException{
		dir_filename = output_filename_With_Path;
		
		//PageSizeWithRotate = PageSize.A4.rotate();
		//Rectangle r = new Rectangle(0,0,1300,1000);
		doc = new Document(PageSizeWithRotate);
		PdfWriter.getInstance(doc, new FileOutputStream(output_filename_With_Path));
		doc.open();
		
		iniChineseFont();
	}
	
	public PDF(String output_filename_With_Path,Rectangle PageSizeWithRotate,String fontStyleFilePath) throws DocumentException, IOException{
		dir_filename = output_filename_With_Path;
		
		//PageSizeWithRotate = PageSize.A4.rotate();
		doc = new Document(PageSizeWithRotate);
		
		PdfWriter.getInstance(doc, new FileOutputStream(output_filename_With_Path));
		doc.open();
		
		iniChineseFont(fontStyleFilePath);
	}
	//=========================================================================================
	public void addPDFContent(Element pdf_item){
		pdf_item_ar.add(pdf_item);
	}
	
	public void addSpacer(){
		addSpacer(default_Distance_Between_Items);
	}
	public void addSpacer(int spacer_height){
		Paragraph spacer = createParagraph("");
		spacer.setSpacingAfter(spacer_height);
		addPDFContent(spacer);
	}
	//====================================================================
	//PDF Page Component
	//***********************************************
	//新增table
	public PdfPTable addTable(int column_num) throws DocumentException{
		PdfPTable table = new PdfPTable(column_num);
		table.setWidthPercentage(100);
		pdf_item_ar.add(table);
		return table;
	}
	public PdfPTable addTable(int[] column_width_ar) throws DocumentException{
		PdfPTable table = addTable(column_width_ar.length);
		table.setWidths(column_width_ar);
		pdf_item_ar.add(table);
		return table;
	}
	
	public void addImage(String imgUri) throws Exception{
		File img = new File(imgUri);
		if(img.exists()){
			Image image = Image.getInstance(imgUri);
			image.setAlignment(Image.ALIGN_CENTER);
			this.addPDFContent(image);
		} else log("Image File doesn't exit,Uri = " + imgUri);
	}
	
	
	//***********************************************
	//新增table cell
	public  PdfPCell createTableCell(int colspan,String content) throws DocumentException, IOException{
		Paragraph para = createParagraph(content);
		PdfPCell cell = createTableCell(para);
		cell.setColspan(colspan);
		return cell;
	}
	
	public  PdfPCell createTableCell(int colspan,String content,int align) throws DocumentException, IOException{
		PdfPCell cell = createTableCell(colspan,content);
		cell.setHorizontalAlignment(align);
		return cell;
	}
	public  PdfPCell createTableCellWithInnerTable(int colspan,int column_num,String innerTableKey) throws DocumentException, IOException{
		PdfPTable table = new PdfPTable(column_num);
		PdfPCell cell = createTableCell(table);
		cell.setColspan(colspan);
		innerTableMap.put(innerTableKey, table);
		return cell;
	}
	public  PdfPCell createTableCellWithInnerTable(int colspan,int column_num,String innerTableKey,int align) throws DocumentException, IOException{
		PdfPCell cell = createTableCellWithInnerTable(colspan,column_num,innerTableKey);
		cell.setHorizontalAlignment(align);
		return cell;
	}
	//***********************************************
	//新增段落
	public Paragraph createParagraph(String content,int size,BaseColor color,int FontType,int align){
		Font fontChinese = new Font(bfChinese, size, FontType, color); 
		Paragraph para = new Paragraph(content,fontChinese);
		para.setAlignment(align);
		return para;
	}
	
	public Paragraph createParagraph(String content){
		Paragraph para = createParagraph(content,default_FontSize,default_FontColor,default_FontType,default_FontAlign);
		return para;
	}
	public Paragraph createParagraph(String content,int align){
		Paragraph para = createParagraph(content,default_FontSize,default_FontColor,default_FontType,align);
		return para;
	}
	//====================================================================
	//Start to Write File
	public void flush() throws DocumentException{
		flushContentToDocument();
		doc.close();
	}
	public void goNextPage() throws DocumentException{
		flushContentToDocument();
		doc.newPage();
	}
	
	public PdfPTable getInnerTable(String innerTableKey){
		return innerTableMap.get(innerTableKey);
	}
	//=============================================================================
	private void iniChineseFont() throws DocumentException, IOException{
		bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
	}
	
	private void iniChineseFont(String fontStyleFilePath) throws DocumentException, IOException{
		bfChinese = BaseFont.createFont(fontStyleFilePath, BaseFont.IDENTITY_H, false);
	}
	
	private PdfPCell createTableCell(Paragraph para){
		PdfPCell cell = new PdfPCell(para);
		return cell;
	}
	private PdfPCell createTableCell(PdfPTable inner_table){
		PdfPCell cell = new PdfPCell(inner_table);
		return cell;
	}
	private void flushContentToDocument() throws DocumentException{
		for(int i = 0;i < pdf_item_ar.size();i++){
			doc.add(pdf_item_ar.get(i));
		}
		pdf_item_ar = new ArrayList<Element>();
	}
	//=============================================================================
	public static BaseColor PDFBaseColor(int red,int green,int blue){
		BaseColor color = new BaseColor(red, green, blue);
		return color;
	}
	
}
