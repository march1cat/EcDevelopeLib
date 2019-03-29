package ec.db;

public class SQLRange {

	private String columnID = null;
	private Object startValue = null;
	private Object endValue = null;
	private boolean isHigherOrEqualStart = true;
	private boolean isLowerOrEqualEnd = true;
	
	public SQLRange(String columnID){
		this.columnID = columnID;
	}
	
	public Object getStartValue() {
		return startValue;
	}
	public void setStartValue(Object startValue) {
		this.startValue = startValue;
	}
	public Object getEndValue() {
		return endValue;
	}
	public void setEndValue(Object endValue) {
		this.endValue = endValue;
	}
	public boolean isHigherOrEqualStart() {
		return isHigherOrEqualStart;
	}
	public void setHigherOrEqualStart(boolean isHigherOrEqualStart) {
		this.isHigherOrEqualStart = isHigherOrEqualStart;
	}
	public boolean isLowerOrEqualEnd() {
		return isLowerOrEqualEnd;
	}
	public void setLowerOrEqualEnd(boolean isLowerOrEqualEnd) {
		this.isLowerOrEqualEnd = isLowerOrEqualEnd;
	}
	
	public String toSQLWhereText(){
		String SQL = "";
		if(startValue != null) {
			if(isHigherOrEqualStart()) SQL += " " + columnID + " >= ? ";
			else SQL += " " + columnID + " > ? ";
		}
		if(endValue != null) {
			if(SQL.length() > 0) SQL += " and ";
			if(isLowerOrEqualEnd()) SQL += " " + columnID + " <= ? ";
			else SQL += " " + columnID + " < ? ";
		}
		return SQL;
	}
	
	
	
}
