package ecom.control.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String requestURI = req.getRequestURI();
        String loginURI = req.getContextPath() + "/admin/login";

        // CONTROLLO SESSIONE per tutte le altre pagine (es. /admin/dashboard)
        HttpSession session = req.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("utenteLoggato") != null);
        boolean isAdmin = (session != null && "admin".equals(session.getAttribute("ruolo")));
        boolean isCliente = (session != null && "cliente".equals(session.getAttribute("ruolo")));

        
        //verifico se è un cliente
        if(isLoggedIn && isCliente) {
        	res.sendError(HttpServletResponse.SC_FORBIDDEN, "Pagina riseervata agli amministratori!");
        	return;
        }
        
        // Se la richiesta è per la pagina di login e non arriva da un cliente, lascio passare
        if (requestURI.equals(loginURI)) {
            chain.doFilter(request, response);
            return;
        }
        
        if (isLoggedIn && isAdmin) {
            // L'utente è un amministratore confermato
            chain.doFilter(request, response);
        } else {            
            // Accesso Negato, reindirizzo al login
            res.sendRedirect(loginURI);
        }
    }
}