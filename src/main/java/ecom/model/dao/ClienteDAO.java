package ecom.model.dao;

import ecom.model.bean.Cliente;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import org.mindrot.jbcrypt.BCrypt; //hash delle password

public class ClienteDAO implements GenericDAO<Cliente, Integer> {
	private DataSource ds;

	public ClienteDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void insert(Cliente c) throws SQLException {
		String hashedPassword = BCrypt.hashpw(c.getPassword(), BCrypt.gensalt());

		String queryUtente = "INSERT INTO Utente (email, password) VALUES (?, ?)";
		String queryCliente = "INSERT INTO Cliente (utente_id, nome, cognome, telefono) VALUES (?, ?, ?, ?)";

		Connection con = null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false); // Inizio Transazione

			// Prima Inserimento Utente
			int utenteId = 0;
			try (PreparedStatement psU = con.prepareStatement(queryUtente, Statement.RETURN_GENERATED_KEYS)) {
				psU.setString(1, c.getEmail());
				psU.setString(2, hashedPassword);
				psU.executeUpdate();
				try (ResultSet rs = psU.getGeneratedKeys()) {
					if (rs.next())
						utenteId = rs.getInt(1);
				}
			}

			c.setId(utenteId); // Aggiorno l'ID nel bean Cliente

			// Inserisco il Cliente
			try (PreparedStatement psC = con.prepareStatement(queryCliente)) {
				psC.setInt(1, utenteId);
				psC.setString(2, c.getNome());
				psC.setString(3, c.getCognome());
				psC.setString(4, c.getTelefono());
				psC.executeUpdate();
			}

