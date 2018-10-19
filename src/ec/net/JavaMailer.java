package ec.net;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class JavaMailer {

	private String smtp_host = null;
	private int port = 25;
	private String user_acc = null;
	private String user_password = null;
	
	private Properties p = null;
	private Session session = null;
	private Transport tran = null;
	
	private Message msg = null;
	private Multipart multi_mp = null;
	
	private ArrayList<Address> to_receipt_list = new ArrayList<Address>();
	private ArrayList<Address> cc_receipt_list = new ArrayList<Address>();
	private ArrayList<Address> bcc_receipt_list = new ArrayList<Address>();
	
	public static final int MailReceiverGroup_TO = 1;
	public static final int MailReceiverGroup_CC = 2;
	public static final int MailReceiverGroup_BCC = 3;
	
	public JavaMailer(String smtp_host,String user_acc,String user_password){
		iniSmtpData(smtp_host,25,user_acc,user_password);
		iniProperty();
	}
	
	public JavaMailer(String smtp_host,int port,String user_acc,String user_password){
		iniSmtpData(smtp_host,port,user_acc,user_password);
		iniProperty();
	}
	
	private void iniSmtpData(String smtp_host,int port,String user_acc,String user_password){
		this.port = port;
		this.smtp_host = smtp_host;
		this.user_acc = user_acc;
		this.user_password = user_password;
	}
	
	private void iniProperty(){
		p = new Properties();
		p.put("mail.smtp.auth", "true");
		p.put("mail.transport.protocol", "smtp");
		p.put("mail.smtp.host", smtp_host);
		p.put("mail.smtp.port", String.valueOf(port));
	}
	//==============================================================
	public void createMailMessage(String title,String from) throws MessagingException{
		msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(from));
		msg.setRecipients(Message.RecipientType.TO, to_receipt_list.toArray(new Address[to_receipt_list.size()]));
		msg.setRecipients(Message.RecipientType.CC, cc_receipt_list.toArray(new Address[cc_receipt_list.size()]));
		msg.setRecipients(Message.RecipientType.BCC, bcc_receipt_list.toArray(new Address[bcc_receipt_list.size()]));
		msg.setSentDate(new Date());
		msg.setSubject(title);
		
		multi_mp = new MimeMultipart();
	}
	public void addMailTextBody(String content) throws MessagingException{
		addMailTextBody(content,"text/html;charset=UTF-8");
	}
	public void addMailTextBody(String content,String encode) throws MessagingException{
		MimeBodyPart mbp = new MimeBodyPart();
		mbp.setContent(content,encode);
		multi_mp.addBodyPart(mbp);
	}
	
	public void addReceipient(String adr,int mailReceiverGroup) throws AddressException{
		switch(mailReceiverGroup){
			case MailReceiverGroup_TO:
				to_receipt_list.add(new InternetAddress(adr));
				break;
			case MailReceiverGroup_CC:
				cc_receipt_list.add(new InternetAddress(adr));
				break;
			case MailReceiverGroup_BCC:
				bcc_receipt_list.add(new InternetAddress(adr));
				break;
		}
	}
	
	//===============================================================
	public void addAttachFile(String filepathWithName,String outputFileName,String fileencode) throws MessagingException, UnsupportedEncodingException{
		MimeBodyPart mbp = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(filepathWithName);
        mbp.setDataHandler(new DataHandler(fds));
        mbp.setFileName(MimeUtility.encodeText(outputFileName, fileencode, "B"));
        multi_mp.addBodyPart(mbp);
	}
	//===============================================================
	public void connectSMTPServer() throws MessagingException{
		session = Session.getInstance(p);
		tran = session.getTransport("smtp");
		tran.connect(smtp_host,port, user_acc, user_password);
	}
	public void send() throws AddressException, MessagingException{
		msg.setContent(multi_mp);
		tran.sendMessage(msg, msg.getAllRecipients());
	}
	
}
