package dao.impl;

import java.util.concurrent.ConcurrentHashMap;

import entity.Paper;
import entity.SimplePaper;

public class PaperDaoSimpleImplementation extends PaperDaoDemo {

	public PaperDaoSimpleImplementation() {

		ConcurrentHashMap<Integer, Paper> papers = new ConcurrentHashMap<Integer, Paper>();
		papers.put(1, new SimplePaper(1, "Paper"));
		papers.put(2, new SimplePaper(2, "From"));
		papers.put(3, new SimplePaper(3, "Simple"));
		papers.put(4, new SimplePaper(4, "DAO"));
		papers.put(5, new SimplePaper(5, "Implementation"));

		this.setPapers(papers);
	}

}
