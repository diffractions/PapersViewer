package utility;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

public class JBDCUtil {

	private final static String NET_START_MYSQL = "cmd start cmd.exe /c net start mysql";

	public static Connection getConnection(String JDBC_URL) throws SQLException {
		Connection conn;
		try {
			conn = DriverManager.getConnection(JDBC_URL);
		} catch (SQLException e) {
			Logger.getLogger("LOG").error("FOUND EXCEPTION IN CONNECTION, TRY RECONNECT", e);
			try {
				Process proc = Runtime.getRuntime().exec(NET_START_MYSQL);
				proc.waitFor();
				conn = DriverManager.getConnection(JDBC_URL);
				Logger.getLogger("LOG").info("RECONNECT.OK");
			} catch (IOException | SQLException | InterruptedException e1) {
				try {
					e1.initCause(e);
				} catch (Exception e2) {
					Logger.getLogger("LOG").error("EXCEPTION IN INIT CAUSE", e2);
				}
				Logger.getLogger("LOG").info("RECONNECT.FAILURE");
				throw new SQLException(e1);
			}
		}
		return conn;
	}

	public static void deregisterDrivers() {
		try {
			Enumeration<Driver> dr = DriverManager.getDrivers();
			if (dr.hasMoreElements() == true)
				while (dr.hasMoreElements()) {
					Driver d = dr.nextElement();
					DriverManager.deregisterDriver(d);
					Logger.getLogger("LOG").info("DEREGISTER DRIVER: " + d);
				}
		} catch (Exception e1) {
			Logger.getLogger("LOG").fatal("", e1);
		}

		try {
			AbandonedConnectionCleanupThread.shutdown();
		} catch (InterruptedException e) {
			Logger.getLogger("LOG").fatal("SEVERE problem cleaning up: " + e.getMessage(), e);
		}
	}

	public static void registerDrivers() {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			Enumeration<Driver> dr = DriverManager.getDrivers();
			if (dr.hasMoreElements() == false)
				throw new NullPointerException("DRIVERS NOT FOUNDS");
			else {
				Logger.getLogger("LOG").info("REGISTER DRIVER: " + dr.nextElement());
			}
		} catch (Exception e1) {
			Logger.getLogger("LOG").fatal("", e1);
		}
	}

	public static void rollBackQuaetly(Connection conn) throws SQLException {
		if (conn != null) {
			conn.rollback();
		}
	}

	public static void closeQuaetly(AutoCloseable resources) throws Exception {
		if (resources != null) {
			resources.close();
		}
	}

	public static void closeQuaetly(AutoCloseable... resources)
			throws Exception {
		for (AutoCloseable res : resources) {
			closeQuaetly(res);
		}
	}

}
