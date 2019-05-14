package ec.db;

import ec.system.Basis;

public class SQLLimit extends Basis{

	
	private int offset = -1;
	private int limitSize = -1;
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLimitSize() {
		return limitSize;
	}
	public void setLimitSize(int limitSize) {
		this.limitSize = limitSize;
	}
	
	public String toSQL() {
		
		if(offset < 0 && limitSize < 0) return "";
		else {
			StringBuffer t = new StringBuffer();
			if(offset >= 0) {
				t.append(" limit " + offset + "," + limitSize + " ");
			} else {
				t.append(" limit  " + limitSize + " ");
			}
			return t.toString();
		}
	}
	
}
