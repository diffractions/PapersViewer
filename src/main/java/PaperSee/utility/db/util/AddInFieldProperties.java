package utility.db.util;

import java.sql.SQLException;

public class AddInFieldProperties extends AddInFieldBase implements AddInField {

	private String cellName;

	/**
	 * @param cellName
	 */
	public AddInFieldProperties(String cellName) {
		this.cellName = cellName;
	}

	@Override
	public void addIn(String paper_id, String matherialName, Object vall)
			throws SQLException {
		DBControls.addInProperties(paper_id, cellName, vall);

	}

}
