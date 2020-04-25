package rpc;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Comment;
import entity.Comment.CommentBuilder;
import entity.Item;
import entity.Item.ItemBuilder;

/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/main")
public class SearchItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchItem() {
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
			List<Item> items = connection.getAllItems();
			
			JSONArray array = new JSONArray();
			for (Item item : items) {
				JSONObject obj = item.toJSONObject();
				List<Comment> comments = connection.getComments(userId, item.getItemId());
				for (Comment comment : comments) {
					JSONObject comObj = comment.toJSONObject();
					obj.append("comments", comObj);
				}
				array.put(obj);
			}
			RpcHelper.writeJsonArray(response, array);
			
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
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
	  		 JSONObject input = RpcHelper.readJSONObject(request);

	  		 if (input.has("item_id")) {
	  			 CommentBuilder builder = new CommentBuilder();
				 builder.setUserId(input.getString("user_id"));
				 builder.setItemId(input.getString("item_id"));
				 builder.setContent(input.getString("content"));
				 Comment comment = builder.build();
				 connection.addComments(comment);
				 
	  		 } else {
	  			 ItemBuilder builder = new ItemBuilder();
	  			 builder.setUserId(input.getString("user_id"));
				 builder.setName(input.getString("name"));
				 builder.setCategory(input.getString("category"));
				 builder.setContent(input.getString("content"));
				 Item item = builder.build();
				 connection.addItems(item);
	  		 }
			 
	  		 RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
	  		
	  	 } catch (Exception e) {
	  		 e.printStackTrace();
	  	 } finally {
	  		 connection.close();
	  	 }
	}
	
	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
	  		 JSONObject input = RpcHelper.readJSONObject(request);
	  		 String todeleteId;
	  		 if (input.has("comment_id")) {
	  			todeleteId = input.getString("comment_id");
	  		 } else {
	  			todeleteId = input.getString("item_id");
	  			connection.deleteItems(todeleteId);
	  		 }
	  		
	  		 RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
	  		
	  	 } catch (Exception e) {
	  		 e.printStackTrace();
	  	 } finally {
	  		 connection.close();
	  	 }
	}

}
