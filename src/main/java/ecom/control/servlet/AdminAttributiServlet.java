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

import ecom.model.bean.Attributo;
import ecom.model.dao.AttributoDAO;

@WebServlet("/admin/attributi")
public class AdminAttributiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AttributoDAO attridutoDAO;
	private int pageSize = 12;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.attridutoDAO = new AttributoDAO(ds);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

		List<Attributo> attributi = null;
		int totaleAttributi = 0; // per paginazione
		try {
			if (ordinamento.equalsIgnoreCase("recenti")) {
				attributi = attridutoDAO.findAllPaginazione(pagina, pageSize);
				totaleAttributi = attridutoDAO.countAttributi();
			} else if (ordinamento.equalsIgnoreCase("alfabetico")) {
				attributi = attridutoDAO.findAlfabetico(pagina, pageSize);
				totaleAttributi = attridutoDAO.countAttributi();
			} else if (ordinamento.equalsIgnoreCase("findById")) {
				int attributoId = Integer.parseInt(request.getParameter("id_attributo"));
				Attributo attributo = attridutoDAO.findById(attributoId);
				attributi = new ArrayList<>(); // nuova lista per gestire questo caso particolare
				if (attributo != null) {
					attributi.add(attributo);
					totaleAttributi = 1;
				}
			} else {
				// default
				attributi = attridutoDAO.findAllPaginazione(pagina, pageSize);
				totaleAttributi = attridutoDAO.countAttributi();
			}

			int pagineTotali = (int) Math.ceil((double) totaleAttributi / pageSize);

			request.setAttribute("attributAdmin", attributi);
			request.setAttribute("totaleAttributi", totaleAttributi);
			request.setAttribute("pagineTotali", pagineTotali);
			request.setAttribute("paginaCorrente", pagina);

			request.getRequestDispatcher("/WEB-INF/admin/attributi.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			request.setAttribute("erroreAttributi", "Si è verificato un errore nel caricamento degli attributi");
			request.getRequestDispatcher("/WEB-INF/admin/attributi.jsp").forward(request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
