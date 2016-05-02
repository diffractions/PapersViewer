package dao.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import dao.PaperDao;
import dao.exceptions.DaoSystemException;
import dao.exceptions.NoSuchEntityException;
import entity.Paper;

public class PaperDaoDemo implements PaperDao {

	private ConcurrentHashMap<Integer, Paper> papers;

	public ConcurrentHashMap<Integer, Paper> getPapers() {
		return papers;
	}

	public void setPapers(ConcurrentHashMap<Integer, Paper> papers) {
		this.papers = papers;
	}

	public CopyOnWriteArraySet<Paper> selectAll() throws DaoSystemException {
		if (papers != null)
			return new CopyOnWriteArraySet<Paper>(papers.values());
		throw new DaoSystemException("Papers map is empty !");

	}

	public Paper selectById(int id) throws DaoSystemException,
			NoSuchEntityException {

		Paper paper;
		try {
			paper = (Paper) papers.get(id).clone();
		} catch (NullPointerException | CloneNotSupportedException e) {
			paper = null;
		}

		if (paper == null) {
			throw new NoSuchEntityException("Wrong ID number: \'" + id
					+ "\' is incorrect!");
		}

		// println(" >>>" + paper.hashCode());
		return paper;
	}

}
