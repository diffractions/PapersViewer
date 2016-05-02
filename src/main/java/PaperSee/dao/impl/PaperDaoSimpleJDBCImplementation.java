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

import com.sun.javafx.binding.StringFormatter;

import dao.PaperDao;
import dao.exceptions.DaoRuntimeException;
import dao.exceptions.DaoSystemException;
import dao.exceptions.NoSuchEntityException;
import entity.Paper;
import entity.SimplePaper;
import static utility.JBDCUtil.*;
import static utility.LogPrinter.*;

public class PaperDaoSimpleJDBCImplementation implements PaperDao {

	private String driverName;
	private final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/paper_test?user=root&password=si17st18";
	private final String SELECT_ALL_SQL = "select id, title from papers;";
	private final String SELECT_BY_ID_SQL = "select id, title from papers where id= %d ";

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverName() {
		return driverName;
	}

	public void deregistrDriver() {
		try {
			Enumeration<Driver> dr = DriverManager.getDrivers();
			if (dr.hasMoreElements() == true)
				while (dr.hasMoreElements()) {
					Driver d = dr.nextElement();
					DriverManager.deregisterDriver(d);
					println(" drivers - " + d);
				}
		} catch (Exception e1) {
			throw new DaoRuntimeException(e1);
		}
	}

	public void registrDriver() {
		try {
			Class.forName(driverName);
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
	public CopyOnWriteArraySet<Paper> selectAll() throws DaoSystemException {
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		CopyOnWriteArraySet<Paper> papers = new CopyOnWriteArraySet<Paper>();
		try {
			conn = DriverManager.getConnection(JDBC_URL, new Properties());
			conn.setAutoCommit(false);
			stat = conn.createStatement();
			rs = stat.executeQuery(SELECT_ALL_SQL);
			while (rs.next()) {
				papers.add(new SimplePaper(rs.getInt("id"), rs
						.getString("title")));
			}
			conn.commit();
		} catch (SQLException e) {
			try {
				rollBackQuaetly(conn);
			} catch (SQLException e1) {
				e.addSuppressed(e1);
			}
			throw new DaoSystemException(e);
		} finally {
			try {
				closeQuaetly(rs, stat, conn);
			} catch (Exception e1) {
				throw new DaoSystemException(e1);
			}
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
			rs = stat.executeQuery(StringFormatter.format(SELECT_BY_ID_SQL, id)
					.get());
			if (rs.next()) {
				conn.commit();
				return new SimplePaper(rs.getInt("id"), rs.getString("title"));
			} else {
				rollBackQuaetly(conn);
				throw new NoSuchEntityException("Wrong ID number: \'" + id
						+ "\' is incorrect!");
			}

		} catch (SQLException e) {
			try {
				rollBackQuaetly(conn);
			} catch (SQLException e1) {
				e.addSuppressed(e1);
			}
			throw new DaoSystemException(e);
		} finally {
			try {
				closeQuaetly(rs, stat, conn);
			} catch (Exception e1) {
				throw new DaoSystemException(e1);
			}
		}
	}
}
