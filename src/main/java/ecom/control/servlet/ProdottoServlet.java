package ecom.control.servlet;

import ecom.model.bean.*;
import ecom.model.dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/prodotto")
public class ProdottoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ProdottoDAO prodottoDAO;
	private ImmagineDAO immagineDAO;
	private CaratteristicheProdottoDAO caratteristicheDAO;
	private RecensioneDAO recensioneDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.prodottoDAO = new ProdottoDAO(ds);
		this.immagineDAO = new ImmagineDAO(ds);
		this.caratteristicheDAO = new CaratteristicheProdottoDAO(ds);
		this.recensioneDAO = new RecensioneDAO(ds);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String idParam = request.getParameter("id");

		if (idParam == null || idParam.isEmpty()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Prodotto non trovato");
			return;
		}

		try {
			int idProdotto = Integer.parseInt(idParam);

			// Recupero dati
			Prodotto prodotto = prodottoDAO.findById(idProdotto);
			if (prodotto == null) {
				// Prodotto inesistente o cancellato (Soft Delete)
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Prodotto non trovato");
				return;
			}

			// Recupero Dati Relazionati
			List<Immagine> immagini = immagineDAO.findByProdottoId(idProdotto);
			Map<String, String> specifiche = caratteristicheDAO.getCaratteristicheMapByProdottoId(idProdotto);
			List<Recensione> recensioni = recensioneDAO.findByProdotto(idProdotto);

			// Salvo tutto nella request
			request.setAttribute("prodotto", prodotto);
			request.setAttribute("immagini", immagini);
			request.setAttribute("specifiche", specifiche);
			request.setAttribute("recensioni", recensioni);
			
			// Inoltro alla view
			request.getRequestDispatcher("WEB-INF/views/prodotto.jsp").forward(request, response);
			
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore DB");
		} catch (NumberFormatException | NullPointerException e) {
			// Se l'utente scrive /prodotto?id=testo
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Prodotto non trovato");
		}
	}
}