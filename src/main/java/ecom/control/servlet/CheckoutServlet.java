package ecom.control.servlet;

import ecom.model.bean.*;
import ecom.model.dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private VoceCarrelloDAO voceDAO;
    private ProdottoDAO prodottoDAO;
    private IndirizzoDAO indirizzoDAO;
    private OrdineDAO ordineDAO;
    private DettagliOrdineDAO dettagliDAO;

    @Override
    public void init() throws ServletException {
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        this.voceDAO = new VoceCarrelloDAO(ds);
        this.prodottoDAO = new ProdottoDAO(ds);
        this.indirizzoDAO = new IndirizzoDAO(ds);
        this.ordineDAO = new OrdineDAO(ds);
        this.dettagliDAO = new DettagliOrdineDAO(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        Cliente cliente = (Cliente) request.getSession().getAttribute("utenteLoggato");

        try {
            // Carico gli indirizzi per la tendina della View
            List<Indirizzo> indirizzi = indirizzoDAO.findByClienteId(cliente.getId());
            request.setAttribute("indirizzi", indirizzi);
            
            request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel caricamento del modulo checkout");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Dati Utente dal Filtro di Autenticazione
        Cliente cliente = (Cliente) request.getSession().getAttribute("utenteLoggato");
        
        // ID Carrello dal Filtro GuestCartFilter
        String cartId = (String) request.getAttribute("cartId");

        // Parametri dal Form HTML
        String indirizzoIdStr = request.getParameter("indirizzoId");
        String metodoPagamento = request.getParameter("metodoPagamento");
        String notaCliente = request.getParameter("notaCliente");

        try {
            if (indirizzoIdStr == null || indirizzoIdStr.trim().isEmpty()) {
                request.getSession().setAttribute("errore", "Per favore, seleziona o inserisci un indirizzo di spedizione prima di procedere.");
                response.sendRedirect(request.getContextPath() + "/checkout");
                return;
            }

            if (cartId == null) {
                response.sendRedirect(request.getContextPath() + "/carrello");
                return;
            }

            int indirizzoId = Integer.parseInt(indirizzoIdStr);
            List<VoceCarrello> voci = voceDAO.findByCarrello(cartId);

            if (voci.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/carrello");
                return;
            }

            // CALCOLO DEL SUBTOTALE E SPEDIZIONE ---
            double subtotale = 0.0;
            for (VoceCarrello v : voci) {
                Prodotto p = prodottoDAO.findById(v.getProdottoId());
                if (p != null) subtotale += p.getPrezzo() * v.getQuantita();
            }

            // Aggiungiamo le spese di spedizione in base alla soglia
            double speseSpedizione = (subtotale > 0 && subtotale < 50.00) ? 5.90 : 0.0;
            double totaleFinale = subtotale + speseSpedizione;

            // Creazione dell'Ordine (passo totaleFinale invece di totale!)
            Ordine nuovoOrdine = new Ordine(0, cliente.getId(), indirizzoId, 
                new Timestamp(System.currentTimeMillis()), totaleFinale, "IN_ELABORAZIONE", 
                metodoPagamento, notaCliente);
            
            ordineDAO.insert(nuovoOrdine); // Viene generato l'ID dell'ordine

            // Elaborazione dei Dettagli
            for (VoceCarrello v : voci) {
                Prodotto p = prodottoDAO.findById(v.getProdottoId());
                if (p != null) {
                    
                    // Salvo i dettagli congelando il prezzo unitario al momento dell'acquisto
                    DettagliOrdine dettaglio = new DettagliOrdine(
                        nuovoOrdine.getId(), 
                        p.getId(), 
                        v.getQuantita(), 
                        p.getPrezzo() 
                    );
                    dettagliDAO.insert(dettaglio);
                    
                    // Svuoto il carrello eliminando questa voce
                    voceDAO.deleteProdotto(cartId, p.getId());
                    
                    // AGGIORNAMENTO MAGAZZINO
                    int nuovoStock = p.getStock() - v.getQuantita();
                    p.setStock(Math.max(nuovoStock, 0)); 
                    
                    prodottoDAO.update(p); 
                }
            }

            // Redirect alla pagina degli ordini
            request.getSession().setAttribute("messaggio", "Ordine #" + nuovoOrdine.getId() + " effettuato con successo!");
            response.sendRedirect(request.getContextPath() + "/user/ordini");

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante l'elaborazione dell'ordine");
        }
    }
}