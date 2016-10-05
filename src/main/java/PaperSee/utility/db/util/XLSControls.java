package utility.db.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

public class XLSControls {

	static File doc = new File(System.getProperty("user.home")
			+ "\\desktop\\! content.xls");
	// static String sheetName = "1";
	static String sheetName = "Papers";

	static Sheet getSheet() throws FileNotFoundException, IOException {
		return getFileWorkbook(doc).getSheet(sheetName);
	}

	static String[] getStringArrVall(Cell val, String split) {
		if (val != null) {
			if (val.getCellType() == Cell.CELL_TYPE_STRING) {
				if (val.getStringCellValue().equals(" "))
					return null;
				return val.getStringCellValue().split(split);
			} else if (val.getCellType() == Cell.CELL_TYPE_NUMERIC)
				return ("" + val.getNumericCellValue()).split(",");
		}
		return null;
	}

	private static HSSFWorkbook getFileWorkbook(File doc) throws IOException,
			FileNotFoundException {
		return new HSSFWorkbook(new FileInputStream(doc));
	}

	static String getPaperId(Cell paper_id) throws SQLException {
		System.out.println(":" + paper_id);
		if (paper_id != null)
			return paper_id.getHyperlink().getAddress().replace(".pdf", "")
					.replace(".doc", "").replace(".PDF", "");
		return null;

	}

	static int getColorVall(Cell cellVall) throws SQLException {

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
			case 13:
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
			default:
				return 1;
			}
		}

		return 0;
	}

}
