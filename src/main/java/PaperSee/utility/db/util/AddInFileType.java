package utility.db.util;

import java.sql.SQLException;

public class AddInFileType extends AddInFieldBase implements AddInField {

	@Override
	public void addIn(String paper_id, String matherialName, Object vall)
			throws SQLException {
		DBControls.addInFileType(paper_id, matherialName, vall);

	}

}
