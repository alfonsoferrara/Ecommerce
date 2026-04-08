package ecom.control.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import ecom.model.bean.Attributo;
import ecom.model.bean.Categoria;
import ecom.model.bean.Immagine;
import ecom.model.bean.Prodotto;
import ecom.model.dao.AttributoDAO;
import ecom.model.dao.CaratteristicheProdottoDAO;
import ecom.model.dao.CategoriaDAO;
import ecom.model.dao.ImmagineDAO;
import ecom.model.dao.ProdottoDAO;

@WebServlet("/admin/prodotto")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB (dopo questa soglia scrive su disco temporaneo)
		maxFileSize = 1024 * 1024 * 10, // 10MB (dimensione massima per singolo file)
		maxRequestSize = 1024 * 1024 * 50 // 50MB (dimensione massima totale della richiesta)
)
public class AdminDettagliProdottoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProdottoDAO prodottoDAO;
	private CategoriaDAO categoriaDAO;
	private ImmagineDAO immagineDAO;
	private CaratteristicheProdottoDAO caratteristicheProdottoDAO;
	private AttributoDAO attributoDAO;

	@Override
	public void init() throws ServletException {
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		this.prodottoDAO = new ProdottoDAO(ds);
		this.categoriaDAO = new CategoriaDAO(ds);
		this.immagineDAO = new ImmagineDAO(ds);
		this.caratteristicheProdottoDAO = new CaratteristicheProdottoDAO(ds);
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

		String prodottoId = request.getParameter("id");

		try {
			// SE E' UN NUOVO PRODOTTO
			if (prodottoId == null || prodottoId.isEmpty() || prodottoId.equals("new")) {
				Prodotto prodottoVuoto = new Prodotto();
				prodottoVuoto.setId(0); // ID 0 fa capire alla JSP che è un nuovo inserimento
				prodottoVuoto.setAttivo(true); // Di default un nuovo prodotto è attivo

				request.setAttribute("prodotto", prodottoVuoto);
				request.setAttribute("immagini", new ArrayList<>()); // Nessuna immagine
				request.setAttribute("specifiche", new HashMap<>()); // Nessuna specifica

			}
			// SE E' UNA MODIFICA A UN PRODOTTO ESISTENTE
			else {
				int prodotto_id = Integer.parseInt(prodottoId);
				Prodotto prodotto = prodottoDAO.findById(prodotto_id);

				if (prodotto == null) {
//					request.getSession().setAttribute("errore", "Prodotto non trovato");
					response.sendRedirect(request.getContextPath() + "/admin/prodotti");
					return;
				}

				request.setAttribute("prodotto", prodotto);

				List<Immagine> immagini = immagineDAO.findByProdottoId(prodotto_id);
				request.setAttribute("immagini", immagini);

				Map<String, String> specifiche = caratteristicheProdottoDAO
						.getCaratteristicheMapByProdottoId(prodotto_id);
				request.setAttribute("specifiche", specifiche);
			}

			// RECUPERO CATEGORIE E ATTRIBUTI (Serve in ENTRAMBI i casi)
			List<Categoria> tutteCategorie = categoriaDAO.findAll();
			request.setAttribute("tutteCategorie", tutteCategorie);

			List<Attributo> attributi = attributoDAO.findAll();
			request.setAttribute("tuttiAttributi", attributi);

			request.getRequestDispatcher("/WEB-INF/admin/dettagliProdotto.jsp").forward(request, response);

		} catch (SQLException | NumberFormatException | NullPointerException e) {
			e.printStackTrace();
			request.setAttribute("errore", "Errore nel caricamento del prodotto");
			request.getRequestDispatcher("/admin/prodotti").forward(request, response);
			return;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String prodottoIdStr = request.getParameter("id");
		String action = request.getParameter("action"); // Per distinguere diverse operazioni

		if (prodottoIdStr == null || prodottoIdStr.isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/admin/prodotti");
			return;
		}

		try {
			int prodottoId = Integer.parseInt(prodottoIdStr);

			// Gestione azioni specifiche
			if (action != null) {
				switch (action) {
				case "setPrincipalImage":
					gestisciImpostazioneImmaginePrincipale(request, response, prodottoId);
					return;
				case "deleteImage":
					gestisciEliminazioneImmagine(request, response, prodottoId);
					return;
				case "deleteCharacteristic":
					gestisciEliminazioneCaratteristica(request, response, prodottoId);
					return;
				}
			}

			// Se non è un'azione specifica, procedi con l'aggiornamento completo
			gestisciAggiornamentoCompleto(request, response, prodottoId);

		} catch (SQLException | NumberFormatException e) {
			e.printStackTrace();
			request.getSession().setAttribute("errore", "Errore durante il salvataggio: " + e.getMessage());
			response.sendRedirect(request.getContextPath() + "/admin/prodotto?id=" + prodottoIdStr);
		}
	}

	private void gestisciAggiornamentoCompleto(HttpServletRequest request, HttpServletResponse response, int prodottoId)
            throws ServletException, IOException, SQLException {
        
        Prodotto prodotto;
        boolean isNuovoProdotto = (prodottoId == 0);

        if (isNuovoProdotto) {
            // CREAZIONE
            prodotto = new Prodotto();
            aggiornaDatiBaseProdotto(request, prodotto);
            prodotto.setAttivo(true); // Forziamo attivo alla creazione
            
            // Il DAO deve fare la INSERT e restituire il nuovo ID autogenerato dal Database!
            prodottoId = prodottoDAO.insertAndReturnId(prodotto); 
            
        } else {
            // MODIFICA
            prodotto = prodottoDAO.findById(prodottoId);
            if (prodotto == null) {
//                request.getSession().setAttribute("errore", "Prodotto non trovato");
                response.sendRedirect(request.getContextPath() + "/admin/prodotti");
                return;
            }
            aggiornaDatiBaseProdotto(request, prodotto);
            prodottoDAO.update(prodotto);
        }
        
        gestisciUploadImmagini(request, prodottoId);
        gestisciEliminazioneImmagini(request, prodottoId);
        aggiornaCaratteristicheEsistenti(request, prodottoId);
        aggiungiNuoveCaratteristiche(request, prodottoId);

        request.getSession().setAttribute("messaggio", isNuovoProdotto ? "Prodotto creato con successo!" : "Prodotto aggiornato con successo!");
        response.sendRedirect(request.getContextPath() + "/admin/prodotto?id=" + prodottoId);
    }

	private void aggiornaDatiBaseProdotto(HttpServletRequest request, Prodotto prodotto) {
		String nome = request.getParameter("nome");
		String descrizione = request.getParameter("descrizione");
		String prezzoStr = request.getParameter("prezzo");
		String stockStr = request.getParameter("stock");
		String categoriaIdStr = request.getParameter("categoriaId");
		String attivoStr = request.getParameter("attivo");

		if (nome != null && !nome.trim().isEmpty()) {
			prodotto.setNome(nome);
		}

		prodotto.setDescrizione(descrizione);

		if (prezzoStr != null && !prezzoStr.isEmpty()) {
			prodotto.setPrezzo(Double.parseDouble(prezzoStr));
		}

		if (stockStr != null && !stockStr.isEmpty()) {
			prodotto.setStock(Integer.parseInt(stockStr));
		}

		if (categoriaIdStr != null && !categoriaIdStr.isEmpty()) {
			prodotto.setCategoriaId(Integer.parseInt(categoriaIdStr));
		}

		if (attivoStr != null) {
			prodotto.setAttivo(Boolean.parseBoolean(attivoStr));
		}
	}

	private void gestisciUploadImmagini(HttpServletRequest request, int prodottoId)
			throws IOException, ServletException {
		String applicationPath = request.getServletContext().getRealPath("");
		String uploadFilePath = applicationPath + File.separator + "images";

		File uploadFolder = new File(uploadFilePath);
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}

		Collection<Part> parts = request.getParts();
		for (Part part : parts) {
			if (part.getName().equals("nuoveImmagini") && part.getSize() > 0) {
				String fileName = extractFileName(part);

				if (fileName != null && !fileName.isEmpty()) {
					// Pulisci il nome file da caratteristiche problematiche
					fileName = sanitizeFileName(fileName);
					String uniqueFileName = prodottoId + "_" + System.currentTimeMillis() + "_" + fileName;

					part.write(uploadFilePath + File.separator + uniqueFileName);

					String urlDb = "images/" + uniqueFileName;

					// Verifica se è la prima immagine del prodotto (nessuna immagine esistente)
					try {
						List<Immagine> immaginiEsistenti = immagineDAO.findByProdottoId(prodottoId);
						boolean isPrincipal = immaginiEsistenti.isEmpty();

						Immagine nuovaImm = new Immagine(0, prodottoId, urlDb, isPrincipal);
						immagineDAO.insert(nuovaImm);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void gestisciImpostazioneImmaginePrincipale(HttpServletRequest request, HttpServletResponse response,
			int prodottoId) throws SQLException, IOException {

		String immagineIdStr = request.getParameter("immagineId");
		if (immagineIdStr == null || immagineIdStr.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID immagine mancante");
			return;
		}

		int immagineId = Integer.parseInt(immagineIdStr);

		// Trova tutte le immagini del prodotto
		List<Immagine> immagini = immagineDAO.findByProdottoId(prodottoId);

		// Imposta tutte come non principali
		for (Immagine img : immagini) {
			if (img.isPrincipal()) {
				img.setPrincipal(false);
				immagineDAO.update(img);
			}
		}

		// Imposta quella selezionata come principale
		Immagine immaginePrincipale = immagineDAO.findById(immagineId);
		if (immaginePrincipale != null && immaginePrincipale.getProdottoId() == prodottoId) {
			immaginePrincipale.setPrincipal(true);
			immagineDAO.update(immaginePrincipale);
		}

		request.getSession().setAttribute("messaggio", "Immagine principale aggiornata!");
		response.sendRedirect(request.getContextPath() + "/admin/prodotto?id=" + prodottoId);
	}

	private void gestisciEliminazioneImmagine(HttpServletRequest request, HttpServletResponse response, int prodottoId)
			throws SQLException, IOException, ServletException {

		String immagineIdStr = request.getParameter("immagineId");
		if (immagineIdStr == null || immagineIdStr.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID immagine mancante");
			return;
		}

		int immagineId = Integer.parseInt(immagineIdStr);
		Immagine immagine = immagineDAO.findById(immagineId);

		if (immagine != null && immagine.getProdottoId() == prodottoId) {
			// Elimina il file fisico
			String applicationPath = request.getServletContext().getRealPath("");
			String filePath = applicationPath + File.separator + immagine.getUrl();
			File fileDaEliminare = new File(filePath);
			if (fileDaEliminare.exists()) {
				fileDaEliminare.delete();
			}

			// Verifica se l'immagine da eliminare è quella principale
			boolean wasPrincipal = immagine.isPrincipal();

			// Elimina dal database
			immagineDAO.delete(immagineId);

			// Se era l'immagine principale e ci sono altre immagini, imposta la prima come
			// principale
			if (wasPrincipal) {
				List<Immagine> immaginiRimanenti = immagineDAO.findByProdottoId(prodottoId);
				if (!immaginiRimanenti.isEmpty()) {
					Immagine nuovaPrincipale = immaginiRimanenti.get(0);
					nuovaPrincipale.setPrincipal(true);
					immagineDAO.update(nuovaPrincipale);
				}
			}

			request.getSession().setAttribute("messaggio", "Immagine eliminata con successo!");
		}

		response.sendRedirect(request.getContextPath() + "/admin/prodotto?id=" + prodottoId);
	}

	private void gestisciEliminazioneImmagini(HttpServletRequest request, int prodottoId) throws SQLException {
		String[] immaginiDaEliminare = request.getParameterValues("immaginiDaEliminare");
		if (immaginiDaEliminare != null) {
			for (String immagineIdStr : immaginiDaEliminare) {
				int immagineId = Integer.parseInt(immagineIdStr);
				Immagine immagine = immagineDAO.findById(immagineId);
				if (immagine != null && immagine.getProdottoId() == prodottoId) {
					immagineDAO.delete(immagineId);
				}
			}
		}
	}

	private void aggiornaCaratteristicheEsistenti(HttpServletRequest request, int prodottoId) throws SQLException {
		// Recupera le caratteristiche esistenti dal database
		Map<String, String> caratteristicheEsistenti = caratteristicheProdottoDAO
				.getCaratteristicheMapByProdottoId(prodottoId);

		// Per ogni caratteristica esistente, aggiorna il valore se presente nel form
		for (Map.Entry<String, String> entry : caratteristicheEsistenti.entrySet()) {
			String nomeCaratteristica = entry.getKey();
			String paramName = "spec_" + nomeCaratteristica;
			String nuovoValore = request.getParameter(paramName);

			if (nuovoValore != null && !nuovoValore.trim().isEmpty()) {
				if (!nuovoValore.equals(entry.getValue())) {
					caratteristicheProdottoDAO.updateCaratteristica(prodottoId, nomeCaratteristica, nuovoValore);
				}
			}
		}
	}

	private void aggiungiNuoveCaratteristiche(HttpServletRequest request, int prodottoId) throws SQLException {
		String[] nuoviAttributi = request.getParameterValues("nuovoAttributo");
		String[] nuoviValori = request.getParameterValues("nuovoValore");

		if (nuoviAttributi != null && nuoviValori != null) {
			for (int i = 0; i < nuoviAttributi.length; i++) {
				String attributo = nuoviAttributi[i];
				String valore = nuoviValori[i];

				if (attributo != null && !attributo.trim().isEmpty() && valore != null && !valore.trim().isEmpty()) {

					caratteristicheProdottoDAO.insertOrUpdate(prodottoId, attributo.trim(), valore.trim());
				}
			}
		}
	}

	private void gestisciEliminazioneCaratteristica(HttpServletRequest request, HttpServletResponse response,
			int prodottoId) throws SQLException, IOException {

		String nomeCaratteristica = request.getParameter("nomeCaratteristica");
		if (nomeCaratteristica != null && !nomeCaratteristica.isEmpty()) {
			caratteristicheProdottoDAO.deleteCaratteristica(prodottoId, nomeCaratteristica);
			request.getSession().setAttribute("messaggio", "Caratteristica eliminata con successo!");
		}

		response.sendRedirect(request.getContextPath() + "/admin/prodotto?id=" + prodottoId);
	}

	private String extractFileName(Part part) {
		String contentDisposition = part.getHeader("content-disposition");
		for (String token : contentDisposition.split(";")) {
			if (token.trim().startsWith("filename")) {
				String fileName = token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
				// Rimuovi il percorso se presente (per browser Windows)
				return fileName.substring(fileName.lastIndexOf(File.separator) + 1);
			}
		}
		return null;
	}

	private String sanitizeFileName(String fileName) {
		// Rimuovi caratteri problematici per il filesystem
		return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
	}
}
