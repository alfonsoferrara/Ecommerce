package ecom.control.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import ecom.model.bean.Ordine;
import ecom.model.dao.OrdineDAO;

@WebServlet("/admin/ordini")
public class AdminOrdiniServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OrdineDAO ordineDAO;
	private int pageSize = 12;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.ordineDAO = new OrdineDAO(ds);
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

		List<Ordine> ordini = null;
		int totaleOrdini = 0; // per paginazione

		try {
			if (ordinamento.equalsIgnoreCase("recenti")) {
				ordini = ordineDAO.findLastXOrders(pag, pageSize);
				totaleOrdini = ordineDAO.countOrdini();
			} else if (ordinamento.equalsIgnoreCase("metodoPagamento")) {
				String metodoDiPagamento = request.getParameter("pagamento");
				ordini = ordineDAO.findByMetodoDiPagamento(metodoDiPagamento, pag, pageSize);
				totaleOrdini = ordineDAO.countByMetodoDiPagamento(metodoDiPagamento);
			} else if (ordinamento.equalsIgnoreCase("clienteID")) {
				String clienteId = request.getParameter("id_cliente");
				ordini = ordineDAO.findByClienteId(Integer.parseInt(clienteId), pag, pageSize);
				totaleOrdini = ordineDAO.countByClienteId(Integer.parseInt(clienteId));
			} else if (ordinamento.equalsIgnoreCase("ordineId")) {
				String ordineId = request.getParameter("id_ordine");
				Ordine ordine = ordineDAO.findById(Integer.parseInt(ordineId));
				ordini = new ArrayList<>(); // nuova lista per gestire questo caso particolare
				if (ordine != null) {
					ordini.add(ordine);
					totaleOrdini = 1;
				}
			} else if (ordinamento.equalsIgnoreCase("stato")) {
				String stato = request.getParameter("stato_ordine");
				ordini = ordineDAO.findByStatus(stato, pag, pageSize);
				totaleOrdini = ordineDAO.countByStatus(stato);
			} else if (ordinamento.equalsIgnoreCase("rangeData")) {
				String dataInizio = request.getParameter("dataInizio");
				String dataFine = request.getParameter("dataFine");
				if (dataInizio != null && !dataInizio.isEmpty() && dataFine != null && !dataFine.isEmpty()) {
					ordini = ordineDAO.findByDateRange(dataInizio, dataFine, pag, pageSize); // range completo
					totaleOrdini = ordineDAO.countByDateRange(dataInizio, dataFine);
				} else if (dataInizio != null && !dataInizio.isEmpty()) {
					// dataFine è vuota
					ordini = ordineDAO.findByDataInizio(dataInizio, pag, pageSize);
					totaleOrdini = ordineDAO.countByDataInizio(dataInizio);
				} else if (dataFine != null && !dataFine.isEmpty()) {
					// dataInizio è vuota
					ordini = ordineDAO.findByDataFine(dataFine, pag, pageSize);
					totaleOrdini = ordineDAO.countByDataFine(dataFine);
				}
			} else {
				// Nessun filtro valido - default
				ordini = ordineDAO.findLastXOrders(pag, 12);
				totaleOrdini = ordineDAO.countOrdini();
			}

			int totalPages = (int) Math.ceil((double) totaleOrdini / pageSize);

			request.setAttribute("totalPages", totalPages);
			request.setAttribute("currentPage", pag);
			request.setAttribute("pageSize", pageSize);
			request.setAttribute("ordini", ordini);
			request.getRequestDispatcher("/WEB-INF/admin/ordini.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			request.setAttribute("errore", "Errore nel caricamento degli ordini");
			request.getRequestDispatcher("/WEB-INF/admin/ordini.jsp").forward(request, response);
			return;

		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
