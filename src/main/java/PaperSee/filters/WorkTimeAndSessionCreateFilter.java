package filters;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static utility.LogPrinter.*;

public class WorkTimeAndSessionCreateFilter extends BaseHTTPFilter {

	public void doFilter(HttpServletRequest arg0, HttpServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {

		long IN = System.nanoTime();
		HttpSession session = createSession(arg0);
		arg2.doFilter(arg0, arg1);

		long OUT = System.nanoTime();
		int time = (int) TimeUnit.MILLISECONDS.convert((OUT - IN),
				TimeUnit.NANOSECONDS);
		printlnEnd(new StringBuilder("Time to create response in server: ")
				.append(time).append(" ms;\nRequest uri: ")
				.append(arg0.getRequestURI())
		);


		addTime(arg0, session, time);

	}

	private HttpSession createSession(HttpServletRequest arg0) {
		HttpSession session = arg0.getSession(true);

		if (session.isNew()) {
			session.setMaxInactiveInterval(60);
			session.setAttribute("first Request URI", arg0.getRequestURI());
		} /*
		 * else { arg0.changeSessionId(); }
		 */
		return session;
	}

	@SuppressWarnings("unchecked")
	private void addTime(HttpServletRequest arg0, HttpSession session, int time) {
		ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>> list;
		if ((list = (ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>>) session
				.getAttribute("LoadTime")) != null)
			list.get(arg0.getRequestURI()).add(time);
	}
}
