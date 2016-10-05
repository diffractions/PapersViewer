package dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import dao.exceptions.DaoSystemException;
import dao.exceptions.NoSuchEntityException;
import entity.Paper;

public interface PaperDao {

	public CopyOnWriteArraySet<Paper> selectAll(String [] collNames, String [] sortKeys) throws DaoSystemException;

	public Paper selectById(String id) throws DaoSystemException,
			NoSuchEntityException;

	public CopyOnWriteArraySet<Paper> search(String str)
			throws DaoSystemException;

	List<Map<String, List<String>>> selectValues(String paperName, String id)
			throws DaoSystemException, NoSuchEntityException;
}
