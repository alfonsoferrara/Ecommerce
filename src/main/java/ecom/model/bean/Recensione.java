package ecom.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Recensione implements Serializable {
	private int id;
	private int prodottoId;
	private int clienteId;
	private int valutazione; // TINYINT mappato come int
	private String titolo;
	private String commento;
	private Timestamp data;

	public Recensione() {
	}

	public Recensione(int id, int prodottoId, int clienteId, int valutazione, String titolo, String commento,
			Timestamp data) {
		this.id = id;
		this.prodottoId = prodottoId;
		this.clienteId = clienteId;
		this.valutazione = valutazione;
		this.titolo = titolo;
		this.commento = commento;
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProdottoId() {
		return prodottoId;
	}

	public void setProdottoId(int prodottoId) {
		this.prodottoId = prodottoId;
	}

	public int getClienteId() {
		return clienteId;
	}

	public void setClienteId(int clienteId) {
		this.clienteId = clienteId;
	}

	public int getValutazione() {
		return valutazione;
	}

	public void setValutazione(int valutazione) {
		this.valutazione = valutazione;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getCommento() {
		return commento;
	}

	public void setCommento(String commento) {
		this.commento = commento;
	}

	public Timestamp getData() {
		return data;
	}

	public void setData(Timestamp data) {
		this.data = data;
	}
}