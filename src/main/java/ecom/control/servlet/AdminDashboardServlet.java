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
			// recupero ultimi 10 ordini
			List<Ordine> ordini = ordineDAO.findLastXOrders(1, 10);
			//mostra 10 prodotti terminati, in ordine casuale
			List<Prodotto> prodotti = prodottoDAO.find10Terminati();
			
			request.setAttribute("ordini", ordini);
			request.setAttribute("prodotti", prodotti);
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
