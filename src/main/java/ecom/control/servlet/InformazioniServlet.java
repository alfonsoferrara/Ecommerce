package ecom.control.servlet;

import ecom.model.bean.Cliente;
import ecom.model.dao.ClienteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user/informazioni")
public class InformazioniServlet extends HttpServlet {

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

        HttpSession session = request.getSession();
        Cliente cliente = (Cliente) session.getAttribute("utenteLoggato");

        request.setAttribute("cliente", cliente);
        request.getRequestDispatcher("/WEB-INF/user/informazioni.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cliente cliente = (Cliente) session.getAttribute("utenteLoggato");

        if (cliente == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validazione base
        if (nome == null || nome.trim().isEmpty() ||
            cognome == null || cognome.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {

            session.setAttribute("errore", "Nome, Cognome ed Email sono obbligatori.");
            response.sendRedirect(request.getContextPath() + "/user/informazioni");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            session.setAttribute("errore", "Formato email non valido.");
            response.sendRedirect(request.getContextPath() + "/user/informazioni");
            return;
        }

        try {
            // Aggiorna i dati del cliente
            cliente.setNome(nome.trim());
            cliente.setCognome(cognome.trim());
            cliente.setEmail(email.trim().toLowerCase());
            cliente.setTelefono(telefono);

            // Aggiorna la password solo se è stata inserita
            if (password != null && !password.trim().isEmpty()) {
                cliente.setPassword(password);
            }

            clienteDAO.update(cliente);
            session.setAttribute("messaggio", "Profilo aggiornato con successo!");
            response.sendRedirect(request.getContextPath() + "/user/profilo");

        } catch (SQLException e) {
            e.printStackTrace();

            if (e.getMessage().contains("Duplicate entry") || e.getMessage().contains("UNIQUE")) {
                session.setAttribute("errore", "Email già utilizzata da un altro account.");
            } else {
                session.setAttribute("errore", "Errore durante l'aggiornamento del profilo.");
            }
            response.sendRedirect(request.getContextPath() + "/user/informazioni");
        }
    }
}