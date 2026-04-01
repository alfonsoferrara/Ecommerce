package ecom.model.dao;

import ecom.model.bean.Carrello;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class CarrelloDAO implements GenericDAO<Carrello, String> {
	private DataSource ds;

	public CarrelloDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void insert(Carrello c) throws SQLException {
		String query = "INSERT INTO Carrello (id, cliente_id) VALUES (?, ?)";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, c.getId());
			if (c.getClienteId() != null)
				ps.setInt(2, c.getClienteId());
			else
				ps.setNull(2, Types.INTEGER); // Types.INTEGER serve a dire al db che la colonna che viene lasciata
												// vuota ha attributi di tipo int

			ps.executeUpdate();
		}

		// l'attritubo data viene generato direttamente dal db
	}

	@Override
	public Carrello findById(String id) throws SQLException {
		String query = "SELECT * FROM Carrello WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Carrello(rs.getString("id"), (Integer) rs.getObject("cliente_id"),
							rs.getTimestamp("data"));
					/*
					 * Grazie a rs.getObject e al cast ad Integer
					 * se nel DB c'è un numero, JDBC restituisce un oggetto di tipo Integer.
					 * se nel DB c'è NULL, JDBC restituisce un riferimento null di Java.
					 */
				}
			}
		}
		return null;
	}

	// Metodo extra utile per il Login: trova il carrello di un cliente specifico
	public Carrello findByCliente(int clienteId) throws SQLException {
		String query = "SELECT * FROM Carrello WHERE cliente_id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, clienteId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Carrello(rs.getString("id"), rs.getInt("cliente_id"), rs.getTimestamp("data"));
				}
			}
		}
		return null;
	}

	@Override
	public List<Carrello> findAll() throws SQLException {
		List<Carrello> carrelli = new ArrayList<>();
		String query = "SELECT * FROM Carrello";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				carrelli.add(new Carrello(rs.getString("id"), (Integer) rs.getObject("cliente_id"),
						rs.getTimestamp("data")));
			}
		}
		return carrelli;
	}

	@Override
	public void update(Carrello c) throws SQLException {
		String query = "UPDATE Carrello SET cliente_id = ? WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			if (c.getClienteId() != null)
				ps.setInt(1, c.getClienteId());
			else
				ps.setNull(1, Types.INTEGER);
			ps.setString(2, c.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void delete(String id) throws SQLException {
		String query = "DELETE FROM Carrello WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, id);
			ps.executeUpdate();
		}
	}
}