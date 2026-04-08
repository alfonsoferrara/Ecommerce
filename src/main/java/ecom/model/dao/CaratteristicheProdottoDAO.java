package ecom.model.dao;

import ecom.model.bean.CaratteristicheProdotto;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

public class CaratteristicheProdottoDAO implements GenericDAO<CaratteristicheProdotto, Integer> {
	private DataSource ds;

	public CaratteristicheProdottoDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void insert(CaratteristicheProdotto cp) throws SQLException {
		String query = "INSERT INTO Caratteristiche_Prodotto (prodotto_id, attributo_id, valoreAttr) VALUES (?, ?, ?)";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, cp.getProdottoId());
			ps.setInt(2, cp.getAttributoId());
			ps.setString(3, cp.getValoreAttr());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					cp.setId(rs.getInt(1));
			}
		}
	}

	public List<CaratteristicheProdotto> findByProdottoId(int prodottoId) throws SQLException {
		List<CaratteristicheProdotto> list = new ArrayList<>();
		String query = "SELECT * FROM Caratteristiche_Prodotto WHERE prodotto_id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, prodottoId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next())
					list.add(new CaratteristicheProdotto(rs.getInt("id"), rs.getInt("prodotto_id"),
							rs.getInt("attributo_id"), rs.getString("valoreAttr")));
			}
		}
		return list;
	}

	// metodo che mi permette di avere stringhe del tipo Colore: nero filtrando per
	// id prodotto
	public Map<String, String> getCaratteristicheMapByProdottoId(int prodottoId) throws SQLException {
		Map<String, String> caratteristiche = new HashMap<>();
		String query = "SELECT a.nome, c.valoreAttr FROM Attributo a "
				+ "INNER JOIN Caratteristiche_Prodotto c ON a.id = c.attributo_id " + "WHERE c.prodotto_id = ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, prodottoId);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					String nomeAttributo = rs.getString("nome");
					String valore = rs.getString("valoreAttr");
					caratteristiche.put(nomeAttributo, valore);
				}
			}
		}

		return caratteristiche;
	}

	@Override
	public CaratteristicheProdotto findById(Integer id) throws SQLException {
		String query = "SELECT * FROM Caratteristiche_Prodotto WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return new CaratteristicheProdotto(rs.getInt("id"), rs.getInt("prodotto_id"),
							rs.getInt("attributo_id"), rs.getString("valoreAttr"));
			}
		}
		return null;
	}

	@Override
	public List<CaratteristicheProdotto> findAll() throws SQLException {
		String query = "SELECT * FROM Caratteristiche_Prodotto";
		List<CaratteristicheProdotto> list = new ArrayList<>();
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next())
				list.add(new CaratteristicheProdotto(rs.getInt("id"), rs.getInt("prodotto_id"),
						rs.getInt("attributo_id"), rs.getString("valoreAttr")));
		}
		return list;
	}

	@Override
	public void update(CaratteristicheProdotto cp) throws SQLException {
		String query = "UPDATE Caratteristiche_Prodotto SET valoreAttr=? WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, cp.getValoreAttr());
			ps.setInt(2, cp.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void delete(Integer id) throws SQLException {
		String query = "DELETE FROM Caratteristiche_Prodotto WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}
	
	//metodi per gestire l'aggiornamento o creazione di prodotti lato admin
	public void updateCaratteristica(int prodottoId, String nomeAttributo, String nuovoValore) throws SQLException {
	    String sql = "UPDATE Caratteristiche_Prodotto cp " +
	                 "INNER JOIN Attributo a ON cp.attributo_id = a.id " +
	                 "SET cp.valoreAttr = ? " +
	                 "WHERE cp.prodotto_id = ? AND a.nome = ?";
	    
	    try (Connection conn = ds.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, nuovoValore);
	        stmt.setInt(2, prodottoId);
	        stmt.setString(3, nomeAttributo);
	        stmt.executeUpdate();
	    }
	}

	public void insertOrUpdate(int prodottoId, String nomeAttributo, String valore) throws SQLException {
	    // Prima verifica se l'attributo esiste
	    int attributoId = getOrCreateAttributo(nomeAttributo);
	    
	    String sql = "INSERT INTO Caratteristiche_Prodotto (prodotto_id, attributo_id, valoreAttr) " +
	                 "VALUES (?, ?, ?) " +
	                 "ON DUPLICATE KEY UPDATE valoreAttr = ?";
	    
	    try (Connection conn = ds.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, prodottoId);
	        stmt.setInt(2, attributoId);
	        stmt.setString(3, valore);
	        stmt.setString(4, valore);
	        stmt.executeUpdate();
	    }
	}

	private int getOrCreateAttributo(String nomeAttributo) throws SQLException {
	    // Cerca l'attributo
	    String selectSql = "SELECT id FROM Attributo WHERE nome = ?";
	    try (Connection conn = ds.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(selectSql)) {
	        stmt.setString(1, nomeAttributo);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt("id");
	        }
	    }
	    
	    // Se non esiste, crealo
	    String insertSql = "INSERT INTO Attributo (nome) VALUES (?)";
	    try (Connection conn = ds.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
	        stmt.setString(1, nomeAttributo);
	        stmt.executeUpdate();
	        ResultSet rs = stmt.getGeneratedKeys();
	        if (rs.next()) {
	            return rs.getInt(1);
	        }
	    }
	    
	    throw new SQLException("Impossibile creare l'attributo: " + nomeAttributo);
	}

	public void deleteCaratteristica(int prodottoId, String nomeAttributo) throws SQLException {
	    String sql = "DELETE cp FROM Caratteristiche_Prodotto cp " +
	                 "INNER JOIN Attributo a ON cp.attributo_id = a.id " +
	                 "WHERE cp.prodotto_id = ? AND a.nome = ?";
	    
	    try (Connection conn = ds.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, prodottoId);
	        stmt.setString(2, nomeAttributo);
	        stmt.executeUpdate();
	    }
	}
}