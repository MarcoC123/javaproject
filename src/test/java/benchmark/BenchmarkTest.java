package benchmark;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import benchmark.executor.BenchmarkExecutor;

public class BenchmarkTest {
	
	   @Test
	    void testMeasureInsertBenchmark() throws SQLException {
	        BenchmarkExecutor benchmarkExecutor = new BenchmarkExecutor();

	        Connection connection = mock(Connection.class);
	        PreparedStatement preparedStatement = mock(PreparedStatement.class);

	        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

	        int commitFrequency = 5;
	        int numberOfExecutions = 20;

	        benchmarkExecutor.measureInsertBenchmark(connection, commitFrequency, numberOfExecutions);

	        verify(connection, times(4)).commit(); 
	    }
	   
	   @Test
	    void testMeasureSelectBenchmark() throws SQLException {
	        BenchmarkExecutor benchmarkExecutor = new BenchmarkExecutor();

	        Connection connection = mock(Connection.class);
	        PreparedStatement preparedStatement = mock(PreparedStatement.class);

	        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

	        int numberOfExecutions = 20;

	        benchmarkExecutor.measureSelectBenchmark(connection, numberOfExecutions);

	        verify(preparedStatement, times(20)).executeQuery(); 
	    }
	   
	   @Test
	    void testCleanTable() throws SQLException {
	        BenchmarkExecutor benchmarkExecutor = new BenchmarkExecutor();

	        Connection connection = mock(Connection.class);
	        PreparedStatement preparedStatement = mock(PreparedStatement.class);

	        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);


	        benchmarkExecutor.cleanTable(connection);

	        verify(connection, times(1)).commit(); 
	    }
}
