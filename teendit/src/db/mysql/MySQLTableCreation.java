package db.mysql;

import java.sql.DriverManager;
//import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

/**
 * The tool class for creating a table in the db
 * and setup corresponding schema.
 * Run the main method the first time when the database is
 * created and connected.
 */
public class MySQLTableCreation {
	public static void main(String[] args) {
		try {
			// Step 1 Connect to MySQL.
			System.out.println("Connecting to " + MySQLDBUtil.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);
			
			if (conn == null) {
				return;
			}
			
			// Step 2 Drop tables in case they exist.
			Statement statement = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS items";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS users";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS comments";
			statement.executeUpdate(sql);
				
			sql = "DROP TABLE IF EXISTS replies";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS parents";
			statement.executeUpdate(sql);
			
			// Step 3 Create new tables
			sql = "CREATE TABLE items ("
					+ "item_id INT NOT NULL AUTO_INCREMENT,"
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "name VARCHAR(255),"
					+ "category VARCHAR(255),"
					+ "content VARCHAR(255),"
					+ "PRIMARY KEY (item_id)"
					+ ")";
			statement.executeUpdate(sql);
			
			sql = "CREATE TABLE comments ("
					+ "comment_id INT NOT NULL AUTO_INCREMENT,"
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "item_id INT NOT NULL,"
					+ "content VARCHAR(255),"
					+ "PRIMARY KEY (comment_id)"
					+ ")";
			statement.executeUpdate(sql);
			
			sql = "CREATE TABLE replies ("
					+ "reply_id INT NOT NULL AUTO_INCREMENT,"
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "comment_id INT NOT NULL,"
					+ "content VARCHAR(255),"
					+ "PRIMARY KEY (reply_id)"
					+ ")";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE users ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "password VARCHAR(255) NOT NULL,"
					+ "email VARCHAR(255) NOT NULL,"
					+ "parent_email VARCHAR(255),"
					+ "time_to_view INT,"
					+ "time_viewed INT,"
					+ "PRIMARY KEY (user_id)"
					+ ")";
			statement.executeUpdate(sql);
			
			sql = "CREATE TABLE parents ("
					+ "parent_email VARCHAR(255) NOT NULL,"
					+ "password VARCHAR(255) NOT NULL,"
					+ "user_id VARCHAR(255),"
					+ "time_to_view INT,"
					+ "file_name VARCHAR(255),"
					+ "PRIMARY KEY (parent_email)"
					+ ")";
			statement.executeUpdate(sql);
			
			conn.close();
			System.out.println("Import done successfully");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
