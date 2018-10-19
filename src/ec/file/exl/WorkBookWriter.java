package ec.file.exl;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class WorkBookWriter {

	private WorkBook workBook = null;
	private String fileName = null;
	
	public WorkBookWriter(String fileName){
		this.fileName = fileName;
	}
	
	
	
	public void writeListToExcel(String sheetName,List<Object> refKeyLst,List<Map> dataList) throws Exception{
		WorkBook workBook = WorkBook.createExcelToWorkBook(fileName);
		Sheet sheet = workBook.createSheet(sheetName);
		writeDataToSheet(sheet,refKeyLst,dataList,0);
		workBook.save(fileName);
	}
	
	public void appendListToExcel(String sheetName,List<Object> refKeyLst,List<Map> dataList) throws Exception {
		try {
			WorkBook workBook = WorkBook.loadExcelToWorkBook(fileName);
			Sheet sheet = workBook.getSheetByName(sheetName);
			int start = 0;
			if(sheet.isNullSheet()){
				sheet = workBook.createSheet(sheetName);
			} else {
				start = sheet.getNumberOfRows();
			}
			writeDataToSheet(sheet,refKeyLst,dataList,start);
			workBook.save(fileName);
		} catch (FileNotFoundException e) {
			writeListToExcel(sheetName,refKeyLst,dataList);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	private void writeDataToSheet(Sheet sheet,List<Object> referKeyLst,List<Map> dataList,int start) throws Exception{
		Row row = sheet.createRow(start);

		for(int i = 0; i < dataList.size();i++){
			Map dtMap = dataList.get(i);
			
			Row rowItem = sheet.createRow(i + start);
			for(int j = 0;j < referKeyLst.size();j++){
				Object key = referKeyLst.get(j);
				rowItem.setCellValue(j, NVSL(dtMap.get(key),"1"));
			}
		}
	}
	
	private String NVSL(Object data,String empR){
		if(data == null) return empR;
		else return data.toString();
	}
}
