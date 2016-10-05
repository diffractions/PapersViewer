package dao.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import dao.PaperDao;
import dao.exceptions.DaoSystemException;
import dao.exceptions.NoSuchEntityException;
import entity.Paper;

public class PaperDaoDemo implements PaperDao {

	private ConcurrentHashMap<String, Paper> papers;

	public ConcurrentHashMap<String, Paper> getPapers() {
		return papers;
	}

	public void setPapers(ConcurrentHashMap<String, Paper> papers) {
		this.papers = papers;
	}

	public CopyOnWriteArraySet<Paper> selectAll(String [] collNames, String [] sortKeys) throws DaoSystemException {
		if (papers != null)
			return new CopyOnWriteArraySet<Paper>(papers.values());
		throw new DaoSystemException("Papers map is empty !");

	}

	public Paper selectById(String id) throws DaoSystemException,
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

	@Override
	public CopyOnWriteArraySet<Paper> search(String str)
			throws DaoSystemException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, List<String>>> selectValues(String paperName, String id)
			throws DaoSystemException, NoSuchEntityException {
		// TODO Auto-generated method stub
		return null;
	}

}
