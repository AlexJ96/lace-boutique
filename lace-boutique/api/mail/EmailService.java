package api.mail;

import java.io.StringWriter;
import java.util.Properties;

import javax.mail.Authenticator;
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

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class EmailService {
	
	private String username = "alex_football_2k8@hotmail.co.uk";
	private String password = "";
	
	public Session getEmailSession() {
	   Properties prop = new Properties();
	   prop.put("mail.smtp.auth", true);
	   prop.put("mail.smtp.starttls.enable", "true");
	   prop.put("mail.smtp.host", "smtp.live.com");
	   prop.put("mail.smtp.port", "587");
	   
	   Session session = Session.getInstance(prop, new Authenticator() {
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication(username, password);
		    }
		});
	   
	   return session;
	}
	
	public void testEmail() {
		
		Message message = new MimeMessage(getEmailSession());
		try {
			message.setFrom(new InternetAddress("alex_football_2k8@hotmail.co.uk"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("alex_football_2k8@hotmail.co.uk"));
			message.setSubject("Test Subject");
			 
			String msg = "This is my first email using JavaMailer";
			 
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.init();
			
			Template t = ve.getTemplate("velocity/template.vm");
			/* create a context and add data */
			VelocityContext context = new VelocityContext();
			StringWriter out = new StringWriter();
			t.merge(context, out);
			
			
			mimeBodyPart.setContent(out.toString(), "text/html");
			 
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			 
			message.setContent(multipart);
			 
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
