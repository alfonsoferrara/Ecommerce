package ecom.model.dao;

import ecom.model.bean.Recensione;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class RecensioneDAO implements GenericDAO<Recensione, Integer> {
	private DataSource ds;

	public RecensioneDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void insert(Recensione r) throws SQLException {
		String query = "INSERT INTO Recensione (prodotto_id, cliente_id, valutazione, titolo, commento) VALUES (?, ?, ?, ?)";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, r.getProdottoId());
			ps.setInt(2, r.getClienteId());
			ps.setInt(3, r.getValutazione());
			ps.setString(4, r.getTitolo());
			ps.setString(5, r.getCommento());
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					r.setId(rs.getInt(1));
			}
		}
	}

	public List<Recensione> findByProdotto(int prodottoId) throws SQLException {
		List<Recensione> list = new ArrayList<>();
		String query = "SELECT * FROM Recensione WHERE prodotto_id = ? ORDER BY data DESC";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, prodottoId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(new Recensione(rs.getInt("id"), rs.getInt("prodotto_id"), rs.getInt("cliente_id"),
							rs.getInt("valutazione"), rs.getString("titolo"), rs.getString("commento"),
							rs.getTimestamp("data")));
				}
			}
		}
		return list;
	}

	@Override
	public Recensione findById(Integer id) throws SQLException {
		String query = "SELECT * FROM Recensione WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Recensione(rs.getInt("id"), rs.getInt("prodotto_id"), rs.getInt("cliente_id"),
							rs.getInt("valutazione"), rs.getString("titolo"), rs.getString("commento"),
							rs.getTimestamp("data"));
				}
			}
		}
		return null;
	}

	public List<Recensione> findByClienteId(int id) throws SQLException {
		List<Recensione> list = new ArrayList<>();
		String query = "SELECT * FROM Recensione WHERE cliente_id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new Recensione(rs.getInt("id"), rs.getInt("prodotto_id"), rs.getInt("cliente_id"),
						rs.getInt("valutazione"), rs.getString("titolo"), rs.getString("commento"),
						rs.getTimestamp("data")));
			}
		}
		return list;
	}

	@Override
	public List<Recensione> findAll() throws SQLException {
		List<Recensione> list = new ArrayList<>();
		String query = "SELECT * FROM Recensione ORDER BY data DESC";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.add(new Recensione(rs.getInt("id"), rs.getInt("prodotto_id"), rs.getInt("cliente_id"),
						rs.getInt("valutazione"), rs.getString("titolo"), rs.getString("commento"),
						rs.getTimestamp("data")));
			}
		}
		return list;
	}

	@Override
	public void update(Recensione r) throws SQLException {
		// si aggiora solo la valutazione e il commento.
		// data, cliente_id e prodotto_id rimangono invariati dato che non ha senso che
		// cambino.
		String query = "UPDATE Recensione SET valutazione = ?, titolo = ?, commento = ? WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, r.getValutazione());
			ps.setString(2, r.getCommento());
			ps.setString(3, r.getTitolo());
			ps.setInt(4, r.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void delete(Integer id) throws SQLException {
		String query = "DELETE FROM Recensione WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
}