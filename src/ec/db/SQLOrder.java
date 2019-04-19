package ec.db;

import ec.system.Basis;

public class SQLOrder extends Basis {

	public enum OrderType {
		ASC,DESC
	}
	
	
	private String[] orderColNames = null;
	private Object orderType = null;
	
	public SQLOrder() {}
	
	public SQLOrder(String[] orderColNames) {
		this.orderColNames = orderColNames;
		this.orderType = OrderType.ASC;
	}
	
	public SQLOrder(String[] orderColNames, Object orderType) {
		this.orderColNames = orderColNames;
		this.orderType = orderType;
	}

	public String toSQL() {
		if(orderColNames != null) {
			StringBuffer SQL = new StringBuffer("  order by ");
			for(int i = 0 ;i < orderColNames.length;i++){
				SQL.append(" " + orderColNames[i]);
				if(i != orderColNames.length - 1) SQL.append(",");
			}
			if(orderType == OrderType.ASC) SQL.append(" asc ");
			else SQL.append(" desc ");
			return SQL.toString();
		} else return "";
	}
	
	
	
	
}
