document.addEventListener("DOMContentLoaded", function() {
    
    const ordinamentoSelect = document.getElementById("ordinamentoSelect");
    const groupAdminId = document.getElementById("group-admin-id");
    const btnReset = document.getElementById("btnResetFilters");

    // Funzione per mostrare/nascondere l'input ID
    function updateFilters() {
        if (ordinamentoSelect && groupAdminId) {
            if (ordinamentoSelect.value === "findById") {
                groupAdminId.classList.remove("filter-hidden");
            } else {
                groupAdminId.classList.add("filter-hidden");
            }
        }
    }

    // Inizializza al caricamento (preserva il form dopo il refresh)
    updateFilters();

    // Ascolta i cambiamenti
    if (ordinamentoSelect) {
        ordinamentoSelect.addEventListener("change", updateFilters);
    }

    // Gestione pulsante Reset
    if (btnReset) {
        btnReset.addEventListener("click", function(e) {
            e.preventDefault();
            window.location.href = this.getAttribute("href");
        });
    }

});