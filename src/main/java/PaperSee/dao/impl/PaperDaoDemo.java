package dao.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import dao.PaperDao;
import dao.exceptions.DaoSystemException;
import dao.exceptions.NoSuchEntityException;
import entity.Paper;

@SuppressWarnings("unused")
public class PaperDaoDemo implements PaperDao {
	private Map<Integer, Paper> papers;// = new ConcurrentHashMap<Integer, Paper>();

	public PaperDaoDemo() {
		System.out.println(">>>  ADD PAPERS IN PAPER DAO DEMO");
//		papers.put(1, new Paper(1, "my1"));
//		papers.put(2, new Paper(2, "my2"));
//		papers.put(3, new Paper(3, "my3"));
//		papers.put(4, new Paper(4, "my4"));
//		papers.put(5, new Paper(5, "my5"));
	}

	public Map<Integer, Paper> getPapers() {
		return papers;
	}

	public CopyOnWriteArraySet<Paper> selectAll() throws DaoSystemException {
		return new CopyOnWriteArraySet<>(papers.values());

	}

	public Paper selectById(int id) throws DaoSystemException,
			NoSuchEntityException {
		Paper paper = papers.get(id);
		if (paper == null)
			throw new NoSuchEntityException("ID number that you input " + id
					+ " is wrong");
		return paper;
	}

	public void setPapers(Map<Integer, Paper> papers) {
		this.papers = papers;
	}
}
