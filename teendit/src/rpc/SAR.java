package rpc;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Comment;
import entity.Item;

/**
 * Servlet implementation class SAR
 */
@WebServlet("/sar")
public class SAR extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SAR() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}
		String userId = session.getAttribute("user_id").toString(); 
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			List<Item> items = connection.getItems(userId); // All posts
			List<Comment> comments = connection.getUserComment(userId);  // All comments
			String email = connection.getEmail(userId);   // Teenager's email
			String parentEmail = connection.findParent(userId);  // Parent's email
			
			JSONObject obj = new JSONObject();
			obj.put("email", email);
			obj.put("parent_email", parentEmail);
			
			for (Item item : items) {
				JSONObject itmObj = item.toJSONObject();
				obj.append("posts", itmObj);
			}
			
			for (Comment comment : comments) {
				JSONObject comObj = comment.toJSONObject();
				obj.append("comments", comObj);
			}
			
			RpcHelper.writeJsonObject(response, obj);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
