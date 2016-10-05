package utility.db.util;

import java.sql.SQLException;

import org.apache.poi.ss.usermodel.Sheet;

public interface AddInField {
	public void addIn(String paper_id, String matherialName, Object vall)
			throws SQLException;

	public void addInPaper(Sheet sheet, int cellNumber) throws SQLException;

	public void addInMaterial(Sheet sheet, int cellNumber) throws SQLException;
}
