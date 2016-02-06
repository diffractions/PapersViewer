package dao;


import java.util.concurrent.CopyOnWriteArraySet;

import dao.exceptions.DaoSystemException;
import dao.exceptions.NoSuchEntityException;
import entity.Paper;

public interface PaperDao {

	public CopyOnWriteArraySet<Paper> selectAll() throws DaoSystemException ;
	public Paper selectById(int id) throws DaoSystemException, NoSuchEntityException ;
	
}
