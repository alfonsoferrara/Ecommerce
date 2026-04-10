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

import ecom.model.bean.Cliente;
import ecom.model.dao.ClienteDAO;

@WebServlet("/admin/clienti")
public class AdminClientiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ClienteDAO clienteDAO;
	private int pageSize = 12;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.clienteDAO = new ClienteDAO(ds);
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

		List<Cliente> clienti = null;
		int totaleClienti = 0; // per paginazione
		try {
			if (ordinamento.equalsIgnoreCase("recenti")) {
				clienti = clienteDAO.findAllPaginazione(pagina, pageSize);
				totaleClienti = clienteDAO.countAll();
			} else if (ordinamento.equalsIgnoreCase("alfabeticoNome")) {
				clienti = clienteDAO.findAlfabeticoNome(pagina, pageSize);
				totaleClienti = clienteDAO.countAll();
			} else if (ordinamento.equalsIgnoreCase("alfabeticoCognome")) {
				clienti = clienteDAO.findAlfabeticoCognome(pagina, pageSize);
				totaleClienti = clienteDAO.countAll();
			} else if (ordinamento.equalsIgnoreCase("findById")) {
				int clienteId = Integer.parseInt(request.getParameter("id_cliente"));
				Cliente cliente = clienteDAO.findById(clienteId);
				clienti = new ArrayList<>(); // nuova lista per gestire questo caso particolare
				if (cliente != null) {
					clienti.add(cliente);
					totaleClienti = 1;
				}
			} else {
				// di default recenti
				clienti = clienteDAO.findAllPaginazione(pagina, pageSize);
				totaleClienti = clienteDAO.countAll();
			}

			int pagineTotali = (int) Math.ceil((double) totaleClienti / pageSize);

			request.setAttribute("clienti", clienti);
			request.setAttribute("totaleClienti", totaleClienti);
			request.setAttribute("pagineTotali", pagineTotali);
			request.setAttribute("paginaCorrente", pagina);

			request.getRequestDispatcher("/WEB-INF/admin/clienti.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			request.setAttribute("erroreClienti", "Si è verificato un errore nel caricamento dei clienti");
			request.getRequestDispatcher("/WEB-INF/admin/clienti.jsp").forward(request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
