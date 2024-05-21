package dao;

import model.Banda;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class BandaDAO extends DAO {	
	public BandaDAO() {
		super();
		conectar();
	}
	
	
	public void finalize() {
		close();
	}
	
	
	public boolean insert(Banda banda) {
		boolean status = false;
		try {
			String sql = "INSERT INTO banda (nome, descricao, senha, cache, dataCriacao,objetivo,estilo) "
		               + "VALUES ('" + banda.getNome() + "', "
		               + banda.getDescricao() + ", " + banda.getCache()+ ", " + banda.getDataCriacao() + ", " + banda.getEstilo()+", m, ?);";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	
	public Banda get(int id) {
		Banda banda = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM banda WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	banda = new Banda(rs.getInt("id"), rs.getString("nome") , rs.getString("descricao"), rs.getFloat("cache"),rs.getString("estilo"), rs.getTimestamp("datacriacao").toLocalDateTime());
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return banda;
	}
	
	
	public List<Banda> get() {
		return get("");
	}

	
	public List<Banda> getOrderByID() {
		return get("id");		
	}
	
	
	public List<Banda> getOrderByDescricao() {
		return get("descricao");		
	}
	
	
	public List<Banda> getOrderByPreco() {
		return get("preco");		
	}
	
	
	private List<Banda> get(String orderBy) {
		List<Banda> bandas = new ArrayList<Banda>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM banda" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Banda p = new Banda(rs.getInt("id"), rs.getString("nome"),rs.getString("descricao"),rs.getFloat("cache"), rs.getString("estilo")
	        							, rs.getTimestamp("dataCriacao").toLocalDateTime());
	        	bandas.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return bandas;
	}
	
	
	public boolean update(Banda banda) {
		boolean status = false;
		try {  
			String sql = "UPDATE banda SET nome = '" + banda.getNome() + "', "
					   + "descricao = " + banda.getDescricao() + ", " 
					   + "cache = " + banda.getCache() + ","
					   + "estilo = " + banda.getEstilo() + ","
					   + "datafabricacao = ? WHERE id = " + banda.getID();
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(banda.getDataCriacao()));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
	
	public boolean delete(int id) {
		boolean status = false;
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM banda WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
}