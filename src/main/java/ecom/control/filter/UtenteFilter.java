package ecom.control.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// filtro per il checkout e tutta la cartella /user/*
@WebFilter(urlPatterns = { "/checkout", "/user/*" })
public class UtenteFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		HttpSession session = req.getSession(false);

		// Controllo se esiste una sessione e se c'è un utente loggato con ruolo
		// 'cliente'
		boolean isLoggedIn = (session != null && session.getAttribute("utenteLoggato") != null);
		boolean isCliente = (session != null && "cliente".equals(session.getAttribute("ruolo")));

		if (isLoggedIn && isCliente) {
			chain.doFilter(request, response);
		} else {
			// Salva l'URL originale che l'utente stava cercando di raggiungere
			String originalUrl = req.getRequestURI();
			String queryString = req.getQueryString();
			if (queryString != null && !queryString.isEmpty()) {
				originalUrl += "?" + queryString;
			}

			// Salva in sessione (non in request perché verrà persa nel redirect)
			req.getSession().setAttribute("redirectAfterLogin", originalUrl);
			// Accesso Negato, reindirizzo al login
			res.sendRedirect(req.getContextPath() + "/login");
		}
	}
}