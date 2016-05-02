package utility;

import java.sql.Connection;
import java.sql.SQLException;

public class JBDCUtil {

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
