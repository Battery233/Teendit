package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			HttpSession session = request.getSession(false);
			JSONObject obj = new JSONObject();
			if (session != null) {
				String userId = session.getAttribute("user_id").toString();
				obj.put("status", "OK").put("user_id", userId).put("time", connection.getTime(userId)).put("time_viewed", connection.getTimeViewed(userId));
			} else {
				obj.put("status", "Invalid Session");
				response.setStatus(403);
			}
			RpcHelper.writeJsonObject(response, obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			JSONObject input = RpcHelper.readJSONObject(request);
			String userId;
			boolean isChildren = false;
			if (input.has("user_id")) {
				userId = input.getString("user_id");
				isChildren = true;
			} else {
				userId = input.getString("parent_email");
			}
			String password = input.getString("password");
			
			JSONObject obj = new JSONObject();
			if ((isChildren && connection.verifyLogin(userId, password, isChildren) && connection.getTime(userId) != -1) || (!isChildren && connection.verifyLogin(userId, password, isChildren))) {
				HttpSession session = request.getSession();
				if (isChildren) {
					session.setAttribute("user_id", userId);
				} else {
					session.setAttribute("parent_email", userId);
				}
				session.setMaxInactiveInterval(600);
				if (isChildren) {
					obj.put("status", "OK").put("user_id", userId).put("time", connection.getTime(userId)).put("time_viewed", connection.getTimeViewed(userId));
				} else {
					obj.put("status", "OK").put("parent_email", userId);
				}
			} else if (isChildren && connection.verifyLogin(userId, password, isChildren) && connection.getTime(userId) == -1) {
				obj.put("status", "User Haven't been verified");
				response.setStatus(401);
			} else if (isChildren && connection.verifyLogin(userId, password, isChildren) && connection.getTime(userId) <= connection.getTimeViewed(userId)) {
				obj.put("status", "You don't have any time to view for today");
				response.setStatus(401);
			} else {
				obj.put("status", "User Doesn't Exist");
				response.setStatus(401);
			}
			RpcHelper.writeJsonObject(response, obj);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

}
