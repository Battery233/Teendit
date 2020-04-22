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
 * Servlet implementation class Logout
 */
@WebServlet("/logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			String userId = session.getAttribute("user_id").toString();
			session.invalidate();
			DBConnection connection = DBConnectionFactory.getConnection();
			try {
		  		 JSONObject input = RpcHelper.readJSONObject(request);
		  		 int timeViewed = input.getInt("time_viewed");
		  		 connection.updateTimeViewed(userId, timeViewed);
		  		
		  	 } catch (Exception e) {
		  		 e.printStackTrace();
		  	 } finally {
		  		 connection.close();
		  	 }
		}
		response.sendRedirect("index.html");
	}

}
