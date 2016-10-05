package utility.db.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.poi.ss.usermodel.Sheet;

public class Test {

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SQLException {

		Sheet sheet = XLSControls.getSheet();

		Connection conn = DriverManager
				.getConnection("jdbc:mysql://127.0.0.1/paper?user=root&password=si17st18");
		Statement getCollStatement = conn.createStatement();

		String tableName = "paper";
		ResultSet getCollCount = getCollStatement
				.executeQuery("Select * from paper." + tableName);
		int colls = getCollCount.getMetaData().getColumnCount();

		for (int i = 2; i <= colls; i++) {
			String cellName = getCollCount.getMetaData().getColumnName(i);
			new AddInFieldPaper(cellName).addInPaper(sheet, i - 2);

		}

		String tableName2 = "properties";
		ResultSet getCollCount2 = getCollStatement
				.executeQuery("Select * from paper." + tableName2);
		int colls2 = getCollCount2.getMetaData().getColumnCount();

		for (int i = 2; i <= colls2; i++) {
			String cellName = getCollCount2.getMetaData().getColumnName(i);
			new AddInFieldProperties(cellName).addInMaterial(sheet, 7 + i);

		}

		getCollStatement.close();
		conn.close();

		new AddInFieldMatherial().addInMaterial(sheet, 6);
		new AddInFieldMethod().addInMaterial(sheet, 8);
		new AddInFileType().addInFileType(sheet, 5);

	}

}
