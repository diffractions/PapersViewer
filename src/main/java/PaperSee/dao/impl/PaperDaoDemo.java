package dao.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import dao.PaperDao;
import dao.exceptions.DaoSystemException;
import dao.exceptions.NoSuchEntityException;
import entity.Paper;

//import entity.SimplePaper;

public class PaperDaoDemo implements PaperDao {

	private ConcurrentHashMap<Integer, Paper> papers;

	// public PaperDaoDemo() {
	// papers = new ConcurrentHashMap<Integer, Paper>();
	// papers.put(1, new SimplePaper(1,"1"));
	// papers.put(2, new SimplePaper(2,"2"));
	// papers.put(3, new SimplePaper(3,"3"));
	// papers.put(4, new SimplePaper(4,"4"));
	// papers.put(5, new SimplePaper(5,"5"));
	// }

	public ConcurrentHashMap<Integer, Paper> getPapers() {
		return papers;
	}

	public void setPapers(ConcurrentHashMap<Integer, Paper> papers) {
		this.papers = papers;
	}

	public CopyOnWriteArraySet<Paper> selectAll() throws DaoSystemException {
		if (papers != null)
			return new CopyOnWriteArraySet<>(papers.values());
		throw new DaoSystemException("Papers map is empty !");

	}

	public Paper selectById(int id) throws DaoSystemException,
			NoSuchEntityException {
		
		Paper paper;
		try {
			paper = (Paper) papers.get(id).clone();
		} catch (CloneNotSupportedException e) {
			paper = null;
		}
		
		if (paper == null) {
			throw new NoSuchEntityException("Wrong ID number: \'" + id
			+ "\' is incorrect!");
		}

//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>" + paper.hashCode());
		return paper;
	}

}
