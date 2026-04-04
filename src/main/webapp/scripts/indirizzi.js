document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('addAddressForm');

    if (!form) return;

    const via = document.getElementById('via');
    const civico = document.getElementById('civico');
    const citta = document.getElementById('citta');
    const provincia = document.getElementById('provincia');
    const cap = document.getElementById('cap');

    // Funzioni di supporto DOM
    function showError(input, message) {
        input.classList.add('input-error');
        const errorDiv = input.parentElement.querySelector('.error-message');
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
    }

    function clearError(input) {
        input.classList.remove('input-error');
        const errorDiv = input.parentElement.querySelector('.error-message');
        errorDiv.style.display = 'none';
    }

    // Regole di validazione
    function validateVia() {
        const regex = /^[a-zA-Zàèéìòù\'\s0-9\.]{3,100}$/;
        if (!regex.test(via.value.trim())) {
            showError(via, "Inserisci una via valida (minimo 3 caratteri).");
            return false;
        }
        clearError(via);
        return true;
    }

    function validateCivico() {
        const regex = /^[a-zA-Z0-9\/\s\-]{1,10}$/;
        if (!regex.test(civico.value.trim())) {
            showError(civico, "Civico non valido.");
            return false;
        }
        clearError(civico);
        return true;
    }

    function validateCitta() {
        const regex = /^[a-zA-Zàèéìòù\'\s]{2,50}$/;
        if (!regex.test(citta.value.trim())) {
            showError(citta, "Inserisci una città valida.");
            return false;
        }
        clearError(citta);
        return true;
    }

    function validateProvincia() {
        // Esattamente 2 lettere
        const regex = /^[a-zA-Z]{2}$/;
        if (!regex.test(provincia.value.trim())) {
            showError(provincia, "La provincia deve essere di 2 lettere (es. RM).");
            return false;
        }
        provincia.value = provincia.value.toUpperCase(); // Formatta in automatico
        clearError(provincia);
        return true;
    }

    function validateCap() {
        // Esattamente 5 numeri
        const regex = /^[0-9]{5}$/;
        if (!regex.test(cap.value.trim())) {
            showError(cap, "Il CAP deve contenere esattamente 5 numeri.");
            return false;
        }
        clearError(cap);
        return true;
    }

    // Validazione interattiva (mentre l'utente esce dal campo)
    via.addEventListener('change', validateVia);
    civico.addEventListener('change', validateCivico);
    citta.addEventListener('change', validateCitta);
    provincia.addEventListener('input', validateProvincia); // 'input' per renderlo maiuscolo in tempo reale
    cap.addEventListener('change', validateCap);

    // Validazione al Submit
    form.addEventListener('submit', function(e) {
        const isViaValid = validateVia();
        const isCivicoValid = validateCivico();
        const isCittaValid = validateCitta();
        const isProvinciaValid = validateProvincia();
        const isCapValid = validateCap();

        if (!isViaValid || !isCivicoValid || !isCittaValid || !isProvinciaValid || !isCapValid) {
            e.preventDefault(); // Blocca l'invio se ci sono errori
        }
    });
});