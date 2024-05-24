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
            String sql = "INSERT INTO banda (nome, descricao, senha, cache, datacriacao , objetivo, estilo) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setString(1, banda.getNome());
			st.setString(2, banda.getDescricao());
			st.setString(3, banda.getSenha());
			st.setFloat(4, banda.getCache());
			st.setTimestamp(5, Timestamp.valueOf(banda.getDataCriacaoTimestamp()));
			st.setString(6, banda.getObjetivo());
			st.setString(7, banda.getEstilo());
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
	        	banda = new Banda(rs.getInt("id"), rs.getString("nome") , rs.getString("descricao"), rs.getString("senha") ,rs.getFloat("cache"),rs.getString("estilo"),rs.getString("objetivo"));
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
	        	Banda p = new Banda(rs.getInt("id"), rs.getString("nome") , rs.getString("descricao"), rs.getString("senha") ,rs.getFloat("cache"),rs.getString("estilo"),rs.getString("objetivo"));
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
		    st.setTimestamp(1, Timestamp.valueOf(banda.getDataCriacaoTimestamp()));
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