package db;
import io.github.cdimascio.dotenv.Dotenv;


public class MySQLDBUtil {
	static Dotenv dotenv = Dotenv.configure().load();
	private static final String INSTANCE = dotenv.get("MYSQL_INSTANCE");
	private static final String PORT_NUM = "3306";
	public static final String DB_NAME = "db_job";
	private static final String USERNAME = dotenv.get("MYSQL_USERNAME");
	private static final String PASSWORD = dotenv.get("MYSQL_PASSWORD");
	public static final String URL = "jdbc:mysql://"
			+ INSTANCE + ":" + PORT_NUM + "/" + DB_NAME
			+ "?user=" + USERNAME + "&password=" + PASSWORD
			+ "&autoReconnect=true&serverTimezone=UTC";

}
