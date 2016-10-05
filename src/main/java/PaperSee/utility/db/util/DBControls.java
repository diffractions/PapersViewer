package utility.db.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class DBControls {

	public static void addInPaper(String paper_id, String collName, Object vall)
			throws SQLException {
		String tableName = "paper";
		addField(tableName, collName, paper_id, vall);
	}

	public static void addInPaper(String paper_id, String title,
			String authors, String journal, String year, String type)
			throws SQLException {
		Connection conn = getConnection();
		boolean updaitng = updating("paper", "paper_Id", paper_id);
		System.out.println(updaitng);

		@SuppressWarnings("resource")
		PreparedStatement statement = updaitng ? conn
				.prepareStatement("update paper set title = ?, authors = ?, journal = ?,  year=?, type = ? where paper_id = ?")
				: conn.prepareStatement("insert into paper values (?,?,?,?,?,?)");
		if (!updaitng) {
			statement.setString(1, paper_id);
			statement.setString(2, title);
			statement.setString(3, authors);
			statement.setString(4, journal);
			statement.setString(5, year);
			statement.setString(6, type);
		} else {
			statement.setString(6, paper_id);
			statement.setString(1, title);
			statement.setString(2, authors);
			statement.setString(3, journal);
			statement.setString(4, year);
			statement.setString(5, type);
		}
		System.out.println(statement.execute());

	}

	public static void addInProperties(String paper_id, String collName,
			Object vall) throws SQLException {
		String tableName = "properties";
		addField(tableName, collName, paper_id, vall);
	}

	public static void addInMatherial(String paper_id, String matherialName,
			Object vall) throws SQLException {
		String tableName = "matherial";
		String collName = "matherial";
		String collValName = "val";
		addField(tableName, collName, paper_id, matherialName);
		addField(tableName, collValName, paper_id, vall);
	}

	public static void addInMethod(String paper_id, String methodName,
			Object vall) throws SQLException {
		String tableName = "method";
		String collName = "method";
		String collValName = "val";
		addField(tableName, collName, paper_id, methodName);
		addField(tableName, collValName, paper_id, vall);
	}
	

	public static void addInFileType(String paper_id, String type,
			Object vall) throws SQLException {
		String tableName = "file_type";
		String collName = "file_type"; 
		addField(tableName, collName, paper_id, type); 
	}

	private static boolean addField(String tableName, String collName,
			String paper_id, Object value) throws SQLException {

		Connection conn = getConnection();

		PreparedStatement addFieldStatement = null;

		boolean updating = updating(tableName, collName, paper_id);

		Statement paperIdSearch = conn.createStatement();
		int collType = paperIdSearch
				.executeQuery(
						"select `" + tableName + "`.`" + collName + "` from  `"
								+ tableName + "`").getMetaData()
				.getColumnType(1);

		paperIdSearch.close();

		if (!updating) {
			addFieldStatement = insert(tableName, collName, paper_id, conn);
			multiVallCollExecute(addFieldStatement, value, 2, collType);
		} else {
			addFieldStatement = update(tableName, collName, paper_id, conn);
			multiVallCollExecute(addFieldStatement, value, 1, collType);
		}

		boolean done = addFieldStatement.execute();
		conn.close();
		return done;
	}

	private static Connection getConnection() throws SQLException {
		Connection conn = DriverManager
				.getConnection("jdbc:mysql://127.0.0.1/paper?user=root&password=si17st18");
		return conn;
	}

	private static boolean updating(String tableName, String collName,
			String paper_id) throws SQLException {
		Connection conn = getConnection();
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

	private static PreparedStatement update(String tableName, String collName,
			String paper_id, Connection conn) throws SQLException {
		PreparedStatement addFieldStatement;
		addFieldStatement = conn.prepareStatement("update `" + tableName
				+ "` set `" + collName + "`= ? where  paper_id = ? ");
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
			st.setInt(parameter, (int) value);
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
