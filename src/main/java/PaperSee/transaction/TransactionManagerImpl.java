package transaction;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.Callable;

import dao.exceptions.DaoRuntimeException;
import static utility.JBDCUtil.*;
import static utility.LogPrinter.println;

public class TransactionManagerImpl extends BaseDataSource implements
		TransactionManager {

	private final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/paper_test?user=root&password=si17st18";
	private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

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
	public <T> T doInTransaction(Callable<T> unitOfWork) throws Exception {
		Connection conn = DriverManager.getConnection(JDBC_URL);
		conn.setAutoCommit(false);
		connectionHolder.set(conn);

		try {
			T result = unitOfWork.call();
			conn.commit();
			return result;
		} catch (SQLException e) {
			conn.rollback();
			throw e;
		} finally {
			closeQuaetly(conn);
			connectionHolder.remove();
		}

	}
	
	public Connection getConnection(){
		return connectionHolder.get();
	}

}
