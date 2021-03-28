package mesCommandes;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class MonFiltre implements Filter {
	
	private FilterConfig filterConfig = null;
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,ServletException {
		String nom = null;
		HttpServletRequest hrequest = (HttpServletRequest) request;
		HttpServletResponse hresponse = (HttpServletResponse) response;
		Cookie[] cookies = hrequest.getCookies();
		// test s'il existe un cookie dont l'attribut est "nom"
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals("nom")) {
				nom = cookies[i].getValue();
				break;
			}
		}
		if (nom == null) {
			// appel inscrire
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.sendRedirect("inscrire");
		} else {
			chain.doFilter(request, response);
		}
	}
	public void destroy() {
		this.filterConfig = null;
	}
}
