<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<footer>
    <div class="reassurance-section">
        <div class="reassurance-block">
            <i class="fas fa-headset"></i>
            <h4>Assistenza Clienti 24/7</h4>
            <p>Siamo qui per aiutarti in qualsiasi momento.</p>
        </div>
        <div class="reassurance-block">
            <i class="fas fa-star"></i>
            <h4>Top Qualità</h4>
            <p>Materiali selezionati e garantiti.</p>
        </div>
        <div class="reassurance-block">
            <i class="fas fa-box-open"></i>
            <h4>Reso Gratuito</h4>
            <p>Hai 30 giorni per restituire il tuo ordine.</p>
        </div>
    </div>

    <div class="footer-links-section">
        <div class="link-column">
            <h5>L'Azienda</h5>
            <a href="#">Chi siamo</a>
            <a href="#">Lavora con noi</a>
            <a href="#">Sostenibilità</a>
        </div>
        <div class="link-column">
            <h5>Supporto</h5>
            <a href="#">Contattaci</a>
            <a href="#">FAQ</a>
            <a href="#">Info Spedizioni</a>
        </div>
        <div class="link-column">
            <h5>Note Legali</h5>
            <a href="#">Privacy Policy</a>
            <a href="#">Termini & Condizioni</a>
            <a href="#">Cookie Policy</a>
        </div>
    </div>

    <div class="copyright-section">
        <% 
            // Generazione dinamica dell'anno corrente
            int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR); 
        %>
        &copy; <%= currentYear %> CarryCrew S.r.l. - Tutti i diritti riservati. <br>
        P.IVA: 12345678901 - Via Roma 1, 00100 Roma (RM) - info@carrycrew.it
    </div>
</footer>

<script src="${pageContext.request.contextPath}/scripts/main.js"></script>

</body>
</html>