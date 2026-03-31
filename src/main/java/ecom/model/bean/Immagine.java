package ecom.model.bean;

import java.io.Serializable;

public class Immagine implements Serializable {
	private int id;
	private int prodottoId;
	private String url;
	private boolean principal;

	public Immagine() {
	}

	public Immagine(int id, int prodottoId, String url, boolean principal) {
		this.id = id;
		this.prodottoId = prodottoId;
		this.url = url;
		this.principal = principal;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}
}