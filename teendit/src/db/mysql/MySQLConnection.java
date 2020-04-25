package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db.DBConnection;
import entity.Comment;
import entity.Comment.CommentBuilder;
import entity.Item;
import entity.Reply;
import entity.Reply.ReplyBuilder;
import entity.Item.ItemBuilder;

/**
 * Tool class for database item adding, editing, deleting
 * and reading.
 */
public class MySQLConnection implements DBConnection {
	
	private Connection conn;
	
	// setup connection to the db
	public MySQLConnection() {
	  	 try {
	  		 Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
	  		 conn = DriverManager.getConnection(MySQLDBUtil.URL);
	  		
	  	 } catch (Exception e) {
	  		 e.printStackTrace();
	  	 }
	}

	@Override
	public void close() {
		if (conn != null) {
	  		 try {
	  			 conn.close();
	  		 } catch (Exception e) {
	  			 e.printStackTrace();
	  		 }
	  	 }
	}

	@Override
	public void addItems(Item item) {
		if (conn == null) {
			System.err.println("DB connection failed");
	  		return;
		}
		try {
	  		 String sql = "INSERT IGNORE INTO items VALUES (?, ?, ?, ?, ?)";
	  		 PreparedStatement ps = conn.prepareStatement(sql);
	  		 ps.setString(1, null);
	  		 ps.setString(2, item.getUserId());
	  		 ps.setString(3, item.getName());
	  		ps.setString(4, item.getCategory());
	  		 ps.setString(5, item.getContent());
	  		 //ps.setString(5, item.getTime());
	  		 //ps.setInt(4, item.isChecked());
	  		 ps.execute();
	  		
	  	 } catch (Exception e) {
	  		 e.printStackTrace();
	  	 }
	}
	
	@Override
	public void addComments(Comment comment) {
		if (conn == null) {
			System.err.println("DB connection failed");
	  		return;
		}
		try {
	  		 String sql = "INSERT IGNORE INTO comments VALUES (?, ?, ?, ?)";
	  		 PreparedStatement ps = conn.prepareStatement(sql);
	  		 ps.setString(1, null);
	  		 ps.setString(2, comment.getUserId());
	  		 ps.setString(3, comment.getItemId());
	  		 ps.setString(4, comment.getContent());
	  		 ps.execute();
	  		
	  	 } catch (Exception e) {
	  		 e.printStackTrace();
	  	 }
	}
	
	@Override
	public void addReplies(Reply reply) {
		if (conn == null) {
			System.err.println("DB connection failed");
	  		return;
		}
		try {
	  		 String sql = "INSERT IGNORE INTO replies VALUES (?, ?, ?, ?)";
	  		 PreparedStatement ps = conn.prepareStatement(sql);
	  		 ps.setString(1, null);
	  		 ps.setString(2, reply.getUserId());
	  		 //ps.setString(3, item.getName());
	  		 ps.setString(3, reply.getCommentId());
	  		 ps.setString(4, reply.getContent());
	  		 //ps.setString(5, item.getTime());
	  		 //ps.setInt(4, item.isChecked());
	  		 ps.execute();
	  		
	  	 } catch (Exception e) {
	  		 e.printStackTrace();
	  	 }
	}
	
	
	@Override
	public void updateItems(Item item) {
		if (conn == null) {
			System.err.println("DB connection failed");
	  		return;
		}
		try {
	  		 String sql = "UPDATE items SET content = ?"
	  		 		+ " WHERE user_id = ? AND item_id = ?";
	  		 PreparedStatement ps = conn.prepareStatement(sql);
	  		 //ps.setString(1, item.getName());
	  		 ps.setString(1, item.getContent());
	  		 //ps.setString(3, item.getTime());
	  		 //ps.setInt(2, item.isChecked());
	  		 ps.setString(2, item.getUserId());
	  		 ps.setInt(3, Integer.parseInt(item.getItemId()));
	  		 ps.executeUpdate();
	  		
	  	 } catch (Exception e) {
	  		 e.printStackTrace();
	  	 }
	}

