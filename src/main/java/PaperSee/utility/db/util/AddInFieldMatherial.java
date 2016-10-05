package utility.db.util;

import java.sql.SQLException;

public class AddInFieldMatherial extends AddInFieldBase  implements AddInField {

	@Override
	public void addIn(String paper_id, String matherialName, Object vall)
			throws SQLException {
		DBControls.addInMatherial(paper_id, matherialName, vall);

	}
 

}
