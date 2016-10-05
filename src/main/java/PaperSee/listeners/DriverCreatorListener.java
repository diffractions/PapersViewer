package listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import static utility.JBDCUtil.deregisterDrivers;
import static utility.JBDCUtil.registerDrivers;

/**
 * Application Lifecycle Listener implementation class DriverCreatorListener
 *
 */
@WebListener
public class DriverCreatorListener implements ServletContextListener {

	public static Logger log = Logger.getLogger("LOG");

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		log.info("DESTROY: Driver Creator Listener");
		deregisterDrivers();
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		log.info("INITIALIZE: Driver Creator Listener");
		registerDrivers();
	}

}
