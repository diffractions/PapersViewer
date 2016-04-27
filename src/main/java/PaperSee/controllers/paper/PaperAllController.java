package controllers.paper;

import inject.Inject;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import transaction.TransactionManager;
import transaction.exception.TransactionException;
import dao.PaperDao;
import dao.exceptions.DaoException;
import entity.Paper;

//import entity.SimplePaper;

public class PaperAllController extends DependencyInjectionServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String PAGE_OK = "/PaperAll.jsp";
	public static final String PAGE_ERROR = "/404.jsp";
	public static final String ATTRIBUTE_MODEL_TO_VIEW = "papers";
	public static final String ATTRIBUTE_ERR = "errorString";

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
			// System.out.println("______________________________________\n"
			// + ">>>  Add " + ATTRIBUTE_MODEL_TO_VIEW
			// + " to request attribute");

			if (paperDao == null) {
				throw new DaoException("Papers DAO not found");
			}

			if (txManager == null) {
				throw new TransactionException("?????????????????????");
			} else {
				Callable<CopyOnWriteArraySet<Paper>> returned = new Callable<CopyOnWriteArraySet<Paper>>() {
					@Override
					public CopyOnWriteArraySet<Paper> call() throws Exception {

						return paperDao.selectAll();
					}
				};

				CopyOnWriteArraySet<Paper> model = txManager
						.doInTransaction(returned);
				// System.out.println(">>>  ALL_PAPERS:" + model);
				req.setAttribute(ATTRIBUTE_MODEL_TO_VIEW, model);
				// System.out.println(">>>  Redirect to :" + PAGE_OK);
				getServletContext().getRequestDispatcher(PAGE_OK).include(req,
						resp);
				return;

			}

			// CopyOnWriteArraySet<Paper> model = paperDao.selectAll();
			// System.out.println(">>>  ALL_PAPERS:" + model);
			// req.setAttribute(ATTRIBUTE_MODEL_TO_VIEW, model);
			//
			// System.out.println(">>>  Redirect to :" + PAGE_OK);
			// getServletContext().getRequestDispatcher(PAGE_OK)
			// .include(req, resp);
			// return;
		} catch (/* DaoException | TransactionException | */Exception e) {

			System.out
					.println("CONTROLLERS.PAPER.PAC.DoGet:EXCEPTION.caused by:"
							+ e.getMessage());
			req.setAttribute(ATTRIBUTE_ERR, e.getMessage());

		}

		// System.out.println(">>>  Redirect to :" + PAGE_ERROR);
		getServletContext().getRequestDispatcher(PAGE_ERROR).include(req, resp);
	}
}
