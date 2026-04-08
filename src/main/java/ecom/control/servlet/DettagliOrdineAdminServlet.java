package ecom.control.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import ecom.model.bean.Cliente;
import ecom.model.bean.DettagliOrdine;
import ecom.model.bean.Indirizzo;
import ecom.model.bean.Ordine;
import ecom.model.bean.Prodotto;
import ecom.model.dao.ClienteDAO;
import ecom.model.dao.DettagliOrdineDAO;
import ecom.model.dao.IndirizzoDAO;
import ecom.model.dao.OrdineDAO;
import ecom.model.dao.ProdottoDAO;

@WebServlet("/admin/dettagliOrdine")
public class DettagliOrdineAdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OrdineDAO ordineDAO;
	private ClienteDAO clienteDAO;
	private IndirizzoDAO indirizzoDAO;
	private DettagliOrdineDAO dettagliDAO;
	private ProdottoDAO prodottoDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.ordineDAO = new OrdineDAO(ds);
		this.dettagliDAO = new DettagliOrdineDAO(ds);
		this.prodottoDAO = new ProdottoDAO(ds);
		this.indirizzoDAO = new IndirizzoDAO(ds);
		this.clienteDAO = new ClienteDAO(ds);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//messaggio di successo per aggiornamento stato ordine nella doPost
		String messaggio = (String) request.getSession().getAttribute("messaggio");
		if (messaggio != null) {
		    request.setAttribute("operazioneRiuscita", messaggio);
		    request.getSession().removeAttribute("messaggio");
		}
		
		
		String ordineId = request.getParameter("id");
		if (ordineId == null || ordineId.isEmpty()) {
			request.getRequestDispatcher("/WEB-INF/admin/ordini.jsp").forward(request, response);
			return;
		}

		try {
			// RECUPERO ORDINE
			int ordine_id = Integer.parseInt(ordineId);
			Ordine ordine = ordineDAO.findById(ordine_id);

			if (ordine == null) {
			    response.sendRedirect(request.getContextPath() + "/admin/ordini");
				return;
			}
			request.setAttribute("ordine", ordine);

			// RECUPERO CLIENTE
			int clienteId = ordine.getClienteId();
			Cliente cliente = clienteDAO.findById(clienteId);
			request.setAttribute("cliente", cliente);

			// RECUPERO INDIRIZZO
			int indirizzoId = ordine.getIndirizzoId();
			Indirizzo indirizzo = indirizzoDAO.findById(indirizzoId);
			request.setAttribute("indirizzo", indirizzo);

			// DETTAGLI DELL'ORDINE
			List<DettagliOrdine> dettagli = dettagliDAO.findByOrdineId(ordine_id);

			// CREO LA MAP COME PER IL CARRELLO (DettagliOrdine, Prodotto)
			Map<DettagliOrdine, Prodotto> dettagliMap = new HashMap<>();
			double subtotale = 0.0;

			for (DettagliOrdine dettaglio : dettagli) {
				Prodotto p = prodottoDAO.findById(dettaglio.getProdottoId());
				if (p != null) {
					dettagliMap.put(dettaglio, p);
					subtotale += dettaglio.getPrezzoUnitario() * dettaglio.getQuantita();
				}
			}

			// Spese di spedizione
			double speseSpedizione = (subtotale > 0 && subtotale < 50.00) ? 5.90 : 0.0;

			// Costo contrassegno
			double costoCContrassegno = "Contrassegno".equals(ordine.getMetodoPagamento()) ? 5.00 : 0.0;

			// Imposta gli attributi per la JSP
			request.setAttribute("dettagli", dettagliMap);
			request.setAttribute("subtotale", subtotale);
			request.setAttribute("speseSpedizione", speseSpedizione);
			request.setAttribute("costoContrassegno", costoCContrassegno);

			request.getRequestDispatcher("/WEB-INF/admin/dettagliOrdine.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
			request.setAttribute("errore", "Si è verificato un problema");
			request.getRequestDispatcher("/WEB-INF/admin/ordini.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// modificare lo stato dell'ordine

		String ordineId = request.getParameter("id");
		if (ordineId == null || ordineId.isEmpty()) {
			request.getRequestDispatcher("/WEB-INF/admin/ordini.jsp").forward(request, response);
			return;
		}

		String statoCambiato = request.getParameter("statoCambiato");
		if (statoCambiato == null || statoCambiato.isEmpty()) {
			request.getRequestDispatcher("/WEB-INF/admin/ordini.jsp").forward(request, response);
			return;
		}

		try {
			// RECUPERO ORDINE
			int ordine_id = Integer.parseInt(ordineId);
			Ordine ordine = ordineDAO.findById(ordine_id);

			if (ordine == null) {
				request.setAttribute("errore", "Non esiste un ordine con tale id");
				request.getRequestDispatcher("/WEB-INF/admin/ordini.jsp").forward(request, response);
				return;
			}

			if (!statoCambiato.equalsIgnoreCase(ordine.getStato())) {
			    ordine.setStato(statoCambiato);
			    ordineDAO.update(ordine);
			    request.getSession().setAttribute("messaggio", "Stato aggiornato correttamente!");
			}
			response.sendRedirect(request.getContextPath() + "/admin/dettagliOrdine?id=" + ordineId);

		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
			request.setAttribute("errore", "Si è verificato un problema");
			request.getRequestDispatcher("/WEB-INF/admin/ordini.jsp").forward(request, response);
		}

	}

}
