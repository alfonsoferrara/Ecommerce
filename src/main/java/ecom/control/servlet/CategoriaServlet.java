package ecom.control.servlet;

import ecom.model.bean.Categoria;
import ecom.model.bean.Immagine;
import ecom.model.bean.Prodotto;
import ecom.model.dao.CategoriaDAO;
import ecom.model.dao.ImmagineDAO;
import ecom.model.dao.ProdottoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/categoria")
public class CategoriaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ProdottoDAO prodottoDAO;
	private CategoriaDAO categoriaDAO;
	private int maxNumeroProdotti = 12; // per la paginazione
	private ImmagineDAO immagineDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.prodottoDAO = new ProdottoDAO(ds);
		this.categoriaDAO = new CategoriaDAO(ds);
		this.immagineDAO = new ImmagineDAO(ds);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String catParam = request.getParameter("id");
			String catPagina = request.getParameter("pagina");

			// ORDINAMENTO DEI PRODOTTI
			String ordine = request.getParameter("ordine");
			if (ordine == null || ordine.isEmpty()) {
				ordine = "recenti";
			}

			String catNome = "";
			String catDesc = "";
			List<Prodotto> prodotti = null;
			int totaleProdotti = 0;

			if (catParam != null && !catParam.isEmpty()) {
				int catId = Integer.parseInt(catParam);

				// recupero nome e descrizione categoria
				Categoria categoria = categoriaDAO.findById(catId);
				catNome = categoria.getNome();
				catDesc = categoria.getDescrizione();

				if (catPagina != null && !catPagina.isEmpty()) {
					// se viene specificata una pagina allora la uso
					int numPagina = Integer.parseInt(catPagina);
					prodotti = prodottoDAO.findByCategoriaId_OrderBy(numPagina, maxNumeroProdotti, catId, ordine);
				} else {
					// altrimenti recupero dalla prima pagina
					prodotti = prodottoDAO.findByCategoriaId_OrderBy(1, maxNumeroProdotti, catId, ordine);
				}

				totaleProdotti = prodottoDAO.countProdottiByCategoriaId(catId);

				// RECUPERA LE IMMAGINI PRINCIPALI PER OGNI PRODOTTO
				Map<Integer, String> immaginiPrincipali = new HashMap<>();
				for (Prodotto prodotto : prodotti) {
					Immagine imgPrincipale = immagineDAO.findPrincipalByProdottoId(prodotto.getId());
					if (imgPrincipale != null) {
						immaginiPrincipali.put(prodotto.getId(), imgPrincipale.getUrl());
					} else {
						// Immagine di default se non esiste
						immaginiPrincipali.put(prodotto.getId(), "/images/default.jpg");
					}
				}

				request.setAttribute("immaginiPrincipali", immaginiPrincipali);
			}

			int totalePagine = (int) Math.ceil((double) totaleProdotti / maxNumeroProdotti);

			request.setAttribute("totalePagine", totalePagine);
			request.setAttribute("totaleProdotti", totaleProdotti); // per la paginazione
			request.setAttribute("prodotti", prodotti);
			request.setAttribute("catNome", catNome);
			request.setAttribute("catDesc", catDesc);

			request.getRequestDispatcher("WEB-INF/views/catalogo.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException |

				NullPointerException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_NOT_FOUND,
					"Errore nel caricamento del catalogo, controlla l'indirizzo inserito!");
		}
	}
}
