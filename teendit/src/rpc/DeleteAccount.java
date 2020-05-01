package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class DeleteAccount
 */
@WebServlet("/deleteaccount")
public class DeleteAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteAccount() {
        super();
    }
	
	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 * Handle the GET reuqest to delete all accoount info.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}
		String toDeleteId = session.getAttribute("user_id").toString();  // Get the user ID
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
	  		 connection.deleteAccount(toDeleteId);
	  		 session.invalidate();
	  		
	  		 response.sendRedirect("index.html");
	  		
	  	 } catch (Exception e) {
	  		 e.printStackTrace();
	  	 } finally {
	  		 connection.close();
	  	 }
	}

}
