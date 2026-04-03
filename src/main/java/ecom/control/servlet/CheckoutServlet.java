package ecom.control.servlet;

import ecom.model.bean.*;
import ecom.model.dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private VoceCarrelloDAO voceDAO;
	private ProdottoDAO prodottoDAO;
	private IndirizzoDAO indirizzoDAO;
	private OrdineDAO ordineDAO;
	private DettagliOrdineDAO dettagliDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.voceDAO = new VoceCarrelloDAO(ds);
		this.prodottoDAO = new ProdottoDAO(ds);
		this.indirizzoDAO = new IndirizzoDAO(ds);
		this.ordineDAO = new OrdineDAO(ds);
		this.dettagliDAO = new DettagliOrdineDAO(ds);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Cliente cliente = (Cliente) request.getSession().getAttribute("utenteLoggato");
		String cartId = (String) request.getAttribute("cartId");

		try {
			// Verifica carrello non vuoto
			List<VoceCarrello> voci = voceDAO.findByCarrello(cartId);
			if (voci == null || voci.isEmpty()) {
				request.getSession().setAttribute("errore", "Il carrello è vuoto.");
				response.sendRedirect(request.getContextPath() + "/carrello");
				return;
			}

			// Carica indirizzi del cliente
			List<Indirizzo> indirizzi = indirizzoDAO.findByClienteId(cliente.getId());
			request.setAttribute("indirizzi", indirizzi);
			
			//Metodi di pagamento possibili
			String[] metodiDiPagamento = {"Carta di credito/Debito", "Contrassegno", "PayPal", "Klarna"};
			request.setAttribute("metodiDiPagamento", metodiDiPagamento);

			request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);

		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Errore nel caricamento del modulo checkout");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Cliente cliente = (Cliente) request.getSession().getAttribute("utenteLoggato");
		String cartId = (String) request.getAttribute("cartId");

		String opzioneIndirizzo = request.getParameter("opzioneIndirizzo");
		String metodoPagamento = request.getParameter("metodoPagamento");
		String notaCliente = request.getParameter("notaCliente");

		try {
			// VALIDAZIONE BASE
			if (cartId == null) {
				response.sendRedirect(request.getContextPath() + "/carrello");
				return;
			}

			List<VoceCarrello> voci = voceDAO.findByCarrello(cartId);
			if (voci.isEmpty()) {
				response.sendRedirect(request.getContextPath() + "/carrello");
				return;
			}

			if (metodoPagamento == null || metodoPagamento.trim().isEmpty()) {
				request.getSession().setAttribute("errore", "Seleziona un metodo di pagamento.");
				response.sendRedirect(request.getContextPath() + "/checkout");
				return;
			}

			// --- GESTIONE INDIRIZZO ---
			int indirizzoId;

			// Se l'utente ha scelto "esistente" o non ci sono opzioni (solo nuovo)
			if ("esistente".equals(opzioneIndirizzo) || (opzioneIndirizzo == null && !voci.isEmpty())) {
				// Caso 1: Usa indirizzo esistente
				String indirizzoIdStr = request.getParameter("indirizzoId");
				if (indirizzoIdStr == null || indirizzoIdStr.trim().isEmpty()) {
					request.getSession().setAttribute("errore", "Seleziona un indirizzo esistente.");
					response.sendRedirect(request.getContextPath() + "/checkout");
					return;
				}
				indirizzoId = Integer.parseInt(indirizzoIdStr);

				// Verifica che l'indirizzo appartenga realmente al cliente
				Indirizzo indirizzo = indirizzoDAO.findById(indirizzoId);
				if (indirizzo == null || indirizzo.getClienteId() != cliente.getId()) {
					request.getSession().setAttribute("errore", "Indirizzo non valido.");
					response.sendRedirect(request.getContextPath() + "/checkout");
					return;
				}

			} else {
				// Caso 2: Crea nuovo indirizzo
				String via = request.getParameter("via");
				String civico = request.getParameter("civico");
				String citta = request.getParameter("citta");
				String provincia = request.getParameter("provincia");
				String cap = request.getParameter("cap");

				// Validazione campi nuovo indirizzo
				String erroreValidazione = validaIndirizzo(via, civico, citta, provincia, cap);
				if (erroreValidazione != null) {
					request.getSession().setAttribute("errore", erroreValidazione);
					response.sendRedirect(request.getContextPath() + "/checkout");
					return;
				}

				// Crea e salva nuovo indirizzo
				Indirizzo nuovoIndirizzo = new Indirizzo(0, cliente.getId(), via.trim(), civico.trim(), citta.trim(),
						provincia.trim().toUpperCase(), cap.trim());
				indirizzoDAO.insert(nuovoIndirizzo);
				indirizzoId = nuovoIndirizzo.getId();
			}

			// --- CREAZIONE ORDINE ---
			// Calcolo totale
			double subtotale = 0.0;
			for (VoceCarrello v : voci) {
				Prodotto p = prodottoDAO.findById(v.getProdottoId());
				if (p != null) {
					subtotale += p.getPrezzo() * v.getQuantita();
				}
			}

			double speseSpedizione = (subtotale > 0 && subtotale < 50.00) ? 5.90 : 0.0;
			double totaleFinale = subtotale + speseSpedizione;

			//Costo aggiuntivo per contrassegno
			if(metodoPagamento.equals("Contrassegno")) {
				totaleFinale += 5.00;
			}
			
			// Crea ordine
			Ordine nuovoOrdine = new Ordine(0, cliente.getId(), indirizzoId, new Timestamp(System.currentTimeMillis()),
					totaleFinale, "IN_ELABORAZIONE", metodoPagamento, notaCliente);
			ordineDAO.insert(nuovoOrdine);

			// Dettagli ordine e aggiornamento stock
			for (VoceCarrello v : voci) {
				Prodotto p = prodottoDAO.findById(v.getProdottoId());
				if (p != null) {
					DettagliOrdine dettaglio = new DettagliOrdine(nuovoOrdine.getId(), p.getId(), v.getQuantita(),
							p.getPrezzo());
					dettagliDAO.insert(dettaglio);

					// Svuota carrello
					voceDAO.deleteProdotto(cartId, p.getId());

					// Aggiorna stock
					int nuovoStock = p.getStock() - v.getQuantita();
					p.setStock(Math.max(nuovoStock, 0));
					prodottoDAO.update(p);
				}
			}

			request.getSession().setAttribute("messaggio",
					"Ordine #" + nuovoOrdine.getId() + " effettuato con successo!");
			response.sendRedirect(request.getContextPath() + "/user/ordini");

		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Errore durante l'elaborazione dell'ordine");
		}
	}

	// Valida i campi di un nuovo indirizzo
	private String validaIndirizzo(String via, String civico, String citta, String provincia, String cap) {
		if (via == null || via.trim().isEmpty()) {
			return "Il campo Via è obbligatorio.";
		}
		if (civico == null || civico.trim().isEmpty()) {
			return "Il campo Civico è obbligatorio.";
		}
		if (citta == null || citta.trim().isEmpty()) {
			return "Il campo Città è obbligatorio.";
		}
		if (provincia == null || provincia.trim().isEmpty()) {
			return "Il campo Provincia è obbligatorio.";
		}
		if (provincia.length() != 2) {
			return "La provincia deve essere composta da 2 lettere (es. RM, MI, NA).";
		}
		if (cap == null || cap.trim().isEmpty()) {
			return "Il campo CAP è obbligatorio.";
		}
		if (!cap.matches("^[0-9]{5}$")) {
			return "Il CAP deve essere composto da 5 cifre numeriche.";
		}
		return null; // tutto ok
	}
}