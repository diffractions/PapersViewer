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
//import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

import static utility.LogPrinter.println;

/**
 * Application Lifecycle Listener implementation class filterStartListener
 *
 */
@WebListener
public class SessionWriteFileAndChangeListener implements
		HttpSessionIdListener, HttpSessionListener,
		HttpSessionAttributeListener {

	@Override
	public void sessionIdChanged(HttpSessionEvent arg0, String arg1) {

		// System.out.println("========== SESSION ID WAS CHANGED ==========");
		// System.out.println("--------SESSION INFO--------");
		// System.out.println("|--->  New session id : "
		// + arg0.getSession().getId());
		// System.out.println("|--->  Session max inactive time : "
		// + arg0.getSession().getMaxInactiveInterval());

	}

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {

		// System.out.println("========== SESSION WAS CREATED ==========");
		// System.out.println("|===>> SESSION INFO");
		// HttpSession session = arg0.getSession();
		// System.out.println("|--->  Session id : " + session.getId());
		// System.out.println("|--->  Session from : "
		// + new Date(session.getCreationTime()));
		// System.out.println("|--->  Session max inactive time : "
		// + session.getMaxInactiveInterval());

	}

	@SuppressWarnings("unchecked")
	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {

		// System.out.println("========== SESSION WAS DESTROYED ==========");
		// System.out.println("|===>> SESSION INFO");
		// System.out.println("|--->  Session was created by : "
		// + arg0.getSession().getAttribute("first Request URI"));
		//
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
			// System.out.println("|--->> Session story:");
			while (stryFile.hasNext()) {
				Entry<String, Integer> e = stryFile.next();
				// System.out.println("|--->> " + e);
				double sum = 0;
				for (Integer i : loadTime.get(e.getKey()))
					sum += i;
				writer.write(e + "; mean time of load:" + (sum / e.getValue())
						+ "." + System.lineSeparator());
			}
			writer.write("Save date " + new Date(System.currentTimeMillis())
					+ System.lineSeparator()
					+ "--------------------------------"
					+ System.lineSeparator());
			writer.flush();
		} catch (IOException e) {
			println(e);
		}

	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {

		// System.out.println("|===>> Session attribute added");
		// System.out.println("|--->  Atribute name:" + event.getName());
		// System.out.println("|--->  Atribute value:" + event.getValue());
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {

	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {

	}

}
