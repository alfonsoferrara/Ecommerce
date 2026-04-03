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
    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        this.clienteDAO = new ClienteDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Il filtro garantisce che esista
        Cliente cliente = (Cliente) request.getSession().getAttribute("utenteLoggato");

        try {
        	//passo solo il "profilo"
        	request.setAttribute("cliente", cliente);
            request.getRequestDispatcher("/WEB-INF/user/profilo.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore caricamento profilo");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        doGet(request, response);
    }
}