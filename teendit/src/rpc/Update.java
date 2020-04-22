package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class Update
 */
@WebServlet("/update")
public class Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Update() {
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
			String userId = ""; 
			boolean isChildren = false;
			if (input.has("user_id")) {
				userId = input.getString("user_id");
				isChildren = true;
			} else if (input.has("parent_email")){
				userId = input.getString("parent_email");
			}
			
			if (input.has("password")) {
				String password = input.getString("password");
				connection.updatePassword(userId, password, isChildren);
			}
			
			if (input.has("email")) {
				String email = input.getString("email");
				connection.updateEmail(userId, email);
			}
			
			if (input.has("time_to_view")) {
				int timeView = input.getInt("time_to_view");
				connection.updateTimeToView(userId, timeView);
			}
			
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
