package dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import dao.PaperInsertDao;
import dao.exceptions.DaoSystemException;
import static utility.JBDCUtil.*;

public class PaperInsertDaoJDBCTransactionImplementation implements
		PaperInsertDao {

	public static Logger log = Logger.getLogger("LOG");

	private DataSource dataSource;

	private final String UPDATE_ALL_PAPER_ITEMS = "update paper set title = ?, authors = ?, journal = ?,  year=?, type = ? where paper_id = ?";
	private final String INSERT_ALL_PAPER_ITEMS = "insert into paper values (?,?,?,?,?,?)";
	private final String PAPER_TABLE_NAME = "paper";
//	private final String PROPORTIES_TABLE_NAME = "properties";
	private final String PAPER_PRIMARY_KEY = "paper_Id";

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public int addInPaper(String paper_id, String title, String authors,
			String journal, String year, String type) throws DaoSystemException {

		try {
			Connection conn = dataSource.getConnection();

			PreparedStatement statement;
			if (updating(PAPER_TABLE_NAME, PAPER_PRIMARY_KEY, paper_id, conn)) {
				statement = conn.prepareStatement(UPDATE_ALL_PAPER_ITEMS);
				statement.setString(6, paper_id);
				statement.setString(1, title);
				statement.setString(2, authors);
				statement.setString(3, journal);
				statement.setString(4, year);
				statement.setString(5, type);
			} else {
				statement = conn.prepareStatement(INSERT_ALL_PAPER_ITEMS);
				statement.setString(1, paper_id);
				statement.setString(2, title);
				statement.setString(3, authors);
				statement.setString(4, journal);
				statement.setString(5, year);
				statement.setString(6, type);
			}

			return statement.executeUpdate();
		} catch (SQLException e) {
			throw new DaoSystemException(e);
		}

	}

	// @Override
	// public boolean addInPaper(String paper_id, String collName, Object vall)
	// throws DaoSystemException {
	//
	// try {
	// return addField(PAPER_TABLE_NAME, collName, paper_id, vall,
	// dataSource.getConnection());
	// } catch (SQLException e) {
	// throw new DaoSystemException(e);
	// }
	// }
	//
	// @Override
	// public boolean addInProperties(String paper_id, String collName, Object
	// vall)
	// throws DaoSystemException {
	//
	// try {
	// return addField(PROPORTIES_TABLE_NAME, collName, paper_id, vall,
	// dataSource.getConnection());
	// } catch (SQLException e) {
	// throw new DaoSystemException(e);
	// }
	// }
	//
	// @Override
	// public boolean addInMatherial(String paper_id, String matherialName,
	// Object vall) throws DaoSystemException {
	// try {
	// Connection conn = dataSource.getConnection();
	//
	// String tableName = "matherial";
	// String collName = "matherial";
	// String collValName = "val";
	// return addField(tableName, collName, paper_id, matherialName, conn)
	// && addField(tableName, collValName, paper_id, vall, conn);
	// } catch (SQLException e) {
	// throw new DaoSystemException(e);
	// }
	// }
	//
	// @Override
	// public boolean addInMethod(String paper_id, String methodName, Object
	// vall)
	// throws DaoSystemException {
	// try {
	// Connection conn = dataSource.getConnection();
	//
	// String tableName = "method";
	// String collName = "method";
	// String collValName = "val";
	// return addField(tableName, collName, paper_id, methodName, conn)
	// && addField(tableName, collValName, paper_id, vall, conn);
	// } catch (SQLException e) {
	// throw new DaoSystemException(e);
	// }
	// }

	@Override
	public boolean updateRandom(String tableName, String collName,
			String paper_id, Object newValue, Map<String, Object> oldValue) throws DaoSystemException {
		try {
			Connection conn = dataSource.getConnection();

			return update(tableName, collName, paper_id, newValue, oldValue, conn);
		} catch (SQLException e) {
			throw new DaoSystemException(e);
		}
	}

}
