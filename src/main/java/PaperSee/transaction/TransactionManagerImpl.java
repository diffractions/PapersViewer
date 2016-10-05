package transaction;

//import java.io.IOException;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import javax.sql.DataSource;

import transaction.exception.TransactionException;
import utility.JBDCUtil;
import static utility.JBDCUtil.closeQuaetly;

public class TransactionManagerImpl extends BaseDataSource implements
		TransactionManager, DataSource {


	private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();
	private final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/paper?user=root&password=si17st18";
private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	@Override
	public <T> T doInTransaction(Callable<T> unitOfWork)
			throws TransactionException {

		Connection conn = null;
		T result = null;

		try {
			conn = JBDCUtil.getConnection(JDBC_URL);
//			conn = dataSource.getConnection();
		} catch (SQLException e) {
			throw new TransactionException("Failed to connect to database",e);
		}

		try {
			conn.setAutoCommit(false);
			connectionHolder.set(conn);

			try {
				result = unitOfWork.call();
			} catch (Exception e) {
				throw new SQLException(e);
			}

			conn.commit();
			return result;
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e.addSuppressed(e1);
			}
			throw new TransactionException(e);
		} finally {
			try {
				closeQuaetly(conn);
			} catch (Exception e) {
				throw new TransactionException(e);
			}
			connectionHolder.remove();
		}

	}

	@Override
	public Connection getConnection() {
		return connectionHolder.get();
	}



}
