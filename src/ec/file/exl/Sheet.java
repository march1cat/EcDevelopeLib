package ec.file.exl;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class Sheet {

	HSSFSheet xlsSheet = null;
	XSSFSheet xlsxSheet = null;
	
	public Sheet(HSSFSheet hSheet){
		this.xlsSheet = hSheet;
	}
	public Sheet(XSSFSheet xSheet){
		this.xlsxSheet = xSheet;
	}
	
	public boolean isNullSheet(){
		return xlsSheet == null && xlsxSheet == null;
	}
	
	private boolean isXls(){
		return xlsSheet != null;
	}
	private boolean isXlsx(){
		return xlsxSheet != null;
	}
	//===================================================================================================
	//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
	//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
	//===================================================================================================
	//Read Operation
	public String getSheetName(){
		if (isXls()) {
			return xlsSheet.getSheetName();
		} else if (isXlsx()) {
			return xlsxSheet.getSheetName();
		} else {
			return "";
		}
	}
	
	public int getNumberOfRows(){
		if (isXls()) {
			return xlsSheet.getLastRowNum() + 1;
		} else if (isXlsx()) {
			return xlsxSheet.getLastRowNum() + 1;
		} else {
			return -1;
		}
	}
	
	
	public Row getRow(int index) throws Exception{
		if (isXls()) {
			Row row = new Row(xlsSheet.getRow(index));
			return row;
		} else if (isXlsx()) {
			Row row = new Row(xlsxSheet.getRow(index));
			return row;
		} else {
			throw new Exception("");
		}
	}
	//===================================================================================================
	//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
	//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
	//===================================================================================================
	//Write Operation
	public Row createRow(int index) throws Exception{
		if (isXls()) {
			Row row = new Row(xlsSheet.createRow(index));
			return row;
		} else if (isXlsx()) {
			Row row = new Row(xlsxSheet.createRow(index));
			return row;
		} else {
			throw new Exception("createRow Error");
		}
	}
}
