package ecom.control.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import ecom.model.bean.Cliente;
import ecom.model.bean.Indirizzo;
import ecom.model.bean.Ordine;
import ecom.model.dao.ClienteDAO;
import ecom.model.dao.IndirizzoDAO;
import ecom.model.dao.OrdineDAO;

@WebServlet("/admin/cliente")
public class AdminDettagliCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ClienteDAO clienteDAO;
	private OrdineDAO ordineDAO;
	private IndirizzoDAO indirizzoDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.clienteDAO = new ClienteDAO(ds);
		this.ordineDAO = new OrdineDAO(ds);
		this.indirizzoDAO = new IndirizzoDAO(ds);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String messaggio = (String) request.getSession().getAttribute("messaggio");
		if (messaggio != null) {
			request.setAttribute("operazioneRiuscita", messaggio);
			request.getSession().removeAttribute("messaggio"); // Pulizia della sessione
		}

		String erroreSessione = (String) request.getSession().getAttribute("errore");
		if (erroreSessione != null) {
			request.setAttribute("errore", erroreSessione);
			request.getSession().removeAttribute("errore");
		}

		String clienteId = request.getParameter("id");

		try {
			// Se è un nuovo cliente
			if (clienteId == null || clienteId.isEmpty() || clienteId.equals("new")) {
				Cliente clienteVuoto = new Cliente();
				clienteVuoto.setId(0); // ID 0 fa capire alla JSP che è un nuovo inserimento

				request.setAttribute("cliente", clienteVuoto);
				request.setAttribute("ordini", new ArrayList<>()); // non ha ordini
				request.setAttribute("indirizzi", new ArrayList<>()); // non ha indirizzi

			}
			// Se è una modifica ad un cliente esistente
			else {
				int cliente_id = Integer.parseInt(clienteId);
				Cliente cliente = clienteDAO.findById(cliente_id);

				if (cliente == null) {
					request.getSession().setAttribute("errore", "Nessun cliente trovato");
					response.sendRedirect(request.getContextPath() + "/admin/clienti");
					return;
				}

				List<Ordine> ordini = ordineDAO.findByClienteId_noPaginazione(cliente_id);
				List<Indirizzo> indirizzi = indirizzoDAO.findByClienteId(cliente_id);

				request.setAttribute("cliente", cliente);
				request.setAttribute("ordini", ordini);
				request.setAttribute("indirizzi", indirizzi);
			}

			request.getRequestDispatcher("/WEB-INF/admin/dettagliCliente.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			request.setAttribute("errore", "Errore nel caricamento del cliente");
			request.getRequestDispatcher("/admin/clienti").forward(request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String clienteId = request.getParameter("id");
		String azione = request.getParameter("azione");

		if (clienteId == null || clienteId.isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/admin/clienti");
			return;
		}

		try {
			int cliente_id = Integer.parseInt(clienteId);

			// richiesta di eliminazione
			if ("elimina".equalsIgnoreCase(azione)) {
				System.out.println("dentro blocco elimina, parametro corretto");
				try {
					clienteDAO.delete(cliente_id);
				} catch (SQLException e) {
					e.printStackTrace();
					String messaggio = "Il cliente non può essere eliminato poiché ha effettuato degli ordini";
					request.getSession().setAttribute("errore", "Errore durante l'eliminazione: " + messaggio);
					response.sendRedirect(request.getContextPath() + "/admin/cliente?id=" + cliente_id);
					return;
				}

				request.getSession().setAttribute("messaggio", "Cliente eliminato con successo!");
				response.sendRedirect(request.getContextPath() + "/admin/clienti");
				return; // Interrompo l'esecuzione per evitare di fare anche l'aggiornamento
			}
			// procedi con l'aggiornamento completo
			gestisciAggiornamentoCompleto(request, response, cliente_id);

		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
			request.getSession().setAttribute("errore", "Errore durante il salvataggio: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/admin/cliente?id=" + clienteId);
		}
	}

	private void gestisciAggiornamentoCompleto(HttpServletRequest request, HttpServletResponse response, int cliente_id)
			throws ServletException, IOException, SQLException {

		Cliente cliente;
		boolean isNuovoCliente = (cliente_id == 0);

		if (isNuovoCliente) {
			// CREAZIONE
			cliente = new Cliente();
			aggiornaDatiBaseCliente(request, cliente);

			// Il DAO fa la INSERT, esegue l'hash della password (se presente) e restituisce
			// l'ID
			try {
				cliente_id = clienteDAO.insertAndReturnId(cliente);
			} catch (SQLException e) {
				String errorMessage = e.getMessage();
				if (errorMessage != null && errorMessage.contains("mail")) {
					request.setAttribute("errore", "Indirizzo email non disponibile, sceglierne un altro.");
					request.setAttribute("cliente", cliente); // Ripopola il form
					request.getRequestDispatcher("/WEB-INF/admin/dettagliCliente.jsp").forward(request, response);
				}
				return;
			}

		} else {
			// MODIFICA
			cliente = clienteDAO.findById(cliente_id);
			if (cliente == null) {
				request.getSession().setAttribute("errore", "Cliente non trovato");
				response.sendRedirect(request.getContextPath() + "/admin/clienti");
				return;
			}

			aggiornaDatiBaseCliente(request, cliente);

			try {
				// Aggiorna i dati anagrafici e l'email
				clienteDAO.update(cliente);

				// GESTIONE CAMBIO PASSWORD
				String nuovaPassword = request.getParameter("password");
				if (nuovaPassword != null && !nuovaPassword.trim().isEmpty()) {
					clienteDAO.changePassword(cliente_id, nuovaPassword);
				}

			} catch (SQLException e) {
				String errorMessage = e.getMessage();
				if (errorMessage != null && errorMessage.contains("mail")) {
					request.getSession().setAttribute("errore",
							"Indirizzo email non disponibile, sceglierne un altro.");
					response.sendRedirect(request.getContextPath() + "/admin/cliente?id=" + cliente_id);
				}
				return;
			}
		}

		request.getSession().setAttribute("messaggio",
				isNuovoCliente ? "Cliente creato con successo!" : "Cliente aggiornato con successo!");
		response.sendRedirect(request.getContextPath() + "/admin/cliente?id=" + cliente_id);
	}

	private void aggiornaDatiBaseCliente(HttpServletRequest request, Cliente cliente) {
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String telefono = request.getParameter("telefono");

		if (nome != null && !nome.trim().isEmpty()) {
			cliente.setNome(nome);
		}
		if (cognome != null && !cognome.trim().isEmpty()) {
			cliente.setCognome(cognome);
		}
		if (email != null && !email.trim().isEmpty()) {
			cliente.setEmail(email);
		}
		if (password != null && !password.trim().isEmpty()) {
			cliente.setPassword(password);
		}
		if (telefono != null && !telefono.trim().isEmpty()) {
			cliente.setTelefono(telefono);
		}
	}

}
