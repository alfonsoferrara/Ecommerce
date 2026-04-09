package ecom.control.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import ecom.model.bean.Categoria;
import ecom.model.dao.CategoriaDAO;

@WebServlet("/admin/categoria")
public class AdminDettagliCategoria extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CategoriaDAO categoriaDAO;

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

		String erroreSessione = (String) request.getSession().getAttribute("errore");
		if (erroreSessione != null) {
			request.setAttribute("errore", erroreSessione);
			request.getSession().removeAttribute("errore");
		}

		String categoriaId = request.getParameter("id");

		try {
			// Se è una nuova categoria
			if (categoriaId == null || categoriaId.isEmpty() || categoriaId.equals("new")) {
				Categoria categoriaVuota = new Categoria();
				categoriaVuota.setId(0); // ID 0 fa capire alla JSP che è un nuovo inserimento

				request.setAttribute("categoria", categoriaVuota);

			}
			// Se è una modifica ad una categoria esistente
			else {
				int categoria_id = Integer.parseInt(categoriaId);
				Categoria categoria = categoriaDAO.findById(categoria_id);

				if (categoria == null) {
					response.sendRedirect(request.getContextPath() + "/admin/categorie");
					return;
				}

				request.setAttribute("categoria", categoria);
			}

			request.getRequestDispatcher("/WEB-INF/admin/dettagliCategoria.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			request.setAttribute("errore", "Errore nel caricamento della categoria");
			request.getRequestDispatcher("/admin/categorie").forward(request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String categoriaId = request.getParameter("id");
		String azione = request.getParameter("azione");

		if (categoriaId == null || categoriaId.isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/admin/categorie");
			return;
		}

		try {
			int categoria_id = Integer.parseInt(categoriaId);
			
			// richiesta di eliminazione
			if ("elimina".equals(azione)) {
				try {
					categoriaDAO.delete(categoria_id);
				} catch (SQLException e) {
					e.printStackTrace();
					String messaggio = "La categoria non può essere eliminata poiché possiede dei prodotti al suo interno";
					request.getSession().setAttribute("errore", "Errore durante l'eliminazione: " + messaggio);
					response.sendRedirect(request.getContextPath() + "/admin/categoria?id=" + categoriaId);
					return;
				}

				request.getSession().setAttribute("messaggio", "Categoria eliminata con successo!");
				response.sendRedirect(request.getContextPath() + "/admin/categorie");
				return; // Interrompo l'esecuzione per evitare di fare anche l'aggiornamento
			}
			// procedi con l'aggiornamento completo
			gestisciAggiornamentoCompleto(request, response, categoria_id);

		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
			request.getSession().setAttribute("errore", "Errore durante il salvataggio: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/admin/categoria?id=" + categoriaId);
		}
	}

	private void gestisciAggiornamentoCompleto(HttpServletRequest request, HttpServletResponse response,
			int categoria_id) throws ServletException, IOException, SQLException {

		Categoria categoria;
		boolean isNuovaCategoria = (categoria_id == 0);

		if (isNuovaCategoria) {
			// CREAZIONE
			categoria = new Categoria();
			aggiornaDatiBaseCategoria(request, categoria);

			// Il DAO deve fare la INSERT e restituire il nuovo ID autogenerato dal
			// Database!
			categoria_id = categoriaDAO.insertAndReturnId(categoria);

		} else {
			// MODIFICA
			categoria = categoriaDAO.findById(categoria_id);
			if (categoria == null) {
				// request.getSession().setAttribute("errore", "Prodotto non trovato");
				response.sendRedirect(request.getContextPath() + "/admin/categorie");
				return;
			}
			aggiornaDatiBaseCategoria(request, categoria);
			categoriaDAO.update(categoria);
		}

		request.getSession().setAttribute("messaggio",
				isNuovaCategoria ? "Categoria creata con successo!" : "Categoria aggiornata con successo!");
		response.sendRedirect(request.getContextPath() + "/admin/categoria?id=" + categoria_id);
	}

	private void aggiornaDatiBaseCategoria(HttpServletRequest request, Categoria categoria) {
		String nome = request.getParameter("nome");
		String descrizione = request.getParameter("descrizione");

		if (nome != null && !nome.trim().isEmpty()) {
			categoria.setNome(nome);
		}

		categoria.setDescrizione(descrizione);
	}
}