			con.commit(); // Conferma Transazione
		} catch (SQLException e) {
			if (con != null)
				con.rollback(); // Se fallisce, annulla tutto!
			throw e;
		} finally {
			if (con != null) {
				con.setAutoCommit(true);
				con.close();
			}
		}
	}

	public int insertAndReturnId(Cliente c) throws SQLException {
		String hashedPassword = BCrypt.hashpw(c.getPassword(), BCrypt.gensalt());

		String queryUtente = "INSERT INTO Utente (email, password) VALUES (?, ?)";
		String queryCliente = "INSERT INTO Cliente (utente_id, nome, cognome, telefono) VALUES (?, ?, ?, ?)";

		Connection con = null;

		try {
			con = ds.getConnection();
			con.setAutoCommit(false); // Inizio Transazione

			// Primo Inserimento Utente
			int utenteId = 0;
			try (PreparedStatement psU = con.prepareStatement(queryUtente, Statement.RETURN_GENERATED_KEYS)) {
				psU.setString(1, c.getEmail());
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

			c.setId(utenteId); // Aggiorno l'ID nel bean Cliente

			// Secondo Inserimento Cliente (RIMOSSO Statement.RETURN_GENERATED_KEYS)
			try (PreparedStatement psC = con.prepareStatement(queryCliente)) {
				psC.setInt(1, utenteId);
				psC.setString(2, c.getNome());
				psC.setString(3, c.getCognome());
				psC.setString(4, c.getTelefono()); // Telefono non c'era nel form JSP, assicurati non sia null!
				psC.executeUpdate();
			}

			con.commit(); // Conferma Transazione

			// Ritorno direttamente utenteId, che è anche l'ID del cliente
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
	public Cliente findById(Integer id) throws SQLException {
		// JOIN per recuperare i dati da entrambe le tabelle
		String query = "SELECT u.id, u.email, u.password, c.nome, c.cognome, c.telefono "
				+ "FROM Cliente c JOIN Utente u ON c.utente_id = u.id WHERE c.utente_id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Cliente(rs.getInt("id"), rs.getString("email"), rs.getString("password"),
							rs.getString("nome"), rs.getString("cognome"), rs.getString("telefono"));
				}
			}
		}
		return null;
	}

	@Override
	public List<Cliente> findAll() throws SQLException {
		List<Cliente> clienti = new ArrayList<>();
		String query = "SELECT u.id, u.email, u.password, c.nome, c.cognome, c.telefono "
				+ "FROM Cliente c JOIN Utente u ON c.utente_id = u.id";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				clienti.add(new Cliente(rs.getInt("id"), rs.getString("email"), rs.getString("password"),
						rs.getString("nome"), rs.getString("cognome"), rs.getString("telefono")));
			}
		}
		return clienti;
	}

	public List<Cliente> findAllPaginazione(int pagina, int pageSize) throws SQLException {
		int offset = (pagina - 1) * pageSize;
		List<Cliente> clienti = new ArrayList<>();
		String query = "SELECT u.id, u.email, u.password, c.nome, c.cognome, c.telefono "
				+ "FROM Cliente c JOIN Utente u ON c.utente_id = u.id ORDER BY u.id DESC LIMIT ? OFFSET ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {
			ps.setInt(1, pageSize);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				clienti.add(new Cliente(rs.getInt("id"), rs.getString("email"), rs.getString("password"),
						rs.getString("nome"), rs.getString("cognome"), rs.getString("telefono")));
			}
		}
		return clienti;
	}

	public List<Cliente> findAlfabeticoNome(int pagina, int pageSize) throws SQLException {
		int offset = (pagina - 1) * pageSize;
		List<Cliente> clienti = new ArrayList<>();
		String query = "SELECT u.id, u.email, u.password, c.nome, c.cognome, c.telefono "
				+ "FROM Cliente c JOIN Utente u ON c.utente_id = u.id ORDER BY nome ASC LIMIT ? OFFSET ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {

			ps.setInt(1, pageSize);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				clienti.add(new Cliente(rs.getInt("id"), rs.getString("email"), rs.getString("password"),
						rs.getString("nome"), rs.getString("cognome"), rs.getString("telefono")));
			}
		}

		return clienti;
	}

	public List<Cliente> findAlfabeticoCognome(int pagina, int pageSize) throws SQLException {
		int offset = (pagina - 1) * pageSize;
		List<Cliente> clienti = new ArrayList<>();
		String query = "SELECT u.id, u.email, u.password, c.nome, c.cognome, c.telefono "
				+ "FROM Cliente c JOIN Utente u ON c.utente_id = u.id ORDER BY cognome ASC LIMIT ? OFFSET ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {

			ps.setInt(1, pageSize);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				clienti.add(new Cliente(rs.getInt("id"), rs.getString("email"), rs.getString("password"),
						rs.getString("nome"), rs.getString("cognome"), rs.getString("telefono")));
			}
		}

		return clienti;
	}

	@Override
	public void update(Cliente c) throws SQLException {
		// Aggiorna solo i campi del cliente, NON la password
		String queryCliente = "UPDATE Cliente SET nome=?, cognome=?, telefono=? WHERE utente_id=?";
		String queryUtente = "UPDATE Utente SET email=? WHERE id=?";

		try (Connection con = ds.getConnection()) {
			con.setAutoCommit(false);

			try (PreparedStatement psU = con.prepareStatement(queryUtente);
					PreparedStatement psC = con.prepareStatement(queryCliente)) {

				psU.setString(1, c.getEmail());
				psU.setInt(2, c.getId());
				psU.executeUpdate();

				psC.setString(1, c.getNome());
				psC.setString(2, c.getCognome());
				psC.setString(3, c.getTelefono());
				psC.setInt(4, c.getId());
				psC.executeUpdate();

				con.commit();
			} catch (SQLException e) {
				con.rollback();
				throw e;
			} finally {
				con.setAutoCommit(true);
			}
		}
	}

	// Metodo separato per cambiare password
	public void changePassword(int userId, String newPassword) throws SQLException {
		String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		String query = "UPDATE Utente SET password=? WHERE id=?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, hashedPassword);
			ps.setInt(2, userId);
			ps.executeUpdate();
		}
	}

	@Override
	public void delete(Integer id) throws SQLException {
		// Grazie all'ON DELETE CASCADE, basta eliminare l'Utente e il Cliente sparirà
		// automaticamente
		String query = "DELETE FROM Utente WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}

	public int countAll() throws SQLException {
		String sql = "SELECT COUNT(*) FROM Cliente";

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