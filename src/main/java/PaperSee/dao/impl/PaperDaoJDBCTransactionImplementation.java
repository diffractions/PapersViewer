package dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import dao.PaperDao;
import dao.exceptions.DaoSystemException;
import dao.exceptions.NoSuchEntityException;
import entity.Paper;
import entity.SimplePaper;
import static utility.JBDCUtil.*;

public class PaperDaoJDBCTransactionImplementation implements PaperDao {

	public static Logger log = Logger.getLogger("LOG");

	private final static String PAPER_TABL_NAME = "paper";
	private final static String ID_COL_NAME = "paper_id";
	private final static String TITLE_COL_NAME = "title";
	private final static String METHOD_COL_NAME = "method";
	private final static String MATHERIAL_COL_NAME = "matherial";
	private final static String PROPORTIES_COL_NAME = "properties";

	private final static String GEN_SEL = "@GEN";
	private final static String PROP_SEL = "@PROP";
	private final static String SORT_SEL = "@SORT";

	private final static String SELECT_ALL_SQL =

	"SELECT " + GEN_SEL + " FROM " + PAPER_TABL_NAME + " LEFT JOIN (SELECT "
			+ ID_COL_NAME + ", GROUP_CONCAT(" + MATHERIAL_COL_NAME + ") AS "
			+ MATHERIAL_COL_NAME + " FROM " + MATHERIAL_COL_NAME + " GROUP BY "
			+ ID_COL_NAME + ") AS " + MATHERIAL_COL_NAME + " USING  ("
			+ ID_COL_NAME + ")" + " LEFT JOIN (SELECT " + ID_COL_NAME
			+ ", GROUP_CONCAT(" + METHOD_COL_NAME + ") AS " + METHOD_COL_NAME
			+ " FROM " + METHOD_COL_NAME + " GROUP BY " + ID_COL_NAME + ") AS "
			+ METHOD_COL_NAME + " USING  (" + ID_COL_NAME
			+ ") LEFT JOIN (SELECT " + ID_COL_NAME + ", " + PROP_SEL + " FROM "
			+ PROPORTIES_COL_NAME + " GROUP BY " + ID_COL_NAME + ") AS "
			+ PROPORTIES_COL_NAME + " USING  (" + ID_COL_NAME + ") " + SORT_SEL
			+ " ;";

	private final String SELECT_PAPER_VALUES_SQL = "SELECT * FROM $"
			+ PAPER_TABL_NAME + " WHERE " + ID_COL_NAME + " = ? ;";

	private final String SELECT_BY_ID_SQL = "SELECT " + ID_COL_NAME + ", "
			+ TITLE_COL_NAME + " FROM " + PAPER_TABL_NAME + " WHERE "
			+ ID_COL_NAME + "= ? ;";


	private final static String SEARCH_COL_NAME = "search_word_sentence";
	private final static String SEARCH_SQL = " CALL search(?);";

