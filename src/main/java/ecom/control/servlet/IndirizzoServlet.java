package ecom.control.servlet;

import ecom.model.bean.Cliente;
import ecom.model.bean.Indirizzo;
import ecom.model.dao.IndirizzoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

// Mappata sotto /user/ così è automaticamente protetta da UtenteFilter
@WebServlet("/user/indirizzo")
public class IndirizzoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IndirizzoDAO indirizzoDAO;

    @Override
    public void init() throws ServletException {
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        this.indirizzoDAO = new IndirizzoDAO(ds);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Cliente cliente = (Cliente) request.getSession().getAttribute("utenteLoggato");
        
        // Recupero i dati dal form
        String via = request.getParameter("via");
        String civico = request.getParameter("civico");
        String citta = request.getParameter("citta");
        String cap = request.getParameter("cap");
        String provincia = request.getParameter("provincia");
        
        // validazione
        if(via != null && citta != null && cap != null && provincia != null) {
            Indirizzo nuovoIndirizzo = new Indirizzo(0, cliente.getId(), via, civico, citta, provincia, cap);
            try {
                // Inserisce nel DB
                indirizzoDAO.insert(nuovoIndirizzo);
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        // rimanda l'utente esattamente alla pagina da cui proveniva
        String referer = request.getHeader("Referer");
        if(referer != null && referer.contains("checkout")) {
            response.sendRedirect(request.getContextPath() + "/checkout");
        } else {
            response.sendRedirect(request.getContextPath() + "/user/indirizzi");
        }
    }
}