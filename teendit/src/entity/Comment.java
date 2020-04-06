package entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Comment {
	private String commentId;
	private String userId;
	private String itemId;
	//private String name;
	private String content;
	//private String time;
	//private int checked;
	
	/**
	 * This is a builder pattern in Java.
	 */
	private Comment(CommentBuilder builder) {
		this.commentId = builder.commentId;
		this.userId = builder.userId;
		this.itemId = builder.itemId;
		//this.name = builder.name;
		this.content = builder.content;
		//this.time = builder.time;
		//this.checked = builder.checked;
	}
	
	public String getCommentId() {
		return commentId;
	}
	
	public String getItemId() {
		return itemId;
	}

	public String getUserId() {
		return userId;
	}

//	public String getName() {
//		return name;
//	}

	public String getContent() {
		return content;
	}

//	public String getTime() {
//		return time;
//	}

//	public int isChecked() {
//		return checked;
//	}
	
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("comment_id", commentId);
			obj.put("user_id", userId);
			obj.put("item_id", itemId);
//			obj.put("name", name);
			obj.put("content", content);
//			obj.put("time", time);
//			obj.put("checked", checked);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static class CommentBuilder {
		private String commentId;
		private String userId;
		private String itemId;
		//private String name;
		private String content;
		//private String time;
//		private int checked;
		
		
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
//		public ItemBuilder setName(String name) {
//			this.name = name;
//			return this;
//		}
		public CommentBuilder setContent(String content) {
			this.content = content;
			return this;
		}
//		public ItemBuilder setTime(String time) {
//			this.time = time;
//			return this;
//		}
//		public ItemBuilder setChecked(int checked) {
//			this.checked = checked;
//			return this;
//		}
		
		public Comment build() {
			return new Comment(this);
		}
	}


}
