document.addEventListener("DOMContentLoaded", function() {
     const ordinamentoSelect = document.getElementById("ordinamentoSelect");

     // Gruppi di filtri
     const groupProdotto = document.getElementById("group-prodotto");
     const groupCategoria = document.getElementById("group-categoria");
     const groupStato = document.getElementById("group-stato");
     const groupStockMin = document.getElementById("group-stock-min");
     const groupStockMax = document.getElementById("group-stock-max");

     function updateFilters() {
         // Nascondi tutti i filtri extra di default
         groupProdotto.classList.add("filter-hidden");
         groupCategoria.classList.add("filter-hidden");
         groupStato.classList.add("filter-hidden");
         groupStockMin.classList.add("filter-hidden");
         groupStockMax.classList.add("filter-hidden");

         // Mostra solo quello selezionato
         const val = ordinamentoSelect.value;
         if (val === "prodottoId") {
             groupProdotto.classList.remove("filter-hidden");
         } else if (val === "categoriaId") {
             groupCategoria.classList.remove("filter-hidden");
         } else if (val === "stato") {
             groupStato.classList.remove("filter-hidden");
         } else if (val === "stock") {
             // Per il range stock mostriamo entrambi gli input
             groupStockMin.classList.remove("filter-hidden");
             groupStockMax.classList.remove("filter-hidden");
         }
     }

     if (ordinamentoSelect) {
         // Esegue al caricamento per mantenere lo stato
         updateFilters();
         // Esegue al cambio di selezione
         ordinamentoSelect.addEventListener("change", updateFilters);
     }
 });