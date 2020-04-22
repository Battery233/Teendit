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
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			JSONObject input = RpcHelper.readJSONObject(request);
			
			String token = input.getString("token");
			String file = input.getString("base64_file");
			String encryptedFile = Crypto.encrypt(file);
			String password = input.getString("password");
			String format = input.getString("format");
			int timeView = input.getInt("time_to_view");
			
			String parentEmail = connection.getParentEmail(token);
			
			String randomName = UUID.randomUUID().toString()+"(" + format + ")";
			
			try {
				String filePath = "/Users/wyg/Desktop/encryption/" + randomName +".txt";  // To change later!!!
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
