package listeners;

import java.util.GregorianCalendar;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import static utility.LogPrinter.println;

/**
 * Application Lifecycle Listener implementation class ShowTimerListener
 *
 */
@WebListener
public class ShowTimerListener implements ServletContextListener {

	Thread timer = null;
	volatile boolean start = true;

	/**
	 * Default constructor.
	 */
	public ShowTimerListener() {
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		timer.interrupt();
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		println("SHOW TIMER LISTENER STRAT");
		timer = new Thread(new Runnable() {

			@Override
			public void run() {

				while (start
						&& new GregorianCalendar().get(GregorianCalendar.SECOND) != 0) {
				}

				while (start && true) {
					GregorianCalendar inTime = new GregorianCalendar();
					System.out.println("===========\n>>>"
							+ inTime.get(GregorianCalendar.HOUR) + ":"
							+ inTime.get(GregorianCalendar.MINUTE)
							+ "<<<\n===========");

					synchronized (this) {
						try {
							this.wait(60 * 1000);
							this.notifyAll();
						} catch (InterruptedException e) {
							println("Timer was stopped",e);
						}
					}

				}

				println("TIMER STOP");
			}
		}, "TIMER") {
			@Override
			public void interrupt() {
				super.interrupt();
				start = false;
			}
		};
		timer.setDaemon(true);
		timer.start();
	}

}
