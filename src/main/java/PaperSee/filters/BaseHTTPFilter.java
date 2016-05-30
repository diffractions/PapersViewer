package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

abstract public class BaseHTTPFilter implements Filter {

	private FilterConfig config;

	public void setFilterConfig(FilterConfig config) {
		this.config = config;
	}

	public FilterConfig getFilterConfig() {
		return config;
	}

	@Override
	public void destroy() {
		Logger.getLogger("LOG").info("DESTROY: " + this.getClass().getSimpleName() );

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {

		if (arg0 instanceof HttpServletRequest
				&& arg1 instanceof HttpServletResponse) {

			doFilter((HttpServletRequest) arg0, (HttpServletResponse) arg1,
					arg2);

		} else {

			arg2.doFilter(arg0, arg1);

		}
	}

	abstract public void doFilter(HttpServletRequest arg0,
			HttpServletResponse arg1, FilterChain arg2) throws IOException,
			ServletException;

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		Logger.getLogger("LOG").info("INIT: " + this.getClass().getSimpleName() );
		setFilterConfig(arg0);

	}

	/**
	 * 
	 */
	public BaseHTTPFilter() {
		Logger.getLogger("LOG").info("START: " + this.getClass().getSimpleName() );
	}

}
