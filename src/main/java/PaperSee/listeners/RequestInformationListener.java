package listeners;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * Application Lifecycle Listener implementation class
 * RequestInformationListener
 *
 */
@WebListener
public class RequestInformationListener implements ServletRequestListener,
		ServletRequestAttributeListener {

	public static Logger log = Logger.getLogger("LOG");

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

		HttpServletRequest arg0 = (HttpServletRequest) sre.getServletRequest();
		log.trace("|\n|========== New request =========="
				+ "\n|===>> Client information" + "\n|--->  Addr: "
				+ arg0.getRemoteAddr() + "\n|--->  Host: "
				+ arg0.getRemoteHost() + "\n|--->  Port: "
				+ arg0.getRemotePort() + "\n|--->  User: "
				+ arg0.getRemoteUser() + "\n|--->  auth: " + arg0.getAuthType()
				+ "\n|--->  PathInfo: " + arg0.getPathInfo()
				+ "\n|--->  ServerName: " + arg0.getServerName());

		StringBuilder sb = new StringBuilder("|\n|===>> Header information");
		Enumeration<String> names = arg0.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			sb.append("\n|--->  " + name + " : " + arg0.getHeader(name));
		}
		log.trace(sb);

		log.trace("|\n|===>> Request URI:" + "\n|--->  " + arg0.getRequestURI()
				+ "\n|===>> Request URL:" + "\n|--->  "
				+ arg0.getRequestURL().toString() + "\n|===>> Query String:"
				+ "\n|--->  " + arg0.getQueryString()
				+ "\n|===>> Context Path:" + "\n|--->  "
				+ arg0.getContextPath());

		Map<String, String[]> parameters = arg0.getParameterMap();
		if (parameters.size() > 0) {
			StringBuilder sb1 = new StringBuilder(
					"|\n|===>> PARAMETERS IN  REQUEST");
			for (Entry<String, String[]> parameter : parameters.entrySet()) {
				sb1.append("\n|--->  " + parameter.getKey() + " : "
						+ Arrays.toString(parameter.getValue()));
			}
			log.trace(sb1);
		}
	}

	/**
	 * @see ServletRequestAttributeListener#attributeAdded(ServletRequestAttributeEvent)
	 */
	public void attributeAdded(ServletRequestAttributeEvent srae) {
//		log.trace("|\n|===>> Request attribute added"
//				+ "\n|--->  Atribute name:" + srae.getName()
//				+ "\n|--->  Atribute value:" + srae.getValue());

	}

	/**
	 * @see ServletRequestAttributeListener#attributeReplaced(ServletRequestAttributeEvent)
	 */
	public void attributeReplaced(ServletRequestAttributeEvent srae) {

	}

}
