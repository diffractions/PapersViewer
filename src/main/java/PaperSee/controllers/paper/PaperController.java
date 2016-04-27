package controllers.paper;

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
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String PAGE_OK = "/Paper.jsp";
	public static final String PAGE_ERROR = "/404.jsp";
	public static final String ATTRIBUTE_MODEL_TO_VIEW = "paper";
	public static final String ATTRIBUTE_ERR = "errorString";
	public static final String PARAM_ID = "id";

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
				Callable<Paper> returned = new Callable<Paper>() {
					@Override
					public Paper call() throws Exception {
						// System.out.println("______________________________________\n"
						// + ">>>  Add " + ATTRIBUTE_MODEL_TO_VIEW
						// + " to request attribute");
						int id = Integer.parseInt(req.getParameter(PARAM_ID));
						Paper modelPaper = paperDao.selectById(id);
						return modelPaper;
					}
				};

				Paper modelPaper = txManager.doInTransaction(returned);
				req.setAttribute(ATTRIBUTE_MODEL_TO_VIEW, modelPaper);

				// OK
				// System.out.println(">>>  Redirect to :" + PAGE_OK);
				getServletContext().getRequestDispatcher(PAGE_OK).include(req,
						resp);
				return;

			}

		} catch (/* DaoException | NumberFormatException | TransactionException */Exception e) {
			req.setAttribute(ATTRIBUTE_ERR, e.getMessage());
			// System.err.println(">>>  Wrong ID");
		}

		// System.out.println(">>>  Redirect to :" + PAGE_ERROR);
		// resp.sendRedirect(PAGE_ERROR);
		getServletContext().getRequestDispatcher(PAGE_ERROR).include(req, resp);
	}
}
