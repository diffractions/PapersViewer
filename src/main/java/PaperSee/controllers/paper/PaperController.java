package controllers.paper;

import org.apache.log4j.Logger;
import inject.Inject;

import java.io.IOException;
import java.util.concurrent.Callable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import transaction.TransactionManager;
import transaction.exception.TransactionException;
import dao.PaperDao;
import dao.exceptions.DaoException;
import entity.Paper;

public class PaperController extends DependencyInjectionServlet {

	private static final long serialVersionUID = 1L;

	public static final String PAGE_OK = "/Paper.jsp";
	public static final String PAGE_ERROR = "/404.jsp";
	public static final String ATTRIBUTE_MODEL_TO_VIEW = "paper";
	public static final String ATTRIBUTE_ERR_STR = "errorString";
	public static final String ATTRIBUTE_ERR_CODE = "errorCode";
	public static final String PARAM_ID = "id";

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

			Paper modelPaper;
			int id = Integer.parseInt(req.getParameter(PARAM_ID));
			Logger.getLogger("LOG").info(
					">>>  Add " + ATTRIBUTE_MODEL_TO_VIEW
							+ " to request attribute");

			if (txManager == null) {
				Logger.getLogger("LOG").warn("Transaction field is empty");
				modelPaper = paperDao.selectById(id);
			} else {
				Callable<Paper> returned = new Callable<Paper>() {
					@Override
					public Paper call() throws Exception {
						return paperDao.selectById(id);
					}
				};
				modelPaper = txManager.doInTransaction(returned);
			}

			req.setAttribute(ATTRIBUTE_MODEL_TO_VIEW, modelPaper);
			Logger.getLogger("LOG").info(">>>  Redirect to :" + PAGE_OK);
			getServletContext().getRequestDispatcher(PAGE_OK)
					.include(req, resp);
			return;

		} catch (DaoException | NumberFormatException | TransactionException e) {
			req.setAttribute(ATTRIBUTE_ERR_STR, e.getMessage());
			req.setAttribute(ATTRIBUTE_ERR_CODE, "404");
			Logger.getLogger("LOG").error("", e);
		}

		Logger.getLogger("LOG").warn(">>>  Redirect to :" + PAGE_ERROR);
		// resp.sendRedirect(PAGE_ERROR);
		getServletContext().getRequestDispatcher(PAGE_ERROR).include(req, resp);
	}
}
