package ecom.control.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import ecom.model.bean.Ordine;
import ecom.model.bean.Prodotto;
import ecom.model.bean.ProdottoConUltimoAcquisto;
import ecom.model.dao.OrdineDAO;
import ecom.model.dao.ProdottoDAO;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OrdineDAO ordineDAO;
	private ProdottoDAO prodottoDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.ordineDAO = new OrdineDAO(ds);
		this.prodottoDAO = new ProdottoDAO(ds);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// BOX TOTALE DEGLI ORDINI RANGE DATA
			double totaleVendite = 0;
			String dataInizio = request.getParameter("dataInizio");
			String dataFine = request.getParameter("dataFine");
			if (dataInizio != null && !dataInizio.isEmpty() && dataFine != null && !dataFine.isEmpty()) {
				totaleVendite = ordineDAO.countTotaleByDateRange(dataInizio, dataFine);
			} else if (dataInizio != null && !dataInizio.isEmpty()) {
				// dataFine è vuota
				totaleVendite = ordineDAO.countTotaleByDataInizio(dataInizio);
			} else if (dataFine != null && !dataFine.isEmpty()) {
				// dataInizio è vuota
				totaleVendite = ordineDAO.countTotaleByDataFine(dataFine);
			} else {
				// default - mese corrente
				totaleVendite = ordineDAO.countTotaleByMeseCorrente();
			}
			request.setAttribute("totaleVendite", totaleVendite);

			// BOX ORDINI DA EVADERE
			int ordiniDaEvadere = ordineDAO.countByStatus("IN ELABORAZIONE");
			request.setAttribute("ordiniDaEvadere", ordiniDaEvadere);

			//BOX PRODOTTI IN ESAURIMENTO
			int prodottiInEsaurimento = prodottoDAO.countProdottiByStockMin(5);
			request.setAttribute("prodottiInEsaurimento", prodottiInEsaurimento);

			// RECUPERO ULTIMI 5 ORDINI
			List<Ordine> ordiniTab = ordineDAO.findLastXOrders(1, 5);
			request.setAttribute("ordiniTab", ordiniTab);

			// MOSTRA 5 PRODOTTI TERMINATI IN ORDINE CASUALE
			List<ProdottoConUltimoAcquisto> prodotti = prodottoDAO.find10Terminati();
			request.setAttribute("prodottiTerminati", prodotti);

			request.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			request.setAttribute("errore", "Errore nel caricamento degli ordini");
			return;
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
