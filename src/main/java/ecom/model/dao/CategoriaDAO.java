package ecom.model.dao;

import ecom.model.bean.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

public class CategoriaDAO implements GenericDAO<Categoria, Integer> {
	private DataSource ds;

	public CategoriaDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void insert(Categoria c) throws SQLException {
		String query = "INSERT INTO Categoria (nome, descrizione) VALUES (?, ?)";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, c.getNome());
			ps.setString(2, c.getDescrizione());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					c.setId(rs.getInt(1));
			}
		}
	}

	public int insertAndReturnId(Categoria c) throws SQLException {
		String query = "INSERT INTO Categoria (nome, descrizione) VALUES (?, ?)";
		int generatedId = 0;

		// Il secondo parametro dice a JDBC di farsi restituire l'ID generato
		try (PreparedStatement ps = ds.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, c.getNome());
			ps.setString(2, c.getDescrizione());
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
	public Categoria findById(Integer id) throws SQLException {
		String query = "SELECT * FROM Categoria WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return new Categoria(rs.getInt("id"), rs.getString("nome"), rs.getString("descrizione"));
			}
		}
		return null;
	}

	public Categoria findByNome(String nome) throws SQLException {
		String query = "SELECT * FROM categoria WHERE nome = ?";
		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, nome);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Categoria categoria = new Categoria();
					categoria.setId(rs.getInt("id"));
					categoria.setNome(rs.getString("nome"));
					categoria.setDescrizione(rs.getString("descrizione"));
					return categoria;
				}
			}
		}
		return null;
	}

	@Override
	public List<Categoria> findAll() throws SQLException {
		List<Categoria> list = new ArrayList<>();
		String query = "SELECT * FROM Categoria";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next())
				list.add(new Categoria(rs.getInt("id"), rs.getString("nome"), rs.getString("descrizione")));
		}
		return list;
	}

	// Metodo che restituisce le categorie in ordine di creaizone dalla piu recente
	// con logica di paginazione
	public List<Categoria> findAllPaginazione(int pagina, int pageSize) throws SQLException {
		int offset = (pagina - 1) * pageSize;
		List<Categoria> list = new ArrayList<>();

		String query = "SELECT * FROM Categoria ORDER BY id DESC LIMIT ? OFFSET ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {
			ps.setInt(1, pageSize);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				list.add(new Categoria(rs.getInt("id"), rs.getString("nome"), rs.getString("descrizione")));
		}
		return list;
	}

	// Metodo che restituisce le categorie in alfabetico
	public List<Categoria> findAlfabetico(int pagina, int pageSize) throws SQLException {
		int offset = (pagina - 1) * pageSize;
		List<Categoria> list = new ArrayList<>();

		String query = "SELECT * FROM Categoria ORDER BY nome ASC LIMIT ? OFFSET ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {
			ps.setInt(1, pageSize);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				list.add(new Categoria(rs.getInt("id"), rs.getString("nome"), rs.getString("descrizione")));
		}
		return list;
	}

	@Override
	public void update(Categoria c) throws SQLException {
		String query = "UPDATE Categoria SET nome=?, descrizione=? WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, c.getNome());
			ps.setString(2, c.getDescrizione());
			ps.setInt(3, c.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void delete(Integer id) throws SQLException {
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement("DELETE FROM Categoria WHERE id=?")) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}

	// conteggio categorie recenti per gestire la paginazione
	public int countCategorie() {
		String sql = "SELECT COUNT(*) FROM Categoria";

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

	// restituisce il numero di prodotti contenuti in ogni categoria
	public Map<String, Integer> getNumeroProdottiPerCategoria() {
		Map<String, Integer> result = new HashMap<>();
		String query = "SELECT c.nome, COUNT(*) as numero_prodotti " + "FROM prodotto p, categoria c "
				+ "WHERE c.id = p.categoria_id " + "GROUP BY c.id";

		try (Connection conn = ds.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				String nomeCategoria = rs.getString("nome");
				int numeroProdotti = rs.getInt("numero_prodotti");
				result.put(nomeCategoria, numeroProdotti);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
}