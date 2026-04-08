package ecom.model.dao;

import ecom.model.bean.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class CategoriaDAO implements GenericDAO<Categoria, Integer> {
	private DataSource ds;

	public CategoriaDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void insert(Categoria c) throws SQLException {
		String query = "INSERT INTO Categoria (nome, descrizione) VALUES (?, ?)";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, c.getNome());
			ps.setString(2, c.getDescrizione());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					c.setId(rs.getInt(1));
			}
		}
	}

	@Override
	public Categoria findById(Integer id) throws SQLException {
		String query = "SELECT * FROM Categoria WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return new Categoria(rs.getInt("id"), rs.getString("nome"), rs.getString("descrizione"));
			}
		}
		return null;
	}

	public Categoria findByNome(String nome) throws SQLException {
		String query = "SELECT * FROM categoria WHERE nome = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, nome);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Categoria categoria = new Categoria();
					categoria.setId(rs.getInt("id"));
					categoria.setNome(rs.getString("nome"));
					categoria.setDescrizione(rs.getString("descrizione"));
					return categoria;
				}
			}
		}
		return null;
	}

	@Override
	public List<Categoria> findAll() throws SQLException {
		List<Categoria> list = new ArrayList<>();
		String query = "SELECT * FROM Categoria";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next())
				list.add(new Categoria(rs.getInt("id"), rs.getString("nome"), rs.getString("descrizione")));
		}
		return list;
	}

	@Override
	public void update(Categoria c) throws SQLException {
		String query = "UPDATE Categoria SET nome=?, descrizione=? WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, c.getNome());
			ps.setString(2, c.getDescrizione());
			ps.setInt(3, c.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void delete(Integer id) throws SQLException {
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement("DELETE FROM Categoria WHERE id=?")) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
}