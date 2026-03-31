package ecom.model.bean;

import java.io.Serializable;

public class DettagliOrdine implements Serializable {
	private int ordineId;
	private int prodottoId;
	private int quantita;
	private double prezzoUnitario;

	public DettagliOrdine() {
	}

	public DettagliOrdine(int ordineId, int prodottoId, int quantita, double prezzoUnitario) {
		this.ordineId = ordineId;
		this.prodottoId = prodottoId;
		this.quantita = quantita;
		this.prezzoUnitario = prezzoUnitario;
	}

	public int getOrdineId() {
		return ordineId;
	}

	public void setOrdineId(int ordineId) {
		this.ordineId = ordineId;
	}

	public int getProdottoId() {
		return prodottoId;
	}

	public void setProdottoId(int prodottoId) {
		this.prodottoId = prodottoId;
	}

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public double getPrezzoUnitario() {
		return prezzoUnitario;
	}

	public void setPrezzoUnitario(double prezzoUnitario) {
		this.prezzoUnitario = prezzoUnitario;
	}
}