package entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Item {
	private String itemId;
	private String userId;
	private String name;
	private String content;
	//private String time;
	//private int checked;
	
	/**
	 * This is a builder pattern in Java.
	 */
	private Item(ItemBuilder builder) {
		this.itemId = builder.itemId;
		this.userId = builder.userId;
		this.name = builder.name;
		this.content = builder.content;
		//this.time = builder.time;
		//this.checked = builder.checked;
	}
	
	public String getItemId() {
		return itemId;
	}

	public String getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

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
			obj.put("item_id", itemId);
			obj.put("user_id", userId);
//			obj.put("name", name);
			obj.put("content", content);
//			obj.put("time", time);
//			obj.put("checked", checked);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static class ItemBuilder {
		private String itemId;
		private String userId;
		private String name;
		private String content;
		//private String time;
//		private int checked;
		
		
		public ItemBuilder setItemId(String itemId) {
			this.itemId = itemId;
			return this;
		}
		public ItemBuilder setUserId(String userId) {
			this.userId = userId;
			return this;
		}
		public ItemBuilder setName(String name) {
			this.name = name;
			return this;
		}
		public ItemBuilder setContent(String content) {
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
		
		public Item build() {
			return new Item(this);
		}
	}


}
