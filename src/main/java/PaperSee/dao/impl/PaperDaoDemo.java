package dao.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import dao.PaperDao;
import dao.exceptions.DaoSystemException;
import dao.exceptions.NoSuchEntityException;
import entity.Paper;

public class PaperDaoDemo implements PaperDao {
	private ConcurrentHashMap<Integer, Paper> papers;

	public PaperDaoDemo() {
//		papers = new ConcurrentHashMap<>();
//		papers.put(1, new Paper(1,"1"));
//		papers.put(2, new Paper(2,"2"));
//		papers.put(3, new Paper(3,"3")); 
//		papers.put(4, new Paper(4,"4"));
//		papers.put(5, new Paper(5,"5"));
	}

	public ConcurrentHashMap<Integer, Paper> getPapers() {
		return papers;
	}

	public CopyOnWriteArraySet<Paper> selectAll() throws DaoSystemException {
		if (papers != null)
			return new CopyOnWriteArraySet<>(papers.values());
		throw new DaoSystemException("Papers map is empty !");

	}

	public Paper selectById(int id) throws DaoSystemException,
			NoSuchEntityException {
		Paper paper = papers.get(id);
		if (paper == null)
			throw new NoSuchEntityException("Wrong ID number: \'" + id
					+ "\' is incorrect!");
		return paper;
	}

	public void setPapers(ConcurrentHashMap<Integer, Paper> papers) {
		this.papers = papers;
	}
}
