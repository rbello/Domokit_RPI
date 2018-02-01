package fr.evolya.domokit.ctrl;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import fr.evolya.javatoolkit.code.annotations.AsynchOperation;
import fr.evolya.javatoolkit.code.funcint.Action;

public class Email {

	@AsynchOperation
	public static void sendEmailAsynch(String smtpHost, int smtpPort, String username, String password,
			String msgTitle, String from, String to, String msgContents, Action<MessagingException> callback) {
		sendEmailAsynch(smtpHost, smtpPort, username, password, msgTitle, from, to, msgContents, false, callback);
	}
	
	@AsynchOperation
	public static void sendEmailAsynch(String smtpHost, int smtpPort, String username, String password,
			String msgTitle, String from, String to, String msgContents, boolean debug, 
			Action<MessagingException> callback) {
		sendEmailAsynch(smtpHost, smtpPort, username, password, msgTitle, from, to, msgContents, debug, null, null, callback);
	}
	
	@AsynchOperation
	public static void sendEmailAsynch(String smtpHost, int smtpPort, String username, String password,
			String msgTitle, String from, String to, String msgContents, boolean debug,
			File attachment, String attachmentName, Action<MessagingException> callback) {
		
		new Thread(() -> {
			
			try {
				sendEmail(smtpHost, smtpPort, username, password, msgTitle, from, to, msgContents, debug, attachment, attachmentName);
				callback.call(null); // Mean success 
			}
			catch (MessagingException ex) {
				callback.call(ex);
			}
			
		}).start();
		
	}
	
	public static boolean sendEmail(String smtpHost, int smtpPort, String username, String password,
			String msgTitle, String from, String to, String msgContents) {
		try {
			sendEmail(smtpHost, smtpPort, username, password, msgTitle, from, to, msgContents, false);
			return true;
		}
		catch (MessagingException e) {
			return false;
		}
	}
	
	public static void sendEmail(String smtpHost, int smtpPort, String username, String password,
			String msgTitle, String from, String to, String msgContents, boolean debug)
					throws MessagingException {
		sendEmail(smtpHost, smtpPort, username, password, msgTitle, from, to, msgContents, debug, null, null);
	}
	
	public static void sendEmail(String smtpHost, int smtpPort, String username, String password,
			String msgTitle, String from, String to, String msgContents, boolean debug,
			File attachment, String attachmentName) throws MessagingException {
		
		Properties props = new Properties();
	    
	    props.put("mail.smtp.auth", true);
	    props.put("mail.smtp.ehlo", true);
	    props.put("mail.smtp.ssl.enable", true);
	    props.put("mail.smtp.starttls.enable", true);
	    props.put("mail.smtp.host", smtpHost);
	    props.put("mail.smtp.port", smtpPort);
	    props.put("mail.smtp.connectiontimeout", "10000");
	    
	    Session session = Session.getInstance(props,
	            new javax.mail.Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(username, password);
	                }
	            });
	    
	    session.setDebug(debug);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
        message.setSubject(msgTitle);
        message.setText(msgContents);

        if (attachment != null) {
	        MimeBodyPart messageBodyPart = new MimeBodyPart();
	        Multipart multipart = new MimeMultipart();
	        messageBodyPart = new MimeBodyPart();
	        String file = attachment.getAbsolutePath();
	        DataSource source = new FileDataSource(file);
	        messageBodyPart.setDataHandler(new DataHandler(source));
	        messageBodyPart.setFileName(attachmentName);
	        multipart.addBodyPart(messageBodyPart);
	        message.setContent(multipart);
        }

        Transport.send(message);

	}
	
}
