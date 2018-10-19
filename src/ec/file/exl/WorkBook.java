package ec.file.exl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class WorkBook {

	HSSFWorkbook xlsExcel = null;
	XSSFWorkbook xlsxExcel = null;
	private WorkBook(HSSFWorkbook excel){
		xlsExcel = excel;
	}
	private WorkBook(XSSFWorkbook excel){
		xlsxExcel = excel;
	}
	
	private boolean isXls(){
		return xlsExcel != null;
	}
	private boolean isXlsx(){
		return xlsxExcel != null;
	}
	
	//===================================================================================================
	//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
	//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
	//===================================================================================================
	//Read Operation
	
	public int getSheetNum(){
		if (isXls()) {
			return xlsExcel.getNumberOfSheets();
		} else if (isXlsx()) {
			return xlsxExcel.getNumberOfSheets();
		} else {
			return -1;
		}
	}
	public String getSheetName(int sheetIndex){
		if (isXls()) {
			return xlsExcel.getSheetName(sheetIndex);
		} else if (isXlsx()) {
			return xlsxExcel.getSheetName(sheetIndex);
		} else {
			return "";
		}
	}
	
	public Sheet getSheetByIndex(int sheetIndex) throws Exception{
		if (isXls()) {
			return new Sheet(xlsExcel.getSheetAt(sheetIndex));
		} else if (isXlsx()) {
			return new Sheet(xlsxExcel.getSheetAt(sheetIndex));
		} else {
			throw new Exception("Sheet sheetIndex:" + sheetIndex   + " is Empty ");
		}
	}
	
	public Sheet getSheetByName(String sheetName) throws Exception{
		if (isXls()) {
			return new Sheet(xlsExcel.getSheet(sheetName));
		} else if (isXlsx()) {
			return new Sheet(xlsxExcel.getSheet(sheetName));
		} else {
			throw new Exception("Sheet sheetName: " + sheetName   + " is Empty ");
		}
	}
	//===================================================================================================
	//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
	//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
	//===================================================================================================
	//Write Operation
	public Sheet createSheet(String sheetName) throws Exception{
		if (isXls()) {
			return new Sheet(xlsExcel.createSheet(sheetName));
		} else if (isXlsx()) {
			return new Sheet(xlsxExcel.createSheet(sheetName));
		} else {
			throw new Exception("Create Sheet Error");
		}
	}
	
	public void save(String fileName) throws Exception{
		FileOutputStream fileOut = new FileOutputStream(fileName);
		if (isXls()) {
			xlsExcel.write(fileOut);
		} else if (isXlsx()) {
			xlsxExcel.write(fileOut);
		} else {
			throw new Exception("Save Excel Error");
		}
	}
	
	//===================================================================================================
	//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
	//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
	//===================================================================================================
	public static WorkBook loadExcelToWorkBook(String fileName) throws FileNotFoundException,Exception {
		WorkBook workBook = null;
		if(isHSSFType(fileName)){
			FileInputStream file = new FileInputStream(new File(fileName));	
			HSSFWorkbook excel = new HSSFWorkbook(file);
			workBook = new WorkBook(excel);
		} else if(isXSSFType(fileName)){
			FileInputStream file = new FileInputStream(new File(fileName));	
			XSSFWorkbook excel = new XSSFWorkbook(file);
			workBook = new WorkBook(excel);
		} else {
			throw new Exception("沒有符合的Excel格式:" + fileName);
		}
		return workBook;
	}
	
	public static WorkBook loadXlsTypeExcelToWorkBook(InputStream fisIns) throws FileNotFoundException,Exception {
		HSSFWorkbook excel = new HSSFWorkbook(fisIns);
		WorkBook workBook = new WorkBook(excel);
		return workBook;
	}
	public static WorkBook loadXlsxTypeExcelToWorkBook(InputStream fisIns) throws FileNotFoundException,Exception {
		XSSFWorkbook excel = new XSSFWorkbook(fisIns);
		WorkBook workBook = new WorkBook(excel);
		return workBook;
	}
	
	public static WorkBook createExcelToWorkBook(String fileName) throws Exception{
		WorkBook workBook = null;
		if(isHSSFType(fileName)){
			workBook = new WorkBook(new HSSFWorkbook());
		} else if(isXSSFType(fileName)){
			workBook = new WorkBook(new XSSFWorkbook());
		} else {
			throw new Exception("沒有符合的Excel格式:" + fileName);
		}
		return workBook;
	}
	
	private static boolean isHSSFType(String fileName){
		return (fileName.endsWith("xls"));
	}
	private static boolean isXSSFType(String fileName){
		return (fileName.endsWith("xlsx"));
	}
}
