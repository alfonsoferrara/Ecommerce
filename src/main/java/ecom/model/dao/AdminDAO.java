package ecom.model.dao;

import ecom.model.bean.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.mindrot.jbcrypt.BCrypt;

public class AdminDAO implements GenericDAO<Admin, Integer> {
	private DataSource ds;

	public AdminDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void insert(Admin a) throws SQLException {
		String queryUtente = "INSERT INTO Utente (email, password) VALUES (?, ?)";
		String queryAdmin = "INSERT INTO Admin (utente_id) VALUES (?)";

		Connection con = null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false); // Inizio Transazione

			int utenteId = 0;
			try (PreparedStatement psU = con.prepareStatement(queryUtente, Statement.RETURN_GENERATED_KEYS)) {
				psU.setString(1, a.getEmail());
				psU.setString(2, a.getPassword());
				psU.executeUpdate();
				try (ResultSet rs = psU.getGeneratedKeys()) {
					if (rs.next())
						utenteId = rs.getInt(1);
				}
			}

			a.setId(utenteId);

			try (PreparedStatement psA = con.prepareStatement(queryAdmin)) {
				psA.setInt(1, utenteId);
				psA.executeUpdate();
			}

			con.commit();
		} catch (SQLException e) {
			if (con != null)
				con.rollback();
			throw e;
		} finally {
			if (con != null) {
				con.setAutoCommit(true);
				con.close();
			}
		}
	}

	public int insertAndReturnId(Admin a) throws SQLException {
		String hashedPassword = BCrypt.hashpw(a.getPassword(), BCrypt.gensalt());

		String queryUtente = "INSERT INTO Utente (email, password) VALUES (?, ?)";
		String queryAdmin = "INSERT INTO Admin (utente_id) VALUES (?)";

		Connection con = null;

		try {
			con = ds.getConnection();
			con.setAutoCommit(false); // Inizio Transazione

			// Primo Inserimento Utente
			int utenteId = 0;
			try (PreparedStatement psU = con.prepareStatement(queryUtente, Statement.RETURN_GENERATED_KEYS)) {
				psU.setString(1, a.getEmail());
				psU.setString(2, hashedPassword);
				psU.executeUpdate();

				try (ResultSet rs = psU.getGeneratedKeys()) {
					if (rs.next()) {
						utenteId = rs.getInt(1);
					} else {
						throw new SQLException("Creazione utente fallita, nessun ID ottenuto.");
					}
				}
			}

			a.setId(utenteId); // Aggiorno l'ID nel bean Admin

			// Secondo Inserimento Admin
			try (PreparedStatement psA = con.prepareStatement(queryAdmin)) {
				psA.setInt(1, utenteId);
				psA.executeUpdate();
			}

			con.commit(); // Conferma Transazione

			return utenteId;

		} catch (SQLException e) {
			if (con != null) {
				con.rollback(); // Se fallisce, annulla tutto!
			}
			throw e;
		} finally {
			if (con != null) {
				con.setAutoCommit(true);
				con.close();
			}
		}
	}

	@Override
	public Admin findById(Integer id) throws SQLException {
		String query = "SELECT u.id, u.email, u.password, a.data_creazione FROM Admin a JOIN Utente u ON a.utente_id = u.id WHERE a.utente_id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return new Admin(rs.getInt("id"), rs.getString("email"), rs.getString("password"),
							rs.getTimestamp("data_creazione"));
			}
		}
		return null;
	}

	@Override
	public List<Admin> findAll() throws SQLException {
		List<Admin> admins = new ArrayList<>();
		String query = "SELECT u.id, u.email, u.password, a.data_creazione FROM Admin a JOIN Utente u ON a.utente_id = u.id";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next())
				admins.add(new Admin(rs.getInt("id"), rs.getString("email"), rs.getString("password"),
						rs.getTimestamp("data_creazione")));
		}
		return admins;
	}

	// Metodo che restituisce gli admin in ordine di creaizone
	// con logica di paginazione
	public List<Admin> findAllPaginazione(int pagina, int pageSize) throws SQLException {
		int offset = (pagina - 1) * pageSize;
		List<Admin> admins = new ArrayList<>();
		String query = "SELECT u.id, u.email, u.password, a.data_creazione "
				+ "FROM Admin a JOIN Utente u ON a.utente_id = u.id ORDER BY u.id DESC LIMIT ? OFFSET ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {
			ps.setInt(1, pageSize);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				admins.add(new Admin(rs.getInt("id"), rs.getString("email"), rs.getString("password"),
						rs.getTimestamp("data_creazione")));
			}
		}
		return admins;
	}

	public List<Admin> findAlfabeticoEmail(int pagina, int pageSize) throws SQLException {
		int offset = (pagina - 1) * pageSize;
		List<Admin> admins = new ArrayList<>();
		String query = "SELECT u.id, u.email, u.password, a.data_creazione "
				+ "FROM Admin a JOIN Utente u ON a.utente_id = u.id ORDER BY u.email ASC LIMIT ? OFFSET ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {
			ps.setInt(1, pageSize);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				admins.add(new Admin(rs.getInt("id"), rs.getString("email"), rs.getString("password"),
						rs.getTimestamp("data_creazione")));
			}
		}
		return admins;
	}

	@Override
	public void update(Admin a) throws SQLException {
		// Un Admin aggiorna solo i campi della tabella Utente
		String query = "UPDATE Utente SET email=?, password=? WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, a.getEmail());
			ps.setString(2, a.getPassword());
			ps.setInt(3, a.getId());
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

	// conteggio admin recenti per gestire la paginazione
	public int countAdmin() {
		String sql = "SELECT COUNT(*) FROM Admin";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}
}