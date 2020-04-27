package Email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * The class used for generating and sending emails.
 */
public class SendEmail {
	public static final String HOST = "smtp.gmail.com";
    public static final String PROTOCOL = "smtp";   
    public static final int PORT = 587;
    public static final String FROM = "teendit@gmail.com"; // sender's email
    public static final String PWD = "Teendit17";// send's password
     
    /**
     * Get the Session
     * @return
     */
    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);//set the server address
        props.put("mail.store.protocol" , PROTOCOL);//set the protocol
        props.put("mail.smtp.port", PORT);//set the port
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth" , "true");// authentication
         
        Authenticator authenticator = new Authenticator() {
 
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PWD);
            }
             
        };
        Session session = Session.getDefaultInstance(props , authenticator);
         
        return session;
    }

    /**
     * The function for sending email.
     * @param toEmail
     * @param content
     */
    public static void send(String toEmail , String content) {
        Session session = getSession();
        try {
            System.out.println("--send--"+content);
            // Instantiate a message
            Message msg = new MimeMessage(session);
  
            //Set message attributes
            msg.setFrom(new InternetAddress(FROM));
            InternetAddress[] address = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject("Teendit Registration");
            msg.setSentDate(new Date());
            msg.setContent(content , "text/html;charset=utf-8");
  
            //Send the message
            Transport.send(msg);
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
