document.addEventListener('DOMContentLoaded', function() {
    const btnStampa = document.getElementById('btn-stampa-ricevuta');

    if (btnStampa) {
        btnStampa.addEventListener('click', function(e) {
            e.preventDefault(); // Evita eventuali comportamenti di default del link
            window.print();     // Richiama la finestra di dialogo stampa del browser
        });
    }

});