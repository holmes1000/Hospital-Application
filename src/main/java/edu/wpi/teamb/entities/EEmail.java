package edu.wpi.teamb.entities;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EEmail {
  //Disable SSL certificate validation
//  static {
//    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
//        (hostname, sslSession) -> hostname.equals("localhost"));
//  }
  public static void sendEmail(String recipient) {
    Properties properties = new Properties();

    //setups properties for email
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.host", "smtp.gmail.com");
    properties.put("mail.smtp.port", "587");

    //email account credentials
    String teambEmail = "teambd2023@gmail.com";
    String password = "cenpswqqtjrhzanj";

    Session session = Session.getInstance(properties, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(teambEmail, password);
      }
    });

    Message message = prepareMessage(session, teambEmail, recipient);
    try{
      Transport transport = session.getTransport("smtp");
      transport.connect("smtp.gmail.com", teambEmail, "cenpswqqtjrhzanj");
      transport.sendMessage(message, message.getAllRecipients());
      transport.close();
      System.out.println("Message sent successfully");
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  private static Message prepareMessage(Session session, String fromAddress, String toAddress) {
    Message message = new MimeMessage(session);
    try {
      message.setFrom(new InternetAddress(fromAddress));
      message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
      message.setSubject("Hi Jackson");
      message.setText("This is a test email/n Best regards, Team B");
      return message;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
