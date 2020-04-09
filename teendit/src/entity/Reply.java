package entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Reply object.
 * Every reply is an reply object.
 * This object contains all the information of the reply, 
 * including reply Id, user Id, comment Id and content.
 */
public class Reply {
	private String replyId;
	private String userId;
	private String commentId;
	//private String name;
	private String content;
	//private String time;
	//private int checked;
	
	/**
	 * This is a builder pattern in Java.
	 */
	private Reply(ReplyBuilder builder) {
		this.replyId = builder.replyId;
		this.userId = builder.userId;
		this.commentId = builder.commentId;
		//this.name = builder.name;
		this.content = builder.content;
		//this.time = builder.time;
		//this.checked = builder.checked;
	}
	
	//getters for the values
	public String getReplyId() {
		return commentId;
	}
	
	public String getCommentId() {
		return commentId;
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
	
	// Create a json object using the values
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("reply_id", replyId);
			obj.put("user_id", userId);
			obj.put("comment_id", commentId);
//			obj.put("name", name);
			obj.put("content", content);
//			obj.put("time", time);
//			obj.put("checked", checked);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	// a builder class for create the reply objects and init values
	public static class ReplyBuilder {
		private String replyId;
		private String userId;
		private String commentId;
		//private String name;
		private String content;
		//private String time;
//		private int checked;
		
		
		public ReplyBuilder setReplyId(String replyId) {
			this.replyId = replyId;
			return this;
		}
		public ReplyBuilder setUserId(String userId) {
			this.userId = userId;
			return this;
		}
		public ReplyBuilder setCommentId(String commentId) {
			this.commentId = commentId;
			return this;
		}
//		public ItemBuilder setName(String name) {
//			this.name = name;
//			return this;
//		}
		public ReplyBuilder setContent(String content) {
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
		
		public Reply build() {
			return new Reply(this);
		}
	}
}
