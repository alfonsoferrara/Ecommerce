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

import ecom.model.bean.Categoria;
import ecom.model.bean.Cliente;
import ecom.model.bean.Ordine;
import ecom.model.bean.Prodotto;
import ecom.model.dao.CategoriaDAO;
import ecom.model.dao.ClienteDAO;
import ecom.model.dao.OrdineDAO;
import ecom.model.dao.ProdottoDAO;

@WebServlet("/admin/prodotti")
public class AdminProdottiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProdottoDAO prodottoDAO;
	private CategoriaDAO categoriaDAO;
	private int pageSize = 12;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.prodottoDAO = new ProdottoDAO(ds);
		this.categoriaDAO = new CategoriaDAO(ds);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String numPagina = request.getParameter("pagina"); // paginazione
		int pag = 0;
		if (numPagina != null && !numPagina.isEmpty()) {
			pag = Integer.parseInt(numPagina);
		} else {
			pag = 1; // pagina di default se non specifiata
		}

		String ordinamento = request.getParameter("ordinamento");
		// ORDINAMENTO DEGLI ORDINI
		if (ordinamento == null || ordinamento.isEmpty()) {
			ordinamento = "recenti";
		}

		List<Prodotto> prodotti = null;
		ArrayList<String> nomiCategorie = new ArrayList<String>();

		int totaleProdotti = 0; // per paginazione
		try {
			// Rrecupera le categorie per il dropdown
            List<Categoria> tutteCategorie = categoriaDAO.findAll();
            request.setAttribute("tutteCategorie", tutteCategorie);
			
			if (ordinamento.equalsIgnoreCase("recenti")) {
				prodotti = prodottoDAO.findRecenti(pag, pageSize);
				totaleProdotti = prodottoDAO.countProdotti();
			} else if (ordinamento.equalsIgnoreCase("categoriaId")) {
				// GESTISCI SIA ID CHE NOME CATEGORIA
                String categoriaParam = request.getParameter("categoria_id");
                int categoriaId = -1;
                
                // Se il parametro contiene lettere, è un nome categoria
                if (categoriaParam != null && !categoriaParam.isEmpty()) {
                    try {
                        categoriaId = Integer.parseInt(categoriaParam);
                    } catch (NumberFormatException e) {
                        // Non è un numero, cerca per nome
                        Categoria cat = categoriaDAO.findByNome(categoriaParam);
                        if (cat != null) {
                            categoriaId = cat.getId();
                        }
                    }
                }
                
                if (categoriaId > 0) {
                    prodotti = prodottoDAO.findByCategoriaId_OrderBy(pag, pageSize, categoriaId, "recenti");
                    totaleProdotti = prodottoDAO.countProdottiByCategoriaId(categoriaId);
                } else {
                    // Categoria non trovata
                    prodotti = new ArrayList<>();
                    totaleProdotti = 0;
                }
			} else if (ordinamento.equalsIgnoreCase("prodottoId")) {
				String prodottoId = request.getParameter("prodotto_id");
				Prodotto prodotto = prodottoDAO.findById(Integer.parseInt(prodottoId));
				prodotti = new ArrayList<>(); // nuova lista per gestire questo caso particolare
				if (prodotto != null) {
					prodotti.add(prodotto);
					totaleProdotti = 1;
				}
			} else if (ordinamento.equalsIgnoreCase("stato")) {
				int stato = Integer.parseInt(request.getParameter("stato_prodotto"));
				prodotti = prodottoDAO.findByStatus(stato, pag, pageSize);
				totaleProdotti = prodottoDAO.countProdottiByStatus(stato);
			} else if (ordinamento.equalsIgnoreCase("stock")) {
				String minimo = request.getParameter("minimo");
				String massimo = request.getParameter("massimo");
				if (minimo != null && !minimo.isEmpty() && massimo != null && !massimo.isEmpty()) {
					int min = Integer.parseInt(minimo);
					int max = Integer.parseInt(massimo);

					prodotti = prodottoDAO.findByStockRange(min, max, pag, pageSize); // range completo
					totaleProdotti = prodottoDAO.countProdottiByStockRange(min, max);
				} else if (minimo != null && !minimo.isEmpty()) {
					// max è vuoto
					int min = Integer.parseInt(minimo);
					prodotti = prodottoDAO.findByStockMinimo(min, pag, pageSize);
					totaleProdotti = prodottoDAO.countProdottiByStockMin(min);
				} else if (massimo != null && !massimo.isEmpty()) {
					// min è vuoto
					int max = Integer.parseInt(massimo);

					prodotti = prodottoDAO.findByStockMassimo(max, pag, pageSize);
					totaleProdotti = prodottoDAO.countProdottiByStockMax(max);
				}
			} else {
				// Nessun filtro valido - default
				prodotti = prodottoDAO.findRecenti(pag, pageSize);
				totaleProdotti = prodottoDAO.countProdotti();
			}

			// recupero nomi categorie
			for (Prodotto prodotto : prodotti) {
				int idCategoria = prodotto.getCategoriaId();
				Categoria categoria = categoriaDAO.findById(idCategoria);
				String nomeCategoria = categoria.getNome();
				nomiCategorie.add(nomeCategoria);
			}

			int totalPages = (int) Math.ceil((double) totaleProdotti / pageSize);

			request.setAttribute("totalPages", totalPages);
			request.setAttribute("currentPage", pag);
			request.setAttribute("pageSize", pageSize);
			request.setAttribute("prodotti", prodotti);
			request.setAttribute("nomiCategorie", nomiCategorie);
			request.getRequestDispatcher("/WEB-INF/admin/prodotti.jsp").forward(request, response);
			
		} catch (SQLException | NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			request.setAttribute("errore", "Errore nel caricamento dei prodotti");
			request.getRequestDispatcher("/WEB-INF/admin/prodotti.jsp").forward(request, response);
			return;

		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
