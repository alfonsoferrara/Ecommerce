package ecom.control.servlet;

import ecom.model.bean.*;
import ecom.model.dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/riepilogo")
public class OrdineRiepilogoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private OrdineDAO ordineDAO;
	private DettagliOrdineDAO dettagliDAO;
	private ProdottoDAO prodottoDAO;
	private IndirizzoDAO indirizzoDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.ordineDAO = new OrdineDAO(ds);
		this.dettagliDAO = new DettagliOrdineDAO(ds);
		this.prodottoDAO = new ProdottoDAO(ds);
		this.indirizzoDAO = new IndirizzoDAO(ds);
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

		String ordineIdStr = request.getParameter("id");
		if (ordineIdStr == null || ordineIdStr.isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/user/ordini");
			return;
		}

		try {
			int ordineId = Integer.parseInt(ordineIdStr);

			// Recupera l'ordine
			Ordine ordine = ordineDAO.findById(ordineId);

			// Verifica che l'ordine appartenga al cliente loggato
			if (ordine == null || ordine.getClienteId() != cliente.getId()) {
				response.sendRedirect(request.getContextPath() + "/user/ordini");
				return;
			}
			
			// Recupera i dettagli dell'ordine
			List<DettagliOrdine> dettagli = dettagliDAO.findByOrdineId(ordineId);

			// CREO LA MAP COME PER IL CARRELLO (DettagliOrdine, Prodotto)
			Map<DettagliOrdine, Prodotto> dettagliMap = new HashMap<>();
			double subtotale = 0.0;

			for (DettagliOrdine dettaglio : dettagli) {
				Prodotto p = prodottoDAO.findById_Admin(dettaglio.getProdottoId());
				if (p != null) {
					dettagliMap.put(dettaglio, p);
					subtotale += dettaglio.getPrezzoUnitario() * dettaglio.getQuantita();
				}
			}

			// Recupera l'indirizzo di spedizione
			Indirizzo indirizzo = indirizzoDAO.findById(ordine.getIndirizzoId());

			// Spese di spedizione
			double speseSpedizione = (subtotale > 0 && subtotale < 50.00) ? 5.90 : 0.0;

			// Costo contrassegno
			double costoCContrassegno = "Contrassegno".equals(ordine.getMetodoPagamento()) ? 5.00 : 0.0;

			// Imposta gli attributi per la JSP
			request.setAttribute("ordine", ordine);
			request.setAttribute("dettagli", dettagliMap);
			request.setAttribute("indirizzo", indirizzo);
			request.setAttribute("subtotale", subtotale);
			request.setAttribute("speseSpedizione", speseSpedizione);
			request.setAttribute("costoContrassegno", costoCContrassegno);

			request.getRequestDispatcher("/WEB-INF/views/ordineRiepilogo.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Errore nel caricamento del riepilogo ordine");
		}
	}
}