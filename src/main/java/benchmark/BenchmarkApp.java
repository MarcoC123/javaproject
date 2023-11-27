package benchmark;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import benchmark.connector.BenchmarkConnector;
import benchmark.executor.BenchmarkExecutor;

public class BenchmarkApp {

	private static final Logger logger = LoggerFactory.getLogger(BenchmarkApp.class);

	/**
	 * read the configuration file
	 * 
	 * @param filePath
	 * @return
	 */
	public static Properties loadProperties(String filePath) {
		Properties properties = new Properties();

		try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
			properties.load(fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return properties;
	}

	public static void main(String[] args) {

		String filePath = "src/main/resources/config.properties";

		Properties properties = loadProperties(filePath);

		// take the value of the properties used for connecting to the MYSQL database
		String url = properties.getProperty("db.url").trim();
		String username = properties.getProperty("db.username").trim();
		String password = properties.getProperty("db.password").trim();

		// take the value of the properties used for frequency of commits and number of
		// data inserted
		int commitFrequency = Integer.parseInt(properties.getProperty("db.frequency").trim());
		int numberOfExecutions = Integer.parseInt(properties.getProperty("db.numExecution").trim());

		logger.info("------STARTED APPLICATION------ \n");

		// attempt connection to the Database
		try (Connection connection = BenchmarkConnector.getConnection(url, username, password)) {
			BenchmarkExecutor benchmarkExecutor = new BenchmarkExecutor();

			benchmarkExecutor.executeBenchmarks(connection, commitFrequency, numberOfExecutions);

			logger.info("------END APPLICATION------ \n");

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Something went wrong during the application");
		}
	}

}
