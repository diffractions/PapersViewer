package listeners;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

/**
 * Application Lifecycle Listener implementation class filterStartListener
 *
 */
@WebListener
public class SessionWriteFileAndChangeListener implements
		HttpSessionIdListener, HttpSessionListener,
		HttpSessionAttributeListener, HttpSessionActivationListener,
		HttpSessionBindingListener {

	@Override
	public void sessionIdChanged(HttpSessionEvent arg0, String arg1) {

		Logger.getLogger("LOG").trace(
				"|\n|========== SESSION ID WAS CHANGED =========="
						+ "\n--------SESSION INFO--------"
						+ "\n|--->  New session id : "
						+ arg0.getSession().getId()
						+ "\n|--->  Session max inactive time : "
						+ arg0.getSession().getMaxInactiveInterval());

	}

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		HttpSession session = arg0.getSession();
		Logger.getLogger("LOG").trace(
				"|\n|========== SESSION WAS CREATED =========="
						+ "\n|===>> SESSION INFO" + "\n|--->  Session id : "
						+ session.getId() + "\n|--->  Session from : "
						+ new Date(session.getCreationTime())
						+ "\n|--->  Session max inactive time : "
						+ session.getMaxInactiveInterval());

	}

	@SuppressWarnings("unchecked")
	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {

		Logger.getLogger("LOG").trace(
				"|\n|========== SESSION WAS DESTROYED =========="
						+ "\n|===>> SESSION INFO"
						+ "\n|--->  Session was created by : "
						+ arg0.getSession().getAttribute("first Request URI"));

		writeStory(
				arg0.getSession()
						.getServletContext()
						.getRealPath(
								arg0.getSession().getServletContext()
										.getInitParameter("sessions")),
				(Map<String, Integer>) arg0.getSession().getAttribute("map"),
				(ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>>) arg0
						.getSession().getAttribute("LoadTime"),
				(System.currentTimeMillis() - arg0.getSession()
						.getCreationTime()) / 1000.0);

	}

	private synchronized void writeStory(String path,
			Map<String, Integer> story,
			ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>> loadTime,
			double time) {

		File file = new File(path);

		try (FileWriter writer = new FileWriter(file, true)) {
			writer.write("Session life time :" + time + "s"
					+ System.lineSeparator() + "Session story:"
					+ System.lineSeparator());
			Iterator<Entry<String, Integer>> stryFile = story.entrySet()
					.iterator();

			StringBuilder sb2 = new StringBuilder("|\n|--->> Session story:");
			while (stryFile.hasNext()) {
				Entry<String, Integer> e = stryFile.next();
				sb2.append("\n|--->> " + e);
				double sum = 0;
				for (Integer i : loadTime.get(e.getKey()))
					sum += i;
				writer.write(e + "; mean time of load:" + (sum / e.getValue())
						+ "." + System.lineSeparator());
			}
			Logger.getLogger("LOG").trace(sb2);
			writer.write("Save date " + new Date(System.currentTimeMillis())
					+ System.lineSeparator()
					+ "--------------------------------"
					+ System.lineSeparator());
			writer.flush();
		} catch (IOException e) {
			Logger.getLogger("LOG").error("", e);
		}

	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {

		Logger.getLogger("LOG").trace(
				"|\n|===>> Session attribute added" + "\n|--->  Atribute name:"
						+ event.getName() + "\n|--->  Atribute value:"
						+ event.getValue());
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {

	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {

	}

	@Override
	public void valueBound(HttpSessionBindingEvent event) {

	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {

	}

	@Override
	public void sessionWillPassivate(HttpSessionEvent se) {

	}

	@Override
	public void sessionDidActivate(HttpSessionEvent se) {

	}

}
