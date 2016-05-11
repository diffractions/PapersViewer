package listeners;

import static utility.LogPrinter.println;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

/**
 * Application Lifecycle Listener implementation class DriverCreatorListener
 *
 */
@WebListener
public class DriverCreatorListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public DriverCreatorListener() {
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			Enumeration<Driver> dr = DriverManager.getDrivers();
			if (dr.hasMoreElements() == true)
				while (dr.hasMoreElements()) {
					Driver d = dr.nextElement();
					DriverManager.deregisterDriver(d);
					println("DEREGISTER DRIVER: " + d);
				}
		} catch (Exception e1) {
			println(e1);
		}

		try {
			AbandonedConnectionCleanupThread.shutdown();
		} catch (InterruptedException e) {
			println("SEVERE problem cleaning up: " + e.getMessage(), e);
		}
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Enumeration<Driver> dr = DriverManager.getDrivers();
			if (dr.hasMoreElements() == false)
				throw new NullPointerException("DRIVERS NOT FOUNDS");

			while (dr.hasMoreElements()) {
				println("drivers - " + dr.nextElement());
			}
		} catch (Exception e1) {
			println(e1);
		}
	}

}
