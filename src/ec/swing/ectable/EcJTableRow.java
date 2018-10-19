package ec.swing.ectable;

import java.util.HashMap;
import java.util.Map;

public abstract class EcJTableRow {

	private Map<Object,String> dataMp = null;
	private EcJTable container = null;
	
	public EcJTableRow(){
		
	}
	
	public void setContainer(EcJTable container){
		this.container = container;
	}

	public void setData(Object colName,String value){
		if(dataMp == null) dataMp = new HashMap<Object, String>();
		dataMp.put(colName, value);
	}
	
	public String getData(Object colName){
		return dataMp != null ? dataMp.get(colName) : "";
	}
	
	public String[] getDatas(EcJTableColumnTitle[] titleAr){
		String[] ar = new String[titleAr.length];
		for(int i = 0; i < ar.length;i++){
			ar[i] = getData(titleAr[i].getColumnName());
		}
		return ar;
	}
	
	public void refreshContainer(){
		if(container != null) container.refreshTable();
	}
}
