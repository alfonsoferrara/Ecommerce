package ecom.model.bean;

import java.io.Serializable;

public class Indirizzo implements Serializable {
	private int id;
	private int clienteId;
	private String via;
	private String civico;
	private String citta;
	private String provincia;
	private String cap;

	public Indirizzo() {
	}

	public Indirizzo(int id, int clienteId, String via, String civico, String citta, String provincia, String cap) {
		this.id = id;
		this.clienteId = clienteId;
		this.via = via;
		this.civico = civico;
		this.citta = citta;
		this.provincia = provincia;
		this.cap = cap;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getClienteId() {
		return clienteId;
	}

	public void setClienteId(int clienteId) {
		this.clienteId = clienteId;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}
}