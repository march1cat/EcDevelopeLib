package ec.swing.ectable;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class EcJTable {

	
	private EcJTableColumnTitle[] titleAr = null;
	private DefaultTableModel dm = null;
	private JTable table = null;
	private JScrollPane pane = null;
	private List<EcJTableRow> rowList = null;
	private EcJTableListener listener = null;
	
	private double[] rateAr = null;
	private Map<Integer,Color> colColrMp = null;
	
	public EcJTable(EcJTableColumnTitle[] titleAr){
		this.titleAr = titleAr;
		initial();
	}
	
	private void initial(){
		dm = new DefaultTableModel(){
		    @Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		clear();
		table = new JTable(dm);
		pane = new JScrollPane(table);
	}
	
	public void prepareTableActionEvent(EcJTableListener listener){
		this.listener = listener;
		if(table != null){
			table.addMouseListener(new MouseAdapter() {
			    public void mousePressed(MouseEvent me) {
			        JTable table =(JTable) me.getSource();
			        Point p = me.getPoint();
			        int row = table.rowAtPoint(p);
			        if (me.getClickCount() == 2 && row != -1) onDoubleClickRow(row);
			    }
			    
			    public void mouseClicked(MouseEvent evt) {
			    	JTable table =(JTable) evt.getSource();
			    	Point p = evt.getPoint();
			        int row = table.rowAtPoint(p);
			        if(row >= 0) onClickRow(row);
			    }
			});
		}
	}
	
	public void setBackgroundColor(Color color){
		this.pane.setBackground(color);
	}
	
	public void onDoubleClickRow(int selectedIndex){
		if(listener != null && rowList != null) listener.onTableDoubleClickRow(this, rowList.get(selectedIndex));
	}
	
	public void onClickRow(int selectedIndex){
		if(listener != null && rowList != null) listener.onTableClickRow(this, rowList.get(selectedIndex));
	}
	
	public void setBounds(int xpos,int ypos,int width,int height){
		pane.setBounds(xpos, ypos, width, height);
	}
	
	public void join(JComponent container){
		container.add(pane);
	}
	
	public void clear(){
		dm.setDataVector(null, titleAr);
		rowList = new ArrayList<>();
		if(rateAr != null) setColumnWidth(rateAr);
		if(colColrMp != null){
			Iterator<Integer> iter = colColrMp.keySet().iterator();
			while(iter.hasNext()){
				Integer colIndex = iter.next();
				this.setColumnColor(colIndex, colColrMp.get(colIndex));
			}
		}
	}
	
	
	public int getX(){
		return pane.getX();
	}
	
	public int getY(){
		return pane.getY();
	}
	
	public int getWidth(){
		return pane.getWidth();
	}
	
	public int getHeight(){
		return pane.getHeight();
	}
	
	public void setColumnWidth(int colIndex,int width){
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
		TableColumn column = table.getTableHeader().getColumnModel().getColumn(colIndex);
		if(column != null){
			column.setPreferredWidth(width);
		}
	}
	
	public void setColumnWidth(double[] rateAr){
		this.rateAr = rateAr;
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
		for(int i =0; i < Math.min(rateAr.length, rateAr.length);i++){
			TableColumn column = table.getTableHeader().getColumnModel().getColumn(i);
			if(column != null){
				column.setPreferredWidth((int)(getWidth() * rateAr[i]));
			}
		}
	}
	
	
	public void setColumnColor(int colIndex,Color bgColor){
		TableColumn column = table.getTableHeader().getColumnModel().getColumn(colIndex);
		if(column != null){
			if(colColrMp == null) colColrMp = new HashMap<>();
			colColrMp.put(colIndex,bgColor);
			column.setCellRenderer(new EcColorColumnRender(bgColor, Color.BLACK));
		}
	}
	
	public void addRow(EcJTableRow ecJRow){
		if(rowList == null) rowList = new ArrayList<>();
		rowList.add(ecJRow);
		dm.addRow(ecJRow.getDatas(titleAr));
		ecJRow.setContainer(this);
	}
	
	public void removeRow(EcJTableRow ecJRow){
		if(rowList != null) {
			rowList.remove(ecJRow);
			refreshTable();
		}
	}
	
	public void refreshTable(){
		dm.setDataVector(null, titleAr);
		for(EcJTableRow row : rowList){
			dm.addRow(row.getDatas(titleAr));
		}
		if(rateAr != null) setColumnWidth(rateAr);
		if(colColrMp != null){
			Iterator<Integer> iter = colColrMp.keySet().iterator();
			while(iter.hasNext()){
				Integer colIndex = iter.next();
				this.setColumnColor(colIndex, colColrMp.get(colIndex));
			}
		}
	}
	
	public EcJTableRow getRowData(int index){
		return rowList != null ? rowList.get(index) : null;
	}
	
	
	public List<EcJTableRow> getDataRowList(){
		return rowList;
	}
	public int getDataRowAmount(){
		return rowList != null ? rowList.size() : 0;
	}
	
	
}
