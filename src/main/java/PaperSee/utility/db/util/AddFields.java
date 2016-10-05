package utility.db.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class AddFields {
	static void addProperties(Sheet sheet1, Connection conn, String tableName,
			String cellName, boolean isString, int cellNumber,boolean isMultiVall,
			Map<Exception, String> exc) throws SQLException {

		PreparedStatement propertiesStatement = null;

		for (Row row : sheet1) {
			String columns = new String();

			try {
				Cell paper_id = row.getCell(5);
				if (paper_id == null) {
					break;
				}
				Cell matherial = row.getCell(cellNumber);
				columns = paper_id + "\n" + matherial;

				String paper_ids = addPaperId(paper_id);

				Statement paperIdSearch = conn.createStatement();
				boolean isPaperFound = paperIdSearch.executeQuery(
						"select " + tableName + ".paper_id from  " + tableName
								+ " where " + tableName + ".paper_id = \'"
								+ paper_ids + "\'").next();

				paperIdSearch.close();

				if (!isPaperFound) {
					propertiesStatement = conn.prepareStatement("insert into "
							+ tableName + " (paper_id, `" + cellName
							+ "` ) values (?, ? )");
					propertiesStatement.setString(1, paper_ids);

					multiVallCollExecute(propertiesStatement, matherial, 2,
							isString,isMultiVall);

				} else {
					propertiesStatement = conn.prepareStatement("update "
							+ tableName + " set `" + cellName
							+ "`= ? where  paper_id = ? ");

					propertiesStatement.setString(2, paper_ids);

					multiVallCollExecute(propertiesStatement, matherial, 1,
							isString,isMultiVall);

				}

				propertiesStatement.close();

			} catch (SQLException e) {
				exc.put(e, columns);
			}
		}
	}

	static String addPaperId(Cell paper_id) throws SQLException {
		System.out.println(paper_id);
		return paper_id.getHyperlink().getAddress().replace(".pdf", "")
				.replace(".doc", "").replace(".PDF", "");

	}

	static String getStringVal(Cell val, int minLrngth) throws SQLException {
		if (val != null) {
			if (val.toString().length() >= minLrngth) {
				if (val.getCellType() == Cell.CELL_TYPE_STRING)
					return val.getStringCellValue();
				else if (val.getCellType() == Cell.CELL_TYPE_NUMERIC)
					return "" + val.getNumericCellValue();
			}
		}
		return null;
	}

	private static boolean multiVallCollExecute(PreparedStatement st, Cell val,
			int parameter, boolean isString, boolean isMultiVall)
			throws SQLException {
		int minLrngth = 0;
		String str = getStringVal(val, minLrngth);
		int color = getColorVall(val);

		if (str != null && isString) {

			if (isMultiVall)
				for (String s : str.split(",")) {

					if (s.length() > minLrngth && !" ".equals(s)) {
						st.setString(parameter, s.trim());
						st.execute();
					}
				}
			else if (str.length() > minLrngth && !" ".equals(str)) {
				st.setString(parameter, str.trim());
				st.execute();
			}

		} else if (str != null && !" ".equals(str)) {

			st.setInt(parameter, color);
			return st.execute();
		}
		return false;
	}

	private static int getColorVall(Cell cellVall) throws SQLException {

		if (cellVall != null) {
			short color = cellVall.getCellStyle().getFillForegroundColor();

			switch (color) {

			case 64:
				return 1;
			case 10:
				return 3;
			case 17:
				return 5;
			case 56:
				return 7;
			case 12:
				return 7;
			case 57:
				return 8;
			case 30:
				return 8;
			case 51:
				return 9;
			case 52:
				return 9;
			case 53:
				return 9;
			case 36:
				return 10;
			}
		}

		return 0;
	}
}
