package ecom.control.servlet;

import ecom.model.bean.Cliente;
import ecom.model.bean.Indirizzo;
import ecom.model.dao.ClienteDAO;
import ecom.model.dao.IndirizzoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/user/profilo")
public class ProfiloServlet extends HttpServlet {
    private IndirizzoDAO indirizzoDAO;
    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        this.indirizzoDAO = new IndirizzoDAO(ds);
        this.clienteDAO = new ClienteDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Il filtro garantisce che esista
        Cliente cliente = (Cliente) request.getSession().getAttribute("utenteLoggato");

        try {
            // Carichiamo tutti gli indirizzi associati a questo cliente
            List<Indirizzo> indirizzi = indirizzoDAO.findByClienteId(cliente.getId());
            request.setAttribute("indirizzi", indirizzi);
            
            request.getRequestDispatcher("/WEB-INF/user/profilo.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore caricamento profilo");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Cliente cliente = (Cliente) request.getSession().getAttribute("utenteLoggato");
        
        // Modifica anagrafica
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String telefono = request.getParameter("telefono");

        if (nome != null && !nome.trim().isEmpty() && cognome != null && !cognome.trim().isEmpty()) {
            cliente.setNome(nome);
            cliente.setCognome(cognome);
            cliente.setTelefono(telefono);
            
            try {
                clienteDAO.update(cliente);
                // Aggiorno il bean in sessione per riflettere le modifiche ovunque
                request.getSession().setAttribute("utenteLoggato", cliente);
                request.getSession().setAttribute("messaggio", "Profilo aggiornato con successo!");
            } catch (SQLException e) {
                e.printStackTrace();
                request.getSession().setAttribute("errore", "Errore durante l'aggiornamento.");
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/user/profilo");
    }
}