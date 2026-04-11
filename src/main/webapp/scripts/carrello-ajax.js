document.addEventListener("DOMContentLoaded", function() {

    // Base URL per le chiamate AJAX
    var baseUrl = window.location.origin + "/ecommerce_tsw";

    // Funzione per mostrare messaggi
    function showMessage(message, isError) {
        var messageDiv = document.getElementById("cartMessage");
        if (!messageDiv) return;

        messageDiv.textContent = message;
        if (isError) {
            messageDiv.className = "cart-message error";
        } else {
            messageDiv.className = "cart-message success";
        }
        messageDiv.style.display = "block";

        setTimeout(function() {
            messageDiv.style.display = "none";
        }, 3000);
    }

    // Funzione per aggiornare il riepilogo
    function updateSummary(subtotal, shipping, total, isFreeShipping) {
        var subtotalElement = document.getElementById("subtotalValue");
        var shippingElement = document.getElementById("shippingValue");
        var totalElement = document.getElementById("totalValue");
        var shippingInfo = document.getElementById("shippingInfo");

        if (subtotalElement) {
            subtotalElement.textContent = "€ " + subtotal.toFixed(2);
        }

        if (shippingElement) {
            if (isFreeShipping) {
                shippingElement.textContent = "Gratuita";
                shippingElement.style.color = "#28a745";
                shippingElement.style.fontWeight = "bold";
            } else {
                shippingElement.textContent = "€ " + shipping.toFixed(2);
                shippingElement.style.color = "";
                shippingElement.style.fontWeight = "";
            }
        }

        if (totalElement) {
            totalElement.textContent = "€ " + total.toFixed(2);
        }

        if (shippingInfo) {
            if (isFreeShipping) {
                shippingInfo.textContent = "Spedizione gratuita";
                shippingInfo.style.color = "#28a745";
            } else {
                shippingInfo.textContent = "Spedizione gratuita sopra i 50\u20ac";
                shippingInfo.style.color = "#666";
            }
        }
    }

    // Funzione per aggiornare il totale di una singola riga
    function updateRowTotal(productId, price, quantity) {
        var totalElement = document.getElementById("total-" + productId);
        if (totalElement) {
            var newTotal = price * quantity;
            totalElement.textContent = "€ " + newTotal.toFixed(2);
        }
    }

    // Gestione aggiornamento quantit\u00e0
    var quantityInputs = document.querySelectorAll(".quantity-update");
    for (var i = 0;i < quantityInputs.length;i++) {
        var input = quantityInputs[i];

        input.addEventListener("change", function() {
            var productId = this.dataset.productId;
            var productPrice = parseFloat(this.dataset.productPrice);
            var newQuantity = parseInt(this.value, 10);
            var oldQuantity = parseInt(this.defaultValue, 10);

            if (isNaN(newQuantity) || newQuantity < 1) {
                this.value = oldQuantity;
                showMessage("La quantit\u00e0 deve essere almeno 1", true);
                return;
            }

            if (newQuantity > 99) {
                this.value = oldQuantity;
                showMessage("Quantit\u00e0 massima consentita: 99", true);
                return;
            }

            var self = this;
            self.disabled = true;

            var formData = new URLSearchParams();
            formData.append("action", "update");
            formData.append("prodottoId", productId);
            formData.append("quantita", newQuantity);

            fetch(baseUrl + "/carrello", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "X-Requested-With": "XMLHttpRequest"
                },
                body: formData.toString()
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(data) {
                    if (data.success) {
                        self.defaultValue = newQuantity;
                        updateRowTotal(productId, productPrice, newQuantity);

                        if (data.subtotal !== undefined && data.shipping !== undefined && data.total !== undefined) {
                            updateSummary(data.subtotal, data.shipping, data.total, data.shipping === 0);
                        }

                        showMessage("Quantit\u00e0 aggiornata con successo", false);
                    } else {
                        showMessage(data.message || "Errore durante l'aggiornamento", true);
                        self.value = oldQuantity;
                    }
                })
                .catch(function(error) {
                    console.error("Errore:", error);
                    showMessage("Errore di connessione", true);
                    self.value = oldQuantity;
                })
                .finally(function() {
                    self.disabled = false;
                });
        });
    }

    // Gestione rimozione prodotto
    var removeButtons = document.querySelectorAll(".remove-item");
    for (var j = 0;j < removeButtons.length;j++) {
        var button = removeButtons[j];

        button.addEventListener("click", function() {
            var productId = this.dataset.productId;
            var productName = this.dataset.productName;

            if (!confirm("Rimuovere " + productName + " dal carrello?")) {
                return;
            }

            var self = this;
            self.disabled = true;

            var formData = new URLSearchParams();
            formData.append("action", "remove");
            formData.append("prodottoId", productId);

            fetch(baseUrl + "/carrello", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "X-Requested-With": "XMLHttpRequest"
                },
                body: formData.toString()
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(data) {
                    if (data.success) {
                        var row = document.getElementById("product-row-" + productId);
                        if (row) {
                            row.remove();
                        }

                        if (data.subtotal !== undefined && data.shipping !== undefined && data.total !== undefined) {
                            updateSummary(data.subtotal, data.shipping, data.total, data.shipping === 0);
                        }

                        showMessage("Prodotto rimosso dal carrello", false);

                        var remainingRows = document.querySelectorAll("#cartBody tr").length;
                        if (remainingRows === 0) {
                            window.location.reload();
                        }
                    } else {
                        showMessage(data.message || "Errore durante la rimozione", true);
                    }
                })
                .catch(function(error) {
                    console.error("Errore:", error);
                    showMessage("Errore di connessione", true);
                })
                .finally(function() {
                    self.disabled = false;
                });
        });
    }

    // Gestione svuota carrello
    var clearCartBtn = document.getElementById("clearCartBtn");
    if (clearCartBtn) {
        clearCartBtn.addEventListener("click", function() {
            if (!confirm("Sei sicuro di voler svuotare l'intero carrello?")) {
                return;
            }

            var self = this;
            self.disabled = true;

            var formData = new URLSearchParams();
            formData.append("action", "clear");

            fetch(baseUrl + "/carrello", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "X-Requested-With": "XMLHttpRequest"
                },
                body: formData.toString()
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(data) {
                    if (data.success) {
                        showMessage("Carrello svuotato con successo", false);
                        setTimeout(function() {
                            window.location.reload();
                        }, 1000);
                    } else {
                        showMessage(data.message || "Errore durante lo svuotamento", true);
                    }
                })
                .catch(function(error) {
                    console.error("Errore:", error);
                    showMessage("Errore di connessione", true);
                })
                .finally(function() {
                    self.disabled = false;
                });
        });
    }

    // Nascondi il messaggio di errore di sessione dopo 3 secondi
    var sessionError = document.getElementById("sessionError");
    if (sessionError) {
        setTimeout(function() {
            sessionError.style.display = "none";
        }, 3000);
    }
});