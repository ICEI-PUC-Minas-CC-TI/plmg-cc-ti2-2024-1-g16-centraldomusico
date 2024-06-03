package dao;

import model.CasaDeShows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class CasaDeShowsDAO extends DAO {	
	public CasaDeShowsDAO() {
		super();
		conectar();
	}
	
	
	public void finalize() {
		close();
	}
	
	
	public boolean insert(CasaDeShows casadeshows) {
		boolean status = false;
		try {
			String sql = "INSERT INTO casadeshows (nomeCasa, nomeDono, valor, endereco, telefone,horario) "
		               + "VALUES ('" + casadeshows.getNome() + "', "
		               + casadeshows.getNomeDono() + ", " + casadeshows.getValor()+ ", " + casadeshows.getEndereco() + ", " + casadeshows.getTelefone()+ ", " + casadeshows.getHorarios()+ ", m, ?);";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}

	
	public CasaDeShows get(int id) {
		CasaDeShows casadeshows = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM casadeshows WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);	
	        if(rs.next()){            
	        	casadeshows = new CasaDeShows(rs.getInt("id"),rs.getString("nomecasa"),rs.getString("nomedono"),rs.getFloat("valor"),rs.getString("endereco"),rs.getString("telefone"), rs.getTimestamp("horarios").toLocalDateTime());
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return casadeshows;
	}
	
	
	public List<CasaDeShows> get() {
		return get("");
	}

	
	public List<CasaDeShows> getOrderByID() {
		return get("id");		
	}
	
	
	public List<CasaDeShows> getOrderByDescricao() {
		return get("descricao");		
	}
	
	
	public List<CasaDeShows> getOrderByPreco() {
		return get("preco");		
	}
	
	
	private List<CasaDeShows> get(String orderBy) {
		List<CasaDeShows> casasdeshows = new ArrayList<CasaDeShows>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM casadeshows" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	            	
	        	CasaDeShows p = new CasaDeShows(rs.getInt("id"),rs.getString("nomecasa"),rs.getString("nomedono"),rs.getFloat("valor"),rs.getString("endereco"),rs.getString("telefone"), rs.getTimestamp("horario").toLocalDateTime());
	        	casasdeshows.add(p);
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return casasdeshows;
	}
	public List<CasaDeShows> getAll() {
		List<CasaDeShows> casasdeshows = new ArrayList<CasaDeShows>();
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM casadeshows";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				CasaDeShows p = new CasaDeShows(rs.getInt("id"),rs.getString("nomecasa"),rs.getString("nomedono"),rs.getFloat("valor"),rs.getString("endereco"),rs.getString("telefone"), rs.getTimestamp("horario").toLocalDateTime());
				casasdeshows.add(p);
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return casasdeshows;
	}
	
	public boolean update(CasaDeShows casadeshow) {
		boolean status = false;
		try {  
			String sql = "UPDATE casadeshows SET nome = '" + casadeshow.getNome() + "', "
					   + "nomeDono = " + casadeshow.getNomeDono() + ", " 
					   + "valor = " + casadeshow.getValor() + ","
					   + "endereco = " + casadeshow.getEndereco() + ","
					   + "telefone = " + casadeshow.getTelefone() + ","
					   + "horario = ? WHERE id = " + casadeshow.getID();
			PreparedStatement st = conexao.prepareStatement(sql);
		    st.setTimestamp(1, Timestamp.valueOf(casadeshow.getHorarios()));
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
			st.executeUpdate("DELETE FROM casadeshows WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
}