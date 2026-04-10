package ecom.control.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import ecom.model.bean.Attributo;
import ecom.model.bean.AttributoDettagli;
import ecom.model.dao.AttributoDAO;


@WebServlet("/admin/attributo")
public class AdminDettagliAttributo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AttributoDAO attributoDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.attributoDAO = new AttributoDAO(ds);
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

		String attributoId = request.getParameter("id");

		try {
			// Se è un nuovo attributo
			if (attributoId == null || attributoId.isEmpty() || attributoId.equals("new")) {
				Attributo attributoVuoto = new Attributo();
				attributoVuoto.setId(0); // ID 0 fa capire alla JSP che è un nuovo inserimento

				request.setAttribute("attributo", attributoVuoto);
				request.setAttribute("dettagliAttributo", new AttributoDettagli()); //non ha prodotti correlati
			}
			// Se è una modifica ad un attributo esistente
			else {
				int attributo_id = Integer.parseInt(attributoId);
				Attributo attributo = attributoDAO.findById(attributo_id);

				if (attributo == null) {
					response.sendRedirect(request.getContextPath() + "/admin/attributi");
					return;
				}
				
				AttributoDettagli dettagli = attributoDAO.getAttributoConProdotti(attributo_id); //prodotti correlati
		        request.setAttribute("dettagliAttributo", dettagli);
				request.setAttribute("attributo", attributo);
			}

			request.getRequestDispatcher("/WEB-INF/admin/dettagliAttributo.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			request.setAttribute("errore", "Errore nel caricamento dell'attributo");
			request.getRequestDispatcher("/admin/attributi").forward(request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String attributoId = request.getParameter("id");
		String azione = request.getParameter("azione");

		if (attributoId == null || attributoId.isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/admin/attributi");
			return;
		}

		try {
			int attributo_id = Integer.parseInt(attributoId);
			
			// richiesta di eliminazione
			if ("elimina".equals(azione)) {
				try {
					attributoDAO.delete(attributo_id);
				} catch (SQLException e) {
					e.printStackTrace();
					request.getSession().setAttribute("errore", "Errore durante l'eliminazione dell'attributo");
					response.sendRedirect(request.getContextPath() + "/admin/attributo?id=" + attributo_id);
					return;
				}

				request.getSession().setAttribute("messaggio", "Attributo eliminato con successo!");
				response.sendRedirect(request.getContextPath() + "/admin/attributi");
				return; // Interrompo l'esecuzione per evitare di fare anche l'aggiornamento
			}
			// procedi con l'aggiornamento completo
			gestisciAggiornamentoCompleto(request, response, attributo_id);

		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
			request.getSession().setAttribute("errore", "Errore durante il salvataggio: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/admin/attributo?id=" + attributoId);
		}
	}

	private void gestisciAggiornamentoCompleto(HttpServletRequest request, HttpServletResponse response,
			int attributo_id) throws ServletException, IOException, SQLException {

		Attributo attributo;
		boolean isNuovoAttributo = (attributo_id == 0);

		if (isNuovoAttributo) {
			// CREAZIONE
			attributo = new Attributo();
			aggiornaDatiBaseAttributo(request, attributo);

			// Il DAO deve fare la INSERT e restituire il nuovo ID autogenerato dal
			// Database!
			attributo_id = attributoDAO.insertAndReturnId(attributo);

		} else {
			// MODIFICA
			attributo = attributoDAO.findById(attributo_id);
			if (attributo == null) {
				 request.getSession().setAttribute("errore", "Attributo non trovato");
				response.sendRedirect(request.getContextPath() + "/admin/attributi");
				return;
			}
			aggiornaDatiBaseAttributo(request, attributo);
			attributoDAO.update(attributo);
		}

		request.getSession().setAttribute("messaggio",
				isNuovoAttributo ? "Attributo creato con successo!" : "Attributo aggiornato con successo!");
		response.sendRedirect(request.getContextPath() + "/admin/attributo?id=" + attributo_id);
	}

	private void aggiornaDatiBaseAttributo(HttpServletRequest request, Attributo attributo) {
		String nome = request.getParameter("nome");

		if (nome != null && !nome.trim().isEmpty()) {
			attributo.setNome(nome);
		}
	}
}
