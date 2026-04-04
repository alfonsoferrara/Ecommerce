document.addEventListener('DOMContentLoaded', function() {
    
    const form = document.getElementById('infoForm');
    if (!form) return;

    // --- Toggle Password ---
    const togglePassword = document.getElementById('togglePassword');
    const passwordInput = document.getElementById('password');

    if (togglePassword && passwordInput) {
        togglePassword.addEventListener('click', function () {
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            this.classList.toggle('fa-eye');
            this.classList.toggle('fa-eye-slash');
        });
    }

    // --- Validazione Form ---
    const nome = document.getElementById('nome');
    const cognome = document.getElementById('cognome');
    const email = document.getElementById('email');
    const telefono = document.getElementById('telefono');

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

    function validateNome() {
        const regex = /^[a-zA-Zàèéìòù\'\s]{2,50}$/;
        if (!regex.test(nome.value.trim())) {
            showError(nome, "Inserisci un nome valido (min 2 lettere).");
            return false;
        }
        clearError(nome);
        return true;
    }

    function validateCognome() {
        const regex = /^[a-zA-Zàèéìòù\'\s]{2,50}$/;
        if (!regex.test(cognome.value.trim())) {
            showError(cognome, "Inserisci un cognome valido (min 2 lettere).");
            return false;
        }
        clearError(cognome);
        return true;
    }

    function validateEmail() {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!regex.test(email.value.trim())) {
            showError(email, "Formato email non valido.");
            return false;
        }
        clearError(email);
        return true;
    }

    function validateTelefono() {
        // Se il telefono è vuoto lo consideriamo valido (opzionale)
        if (telefono.value.trim() === "") {
            clearError(telefono);
            return true;
        }
        
        // Se l'utente scrive qualcosa, deve essere corretto (9-10 cifre)
        const regex = /^[0-9]{9,10}$/;
        if (!regex.test(telefono.value.trim())) {
            showError(telefono, "Inserisci un numero valido (9-10 cifre) o lascia vuoto.");
            return false;
        }
        clearError(telefono);
        return true;
    }

    function validatePassword() {
        // La password è opzionale: se vuota non cambierà nel DB
        if (passwordInput.value === "") {
            clearError(passwordInput);
            return true;
        }

        // Se l'utente decide di cambiarla, deve rispettare le regole di sicurezza
        const passRegex = /^(?=.*[0-9]).{6,}$/;
        if (!passRegex.test(passwordInput.value)) {
            showError(passwordInput, "La nuova password deve avere almeno 6 caratteri e 1 numero.");
            return false;
        }
        clearError(passwordInput);
        return true;
    }

    // Listener interattivi
    nome.addEventListener('change', validateNome);
    cognome.addEventListener('change', validateCognome);
    email.addEventListener('change', validateEmail);
    telefono.addEventListener('change', validateTelefono);
    passwordInput.addEventListener('change', validatePassword);

    // Listener al Submit
    form.addEventListener('submit', function(e) {
        const isNomeValid = validateNome();
        const isCognomeValid = validateCognome();
        const isEmailValid = validateEmail();
        const isPhoneValid = validateTelefono();
        const isPassValid = validatePassword();

        if (!isNomeValid || !isCognomeValid || !isEmailValid || !isPhoneValid || !isPassValid) {
            e.preventDefault();
        }
    });
});