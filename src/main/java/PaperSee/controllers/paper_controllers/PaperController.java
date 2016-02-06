package controllers.paper_controllers;

import inject.Inject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dao.PaperDao;
import dao.exceptions.DaoException;
import entity.Paper;

public class PaperController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String PAGE_OK = "/Paper.jsp";
	public static final String PAGE_ERROR = "getAll.do";
	public static final String ATTRIBUTE_MODEL_TO_VIEW = "paper";
	public static final String PARAM_ID = "id";

	@Inject("paperDao")
	private PaperDao paperDao;
	public static String APP_CTX_PATH;
	ApplicationContext context;

	@Override
	public void init() throws ServletException {
		APP_CTX_PATH = getServletContext().getInitParameter("project_context");
//		System.out.println(">>>  APP_CTX_PATH : " + APP_CTX_PATH);
		context = new ClassPathXmlApplicationContext(APP_CTX_PATH);
		paperDao = (PaperDao) context.getBean("paperDao");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			// System.out.println("______________________________________\n"
			// + ">>>  Add " + ATTRIBUTE_MODEL_TO_VIEW
			// + " to request attribute");
			int id = Integer.parseInt(req.getParameter(PARAM_ID));
			Paper modelPaper = paperDao.selectById(id);
			req.setAttribute(ATTRIBUTE_MODEL_TO_VIEW, modelPaper);

			// OK
			// System.out.println(">>>  Redirect to :" + PAGE_OK);
			getServletContext().getRequestDispatcher(PAGE_OK)
					.include(req, resp);
			return;
		} catch (DaoException | NumberFormatException e) {
			// System.err.println(">>>  Wrong ID");
		}

		// System.out.println(">>>  Redirect to :" + PAGE_ERROR);
		resp.sendRedirect(PAGE_ERROR);
	}
}
