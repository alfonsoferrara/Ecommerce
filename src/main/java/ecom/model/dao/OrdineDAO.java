package ecom.model.dao;

import ecom.model.bean.Ordine;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class OrdineDAO implements GenericDAO<Ordine, Integer> {
	private DataSource ds;

	public OrdineDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void insert(Ordine o) throws SQLException {
		String query = "INSERT INTO Ordine (cliente_id, indirizzo_id, totale, stato, metodo_pagamento, nota_cliente) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, o.getClienteId());
			ps.setInt(2, o.getIndirizzoId());
			ps.setDouble(3, o.getTotale());
			ps.setString(4, o.getStato());
			ps.setString(5, o.getMetodoPagamento());
			ps.setString(6, o.getNotaCliente());
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					o.setId(rs.getInt(1));
			}
		}
	}

	@Override
	public Ordine findById(Integer id) throws SQLException {
		String query = "SELECT * FROM Ordine WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					// Costruttore senza 'spedito'
					return new Ordine(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("indirizzo_id"),
							rs.getTimestamp("data"), rs.getDouble("totale"), rs.getString("stato"),
							rs.getString("metodo_pagamento"), rs.getString("nota_cliente"));
				}
			}
		}
		return null;
	}

	@Override
	public List<Ordine> findAll() throws SQLException {
		List<Ordine> ordini = new ArrayList<>();
		String query = "SELECT * FROM Ordine";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ordini.add(new Ordine(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("indirizzo_id"),
						rs.getTimestamp("data"), rs.getDouble("totale"), rs.getString("stato"),
						rs.getString("metodo_pagamento"), rs.getString("nota_cliente")));
			}
		}
		return ordini;
	}

	@Override
	public void update(Ordine o) throws SQLException {
		String query = "UPDATE Ordine SET stato=?, metodo_pagamento=?, nota_cliente=? WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, o.getStato());
			ps.setString(2, o.getMetodoPagamento());
			ps.setString(3, o.getNotaCliente());
			ps.setInt(4, o.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void delete(Integer id) throws SQLException {
		String query = "DELETE FROM Ordine WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
}