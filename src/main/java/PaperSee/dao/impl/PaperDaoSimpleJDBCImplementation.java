package dao.impl;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import com.sun.javafx.binding.StringFormatter;

import dao.PaperDao;
import dao.exceptions.DaoRuntimeException;
import dao.exceptions.DaoSystemException;
import dao.exceptions.NoSuchEntityException;
import entity.Paper;
import entity.SimplePaper;

import org.apache.log4j.Logger;

import static utility.JBDCUtil.getConnection;
import static utility.JBDCUtil.closeQuaetly;
import static utility.JBDCUtil.rollBackQuaetly;

public class PaperDaoSimpleJDBCImplementation implements PaperDao {

	public static Logger log = Logger.getLogger("LOG");

	private String driverName;
	private final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/paper?user=root&password=si17st18";
	private final String SELECT_ALL_SQL = "select paper_id, title from paper;";
	private final String SELECT_BY_ID_SQL = "select paper_id, title from paper where paper_id= %d ";

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
					log.info(" drivers - " + d);
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
				log.info("drivers - " + dr.nextElement());
			}
		} catch (Exception e1) {
			throw new DaoRuntimeException(e1);
		}
	}

	@Override
	public CopyOnWriteArraySet<Paper> selectAll(String [] collNames, String [] sortKeys) throws DaoSystemException {
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		CopyOnWriteArraySet<Paper> papers = new CopyOnWriteArraySet<Paper>();
		try {
			conn = getConnection(JDBC_URL);
			conn.setAutoCommit(false);
			stat = conn.createStatement();
			rs = stat.executeQuery(SELECT_ALL_SQL);
			while (rs.next()) {
				papers.add(new SimplePaper(rs.getString("paper_id"), rs
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
	public Paper selectById(String id) throws DaoSystemException,
			NoSuchEntityException {
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;

		try {
			conn = getConnection(JDBC_URL);
			conn.setAutoCommit(false);
			stat = conn.createStatement();
			rs = stat.executeQuery(StringFormatter.format(SELECT_BY_ID_SQL, id)
					.get());
			if (rs.next()) {
				conn.commit();
				return new SimplePaper(rs.getString("paper_id"),
						rs.getString("title"));
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

	@Override
	public CopyOnWriteArraySet<Paper> search(String str)
			throws DaoSystemException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, List<String>>> selectValues(String paperName, String id)
			throws DaoSystemException, NoSuchEntityException {
		// TODO Auto-generated method stub
		return null;
	}
}
