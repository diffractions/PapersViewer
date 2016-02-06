package listeners;

//import java.util.Arrays;
//import java.util.Enumeration;
//import java.util.Map;
//import java.util.Map.Entry;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
//import javax.servlet.http.HttpServletRequest;

/**
 * Application Lifecycle Listener implementation class
 * RequestInformationListener
 *
 */
@WebListener
public class RequestInformationListener implements ServletRequestListener,
		ServletRequestAttributeListener {

	/**
	 * @see ServletRequestListener#requestDestroyed(ServletRequestEvent)
	 */
	public void requestDestroyed(ServletRequestEvent sre) {
	}

	/**
	 * @see ServletRequestAttributeListener#attributeRemoved(ServletRequestAttributeEvent)
	 */
	public void attributeRemoved(ServletRequestAttributeEvent srae) {
	}

	/**
	 * @see ServletRequestListener#requestInitialized(ServletRequestEvent)
	 */
	public void requestInitialized(ServletRequestEvent sre) {
//		System.out.println("========== New request ==========");
//		HttpServletRequest arg0 = (HttpServletRequest) sre.getServletRequest();
//		System.out.println("|===>> Client information");
//		System.out.println("|--->  Addr: " + arg0.getRemoteAddr());
//		System.out.println("|--->  Host: " + arg0.getRemoteHost());
//		System.out.println("|--->  Port: " + arg0.getRemotePort());
//		System.out.println("|--->  User: " + arg0.getRemoteUser());
//		System.out.println("|--->  auth: " + arg0.getAuthType());
//		System.out.println("|--->  PathInfo: " + arg0.getPathInfo());
//		System.out.println("|--->  ServerName: " + arg0.getServerName());
//
//		System.out.println("|\n|===>> Header information");
//		Enumeration<String> names = arg0.getHeaderNames();
//		while (names.hasMoreElements()) {
//			String name = names.nextElement();
//			System.out.println("|--->  " + name + " : " + arg0.getHeader(name));
//		}
//
//		System.out.println("|\n|===>> Request URI:" + System.lineSeparator()
//				+ "|--->  " + arg0.getRequestURI());
//		System.out.println("|===>> Request URL:" + System.lineSeparator()
//				+ "|--->  " + arg0.getRequestURL().toString());
//		System.out.println("|===>> Query String:" + System.lineSeparator()
//				+ "|--->  " + arg0.getQueryString());
//		System.out.println("|===>> Context Path:" + System.lineSeparator()
//				+ "|--->  " + arg0.getContextPath());
//
//		Map<String, String[]> parameters = arg0.getParameterMap();
//		if (parameters.size() > 0) {
//			System.out.println("|\n|===>> PARAMETERS IN  REQUEST");
//			for (Entry<String, String[]> parameter : parameters.entrySet()) {
//				System.out.println("|--->  " + parameter.getKey() + " : "
//						+ Arrays.toString(parameter.getValue()));
//			}
//		}
	}

	/**
	 * @see ServletRequestAttributeListener#attributeAdded(ServletRequestAttributeEvent)
	 */
	public void attributeAdded(ServletRequestAttributeEvent srae) {
//		System.out.println("|===>> Request attribute added");
//		System.out.println("|--->  Atribute name:" + srae.getName());
//		System.out.println("|--->  Atribute value:" + srae.getValue());
	}

	/**
	 * @see ServletRequestAttributeListener#attributeReplaced(ServletRequestAttributeEvent)
	 */
	public void attributeReplaced(ServletRequestAttributeEvent srae) {

	}

}
