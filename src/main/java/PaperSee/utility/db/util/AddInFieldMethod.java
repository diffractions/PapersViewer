package utility.db.util;

import java.sql.SQLException;

public class AddInFieldMethod extends AddInFieldBase  implements AddInField {

	@Override
	public void addIn(String paper_id, String matherialName, Object vall)
			throws SQLException {
		DBControls.addInMethod(paper_id, matherialName, vall);

	}

}
