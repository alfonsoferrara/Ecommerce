package ecom.control.servlet;

import ecom.model.bean.Cliente;
import ecom.model.dao.ClienteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/registrazione")
public class RegistrazioneServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ClienteDAO clienteDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.clienteDAO = new ClienteDAO(ds);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Mostra semplicemente la pagina del form
		request.getRequestDispatcher("/WEB-INF/views/registrazione.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// parametri del form
		String nome = request.getParameter("nome");
		String cognome = request.getParameter("cognome");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String telefono = request.getParameter("telefono");

		// Validazione base
		if (nome == null || nome.isEmpty() || cognome == null || cognome.isEmpty() || email == null || email.isEmpty()
				|| password == null || password.isEmpty()) {

			request.setAttribute("errore", "Tutti i campi obbligatori devono essere compilati");
			request.getRequestDispatcher("/WEB-INF/views/registrazione.jsp").forward(request, response);
			return;
		}

		// Controlli di formato base
		if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			request.setAttribute("errore", "Formato email non valido.");
			request.getRequestDispatcher("/WEB-INF/views/registrazione.jsp").forward(request, response);
			return;
		}
		
		// Bean Cliente
		Cliente nuovoCliente = new Cliente();
		nuovoCliente.setNome(nome);
		nuovoCliente.setCognome(cognome);
		nuovoCliente.setEmail(email);
		nuovoCliente.setPassword(password);
		nuovoCliente.setTelefono(telefono);

		try {
			// Salvo nel DB (transazione su Utente e Cliente)
			clienteDAO.insert(nuovoCliente);

			request.getSession().setAttribute("messaggio", "Registrazione completata! Ora puoi accedere.");
			response.sendRedirect(request.getContextPath() + "/login");

		} catch (SQLException e) {
			e.printStackTrace();

			// Gestione errore email duplicata
			if (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("UNIQUE")) {
				request.setAttribute("errore", "Email già registrata. Scegli un'altra email.");
			} else {
				request.setAttribute("errore", "Errore durante la registrazione. Riprova più tardi.");
			}

			request.getRequestDispatcher("/WEB-INF/views/registrazione.jsp").forward(request, response);
		}
	}
}