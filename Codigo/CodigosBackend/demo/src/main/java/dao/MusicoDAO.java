package dao;

import model.Musico;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class MusicoDAO extends DAO {	
	public MusicoDAO() {
		super();
		conectar();
	}
	
	
	public void finalize() {
		close();
	}
	
	
	public boolean insert(Musico musico) {
		boolean status = false;
		try {
			String sql = "INSERT INTO musico (nome, descricao, senha, cache, instrumento,objetivo,estilo) "
		               + "VALUES ('" + musico.getNome() + "', "
		               + musico.getDescricao() + ", " + musico.getSenha()+ ", " + musico.getCache()+ ", " + musico.getInstrumento()+ ", "+ musico.getObjetivo() + ", " + musico.getEstilo()+", m, ?);";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	
	public Musico get(int id) {
		Musico musico = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM musico WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	musico = new Musico(rs.getInt("id"), rs.getString("nome") ,rs.getString("senha"), rs.getString("objetivo"), rs.getString("instrumento"),
	        			rs.getString("descricao"),rs.getFloat("cache"),rs.getString("estilo")
	        			);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return musico;
	}
	
	
	public List<Musico> get() {
		return get("");
	}

	
	public List<Musico> getOrderByID() {
		return get("id");		
	}
	
	
	public List<Musico> getOrderByDescricao() {
		return get("descricao");		
	}
	
	
	public List<Musico> getOrderByPreco() {
		return get("preco");		
	}
	
	
	private List<Musico> get(String orderBy) {
		List<Musico> musicos = new ArrayList<Musico>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM musico" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	Musico p = new Musico(rs.getInt("id"), rs.getString("nome"), rs.getString("senha")
	        							, rs.getString("objetivo"), rs.getString("instrumento")
	        							, rs.getString("descricao"), (float)rs.getDouble("cache"), 
	        			                rs.getString("estilo"));
	        	musicos.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return musicos;
	}
	
	
	public boolean update(Musico musico) {
		boolean status = false;
		try {  
			String sql = "UPDATE produto SET nome = '" + musico.getNome() + "', "
						+ "senha = " + musico.getSenha() + ", " 			
					   + "cache = " + musico.getCache() + ", " 
					   + "instrumento = " + musico.getInstrumento() + ", " 
					   + "objetivo = " + musico.getObjetivo() + ", " 
					   + "estilo = " + musico.getEstilo() + "WHERE id = " + musico.getID();
			PreparedStatement st = conexao.prepareStatement(sql);
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
			st.executeUpdate("DELETE FROM musico WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
}