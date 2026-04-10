package ecom.model.dao;

import ecom.model.bean.Attributo;
import ecom.model.bean.AttributoDettagli;
import ecom.model.bean.ProdottoAssociato;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class AttributoDAO implements GenericDAO<Attributo, Integer> {
	private DataSource ds;

	public AttributoDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void insert(Attributo a) throws SQLException {
		String query = "INSERT INTO Attributo (nome) VALUES (?)";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, a.getNome());
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					a.setId(rs.getInt(1));
			}
		}
	}

	public int insertAndReturnId(Attributo a) throws SQLException {
		String query = "INSERT INTO Attributo (nome) VALUES (?)";
		int generatedId = 0;

		// Il secondo parametro dice a JDBC di farsi restituire l'ID generato
		try (PreparedStatement ps = ds.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, a.getNome());
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
	public Attributo findById(Integer id) throws SQLException {
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement("SELECT * FROM Attributo WHERE id = ?")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return new Attributo(rs.getInt("id"), rs.getString("nome"));
			}
		}
		return null;
	}

	@Override
	public List<Attributo> findAll() throws SQLException {
		List<Attributo> list = new ArrayList<>();
		String query = "SELECT * FROM Attributo";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next())
				list.add(new Attributo(rs.getInt("id"), rs.getString("nome")));
		}
		return list;
	}

	// Metodo che restituisce gli attributi in ordine di creaizone dal piu recente
	// con logica di paginazione
	public List<Attributo> findAllPaginazione(int pagina, int pageSize) throws SQLException {
		int offset = (pagina - 1) * pageSize;
		List<Attributo> list = new ArrayList<>();

		String query = "SELECT * FROM Attributo ORDER BY id DESC LIMIT ? OFFSET ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {
			ps.setInt(1, pageSize);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				list.add(new Attributo(rs.getInt("id"), rs.getString("nome")));
		}
		return list;
	}

	// Metodo che restituisce gli attributi in alfabetico
	public List<Attributo> findAlfabetico(int pagina, int pageSize) throws SQLException {
		int offset = (pagina - 1) * pageSize;
		List<Attributo> list = new ArrayList<>();

		String query = "SELECT * FROM Attributo ORDER BY nome ASC LIMIT ? OFFSET ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query);) {
			ps.setInt(1, pageSize);
			ps.setInt(2, offset);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				list.add(new Attributo(rs.getInt("id"), rs.getString("nome")));
		}
		return list;
	}

	//metodo che restituisce i valori assunti dall'attributo
    public AttributoDettagli getAttributoConProdotti(int attributoId) throws SQLException {
        String sql = "SELECT a.id as attributo_id, a.nome as attributo_nome, " +
                     "p.id as prodotto_id, p.nome as prodotto_nome, c.valoreAttr " +
                     "FROM attributo a " +
                     "INNER JOIN caratteristiche_prodotto c ON a.id = c.attributo_id " +
                     "INNER JOIN prodotto p ON c.prodotto_id = p.id " +
                     "WHERE a.id = ?";
        
        AttributoDettagli dettagli = null;
        
        try (Connection conn = ds.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, attributoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<ProdottoAssociato> prodottiAssociati = new ArrayList<>();
                String attributoNome = null;
                int attributoIdResult = 0;
                
                while (rs.next()) {
                    if (attributoNome == null) {
                        attributoIdResult = rs.getInt("attributo_id");
                        attributoNome = rs.getString("attributo_nome");
                    }
                    
                    ProdottoAssociato prodotto = new ProdottoAssociato();
                    prodotto.setProdottoId(rs.getInt("prodotto_id"));
                    prodotto.setProdottoNome(rs.getString("prodotto_nome"));
                    prodotto.setValoreAttr(rs.getString("valoreAttr"));
                    
                    prodottiAssociati.add(prodotto);
                }
                
                if (attributoNome != null) {
                    dettagli = new AttributoDettagli();
                    dettagli.setId(attributoIdResult);
                    dettagli.setNome(attributoNome);
                    dettagli.setProdottiAssociati(prodottiAssociati);
                }
            }
        }
        
        return dettagli;
    }
	
	@Override
	public void update(Attributo a) throws SQLException {
		String query = "UPDATE Attributo SET nome=? WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, a.getNome());
			ps.setInt(2, a.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void delete(Integer id) throws SQLException {
		String query = "DELETE FROM Attributo WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}

	// conteggio attributi per gestire la paginazione
	public int countAttributi() {
		String sql = "SELECT COUNT(*) FROM Attributo";

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