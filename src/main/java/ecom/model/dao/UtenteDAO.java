package ecom.model.dao;

import ecom.model.bean.Utente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class UtenteDAO implements GenericDAO<Utente, Integer> {
	private DataSource ds;

	public UtenteDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void insert(Utente u) throws SQLException {
		String query = "INSERT INTO Utente (email, password) VALUES (?, ?)";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, u.getEmail());
			ps.setString(2, u.getPassword());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					u.setId(rs.getInt(1));
			}
		}
	}

	@Override
	public Utente findById(Integer id) throws SQLException {
		String query = "SELECT * FROM Utente WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return new Utente(rs.getInt("id"), rs.getString("email"), rs.getString("password"));
			}
		}
		return null;
	}

	public Utente doLogin(String email, String password) throws SQLException {
		String query = "SELECT * FROM Utente WHERE email = ? AND password = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, email);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return new Utente(rs.getInt("id"), rs.getString("email"), rs.getString("password"));
			}
		}
		return null;
	}

	@Override
	public List<Utente> findAll() throws SQLException {
		List<Utente> utenti = new ArrayList<>();
		String query = "SELECT * FROM Utente";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next())
				utenti.add(new Utente(rs.getInt("id"), rs.getString("email"), rs.getString("password")));
		}
		return utenti;
	}

	@Override
	public void update(Utente u) throws SQLException {
		String query = "UPDATE Utente SET email=?, password=? WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, u.getEmail());
			ps.setString(2, u.getPassword());
			ps.setInt(3, u.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void delete(Integer id) throws SQLException {
		String query = "DELETE FROM Utente WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
}