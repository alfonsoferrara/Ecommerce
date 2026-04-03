package ecom.control.filter;

import ecom.model.bean.Carrello;
import ecom.model.dao.CarrelloDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@WebFilter("/*")
public class GuestCartFilter implements Filter {

    private CarrelloDAO carrelloDAO;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Recupero il DataSource dal ServletContext (impostato nel MainContextListener)
        DataSource ds = (DataSource) filterConfig.getServletContext().getAttribute("DataSource");
        this.carrelloDAO = new CarrelloDAO(ds);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Escludo le risorse statiche per non appesantire il DB
        String uri = req.getRequestURI();
        if (uri.matches(".*(styles|images|scripts)$")) {
            chain.doFilter(request, response);
            return;
        }

        String cartId = null;
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("cart_id".equals(c.getName())) {
                    cartId = c.getValue();
                    break;
                }
            }
        }

        // Se l'utente non ha il cookie del carrello, lo creo
        if (cartId == null) {
            cartId = UUID.randomUUID().toString();
            
            try {
                // Inserisco un carrello "Ospite" (cliente_id = null) nel Database
                Carrello nuovoCarrello = new Carrello(cartId, null, null);
                carrelloDAO.insert(nuovoCarrello);

                // Creo il Cookie da inviare al browser
                Cookie cartCookie = new Cookie("cart_id", cartId);
                cartCookie.setPath(req.getContextPath() + "/"); // Valido su tutto il sito
                cartCookie.setMaxAge(60 * 60 * 24 * 30); // Scade in 30 giorni
                res.addCookie(cartCookie);

            } catch (SQLException e) {
                e.printStackTrace();
                // Anche in caso di errore DB, lascio passare la richiesta, 
                // il carrello semplicemente non funzionerà per questo click
            }
        }
        
        // Salvo l'ID direttamente nella request. Così le Servlet non dovranno
        // più scansionare i cookie, ma troveranno il dato già pronto
        req.setAttribute("cartId", cartId);
        chain.doFilter(request, response);
    }
}