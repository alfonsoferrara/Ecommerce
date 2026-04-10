package ecom.control.servlet;

import ecom.model.bean.Utente;
import ecom.model.bean.Admin;
import ecom.model.dao.UtenteDAO;
import ecom.model.dao.AdminDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/login")
public class LoginAdminServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UtenteDAO utenteDAO;
	private AdminDAO adminDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.utenteDAO = new UtenteDAO(ds);
		this.adminDAO = new AdminDAO(ds);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		// SE L'UTENTE È GIÀ LOGGATO, REINDIRIZZA ALLA DASHBOARD
		if (session != null && session.getAttribute("utenteLoggato") != null) {
			String ruolo = (String) session.getAttribute("ruolo");
			if ("admin".equals(ruolo)) {
				response.sendRedirect(request.getContextPath() + "/admin/dashboard");
			}
			return;
		}

		request.getRequestDispatcher("/WEB-INF/admin/login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter("email");
		String password = request.getParameter("password");

		// Validazione base
		if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
			request.setAttribute("errore", "Tutti i campi obbligatori devono essere compilati");
			request.getRequestDispatcher("/WEB-INF/admin/login.jsp").forward(request, response);
			return;
		}

		// Controlli di formato base
		if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			request.setAttribute("errore", "Formato email non valido.");
			request.getRequestDispatcher("/WEB-INF/admin/login.jsp").forward(request, response);
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
					session.setAttribute("adminCorrente", admin);
					response.sendRedirect(request.getContextPath() + "/admin/dashboard");
					return;
				} else {
					// Le credenziali sono giuste, ma NON è un admin!
					request.setAttribute("errore", "Accesso negato. Non hai i permessi di amministratore.");
					request.getRequestDispatcher("/WEB-INF/admin/login.jsp").forward(request, response);
					return;
				}

			} else {
				// Credenziali errate
				request.setAttribute("errore", "Email o password non validi.");
				request.getRequestDispatcher("/WEB-INF/admin/login.jsp").forward(request, response);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il login");
		}
	}
}