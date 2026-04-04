package ecom.control.servlet;

import ecom.model.bean.Cliente;
import ecom.model.bean.Recensione;
import ecom.model.dao.RecensioneDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

@WebServlet("/user/nuova-recensione")
public class NuovaRecensioneServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private RecensioneDAO recensioneDAO;

    @Override
    public void init() throws ServletException {
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        this.recensioneDAO = new RecensioneDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String prodottoIdStr = request.getParameter("prodottoId");
        if (prodottoIdStr == null || prodottoIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/recensioni");
            return;
        }

        request.setAttribute("prodottoId", prodottoIdStr);
        request.getRequestDispatcher("/WEB-INF/user/nuovaRecensione.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cliente cliente = (Cliente) session.getAttribute("utenteLoggato");

        String prodottoIdStr = request.getParameter("prodottoId");
        String titolo = request.getParameter("titolo");
        String valutazioneStr = request.getParameter("valutazione");
        String commento = request.getParameter("commento");

        // Validazione base
        if (prodottoIdStr == null || prodottoIdStr.isEmpty()) {
            session.setAttribute("errore", "Prodotto non valido.");
            response.sendRedirect(request.getContextPath() + "/user/recensioni");
            return;
        }

        if (titolo == null || titolo.trim().isEmpty()) {
            session.setAttribute("errore", "Il titolo è obbligatorio.");
            response.sendRedirect(request.getContextPath() + "/user/nuova-recensione?prodottoId=" + prodottoIdStr);
            return;
        }

        if (valutazioneStr == null || valutazioneStr.isEmpty()) {
            session.setAttribute("errore", "Seleziona una valutazione.");
            response.sendRedirect(request.getContextPath() + "/user/nuova-recensione?prodottoId=" + prodottoIdStr);
            return;
        }

        try {
            int prodottoId = Integer.parseInt(prodottoIdStr);
            int valutazione = Integer.parseInt(valutazioneStr);

            if (valutazione < 1 || valutazione > 5) {
                session.setAttribute("errore", "La valutazione deve essere compresa tra 1 e 5.");
                response.sendRedirect(request.getContextPath() + "/user/nuova-recensione?prodottoId=" + prodottoId);
                return;
            }

            // Crea la recensione
            Recensione recensione = new Recensione();
            recensione.setProdottoId(prodottoId);
            recensione.setClienteId(cliente.getId());
            recensione.setValutazione(valutazione);
            recensione.setTitolo(titolo.trim());
            recensione.setCommento(commento != null ? commento.trim() : null);
            recensione.setData(new Timestamp(System.currentTimeMillis()));

            recensioneDAO.insert(recensione);

            session.setAttribute("messaggio", "Grazie per la tua recensione!");
            response.sendRedirect(request.getContextPath() + "/user/recensioni");

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            session.setAttribute("errore", "Errore durante il salvataggio della recensione.");
            response.sendRedirect(request.getContextPath() + "/user/nuova-recensione?prodottoId=" + prodottoIdStr);
        }
    }
}