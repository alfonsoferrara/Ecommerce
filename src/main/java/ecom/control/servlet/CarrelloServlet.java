package ecom.control.servlet;

import ecom.model.bean.Prodotto;
import ecom.model.bean.VoceCarrello;
import ecom.model.dao.ProdottoDAO;
import ecom.model.dao.VoceCarrelloDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/carrello")
public class CarrelloServlet extends HttpServlet {
	private VoceCarrelloDAO voceDAO;
	private ProdottoDAO prodottoDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.voceDAO = new VoceCarrelloDAO(ds);
		this.prodottoDAO = new ProdottoDAO(ds);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// RECUPERO L'ID FORNITO DAL FILTRO
		String cartId = (String) request.getAttribute("cartId");

		try {
			if (cartId != null) {
				List<VoceCarrello> voci = voceDAO.findByCarrello(cartId);

				Map<VoceCarrello, Prodotto> dettagliCarrello = new HashMap<>();
				double subtotale = 0.0;

				for (VoceCarrello voce : voci) {
					Prodotto p = prodottoDAO.findById(voce.getProdottoId());
					if (p != null) {
						dettagliCarrello.put(voce, p);
						subtotale += (p.getPrezzo() * voce.getQuantita());
					}
				}

				// LOGICA SPEDIZIONE
				double sogliaSpedizioneGratuita = 50.00;
				double costoSpedizioneFisso = 5.90;
				double speseSpedizione = 0.0;

				// Se il carrello non è vuoto e non raggiunge la soglia, applico il costo
				if (subtotale > 0 && subtotale < sogliaSpedizioneGratuita) {
					speseSpedizione = costoSpedizioneFisso;
				}

				double totaleFinale = subtotale + speseSpedizione;

				// Salvo tutto nella request per la JSP
				request.setAttribute("carrelloMap", dettagliCarrello);
				request.setAttribute("subtotale", subtotale);
				request.setAttribute("speseSpedizione", speseSpedizione);
				request.setAttribute("totaleFinale", totaleFinale);

			}

			request.getRequestDispatcher("/WEB-INF/views/carrello.jsp").forward(request, response);

		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Errore durante il caricamento del carrello");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// RECUPERO L'ID FORNITO DAL FILTRO
		String cartId = (String) request.getAttribute("cartId");

		// Sicurezza extra: se per qualche motivo il filtro ha fallito sul DB
		if (cartId == null) {
			response.sendRedirect(request.getContextPath() + "/home");
			return;
		}

		String action = request.getParameter("action");

		// Gestione CLEAR (non richiede prodottoId)
		if ("clear".equals(action)) {
			try {
				voceDAO.deleteAllByCarrello(cartId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			response.sendRedirect(request.getContextPath() + "/carrello");
			return;
		}

		String prodottoIdStr = request.getParameter("prodottoId");

		if (prodottoIdStr == null || prodottoIdStr.isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/carrello");
			return;
		}

		try {

			int prodottoId = Integer.parseInt(prodottoIdStr);
			Prodotto prodotto = prodottoDAO.findById(prodottoId);
			int stockProdotto = prodotto.getStock();

			if ("add".equals(action)) {
				int qta = 1;
				String qtaStr = request.getParameter("quantita");
				if (qtaStr != null && !qtaStr.isEmpty()) {
					qta = Integer.parseInt(qtaStr);
				}

				if (qta > stockProdotto) {
					request.setAttribute("erroreAggiunta",
							"Quantità non disponibile in magazzino. Massimo disponibile: " + stockProdotto);
					request.setAttribute("erroreAggiuntaProdottoId", prodottoId);
				} else {
					VoceCarrello voce = new VoceCarrello(cartId, prodottoId, qta);
					voceDAO.insert(voce);
				}
			} else if ("remove".equals(action)) {
				voceDAO.deleteProdotto(cartId, prodottoId);
			}

			response.sendRedirect(request.getContextPath() + "/carrello");

		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore aggiornamento carrello");
		}
	}
}