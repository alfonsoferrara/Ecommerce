package ecom.control.servlet;

import ecom.model.bean.Cliente;
import ecom.model.bean.Ordine;
import ecom.model.bean.Prodotto;
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
        String pagina = request.getParameter("pagina");
        
		List<Ordine> ordini = null;
		int totaleOrdini = 0;
		int numPagina = 0;
		
        try {
			if (pagina != null && !pagina.isEmpty()) {
				// se viene specificata una pagina allora la uso
				//max. 12 pagine per volta
				numPagina = Integer.parseInt(pagina);
				ordini = ordineDAO.findByClienteId(cliente.getId(), numPagina, 12);
			} else {
				// altrimenti recupero dalla prima pagina
				ordini = ordineDAO.findByClienteId(cliente.getId(), 1, 12);
			}

			totaleOrdini = ordineDAO.countByClienteId(cliente.getId());
			int totalePagine = (int) Math.ceil((double) totaleOrdini / 12);

			request.setAttribute("totalePagine", totalePagine);
			request.setAttribute("paginaCorrente", numPagina);
            request.setAttribute("ordini", ordini);
            request.setAttribute("totaleOrdini", totaleOrdini);
            request.getRequestDispatcher("/WEB-INF/user/ordini.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore caricamento ordini");
        }
    }
}