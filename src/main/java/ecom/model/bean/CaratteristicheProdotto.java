package ecom.model.bean;

import java.io.Serializable;

public class CaratteristicheProdotto implements Serializable {
	private int id;
	private int prodottoId;
	private int attributoId;
	private String valoreAttr;

	public CaratteristicheProdotto() {
	}

	public CaratteristicheProdotto(int id, int prodottoId, int attributoId, String valoreAttr) {
		this.id = id;
		this.prodottoId = prodottoId;
		this.attributoId = attributoId;
		this.valoreAttr = valoreAttr;
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

	public int getAttributoId() {
		return attributoId;
	}

	public void setAttributoId(int attributoId) {
		this.attributoId = attributoId;
	}

	public String getValoreAttr() {
		return valoreAttr;
	}

	public void setValoreAttr(String valoreAttr) {
		this.valoreAttr = valoreAttr;
	}
}