package ecom.control.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// filtro l'intera area di amministrazione
@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        HttpSession session = req.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("utenteLoggato") != null);
        boolean isAdmin = (session != null && "admin".equals(session.getAttribute("ruolo")));

        if (isLoggedIn && isAdmin) {
            // L'utente è un amministratore confermato
            chain.doFilter(request, response);
        } else {
            // Accesso Negato: Restituisce un errore HTTP 403 (Forbidden)
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Area riservata agli amministratori.");
        }
    }
}