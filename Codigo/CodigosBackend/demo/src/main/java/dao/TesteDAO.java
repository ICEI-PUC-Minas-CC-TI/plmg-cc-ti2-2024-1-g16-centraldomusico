package dao;

public class TesteDAO {
    public static void main(String[] args) {
        DAO dao = new DAO();
        
        // Testar a conexão
        if (dao.conectar()) {
            System.out.println("Conexão estabelecida com sucesso!");
        } else {
            System.out.println("Falha ao estabelecer conexão.");
        }
        
        // Fechar a conexão
        if (dao.close()) {
            System.out.println("Conexão fechada com sucesso!");
        } else {
            System.out.println("Falha ao fechar a conexão.");
        }
    }
}
