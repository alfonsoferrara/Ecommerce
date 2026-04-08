document.addEventListener("DOMContentLoaded", function() {

    // 1. Logica per aggiungere nuove righe di caratteristiche
    const btnAddSpec = document.getElementById("btnAddSpec");
    const newSpecsContainer = document.getElementById("new-specs-container");

    if (btnAddSpec && newSpecsContainer) {
        // Prendo il primo elemento del contenitore e lo clono
        const templateRow = newSpecsContainer.querySelector(".spec-new-row");

        btnAddSpec.addEventListener("click", function() {
            const newRow = templateRow.cloneNode(true);
            // Pulisco il valore dell'input testuale clonato
            newRow.querySelector("input").value = "";
            newSpecsContainer.appendChild(newRow);
        });
    }
});

// Funzioni Globali per le Azioni Rapide (Eseguite fuori dal form principale)

function setPrincipalImage(imgId) {
    document.getElementById("hiddenPrincipalImgId").value = imgId;
    document.getElementById("formSetPrincipal").submit();
}

function deleteImage(imgId) {
    if (confirm("Sei sicuro di voler eliminare definitivamente questa immagine?")) {
        document.getElementById("hiddenDeleteImgId").value = imgId;
        document.getElementById("formDeleteImage").submit();
    }
}

function deleteSpec(specName) {
    if (confirm("Sei sicuro di voler eliminare la caratteristica '" + specName + "'?")) {
        document.getElementById("hiddenDeleteSpecName").value = specName;
        document.getElementById("formDeleteSpec").submit();
    }
}