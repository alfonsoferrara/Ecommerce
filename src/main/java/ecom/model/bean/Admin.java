package ecom.model.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Admin extends Utente implements Serializable {
	private Timestamp dataCreazione;

	public Admin() {
		super();
	}

	public Admin(int id, String email, String password, Timestamp dataCreazione) {
		super(id, email, password);
		this.dataCreazione = dataCreazione;
	}

	public Timestamp getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
}