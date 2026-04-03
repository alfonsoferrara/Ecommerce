package ecom.control.servlet;

import ecom.model.bean.Cliente;
import ecom.model.bean.Indirizzo;
import ecom.model.dao.IndirizzoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/user/indirizzi")
public class IndirizziClienteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private IndirizzoDAO indirizzoDAO;

    @Override
    public void init() throws ServletException {
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        this.indirizzoDAO = new IndirizzoDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cliente cliente = (Cliente) session.getAttribute("utenteLoggato");

        try {
            List<Indirizzo> indirizzi = indirizzoDAO.findByClienteId(cliente.getId());
            request.setAttribute("indirizzi", indirizzi);

            request.getRequestDispatcher("/WEB-INF/user/indirizzi.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Errore nel caricamento degli indirizzi");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cliente cliente = (Cliente) session.getAttribute("utenteLoggato");

        String action = request.getParameter("action");
        String indirizzoIdStr = request.getParameter("indirizzoId");

        try {
            // ELIMINAZIONE (solo se l'indirizzo non è usato in ordini)
            if ("delete".equals(action) && indirizzoIdStr != null) {
                int indirizzoId = Integer.parseInt(indirizzoIdStr);
                
                // Verifica che l'indirizzo appartenga al cliente
                Indirizzo indirizzo = indirizzoDAO.findById(indirizzoId);
                if (indirizzo != null && indirizzo.getClienteId() == cliente.getId()) {
                    
                    // Controlla se l'indirizzo è usato in qualche ordine
                    boolean isUsedInOrders = indirizzoDAO.isUsedInOrders(indirizzoId);
                    if (isUsedInOrders) {
                        session.setAttribute("errore", "Impossibile eliminare l'indirizzo perché è associato a uno o più ordini effettuati.");
                    } else {
                        indirizzoDAO.delete(indirizzoId);
                        session.setAttribute("messaggio", "Indirizzo eliminato con successo.");
                    }
                } else {
                    session.setAttribute("errore", "Indirizzo non valido.");
                }
            }

            response.sendRedirect(request.getContextPath() + "/user/indirizzi");

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("errore", "Errore durante l'operazione sull'indirizzo.");
            response.sendRedirect(request.getContextPath() + "/user/indirizzi");
        }
    }
}