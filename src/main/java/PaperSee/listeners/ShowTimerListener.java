package listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

/**
 * Application Lifecycle Listener implementation class ShowTimerListener
 *
 */
@WebListener
public class ShowTimerListener implements ServletContextListener {

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {

		Logger.getLogger("LOG").info("DESTROY: Show Timer Listener");
		Logger.getLogger("TIMER").info("TIMER.STOP");

	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		Logger.getLogger("LOG").info("INITIALIZE: Show Timer Listener");
		Logger.getLogger("TIMER").info("TIMER.START");

	}

}
