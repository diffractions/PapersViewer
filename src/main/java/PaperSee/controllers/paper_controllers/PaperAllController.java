package controllers.paper_controllers;

import inject.Inject;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dao.PaperDao;
import dao.exceptions.DaoException;
import entity.Paper;


public class PaperAllController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String PAGE_OK = "/PaperAll.jsp";
	public static final String PAGE_ERROR = "/404.jsp";
	public static final String ATTRIBUTE_MODEL_TO_VIEW = "papers";

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

			CopyOnWriteArraySet<Paper> model = paperDao.selectAll();
			req.setAttribute(ATTRIBUTE_MODEL_TO_VIEW, model);

			// System.out.println(">>>  Redirect to :" + PAGE_OK);
			getServletContext().getRequestDispatcher(PAGE_OK)
					.include(req, resp);
			return;
		} catch (DaoException e) {
			// System.err.println("PAPERS NOT FOUND!!!");
		}
 
		// System.out.println(">>>  Redirect to :" + PAGE_ERROR);
		getServletContext().getRequestDispatcher(PAGE_ERROR).include(req, resp);
	}
}
