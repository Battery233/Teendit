package db;

import db.mysql.MySQLConnection;

/**
 * Tool class for database type selection
 */
public class DBConnectionFactory {
	// use sql as default db
	private static final String DEFAULT_DB = "mysql";
		
	public static DBConnection getConnection(String db) {
		switch (db) {
		case "mysql":
			return new MySQLConnection();
		case "mongodb":
			return null;  //maybe add mongodb support later
		default:
			throw new IllegalArgumentException("Invalid db:" + db);
		}

	}

	public static DBConnection getConnection() {
		return getConnection(DEFAULT_DB);  // Connect db
	}

}
