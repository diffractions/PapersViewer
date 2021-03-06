package controllers;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileResponseController extends HttpServlet {

	public static Logger log = Logger.getLogger("LOG");
	
	public static final long serialVersionUID = 1L;
	public static final String PAGE_DIRECTORY = "/resources/DirectoryView.jsp";
	public static final String PAGE_FILE = "/showFile";
	public static final String PAGE_ERROR = "/resources/404.jsp";
	public static final String ATTRIBUTE_ERR_CODE = "errorCode";
	public static final String ATTRIBUTE_FILE_PATH = "filePath";
	public static final String ATTRIBUTE_FILES_CHILDREN = "filesChildren";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		log.debug(">>>  Response from file or directory");

		try {
			String absolutePath = req.getRequestURI().substring(
					req.getSession().getServletContext().getContextPath()
							.getBytes().length);
			String path = getServletContext().getRealPath(absolutePath)
					.replace("%20", " ");

			log.debug(">>>  Absolute path : " + absolutePath);
			log.debug(">>>  Real Path : " + path);
			File file = new File(path);

			if (file.exists()) {
 
				if (file.isDirectory()) {

					log.debug(">>>  Directory found");
					log.debug(
							">>> Add files from folder in request attribute");
					req.setAttribute(ATTRIBUTE_FILES_CHILDREN, file.listFiles());

					log.debug(">>>  Redirect to " + PAGE_DIRECTORY);
					getServletContext().getRequestDispatcher(PAGE_DIRECTORY)
							.include(req, resp);

				} else {
					log.debug(">>>  Fille found");
					log.debug(">>> Add file in request attribute");
					req.setAttribute(ATTRIBUTE_FILE_PATH, path);


					log.debug(">>> Det Request Dispatcher: include " + PAGE_FILE);
					getServletContext().getRequestDispatcher(PAGE_FILE)
							.include(req, resp);
				}
				return;
			}
		} catch (FileNotFoundException | NullPointerException e) {
			log.fatal("", e);
		}

		log.debug(">>>  Fille not found");
		log.debug(">>>  Redirect to " + PAGE_ERROR);
		req.setAttribute(ATTRIBUTE_ERR_CODE, "404");
		getServletContext().getRequestDispatcher(PAGE_ERROR).include(req,
				resp);

	}
}
