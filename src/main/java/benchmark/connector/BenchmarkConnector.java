package benchmark.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BenchmarkConnector {
	
	private static final Logger logger = LoggerFactory.getLogger(BenchmarkConnector.class);
	
	/**
	 * Method used for connecting to the database mysql
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String url, String username, String password) throws SQLException {
		logger.info("------ CONNECTING TO DATABASE ------ \n");
		return DriverManager.getConnection(url,username, password);
	}

	public static void closeConnection(Connection connection) {
		if(connection != null) {
			try {
				logger.info("------ CLOSING CONNECTION TO DATABASE ------ \n");
				connection.close();
			}catch(SQLException e ) {
				e.printStackTrace();
				logger.error("Something went wrong while closing the connection to the database");
			}
		}
	}
}
