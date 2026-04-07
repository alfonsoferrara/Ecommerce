document.addEventListener("DOMContentLoaded", function() {
    const ordinamentoSelect = document.getElementById("ordinamentoSelect");

    // Gruppi di filtri
    const groupCliente = document.getElementById("group-cliente");
    const groupOrdine = document.getElementById("group-ordine");
    const groupPagamento = document.getElementById("group-pagamento");
    const groupStato = document.getElementById("group-stato");
    const groupDateInizio = document.getElementById("group-date-inizio");
    const groupDateFine = document.getElementById("group-date-fine");

    function updateFilters() {
        // Nascondi tutti i filtri extra di default
        groupCliente.classList.add("filter-hidden");
        groupOrdine.classList.add("filter-hidden");
        groupPagamento.classList.add("filter-hidden");
        groupStato.classList.add("filter-hidden");
        groupDateInizio.classList.add("filter-hidden");
        groupDateFine.classList.add("filter-hidden");

        // Mostra solo quello selezionato
        const val = ordinamentoSelect.value;
        if (val === "clienteID") {
            groupCliente.classList.remove("filter-hidden");
        } else if (val === "ordineId") {
            groupOrdine.classList.remove("filter-hidden");
        } else if (val === "metodoPagamento") {
            groupPagamento.classList.remove("filter-hidden");
        } else if (val === "stato") {
            groupStato.classList.remove("filter-hidden");
        } else if (val === "rangeData") {
            groupDateInizio.classList.remove("filter-hidden");
            groupDateFine.classList.remove("filter-hidden");
        }
    }

    if (ordinamentoSelect) {
        // Aggiorna al caricamento della pagina (per mantenere visibile il filtro dopo il submit)
        updateFilters();
        // Aggiorna al cambio di selezione
        ordinamentoSelect.addEventListener("change", updateFilters);
    }
});