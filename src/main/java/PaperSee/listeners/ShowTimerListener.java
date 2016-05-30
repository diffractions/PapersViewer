package listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import static utility.LogUtils.timerInitialize;
import static utility.LogUtils.timerDestroy;

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
		timerDestroy();
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		Logger.getLogger("LOG").info("INITIALIZE: Show Timer Listener");
		timerInitialize();
	}

}
