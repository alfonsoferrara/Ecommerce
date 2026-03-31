package ecom.model.bean;

import java.io.Serializable;

public class Prodotto implements Serializable {
	private int id;
	private int categoriaId;
	private String nome;
	private String descrizione;
	private double prezzo;
	private int stock;
	private boolean attivo; // Per il Soft Delete

	public Prodotto() {
	}

	public Prodotto(int id, int categoriaId, String nome, String descrizione, double prezzo, int stock,
			boolean attivo) {
		this.id = id;
		this.categoriaId = categoriaId;
		this.nome = nome;
		this.descrizione = descrizione;
		this.prezzo = prezzo;
		this.stock = stock;
		this.attivo = attivo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(int categoriaId) {
		this.categoriaId = categoriaId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public boolean isAttivo() {
		return attivo;
	}

	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}
}