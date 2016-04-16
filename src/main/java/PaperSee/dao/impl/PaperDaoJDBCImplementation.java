package dao.impl;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArraySet;

import dao.PaperDao;
import dao.exceptions.DaoRuntimeException;
import dao.exceptions.DaoSystemException;
import dao.exceptions.NoSuchEntityException;
import entity.Paper;
import entity.SimplePaper;

public class PaperDaoJDBCImplementation implements PaperDao {

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Enumeration<Driver> dr = DriverManager.getDrivers();
			if (dr.hasMoreElements() == false)
				throw new NullPointerException("DRIVERS NOT FOUNDS");
			while (dr.hasMoreElements()) {
				System.out
						.println("DAO.IMPL.Print drivers:" + dr.nextElement());
			}
		} catch (Exception e1) {
			throw new DaoRuntimeException(e1);
		}

	}

	private final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/paper_test?user=root&password=si17st18";

	@Override
	public CopyOnWriteArraySet<Paper> selectAll() throws DaoSystemException {
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		CopyOnWriteArraySet<Paper> papers = new CopyOnWriteArraySet<Paper>();

		try {
			conn = DriverManager.getConnection(JDBC_URL, new Properties());
			conn.setAutoCommit(false);
			stat = conn.createStatement();
			rs = stat.executeQuery("select id, title from papers;");
			while (rs.next()) {
				papers.add(new SimplePaper(rs.getInt("id"), rs
						.getString("title")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return papers;

	}

	@Override
	public Paper selectById(int id) throws DaoSystemException,
			NoSuchEntityException {
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;

		try {
			conn = DriverManager.getConnection(JDBC_URL, new Properties());
			conn.setAutoCommit(false);
			stat = conn.createStatement();
			rs = stat.executeQuery("select id, title from papers where id="
					+ id + ";");
			if (rs.next()) {
				return new SimplePaper(rs.getInt("id"), rs.getString("title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
