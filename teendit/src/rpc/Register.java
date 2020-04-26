package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import Email.SendEmail;
import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class Register
 */
@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			JSONObject input = RpcHelper.readJSONObject(request);
			String userId = input.getString("user_id");
			String password = input.getString("password");
			String email = input.getString("email");
			String parentEmail = input.getString("parent_email");
			
			JSONObject obj = new JSONObject();
			if (connection.registerUser(userId, password, email, parentEmail) && connection.registerParent(parentEmail, userId)) {
				String token = generateRandomString(20);  // Generate a random token
				connection.setToken(parentEmail, token);  // Set the token.
				// Send the email to parent.
				SendEmail.send(parentEmail, "<h2>Dear Parent,</h2><br><h2>Your child is attempting to create an account on our privacy protected social platform, Teendit, that helps teenagers expand their social network without exposing themselves to internet work's risks, such as social fraud, bullying, and digital kidnapping. We won't transfer your data to any other third party company.</h2>"
						+ "<br><h2>Since your child is between 13 to 18 years old, we need your approval to verify the child's and your guardianship of the child according to Children's Online Privacy Protection Rule (\"COPPA\"). This way, we can assure your child identity and relationship with you in real life, so as to avoid fake and malicious account on the platform. In addition, we would limit the time children use Teendit to prevent social media addiction.</h2>"
						+ "<br><h2>Please send documents that can prove your child's name, birthdate and your guardianship of the child, such as your ID, or driver licenses. Once we verify your identity, we would delete your file completely on our database and activate your child's account. Your document would be properly encrypted and if there are any questions, please email back to us, we will reply to you as soon as possible.</h2>"
						+ "<br><h3>Yours,</h3><h3>Teendit Customer Service</h3><br><br><h2>The link is: </h2><br><h4>https://17735child.tk/ToDoList/?token=" + token + "</h4>");
				obj.put("status", "OK");
			} else {
				obj.put("status", "User or Parent Already Exists");
			}
			RpcHelper.writeJsonObject(response, obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}
	
	/**
	 * Generate a random sequence for random token
	 * @param n   the length of the random sequence
	 * @return    a random string
	 */
	private String generateRandomString(int n) {
		// chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789!@$%*"
                                    + "abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuilder size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(n); 
  
        for (int i = 0; i < n; i++) { 
  
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index 
                = (int)(AlphaNumericString.length() 
                        * Math.random()); 
  
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString 
                          .charAt(index)); 
        } 
  
        return sb.toString();
	}

}
