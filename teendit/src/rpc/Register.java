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
				String token = generateRandomString(20);
				connection.setToken(parentEmail, token);
				SendEmail.send(parentEmail, "<h2>Dear Parent,</h2><br><br><p>This is the link:</p><br><p>https://123.com</p>");
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
                                    + "0123456789!@#$%&*"
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
