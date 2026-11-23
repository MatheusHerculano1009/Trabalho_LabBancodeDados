package edu.curso.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ResourceManager {

    private static final String URL = System.getenv().getOrDefault("DB_URL",
        "jdbc:mysql://localhost:3306/notas_db?createDatabaseIfNotExist=true");
    private static final String USUARIO = System.getenv().getOrDefault("DB_USER", "root");
    private static final String SENHA = System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(URL, USUARIO, SENHA);
        criarTabela(con);
        criarProcedure(con);
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

    // Recria a procedure a cada conexão, assim qualquer alteração aqui já vale no próximo run.
    // Obs.: DELIMITER é comando do cliente MySQL (Workbench/terminal), não é necessário via JDBC.
    public static void criarProcedure(Connection con) throws SQLException {
        String drop = "DROP PROCEDURE IF EXISTS sp_inserir_nota_fiscal";
        String create =
            "CREATE PROCEDURE sp_inserir_nota_fiscal(" +
            "    IN p_numero INT," +
            "    IN p_dataEmissao DATE," +
            "    IN p_fornecedor VARCHAR(100)," +
            "    IN p_valorTotal DECIMAL(12,2)," +
            "    IN p_dataVencimento DATE," +
            "    IN p_observacao VARCHAR(255)) " +
            "BEGIN " +
            "    IF p_valorTotal <= 0 THEN " +
            "        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Valor total deve ser maior que zero'; " +
            "    END IF; " +
            "    IF p_dataVencimento < p_dataEmissao THEN " +
            "        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Vencimento não pode ser anterior à emissão'; " +
            "    END IF; " +
            "    IF EXISTS (SELECT 1 FROM notas_fiscais " +
            "               WHERE numero = p_numero AND fornecedor = p_fornecedor) THEN " +
            "        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Nota já cadastrada para este fornecedor'; " +
            "    END IF; " +
            "    INSERT INTO notas_fiscais (numero, dataEmissao, fornecedor, valorTotal, dataVencimento, observacao) " +
            "    VALUES (p_numero, p_dataEmissao, p_fornecedor, p_valorTotal, p_dataVencimento, p_observacao); " +
            "END";
        try (Statement stm = con.createStatement()) {
            stm.execute(drop);
            stm.execute(create);
        }
    }

}
