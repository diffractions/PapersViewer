package utility.db.util;

import java.sql.SQLException;

public class AddInFieldPaper extends AddInFieldBase  implements AddInField {

	private String cellName;

	/**
	 * @param cellName
	 */
	public AddInFieldPaper(String cellName) {
		this.cellName = cellName;
	}

	@Override
	public void addIn(String paper_id, String matherialName, Object vall)
			throws SQLException {
		DBControls.addInPaper(paper_id, cellName, vall);

	}

}
