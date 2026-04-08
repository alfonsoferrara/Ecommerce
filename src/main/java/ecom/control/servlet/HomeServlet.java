package ecom.control.servlet;

import ecom.model.bean.Immagine;
import ecom.model.bean.Prodotto;
import ecom.model.dao.ImmagineDAO;
import ecom.model.dao.ProdottoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "HomeServlet", urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ProdottoDAO prodottoDAO;
	private ImmagineDAO immagineDAO;

    @Override
    public void init() throws ServletException {
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        this.prodottoDAO = new ProdottoDAO(ds);
        this.immagineDAO = new ImmagineDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            // restituisce solo gli ultimi 4 prodotti aggiunti
            List<Prodotto> vetrina = prodottoDAO.findRecenti(1,4);

            // Passo la lista alla JSP
            request.setAttribute("prodottiVetrina", vetrina);
            
         // RECUPERA LE IMMAGINI PRINCIPALI PER OGNI PRODOTTO
			Map<Integer, String> immaginiPrincipali = new HashMap<>();
			for (Prodotto prodotto : vetrina) {
				Immagine imgPrincipale = immagineDAO.findPrincipalByProdottoId(prodotto.getId());
				if (imgPrincipale != null) {
					immaginiPrincipali.put(prodotto.getId(), imgPrincipale.getUrl());
				} else {
					// Immagine di default se non esiste
					immaginiPrincipali.put(prodotto.getId(), "/images/default.jpg");
				}
			}

			request.setAttribute("immaginiPrincipali", immaginiPrincipali);
            
            request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
            
        } catch (SQLException e) {
            // pagina di errore generica
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel caricamento della Home");
        }
    }
}
