document.addEventListener("DOMContentLoaded", function() {
    
    const ordinamentoSelect = document.getElementById("ordinamentoSelect");
    const groupClienteId = document.getElementById("group-cliente-id");
    const btnReset = document.getElementById("btnResetFilters");

    // Funzione per mostrare/nascondere l'input ID
    function updateFilters() {
        if (ordinamentoSelect && groupClienteId) {
            if (ordinamentoSelect.value === "findById") {
                groupClienteId.classList.remove("filter-hidden");
            } else {
                groupClienteId.classList.add("filter-hidden");
            }
        }
    }

    // Inizializza al caricamento della pagina
    updateFilters();

    // Ascolta i cambiamenti sulla select
    if (ordinamentoSelect) {
        ordinamentoSelect.addEventListener("change", updateFilters);
    }

    // Gestione del pulsante Reset
    if (btnReset) {
        btnReset.addEventListener("click", function(e) {
            e.preventDefault();
            window.location.href = this.getAttribute("href");
        });
    }

});