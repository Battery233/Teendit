package entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Comment object.
 * Every comment is an comment object.
 * This object contains all the information of the comment, 
 * including comment Id, user Id, item Id and content.
 */
public class Comment {
	private String commentId;
	private String userId;
	private String itemId;
	private String content;
	
	/**
	 * This is a builder pattern in Java.
	 */
	private Comment(CommentBuilder builder) {
		this.commentId = builder.commentId;
		this.userId = builder.userId;
		this.itemId = builder.itemId;
		this.content = builder.content;
	}
	
	//getters for the values
	public String getCommentId() {
		return commentId;
	}
	
	public String getItemId() {
		return itemId;
	}

	public String getUserId() {
		return userId;
	}

	public String getContent() {
		return content;
	}

	
	// Create a json object using the values
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("comment_id", commentId);
			obj.put("user_id", userId);
			obj.put("item_id", itemId);
			obj.put("content", content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static class CommentBuilder {
		private String commentId;
		private String userId;
		private String itemId;
		private String content;
		
		
		// a builder class for create the comment objects and init values 
		public CommentBuilder setCommentId(String commentId) {
			this.commentId = commentId;
			return this;
		}
		public CommentBuilder setUserId(String userId) {
			this.userId = userId;
			return this;
		}
		public CommentBuilder setItemId(String itemId) {
			this.itemId = itemId;
			return this;
		}
		
		public CommentBuilder setContent(String content) {
			this.content = content;
			return this;
		}
		
		public Comment build() {
			return new Comment(this);
		}
	}


}
