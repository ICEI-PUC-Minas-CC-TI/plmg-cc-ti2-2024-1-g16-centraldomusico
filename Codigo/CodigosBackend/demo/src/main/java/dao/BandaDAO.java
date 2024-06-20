package dao;

import model.Banda;
import model.Musico;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	
	public boolean joinBand(int bandaId, int musicoId) {
		boolean status = false;
		try {
			if (getNumberOfMembers(bandaId) >= 5) {
				throw new RuntimeException("A banda já tem o número máximo de membros.");
			}
	
			String sql = "INSERT INTO bandamusico (banda_id, musico_id) VALUES (?, ?)";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, bandaId);
			st.setInt(2, musicoId);
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return status;
	}
	
	public List<Musico> getBandaMembers(int bandaId) {
		List<Musico> membros = new ArrayList<>();
		try {
			String sql = "SELECT m.id, m.nome, m.instrumento1, m.instrumento2, m.instrumento3, m.profile_image " +
						 "FROM musico m " +
						 "JOIN bandamusico bm ON m.id = bm.musico_id " +
						 "WHERE bm.banda_id = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, bandaId);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Musico musico = new Musico();
				musico.setId(rs.getInt("id"));
				musico.setNome(rs.getString("nome"));
				musico.setInstrumento1(rs.getString("instrumento1"));
				musico.setInstrumento2(rs.getString("instrumento2"));
				musico.setInstrumento3(rs.getString("instrumento3"));
				
				// Verifica se profile_image não é null antes de atribuir
				if (rs.getObject("profile_image") != null) {
					musico.setProfileImage(rs.getBytes("profile_image"));
				}
	
				membros.add(musico);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return membros;
	}
	
	public boolean isUserInBand(int bandaId, int musicoId) {
		boolean isInBand = false;
		try {
			String sql = "SELECT COUNT(*) FROM bandamusico WHERE banda_id = ? AND musico_id = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, bandaId);
			st.setInt(2, musicoId);
			ResultSet rs = st.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				isInBand = true;
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return isInBand;
	}
	public boolean leaveBand(int bandaId, int musicoId) {
		boolean status = false;
		try {
			String sql = "DELETE FROM bandamusico WHERE banda_id = ? AND musico_id = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, bandaId);
			st.setInt(2, musicoId);
			int rowsAffected = st.executeUpdate();
			st.close();
			if (rowsAffected > 0) {
				status = true;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return status;
	}


	public int getNumberOfMembers(int bandaId) {
		int count = 0;
		try {
			String sql = "SELECT COUNT(*) FROM bandamusico WHERE banda_id = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, bandaId);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return count;
	}
	
	public Banda getByName(String nomeBanda) {
		Banda banda = null;
		try {
			String sql = "SELECT * FROM banda WHERE nome = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setString(1, nomeBanda);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				banda = new Banda(
					rs.getInt("id"),
					rs.getString("nome"),
					rs.getString("descricao"),
					rs.getString("senha"),
					rs.getFloat("cache"),
					rs.getString("estilo"),
					rs.getString("objetivo")
				);
				banda.setDataCriacaoTimestamp(rs.getTimestamp("datacriacao").toLocalDateTime());
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		// se der certo, pegamos os membros da banda
		return banda;
	}
    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	public boolean insert(Banda banda) {
		boolean status = false;
		String encryptedPassword = encryptPassword(banda.getSenha());

		try {
            String sql = "INSERT INTO banda (nome, descricao, senha, cache, datacriacao , objetivo, estilo) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement st = conexao.prepareStatement(sql);
			
			st.setString(1, banda.getNome());
			st.setString(2, banda.getDescricao());
			st.setString(3, encryptedPassword);
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
    public List<Banda> getAllBandas() {
        List<Banda> bandas = new ArrayList<>();
		System.out.println("getAllBandas");
        try {
			System.out.println("try getAllBandas");
            String sql = "SELECT * FROM banda";
            PreparedStatement st = conexao.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Banda banda = new Banda();
                banda.setNome(rs.getString("nome"));
                banda.setDescricao(rs.getString("descricao"));
                banda.setSenha(rs.getString("senha"));
                banda.setCache(rs.getFloat("cache"));
                banda.setDataCriacaoTimestamp(rs.getTimestamp("datacriacao").toLocalDateTime());
                banda.setObjetivo(rs.getString("objetivo"));
                banda.setEstilo(rs.getString("estilo"));
                bandas.add(banda);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bandas;
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
	
	//funcao para deletar banda, vai receber o id da banda e a senha dela, caso a senha esteja correta a banda sera deletada
	public boolean delete(int id, String senha) {
		boolean status = false;
		//criptografa a senha para comparar com a senha do banco
		String encryptedPassword = encryptPassword(senha);
		try {  
			String sql = "DELETE FROM banda WHERE id = ? AND senha = ?";
			PreparedStatement st = conexao.prepareStatement(sql);
			st.setInt(1, id);
			st.setString(2, encryptedPassword);
			st.executeUpdate();
			st.close();
			status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
		return status;
	}
}