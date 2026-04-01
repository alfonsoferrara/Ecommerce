package ecom.model.dao;

import ecom.model.bean.Attributo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class AttributoDAO implements GenericDAO<Attributo, Integer> {
	private DataSource ds;

	public AttributoDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void insert(Attributo a) throws SQLException {
		String query = "INSERT INTO Attributo (nome) VALUES (?)";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, a.getNome());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					a.setId(rs.getInt(1));
			}
		}
	}

	@Override
	public Attributo findById(Integer id) throws SQLException {
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement("SELECT * FROM Attributo WHERE id = ?")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return new Attributo(rs.getInt("id"), rs.getString("nome"));
			}
		}
		return null;
	}

	@Override
	public List<Attributo> findAll() throws SQLException {
		List<Attributo> list = new ArrayList<>();
		String query = "SELECT * FROM Attributo";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next())
				list.add(new Attributo(rs.getInt("id"), rs.getString("nome")));
		}
		return list;
	}

	@Override
	public void update(Attributo a) throws SQLException {
		String query = "UPDATE Attributo SET nome=? WHERE id=?";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, a.getNome());
			ps.setInt(2, a.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void delete(Integer id) throws SQLException {
		String query = "DELETE FROM Attributo WHERE id=?";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
}