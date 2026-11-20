package edu.curso.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ResourceManager {

    private static final String URL = "jdbc:mysql://localhost:3306/notas_db?createDatabaseIfNotExist=true";
    private static final String USUARIO = "root";
    private static final String SENHA = "alunofatec";

    public static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(URL, USUARIO, SENHA);
        criarTabela(con);
        return con;
    }

    public static void criarTabela(Connection con) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS notas_fiscais (" + 
                     "numero int not null, " +
                     "dataEmissao date, " +
                     "fornecedor varchar(100) not null, " +
                     "valorTotal decimal(12, 2), " +
                     "dataVencimento date, " +
                     "observacao varchar(255), " +
                     "primary key (numero, fornecedor)" +
                     ")";
        try (Statement stm = con.createStatement()) {
            stm.execute(sql);
        }
    }
}
