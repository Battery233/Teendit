package rpc;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

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
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			String userId = "";
			String parentEmail = "";
			HttpSession session = request.getSession(false);
			JSONObject obj = new JSONObject();
			if (session != null) {
				Set<String> nameSet = new HashSet<>();
				Enumeration<String> valueNames = session.getAttributeNames();
		        if (valueNames.hasMoreElements()) {
		            while (valueNames.hasMoreElements()) {
		                String param = (String) valueNames.nextElement();
		                nameSet.add(param);
		            }
		        }
		        if (nameSet.contains("user_id")) {
		        	userId = session.getAttribute("user_id").toString();
		        }
		        if (nameSet.contains("parent_email")) {
		        	parentEmail = session.getAttribute("parent_email").toString();
		        }
				if (!userId.equals("")) {
					obj.put("status", "OK").put("user_id", userId).put("time", connection.getTime(userId)).put("time_viewed", connection.getTimeViewed(userId));
				} else {
					obj.put("status", "OK").put("parent_email", userId).put("time_to_view", connection.getTimeParent(parentEmail));
				}
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
			String parent_email = "";
			boolean isChildren = false;
			if (input.has("user_id")) {
				userId = input.getString("user_id");
				parent_email = connection.findParent(userId);
				isChildren = true;
			} else {
				userId = input.getString("parent_email");
			}
			String password = input.getString("password");
			
			JSONObject obj = new JSONObject();
			
			// Check the user ID and the password
			// Also check if the account is valid
			if ((isChildren && connection.verifyLogin(userId, password, isChildren) && connection.getFileName(parent_email).equals("Checked"))
					|| (!isChildren && connection.verifyLogin(userId, password, isChildren))) {
				if (isChildren && connection.getTime(userId) <= connection.getTimeViewed(userId)) {
					obj.put("status", "You don't have any time to view for today");
					response.setStatus(401);
				} else {
					HttpSession session = request.getSession();
					if (isChildren) {
						session.setAttribute("user_id", userId);
						session.setMaxInactiveInterval(600);
					} else {
						session.setAttribute("parent_email", userId);
						//session.setMaxInactiveInterval(60);
					}
					if (isChildren) {
						obj.put("status", "OK").put("user_id", userId).put("time", connection.getTime(userId)).put("time_viewed", connection.getTimeViewed(userId));
					} else {
						obj.put("status", "OK").put("parent_email", userId).put("time_to_view", connection.getTimeParent(userId));
					}
				}
			} else if (isChildren && connection.verifyLogin(userId, password, isChildren) && !connection.getFileName(parent_email).equals("Checked")) {
				obj.put("status", "User Haven't been verified");
				response.setStatus(401);
			} else if (!connection.verifyLogin(userId, password, isChildren)) {
				obj.put("status", "Wrong User Name or Password");
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
