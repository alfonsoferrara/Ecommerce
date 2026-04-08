CREATE schema ecommerce;
use ecommerce;

CREATE TABLE Utente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(191) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE Cliente (
    utente_id INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    CONSTRAINT fk_cliente_utente FOREIGN KEY (utente_id) 
        REFERENCES Utente(id) ON DELETE CASCADE
);

CREATE TABLE Admin (
    utente_id INT PRIMARY KEY,
    data_creazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_admin_utente FOREIGN KEY (utente_id) 
        REFERENCES Utente(id) ON DELETE CASCADE
);

CREATE TABLE Indirizzo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    via VARCHAR(150) NOT NULL,
    civico VARCHAR(20) NOT NULL,
    citta VARCHAR(100) NOT NULL,
    provincia VARCHAR(2) NOT NULL,
    cap VARCHAR(10) NOT NULL,
    CONSTRAINT fk_indirizzo_cliente FOREIGN KEY (cliente_id) 
        REFERENCES Cliente(utente_id) ON DELETE CASCADE
);

CREATE TABLE Categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descrizione TEXT
);

CREATE TABLE Prodotto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    categoria_id INT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    descrizione TEXT,
    prezzo DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0,
    attivo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_prodotto_categoria FOREIGN KEY (categoria_id) 
        REFERENCES Categoria(id) ON DELETE RESTRICT
);

CREATE TABLE Immagine (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prodotto_id INT NOT NULL,
    url VARCHAR(255) NOT NULL,
    is_principal BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_immagine_prodotto FOREIGN KEY (prodotto_id) 
        REFERENCES Prodotto(id) ON DELETE CASCADE
);

CREATE TABLE Attributo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE Caratteristiche_Prodotto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prodotto_id INT NOT NULL,
    attributo_id INT NOT NULL,
    valoreAttr VARCHAR(255) NOT NULL,
    CONSTRAINT fk_caratt_prodotto FOREIGN KEY (prodotto_id) 
        REFERENCES Prodotto(id) ON DELETE CASCADE,
    CONSTRAINT fk_caratt_attributo FOREIGN KEY (attributo_id) 
        REFERENCES Attributo(id) ON DELETE CASCADE,
    UNIQUE (prodotto_id, attributo_id)
);

CREATE TABLE Recensione (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prodotto_id INT NOT NULL,
    cliente_id INT NOT NULL,
    valutazione TINYINT CHECK (valutazione BETWEEN 1 AND 5), /*Un INT standard occupa 4 byte. Un TINYINT occupa solo 1 byte.*/
    titolo TEXT,
    commento TEXT,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recensione_prodotto FOREIGN KEY (prodotto_id) 
        REFERENCES Prodotto(id) ON DELETE CASCADE,
    CONSTRAINT fk_recensione_cliente FOREIGN KEY (cliente_id) 
        REFERENCES Cliente(utente_id) ON DELETE CASCADE
);

CREATE TABLE Carrello (
    id VARCHAR(36) PRIMARY KEY, 
    cliente_id INT UNIQUE DEFAULT NULL,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_carrello_cliente FOREIGN KEY (cliente_id) 
        REFERENCES Cliente(utente_id) ON DELETE CASCADE
);

CREATE TABLE Voce_Carrello (
    carrello_id VARCHAR(36) NOT NULL,
    prodotto_id INT NOT NULL,
    quantita INT NOT NULL DEFAULT 1,
    PRIMARY KEY (carrello_id, prodotto_id),
    CONSTRAINT fk_voce_carrello FOREIGN KEY (carrello_id) 
        REFERENCES Carrello(id) ON DELETE CASCADE,
    CONSTRAINT fk_voce_prodotto FOREIGN KEY (prodotto_id) 
        REFERENCES Prodotto(id) ON DELETE CASCADE
);

CREATE TABLE Ordine (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    indirizzo_id INT NOT NULL,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    totale DECIMAL(10, 2) NOT NULL,
    stato ENUM('IN ELABORAZIONE', 'SPEDITO', 'CONSEGNATO', 'ANNULLATO') DEFAULT 'IN ELABORAZIONE',
    metodo_pagamento VARCHAR(50) NOT NULL,
    nota_cliente TEXT,
    CONSTRAINT fk_ordine_cliente FOREIGN KEY (cliente_id) 
        REFERENCES Cliente(utente_id) ON DELETE RESTRICT,
    CONSTRAINT fk_ordine_indirizzo FOREIGN KEY (indirizzo_id) 
        REFERENCES Indirizzo(id) ON DELETE RESTRICT
);

CREATE TABLE Dettagli_Ordine (
    ordine_id INT NOT NULL,
    prodotto_id INT NOT NULL,
    quantita INT NOT NULL,
    prezzo_unitario DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (ordine_id, prodotto_id),
    CONSTRAINT fk_dettagli_ordine FOREIGN KEY (ordine_id) 
        REFERENCES Ordine(id) ON DELETE CASCADE,
    CONSTRAINT fk_dettagli_prodotto FOREIGN KEY (prodotto_id) 
        REFERENCES Prodotto(id) ON DELETE RESTRICT
        /*
        VINCOLO PROGETTO: Se l'amministratore cancella un prodotto, non deve scomparire dagli ordini effettuati di nessun cliente
        Per evitarlo uso soft delete, cioe' lo stato di un prodotto diventa non attivo invece di essere cancellato, in tal modo
        il prodotto resta disponibile per essere mostrato nei dettagli dell'ordine.
        */
);