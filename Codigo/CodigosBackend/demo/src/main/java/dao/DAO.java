package dao;

import java.sql.*;

public class DAO {
    protected Connection conexao;
    
    public DAO() {
        conexao = null;
    }
    
    public boolean conectar() {
        String driverName = "org.postgresql.Driver";                    
        String serverName = "central-do-musico-bd.postgres.database.azure.com";
        String mydatabase = "centraldomusico";
        int porta = 5432;
        String url = "jdbc:postgresql://" + serverName + ":" + porta + "/" + mydatabase;
        String username = "JesusCristo";
        String password = "A1b2c3d4";
        boolean status = false;
    
        try {
            Class.forName(driverName);
            conexao = DriverManager.getConnection(url, username, password);
            if (conexao != null) {
                status = true;
                System.out.println("Conexão bem-sucedida!");
            } else {
                System.out.println("Falha na conexão.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("O driver não foi encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
    
        return status;
    }
    
    public boolean close() {
        boolean status = false;
        
        if (conexao != null) {
            try {
                conexao.close();
                status = true;
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return status;
    }
}
