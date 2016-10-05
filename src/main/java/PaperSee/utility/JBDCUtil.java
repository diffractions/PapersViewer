package utility;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Enumeration;
import java.util.Map;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

public class JBDCUtil {

	public static Logger log = Logger.getLogger("LOG");

	private final static String NET_START_MYSQL = "cmd start cmd.exe /c net start mysql";

	public static Connection getConnection(String JDBC_URL) throws SQLException {
		Connection conn;
		try {
			conn = DriverManager.getConnection(JDBC_URL);
		} catch (SQLException e) {
			log.error("FOUND EXCEPTION IN CONNECTION, TRY RECONNECT", e);
			try {
				Process proc = Runtime.getRuntime().exec(NET_START_MYSQL);
				proc.waitFor();
				conn = DriverManager.getConnection(JDBC_URL);
				log.info("RECONNECT.OK");
			} catch (IOException | SQLException | InterruptedException e1) {
				try {
					e1.initCause(e);
				} catch (Exception e2) {
					log.error("EXCEPTION IN INITIALIZE CAUSE", e2);
				}
				log.info("RECONNECT.FAILURE");
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
					log.info("DEREGISTER DRIVER: " + d);
				}
		} catch (Exception e1) {
			log.fatal("", e1);
		}

		try {
			AbandonedConnectionCleanupThread.shutdown();
		} catch (InterruptedException e) {
			log.fatal("SEVERE problem cleaning up: " + e.getMessage(), e);
		}
	}

	public static void registerDrivers() {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			Enumeration<Driver> dr = DriverManager.getDrivers();
			if (dr.hasMoreElements() == false)
				throw new NullPointerException("DRIVERS NOT FOUNDS");
			else {
				log.info("REGISTER DRIVER: " + dr.nextElement());
			}
		} catch (Exception e1) {
			log.fatal("", e1);
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

	public static boolean updating(String tableName, String collName,
			String paper_id, Connection conn) throws SQLException {
		boolean updating = false;
		Statement paperIdSearch = conn.createStatement();
		ResultSet updateResultSet = paperIdSearch.executeQuery("select `"
				+ tableName + "`.`" + collName + "` from `" + tableName
				+ "` where `" + tableName + "`.`paper_id` = \'" + paper_id
				+ "\'");

		while (updateResultSet.next()) {
			updating = updateResultSet.getString(1) == null ? true : updating;
			updating = updateResultSet.getString(1) != null
					&& "paper_id".equalsIgnoreCase(collName) ? true : updating;
		}
		paperIdSearch.close();
		return updating;
	}

	public static boolean update(String tableName, String collName,
			String paper_id, Object newValue, Map<String, Object> oldValue,
			Connection conn) throws SQLException {

		String ps = "update `" + tableName + "` set `" + collName
				+ "`= ? where  paper_id = ? ";

		if(!tableName.equals("paper"))
		for (String s : oldValue.keySet()) {
			if (!"".equals(oldValue.get(s)))
				ps = ps + " and `" + s + "` = \'" + oldValue.get(s) + "\'";
		}

		System.out.println("UPD : " + ps);

		PreparedStatement addFieldStatement = conn.prepareStatement(ps);
		addFieldStatement.setString(2, paper_id);

		int collType = getCollType(tableName, collName, conn);
		multiVallCollExecute(addFieldStatement, newValue, 1, collType);
		return addFieldStatement.execute();
	}

	public static boolean addField(String tableName, String collName,
			String paper_id, Object value, Connection conn) throws SQLException {

		PreparedStatement addFieldStatement = null;

		boolean updating = updating(tableName, collName, paper_id, conn);

		if (tableName.equals("paper"))
			updating = updating(tableName, "paper_id", paper_id, conn);

		int collType = getCollType(tableName, collName, conn);

		if (!updating) {
			addFieldStatement = insert(tableName, collName, paper_id, conn);
			multiVallCollExecute(addFieldStatement, value, 2, collType);

			System.out.println("INS:" + paper_id + " : " + collName);
		} else {
			addFieldStatement = update(tableName, collName, paper_id, conn);

			System.out.println("UPD:" + paper_id + " : " + collName + " : "
					+ value);

			multiVallCollExecute(addFieldStatement, value, 1, collType);
		}

		return addFieldStatement.execute();
	}

	private static int getCollType(String tableName, String collName,
			Connection conn) throws SQLException {
		Statement paperIdSearch = conn.createStatement();

		int collType = paperIdSearch
				.executeQuery(
						"select `" + tableName + "`.`" + collName + "` from  `"
								+ tableName + "`").getMetaData()
				.getColumnType(1);

		paperIdSearch.close();
		return collType;
	}

	private static PreparedStatement update(String tableName, String collName,
			String paper_id, Connection conn) throws SQLException {

		System.out.println("update `" + tableName + "` set `" + collName
				+ "`= ? where  paper_id = ? ");

		PreparedStatement addFieldStatement = conn.prepareStatement("update `"
				+ tableName + "` set `" + collName
				+ "`= ? where  paper_id = ? ");
		addFieldStatement.setString(2, paper_id);
		return addFieldStatement;
	}

	private static PreparedStatement insert(String tableName, String collName,
			String paper_id, Connection conn) throws SQLException {
		PreparedStatement addFieldStatement;
		addFieldStatement = conn.prepareStatement("insert into `" + tableName
				+ "` (paper_id, `" + collName + "` ) values (?, ? )");
		addFieldStatement.setString(1, paper_id);
		return addFieldStatement;
	}

	private static void multiVallCollExecute(PreparedStatement st,
			Object value, int parameter, int collType) throws SQLException {

		switch (collType) {
		case Types.VARCHAR: {
			st.setString(parameter, value.toString());
			break;
		}
		case Types.INTEGER: {
			st.setInt(parameter, Integer.parseInt(value.toString()));
			break;
		}
		case Types.DATE: {
			st.setString(parameter, value.toString().substring(0, 4));
			break;
		}
		default:
			throw new SQLException("Type not found");
		}
	}

}
