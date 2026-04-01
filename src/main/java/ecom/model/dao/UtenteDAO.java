package ecom.model.dao;

import ecom.model.bean.Utente;
import org.mindrot.jbcrypt.BCrypt; //hash delle password
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
		String hashedPassword = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
		String query = "INSERT INTO Utente (email, password) VALUES (?, ?)";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, u.getEmail());
			ps.setString(2, hashedPassword);
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
		// Prima cerca l'utente per email
		String query = "SELECT * FROM Utente WHERE email = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, email);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					// Recupera l'hash salvato nel database
					String storedHash = rs.getString("password");

					// Verifica la password in chiaro con l'hash salvato
					if (BCrypt.checkpw(password, storedHash)) {
						// Password corretta
						return new Utente(rs.getInt("id"), rs.getString("email"), storedHash);
					}
				}
				return null; // Utente non trovato o password errata
			}
		}
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
		String hashedPassword = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
		String query = "UPDATE Utente SET email=?, password=? WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, u.getEmail());
			ps.setString(2, hashedPassword);
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