package utility;

import java.sql.Connection;
import java.sql.SQLException;

public class JBDCUtil {

	public static void rollBackQuaetly(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeQuaetly(AutoCloseable resources) {
		if (resources != null) {
			try {
				resources.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeQuaetly(AutoCloseable... resources) {
		for (AutoCloseable res : resources) {
			closeQuaetly(res);
		}
	}

}
