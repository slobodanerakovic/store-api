/**
@Author Slobodan Erakovic
 */
package api.store.ws;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

//@Configuration
//@WebFilter
public class AuthenticationFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		/**
		 * Must be enabled in order to resolve CORS restrictions
		 */
		activateCORSSupport(httpResponse);
		chain.doFilter(request, response);
	}

	private void activateCORSSupport(HttpServletResponse httpResponse) {
		httpResponse.addHeader("Access-Control-Allow-Origin", "*");
		httpResponse.addHeader("Access-Control-Allow-Methods", "GET");
		httpResponse.addHeader("Content-Type", "application/json;charset=UTF-8");
		httpResponse.addHeader("Access-Control-Allow-Headers", "Content-Type");
	}

	@Override
	public void destroy() {
		// do nothing
	}

}
