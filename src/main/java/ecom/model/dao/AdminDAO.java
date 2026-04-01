package ecom.model.dao;

import ecom.model.bean.Admin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

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
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
}