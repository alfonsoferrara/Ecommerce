document.addEventListener("DOMContentLoaded", function() {

    const ordinamentoSelect = document.getElementById("ordinamentoSelect");
    const groupAttributoId = document.getElementById("group-attributo-id");

    // Funzione per mostrare/nascondere l'input ID
    function updateFilters() {
        if (ordinamentoSelect && groupAttributoId) {
            if (ordinamentoSelect.value === "findById") {
                groupAttributoId.classList.remove("filter-hidden");
            } else {
                groupAttributoId.classList.add("filter-hidden");
            }
        }
    }

    // Inizializza al caricamento della pagina (per mantenere i filtri applicati)
    updateFilters();

    // Ascolta i cambiamenti sulla select
    if (ordinamentoSelect) {
        ordinamentoSelect.addEventListener("change", updateFilters);
    }
});