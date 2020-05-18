package sk.tuke.gamestudio.service.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum JDBCManager {
	INSTANCE;
	
	private final String URL = "jdbc:postgresql://localhost/gamestudio";
	private final String USER = "postgres";
	private final String PASSWORD = "postgres";
	
	public Connection getNewConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	public String getURL() {
		return URL;
	}
	
	public String getUSER() {
		return USER;
	}
	
	public String getPASSWORD() {
		return PASSWORD;
	}
}
