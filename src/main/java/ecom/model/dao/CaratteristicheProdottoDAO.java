package ecom.model.dao;

import ecom.model.bean.CaratteristicheProdotto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
}