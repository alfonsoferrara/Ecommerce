package ecom.model.dao;

import ecom.model.bean.Immagine;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class ImmagineDAO implements GenericDAO<Immagine, Integer> {
	private DataSource ds;

	public ImmagineDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void insert(Immagine img) throws SQLException {
		String query = "INSERT INTO Immagine (prodotto_id, url, is_principal) VALUES (?, ?, ?)";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, img.getProdottoId());
			ps.setString(2, img.getUrl());
			ps.setBoolean(3, img.isPrincipal());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					img.setId(rs.getInt(1));
			}
		}
	}

	@Override
	public Immagine findById(Integer id) throws SQLException {
		String query = "SELECT * FROM Immagine WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return new Immagine(rs.getInt("id"), rs.getInt("prodotto_id"), rs.getString("url"),
							rs.getBoolean("is_principal"));
			}
		}
		return null;
	}

	public List<Immagine> findByProdottoId(int prodottoId) throws SQLException {
		List<Immagine> imgs = new ArrayList<>();
		String query = "SELECT * FROM Immagine WHERE prodotto_id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, prodottoId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					imgs.add(new Immagine(rs.getInt("id"), rs.getInt("prodotto_id"), rs.getString("url"),
							rs.getBoolean("is_principal")));
			}
		}
		return imgs;
	}

	public Immagine findPrincipalByProdottoId(int prodottoId) throws SQLException {
		String query = "SELECT * FROM Immagine WHERE prodotto_id = ? and is_principal = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, prodottoId);
			ps.setInt(2, 1);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return new Immagine(rs.getInt("id"), rs.getInt("prodotto_id"), rs.getString("url"),
							rs.getBoolean("is_principal"));
			}
		}
		return null;
	}

	@Override
	public List<Immagine> findAll() throws SQLException {
		List<Immagine> imgs = new ArrayList<>();
		String query = "SELECT * FROM Immagine";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next())
				imgs.add(new Immagine(rs.getInt("id"), rs.getInt("prodotto_id"), rs.getString("url"),
						rs.getBoolean("is_principal")));
		}
		return imgs;
	}

	@Override
	public void update(Immagine img) throws SQLException {
		String query = "UPDATE Immagine SET url=?, is_principal=? WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, img.getUrl());
			ps.setBoolean(2, img.isPrincipal());
			ps.setInt(3, img.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void delete(Integer id) throws SQLException {
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement("DELETE FROM Immagine WHERE id=?")) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
	
	public void impostaImmaginePrincipale(int immagineId, int prodottoId) throws SQLException {
	    // Query 1: Imposta TUTTE le immagini di quel prodotto a false
	    String queryReset = "UPDATE Immagine SET is_principal = FALSE WHERE prodotto_id = ?";
	    
	    // Query 2: Imposta SOLO l'immagine selezionata a true
	    String querySet = "UPDATE Immagine SET is_principal = TRUE WHERE id = ?";
	    
	    Connection con = null;
	    PreparedStatement psReset = null;
	    PreparedStatement psSet = null;
	    
	    try {
	        con = ds.getConnection();
	        // disabilitare l'autocommit per eseguire entrambe o nessuna
	        con.setAutoCommit(false); 
	        
	        // Eseguo il reset
	        psReset = con.prepareStatement(queryReset);
	        psReset.setInt(1, prodottoId);
	        psReset.executeUpdate();
	        
	        // Eseguo il set
	        psSet = con.prepareStatement(querySet);
	        psSet.setInt(1, immagineId);
	        psSet.executeUpdate();
	        
	        con.commit(); // Confermo la transazione
	        
	    } catch (SQLException e) {
	        if (con != null) con.rollback(); // In caso di errore, annullo tutto
	        throw e;
	    } finally {
	        if (con != null) con.setAutoCommit(true);
	        if (psReset != null) psReset.close();
	        if (psSet != null) psSet.close();
	        if (con != null) con.close();
	    }
	}
}