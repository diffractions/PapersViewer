package dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.sql.DataSource;

import dao.PaperDao;
import dao.exceptions.DaoSystemException;
import dao.exceptions.NoSuchEntityException;
import entity.Paper;
import entity.SimplePaper;
import static utility.JBDCUtil.*;

public class PaperDaoJDBCTransactionImplementation implements PaperDao {

	private final String SELECT_ALL_SQL = "select id, title from papers;";
	private final String SELECT_BY_ID_SQL = "select id, title from papers where id= ? ";
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public CopyOnWriteArraySet<Paper> selectAll() throws DaoSystemException {

		PreparedStatement stat = null;
		ResultSet rs = null;
		CopyOnWriteArraySet<Paper> papers = new CopyOnWriteArraySet<Paper>();

		try {
			Connection conn = dataSource.getConnection();
			stat = conn.prepareStatement(SELECT_ALL_SQL);
			rs = stat.executeQuery();
			while (rs.next()) {
				papers.add(new SimplePaper(rs.getInt("id"), rs
						.getString("title")));
			}
			conn.commit();
		} catch (SQLException e) {
			throw new DaoSystemException(e);
		} finally {
			try {
				closeQuaetly(rs, stat);
			} catch (Exception e1) {
				throw new DaoSystemException(e1);
			}
		}

		return papers;
	}

	@Override
	public Paper selectById(int id) throws DaoSystemException,
			NoSuchEntityException {
		PreparedStatement stat = null;
		ResultSet rs = null;

		try {
			Connection conn = dataSource.getConnection();
			stat = conn.prepareStatement(SELECT_BY_ID_SQL);
			stat.setInt(1, id);
			rs = stat.executeQuery();
			if (rs.next()) {
				return new SimplePaper(rs.getInt("id"), rs.getString("title"));
			} else {
				throw new NoSuchEntityException("Wrong ID number: \'" + id
						+ "\' is incorrect!");
			}

		} catch (SQLException e) {
			throw new DaoSystemException(e);
		} finally {
			try {
				closeQuaetly(rs, stat);
			} catch (Exception e1) {
				throw new DaoSystemException(e1);
			}
		}

	}

}
