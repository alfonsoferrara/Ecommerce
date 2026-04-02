package ecom.control.servlet;

import ecom.model.bean.Categoria;
import ecom.model.bean.Prodotto;
import ecom.model.dao.CategoriaDAO;
import ecom.model.dao.ProdottoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/categoria")
public class CategoriaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ProdottoDAO prodottoDAO;
	private CategoriaDAO categoriaDAO;
	private int maxNumeroProdotti = 12; // per la paginazione

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.prodottoDAO = new ProdottoDAO(ds);
		this.categoriaDAO = new CategoriaDAO(ds);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String catParam = request.getParameter("id");
			String catPagina = request.getParameter("pagina");
			String catNome = "";
			String catDesc = "";
			List<Prodotto> prodotti = null;
			int totaleProdotti = 0;
			
			if (catParam != null && !catParam.isEmpty() && catPagina != null && !catPagina.isEmpty()) {
				// filtro i prodotti della categoria tramite id
				int catId = Integer.parseInt(catParam);
				int numPagina = Integer.parseInt(catPagina);
				prodotti = prodottoDAO.findByCategoriaId(numPagina, maxNumeroProdotti, catId); //recupero prodotti della categoria
				totaleProdotti = prodottoDAO.countProdottiByCategoriaId(catId);
				
				// recupero nome e descrizione categoria
				Categoria categoria = categoriaDAO.findById(catId);
				catNome = categoria.getNome();
				catDesc = categoria.getDescrizione();
			}

			request.setAttribute("totaleProdotti", totaleProdotti); // per la paginazione
			request.setAttribute("prodotti", prodotti);
			request.setAttribute("catNome", catNome);
			request.setAttribute("catDesc", catDesc);

			request.getRequestDispatcher("WEB-INF/views/catalogo.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Errore nel caricamento del catalogo");
		}
	}
}
