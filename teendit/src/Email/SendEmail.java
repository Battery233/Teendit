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

public class SendEmail {
	public static final String HOST = "smtp.gmail.com";
    public static final String PROTOCOL = "smtp";   
    public static final int PORT = 587;
    public static final String FROM = "teendit@gmail.com";// sender's email
    public static final String PWD = "Teendit17";// password
     
    /**
     * Get the session
     * @return
     */
    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);// host address
        props.put("mail.store.protocol" , PROTOCOL);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth" , "true");
         
        Authenticator authenticator = new Authenticator() {
 
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PWD);
            }
             
        };
        Session session = Session.getDefaultInstance(props , authenticator);
         
        return session;
    }

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
            msg.setSubject("Testing");
            msg.setSentDate(new Date());
            msg.setContent(content , "text/html;charset=utf-8");
  
            //Send the message
            Transport.send(msg);
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
    // public static void main(String[] args) {
    // 	//SendEmail test = new SendEmail();
    // 	SendEmail.send("yigengw@gmail.com", "<h2>Dear Parent,</h2><br><br><p>This is the link:</p><br><p>https://123.com</p>");
    // }
}
