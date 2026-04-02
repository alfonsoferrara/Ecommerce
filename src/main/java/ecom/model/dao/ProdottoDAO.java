package ecom.model.dao;

import ecom.model.bean.Prodotto;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class ProdottoDAO implements GenericDAO<Prodotto, Integer> {
	private DataSource ds;

	public ProdottoDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void insert(Prodotto p) throws SQLException {
		String query = "INSERT INTO Prodotto (categoria_id, nome, descrizione, prezzo, stock, attivo) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, p.getCategoriaId());
			ps.setString(2, p.getNome());
			ps.setString(3, p.getDescrizione());
			ps.setDouble(4, p.getPrezzo());
			ps.setInt(5, p.getStock());
			ps.setBoolean(6, true); // Di default è attivo
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				// recupero l'identificativo univoco generato dal
				// database tramite AUTO_INCREMENT e lo imposto nel prodotto
				if (rs.next())
					p.setId(rs.getInt(1));
			}
		}
	}

	@Override
	public Prodotto findById(Integer id) throws SQLException {
		String query = "SELECT * FROM Prodotto WHERE id = ? AND attivo = TRUE";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Prodotto(rs.getInt("id"), rs.getInt("categoria_id"), rs.getString("nome"),
							rs.getString("descrizione"), rs.getDouble("prezzo"), rs.getInt("stock"),
							rs.getBoolean("attivo"));
				}
			}
		}
		return null;
	}

	public List<Prodotto> findByCategoriaId(int pagina, int pageSize, int categoria_id) throws SQLException {
		List<Prodotto> prodotti = new ArrayList<>();
		int offset = (pagina - 1) * pageSize;
		String query = "SELECT * FROM Prodotto WHERE categoria_id = ? AND attivo = TRUE LIMIT ? OFFSET ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, pagina);
			ps.setInt(2, offset);
			ps.setInt(3, categoria_id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					prodotti.add(new Prodotto(rs.getInt("id"), rs.getInt("categoria_id"), rs.getString("nome"),
							rs.getString("descrizione"), rs.getDouble("prezzo"), rs.getInt("stock"),
							rs.getBoolean("attivo")));
				}
			}
		}
		return null;
	}

	@Override
	public List<Prodotto> findAll() throws SQLException {
		List<Prodotto> prodotti = new ArrayList<>();
		String query = "SELECT * FROM Prodotto WHERE attivo = TRUE";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				prodotti.add(new Prodotto(rs.getInt("id"), rs.getInt("categoria_id"), rs.getString("nome"),
						rs.getString("descrizione"), rs.getDouble("prezzo"), rs.getInt("stock"),
						rs.getBoolean("attivo")));
			}
		}
		return prodotti;
	}

	// metodo per recuperare ultimi prodotti aggiunti, utile sia in home page che
	// nella categoria
	// ultime novità
	public List<Prodotto> findUltimeNovita(int pagina, int pageSize) throws SQLException {
		List<Prodotto> prodotti = new ArrayList<>();

		int offset = (pagina - 1) * pageSize;

		String query = "SELECT * FROM Prodotto WHERE attivo = TRUE ORDER BY id DESC LIMIT ? OFFSET ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {

			ps.setInt(1, pageSize);
			ps.setInt(2, offset);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				prodotti.add(new Prodotto(rs.getInt("id"), rs.getInt("categoria_id"), rs.getString("nome"),
						rs.getString("descrizione"), rs.getDouble("prezzo"), rs.getInt("stock"),
						rs.getBoolean("attivo")));
			}
		}
		return prodotti;
	}

	@Override
	public void update(Prodotto p) throws SQLException {
		String query = "UPDATE Prodotto SET categoria_id=?, nome=?, descrizione=?, prezzo=?, stock=? WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, p.getCategoriaId());
			ps.setString(2, p.getNome());
			ps.setString(3, p.getDescrizione());
			ps.setDouble(4, p.getPrezzo());
			ps.setInt(5, p.getStock());
			ps.setInt(6, p.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void delete(Integer id) throws SQLException {
		// SOFT DELETE: Non elimino la riga, la nascondo
		// ciò affinche nei dettagli dell'ordine siano visibili i prodotti acquistati
		String query = "UPDATE Prodotto SET attivo = FALSE WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}

	// conteggio prodotti per gestire la paginazione
	public int countProdottiByCategoriaId(int categoria_id) {
		String sql = "SELECT COUNT(*) FROM prodotto WHERE categoria_id = ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setInt(1,categoria_id);
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