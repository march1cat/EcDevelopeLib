package ec.swing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JTextField;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;


public class EcDatePicker extends EcJInfoBox{

	private JDatePickerInvoker invoker = null;
	private String returnDateFormat = "yyyy-MM-dd";
	private JTextField valueCatcherText = null;
	
	public EcDatePicker(int xpos, int ypos,JDatePickerInvoker invoker) {
		super(300, 300, xpos, ypos);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setUndecorated(false);
		getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
		this.invoker = invoker;
		initial();
		this.showup();
	}
	
	public EcDatePicker(int xpos, int ypos,JDatePickerInvoker invoker,String returnDateFormat) {
		super(300, 300, xpos, ypos);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setUndecorated(false);
		this.setAlwaysOnTop(true);
		getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
		this.invoker = invoker;
		this.returnDateFormat = returnDateFormat;
		initial();
		this.showup();
	}
	
	private void initial(){
		UtilDateModel model = new UtilDateModel();
		//model.setDate(20,04,2014);
		// Need this...
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		// Don't know about the formatter, but there it is...
		final JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter(returnDateFormat));
		datePicker.setBounds(10, 10, 120, 30);
		this.add(datePanel);
		datePicker.addActionListener(new java.awt.event.ActionListener() {
		    public void actionPerformed(java.awt.event.ActionEvent e) {
		    	String value = datePicker.getJFormattedTextField().getText();
		    	if(returnDateFormat.length() == value.length()){
		    		value =	invoker.onSelectedDateValue(value);
		    		if(valueCatcherText != null && value != null){
		    			valueCatcherText.setText(value);
		    		}
		    	}
		    }
		});
	}
	
	
	public void setDateValueCatcherText(JTextField valueCatcherText){
		this.valueCatcherText = valueCatcherText;
	}
	
}

class DateLabelFormatter extends AbstractFormatter {
    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = null;

    public DateLabelFormatter(String datePattern){
    	this.datePattern = datePattern;
    	dateFormatter = new SimpleDateFormat(datePattern);
    }
    
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }

        return "";
    }

}
