package rpc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import Encryption.Crypto;
import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class Upload
 */
@WebServlet("/upload")
public class Upload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Upload() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			JSONObject input = RpcHelper.readJSONObject(request);
			
			String token = input.getString("token");  // Get the random token
			String file = input.getString("base64_file");  // Get the base64 string
			String encryptedFile = Crypto.encrypt(file);  // Encrypt the base64 string
			String password = input.getString("password");  // Get the password
			String format = input.getString("format");     // Get the format of the file
			int timeView = input.getInt("time_to_view");   // Get the time limit
			
			String parentEmail = connection.getParentEmail(token);
			
			// Generate a random file name for the uploaded ID
			String randomName = UUID.randomUUID().toString()+"(" + format + ")";
			
			try {
				// To change!!!
				String filePath = "/home/ubuntu/encryption/" + randomName +".txt";  // Store the encrypted base64 String in a local txt file.
	            FileOutputStream fos = new FileOutputStream(filePath);
	            fos.write(encryptedFile.getBytes());
	            fos.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			
			connection.addFileName(token, randomName);
			connection.updatePassword(parentEmail, password, false);
			connection.updateTimeToView(parentEmail, timeView);
			
			connection.deleteToken(parentEmail);
					
			JSONObject obj = new JSONObject();
			obj.put("status", "OK");
			RpcHelper.writeJsonObject(response, obj);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

}
