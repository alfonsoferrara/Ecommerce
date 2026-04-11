package ecom.control.servlet;

import ecom.model.bean.Prodotto;
import ecom.model.bean.VoceCarrello;
import ecom.model.dao.ProdottoDAO;
import ecom.model.dao.VoceCarrelloDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;

import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/carrello")
public class CarrelloServlet extends HttpServlet {
	private VoceCarrelloDAO voceDAO;
	private ProdottoDAO prodottoDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.voceDAO = new VoceCarrelloDAO(ds);
		this.prodottoDAO = new ProdottoDAO(ds);
	}

	// Metodo helper per inviare risposte JSON
	private void sendJsonResponse(HttpServletResponse response, boolean success, String message, Map<String, Object> extraData) 
	        throws IOException {
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    
	    JSONObject jsonResponse = new JSONObject();
	    jsonResponse.put("success", success);
	    jsonResponse.put("message", message);
	    
	    if (extraData != null) {
	        for (Map.Entry<String, Object> entry : extraData.entrySet()) {
	            jsonResponse.put(entry.getKey(), entry.getValue());
	        }
	    }
	    
	    PrintWriter out = response.getWriter();
	    out.print(jsonResponse.toString());
	    out.flush();
	}

	private void sendJsonResponse(HttpServletResponse response, boolean success, String message) 
	        throws IOException {
	    sendJsonResponse(response, success, message, null);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Per cancellare cache ed evitare che un utente possa effettuare di nuovo lo
		// stesso ordine
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		// RECUPERO L'ID FORNITO DAL FILTRO
		String cartId = (String) request.getAttribute("cartId");

		try {
			if (cartId != null) {
				List<VoceCarrello> voci = voceDAO.findByCarrello(cartId);

				Map<VoceCarrello, Prodotto> dettagliCarrello = new HashMap<>();
				double subtotale = 0.0;

				for (VoceCarrello voce : voci) {
					Prodotto p = prodottoDAO.findById(voce.getProdottoId());
					if (p != null) {
						dettagliCarrello.put(voce, p);
						subtotale += (p.getPrezzo() * voce.getQuantita());
					}
				}

				double sogliaSpedizioneGratuita = 50.00;
				double costoSpedizioneFisso = 5.90;
				double speseSpedizione = 0.0;

				if (subtotale > 0 && subtotale < sogliaSpedizioneGratuita) {
					speseSpedizione = costoSpedizioneFisso;
				}

				double totaleFinale = subtotale + speseSpedizione;

				request.setAttribute("carrelloMap", dettagliCarrello);
				request.setAttribute("subtotale", subtotale);
				request.setAttribute("speseSpedizione", speseSpedizione);
				request.setAttribute("totaleFinale", totaleFinale);
			}

			request.getRequestDispatcher("/WEB-INF/views/carrello.jsp").forward(request, response);

		} catch (SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Errore durante il caricamento del carrello");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    // RECUPERO L'ID FORNITO DAL FILTRO
	    String cartId = (String) request.getAttribute("cartId");

	    // Verifica se è una richiesta AJAX
	    boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

	    // Sicurezza extra: se per qualche motivo il filtro ha fallito sul DB
	    if (cartId == null) {
	        if (isAjax) {
	            sendJsonResponse(response, false, "Sessione scaduta, effettua il login");
	        } else {
	            response.sendRedirect(request.getContextPath() + "/home");
	        }
	        return;
	    }

	    String action = request.getParameter("action");

	    // Gestione CLEAR (non richiede prodottoId)
	    if ("clear".equals(action)) {
	        try {
	            voceDAO.deleteAllByCarrello(cartId);

	            if (isAjax) {
	                // Ricalcola i totali (carrello vuoto)
	                double nuovoSubtotale = 0.0;
	                double nuoveSpeseSpedizione = 0.0;
	                double nuovoTotaleFinale = 0.0;
	                
	                JSONObject jsonResponse = new JSONObject();
	                jsonResponse.put("success", true);
	                jsonResponse.put("message", "Carrello svuotato con successo");
	                jsonResponse.put("subtotal", nuovoSubtotale);
	                jsonResponse.put("shipping", nuoveSpeseSpedizione);
	                jsonResponse.put("total", nuovoTotaleFinale);
	                
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                PrintWriter out = response.getWriter();
	                out.print(jsonResponse.toString());
	                out.flush();
	            } else {
	                response.sendRedirect(request.getContextPath() + "/carrello");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            if (isAjax) {
	                sendJsonResponse(response, false, "Errore durante lo svuotamento del carrello");
	            } else {
	                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            }
	        }
	        return;
	    }

	    String prodottoIdStr = request.getParameter("prodottoId");

	    if (prodottoIdStr == null || prodottoIdStr.isEmpty()) {
	        if (isAjax) {
	            sendJsonResponse(response, false, "ID prodotto mancante");
	        } else {
	            response.sendRedirect(request.getContextPath() + "/carrello");
	        }
	        return;
	    }

	    try {
	        int prodottoId = Integer.parseInt(prodottoIdStr);
	        Prodotto prodotto = prodottoDAO.findById(prodottoId);

	        if (prodotto == null) {
	            if (isAjax) {
	                sendJsonResponse(response, false, "Prodotto non trovato");
	            } else {
	                response.sendRedirect(request.getContextPath() + "/carrello");
	            }
	            return;
	        }

	        int stockProdotto = prodotto.getStock();

	        if ("add".equals(action) || "update".equals(action)) {
	            int qtaInput = 1;
	            String qtaStr = request.getParameter("quantita");
	            if (qtaStr != null && !qtaStr.isEmpty()) {
	                qtaInput = Integer.parseInt(qtaStr);
	            }

	            List<VoceCarrello> vociEsistenti = voceDAO.findByCarrello(cartId);
	            VoceCarrello voceGiaPresente = null;
	            for (VoceCarrello v : vociEsistenti) {
	                if (v.getProdottoId() == prodottoId) {
	                    voceGiaPresente = v;
	                    break;
	                }
	            }

	            int nuovaQtaFinale;
	            if ("update".equals(action)) {
	                nuovaQtaFinale = qtaInput;
	            } else {
	                int qtaPrecedente = (voceGiaPresente != null) ? voceGiaPresente.getQuantita() : 0;
	                nuovaQtaFinale = qtaPrecedente + qtaInput;
	            }

	            if (nuovaQtaFinale > stockProdotto) {
	                // Risposta di errore
	                if (isAjax) {
	                    JSONObject jsonResponse = new JSONObject();
	                    jsonResponse.put("success", false);
	                    jsonResponse.put("message", "Quantità non disponibile. Massimo disponibile: " + stockProdotto);
	                    
	                    response.setContentType("application/json");
	                    response.setCharacterEncoding("UTF-8");
	                    PrintWriter out = response.getWriter();
	                    out.print(jsonResponse.toString());
	                    out.flush();
	                    return;
	                } else {
	                    request.getSession().setAttribute("erroreAggiunta",
	                            "Quantità non disponibile. Massimo disponibile: " + stockProdotto);
	                    request.getSession().setAttribute("erroreAggiuntaProdottoId", prodottoId);
	                    
	                    String referer = request.getHeader("Referer");
	                    if (referer != null && !referer.contains("/carrello")) {
	                        response.sendRedirect(referer);
	                    } else {
	                        response.sendRedirect(request.getContextPath() + "/carrello");
	                    }
	                    return;
	                }
	            }

	            // Aggiungi o aggiorna la voce
	            if (voceGiaPresente != null) {
	                voceGiaPresente.setQuantita(nuovaQtaFinale);
	                voceDAO.insert(voceGiaPresente);
	            } else {
	                VoceCarrello nuovaVoce = new VoceCarrello(cartId, prodottoId, nuovaQtaFinale);
	                voceDAO.insert(nuovaVoce);
	            }

	            // Risposta AJAX di successo con ricalcolo totali
	            if (isAjax) {
	                // Ricalcola i totali
	                List<VoceCarrello> vociAggiornate = voceDAO.findByCarrello(cartId);
	                double nuovoSubtotale = 0.0;
	                for (VoceCarrello voce : vociAggiornate) {
	                    Prodotto p = prodottoDAO.findById(voce.getProdottoId());
	                    if (p != null) {
	                        nuovoSubtotale += (p.getPrezzo() * voce.getQuantita());
	                    }
	                }
	                
	                double sogliaSpedizioneGratuita = 50.00;
	                double costoSpedizioneFisso = 5.90;
	                double nuoveSpeseSpedizione = 0.0;
	                
	                if (nuovoSubtotale > 0 && nuovoSubtotale < sogliaSpedizioneGratuita) {
	                    nuoveSpeseSpedizione = costoSpedizioneFisso;
	                }
	                
	                double nuovoTotaleFinale = nuovoSubtotale + nuoveSpeseSpedizione;
	                
	                JSONObject jsonResponse = new JSONObject();
	                jsonResponse.put("success", true);
	                jsonResponse.put("message", "Prodotto aggiunto al carrello!");
	                jsonResponse.put("addedQuantity", qtaInput);
	                jsonResponse.put("newQuantity", nuovaQtaFinale);
	                jsonResponse.put("subtotal", nuovoSubtotale);
	                jsonResponse.put("shipping", nuoveSpeseSpedizione);
	                jsonResponse.put("total", nuovoTotaleFinale);
	                
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                PrintWriter out = response.getWriter();
	                out.print(jsonResponse.toString());
	                out.flush();
	                return;
	            }

	        } else if ("remove".equals(action)) {
	            voceDAO.deleteProdotto(cartId, prodottoId);

	            // Risposta AJAX di successo con ricalcolo totali
	            if (isAjax) {
	                // Ricalcola i totali
	                List<VoceCarrello> vociAggiornate = voceDAO.findByCarrello(cartId);
	                double nuovoSubtotale = 0.0;
	                for (VoceCarrello voce : vociAggiornate) {
	                    Prodotto p = prodottoDAO.findById(voce.getProdottoId());
	                    if (p != null) {
	                        nuovoSubtotale += (p.getPrezzo() * voce.getQuantita());
	                    }
	                }
	                
	                double sogliaSpedizioneGratuita = 50.00;
	                double costoSpedizioneFisso = 5.90;
	                double nuoveSpeseSpedizione = 0.0;
	                
	                if (nuovoSubtotale > 0 && nuovoSubtotale < sogliaSpedizioneGratuita) {
	                    nuoveSpeseSpedizione = costoSpedizioneFisso;
	                }
	                
	                double nuovoTotaleFinale = nuovoSubtotale + nuoveSpeseSpedizione;
	                
	                JSONObject jsonResponse = new JSONObject();
	                jsonResponse.put("success", true);
	                jsonResponse.put("message", "Prodotto rimosso dal carrello");
	                jsonResponse.put("subtotal", nuovoSubtotale);
	                jsonResponse.put("shipping", nuoveSpeseSpedizione);
	                jsonResponse.put("total", nuovoTotaleFinale);
	                
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                PrintWriter out = response.getWriter();
	                out.print(jsonResponse.toString());
	                out.flush();
	                return;
	            }
	        }

	        // Gestione Redirect al Referer (solo per richieste non AJAX)
	        if (!isAjax) {
	            String referer = request.getHeader("Referer");
	            if (referer != null && !referer.contains("/carrello")) {
	                response.sendRedirect(referer);
	            } else {
	                response.sendRedirect(request.getContextPath() + "/carrello");
	            }
	        }

	    } catch (SQLException | NumberFormatException e) {
	        e.printStackTrace();
	        if (isAjax) {
	            JSONObject jsonResponse = new JSONObject();
	            jsonResponse.put("success", false);
	            jsonResponse.put("message", "Errore durante l'aggiornamento del carrello");
	            
	            response.setContentType("application/json");
	            response.setCharacterEncoding("UTF-8");
	            PrintWriter out = response.getWriter();
	            out.print(jsonResponse.toString());
	            out.flush();
	        } else {
	            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore aggiornamento carrello");
	        }
	    }
	}
}