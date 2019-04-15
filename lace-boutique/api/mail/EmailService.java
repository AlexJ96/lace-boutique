package api.mail;

import java.io.StringWriter;
import java.util.List;
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
import org.eclipse.jetty.util.StringUtil;

import api.sql.hibernate.entities.Account;
import api.sql.hibernate.entities.Order;
import api.sql.hibernate.entities.OrderItem;
import api.sql.hibernate.entities.PasswordReset;

public class EmailService {
	
	private String username = "alex_football_2k8@hotmail.co.uk";
	private String password = "O1s2c3a4r5!";
	
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
	
	
	/**
	 * Sends an Email to the account, informing of their account registration.
	 * @param account
	 * @return
	 */
	public boolean sendNewAccountRegistrationEmail(Account account) {
		
		if (account.getEmailAddress() == null || StringUtil.isBlank(account.getEmailAddress())) {
			return false;
		}
		
		Message message = new MimeMessage(getEmailSession());
		try {
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(account.getEmailAddress()));
			message.setSubject("Lace Boutique - Account Registered");
			 
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.init();
			
			Template t = ve.getTemplate("velocity/accountRegistration.vm");
			
			VelocityContext context = new VelocityContext();
			context.put("name", account.getFirstName());
			context.put("email", account.getEmailAddress());
			
			StringWriter out = new StringWriter();
			t.merge(context, out);
		
			mimeBodyPart.setContent(out.toString(), "text/html");
			 
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			 
			message.setContent(multipart);
			 
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Sends Reset Password Email
	 * @param account
	 * @param resetCode
	 * @return
	 */
	public boolean sendResetPasswordEmail(Account account, PasswordReset passwordReset) {
		
		if (account.getEmailAddress() == null || StringUtil.isBlank(account.getEmailAddress())) {
			return false;
		}
		
		Message message = new MimeMessage(getEmailSession());
		try {
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(account.getEmailAddress()));
			message.setSubject("Lace Boutique - Reset Password");
			 
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.init();
			
			Template t = ve.getTemplate("velocity/passwordReset.vm");
			
			VelocityContext context = new VelocityContext();
			context.put("name", account.getFirstName());
			context.put("email", account.getEmailAddress());
			context.put("resetCode", passwordReset.getCode());
			
			StringWriter out = new StringWriter();
			t.merge(context, out);
		
			mimeBodyPart.setContent(out.toString(), "text/html");
			 
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			 
			message.setContent(multipart);
			 
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Sends Password Changed Email
	 * @param account
	 * @param resetCode
	 * @return
	 */
	public boolean sendPasswordChangedEmail(Account account, PasswordReset passwordReset) {
		
		if (account.getEmailAddress() == null || StringUtil.isBlank(account.getEmailAddress())) {
			return false;
		}
		
		Message message = new MimeMessage(getEmailSession());
		try {
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(account.getEmailAddress()));
			message.setSubject("Lace Boutique - Password Changed");
			 
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.init();
			
			Template t = ve.getTemplate("velocity/passwordUpdated.vm");
			
			VelocityContext context = new VelocityContext();
			context.put("name", account.getFirstName());
			context.put("email", account.getEmailAddress());
			context.put("resetCode", passwordReset.getCode());
			
			StringWriter out = new StringWriter();
			t.merge(context, out);
		
			mimeBodyPart.setContent(out.toString(), "text/html");
			 
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			 
			message.setContent(multipart);
			 
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Sends Order Plaed Email
	 * @param order
	 * @param orderItems
	 * @return
	 */
	public boolean sendOrderConfirmedEmail(Order order, List<OrderItem> orderItems) {
		
		Message message = new MimeMessage(getEmailSession());
		try {
			message.setFrom(new InternetAddress(username));
			if (order.getAccount() != null)
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(order.getAccount().getEmailAddress()));
			else 
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(order.getOrderDetails().getEmailAddress()));
			message.setSubject("Lace Boutique - Order Confirmed");
			 
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.init();
			
			Template t = ve.getTemplate("velocity/orderPlaced.vm");
			
			VelocityContext context = new VelocityContext();
			if (order.getAccount() != null) {
				context.put("name", order.getAccount().getFirstName());
				context.put("email", order.getAccount().getEmailAddress());
			} else {
				context.put("name", order.getOrderDetails().getFirstName());
				context.put("email", order.getOrderDetails().getEmailAddress());	
			}
			context.put("orderItems", orderItems);
			context.put("order", order);
			
			StringWriter out = new StringWriter();
			t.merge(context, out);
		
			mimeBodyPart.setContent(out.toString(), "text/html");
			 
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			 
			message.setContent(multipart);
			 
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Sends Order Updated Email
	 * @param order
	 * @param orderItems
	 * @return
	 */
	public boolean sendOrderUpdatedEmail(Order order) {
		
		Message message = new MimeMessage(getEmailSession());
		try {
			message.setFrom(new InternetAddress(username));
			if (order.getAccount() != null)
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(order.getAccount().getEmailAddress()));
			else 
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(order.getOrderDetails().getEmailAddress()));
			message.setSubject("Lace Boutique - Order Updated");
			 
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.init();
			
			Template t = ve.getTemplate("velocity/orderUpdated.vm");
			
			VelocityContext context = new VelocityContext();
			if (order.getAccount() != null) {
				context.put("name", order.getAccount().getFirstName());
				context.put("email", order.getAccount().getEmailAddress());
			} else {
				context.put("name", order.getOrderDetails().getFirstName());
				context.put("email", order.getOrderDetails().getEmailAddress());	
			}
			context.put("order", order);
			
			StringWriter out = new StringWriter();
			t.merge(context, out);
		
			mimeBodyPart.setContent(out.toString(), "text/html");
			 
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			 
			message.setContent(multipart);
			 
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Sends Order Delivered Email
	 * @param order
	 * @param orderItems
	 * @return
	 */
	public boolean sendOrderDeliveredEmail(Order order) {
		
		Message message = new MimeMessage(getEmailSession());
		try {
			message.setFrom(new InternetAddress(username));
			if (order.getAccount() != null)
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(order.getAccount().getEmailAddress()));
			else 
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(order.getOrderDetails().getEmailAddress()));
			message.setSubject("Lace Boutique - Order Delivered");
			 
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.init();
			
			Template t = ve.getTemplate("velocity/orderDelivered.vm");
			
			VelocityContext context = new VelocityContext();
			if (order.getAccount() != null) {
				context.put("name", order.getAccount().getFirstName());
				context.put("email", order.getAccount().getEmailAddress());
			} else {
				context.put("name", order.getOrderDetails().getFirstName());
				context.put("email", order.getOrderDetails().getEmailAddress());	
			}
			context.put("order", order);
			
			StringWriter out = new StringWriter();
			t.merge(context, out);
		
			mimeBodyPart.setContent(out.toString(), "text/html");
			 
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			 
			message.setContent(multipart);
			 
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Order Cancelled Email
	 * @param order
	 * @return
	 */
	public boolean sendOrderCancelledEmail(Order order) {
		
		Message message = new MimeMessage(getEmailSession());
		try {
			message.setFrom(new InternetAddress(username));
			if (order.getAccount() != null)
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(order.getAccount().getEmailAddress()));
			else 
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(order.getOrderDetails().getEmailAddress()));
			message.setSubject("Lace Boutique - Order Cancelled");
			 
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			
			VelocityEngine ve = new VelocityEngine();
			ve.init();
			
			Template t = ve.getTemplate("velocity/orderCancelled.vm");
			
			VelocityContext context = new VelocityContext();
			if (order.getAccount() != null) {
				context.put("name", order.getAccount().getFirstName());
				context.put("email", order.getAccount().getEmailAddress());
			} else {
				context.put("name", order.getOrderDetails().getFirstName());
				context.put("email", order.getOrderDetails().getEmailAddress());	
			}
			context.put("order", order);
			
			StringWriter out = new StringWriter();
			t.merge(context, out);
		
			mimeBodyPart.setContent(out.toString(), "text/html");
			 
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);
			 
			message.setContent(multipart);
			 
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
}
