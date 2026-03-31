package ecom.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Carrello implements Serializable {
	private String id; // VARCHAR(36) UUID
	private Integer clienteId; // Integer invece di int perché può essere NULL per l'ospite
	private Timestamp data;

	public Carrello() {
	}

	public Carrello(String id, Integer clienteId, Timestamp data) {
		this.id = id;
		this.clienteId = clienteId;
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getClienteId() {
		return clienteId;
	}

	public void setClienteId(Integer clienteId) {
		this.clienteId = clienteId;
	}

	public Timestamp getData() {
		return data;
	}

	public void setData(Timestamp data) {
		this.data = data;
	}
}