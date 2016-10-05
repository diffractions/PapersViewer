package filters;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//@WebFilter(filterName = "WorkTimeAndCountClickFilter", urlPatterns = { "/*" })
public class WorkTimeAndCountClickFilter extends BaseHTTPFilter {

	public static final String ATTRIBUTE_MAP_STORY = "map";
	public static final String ATTRIBUTE_LOAD_TIMES = "LoadTime";

	@Override
	public void doFilter(HttpServletRequest arg0, HttpServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {

		long IN = System.nanoTime();

		arg2.doFilter(arg0, arg1);

		HttpSession session = arg0.getSession(false);
		String requestURI = arg0.getRequestURI();
		int count = addRequestInStory(requestURI, session);

		printVisitInfomation(arg1, count, session);

		long OUT = System.nanoTime();
		int time = (int) TimeUnit.MILLISECONDS.convert((OUT - IN),
				TimeUnit.NANOSECONDS);

		addTime(requestURI, session, time);
		log.info(new StringBuilder("Time to create response in server: ")
				.append(time).append(" ms;\nRequest uri: ").append(requestURI));

	}

	@SuppressWarnings("static-access")
	private void printVisitInfomation(HttpServletResponse arg1, int count,
			HttpSession session) throws IOException {
		if (!arg1.isCommitted())
			arg1.getWriter().write(
					"	<div class=\"container showgrid\"><hr>You visit this page "
							+ count
							+ " times in last "
							+ (System.currentTimeMillis() - session
									.getCreationTime())
							/ 1000.0
							+ " seconds."
							+ "<br> class loader "
							+ this.getClass().getClassLoader()
							+ "<br> class loader "
							+ this.getClass().getClassLoader()
									.getSystemClassLoader() + "</div>");
	}

	private int addRequestInStory(String requestURI, HttpSession session) {
		ConcurrentHashMap<String, Integer> sessionStoryMap;
		int count = 0;

		if (session != null) {
			sessionStoryMap = getSessionStoryMap(session);
			count = addPathToSessionStory(sessionStoryMap, requestURI);
			sessionStoryMap.put(requestURI, ++count);
		}
		return count;
	}

	private int addPathToSessionStory(
			ConcurrentHashMap<String, Integer> sessionStoryMap, String uri) {
		int count;
		if (sessionStoryMap.get(uri) != null) {
			log.debug(">>>  Path was found in session story");
			count = sessionStoryMap.get(uri);
		} else {
			log.debug(">>>  Add new path to session story");
			count = 0;
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>> getSessionLoadTime(
			HttpSession session) {
		ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>> loadTimes;
		if (session.getAttribute(ATTRIBUTE_LOAD_TIMES) != null) {
			log.debug(">>>  Session Load Time map was called");
			loadTimes = ((ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>>) session
					.getAttribute(ATTRIBUTE_LOAD_TIMES));
		} else {
			log.debug(">>>  Session Load Time map was created");
			loadTimes = new ConcurrentHashMap<>();
			session.setAttribute(ATTRIBUTE_LOAD_TIMES, loadTimes);
		}
		return loadTimes;
	}

	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<String, Integer> getSessionStoryMap(
			HttpSession session) {
		ConcurrentHashMap<String, Integer> storyMap;
		if (session.getAttribute(ATTRIBUTE_MAP_STORY) != null) {
			log.debug(">>>  Session story map was called");
			storyMap = ((ConcurrentHashMap<String, Integer>) session
					.getAttribute(ATTRIBUTE_MAP_STORY));
		} else {
			log.debug(">>>  Session story map was created");
			storyMap = new ConcurrentHashMap<>();
			session.setAttribute(ATTRIBUTE_MAP_STORY, storyMap);
		}
		return storyMap;
	}

	private void addTime(String requestURI, HttpSession session, int time) {

		ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>> loadTime;

		if (session != null) {
			loadTime = getSessionLoadTime(session);
			addPathToSessionLoadTime(loadTime, requestURI);
			loadTime.get(requestURI).add(time);
		}

	}

	private void addPathToSessionLoadTime(
			ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>> loadTime,
			String uri) {
		if (loadTime.get(uri) == null) {
			log.debug(">>>  Add new path to Load Time");
			loadTime.put(uri, new CopyOnWriteArrayList<Integer>());
		} else {
			log.debug(">>>  Path was found in Load Time");
		}
	}

}
