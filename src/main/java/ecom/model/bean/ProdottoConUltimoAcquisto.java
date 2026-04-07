package ecom.model.bean;

import java.time.LocalDate;

public class ProdottoConUltimoAcquisto {
	private Prodotto prodotto;
	private LocalDate ultimoAcquisto;

	// costruttore, getter e setter
	public ProdottoConUltimoAcquisto(Prodotto prodotto, LocalDate ultimoAcquisto) {
		this.prodotto = prodotto;
		this.ultimoAcquisto = ultimoAcquisto;
	}

	public Prodotto getProdotto() {
		return prodotto;
	}

	public LocalDate getUltimoAcquisto() {
		return ultimoAcquisto;
	}
}