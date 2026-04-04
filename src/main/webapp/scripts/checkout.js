document.addEventListener('DOMContentLoaded', function() {
    
    // --- Gestione Metodo di Pagamento e Totali ---
    const metodoSelect = document.getElementById('metodoPagamento');
    const totaleSpan = document.getElementById('totaleFinaleSpan');
    const contrassegnoRow = document.getElementById('contraccoltoRow');
    
    // Queste variabili globali vengono passate dalla JSP nel tag <script> in fondo
    const costoContrassegno = 5.00;

    function aggiornaTotali() {
        if (!metodoSelect) return;
        
        let costoExtra = 0;
        
        if (metodoSelect.value === 'Contrassegno') {
            costoExtra = costoContrassegno;
            contrassegnoRow.style.display = 'flex';
        } else {
            contrassegnoRow.style.display = 'none';
        }

        const nuovoTotale = window.totaleBase + costoExtra;
        totaleSpan.innerHTML = '€ ' + nuovoTotale.toFixed(2);
    }

    if (metodoSelect) {
        metodoSelect.addEventListener('change', aggiornaTotali);
    }

    // --- Gestione Toggle Indirizzo ---
    const usaEsistente = document.getElementById('usaEsistente');
    const usaNuovo = document.getElementById('usaNuovo');
    const nuovoDiv = document.getElementById('nuovoIndirizzo');
    const selectIndirizzo = document.querySelector('select[name="indirizzoId"]');

    if (usaEsistente && usaNuovo && nuovoDiv) {
        function toggleIndirizzo() {
            const nuoviCampi = nuovoDiv.querySelectorAll('input');
            
            if (usaEsistente.checked) {
                nuovoDiv.style.display = 'none';
                if (selectIndirizzo) selectIndirizzo.required = true;
                nuoviCampi.forEach(input => input.required = false);
            } else {
                nuovoDiv.style.display = 'block';
                if (selectIndirizzo) selectIndirizzo.required = false;
                nuoviCampi.forEach(input => {
                    if (input.name) input.required = true;
                });
            }
        }

        usaEsistente.addEventListener('change', toggleIndirizzo);
        usaNuovo.addEventListener('change', toggleIndirizzo);
        
        // Esecuzione iniziale
        toggleIndirizzo();
    }
});