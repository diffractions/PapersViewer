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
import org.apache.log4j.Logger;

public class PaperAllController extends DependencyInjectionServlet {

	private static final long serialVersionUID = 1L;

	public static final String PAGE_OK = "/PaperAll.jsp";
	public static final String PAGE_ERROR = "/404.jsp";
	public static final String ATTRIBUTE_MODEL_TO_VIEW = "papers";
	public static final String ATTRIBUTE_ERR_STR = "errorString";
	public static final String ATTRIBUTE_ERR_CODE = "errorCode";

	@Inject("paperDao")
	public PaperDao paperDao;

	@Inject("txManager")
	public TransactionManager txManager;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			Logger.getLogger("LOG").info(
					">>>  Add " + ATTRIBUTE_MODEL_TO_VIEW
							+ " to request attribute");

			if (paperDao == null) {
				throw new DaoException("Papers DAO not found");
			}

			CopyOnWriteArraySet<Paper> model;
			if (txManager == null) {
				Logger.getLogger("LOG").warn("Transaction field is empty");
				model = paperDao.selectAll();
			} else {
				Callable<CopyOnWriteArraySet<Paper>> returned = new Callable<CopyOnWriteArraySet<Paper>>() {
					@Override
					public CopyOnWriteArraySet<Paper> call() throws Exception {
						return paperDao.selectAll();
					}
				};
				model = txManager.doInTransaction(returned);
			}

			Logger.getLogger("LOG").info(">>>  ALL_PAPERS:" + model);
			req.setAttribute(ATTRIBUTE_MODEL_TO_VIEW, model);

			Logger.getLogger("LOG").info(">>>  Redirect to :" + PAGE_OK);
			getServletContext().getRequestDispatcher(PAGE_OK)
					.include(req, resp);
			return;

		} catch (DaoException | TransactionException e) {
			Logger.getLogger("LOG").error("", e);

			req.setAttribute(ATTRIBUTE_ERR_CODE, "404");
			req.setAttribute(ATTRIBUTE_ERR_STR, e.getMessage());
		}

		Logger.getLogger("LOG").warn(">>>  Redirect to :" + PAGE_ERROR);
		getServletContext().getRequestDispatcher(PAGE_ERROR).include(req, resp);
	}
}
