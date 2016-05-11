package controllers.paper;

import java.lang.reflect.Field;
import java.util.List;

import inject.FieldReflector;
import inject.Inject;
import inject.exeptions.InjectInitialException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static utility.LogPrinter.*;

/**
 * Servlet implementation class InjectAnnotationsPaperController
 */
public class DependencyInjectionServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String APP_CTX_PATH = "project_context";
	private static ApplicationContext context;
	private String status = "OK";

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	@Override
	public void init() throws ServletException {

		String path = getServletContext().getInitParameter(APP_CTX_PATH);
		if (path == null) {
			throw new ServletException(APP_CTX_PATH + "init param==null");
		}
		// println(">>>  APP_CTX_PATH : " + APP_CTX_PATH);

		try {
			if (context == null)
				context = new ClassPathXmlApplicationContext(path);

			List<Field> allFields = FieldReflector.collectUpTo(this.getClass(),
					DependencyInjectionServlet.class);
			List<Field> markedFields = (List<Field>) FieldReflector
					.filterInject(allFields);

			for (Field field : markedFields) {
				field.setAccessible(true);
				String beanName = null;
				Object bean = null;

				try {
					beanName = field.getAnnotation(Inject.class).value();
					bean = context.getBean(beanName);

					if (!(field.getType().isAssignableFrom(bean.getClass()))) {
						throw new InjectInitialException(
								"In Spring been configuration file: \"" + path
										+ "\" not found bean with name: \" "
										+ beanName + "\" , and type: \" "
										+ field.getGenericType() + "\".");
					}

				} catch (NullPointerException | BeansException e) {
					throw new InjectInitialException(
							"Cannot assign a value for a bean, since: "
									+ e.getLocalizedMessage() + "!", e);
				}

				field.set(this, bean);
			}

		} catch (BeansException | SecurityException | IllegalArgumentException
				| IllegalAccessException | InjectInitialException e) {
			status = "EXCEPTION";
			println(e);
		}

		println(status + "." + this.getClass().getSimpleName());

	}

	@Override
	public void destroy() {
		println("DESTROY: " + this.getClass().getSimpleName());
		super.destroy();
	}
}
