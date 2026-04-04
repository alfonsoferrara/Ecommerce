package ecom.control.servlet;

import ecom.model.bean.Utente;
import ecom.model.bean.Admin;
import ecom.model.bean.Cliente;
import ecom.model.bean.Carrello;
import ecom.model.bean.VoceCarrello;
import ecom.model.dao.UtenteDAO;
import ecom.model.dao.AdminDAO;
import ecom.model.dao.ClienteDAO;
import ecom.model.dao.CarrelloDAO;
import ecom.model.dao.VoceCarrelloDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UtenteDAO utenteDAO;
	private AdminDAO adminDAO;
	private ClienteDAO clienteDAO;
	private CarrelloDAO carrelloDAO;
	private VoceCarrelloDAO voceCarrelloDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.utenteDAO = new UtenteDAO(ds);
		this.adminDAO = new AdminDAO(ds);
		this.clienteDAO = new ClienteDAO(ds);
		this.carrelloDAO = new CarrelloDAO(ds);
		this.voceCarrelloDAO = new VoceCarrelloDAO(ds);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		// SE L'UTENTE È GIÀ LOGGATO, REINDIRIZZA AL PROFILO
		if (session != null && session.getAttribute("utenteLoggato") != null) {
			String ruolo = (String) session.getAttribute("ruolo");
			if ("admin".equals(ruolo)) {
				response.sendRedirect(request.getContextPath() + "/admin/dashboard");
			} else if ("cliente".equals(ruolo)) {
				response.sendRedirect(request.getContextPath() + "/user/profilo");
			} else {
				response.sendRedirect(request.getContextPath() + "/home");
			}
			return;
		}
		request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		String password = request.getParameter("password");

		// Validazione base
		if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
			request.setAttribute("errore", "Tutti i campi obbligatori devono essere compilati");
			request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
			return;
		}

		// Controlli di formato base
		if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			request.setAttribute("errore", "Formato email non valido.");
			request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
			return;
		}

		try {
			// Verifico le credenziali base
			Utente utente = utenteDAO.doLogin(email, password);

			if (utente != null) {
				HttpSession session = request.getSession();

				// Recupero l'ID del carrello ospite (prima del login)
				String cartIdOspite = (String) request.getAttribute("cartId");

				// Controllo se è admin
				Admin admin = adminDAO.findById(utente.getId());
				if (admin != null) {
					session.setAttribute("utenteLoggato", admin);
					session.setAttribute("ruolo", "admin");
					response.sendRedirect(request.getContextPath() + "/admin/dashboard");
					return;
				}

				// Se non è Admin, è un Cliente
				Cliente cliente = clienteDAO.findById(utente.getId());
				if (cliente != null) {
					session.setAttribute("utenteLoggato", cliente);
					session.setAttribute("ruolo", "cliente");

					// --- LOGICA DI MERGE DEL CARRELLO ---
					mergeCarrelli(request, response, cliente.getId(), cartIdOspite);

					// REDIRECT ALLA PAGINA ORIGINALE
					String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
					if (redirectUrl != null && !redirectUrl.isEmpty()) {
						// Rimuovi l'attributo dalla sessione
						session.removeAttribute("redirectAfterLogin");
						response.sendRedirect(redirectUrl);
					} else {
						// Nessuna pagina salvata, vai alla home
						response.sendRedirect(request.getContextPath() + "/home");
					}
					return;
				}
			} else {
				// Credenziali errate
				request.setAttribute("errore", "Email o password non validi.");
				request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il login");
		}
	}

	/**
	 * Gestisce il merge tra il carrello ospite (anonimo) e il carrello esistente
	 * dell'utente
	 */
	private void mergeCarrelli(HttpServletRequest request, HttpServletResponse response, int clienteId,
			String cartIdOspite) throws SQLException {

		if (cartIdOspite == null)
			return;

		Carrello carrelloOspite = carrelloDAO.findById(cartIdOspite);
		if (carrelloOspite == null || carrelloOspite.getClienteId() != null)
			return;

		Carrello carrelloUtente = carrelloDAO.findByCliente(clienteId);

		if (carrelloUtente == null) {
			// Assegna carrello ospite all'utente
			carrelloOspite.setClienteId(clienteId);
			carrelloDAO.update(carrelloOspite);
		} else {
			// Merge: sposta i prodotti dal carrello ospite a quello utente
			List<VoceCarrello> vociOspite = voceCarrelloDAO.findByCarrello(cartIdOspite);

			for (VoceCarrello voceOspite : vociOspite) {
				// Il metodo insert con ON DUPLICATE KEY UPDATE gestisce sia insert che update
				VoceCarrello voce = new VoceCarrello(carrelloUtente.getId(), voceOspite.getProdottoId(),
						voceOspite.getQuantita());
				voceCarrelloDAO.insert(voce);
			}

			// Svuota e cancella il carrello ospite
			voceCarrelloDAO.deleteAllByCarrello(cartIdOspite);
			carrelloDAO.delete(cartIdOspite);
		}

		// Cancella il cookie
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if ("cart_id".equals(c.getName())) {
					c.setMaxAge(0);
					c.setPath(request.getContextPath() + "/");
					response.addCookie(c);
					break;
				}
			}
		}
	}
}