package filters;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CountClickFilter extends BaseHTTPFilter {

	@Override
	public void doFilter(HttpServletRequest arg0, HttpServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {

		int count = addRequestInStory(arg0);
		arg2.doFilter(arg0, arg1);
		printVisitInfomation(arg0, arg1, count);

	}

	private void printVisitInfomation(HttpServletRequest arg0,
			HttpServletResponse arg1, int count) throws IOException {
		if (!arg1.isCommitted())

			arg1.getWriter().write(
					"<hr>You visit this page "
							+ count
							+ " times in last "
							+ (System.currentTimeMillis() - arg0.getSession()
									.getCreationTime()) / 1000.0 + " seconds.");
	}

	private int addRequestInStory(HttpServletRequest arg0) {
		ConcurrentHashMap<String, Integer> sessionStoryMap;
		ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>> loadTime;
		int count = 0;
		String uri = arg0.getRequestURI();
		HttpSession session;

//		System.out.println("______________________________________\n"
//				+ ">>>  Count filter information");

		if ((session = (HttpSession) arg0.getSession(false)) != null) {

			sessionStoryMap = getSessionStoryMap(session);
			loadTime = getSessionLoadTime(session);
			count = addPathToSessionStory(sessionStoryMap, uri);
			addPathToSessionLoadTime(loadTime, uri);
			sessionStoryMap.put(uri, ++count);

		}
		return count;
	}

	private void addPathToSessionLoadTime(
			ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>> loadTime,
			String uri) {
		if (loadTime.get(uri) == null) {
//			System.out.println(">>>  Add new path to Load Time");
			loadTime.put(uri, new CopyOnWriteArrayList<Integer>());
//		} else {
//			System.out.println(">>>  Path was found in Load Time");
		}
	}

	private int addPathToSessionStory(
			ConcurrentHashMap<String, Integer> sessionStoryMap, String uri) {
		int count;
		if (sessionStoryMap.get(uri) != null) {
//			System.out.println(">>>  Path was found in session story");
			count = sessionStoryMap.get(uri);
		} else {
//			System.out.println(">>>  Add new path to session story");
			count = 0;
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>> getSessionLoadTime(
			HttpSession session) {
		ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>> LoadTime;
		if (session.getAttribute("LoadTime") != null) {
//			System.out.println(">>>  Session Load Time map was called");
			LoadTime = ((ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>>) session
					.getAttribute("LoadTime"));
		} else {
//			System.out.println(">>>  Session Load Time map was created");
			LoadTime = new ConcurrentHashMap<>();
			session.setAttribute("LoadTime", LoadTime);
		}
		return LoadTime;
	}

	@SuppressWarnings("unchecked")
	private ConcurrentHashMap<String, Integer> getSessionStoryMap(
			HttpSession session) {
		ConcurrentHashMap<String, Integer> map;
		if (session.getAttribute("map") != null) {
//			System.out.println(">>>  Session story map was called");
			map = ((ConcurrentHashMap<String, Integer>) session
					.getAttribute("map"));
		} else {
//			System.out.println(">>>  Session story map was created");
			map = new ConcurrentHashMap<>();
			session.setAttribute("map", map);
		}
		return map;
	}

}
