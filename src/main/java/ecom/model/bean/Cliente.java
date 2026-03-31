package ecom.model.bean;

import java.io.Serializable;

public class Cliente extends Utente implements Serializable {
	private String nome;
	private String cognome;
	private String telefono;

	public Cliente() {
		super();
	}

	public Cliente(int id, String email, String password, String nome, String cognome, String telefono) {
		super(id, email, password);
		this.nome = nome;
		this.cognome = cognome;
		this.telefono = telefono;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
}