package ecom.model.dao;

import ecom.model.bean.Prodotto;
import ecom.model.bean.ProdottoConUltimoAcquisto;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

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

	public int insertAndReturnId(Prodotto p) throws SQLException {
		String query = "INSERT INTO Prodotto (categoria_id, nome, descrizione, prezzo, stock, attivo) VALUES (?, ?, ?, ?, ?, ?)";
		int generatedId = 0;

		// Il secondo parametro dice a JDBC di farsi restituire l'ID generato
		try (PreparedStatement ps = ds.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, p.getCategoriaId());
			ps.setString(2, p.getNome());
			ps.setString(3, p.getDescrizione());
			ps.setDouble(4, p.getPrezzo());
			ps.setInt(5, p.getStock());
			ps.setBoolean(6, true); // Di default è attivo

			ps.executeUpdate();

			// Recupera l'ID appena generato
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					generatedId = rs.getInt(1);
				}
			}
		}
		return generatedId;
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

	public Prodotto findById_Admin(Integer id) throws SQLException {
		String query = "SELECT * FROM Prodotto WHERE id = ?";
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

	// metodo per ordinare i prodotti nella pagina categoria
	public List<Prodotto> findByCategoriaId_OrderBy(int pagina, int pageSize, int categoria_id, String order)
			throws SQLException {
		List<Prodotto> prodotti = new ArrayList<>();
		int offset = (pagina - 1) * pageSize;

		// Converto la stringa ordine in clausola SQL sicura
		String orderByClause;
		switch (order) {
		case "prezzo_asc":
			orderByClause = "prezzo ASC";
			break;
		case "prezzo_desc":
			orderByClause = "prezzo DESC";
			break;
		case "recenti":
		default:
			orderByClause = "id DESC";
			break;
		}

		String query = "SELECT * FROM Prodotto WHERE categoria_id = ? AND attivo = TRUE ORDER BY " + orderByClause
				+ " LIMIT ? OFFSET ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

			ps.setInt(1, categoria_id);
			ps.setInt(2, pageSize);
			ps.setInt(3, offset);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				prodotti.add(new Prodotto(rs.getInt("id"), rs.getInt("categoria_id"), rs.getString("nome"),
						rs.getString("descrizione"), rs.getDouble("prezzo"), rs.getInt("stock"),
						rs.getBoolean("attivo")));
			}
		}
		return prodotti;
	}

	/**
	 * Trova tutti i prodotti, ordinati dal più recente in base allo stato con
	 * logica di paginazione
	 */
	public List<Prodotto> findByStatus(int stato, int pagina, int pageSize) throws SQLException {
		List<Prodotto> prodotti = new ArrayList<>();
		String query = "SELECT * FROM Prodotto WHERE attivo = ? ORDER BY id DESC LIMIT ? OFFSET ?";
		int offset = (pagina - 1) * pageSize;

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, stato);
			ps.setInt(2, pageSize);
			ps.setInt(3, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					prodotti.add(new Prodotto(rs.getInt("id"), rs.getInt("categoria_id"), rs.getString("nome"),
							rs.getString("descrizione"), rs.getDouble("prezzo"), rs.getInt("stock"),
							rs.getBoolean("attivo")));
				}
			}
		}
		return prodotti;
	}

	/**
	 * Trova tutti i prodotti, ordinati dal più recente in base ad un range di stock
	 * con logica di paginazione
	 */
	public List<Prodotto> findByStockRange(int min, int max, int pagina, int pageSize) throws SQLException {
		List<Prodotto> prodotti = new ArrayList<>();
		String query = "SELECT * FROM Prodotto WHERE attivo = TRUE and stock >= ? and stock <= ? ORDER BY id DESC LIMIT ? OFFSET ?";
		int offset = (pagina - 1) * pageSize;

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, min);
			ps.setInt(2, max);
			ps.setInt(3, pageSize);
			ps.setInt(4, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					prodotti.add(new Prodotto(rs.getInt("id"), rs.getInt("categoria_id"), rs.getString("nome"),
							rs.getString("descrizione"), rs.getDouble("prezzo"), rs.getInt("stock"),
							rs.getBoolean("attivo")));
				}
			}
		}
		return prodotti;
	}

	/**
	 * Trova tutti i prodotti, ordinati dal più recente in base ad un range di stock
	 * minimo con logica di paginazione
	 */
	public List<Prodotto> findByStockMinimo(int min, int pagina, int pageSize) throws SQLException {
		List<Prodotto> prodotti = new ArrayList<>();
		String query = "SELECT * FROM Prodotto WHERE attivo = TRUE and stock >= ? ORDER BY id DESC LIMIT ? OFFSET ?";
		int offset = (pagina - 1) * pageSize;

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, min);
			ps.setInt(2, pageSize);
			ps.setInt(3, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					prodotti.add(new Prodotto(rs.getInt("id"), rs.getInt("categoria_id"), rs.getString("nome"),
							rs.getString("descrizione"), rs.getDouble("prezzo"), rs.getInt("stock"),
							rs.getBoolean("attivo")));
				}
			}
		}
		return prodotti;
	}

	/**
	 * Trova tutti i prodotti, ordinati dal più recente in base ad un range di stock
	 * massimo con logica di paginazione
	 */
	public List<Prodotto> findByStockMassimo(int max, int pagina, int pageSize) throws SQLException {
		List<Prodotto> prodotti = new ArrayList<>();
		String query = "SELECT * FROM Prodotto WHERE attivo = TRUE and stock <= ? ORDER BY id DESC LIMIT ? OFFSET ?";
		int offset = (pagina - 1) * pageSize;

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, max);
			ps.setInt(2, pageSize);
			ps.setInt(3, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					prodotti.add(new Prodotto(rs.getInt("id"), rs.getInt("categoria_id"), rs.getString("nome"),
							rs.getString("descrizione"), rs.getDouble("prezzo"), rs.getInt("stock"),
							rs.getBoolean("attivo")));
				}
			}
		}
		return prodotti;
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
	// nella pagina
	// prodotti di admin
	public List<Prodotto> findRecenti(int pagina, int pageSize) throws SQLException {
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

	/**
	 * Trova gli ultimi 10 prodotti terminati, dal piu recente (Pagina dashboard
	 * admin)
	 */
	public List<ProdottoConUltimoAcquisto> find10Terminati() throws SQLException {
	    int limit = 10;
	    List<ProdottoConUltimoAcquisto> prodotti = new ArrayList<>();
	    String query = "SELECT p.id AS prodotto_id, p.nome, p.stock, p.categoria_id, p.descrizione, p.prezzo, p.attivo, NULL AS ultimo_acquisto \r\n"
	            + "FROM prodotto p \r\n" + "WHERE p.stock = 0 \r\n"
	            + "AND NOT EXISTS (SELECT 1 FROM dettagli_ordine d WHERE d.prodotto_id = p.id)\r\n" + "UNION\r\n"
	            + "SELECT p.id AS prodotto_id, p.nome, p.stock, p.categoria_id, p.descrizione, p.prezzo, p.attivo, MAX(o.data) AS ultimo_acquisto \r\n"
	            + "FROM dettagli_ordine d \r\n" + "JOIN ordine o ON d.ordine_id = o.id \r\n"
	            + "JOIN prodotto p ON d.prodotto_id = p.id \r\n" + "WHERE p.stock = 0 \r\n"
	            + "GROUP BY p.id, p.nome, p.stock, p.categoria_id, p.descrizione, p.prezzo, p.attivo \r\n"
	            + "ORDER BY ultimo_acquisto DESC \r\n" + "LIMIT ?";

	    try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
	        ps.setInt(1, limit);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Prodotto prodotto = new Prodotto(rs.getInt("prodotto_id"), rs.getInt("categoria_id"),
	                        rs.getString("nome"), rs.getString("descrizione"), rs.getDouble("prezzo"),
	                        rs.getInt("stock"), rs.getBoolean("attivo"));

	                java.sql.Date ultimoAcquistoDate = rs.getDate("ultimo_acquisto");
	                LocalDate ultimoAcquisto = ultimoAcquistoDate != null ? ultimoAcquistoDate.toLocalDate() : null;

	                prodotti.add(new ProdottoConUltimoAcquisto(prodotto, ultimoAcquisto));
	            }
	        }
	    }
	    return prodotti;
	}

	@Override
	public void update(Prodotto p) throws SQLException {
		String query = "UPDATE Prodotto SET categoria_id=?, nome=?, descrizione=?, prezzo=?, stock=?, attivo = ? WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, p.getCategoriaId());
			ps.setString(2, p.getNome());
			ps.setString(3, p.getDescrizione());
			ps.setDouble(4, p.getPrezzo());
			ps.setInt(5, p.getStock());
			ps.setBoolean(6, p.isAttivo());
			ps.setInt(7, p.getId());
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

	// conteggio prodotti per gestire la paginazione in categoria
	public int countProdottiByCategoriaId(int categoria_id) {
		String sql = "SELECT COUNT(*) FROM prodotto WHERE categoria_id = ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setInt(1, categoria_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	// conteggio prodotti recenti per gestire la paginazione
	public int countProdotti() {
		String sql = "SELECT COUNT(*) FROM prodotto";

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

	// conteggio prodotti per stato per gestire la paginazione
	public int countProdottiByStatus(int stato) {
		String sql = "SELECT COUNT(*) FROM prodotto WHERE attivo = ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setInt(1, stato);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	// conteggio prodotti per range di stock per gestire la paginazione
	public int countProdottiByStockRange(int min, int max) {
		String sql = "SELECT COUNT(*) FROM prodotto WHERE attivo = TRUE and stock >= ? and stock <= ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setInt(1, min);
			ps.setInt(2, max);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	// conteggio prodotti per range di stock minimo per gestire la paginazione
	public int countProdottiByStockMin(int min) {
		String sql = "SELECT COUNT(*) FROM prodotto WHERE attivo = TRUE and stock >= ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setInt(1, min);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	// conteggio prodotti per range di stock massimo per gestire la paginazione
	public int countProdottiByStockMax(int max) {
		String sql = "SELECT COUNT(*) FROM prodotto WHERE attivo = TRUE and stock <= ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
			ps.setInt(1, max);
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