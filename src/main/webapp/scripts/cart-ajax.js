document.addEventListener("DOMContentLoaded", function() {

    var form = document.getElementById("addToCartForm");
    var messageDiv = document.getElementById("cartMessage");
    var btn = document.getElementById("btnAddToCart");
    var btnText = document.querySelector(".btn-text");
    var btnSpinner = document.querySelector(".btn-spinner");
    var quantitaInput = document.getElementById("quantitaInput");

    if (!form) return;

    function showMessage(message, isError) {
        if (!messageDiv) return;

        messageDiv.textContent = message;
        messageDiv.className = "cart-message " + (isError ? "error" : "success");
        messageDiv.style.display = "block";

        setTimeout(function() {
            if (messageDiv) {
                messageDiv.style.display = "none";
            }
        }, 3000);
    }

    function setLoading(loading) {
        if (!btn) return;

        if (loading) {
            btn.disabled = true;
            if (btnText) btnText.style.display = "none";
            if (btnSpinner) btnSpinner.style.display = "inline-block";
            btn.style.opacity = "0.7";
        } else {
            btn.disabled = false;
            if (btnText) btnText.style.display = "inline-block";
            if (btnSpinner) btnSpinner.style.display = "none";
            btn.style.opacity = "1";
        }
    }

    form.addEventListener("submit", function(e) {
        e.preventDefault();

        var actionUrl = form.getAttribute("action");

        if (!actionUrl || actionUrl === "") {
            showMessage("Errore: URL del form non trovato", true);
            return;
        }

        // Prepara i dati manualmente invece di usare FormData
        var prodottoId = document.querySelector("input[name='prodottoId']").value;
        var quantita = quantitaInput.value;
        var action = document.querySelector("input[name='action']").value;

        console.log("Prodotto ID:", prodottoId);
        console.log("Quantita:", quantita);
        console.log("Action:", action);

        if (!prodottoId) {
            showMessage("Errore: ID prodotto non trovato", true);
            return;
        }

        var quantitaNum = parseInt(quantita, 10);
        var maxStock = parseInt(quantitaInput.getAttribute("max") || "99", 10);

        if (isNaN(quantitaNum) || quantitaNum < 1) {
            showMessage("Inserisci una quantit\u00e0 valida (minimo 1)", true);
            return;
        }

        if (quantitaNum > maxStock) {
            showMessage("Quantit\u00e0 massima disponibile: " + maxStock, true);
            return;
        }

        // Crea i parametri per URL encoded
        var params = new URLSearchParams();
        params.append("action", action);
        params.append("prodottoId", prodottoId);
        params.append("quantita", quantitaNum);

        setLoading(true);

        fetch(actionUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "X-Requested-With": "XMLHttpRequest"
            },
            body: params.toString()
        })
            .then(function(response) {
                if (!response.ok) {
                    throw new Error("HTTP error: " + response.status);
                }
                return response.json();
            })
            .then(function(result) {
                if (result.success) {
                    showMessage(result.message || "Prodotto aggiunto al carrello!", false);

                    if (btn) {
                        btn.classList.add("success-animation");
                        setTimeout(function() {
                            btn.classList.remove("success-animation");
                        }, 500);
                    }

                    if (quantitaInput) {
                        quantitaInput.value = 1;
                    }
                } else {
                    showMessage(result.message || "Errore durante l'aggiunta al carrello", true);
                }
            })
            .catch(function(error) {
                console.error("Errore AJAX:", error);
                showMessage("Errore di connessione. Riprova pi\u00f9 tardi.", true);
            })
            .finally(function() {
                setLoading(false);
            });
    });
});