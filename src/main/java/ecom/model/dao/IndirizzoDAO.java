package ecom.model.dao;

import ecom.model.bean.Indirizzo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class IndirizzoDAO implements GenericDAO<Indirizzo, Integer> {
	private DataSource ds;

	public IndirizzoDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void insert(Indirizzo i) throws SQLException {
		String query = "INSERT INTO Indirizzo (cliente_id, via, civico, citta, provincia, cap) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, i.getClienteId());
			ps.setString(2, i.getVia());
			ps.setString(3, i.getCivico());
			ps.setString(4, i.getCitta());
			ps.setString(5, i.getProvincia());
			ps.setString(6, i.getCap());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					i.setId(rs.getInt(1));
			}
		}
	}

	@Override
	public Indirizzo findById(Integer id) throws SQLException {
		String query = "SELECT * FROM Indirizzo WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return new Indirizzo(rs.getInt("id"), rs.getInt("cliente_id"), rs.getString("via"),
							rs.getString("civico"), rs.getString("citta"), rs.getString("provincia"),
							rs.getString("cap"));
			}
		}
		return null;
	}

	public List<Indirizzo> findByClienteId(int clienteId) throws SQLException {
		List<Indirizzo> indirizzi = new ArrayList<>();
		String query = "SELECT * FROM Indirizzo WHERE cliente_id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, clienteId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					indirizzi.add(new Indirizzo(rs.getInt("id"), rs.getInt("cliente_id"), rs.getString("via"),
							rs.getString("civico"), rs.getString("citta"), rs.getString("provincia"),
							rs.getString("cap")));
			}
		}
		return indirizzi;
	}

	@Override
	public List<Indirizzo> findAll() throws SQLException {
		List<Indirizzo> list = new ArrayList<>();
		String query = "SELECT * FROM Indirizzo";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next())
				list.add(new Indirizzo(rs.getInt("id"), rs.getInt("cliente_id"), rs.getString("via"),
						rs.getString("civico"), rs.getString("citta"), rs.getString("provincia"), rs.getString("cap")));
		}
		return list;
	}

	@Override
	public void update(Indirizzo i) throws SQLException {
		String query = "UPDATE Indirizzo SET via=?, civico=?, citta=?, provincia=?, cap=? WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, i.getVia());
			ps.setString(2, i.getCivico());
			ps.setString(3, i.getCitta());
			ps.setString(4, i.getProvincia());
			ps.setString(5, i.getCap());
			ps.setInt(6, i.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void delete(Integer id) throws SQLException {
		String query = "DELETE FROM Indirizzo WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
}