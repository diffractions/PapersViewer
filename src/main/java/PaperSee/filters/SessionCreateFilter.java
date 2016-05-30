package filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

//@WebFilter(filterName = "WorkTimeFilter", urlPatterns = { "/*" })
public class SessionCreateFilter extends BaseHTTPFilter {

	public static final int MAX_INACTIVE_INTERVAL = 20;
	public static final String ATTRIBUTE_FIRST_REQUEST = "first Request URI";

	public void doFilter(HttpServletRequest arg0, HttpServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {

		createSession(arg0);
		arg2.doFilter(arg0, arg1);

	}

	private void createSession(HttpServletRequest arg0) {
		HttpSession session = arg0.getSession(true);

		if (session.isNew()) {
			session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
			Logger.getLogger("LOG").info(
					"NEW SESSION\ninterval is "
							+ session.getMaxInactiveInterval());
			session.setAttribute(ATTRIBUTE_FIRST_REQUEST, arg0.getRequestURI());
		} else {
			Logger.getLogger("LOG").info(
					"OLD SESSION\ninterval is "
							+ session.getMaxInactiveInterval());/*
																 * arg0.
																 * changeSessionId
																 * () ;
																 */
		}

	}

}
