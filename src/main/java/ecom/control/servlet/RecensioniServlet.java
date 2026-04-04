package ecom.control.servlet;

import ecom.model.bean.Cliente;
import ecom.model.bean.Recensione;
import ecom.model.dao.RecensioneDAO;
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

@WebServlet("/user/recensioni")
public class RecensioniServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private RecensioneDAO recensioneDAO;

    @Override
    public void init() throws ServletException {
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        this.recensioneDAO = new RecensioneDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cliente cliente = (Cliente) session.getAttribute("utenteLoggato");

        if (cliente == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            // Recupera tutte le recensioni dell'utente
            List<Recensione> recensioni = recensioneDAO.findByClienteId(cliente.getId());
            request.setAttribute("recensioni", recensioni);

            request.getRequestDispatcher("/WEB-INF/user/recensioni.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Errore nel caricamento delle recensioni");
        }
    }
}