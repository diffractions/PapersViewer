package transaction;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import transaction.exception.TransactionException;
import dao.exceptions.DaoRuntimeException;
import static utility.JBDCUtil.*;
import static utility.LogPrinter.println;

public class TransactionManagerImpl extends BaseDataSource implements
		TransactionManager {

	private final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/paper_test?user=root&password=si17st18";
	private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();
	private final String NET_START_MYSQL = "cmd start cmd.exe /c net start mysql";

	public void registrDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Enumeration<Driver> dr = DriverManager.getDrivers();
			if (dr.hasMoreElements() == false)
				throw new NullPointerException("DRIVERS NOT FOUNDS");
			while (dr.hasMoreElements()) {
				println("drivers - " + dr.nextElement());
			}
		} catch (Exception e1) {
			throw new DaoRuntimeException(e1);
		}
	}

	@Override
	public <T> T doInTransaction(Callable<T> unitOfWork)
			throws TransactionException {

		Connection conn = null;
		T result = null;

		try {
			conn = DriverManager.getConnection(JDBC_URL);
		} catch (SQLException e) {
			println("FOUND EXCEPTION IN CONNECTION, TRY RECONNECT", e);
			try {
				Process proc = Runtime.getRuntime().exec(NET_START_MYSQL);
				proc.waitFor();
				conn = DriverManager.getConnection(JDBC_URL);
				println("RECONNECT.OK");
			} catch (IOException | SQLException | InterruptedException e1) {
				try {
					e1.initCause(e);
				} catch (Exception e2) {
					println("EXCEPTION IN INIT CAUSE", e2);
				}
				println("RECONNECT.FAILURE");
				throw new TransactionException(e1);
			}
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

	public Connection getConnection() {
		return connectionHolder.get();
	}

}
