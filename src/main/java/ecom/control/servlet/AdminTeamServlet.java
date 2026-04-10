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

import ecom.model.bean.Admin;
import ecom.model.dao.AdminDAO;

@WebServlet("/admin/team")
public class AdminTeamServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AdminDAO adminDAO;
	private int pageSize = 12;

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

		String errore = (String) request.getSession().getAttribute("errore");
		if (errore != null) {
			request.setAttribute("errore", errore);
			request.getSession().removeAttribute("errore"); // Pulizia della sessione
		}

		String pag = request.getParameter("pagina");
		int pagina = 1; // pagina di default se non specifiata
		if (pag != null && !pag.isEmpty()) {
			pagina = Integer.parseInt(pag);
		}
		String ordinamento = request.getParameter("ordinamento");
		// Ordinamento dei prodotti
		if (ordinamento == null || ordinamento.isEmpty()) {
			ordinamento = "recenti";
		}

		List<Admin> adminList = null;
		int totaleAdmin = 0; // per paginazione
		try {
			if (ordinamento.equalsIgnoreCase("recenti")) {
				adminList = adminDAO.findAllPaginazione(pagina, pageSize);
				totaleAdmin = adminDAO.countAdmin();
			} else if (ordinamento.equalsIgnoreCase("alfabetico")) {
				adminList = adminDAO.findAlfabeticoEmail(pagina, pageSize);
				totaleAdmin = adminDAO.countAdmin();
			} else if (ordinamento.equalsIgnoreCase("findById")) {
				int adminID = Integer.parseInt(request.getParameter("id_admin"));
				Admin admin = adminDAO.findById(adminID);
				adminList = new ArrayList<>(); // nuova lista per gestire questo caso particolare
				if (admin != null) {
					adminList.add(admin);
					totaleAdmin = 1;
				}
			} else {
				// di default recenti
				adminList = adminDAO.findAllPaginazione(pagina, pageSize);
				totaleAdmin = adminDAO.countAdmin();
			}

			int pagineTotali = (int) Math.ceil((double) totaleAdmin / pageSize);

			request.setAttribute("adminList", adminList);
			request.setAttribute("totaleAdmin", totaleAdmin);
			request.setAttribute("pagineTotali", pagineTotali);
			request.setAttribute("paginaCorrente", pagina);

			request.getRequestDispatcher("/WEB-INF/admin/team.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			request.setAttribute("erroreTeam", "Si è verificato un errore nel caricamento del team");
			request.getRequestDispatcher("/WEB-INF/admin/team.jsp").forward(request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
