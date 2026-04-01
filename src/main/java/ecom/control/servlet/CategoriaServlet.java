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
			List<Prodotto> prodotti = prodottoDAO.findAll();

			String catNome = "";
			String catDesc = "";

			// recupero nome e descrizione categoria
			// filtro i prodotti della categoria tramite id
			if (catParam != null && !catParam.isEmpty()) {
				int catId = Integer.parseInt(catParam);
				Categoria cat = categoriaDAO.findById(catId);
				catNome = cat.getNome();
				catDesc = cat.getDescrizione();

				List<Prodotto> prodottiFiltrati = new ArrayList<>();
				for (Prodotto p : prodotti) {
					if (p.getCategoriaId() == catId) {
						prodottiFiltrati.add(p);
					}
				}
				prodotti = prodottiFiltrati;
			}

			request.setAttribute("prodotti", prodotti);
			request.setAttribute("catNome", catNome);
			request.setAttribute("catDesc", catDesc);

			request.getRequestDispatcher("WEB-INF/views/catalogo.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel caricamento del catalogo");
		}
	}
}
