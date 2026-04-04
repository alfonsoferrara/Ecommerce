package ecom.model.dao;

import ecom.model.bean.Ordine;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	/**
	 * Trova tutti gli ordini di un cliente, ordinati dal più recente al più vecchio
	 * con logica di paginazione
	 */
	public List<Ordine> findByClienteId(int clienteId, int pagina, int pageSize) throws SQLException {
		List<Ordine> ordini = new ArrayList<>();
		String query = "SELECT * FROM Ordine WHERE cliente_id = ? ORDER BY data DESC LIMIT ? OFFSET ?";
		int offset = (pagina - 1) * pageSize;

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, clienteId);
			ps.setInt(2, pageSize);
			ps.setInt(3, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ordini.add(new Ordine(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("indirizzo_id"),
							rs.getTimestamp("data"), rs.getDouble("totale"), rs.getString("stato"),
							rs.getString("metodo_pagamento"), rs.getString("nota_cliente")));
				}
			}
		}
		return ordini;
	}

	/**
	 * Trova gli ultimi N ordini di un cliente, ordinati dal più recente
	 */
	public List<Ordine> findLastByClienteId(int clienteId, int limit) throws SQLException {
		List<Ordine> ordini = new ArrayList<>();
		String query = "SELECT * FROM Ordine WHERE cliente_id = ? ORDER BY data DESC LIMIT ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, clienteId);
			ps.setInt(2, limit);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ordini.add(new Ordine(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("indirizzo_id"),
							rs.getTimestamp("data"), rs.getDouble("totale"), rs.getString("stato"),
							rs.getString("metodo_pagamento"), rs.getString("nota_cliente")));
				}
			}
		}
		return ordini;
	}

	/**
	 * Conta quanti ordini ha un cliente
	 */
	public int countByClienteId(int clienteId) throws SQLException {
		String query = "SELECT COUNT(*) FROM Ordine WHERE cliente_id = ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, clienteId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return 0;
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