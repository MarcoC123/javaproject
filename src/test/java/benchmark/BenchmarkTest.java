package benchmark;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import benchmark.executor.BenchmarkExecutor;

public class BenchmarkTest {
	
    @Test
    void executeBenchmarks() throws SQLException {
        BenchmarkExecutor benchmarkExecutor = new BenchmarkExecutor();

        Connection connection = mock(Connection.class);

        int commitFrequency = 5;
        int numberOfExecutions = 20;

        benchmarkExecutor.executeBenchmarks(connection, commitFrequency, numberOfExecutions);

        verify(connection).setAutoCommit(false);
        verify(connection).commit();
    }

}
