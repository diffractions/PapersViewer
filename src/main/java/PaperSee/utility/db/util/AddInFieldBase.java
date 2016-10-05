package utility.db.util;

import java.sql.SQLException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public abstract class AddInFieldBase implements AddInField {

	public void addInPaper(Sheet sheet, int cellNumber) throws SQLException {

		for (Row row : sheet) {
			String paper_id = XLSControls.getPaperId(row.getCell(5));
			Cell collVall = row.getCell(cellNumber);

			String[] collValls = XLSControls.getStringArrVall(collVall, "<><>");

			if (paper_id == null) {
				break;
			} else if (collValls == null) {
				continue;
			}

			try {
				for (String s : collValls) {
					/* type. */addIn(paper_id, "", s);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void addInMaterial(Sheet sheet, int cellNumber) throws SQLException {

		for (Row row : sheet) {
			String paper_id = XLSControls.getPaperId(row.getCell(5));
			Cell collVall = row.getCell(cellNumber);

			String[] collValls = XLSControls.getStringArrVall(collVall, ",");

			if (paper_id == null) {
				break;
			} else if (collValls == null) {
				continue;
			}

			try {
				for (String s : collValls) {
					addIn(paper_id, s.trim(),
							XLSControls.getColorVall(collVall));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void addInFileType(Sheet sheet, int cellNumber) throws SQLException {

		for (Row row : sheet) {
			String paper_id = XLSControls.getPaperId(row.getCell(5));
			Cell collVall = row.getCell(cellNumber);


			if (paper_id == null) {
				break;
			} 
			
			try {
			 
					addIn(paper_id, collVall.getHyperlink().getAddress().substring(paper_id.length()+1),
							XLSControls.getColorVall(collVall));
				 
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
