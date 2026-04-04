document.addEventListener('DOMContentLoaded', function() {
    
    // Rende cliccabile l'intera riga della tabella degli ordini
    const rows = document.querySelectorAll('.clickable-row');
    
    rows.forEach(row => {
        row.addEventListener('click', function(e) {
            // Se l'utente ha cliccato direttamente sul bottone "Dettagli", 
            // il browser gestirà il link da solo. Evitiamo doppi click.
            if (e.target.tagName.toLowerCase() === 'a') {
                return;
            }
            
            // Recupera l'URL nascosto nell'attributo data-href e naviga
            const href = this.getAttribute('data-href');
            if (href) {
                window.location.href = href;
            }
        });
    });

});