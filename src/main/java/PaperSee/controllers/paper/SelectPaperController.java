package controllers.paper;

import inject.Inject;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.PaperDao;
import dao.exceptions.DaoException;
import entity.Paper;
import transaction.TransactionManager;
import transaction.exception.TransactionException;
import utility.exception.ReadWriteCodeException;
import org.apache.log4j.Logger;
import static utility.CookiePaperSetUtil.getPapersFromCookie;
import static utility.CookiePaperSetUtil.writePapersInCookie;

public class SelectPaperController extends DependencyInjectionServlet {

	private static final long serialVersionUID = 1L;

	public static final String PAGE_OK = "get.do";
	public static final String PAGE_ERROR = "/404.jsp";
	public static final String PARAM_ID = "id";
	public static final String COOKIE_NAME = "selected";
	public static final String ATTR_ACTIVE_USER_REQUEST_COUNT = "countOfActiveRequest";
	public static final String ATTR_USER_SELECTED_PAPER = COOKIE_NAME;
	public static final String ATTRIBUTE_ERR_CODE = "errorCode";

	@Inject("paperDao")
	public PaperDao paperDao;

	@Inject("txManager")
	public TransactionManager txManager;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {

			if (paperDao == null) {
				throw new DaoException("Papers DAO not found");
			}

			int id = Integer.parseInt(req.getParameter(PARAM_ID));
			Paper paper;

			if (txManager == null) {
				Logger.getLogger("LOG").info("Transaction field is empty");
				paper = paperDao.selectById(id);

			} else {
				Callable<Paper> returned = new Callable<Paper>() {
					@Override
					public Paper call() throws Exception {
						return paperDao.selectById(id);
					}
				};
				paper = txManager.doInTransaction(returned);
			}

			if (paper == null) {
				throw new NullPointerException("Selected paper not found");
			}

			Logger.getLogger("LOG").debug(">>>  ADD SELECTED STORY COOKIE");
			addPaperInStory(req, resp, paper);
			Logger.getLogger("LOG").info(">>>  Redirect to :" + PAGE_OK);
			resp.sendRedirect(PAGE_OK + "?" + PARAM_ID + "=" + id);
			return;

		} catch (DaoException | NumberFormatException | NullPointerException
				| ReadWriteCodeException | TransactionException e) {
			Logger.getLogger("LOG").error("", e);
		}


		req.setAttribute(ATTRIBUTE_ERR_CODE, "404");
		Logger.getLogger("LOG").info(">>>  Redirect to :" + PAGE_ERROR);
		getServletContext().getRequestDispatcher(PAGE_ERROR).include(req, resp);

	}

	/**
	 * Add paper found by PARAM_ID from request in paperDao to specific response
	 * Cookie map with name <b>selected</b>
	 */
	private void addPaperInStory(HttpServletRequest req,
			HttpServletResponse resp, Paper paper) throws DaoException,
			ReadWriteCodeException, NullPointerException {

		incrementCountOfActiveRequest(req);
		Set<Paper> papers = null;

		try {
			papers = readUserSelectedPaperSet(req, paper);
			writeUserSelectedPaperSet(req, resp, papers);
		} catch (NullPointerException | ReadWriteCodeException e) {
			throw e;
		} finally {
			decrementCountOfActiveRequest(papers, req);
		}

	}

	private void decrementCountOfActiveRequest(Set<Paper> papers,
			HttpServletRequest req) {
		if (((AtomicInteger) req.getSession().getAttribute(
				ATTR_ACTIVE_USER_REQUEST_COUNT)).decrementAndGet() == 0) {
			papers = null;
			req.getSession().removeAttribute(ATTR_USER_SELECTED_PAPER);
		}
	}

	/**
	 * Return count of active request from one user browser
	 */
	private AtomicInteger incrementCountOfActiveRequest(HttpServletRequest req) {
		AtomicInteger countOfActiveRequest = (AtomicInteger) req.getSession()
				.getAttribute(ATTR_ACTIVE_USER_REQUEST_COUNT);

		if (countOfActiveRequest == null) {
			countOfActiveRequest = new AtomicInteger(0);
			req.getSession().setAttribute(ATTR_ACTIVE_USER_REQUEST_COUNT,
					countOfActiveRequest);
		}
		countOfActiveRequest.incrementAndGet();
		return countOfActiveRequest;
	}

	/**
	 * get Set of all papers selected by user. Create new set if set in session
	 * not exists.
	 * 
	 * @throws ReadWriteCodeException
	 */
	@SuppressWarnings("unchecked")
	private Set<Paper> readUserSelectedPaperSet(HttpServletRequest req,
			Paper paper) throws ReadWriteCodeException {

		Set<Paper> papers = (Set<Paper>) req.getSession().getAttribute(
				ATTR_USER_SELECTED_PAPER);

		if (papers == null
				&& (papers = getPapersFromCookie(req.getCookies(), COOKIE_NAME)) == null) {
			Logger.getLogger("LOG").debug(">>>  Paper from this request cookie:\n" + papers);
			papers = Collections.singleton(paper);
		} else {
			Logger.getLogger("LOG").info(">>>  Paper from this request cookie:\n" + papers);
			papers = new HashSet<Paper>(papers);
			papers.add(paper);
			papers = Collections.unmodifiableSet(papers);
		}

		req.getSession().setAttribute(ATTR_USER_SELECTED_PAPER, papers);
		return papers;
	}

	private void writeUserSelectedPaperSet(HttpServletRequest req,
			HttpServletResponse resp, Set<Paper> papers)
			throws ReadWriteCodeException {
		Logger.getLogger("LOG").debug(">>>  Papers sending in request cookie:\n" + papers);
		resp.addCookie(writePapersInCookie(papers, COOKIE_NAME,
				req.getContextPath() + "/"));
	}

}
