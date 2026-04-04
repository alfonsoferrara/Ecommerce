<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<footer class="footer">
    <div class="footer-container">
        <!-- Sezione 1: Customer Reassurance -->
        <div class="reassurance">
            <div class="reassurance-item">
                <div class="reassurance-icon">
                    <i class="fas fa-headset"></i>
                </div>
                <div class="reassurance-title">ASSISTENZA CLIENTI</div>
                <div class="reassurance-text">Disponibile 7/7 dalle 9:00 alle 20:00</div>
            </div>
            <div class="reassurance-item">
                <div class="reassurance-icon">
                    <i class="fas fa-gem"></i>
                </div>
                <div class="reassurance-title">TOP QUALITÀ</div>
                <div class="reassurance-text">Prodotti selezionati con cura</div>
            </div>
            <div class="reassurance-item">
                <div class="reassurance-icon">
                    <i class="fas fa-undo-alt"></i>
                </div>
                <div class="reassurance-title">RESO GRATUITO</div>
                <div class="reassurance-text">Hai 14 giorni per ripensarci</div>
            </div>
        </div>
        
        <!-- Sezione 2: Link utili -->
        <div class="footer-links">
            <a href="${pageContext.request.contextPath}/chi-siamo">Chi siamo</a>
            <a href="${pageContext.request.contextPath}/contattaci">Contattaci</a>
            <a href="${pageContext.request.contextPath}/info-spedizioni">Info spedizioni</a>
            <a href="${pageContext.request.contextPath}/privacy-policy">Privacy Policy</a>
            <a href="${pageContext.request.contextPath}/termini-condizioni">Termini e condizioni</a>
        </div>
        
        <!-- Sezione 3: Copyright -->
        <div class="copyright">
            <p>
                &copy; <span id="currentYear">2026</span> SHOP. - Tutti i diritti riservati<br>
                P.IVA 01234567890
            </p>
        </div>
    </div>
</footer>

<script>
    // Aggiorna anno corrente
    document.getElementById('currentYear').textContent = new Date().getFullYear();
</script>