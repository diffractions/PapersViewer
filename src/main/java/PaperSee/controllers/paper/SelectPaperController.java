package controllers.paper;

import inject.Inject;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.PaperDao;
import dao.exceptions.DaoException;
import entity.Paper;
import transaction.TransactionManager;
import transaction.exception.TransactionException;
import utility.ObjectBase64Coder;
import utility.exception.ReadWriteCodeException;
import static utility.LogPrinter.*;

public class SelectPaperController extends DependencyInjectionServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String PAGE_OK = "get.do";
	public static final String PAGE_ERROR = "/404.jsp";
	public static final String PARAM_ID = "id";
	public static final String COOKIE_NAME = "selected";
	public static final String ATTR_ACTIVE_USER_REQUEST_COUNT = "countOfActiveRequest";
	public static final String ATTR_USER_SELECTED_PAPER = COOKIE_NAME;

	@Inject("paperDao")
	public PaperDao paperDao;

	@Inject("txManager")
	public TransactionManager txManager;

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {

			if (paperDao == null) {
				throw new DaoException("Papers DAO not found");
			}

			if (txManager == null) {
				throw new TransactionException("Transaction field is empty");
			} else {
				Callable<Integer> returned = new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						int id = Integer.parseInt(req.getParameter(PARAM_ID));

						// System.out.println("______________________________________\n"
						// + ">>>  ADD SELECTED STORY COOKIE");
						addPaperInStory(req, resp, id);

						return id;
					}
				};

				int id = txManager.doInTransaction(returned);

				// System.out.println(">>>  Redirect to :" + PAGE_OK);
				resp.sendRedirect(PAGE_OK + "?" + PARAM_ID + "=" + id);
			}

			return;

		} catch (/*
				 * DaoException | NumberFormatException | NullPointerException |
				 * ReadWriteCodeException |
				 */Exception e) {
			// NOP
		}

		// System.out.println(">>>  Redirect to :" + PAGE_ERROR);
		getServletContext().getRequestDispatcher(PAGE_ERROR).include(req, resp);

	}

	/**
	 * Add paper found by PARAM_ID from request in paperDao to specific response
	 * Cookie map with name <b>selected</b>
	 */
	private void addPaperInStory(HttpServletRequest req,
			HttpServletResponse resp, int id) throws DaoException,
			ReadWriteCodeException, NullPointerException {

		incrementCountOfActiveRequest(req);
		Set<Paper> papers = null;

		try {

			Paper paper = paperDao.selectById(id);
			papers = getUserSelectedPaperSet(req, paper);
			writePaperInCookie(req, resp, papers);

		} catch (DaoException | NullPointerException | ReadWriteCodeException e) {
			throw e;

		} finally {
			decrementCountOfActiveRequest(papers, req);
		}

	}

	private void writePaperInCookie(HttpServletRequest req,
			HttpServletResponse resp, Set<Paper> papers)
			throws ReadWriteCodeException {
		println("writePaperInCookie:" + papers);
		Cookie selected = writePapersInCookie(papers);
		selected.setPath(req.getContextPath() + "/");
		resp.addCookie(selected);
	}

	private void decrementCountOfActiveRequest(Set<Paper> papers,
			HttpServletRequest req) {

		if (((AtomicInteger) req.getSession().getAttribute(
				ATTR_ACTIVE_USER_REQUEST_COUNT)).decrementAndGet() == 0) {
			papers = null;
			req.getSession().removeAttribute(ATTR_USER_SELECTED_PAPER);
			// System.out.println("*******************************");
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
	 * 
	 */
	@SuppressWarnings("unchecked")
	private Set<Paper> getUserSelectedPaperSet(HttpServletRequest req,
			Paper paper) throws ReadWriteCodeException {

		Set<Paper> papers = (Set<Paper>) req.getSession().getAttribute(
				ATTR_USER_SELECTED_PAPER);

		if (papers == null && (papers = getPapersFromCookie(req)) == null) {
			papers = Collections.singleton(paper);
		} else {
			// System.out.println(">>>  Paper from this request cookie:\n"
			// + papers);
			papers = new HashSet<Paper>(papers);

			// System.out.println("------------------------------------" +
			// paper.hashCode());

			papers.add(paper);
			// System.out.println(">>>  Papers sending in request cookie:\n"
			// + papers);
			papers = Collections.unmodifiableSet(papers);
		}
		req.getSession().setAttribute(ATTR_USER_SELECTED_PAPER, papers);
		return papers;
	}

	/**
	 * Return set of papers obtain from user saved as cookies in browser. If
	 * cookie not found return NULL.
	 * 
	 * @throws ReadWriteCodeException
	 * 
	 */
	private Set<Paper> getPapersFromCookie(HttpServletRequest req)
			throws ReadWriteCodeException {
		Cookie[] cookies = req.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(COOKIE_NAME)) {

					return ObjectBase64Coder.readSetFromString(cookie
							.getValue());

				}
		return null;
	}

	/**
	 * Return object of cookie with wrote BASE64 encode paper set as String
	 * value parameter. Return NULL if can not write paper set in cookie
	 * 
	 * @throws ReadWriteCodeException
	 */
	private Cookie writePapersInCookie(Set<Paper> papers)
			throws ReadWriteCodeException {

		String cookieSetCode = ObjectBase64Coder.writeSetInString(papers);
		return new Cookie(COOKIE_NAME, cookieSetCode);

	}

}
