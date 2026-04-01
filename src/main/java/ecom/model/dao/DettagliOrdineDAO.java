package ecom.model.dao;

import ecom.model.bean.DettagliOrdine;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class DettagliOrdineDAO {
	// poiche DettagliOrdine ha una chiave primaria composta
	// non può implementare GenericDAO, quindi uso metodi appositi
	private DataSource ds;

	public DettagliOrdineDAO(DataSource ds) {
		this.ds = ds;
	}

	public void insert(DettagliOrdine d) throws SQLException {
		String query = "INSERT INTO Dettagli_Ordine (ordine_id, prodotto_id, quantita, prezzo_unitario) VALUES (?, ?, ?, ?)";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, d.getOrdineId());
			ps.setInt(2, d.getProdottoId());
			ps.setInt(3, d.getQuantita());
			ps.setDouble(4, d.getPrezzoUnitario());
			ps.executeUpdate();
		}
	}

	public List<DettagliOrdine> findByOrdineId(int ordineId) throws SQLException {
		List<DettagliOrdine> dettagli = new ArrayList<>();
		String query = "SELECT * FROM Dettagli_Ordine WHERE ordine_id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, ordineId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					dettagli.add(new DettagliOrdine(rs.getInt("ordine_id"), rs.getInt("prodotto_id"),
							rs.getInt("quantita"), rs.getDouble("prezzo_unitario")));
				}
			}
		}
		return dettagli;
	}

	public void deleteProdottoDaOrdine(int ordineId, int prodottoId) throws SQLException {
		String query = "DELETE FROM Dettagli_Ordine WHERE ordine_id = ? AND prodotto_id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, ordineId);
			ps.setInt(2, prodottoId);
			ps.executeUpdate();
		}
	}
}