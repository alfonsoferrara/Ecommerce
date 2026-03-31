package ecom.model.bean;

import java.io.Serializable;

public class VoceCarrello implements Serializable {
	private String carrelloId;
	private int prodottoId;
	private int quantita;

	public VoceCarrello() {
	}

	public VoceCarrello(String carrelloId, int prodottoId, int quantita) {
		this.carrelloId = carrelloId;
		this.prodottoId = prodottoId;
		this.quantita = quantita;
	}

	public String getCarrelloId() {
		return carrelloId;
	}

	public void setCarrelloId(String carrelloId) {
		this.carrelloId = carrelloId;
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
}