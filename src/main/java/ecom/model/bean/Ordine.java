package ecom.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Ordine implements Serializable {
	private int id;
	private int clienteId;
	private int indirizzoId;
	private Timestamp data;
	private double totale;
	private String stato;
	private String metodoPagamento;
	private String notaCliente;

	public Ordine() {
	}

	public Ordine(int id, int clienteId, int indirizzoId, Timestamp data, double totale, String stato,
			String metodoPagamento, String notaCliente) {
		this.id = id;
		this.clienteId = clienteId;
		this.indirizzoId = indirizzoId;
		this.data = data;
		this.totale = totale;
		this.stato = stato;
		this.metodoPagamento = metodoPagamento;
		this.notaCliente = notaCliente;
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

	public int getIndirizzoId() {
		return indirizzoId;
	}

	public void setIndirizzoId(int indirizzoId) {
		this.indirizzoId = indirizzoId;
	}

	public Timestamp getData() {
		return data;
	}

	public void setData(Timestamp data) {
		this.data = data;
	}

	public double getTotale() {
		return totale;
	}

	public void setTotale(double totale) {
		this.totale = totale;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getMetodoPagamento() {
		return metodoPagamento;
	}

	public void setMetodoPagamento(String metodoPagamento) {
		this.metodoPagamento = metodoPagamento;
	}

	public String getNotaCliente() {
		return notaCliente;
	}

	public void setNotaCliente(String notaCliente) {
		this.notaCliente = notaCliente;
	}
}