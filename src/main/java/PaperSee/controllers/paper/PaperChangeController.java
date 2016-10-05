package controllers.paper;

import inject.Inject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import transaction.TransactionManager;
import transaction.exception.TransactionException;
import dao.PaperDao;
import dao.PaperInsertDao;
import dao.exceptions.DaoException;

public class PaperChangeController extends DependencyInjectionServlet {

	private static final long serialVersionUID = 1L;

	public static final String PAGE_OK = "/resources/Insert.jsp";
	public static final String PAGE_ERROR = "/resources/404.jsp";

	public static final String ATTRIBUTE_MODEL_TO_VIEW_GET = "tablSet";
	public static final String ATTRIBUTE_MODEL_TO_VIEW_ID = "paper_id";
	public static final String ATTRIBUTE_MODEL_TO_VIEW_POST = "changes";

	public static final String ATTRIBUTE_ERR_STR = "errorString";
	public static final String ATTRIBUTE_ERR_CODE = "errorCode";

	public static final String PARAM_ID = "id";

	public static final String DB_PAPER_TABL_NAME = "paper";
	public static final String DB_MATHERIAL_TABL_NAME = "matherial";
	public static final String DB_METHOD_TABL_NAME = "method";
	public static final String DB_PROPERTIES_TABL_NAME = "properties";

	private static final String CHANGES_ITEM_INDICATOR = "_ch";
	private static final String PATH_DELIMITER = "/";

	@Inject("paperInsertDao")
	public PaperInsertDao paperInsertDao;

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

			String id = req.getParameter(PARAM_ID);
			// tabl     entr    sett       col
			Map<String, List<Map<String, List<String>>>> modelToViev = null;

			if (txManager == null) {
				log.warn("Transaction field is empty");
				Map<String, List<Map<String, List<String>>>> ret = new HashMap<>();
				ret.put(DB_PAPER_TABL_NAME,
						paperDao.selectValues(DB_PAPER_TABL_NAME, id));
				ret.put(DB_METHOD_TABL_NAME,
						paperDao.selectValues(DB_METHOD_TABL_NAME, id));
				ret.put(DB_MATHERIAL_TABL_NAME,
						paperDao.selectValues(DB_MATHERIAL_TABL_NAME, id));
				ret.put(DB_PROPERTIES_TABL_NAME,
						paperDao.selectValues(DB_PROPERTIES_TABL_NAME, id));
				modelToViev = ret;
			} else {
				Callable<Map<String, List<Map<String, List<String>>>>> returned = new Callable<Map<String, List<Map<String, List<String>>>>>() {
					@Override
					public Map<String, List<Map<String, List<String>>>> call()
							throws Exception {
						Map<String, List<Map<String, List<String>>>> ret = new HashMap<>();
						ret.put(DB_PAPER_TABL_NAME,
								paperDao.selectValues(DB_PAPER_TABL_NAME, id));
						ret.put(DB_METHOD_TABL_NAME,
								paperDao.selectValues(DB_METHOD_TABL_NAME, id));
						ret.put(DB_MATHERIAL_TABL_NAME, paperDao.selectValues(
								DB_MATHERIAL_TABL_NAME, id));
						ret.put(DB_PROPERTIES_TABL_NAME, paperDao.selectValues(
								DB_PROPERTIES_TABL_NAME, id));
						return ret;
					}
				};
				modelToViev = txManager.doInTransaction(returned);

			}

			req.setAttribute(ATTRIBUTE_MODEL_TO_VIEW_ID, id);
			req.setAttribute(ATTRIBUTE_MODEL_TO_VIEW_GET, modelToViev);

			getServletContext().getRequestDispatcher(PAGE_OK)
					.include(req, resp);
			return;

		} catch (TransactionException | DaoException e) {
			req.setAttribute(ATTRIBUTE_ERR_STR, e.getMessage());
			req.setAttribute(ATTRIBUTE_ERR_CODE, "404");
			log.error(e);
		}

		log.warn(">>>  Redirect to :" + PAGE_ERROR);
		getServletContext().getRequestDispatcher(PAGE_ERROR).include(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {

			if (paperInsertDao == null) {
				throw new DaoException("Papers DAO not found");
			}

			Map<String, String[]> parameters = req.getParameterMap();
			int changes = 0;

			if (txManager == null) {
				log.warn("Transaction field is empty");

				for (String s : parameters.keySet()) {

					log.info("parameter from request : " + s + " - val:  "
							+ (parameters.get(s)[0].equals("")) + " : "
							+ Arrays.toString(parameters.get(s)));

					if (s.endsWith(CHANGES_ITEM_INDICATOR)
							&& !parameters.get(s)[0].equals("")) {

						new TransactionException("Transaction not found");
					}
				}
			} else {
				Callable<Integer> returned = new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						int changes = 0;
						for (String paprameterKey : parameters.keySet()) {

							log.info("parameter from request\n: PARAMETER -"
									+ paprameterKey + "\n: VALUES -  "
									+ Arrays.toString(parameters.get(paprameterKey)));

							if (paprameterKey.endsWith(CHANGES_ITEM_INDICATOR)) {
 								for (int i = 0; i < parameters.get(paprameterKey).length; i++) {
 									if (!"".equalsIgnoreCase(parameters.get(paprameterKey)[i])) {

 										String[] colPath = paprameterKey.split(PATH_DELIMITER);
 
 										String TABLE_NAME = colPath[0];
										String COL_NAME = colPath[2];

										changes = (paperInsertDao.updateRandom(
												TABLE_NAME,
												COL_NAME,
												req.getParameter(PARAM_ID),
												parameters.get(paprameterKey)[i],
												
												new HashMap<String, Object>(){
	 
													private static final long serialVersionUID = 1L;

													{
														for(String keys : parameters.keySet()){
															if(keys.startsWith(TABLE_NAME+PATH_DELIMITER+colPath[1]) && !keys.endsWith(CHANGES_ITEM_INDICATOR))
															put(keys.split(PATH_DELIMITER)[2], req.getParameter(keys));
														}
													}
												}
												
												
												
												)) ? changes + 1 : changes;

									}
								}
							}
						}
						return changes;
					}
				};
				changes = txManager.doInTransaction(returned);

			}

			req.setAttribute(ATTRIBUTE_MODEL_TO_VIEW_POST, changes);

			doGet(req, resp);
			return;

		} catch (TransactionException | DaoException e) {
			req.setAttribute(ATTRIBUTE_ERR_STR, e.getMessage());
			req.setAttribute(ATTRIBUTE_ERR_CODE, "404");
			log.error(e);
		}

		log.warn(">>>  Redirect to :" + PAGE_ERROR);
		getServletContext().getRequestDispatcher(PAGE_ERROR).include(req, resp);
	}
}
