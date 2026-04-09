document.addEventListener("DOMContentLoaded", function() {
    
    // Gestione del pulsante Reset
    const btnReset = document.getElementById("btnResetFilters");
    
    if (btnReset) {
        btnReset.addEventListener("click", function(e) {
            e.preventDefault();
            // Reindirizza alla pagina base delle categorie pulendo tutti i parametri GET
            window.location.href = this.getAttribute("href");
        });
    }

});