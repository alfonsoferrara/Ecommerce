package ecom.control.servlet;

import ecom.model.bean.Utente; 
import ecom.model.bean.Admin;
import ecom.model.bean.Cliente;
import ecom.model.bean.Carrello;
import ecom.model.dao.UtenteDAO;
import ecom.model.dao.AdminDAO;
import ecom.model.dao.ClienteDAO;
import ecom.model.dao.CarrelloDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UtenteDAO utenteDAO;
	private AdminDAO adminDAO;
	private ClienteDAO clienteDAO;
	private CarrelloDAO carrelloDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.utenteDAO = new UtenteDAO(ds);
		this.adminDAO = new AdminDAO(ds);
		this.clienteDAO = new ClienteDAO(ds);
		this.carrelloDAO = new CarrelloDAO(ds);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

					// --- LOGICA DI MERGE DEL CARRELLO OSPITE ---
					associaCarrelloOspite(request, cliente.getId());

					response.sendRedirect(request.getContextPath() + "/home");
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

	// gestire il passaggio da carrello anonimo a carrello utente
	private void associaCarrelloOspite(HttpServletRequest request, int clienteId) throws SQLException {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if ("cart_id".equals(c.getName())) {
					String cartIdOspite = c.getValue();
					Carrello carrelloOspite = carrelloDAO.findById(cartIdOspite);

					// Se esiste un carrello ospite non ancora assegnato a nessuno
					if (carrelloOspite != null && carrelloOspite.getClienteId() == null) {
						carrelloOspite.setClienteId(clienteId);
						carrelloDAO.update(carrelloOspite); // L'ospite ora è diventato il proprietario
					}
					break;
				}
			}
		}
	}
}