package dao;
 

import java.util.Map;

import dao.exceptions.DaoSystemException;

public interface PaperInsertDao {
//	public boolean addInPaper(String paper_id, String collName, Object vall)
//			throws DaoSystemException;

	public int addInPaper(String paper_id, String title, String authors,
			String journal, String year, String type) throws DaoSystemException;

//	public boolean addInProperties(String paper_id, String collName, Object vall)
//			throws DaoSystemException;
//
//	public boolean addInMatherial(String paper_id, String matherialName,
//			Object vall) throws DaoSystemException;
//
//	public boolean addInMethod(String paper_id, String methodName, Object vall)
//			throws DaoSystemException;
	
//	public boolean updateRandom(String tableName, String collName,
//			String paper_id, Object newValue, Object oldValue ) throws DaoSystemException ;

	boolean updateRandom(String tableName, String collName, String paper_id,
			Object newValue, Map<String, Object> oldValue)
			throws DaoSystemException;

}
