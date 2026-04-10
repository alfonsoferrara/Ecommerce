package ecom.model.bean;

import java.util.List;
import java.util.ArrayList;

public class AttributoDettagli {
	private int id;
	private String nome;
	private List<ProdottoAssociato> prodottiAssociati;

	// Costruttori
	public AttributoDettagli() {
		this.prodottiAssociati = new ArrayList<>();
	}

	public AttributoDettagli(int id, String nome, List<ProdottoAssociato> prodottiAssociati) {
		this.id = id;
		this.nome = nome;
		this.prodottiAssociati = prodottiAssociati;
	}

	// Getter e Setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<ProdottoAssociato> getProdottiAssociati() {
		return prodottiAssociati;
	}

	public void setProdottiAssociati(List<ProdottoAssociato> prodottiAssociati) {
		this.prodottiAssociati = prodottiAssociati;
	}

	// Metodo utile per verificare se l'attributo ha prodotti associati
	public boolean hasProdotti() {
		return prodottiAssociati != null && !prodottiAssociati.isEmpty();
	}
}