package ecom.control.servlet;

import ecom.model.bean.Cliente;
import ecom.model.bean.Ordine;
import ecom.model.dao.OrdineDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/user/ordini")
public class OrdiniServlet extends HttpServlet {
    private OrdineDAO ordineDAO;

    @Override
    public void init() throws ServletException {
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        this.ordineDAO = new OrdineDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Cliente cliente = (Cliente) request.getSession().getAttribute("utenteLoggato");

        try {
            List<Ordine> ordini = ordineDAO.findByClienteId(cliente.getId());
            
            request.setAttribute("ordini", ordini);
            request.getRequestDispatcher("/WEB-INF/user/ordini.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore caricamento ordini");
        }
    }
}