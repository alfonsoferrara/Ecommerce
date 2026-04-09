package ecom.model.dao;

import ecom.model.bean.Ordine;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class OrdineDAO implements GenericDAO<Ordine, Integer> {
	private DataSource ds;

	public OrdineDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void insert(Ordine o) throws SQLException {
		String query = "INSERT INTO Ordine (cliente_id, indirizzo_id, totale, stato, metodo_pagamento, nota_cliente) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, o.getClienteId());
			ps.setInt(2, o.getIndirizzoId());
			ps.setDouble(3, o.getTotale());
			ps.setString(4, o.getStato());
			ps.setString(5, o.getMetodoPagamento());
			ps.setString(6, o.getNotaCliente());
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next())
					o.setId(rs.getInt(1));
			}
		}
	}

	@Override
	public Ordine findById(Integer id) throws SQLException {
		String query = "SELECT * FROM Ordine WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					// Costruttore senza 'spedito'
					return new Ordine(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("indirizzo_id"),
							rs.getTimestamp("data"), rs.getDouble("totale"), rs.getString("stato"),
							rs.getString("metodo_pagamento"), rs.getString("nota_cliente"));
				}
			}
		}
		return null;
	}

	@Override
	public List<Ordine> findAll() throws SQLException {
		List<Ordine> ordini = new ArrayList<>();
		String query = "SELECT * FROM Ordine";
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				ordini.add(new Ordine(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("indirizzo_id"),
						rs.getTimestamp("data"), rs.getDouble("totale"), rs.getString("stato"),
						rs.getString("metodo_pagamento"), rs.getString("nota_cliente")));
			}
		}
		return ordini;
	}

	/**
	 * Trova tutti gli ordini di un cliente, ordinati dal più recente al più vecchio
	 * con logica di paginazione
	 */
	public List<Ordine> findByClienteId(int clienteId, int pagina, int pageSize) throws SQLException {
		List<Ordine> ordini = new ArrayList<>();
		String query = "SELECT * FROM Ordine WHERE cliente_id = ? ORDER BY data DESC LIMIT ? OFFSET ?";
		int offset = (pagina - 1) * pageSize;

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, clienteId);
			ps.setInt(2, pageSize);
			ps.setInt(3, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ordini.add(new Ordine(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("indirizzo_id"),
							rs.getTimestamp("data"), rs.getDouble("totale"), rs.getString("stato"),
							rs.getString("metodo_pagamento"), rs.getString("nota_cliente")));
				}
			}
		}
		return ordini;
	}
	
	public List<Ordine> findByClienteId_noPaginazione(int clienteId) throws SQLException {
		List<Ordine> ordini = new ArrayList<>();
		String query = "SELECT * FROM Ordine WHERE cliente_id = ? ORDER BY data DESC";
		
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, clienteId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ordini.add(new Ordine(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("indirizzo_id"),
							rs.getTimestamp("data"), rs.getDouble("totale"), rs.getString("stato"),
							rs.getString("metodo_pagamento"), rs.getString("nota_cliente")));
				}
			}
		}
		return ordini;
	}

	/**
	 * Trova gli ultimi N ordini di un cliente, ordinati dal più recente, usato in
	 * profilo cliente
	 */
	public List<Ordine> findLastByClienteId(int clienteId, int limit) throws SQLException {
		List<Ordine> ordini = new ArrayList<>();
		String query = "SELECT * FROM Ordine WHERE cliente_id = ? ORDER BY data DESC LIMIT ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, clienteId);
			ps.setInt(2, limit);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ordini.add(new Ordine(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("indirizzo_id"),
							rs.getTimestamp("data"), rs.getDouble("totale"), rs.getString("stato"),
							rs.getString("metodo_pagamento"), rs.getString("nota_cliente")));
				}
			}
		}
		return ordini;
	}

	/**
	 * Conta quanti ordini ha un cliente, per gestione paginazione
	 */
	public int countByClienteId(int clienteId) throws SQLException {
		String query = "SELECT COUNT(*) FROM Ordine WHERE cliente_id = ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, clienteId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return 0;
	}

	/**
	 * Trova gli ultimi 10 ordini effettuati, ordinati dal più recente (Pagina
	 * dashboard admin)
	 */
	public List<Ordine> findLastXOrders(int pagina, int pageSize) throws SQLException {
		List<Ordine> ordini = new ArrayList<>();
		String query = "SELECT * FROM Ordine ORDER BY data DESC LIMIT ? OFFSET ?";
		int offset = (pagina - 1) * pageSize;

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, pageSize);
			ps.setInt(2, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ordini.add(new Ordine(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("indirizzo_id"),
							rs.getTimestamp("data"), rs.getDouble("totale"), rs.getString("stato"),
							rs.getString("metodo_pagamento"), rs.getString("nota_cliente")));
				}
			}
		}
		return ordini;
	}

	/**
	 * Trova tutti gli ordini, ordinati dal più recente in base al metodo di
	 * pagamento con logica di paginazione
	 */
	public List<Ordine> findByMetodoDiPagamento(String metodoDiPagamento, int pagina, int pageSize)
			throws SQLException {
		List<Ordine> ordini = new ArrayList<>();
		String query = "SELECT * FROM Ordine WHERE metodo_pagamento = ? ORDER BY data DESC LIMIT ? OFFSET ?";
		int offset = (pagina - 1) * pageSize;

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, metodoDiPagamento);
			ps.setInt(2, pageSize);
			ps.setInt(3, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ordini.add(new Ordine(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("indirizzo_id"),
							rs.getTimestamp("data"), rs.getDouble("totale"), rs.getString("stato"),
							rs.getString("metodo_pagamento"), rs.getString("nota_cliente")));
				}
			}
		}
		return ordini;
	}

	/**
	 * Trova tutti gli ordini, ordinati dal più recente in base allo stato con
	 * logica di paginazione
	 */
	public List<Ordine> findByStatus(String stato, int pagina, int pageSize) throws SQLException {
		List<Ordine> ordini = new ArrayList<>();
		String query = "SELECT * FROM Ordine WHERE stato = ? ORDER BY data DESC LIMIT ? OFFSET ?";
		int offset = (pagina - 1) * pageSize;

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, stato);
			ps.setInt(2, pageSize);
			ps.setInt(3, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ordini.add(new Ordine(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("indirizzo_id"),
							rs.getTimestamp("data"), rs.getDouble("totale"), rs.getString("stato"),
							rs.getString("metodo_pagamento"), rs.getString("nota_cliente")));
				}
			}
		}
		return ordini;
	}

	/**
	 * Trova tutti gli ordini in un range di date, ordinati dal più recente con
	 * logica di paginazione
	 */
	public List<Ordine> findByDateRange(String dataInizio, String dataFine, int pagina, int pageSize)
			throws SQLException {
		List<Ordine> ordini = new ArrayList<>();
		String query = "SELECT * FROM Ordine WHERE data BETWEEN ? AND ? ORDER BY data DESC LIMIT ? OFFSET ?";
		int offset = (pagina - 1) * pageSize;

		// Converte le stringhe in date SQL
		java.sql.Date startDate = java.sql.Date.valueOf(dataInizio);
		java.sql.Date endDate = java.sql.Date.valueOf(dataFine);

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setDate(1, startDate);
			ps.setDate(2, endDate);
			ps.setInt(3, pageSize);
			ps.setInt(4, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ordini.add(new Ordine(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("indirizzo_id"),
							rs.getTimestamp("data"), rs.getDouble("totale"), rs.getString("stato"),
							rs.getString("metodo_pagamento"), rs.getString("nota_cliente")));
				}
			}
		}
		return ordini;
	}

	/**
	 * Trova tutti gli ordini da una certa data di inizio in poi, ordinati dal più
	 * recente con logica di paginazione
	 */
	public List<Ordine> findByDataInizio(String dataInizio, int pagina, int pageSize) throws SQLException {
		List<Ordine> ordini = new ArrayList<>();
		String query = "SELECT * FROM Ordine WHERE data >= ? ORDER BY data DESC LIMIT ? OFFSET ?";
		int offset = (pagina - 1) * pageSize;

		// Converte la stringa in data SQL
		java.sql.Date startDate = java.sql.Date.valueOf(dataInizio);

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setDate(1, startDate);
			ps.setInt(2, pageSize);
			ps.setInt(3, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ordini.add(new Ordine(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("indirizzo_id"),
							rs.getTimestamp("data"), rs.getDouble("totale"), rs.getString("stato"),
							rs.getString("metodo_pagamento"), rs.getString("nota_cliente")));
				}
			}
		}
		return ordini;
	}

	/**
	 * Trova tutti gli ordini da una certa data di fine, ordinati dal più recente
	 * con logica di paginazione
	 */
	public List<Ordine> findByDataFine(String dataFine, int pagina, int pageSize) throws SQLException {
		List<Ordine> ordini = new ArrayList<>();
		String query = "SELECT * FROM Ordine WHERE data <= ? ORDER BY data DESC LIMIT ? OFFSET ?";
		int offset = (pagina - 1) * pageSize;

		// Converte la stringa in data SQL
		java.sql.Date endDate = java.sql.Date.valueOf(dataFine);

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setDate(1, endDate);
			ps.setInt(2, pageSize);
			ps.setInt(3, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ordini.add(new Ordine(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("indirizzo_id"),
							rs.getTimestamp("data"), rs.getDouble("totale"), rs.getString("stato"),
							rs.getString("metodo_pagamento"), rs.getString("nota_cliente")));
				}
			}
		}
		return ordini;
	}

	@Override
	public void update(Ordine o) throws SQLException {
		String query = "UPDATE Ordine SET stato=?, metodo_pagamento=?, nota_cliente=? WHERE id=?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setString(1, o.getStato());
			ps.setString(2, o.getMetodoPagamento());
			ps.setString(3, o.getNotaCliente());
			ps.setInt(4, o.getId());
			ps.executeUpdate();
		}
	}

	@Override
	public void delete(Integer id) throws SQLException {
		String query = "DELETE FROM Ordine WHERE id = ?";
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		}
	}

	// conteggio ordini per gestire la paginazione nella pagine Ordini Admin
	public int countOrdini() {
		String sql = "SELECT COUNT(*) FROM Ordine";

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
	
	/**
	 * Conta gli ordini per metodo di pagamento
	 */
	public int countByMetodoDiPagamento(String metodoDiPagamento) throws SQLException {
	    String query = "SELECT COUNT(*) FROM Ordine WHERE metodo_pagamento = ?";
	    
	    try (Connection con = ds.getConnection(); 
	         PreparedStatement ps = con.prepareStatement(query)) {
	        ps.setString(1, metodoDiPagamento);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	            return 0;
	        }
	    }
	}

	/**
	 * Conta gli ordini per stato
	 */
	public int countByStatus(String stato) throws SQLException {
	    String query = "SELECT COUNT(*) FROM Ordine WHERE stato = ?";
	    
	    try (Connection con = ds.getConnection(); 
	         PreparedStatement ps = con.prepareStatement(query)) {
	        ps.setString(1, stato);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	            return 0;
	        }
	    }
	}

	/**
	 * Conta gli ordini in un range di date
	 */
	public int countByDateRange(String dataInizio, String dataFine) throws SQLException {
	    String query = "SELECT COUNT(*) FROM Ordine WHERE data BETWEEN ? AND ?";
	    
	    java.sql.Date startDate = java.sql.Date.valueOf(dataInizio);
	    java.sql.Date endDate = java.sql.Date.valueOf(dataFine);
	    
	    try (Connection con = ds.getConnection(); 
	         PreparedStatement ps = con.prepareStatement(query)) {
	        ps.setDate(1, startDate);
	        ps.setDate(2, endDate);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	            return 0;
	        }
	    }
	}

	/**
	 * Conta gli ordini da una certa data di inizio in poi
	 */
	public int countByDataInizio(String dataInizio) throws SQLException {
	    String query = "SELECT COUNT(*) FROM Ordine WHERE data >= ?";
	    
	    java.sql.Date startDate = java.sql.Date.valueOf(dataInizio);
	    
	    try (Connection con = ds.getConnection(); 
	         PreparedStatement ps = con.prepareStatement(query)) {
	        ps.setDate(1, startDate);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	            return 0;
	        }
	    }
	}

	/**
	 * Conta gli ordini fino a una certa data di fine
	 */
	public int countByDataFine(String dataFine) throws SQLException {
	    String query = "SELECT COUNT(*) FROM Ordine WHERE data <= ?";
	    
	    java.sql.Date endDate = java.sql.Date.valueOf(dataFine);
	    
	    try (Connection con = ds.getConnection(); 
	         PreparedStatement ps = con.prepareStatement(query)) {
	        ps.setDate(1, endDate);
	        
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	            return 0;
	        }
	    }
	}

	/**
	 * Conta tutti gli ordini (per il caso "recenti")
	 */
	public int countAll() throws SQLException {
	    String query = "SELECT COUNT(*) FROM Ordine";
	    
	    try (Connection con = ds.getConnection(); 
	         PreparedStatement ps = con.prepareStatement(query)) {
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	            return 0;
	        }
	    }
	}

}