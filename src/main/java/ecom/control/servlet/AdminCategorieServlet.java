package ecom.control.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import ecom.model.bean.Categoria;
import ecom.model.dao.CategoriaDAO;

@WebServlet("/admin/categorie")
public class AdminCategorieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CategoriaDAO categoriaDAO;
	private int pageSize = 12;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.categoriaDAO = new CategoriaDAO(ds);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String messaggio = (String) request.getSession().getAttribute("messaggio");
		if (messaggio != null) {
			request.setAttribute("operazioneRiuscita", messaggio);
			request.getSession().removeAttribute("messaggio"); // Pulizia della sessione
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

		List<Categoria> categorie = null;
		int totaleCategorie = 0; // per paginazione
		try {
			if (ordinamento.equalsIgnoreCase("recenti")) {
				categorie = categoriaDAO.findAllPaginazione(pagina, pageSize);
			}
			if (ordinamento.equalsIgnoreCase("alfabetico")) {
				categorie = categoriaDAO.findAlfabetico(pagina, pageSize);
			}

			// è lo stesso perche non filtro ma cambio solo l'ordinamento
			totaleCategorie = categoriaDAO.countCategorie();

			Map<String, Integer> numeroProdottiCategoria = categoriaDAO.getNumeroProdottiPerCategoria();
			int pagineTotali = (int) Math.ceil((double) totaleCategorie / pageSize);

			request.setAttribute("categorie", categorie);
			request.setAttribute("totaleCategorie", totaleCategorie);
			request.setAttribute("pagineTotali", pagineTotali);
			request.setAttribute("paginaCorrente", pagina);
			request.setAttribute("numeroProdotti", numeroProdottiCategoria);

			request.getRequestDispatcher("/WEB-INF/admin/categorie.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			request.setAttribute("errorePagCategoria", "Si è verificato un errore nel caricamento delle categorie");
			request.getRequestDispatcher("/WEB-INF/admin/categorie.jsp").forward(request, response);
			return;
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
