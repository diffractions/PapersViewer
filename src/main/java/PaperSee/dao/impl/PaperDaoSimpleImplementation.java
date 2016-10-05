package dao.impl;

import java.util.concurrent.ConcurrentHashMap;

import entity.Paper;
import entity.SimplePaper;

public class PaperDaoSimpleImplementation extends PaperDaoDemo {

	public PaperDaoSimpleImplementation() {

		ConcurrentHashMap<String, Paper> papers = new ConcurrentHashMap<String, Paper>();
		papers.put("s", new SimplePaper("s", "Paper"));
		papers.put("w", new SimplePaper("w", "From"));
		papers.put("e", new SimplePaper("e", "Simple"));
		papers.put("r", new SimplePaper("r", "DAO"));
		papers.put("t", new SimplePaper("t", "Implementation"));

		this.setPapers(papers);
	}

}
