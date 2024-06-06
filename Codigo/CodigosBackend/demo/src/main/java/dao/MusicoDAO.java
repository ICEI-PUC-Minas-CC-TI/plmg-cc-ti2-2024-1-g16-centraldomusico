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
            String sql = "INSERT INTO musico (nome, descricao, senha, cache, instrumento1, instrumento2, instrumento3, objetivo, estilo) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, musico.getNome());
            st.setString(2, musico.getDescricao());
            st.setString(3, musico.getSenha());
            st.setFloat(4, musico.getCache());
            st.setString(5, musico.getInstrumento1());
            st.setString(6, musico.getInstrumento2());
            st.setString(7, musico.getInstrumento3());
            st.setString(8, musico.getObjetivo());
            st.setString(9, musico.getEstilo());
            st.executeUpdate();
            st.close();
            status = true;
            System.out.println("DEU BOM");
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }

    //funcao para checar se o musico tem uma banda
    public String checkBanda(int id) {
        String bandaNome = null;
        try {
            String sql = "SELECT b.nome FROM banda b " +
                         "JOIN bandamusico bm ON b.id = bm.banda_id " +
                         "WHERE bm.musico_id = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                bandaNome = rs.getString("nome");
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("Erro ao checar se o musico tem banda (DAO): " + e.getMessage());
        }
        return bandaNome;
    }

    public String getBandaNomeByMusicoId(int musicoId) {
        String bandaNome = null;
        try {
            String sql = "SELECT b.nome FROM banda b " +
                         "JOIN bandamusico bm ON b.id = bm.banda_id " +
                         "WHERE bm.musico_id = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, musicoId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                bandaNome = rs.getString("nome");
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar nome da banda (DAO): " + e.getMessage());
        }
        return bandaNome;
    }
    

    public Musico get(int id) {
        Musico musico = null;
        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM musico WHERE id=" + id;
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                musico = new Musico(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getString("senha"),
                    rs.getFloat("cache"),
                    rs.getString("instrumento1"),
                    rs.getString("instrumento2"),
                    rs.getString("instrumento3"),
                    rs.getString("objetivo"),
                    rs.getString("estilo")
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

    public Musico getByUsuarioSenha(String usuario, String senha) {
        // Substitua pela lógica real para buscar o usuário no banco de dados
        String query = "SELECT * FROM musico WHERE nome = ? AND senha = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(query)) {
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Musico(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getString("senha"),
                    rs.getFloat("cache"),
                    rs.getString("instrumento1"),
                    rs.getString("instrumento2"),
                    rs.getString("instrumento3"),
                    rs.getString("objetivo"),
                    rs.getString("estilo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Musico> get(String orderBy) {
        List<Musico> musicos = new ArrayList<Musico>();

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM musico" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Musico musico = new Musico(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getString("senha"),
                    rs.getFloat("cache"),
                    rs.getString("instrumento1"),
                    rs.getString("instrumento2"),
                    rs.getString("instrumento3"),
                    rs.getString("objetivo"),
                    rs.getString("estilo")
                );
                musicos.add(musico);
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
            String sql = "UPDATE musico SET nome = ?, descricao = ?, senha = ?, cache = ?, instrumento1 = ?, instrumento2 = ?, instrumento3 = ?, objetivo = ?, estilo = ? WHERE id = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, musico.getNome());
            st.setString(2, musico.getDescricao());
            st.setString(3, musico.getSenha());
            st.setFloat(4, musico.getCache());
            st.setString(5, musico.getInstrumento1());
            st.setString(6, musico.getInstrumento2());
            st.setString(7, musico.getInstrumento3());
            st.setString(8, musico.getObjetivo());
            st.setString(9, musico.getEstilo());
            st.setInt(10, musico.getId());
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }
    //implementar metodo getBandaIdByMusicoId
    public int getBandaIdByMusicoId(int musicoId) {
        int id = -1;
        try {
            String sql = "SELECT banda_id FROM bandamusico WHERE musico_id = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, musicoId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                id = rs.getInt("banda_id");
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar id da banda pelo musico id (DAO): " + e.getMessage());
        }
        return id;
    }
    //pegar id da banda pelo nome
    public int getBandaIdByNome(String nome) {
        int id = -1;
        try {
            String sql = "SELECT id FROM banda WHERE nome = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, nome);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar id da banda pelo nome (DAO): " + e.getMessage());
        }
        return id;
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
