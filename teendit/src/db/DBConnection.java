package db;

import java.util.List;

import entity.Comment;
import entity.Item;
import entity.Reply;

/**
 * The tool interface for establishing connections with databases.
 */
public interface DBConnection {
	/**
	 * Close the connection.
	 */
	public void close();	

	/**
	 * Insert a item for a user.
	 * 
	 * @param item
	 */
	public void addItems(Item item);
	
	public void addComments(Comment comment);
	
	public void addReplies(Reply reply);
	
	/**
	 * Update a item for a user.
	 * 
	 * @param item
	 */
	public void updateItems(Item item);
	
	/**
	 * Delete a item for a user.
	 * 
	 * @param itemId
	 */
	public void deleteItems(String itemId);
	
	/**
	 * Delete a comment for a user.
	 * 
	 * @param commentId
	 */
	public void deleteComments(String commentId);
	
	/**
	 * Delete a reply for a user.
	 * 
	 * @param replyId
	 */
	public void deleteReplies(String replyId);
	
//	/**
//	 * Check if the item exists under the user
//	 * 
//	 * @param userId
//	 * @param itemId
//	 * @return true or false
//	 */
//	public boolean containsItem(String userId, String itemId);
	
	/**
	 * Gets item id based on user id
	 * 
	 * @param userId
	 * @return set of item id
	 */
	public List<Integer> getItemIds(String userId);
	
	/**
	 * Gets item set based on item id
	 * 
	 * @param itemId
	 * @return set of item
	 */
	public List<Item> getItems(String userId);
	
	public List<Item> getAllItems();

	/**
	 * Get full name of a user. (This is not needed for main course, just for demo
	 * and extension).
	 * 
	 * @param userId
	 * @return full name of the user
	 */
	public String getFullname(String userId);

	/**
	 * Return whether the credential is correct. (This is not needed for main
	 * course, just for demo and extension)
	 * 
	 * @param userId
	 * @param password
	 * @param isChildren
	 * @return boolean
	 */
	public boolean verifyLogin(String userId, String password, boolean isChildren);
	
	/**
	 * Register one user
	 * 
	 * @param userId
	 * @param password
	 * @param firstname
	 * @param lastname
	 * @return boolean
	 */
	public boolean registerUser(String userId, String password, String email, String parentEmail);
	
	/**
	 * Register one parent account
	 * @param parentEmail
	 * @param userId
	 * @return  boolean
	 */
	public boolean registerParent(String parentEmail, String userId);
	
	/**
	 * Get comment id based on item id
	 * @param userId
	 * @param itemId
	 * @return  list of comment IDs
	 */
	public List<Integer> getCommentIds (String userId, String itemId);
	
	/**
	 * Get comment based on item id
	 * @param userId
	 * @param itemId
	 * @return list of comments
	 */
	public List<Comment> getComments(String userId, String itemId);
	
	/**
	 * Get reply id based on comment id
	 * @param userId
	 * @param commentId
	 * @return  list of reply IDs
	 */
	public List<Integer> getReplyIds (String userId, String commentId);
	
	/**
	 * Get reply based on comment id
	 * @param userId
	 * @param commentId
	 * @return  list of replies
	 */
	public List<Reply> getReplies(String userId, String commentId);
	
	/**
	 * Get the time limit for viewing web sites
	 * @param userId
	 * @return  the time limit number
	 */
	public int getTime(String userId);
	
	/**
	 * Get the time the user view today
	 * @param userId
	 * @return
	 */
	public int getTimeViewed(String userId);
	
	/**
	 * Add the random file name in the database
	 * @param token
	 * @param name
	 */
	public void addFileName(String token, String name);
	
	/**
	 * Delete all the account info of the user
	 * @param userId
	 */
	public void deleteAccount(String userId);
	
	/**
	 * Update the password for the child or the parent
	 * @param userId
	 * @param password
	 * @param isChildren
	 */
	public void updatePassword(String userId, String password, boolean isChildren);
	
	/**
	 * Update the email address for the child
	 * @param userId
	 * @param email
	 */
	public void updateEmail(String userId, String email);
	
	/**
	 * Change the time limit for viewing web sites
	 * @param userId
	 * @param timeView
	 */
	public void updateTimeToView(String userId, int timeView);
	
	/**
	 * Change the time of viewing for each day
	 * @param userId
	 * @param timeViewed
	 */
	public void updateTimeViewed(String userId, int timeViewed);
	
	public void setToken(String parentEmail, String token);
	
}