	@Override
	public void deleteItems(String itemId) {
		if (conn == null) {
			System.err.println("DB connection failed");
	  		return;
	  	}
		try {
			String sql = "DELETE FROM items WHERE item_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(itemId));
			ps.execute();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteComments(String commentId) {
		if (conn == null) {
			System.err.println("DB connection failed");
	  		return;
	  	}
		try {
			String sql = "DELETE FROM comments WHERE comment_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(commentId));
			ps.execute();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteReplies(String replyId) {
		if (conn == null) {
			System.err.println("DB connection failed");
	  		return;
	  	}
		try {
			String sql = "DELETE FROM replies WHERE reply_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(replyId));
			ps.execute();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Override
//	public boolean containsItem(String userId, String itemId) {
//		if (conn == null) {
//			return false;
//		}
//		try {
//			String sql = "SELECT item_id FROM items WHERE user_id = ? AND item_id = ?";
//			PreparedStatement stmt = conn.prepareStatement(sql);
//			stmt.setString(1, userId);
//			stmt.setString(2, itemId);
//			
//			ResultSet rs = stmt.executeQuery();
//			
//			if (rs != null) {
//				return true;
//			}
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}

	@Override
	public List<Integer> getItemIds(String userId) {
		if (conn == null) {
			return new ArrayList<>();
		}
		List<Integer> items = new ArrayList<>();
		try {
			String sql = "SELECT item_id FROM items WHERE user_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				Integer itemId = rs.getInt("item_id");
				items.add(itemId);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Collections.sort(items);
		return items;
	}
	
	@Override
	public List<Item> getItems(String userId) {
		if (conn == null) {
			return new ArrayList<>();
		}
		
		List<Item> Items = new ArrayList<>();
		List<Integer> itemIds = getItemIds(userId);
		
		try {
			String sql = "SELECT * FROM items WHERE item_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			for (int itemId : itemIds) {
				stmt.setInt(1, itemId);
				
				ResultSet rs = stmt.executeQuery();
				
				ItemBuilder builder = new ItemBuilder();
				
				while (rs.next()) {
					builder.setItemId(rs.getString("item_id"));
					builder.setUserId(rs.getString("user_id"));
					//builder.setName(rs.getString("name"));
					builder.setContent(rs.getString("content"));
					//builder.setTime(rs.getString("time"));
					//builder.setChecked(rs.getInt("checked"));
					
					Items.add(builder.build());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Items;
	}
	
	@Override
	public List<Item> getAllItems() {
		if (conn == null) {
			return new ArrayList<>();
		}
		
		List<Item> Items = new ArrayList<>();
		
		try {
			String sql = "SELECT * FROM items";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			ItemBuilder builder = new ItemBuilder();
			
			while (rs.next()) {
				builder.setItemId(rs.getString("item_id"));
				builder.setUserId(rs.getString("user_id"));
				builder.setName(rs.getString("name"));
				builder.setCategory(rs.getString("category"));
				builder.setContent(rs.getString("content"));
				//builder.setTime(rs.getString("time"));
				//builder.setChecked(rs.getInt("checked"));
				
				Items.add(builder.build());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Items;
	}
	
	@Override
	public List<Integer> getCommentIds (String userId, String itemId) {
		if (conn == null) {
			return new ArrayList<>();
		}
		List<Integer> comments = new ArrayList<>();
		try {
			String sql = "SELECT comment_id FROM comments WHERE item_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, itemId);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				Integer commentId = rs.getInt("comment_id");
				comments.add(commentId);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Collections.sort(comments);
		return comments;
	}
	
	@Override
	public List<Comment> getComments(String userId, String itemId) {
		if (conn == null) {
			return new ArrayList<>();
		}
		
		List<Comment> comments = new ArrayList<>();
		List<Integer> commentIds = getCommentIds(userId, itemId);
		
		try {
			String sql = "SELECT * FROM comments WHERE comment_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			for (int commentId : commentIds) {
				stmt.setInt(1, commentId);
				
				ResultSet rs = stmt.executeQuery();
				
				CommentBuilder builder = new CommentBuilder();
				
				while (rs.next()) {
					builder.setCommentId(rs.getString("comment_id"));
					builder.setUserId(rs.getString("user_id"));
					builder.setItemId(rs.getString("item_id"));
					//builder.setName(rs.getString("name"));
					builder.setContent(rs.getString("content"));
					//builder.setTime(rs.getString("time"));
					//builder.setChecked(rs.getInt("checked"));
					
					comments.add(builder.build());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return comments;
	}

	@Override
	public List<Integer> getReplyIds (String userId, String commentId) {
		if (conn == null) {
			return new ArrayList<>();
		}
		List<Integer> replies = new ArrayList<>();
		try {
			String sql = "SELECT reply_id FROM replies WHERE comment_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, commentId);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				Integer replyId = rs.getInt("reply_id");
				replies.add(replyId);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Collections.sort(replies);
		return replies;
	}
	
	@Override
	public List<Reply> getReplies(String userId, String commentId) {
		if (conn == null) {
			return new ArrayList<>();
		}
		
		List<Reply> replies = new ArrayList<>();
		List<Integer> replyIds = getReplyIds(userId, commentId);
		
		try {
			String sql = "SELECT * FROM replies WHERE reply_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			for (int replyId : replyIds) {
				stmt.setInt(1, replyId);
				
				ResultSet rs = stmt.executeQuery();
				
				ReplyBuilder builder = new ReplyBuilder();
				
				while (rs.next()) {
					builder.setReplyId(rs.getString("reply_id"));
					builder.setUserId(rs.getString("user_id"));
					builder.setCommentId(rs.getString("comment_id"));
					//builder.setName(rs.getString("name"));
					builder.setContent(rs.getString("content"));
					//builder.setTime(rs.getString("time"));
					//builder.setChecked(rs.getInt("checked"));
					
					replies.add(builder.build());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return replies;
	}
	
	
	@Override
	public String getFullname(String userId) {
		if (conn == null) {
			return "";
		}
		String name = "";
		try {
			String sql = "SELECT first_name, last_name FROM users WHERE user_id = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				name = rs.getString("first_name") + " " + rs.getString("last_name");
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return name;
	}

	@Override
	public boolean verifyLogin(String userId, String password, boolean isChildren) {
		if (conn == null) {
			return false;
		}
		if (isChildren) {
			try {
				String sql = "SELECT user_id FROM users WHERE user_id = ? AND password = ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, userId);
				stmt.setString(2, password);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					return true;
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		} else {
			try {
				String sql = "SELECT parent_email FROM parents WHERE parent_email = ? AND password = ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, userId);
				stmt.setString(2, password);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					return true;
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return false;
	}
	
	@Override
	public boolean registerUser(String userId, String password, String email, String parentEmail) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return false;
		}

		try {
			String sql = "INSERT IGNORE INTO users VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, password);
			ps.setString(3, email);
			ps.setString(4, parentEmail);
			ps.setInt(5, -1);
			ps.setInt(6, 0);
			
			return ps.executeUpdate() == 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;	
	}
	
	@Override
	public boolean registerParent(String parentEmail, String userId) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return false;
		}

		try {
			String sql = "INSERT IGNORE INTO parents VALUES (?, ?, ?, ?, ?, ?)";
			String password = generateRandomString(13);
			String fileName = "No File Uploaded";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, parentEmail);
			ps.setString(2, password);
			ps.setString(3, userId);
			ps.setString(4, "Unset");
			ps.setInt(5, -1);
			ps.setString(6, fileName);
			
			return ps.executeUpdate() == 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Generate a random sequence for parent initial password
	 * @param n   the length of the random sequence
	 * @return    a random string
	 */
	private String generateRandomString(int n) {
		// chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789!@#$%&*"
                                    + "abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuilder size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(n); 
  
        for (int i = 0; i < n; i++) { 
  
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index 
                = (int)(AlphaNumericString.length() 
                        * Math.random()); 
  
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString 
                          .charAt(index)); 
        } 
  
        return sb.toString();
	}
	
	@Override
	public int getTime(String userId) {
		if (conn == null) {
			return -2;
		}
		int time = -2;
		try {
			String sql = "SELECT time_to_view FROM users WHERE user_id = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				time = rs.getInt("time_to_view");
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return time;
	}
	
	@Override
	public int getTimeViewed(String userId) {
		if (conn == null) {
			return 0;
		}
		int time = 0;
		try {
			String sql = "SELECT time_viewed FROM users WHERE user_id = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				time = rs.getInt("time_viewed");
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return time;
	}
	
	@Override
	public void addFileName(String token, String name) {
		if (conn == null) {
			return;
		}
		try {
			String sql = "UPDATE parents SET file_name = ? WHERE token = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, token);
			ps.execute();		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void deleteAccount(String userId) {
		if (conn == null) {
			return;
		}
		try {
			String sql = "DELETE FROM users WHERE user_id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.execute();	
			
			sql = "DELETE FROM parents WHERE user_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.execute();
			
			sql = "DELETE FROM items WHERE user_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.execute();
			
			sql = "DELETE FROM comments WHERE user_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.execute();
			
			sql = "DELETE FROM replies WHERE user_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.execute();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void updatePassword(String userId, String password, boolean isChildren) {
		if (conn == null) {
			System.err.println("DB connection failed");
	  		return;
		}
		if (isChildren) {
			try {
		  		 String sql = "UPDATE users SET password = ? WHERE user_id = ?";
		  		 PreparedStatement ps = conn.prepareStatement(sql);
		  		 ps.setString(1, password);
		  		 ps.setString(2, userId);
		  		 ps.executeUpdate();
		  		
		  	 } catch (Exception e) {
		  		 e.printStackTrace();
		  	 }
		} else {
			try {
		  		 String sql = "UPDATE parents SET password = ? WHERE parent_email = ?";
		  		 PreparedStatement ps = conn.prepareStatement(sql);
		  		 ps.setString(1, password);
		  		 ps.setString(2, userId);
		  		 ps.executeUpdate();
		  		
		  	 } catch (Exception e) {
		  		 e.printStackTrace();
		  	 }
		}
	}
	
	@Override
	public void updateEmail(String userId, String email) {
		if (conn == null) {
			System.err.println("DB connection failed");
	  		return;
		}
		
		try {
	  		 String sql = "UPDATE users SET email = ? WHERE user_id = ?";
	  		 PreparedStatement ps = conn.prepareStatement(sql);
	  		 ps.setString(1, email);
	  		 ps.setString(2, userId);
	  		 ps.executeUpdate();
	  		
	  	 } catch (Exception e) {
	  		 e.printStackTrace();
	  	 }	
	}
	
	@Override
	public void updateTimeToView(String userId, int timeView) {
		if (conn == null) {
			System.err.println("DB connection failed");
	  		return;
		}
		try {
	  		 String sql = "UPDATE parents SET time_to_view = ? WHERE parent_email = ?";
	  		 PreparedStatement ps = conn.prepareStatement(sql);
	  		 ps.setInt(1, timeView);
	  		 ps.setString(2, userId);
	  		 ps.executeUpdate();
	  		 
	  		 String childId = null;
	  		 sql = "SELECT user_id FROM parents WHERE parent_email = ?";
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 stmt.setString(1, userId);
			 ResultSet rs = stmt.executeQuery();
			 if (rs.next()) {
				 childId = rs.getString("user_id");
			 }
			 
			 sql = "UPDATE users SET time_to_view = ? WHERE user_id = ?";
			 PreparedStatement preS = conn.prepareStatement(sql);
			 preS.setInt(1, timeView);
			 preS.setString(2, childId);
			 preS.executeUpdate();
	  		 
	  	 } catch (Exception e) {
	  		 e.printStackTrace();
	  	 }
	}
	
	@Override
	public void updateTimeViewed(String userId, int timeViewed) {
		if (conn == null) {
			System.err.println("DB connection failed");
	  		return;
		}
		try {
			int time = 0;
			String sql = "SELECT time_viewed FROM users WHERE user_id = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				time = rs.getInt("time_viewed");
			}
			time += timeViewed;
	  		sql = "UPDATE users SET time_viewed = ? WHERE user_id = ?";
	  		PreparedStatement ps = conn.prepareStatement(sql);
	  		ps.setInt(1, time);
	  		ps.setString(2, userId);
	  		ps.executeUpdate();
	  		 
	  	 } catch (Exception e) {
	  		 e.printStackTrace();
	  	 }
	}
	
	@Override
	public void setToken(String parentEmail, String token) {
		if (conn == null) {
			System.err.println("DB connection failed");
	  		return;
		}
		try {
	  		 String sql = "UPDATE parents SET token = ? WHERE parent_email = ?";
	  		 PreparedStatement ps = conn.prepareStatement(sql);
	  		 ps.setString(1, token);
	  		 ps.setString(2, parentEmail);
	  		 ps.executeUpdate();
	  		 
	  	 } catch (Exception e) {
	  		 e.printStackTrace();
	  	 }
	}
	
	@Override
	public String getParentEmail(String token) {
		if (conn == null) {
			return "";
		}
		String email = "";
		try {
			String sql = "SELECT parent_email FROM parents WHERE token = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, token);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				email = rs.getString("parent_email");
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return email;
	}
	
	@Override
	public void deleteToken(String parentEmail) {
		if (conn == null) {
			System.err.println("DB connection failed");
	  		return;
		}
		try {
	  		 String sql = "UPDATE parents SET token = ? WHERE parent_email = ?";
	  		 PreparedStatement ps = conn.prepareStatement(sql);
	  		 String token = "Expired";
	  		 ps.setString(1, token);
	  		 ps.setString(2, parentEmail);
	  		 ps.executeUpdate();
	  		 
	  	 } catch (Exception e) {
	  		 e.printStackTrace();
	  	 }
	}
	
	@Override
	public String findParent(String userId) {
		if (conn == null) {
			return "";
		}
		String email = "";
		try {
			String sql = "SELECT parent_email FROM users WHERE user_id = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				email = rs.getString("parent_email");
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return email;
	}
	
	@Override
	public String getFileName(String parent_email) {
		if (conn == null) {
			return "";
		}
		String name = "";
		try {
			String sql = "SELECT file_name FROM parents WHERE parent_email = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, parent_email);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				name = rs.getString("file_name");
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return name;
	}
}
