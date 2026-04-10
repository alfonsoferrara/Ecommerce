package ecom.control.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import ecom.model.bean.Admin;
import ecom.model.dao.AdminDAO;

@WebServlet("/admin/membroteam")
public class AdminDettagliTeam extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AdminDAO adminDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.adminDAO = new AdminDAO(ds);
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

		String adminId = request.getParameter("id");

		// Ottieni l'admin corrente dalla sessione
		Admin adminCorrente = (Admin) request.getSession().getAttribute("adminCorrente");

		try {
			// Se è un nuovo cliente
			if (adminId == null || adminId.isEmpty() || adminId.equals("new")) {
				Admin adminVuoto = new Admin();
				adminVuoto.setId(0); // ID 0 fa capire alla JSP che è un nuovo inserimento

				request.setAttribute("admin", adminVuoto);

			}
			// Se è una modifica ad un cliente esistente
			else {
				int admin_id = Integer.parseInt(adminId);
				Admin admin = adminDAO.findById(admin_id);

				if (admin == null) {
					request.getSession().setAttribute("errore", "Nessun admin trovato");
					response.sendRedirect(request.getContextPath() + "/admin/team");
					return;
				}

				request.setAttribute("admin", admin);
				int numeroDiAdmin = adminDAO.countAdmin();
				request.setAttribute("numeroAdminAttuali", numeroDiAdmin); // se vi è un solo admin non si può eliminare

				// Indica se l'admin corrente è quello visualizzato
				boolean isAdminCorrente = (adminCorrente != null && adminCorrente.getId() == admin_id);
				request.setAttribute("isAdminCorrente", isAdminCorrente);

				// Indica se l'eliminazione è permessa
				boolean eliminazionePermessa = (numeroDiAdmin > 1) && !isAdminCorrente;
				request.setAttribute("eliminazionePermessa", eliminazionePermessa);
			}

			request.getRequestDispatcher("/WEB-INF/admin/dettagliTeam.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			request.setAttribute("errore", "Errore nel caricamento del membro del team");
			request.getRequestDispatcher("/admin/team").forward(request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String adminId = request.getParameter("id");
		String azione = request.getParameter("azione");

		if (adminId == null || adminId.isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/admin/team");
			return;
		}

		try {
			int admin_id = Integer.parseInt(adminId);

			Admin adminCorrente = (Admin) request.getSession().getAttribute("adminCorrente");

			// richiesta di eliminazione
			if ("elimina".equalsIgnoreCase(azione)) {

				// Verifica che non si stia eliminando l'account in uso
				if (adminCorrente != null && adminCorrente.getId() == admin_id) {
					request.getSession().setAttribute("errore",
							"Non puoi eliminare il tuo stesso account amministratore!");
					response.sendRedirect(request.getContextPath() + "/admin/membroteam?id=" + admin_id);
					return;
				}

				// Verifica che ci sia almeno un altro admin
				int numeroAdmin = adminDAO.countAdmin();
				if (numeroAdmin <= 1) {
					request.getSession().setAttribute("errore",
							"Impossibile eliminare: deve esistere almeno un amministratore nel sistema!");
					response.sendRedirect(request.getContextPath() + "/admin/membroteam?id=" + admin_id);
					return;
				}

				// Procedi con l'eliminazione
				try {
					adminDAO.delete(admin_id);
				} catch (SQLException e) {
					e.printStackTrace();
					request.getSession().setAttribute("errore", "Errore durante l'eliminazione: " + e.getMessage());
					response.sendRedirect(request.getContextPath() + "/admin/membroteam?id=" + admin_id);
					return;
				}

				request.getSession().setAttribute("messaggio", "Admin eliminato con successo!");
				response.sendRedirect(request.getContextPath() + "/admin/team");
				return;
			}

			// procedi con l'aggiornamento completo
			gestisciAggiornamentoCompleto(request, response, admin_id);

		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
			request.getSession().setAttribute("errore", "Errore durante il salvataggio: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/admin/membroteam?id=" + adminId);
		}
	}

	private void gestisciAggiornamentoCompleto(HttpServletRequest request, HttpServletResponse response, int admin_id)
			throws ServletException, IOException, SQLException {

		Admin admin;
		boolean isNuovoAdmin = (admin_id == 0);

		if (isNuovoAdmin) {
			// CREAZIONE
			admin = new Admin();
			aggiornaDatiBaseAdmin(request, admin);

			// Il DAO deve fare la INSERT e restituire il nuovo ID autogenerato dal
			// Database!
			try {
				admin_id = adminDAO.insertAndReturnId(admin);
			} catch (SQLException e) {
				String errorMessage = e.getMessage();
				if (errorMessage != null && errorMessage.contains("mail")) {
					request.setAttribute("errore", "Indirizzo email non disponibile, sceglierne un altro.");
					request.setAttribute("admin", admin); // Ripopola il form
					request.getRequestDispatcher("/WEB-INF/admin/dettagliTeam.jsp").forward(request, response);
				}
				return;
			}

		} else {
			// MODIFICA
			admin = adminDAO.findById(admin_id);
			if (admin == null) {
				request.getSession().setAttribute("errore", "Admin non trovato");
				response.sendRedirect(request.getContextPath() + "/admin/team");
				return;
			}
			aggiornaDatiBaseAdmin(request, admin);
			try {
				adminDAO.update(admin);

				// GESTIONE CAMBIO PASSWORD
				String nuovaPassword = request.getParameter("password");
				if (nuovaPassword != null && !nuovaPassword.trim().isEmpty()) {
					adminDAO.changePassword(admin_id, nuovaPassword);
				}
			} catch (SQLException e) {
				String errorMessage = e.getMessage();
				if (errorMessage != null && errorMessage.contains("mail")) {
					request.getSession().setAttribute("errore",
							"Indirizzo email non disponibile, sceglierne un altro.");
					response.sendRedirect(request.getContextPath() + "/admin/membroteam?id=" + admin_id);
				}
				return;
			}
		}

		request.getSession().setAttribute("messaggio",
				isNuovoAdmin ? "Admin creato con successo!" : "Admin aggiornato con successo!");
		response.sendRedirect(request.getContextPath() + "/admin/membroteam?id=" + admin_id);
	}

	private void aggiornaDatiBaseAdmin(HttpServletRequest request, Admin admin) {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		if (email != null && !email.trim().isEmpty()) {
			admin.setEmail(email);
		}
		if (password != null && !password.trim().isEmpty()) {
			admin.setPassword(password);
		}
	}
}
