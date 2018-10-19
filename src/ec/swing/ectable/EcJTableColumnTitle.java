package ec.swing.ectable;

public class EcJTableColumnTitle {

	private String title = null;
	private Object colName = null;
	
	public EcJTableColumnTitle(Object colName,String title){
		this.title = title;
		this.colName = colName;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title;
	}
	
	public Object getColumnName(){
		return colName;
	}
	
}
