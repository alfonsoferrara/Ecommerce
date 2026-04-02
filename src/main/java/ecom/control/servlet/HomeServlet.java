package ecom.control.servlet;

import ecom.model.bean.Prodotto;
import ecom.model.dao.ProdottoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ProdottoDAO prodottoDAO;

    @Override
    public void init() throws ServletException {
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        this.prodottoDAO = new ProdottoDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // restituisce solo gli ultimi 4 prodotti aggiunti
            List<Prodotto> vetrina = prodottoDAO.findUltimeNovita(1,4);

            // Passo la lista alla JSP
            request.setAttribute("prodottiVetrina", vetrina);
            
            request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
            
        } catch (SQLException e) {
            // pagina di errore generica
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel caricamento della Home");
        }
    }
}
