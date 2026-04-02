package ecom.model.dao;

import ecom.model.bean.VoceCarrello;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class VoceCarrelloDAO {
	private DataSource ds;

	public VoceCarrelloDAO(DataSource ds) {
		this.ds = ds;
	}

	public void insert(VoceCarrello v) throws SQLException {
		String query = "INSERT INTO Voce_Carrello (carrello_id, prodotto_id, quantita) VALUES (?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE quantita = quantita + ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, v.getCarrelloId());
			ps.setInt(2, v.getProdottoId());
			ps.setInt(3, v.getQuantita());
			ps.setInt(4, v.getQuantita()); // Se esiste già, somma la quantità
			ps.executeUpdate();
		}
	}

	public List<VoceCarrello> findByCarrello(String carrelloId) throws SQLException {
		List<VoceCarrello> voci = new ArrayList<>();
		String query = "SELECT * FROM Voce_Carrello WHERE carrello_id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, carrelloId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					voci.add(new VoceCarrello(rs.getString("carrello_id"), rs.getInt("prodotto_id"),
							rs.getInt("quantita")));
				}
			}
		}
		return voci;
	}

	public void deleteProdotto(String carrelloId, int prodottoId) throws SQLException {
		String query = "DELETE FROM Voce_Carrello WHERE carrello_id = ? AND prodotto_id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, carrelloId);
			ps.setInt(2, prodottoId);
			ps.executeUpdate();
		}
	}

	public void deleteAllByCarrello(String cartId) throws SQLException {
		String query = "DELETE FROM Voce_Carrello WHERE carrello_id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, cartId);
			ps.executeUpdate();
		}
	}
}