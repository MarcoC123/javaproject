package benchmark.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import benchmark.connector.BenchmarkConnector;

public class BenchmarkExecutor {
	
	private static final Logger logger = LoggerFactory.getLogger(BenchmarkExecutor.class);

	/**
	 * execute the three operation: measure insert, measure select and clean table
	 * @param connection
	 * @throws SQLException
	 */
	public void executeBenchmarks(Connection connection, int commitFrequency, int numberOfExecutions) {
		try {
			connection.setAutoCommit(false);

			measureInsertBenchmark(connection,commitFrequency, numberOfExecutions);

			measureSelectBenchmark(connection, numberOfExecutions);

			cleanTable(connection);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			BenchmarkConnector.closeConnection(connection);
		}
	}
	
	/**
	 * measure the min,max,avg time of execution for the SELECT
	 * @param connection
	 * @throws SQLException
	 */
	public void measureSelectBenchmark(Connection connection, int numberOfExecutions) throws SQLException {

		String selectSql = "SELECT * from benchmark_table WHERE id = ?";
		long minTime = Long.MAX_VALUE;
		long maxTime = Long.MIN_VALUE;

		long totalExecutionTime = 0;

		for (int i = 0; i < numberOfExecutions; i++) {
			long startTime = System.currentTimeMillis();

			try (PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {

				preparedStatement.setInt(1, i);

				preparedStatement.executeQuery();

				long executionTime = System.currentTimeMillis() - startTime;
				totalExecutionTime += executionTime;

				minTime = Math.min(minTime, executionTime);
				maxTime = Math.max(maxTime, executionTime);

			}
		}

		double avgTime = (double) totalExecutionTime / numberOfExecutions;
		
		logger.info("###### SELECT benchmark Results: ######");
		logger.info("Min Time: " +colorize(minTime) + " ms");
		logger.info("Max Time: " + colorize(maxTime) + " ms");
		logger.info("Avg Time: " + colorize(avgTime) + " ms \n");
	}

	/**
	 * measure the min,max,avg time of execution for the INSERT, the commit is setted for every 10 statements
	 * @param connection
	 * @throws SQLException
	 */
	public void measureInsertBenchmark(Connection connection, int commitFrequency,int numberOfExecutions) throws SQLException {

		String insertSql = "INSERT INTO benchmark_table (nome,indirizzo,numero_telefono,email) VALUES (?,?,?,?)";
		long minTime = Long.MAX_VALUE;
		long maxTime = Long.MIN_VALUE;

		long totalExecutionTime = 0;

		for (int i = 0; i < numberOfExecutions; i++) {
			long startTime = System.currentTimeMillis();

			try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

				preparedStatement.setString(1, "test_nome" + i);
				preparedStatement.setString(2, "test_indirizzo" + i);
				preparedStatement.setString(3, "+39 " + i);
				preparedStatement.setString(4, "test_email" + i + "@esempio.com");

				preparedStatement.executeUpdate();

				long executionTime = System.currentTimeMillis() - startTime;
				totalExecutionTime += executionTime;

				minTime = Math.min(minTime, executionTime);
				maxTime = Math.max(maxTime, executionTime);

				if((i+1) % commitFrequency == 0 || i == numberOfExecutions -1) {
					connection.commit();
				}
			}
		}

		double avgTime = (double) totalExecutionTime / numberOfExecutions;

		logger.info("###### INSERT benchmark Results: ######");
		logger.info("Min Time: " + colorize(minTime) + " ms");
		logger.info("Max Time: " + colorize(maxTime) + " ms");
		logger.info("Avg Time: " + colorize(avgTime) + " ms \n");
	}
	
	
	/**
	 * simple method used to color the logger
	 * @param value
	 * @return
	 */
	 private String colorize(Object value) {
	        return "\u001B[36m" + value + "\u001B[0m";
	    }
	
	/**
	 * method used for cleaning the table at the end of the execution
	 * @param connection
	 * @throws SQLException
	 */
	 public void cleanTable(Connection connection) throws SQLException {

		String deleteSql = "DELETE FROM benchmark_table";

		try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {

			int numLine = preparedStatement.executeUpdate();
			
			connection.commit();

			logger.info("###### rimosse " +colorize(numLine) + " righe di dati dalla tabella benchmark_table ###### \n");
		}
	}

}
