package dao;

import model.Musico;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MusicoDAO extends DAO {
    public MusicoDAO() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

    public Musico getByUsername(String username) {
        Musico musico = null;
        try {
            String sql = "SELECT * FROM musico WHERE nome = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
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
            rs.close();
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return musico;
    }
    //metodo que recebe um nome e verifica se ja existe um musico com esse nome
    public boolean checkNomeMusico(String username) {
        boolean status = false;
        try {
            String sql = "SELECT * FROM musico WHERE nome = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, username);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                System.out.println("Nome de usuario ja existe");
                status = true;
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("Erro ao checar nome de usuario (DAO): " + e.getMessage());
        }
        return status;
    }

    public boolean insert(Musico musico) {
        boolean status = false;
        try {
            System.out.println("MUSICO: "+musico);
            //printar todos os atributos do musico
            System.out.println("NOME: "+musico.getNome());
            System.out.println("DESCRICAO: "+musico.getDescricao());
            System.out.println("SENHA: "+musico.getSenha());
            System.out.println("CACHE: "+musico.getCache());
            System.out.println("INSTRUMENTO1: "+musico.getInstrumento1());
            System.out.println("INSTRUMENTO2: "+musico.getInstrumento2());
            System.out.println("INSTRUMENTO3: "+musico.getInstrumento3());
            System.out.println("OBJETIVO: "+musico.getObjetivo());
            System.out.println("ESTILO: "+musico.getEstilo());
            System.out.println("PROFILE_IMAGE: "+musico.getProfileImage());
            System.out.println("TELEFONE: "+musico.getTelefone());

            String encryptedPassword = encryptPassword(musico.getSenha());
    
            String sql = "INSERT INTO musico (nome, descricao, senha, cache, instrumento1, instrumento2, instrumento3, objetivo, estilo, profile_image,telefone) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, musico.getNome());
            st.setString(2, musico.getDescricao());
            st.setString(3, encryptedPassword);
            st.setFloat(4, musico.getCache());
            st.setString(5, musico.getInstrumento1());
            st.setString(6, musico.getInstrumento2());
            st.setString(7, musico.getInstrumento3());
            st.setString(8, musico.getObjetivo());
            st.setString(9, musico.getEstilo());
            st.setBytes(10, musico.getProfileImage()); // Set the image as a byte array
            st.setString(11, musico.getTelefone());
            st.executeUpdate();
            st.close();
            status = true;
            System.out.println("DEU BOM");
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
    public boolean login(String username, String password) {
        boolean status = false;
        try {
            // Criptografar a senha fornecida pelo usuário usando MD5
            String encryptedPassword = encryptPassword(password);
    
            // Recuperar a senha criptografada armazenada no banco de dados
            Musico musico = getByUsername(username);
            if (musico != null) {
                String storedPassword = musico.getSenha();
    
                // Comparar as senhas criptografadas
                if (storedPassword.equals(encryptedPassword)) {
                    status = true;
                    System.out.println("Login bem-sucedido");
                } else {
                    System.out.println("Nome de usuário ou senha inválidos");
                }
            } else {
                System.out.println("Usuário não encontrado");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return status;
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
    //funcao para cadastrar instrumento na tabela 'verifica', ela tem colunas nomeinstrumento foto e musico_id
    public boolean cadastrarInstrumento(String nomeInstrumento, byte[] foto, int musico_id) {
        boolean status = false;
        try {
            String sql = "INSERT INTO verifica (nomeinstrumento, imagem, musico_id) VALUES (?, ?, ?)";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setString(1, nomeInstrumento);
            st.setBytes(2, foto);
            st.setInt(3, musico_id);
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar instrumento (DAO): " + e.getMessage());
        }
        return status;
    }

    //funcao para buscar instrumentos do musico, vai receber o id do musico e retornar uma lista de instrumentos e suas respectivas imagens
    public List<String> buscarInstrumentos(int musico_id) {
        List<String> instrumentos = new ArrayList<String>();
        try {
            String sql = "SELECT nomeinstrumento FROM verifica WHERE musico_id = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, musico_id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                instrumentos.add(rs.getString("nomeinstrumento"));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar instrumentos (DAO): " + e.getMessage());
        }
        return instrumentos;
    }
    //funcao para buscar imagens dos instrumentos do musico, vai receber o id do musico e retornar uma lista de imagens
    public List<byte[]> buscarImagensInstrumentos(int musico_id) {
        List<byte[]> imagens = new ArrayList<byte[]>();
        try {
            String sql = "SELECT imagem FROM verifica WHERE musico_id = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, musico_id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                imagens.add(rs.getBytes("imagem"));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar imagens dos instrumentos (DAO): " + e.getMessage());
        }
        return imagens;
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
            String sql = "SELECT * FROM musico WHERE id = ?";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
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
                    rs.getString("estilo"),
                    rs.getBytes("profile_image"),
                    rs.getString("telefone")
                );
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
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

    public Musico getByUsuarioSenha(String username, String senha) {
        Musico musico = getByUsername(username);
        if (musico != null) {
            String encryptedPassword = encryptPassword(senha);
            if (musico.getSenha().equals(encryptedPassword)) {
                return musico;
            }
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
            String sql = "UPDATE musico SET nome = ?, descricao = ?, senha = ?, cache = ?, instrumento1 = ?, instrumento2 = ?, instrumento3 = ?, objetivo = ?, estilo = ?";
            
            // Adicionar profile_image apenas se não for null
            if (musico.getProfileImage() != null) {
                sql += ", profile_image = ?";
            }
            
            sql += " WHERE id = ?";
            
            PreparedStatement st = conexao.prepareStatement(sql);
            int parameterIndex = 1;
            
            st.setString(parameterIndex++, musico.getNome());
            st.setString(parameterIndex++, musico.getDescricao());
            st.setString(parameterIndex++, musico.getSenha());
            st.setFloat(parameterIndex++, musico.getCache());
            st.setString(parameterIndex++, musico.getInstrumento1());
            st.setString(parameterIndex++, musico.getInstrumento2());
            st.setString(parameterIndex++, musico.getInstrumento3());
            st.setString(parameterIndex++, musico.getObjetivo());
            st.setString(parameterIndex++, musico.getEstilo());
            
            // Adicionar profile_image apenas se não for null
            if (musico.getProfileImage() != null) {
                st.setBytes(parameterIndex++, musico.getProfileImage());
                System.out.println("Profile image not null");
            }
            
            st.setInt(parameterIndex++, musico.getId());
            
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

    //metodo para deletar musico, vai receber o id do musico e a senha, caso a senha esteja correta o musico sera deletado
    public boolean delete(int id, String senha) {
        boolean status = false;
        //criptografar a senha fornecida pelo usuario
        System.out.println("SENHA ANTES DE CRIPTOGRAFAR: "+senha);
        String encryptedPassword = encryptPassword(senha);
        try {
            //checar senha no banco de dados
            Musico musico = get(id);
            if (musico != null) {
                if (musico.getSenha().equals(encryptedPassword)) {
                    //remover musico das tabelas das quais ele faz parte
                    int bandaId = getBandaIdByMusicoId(id);
                    if (bandaId != -1) {
                        String sql = "DELETE FROM bandamusico WHERE musico_id = ?";
                        PreparedStatement st = conexao.prepareStatement(sql);
                        st.setInt(1, id);
                        st.executeUpdate();
                        st.close();
                    }
                    //deletar musico da tabela verifica
                    String sql2 = "DELETE FROM verifica WHERE musico_id = ?";
                    PreparedStatement st2 = conexao.prepareStatement(sql2);
                    st2.setInt(1, id);
                    st2.executeUpdate();
                    st2.close();
                    //deletar musico da tabela musico
                    String sql = "DELETE FROM musico WHERE id = ?";
                    PreparedStatement st = conexao.prepareStatement(sql);
                    st.setInt(1, id);
                    st.executeUpdate();
                    st.close();
                    status = true;
                } else {
                    System.out.println("Senha incorreta");
                }
            } else {
                System.out.println("Musico nao encontrado");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar musico (DAO): " + e.getMessage());
        }
        return status;
    }
}