	// private final static String PAPER_TEXT_TABL_NAME = "paper_text";
	// private final static String TEXT_FIELD_COL_NAME = "text";
	//
	// private final String SEARCH_SQL = "SELECT " + PAPER_TABL_NAME + "."
	// + ID_COL_NAME + ", " + PAPER_TABL_NAME + "." + TITLE_COL_NAME
	// + " FROM " + PAPER_TEXT_TABL_NAME + ", " + PAPER_TABL_NAME
	// + " WHERE " + PAPER_TABL_NAME + "." + ID_COL_NAME + "="
	// + PAPER_TEXT_TABL_NAME + "." + ID_COL_NAME + " AND "
	// + TEXT_FIELD_COL_NAME + " LIKE ? ;";
	//
	// private final static String SEARCH_SQL = "SELECT " + PAPER_TEXT_TABL_NAME
	// + "."
	// + ID_COL_NAME + ", SUBSTRING_INDEX(SUBSTRING("
	// + PAPER_TEXT_TABL_NAME + "." + TEXT_FIELD_COL_NAME
	// + " , 1, LOCATE(\".\"," + PAPER_TEXT_TABL_NAME + "."
	// + TEXT_FIELD_COL_NAME + ", LOCATE(?, "
	// + PAPER_TEXT_TABL_NAME + "." + TEXT_FIELD_COL_NAME
	// + "))), '.', -2) AS " + TITLE_COL_NAME + " FROM "
	// + PAPER_TEXT_TABL_NAME + " WHERE LOCATE(?, "
	// + PAPER_TEXT_TABL_NAME + "." + TEXT_FIELD_COL_NAME + ") ;";

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public CopyOnWriteArraySet<Paper> selectAll(String[] collNames,
			String[] sortKeys) throws DaoSystemException {

		Statement selectAllStat = null;
		ResultSet selectAllResultSet = null;
		CopyOnWriteArraySet<Paper> papers = new CopyOnWriteArraySet<Paper>();

		try {
			Connection conn = dataSource.getConnection();

			String sort = getSortedCode(sortKeys);
			String prop = getProportiesColumns();
			String coll = getUSerSelectedColumns(collNames, prop);
			String head = ID_COL_NAME + " AS " + ID_COL_NAME + "s, "
					+ TITLE_COL_NAME + " AS " + TITLE_COL_NAME + "s, ";

			String workingSQL = SELECT_ALL_SQL.replace(GEN_SEL, head + coll)
					.replace(PROP_SEL, prop).replace(SORT_SEL, sort);
			log.debug("Generated working SELECT ALL SQL\n" + workingSQL);

			selectAllStat = conn.createStatement();
			selectAllStat.execute(workingSQL);
			selectAllResultSet = selectAllStat.getResultSet();
			log.debug("SELECT ALL SQL statement was execute!");

			while (selectAllResultSet.next()) {
				SimplePaper proportiesTable = new SimplePaper(
						selectAllResultSet.getString(ID_COL_NAME + "s"),
						selectAllResultSet.getString(TITLE_COL_NAME + "s"));
				proportiesTable.setProporties(createProportiesTableRow(
						selectAllResultSet, coll));
				papers.add(proportiesTable);
			}

			log.debug("Paper proporties table\n" + papers);

		} catch (SQLException e) {
			log.error(e);
			throw new DaoSystemException(e);
		} finally {
			log.trace("Close SELECT_ALL_SQL ResultSet and SELECT_ALL_SQL Statement");
			try {
				closeQuaetly(selectAllResultSet, selectAllStat);
			} catch (Exception e1) {
				log.error(e1);
				throw new DaoSystemException(e1);
			}
		}

		return papers;
	}

	private String getUSerSelectedColumns(String[] collNames, String prop) {

		String coll = null;
		if (collNames != null) {
			coll = " ";
			int selectedColumnCount = collNames.length;
			for (int i = 0; i < selectedColumnCount; i++)
				coll = coll + " " + collNames[i]
						+ ((i + 1 < selectedColumnCount) ? ", " : " ");

			log.trace("User selected column\n" + coll);

		} else {
			coll = ID_COL_NAME + ", " + TITLE_COL_NAME + ", " + METHOD_COL_NAME
					+ ", " + MATHERIAL_COL_NAME + ", " + prop;

			log.trace("User selected column was empty and column autogenerating was performed:\n"
					+ coll);
		}
		return coll;
	}

	private Map<String, String> createProportiesTableRow(ResultSet set,
			String userSelectedCollNames) throws SQLException {

		Map<String, String> proporties = new LinkedHashMap<>();

		for (String s : userSelectedCollNames.split(",")) {
			String collName = s.trim();
			String collVall = set.getString(collName);

			boolean notDuplicte = !((ID_COL_NAME).equalsIgnoreCase(collName));

			log.trace("Add value to proporties table; Duplictes :"
					+ notDuplicte + "; key: " + collName + "- val.: "
					+ collVall);

			if (notDuplicte) {
				proporties.put(collName, collVall);
			}
		}

		log.trace("Print proporties table :\n" + proporties);
		return proporties;
	}

	private String getSortedCode(String[] sortKeys) {
		String sorted = "";
		if (sortKeys != null) {
			sorted = " order by ";
			int selectedColumnCount = sortKeys.length;
			for (int i = 0; i < selectedColumnCount; i++)
				sorted = sorted + " `" + sortKeys[i] + "` DESC "
						+ ((i + 1 < selectedColumnCount) ? ", " : " ");

		}

		log.trace("User selected column for sort\n" + sorted);
		return sorted;
	}

