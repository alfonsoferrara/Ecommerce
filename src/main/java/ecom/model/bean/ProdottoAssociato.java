package ecom.model.bean;

public class ProdottoAssociato {
	private int prodottoId;
	private String prodottoNome;
	private String valoreAttr;

	// Costruttori
	public ProdottoAssociato() {
	}

	public ProdottoAssociato(int prodottoId, String prodottoNome, String valoreAttr) {
		this.prodottoId = prodottoId;
		this.prodottoNome = prodottoNome;
		this.valoreAttr = valoreAttr;
	}

	// Getter e Setter
	public int getProdottoId() {
		return prodottoId;
	}

	public void setProdottoId(int prodottoId) {
		this.prodottoId = prodottoId;
	}

	public String getProdottoNome() {
		return prodottoNome;
	}

	public void setProdottoNome(String prodottoNome) {
		this.prodottoNome = prodottoNome;
	}

	public String getValoreAttr() {
		return valoreAttr;
	}

	public void setValoreAttr(String valoreAttr) {
		this.valoreAttr = valoreAttr;
	}
}
