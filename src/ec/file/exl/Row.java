package ec.file.exl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;

public class Row {

	private HSSFRow xlsrow = null;
	private XSSFRow xlsxrow = null;
	
	public Row(HSSFRow hrow){
		this.xlsrow = hrow;
	}
	public Row(XSSFRow xrow){
		this.xlsxrow = xrow;
	}
	
	public String getCellData(int cellIndex){
		Cell cell = null;
		if(isXls()){
			cell = xlsrow.getCell(cellIndex);
		} else if(isXlsx()){
			cell = xlsxrow.getCell(cellIndex);
		} else {
			return "";
		}
		
		if(cell != null){
			cell.setCellType(Cell.CELL_TYPE_STRING);
			if(cell.getCellType() == Cell.CELL_TYPE_STRING){
				return nvsl(cell.getStringCellValue(), "");
			} else if(cell.getCellType() == cell.CELL_TYPE_NUMERIC){
				return nvsl(new BigDecimal(cell.getNumericCellValue()).toPlainString(), "");
			} else if(cell.getCellType() == cell.CELL_TYPE_FORMULA){
				switch(cell.getCachedFormulaResultType()) {
		            case Cell.CELL_TYPE_NUMERIC:
		            	return nvsl(new BigDecimal(cell.getNumericCellValue()).toPlainString(), "");
		            case Cell.CELL_TYPE_STRING:
		            	return nvsl(cell.getRichStringCellValue(), "");
		        }
			} else {
				return nvsl(cell.getStringCellValue(), "");
			}
		} else {
			return "";
		}
		return "";
	}
	
	public Date getDateFromDate(int cellIndex){
		String data = getCellData(cellIndex);
		if(!"".equals(data)){
			try{
				Date date = DateUtil.getJavaDate(Double.parseDouble(data));
				return date;
			} catch(Exception e){
				return null;
			}
		} else {
			return null;
		}
	}
	
	private boolean isXls(){
		return xlsrow != null;
	}
	private boolean isXlsx(){
		return xlsxrow != null;
	}
	
	public int getCellNum(){
		if(isXls()) return xlsrow.getLastCellNum();
		if(isXlsx()) return xlsxrow.getLastCellNum();
		return -1;
	}
	
	private String nvsl(Object value,String emptyReturnVal){
		if(value == null) return emptyReturnVal;
		else return value.toString();
	}
	
	
	public void setCellValue(int cellIndex,Object value) throws Exception{
		Cell cell = null;
		if(isXls()) {
			cell = xlsrow.getCell(cellIndex);
			if(cell == null) cell = xlsrow.createCell(cellIndex);
		}
		if(isXlsx()) {
			cell = xlsxrow.getCell(cellIndex);
			if(cell == null) cell = xlsxrow.createCell(cellIndex);
		}
		if(cell != null) {
			String input = (value != null) ? value.toString() : "";
			cell.setCellValue(input);
		} else {
			throw new Exception("Set Cell Value Error");
		}
	}
	
	public short getCellBkgColor(int cellIndex){
		Cell cell = null;
		if(isXls()){
			cell = xlsrow.getCell(cellIndex);
		} else if(isXlsx()){
			cell = xlsxrow.getCell(cellIndex);
		}
		return (cell != null) ? cell.getCellStyle().getFillForegroundColor() : null;
	}
	
	
}
