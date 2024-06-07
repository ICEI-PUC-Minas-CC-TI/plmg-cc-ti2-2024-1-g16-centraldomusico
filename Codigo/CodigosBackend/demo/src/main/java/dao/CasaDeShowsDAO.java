package dao;

import model.CasaDeShows;
import model.Banda;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.*;


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
				LocalTime horario = rs.getTimestamp("horario").toLocalDateTime().toLocalTime();
           
	        	casadeshows = new CasaDeShows(rs.getInt("id"),rs.getString("nomecasa"),rs.getString("nomedono"),rs.getFloat("valor"),rs.getString("endereco"),rs.getString("telefone"), horario);
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
	
	//implementar metodo de postar anuncio
	public boolean postarAnuncio(CasaDeShows casa) {
		boolean status = false;
		try {
			System.out.println("casa de shows: "+casa);
			String sql = "INSERT INTO casadeshows (nomecasa, nomedono, valor, endereco, telefone, horario) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setString(1, casa.getNome());
			st.setString(2, casa.getNomeDono());
			st.setFloat(3, casa.getValor());
			st.setString(4, casa.getEndereco());
			st.setString(5, casa.getTelefone());
			//inserir horario, ex 21:00
			st.setTimestamp(6, Timestamp.valueOf(casa.getHorarios().atDate(LocalDate.now())));
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}
	
	private List<CasaDeShows> get(String orderBy) {
		List<CasaDeShows> casasdeshows = new ArrayList<CasaDeShows>();
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM casadeshows" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
			ResultSet rs = st.executeQuery(sql);	           
	        while(rs.next()) {	   
				LocalTime horario = rs.getTimestamp("horario").toLocalDateTime().toLocalTime();         	
	        	CasaDeShows p = new CasaDeShows(rs.getInt("id"),rs.getString("nomecasa"),rs.getString("nomedono"),rs.getFloat("valor"),rs.getString("endereco"),rs.getString("telefone"),horario);
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
				LocalTime horario = rs.getTimestamp("horario").toLocalDateTime().toLocalTime();
				CasaDeShows p = new CasaDeShows(rs.getInt("id"),rs.getString("nomecasa"),rs.getString("nomedono"),rs.getFloat("valor"),rs.getString("endereco"),rs.getString("telefone"), horario);
				casasdeshows.add(p);
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return casasdeshows;
	}
	//criar metodo getbandas para pegar todas as bandas inscritas nesse evento
	public List<Banda> getBandas(int id) {
		List<Banda> bandas = new ArrayList<Banda>();
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM banda INNER JOIN casamusico ON banda.id = casamusico.banda_id WHERE casamusico.casa_id = " + id;
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				Banda p = new Banda(rs.getInt("id"),rs.getString("nome"),rs.getString("descricao"),rs.getString("senha"),rs.getFloat("cache"),rs.getString("estilo"),rs.getString("objetivo"));
				bandas.add(p);
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return bandas;
	}
	//criar metodo desinscreverBanda para desinscrever a banda desse evento
	public boolean desinscreverBanda(int casaId, int bandaId) {
		boolean status = false;
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "DELETE FROM casamusico WHERE casa_id = " + casaId + " AND banda_id = " + bandaId;
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return status;
	}
	//criar metodo isInscrito para verificar se a banda esta inscrita nesse evento
	public boolean isInscrito(int casaId, int bandaId) {
		boolean status = false;
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM casamusico WHERE casa_id = " + casaId + " AND banda_id = " + bandaId;
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()) {
				status = true;
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return status;
	}
	public boolean inscreverBanda(int casaId, int bandaId) {
        boolean status = false;
        try {
            String sql = "INSERT INTO casamusico (casa_id, banda_id) VALUES (?, ?)";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, casaId);
            st.setInt(2, bandaId);
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;	
    }
    //criar metodo getAnunciosBanda que retorna todos os eventos que a banda está inscrita
	public List<CasaDeShows> getAnunciosBanda(int bandaId) {
		System.out.println("Banda ID: " + bandaId);
		List<CasaDeShows> casas = new ArrayList<CasaDeShows>();
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM casadeshows INNER JOIN casamusico ON casadeshows.id = casamusico.casa_id WHERE casamusico.banda_id = " + bandaId;
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				LocalTime horario = rs.getTimestamp("horario").toLocalDateTime().toLocalTime();
				CasaDeShows p = new CasaDeShows(rs.getInt("id"), rs.getString("nomecasa"), rs.getString("nomedono"), rs.getFloat("valor"), rs.getString("endereco"), rs.getString("telefone"), horario);
				casas.add(p);
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return casas;
	}
    public CasaDeShows getCasaDeShows(int id) {
        CasaDeShows casa = null;
        try {
            PreparedStatement ps = conexao.prepareStatement("SELECT * FROM casadeshows WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                casa = new CasaDeShows(
                    rs.getInt("id"),
                    rs.getString("nomecasa"),
                    rs.getString("endereco"),
                    rs.getFloat("valor"),
                    rs.getTimestamp("horario").toLocalDateTime().toLocalTime(),
                    rs.getString("nomedono"),
                    rs.getString("telefone")
                );
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return casa;
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