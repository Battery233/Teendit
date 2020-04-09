package db.mysql;

/**
 * data base configuration information.
 */
public class MySQLDBUtil {
	private static final String HOSTNAME = "localhost";  //host address
	private static final String PORT_NUM = "3306";    // sql port 
	public static final String DB_NAME = "children";   // the database name
	private static final String USERNAME = "root";    // user name
	private static final String PASSWORD = "root";    // password
	public static final String URL = "jdbc:mysql://"
			+ HOSTNAME + ":" + PORT_NUM + "/" + DB_NAME
			+ "?user=" + USERNAME + "&password=" + PASSWORD
			+ "&autoReconnect=true&serverTimezone=UTC"
			+"&useUnicode=true&characterEncoding=utf-8";     //URL builder
}