	private String getProportiesColumns() throws SQLException {

		try (Statement statppp = dataSource.getConnection().createStatement();) {
			ResultSet t = statppp
					.executeQuery("select * from properties limit 1;");
			String coll = "";
			for (int i = 1; i <= t.getMetaData().getColumnCount(); i++) {
				String name = t.getMetaData().getColumnName(i);
				if (!ID_COL_NAME.equalsIgnoreCase(name)) {
					coll = coll
					// + "`"
							+ name
							// + "`"
							+ ((i < t.getMetaData().getColumnCount()) ? ", "
									: " ");
				}
			}
			return coll;
		}
	}

	@Override
	public Paper selectById(String id) throws DaoSystemException,
			NoSuchEntityException {
		PreparedStatement stat = null;
		ResultSet rs = null;

		try {
			Connection conn = dataSource.getConnection();
			stat = conn.prepareStatement(SELECT_BY_ID_SQL);
			stat.setString(1, id);
			rs = stat.executeQuery();
			if (rs.next()) {
				return new SimplePaper(rs.getString(ID_COL_NAME),
						rs.getString(TITLE_COL_NAME));
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

	@Override
	public CopyOnWriteArraySet<Paper> search(String str)
			throws DaoSystemException {
		log.info("Find in papers :\'" + str + "\'");
		
		CallableStatement stat = null;
		ResultSet rs = null;
		CopyOnWriteArraySet<Paper> ret = new CopyOnWriteArraySet<>();

		try {
			Connection conn = dataSource.getConnection();
			stat = conn.prepareCall(SEARCH_SQL);
			stat.setString(1, str);
			rs = stat.executeQuery();

//			while (rs.next()) {
//				ret.add(new SimplePaper(rs.getString(ID_COL_NAME), rs
//						.getString(SEARCH_COL_NAME)));
//			}
			
			while (rs.next()) {
				SimplePaper papers = new SimplePaper(rs.getString(ID_COL_NAME),null);
				papers.setProporties(createProportiesTableRow(
						rs, SEARCH_COL_NAME)); 

				ret.add(papers);
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

		return ret;
	}

	@Override
	public List<Map<String, List<String>>> selectValues(String tableName,
			String id) throws DaoSystemException, NoSuchEntityException {
		PreparedStatement stat = null;
		ResultSet rs = null;

		try {
			Connection conn = dataSource.getConnection();

			stat = conn.prepareStatement(SELECT_PAPER_VALUES_SQL.replace("$"
					+ PAPER_TABL_NAME, tableName));

			stat.setString(1, id);
			rs = stat.executeQuery();

			List<Map<String, List<String>>> rets = new LinkedList<>();
			boolean consist_id = false;
			while (rs.next()) {

				Map<String, List<String>> ret = new HashMap<>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					put(rs.getMetaData().getColumnName(i), rs.getString(i), ret);
				}

				consist_id = true;
				rets.add(ret);

			}

			if (!consist_id)
				throw new NoSuchEntityException("Wrong ID number: \'" + id
						+ "\' is incorrect!");

			consist_id = false;
			return rets;

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

	private List<String> put(String key, String value,
			Map<String, List<String>> ret) {
		List<String> items = null;
		if (ret.get(key) == null) {
			items = new LinkedList<>();
		} else
			items = ret.get(key);

		items.add(value);
		return ret.put(key, items);
	}

}



//DELIMITER // 
// 
//CREATE PROCEDURE `search` (IN var1 VARCHAR(100))  
//LANGUAGE SQL  
//DETERMINISTIC  
//SQL SECURITY DEFINER   
//BEGIN  
//   SELECT paper_text.paper_id, SUBSTRING_INDEX(SUBSTRING(paper_text.text , 1, LOCATE('. ',paper_text.text, LOCATE(var1, paper_text.text))), '. ', -1) AS search_word_sentence FROM paper_text WHERE LOCATE(var1, paper_text.text) ;
//
//END//
//
//DELIMITER ;
//CALL search('45.');
//
//DROP PROCEDURE IF EXISTS search;
 