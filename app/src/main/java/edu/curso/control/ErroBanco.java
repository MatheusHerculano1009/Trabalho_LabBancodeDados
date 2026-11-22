package edu.curso.control;

import java.sql.SQLException;

public class ErroBanco {

    public static String traduzir(SQLException e) {
        
        if ("45000".equals(e.getSQLState())) {
            return e.getMessage();
        }
        
        if (e.getSQLState() != null && e.getSQLState().startsWith("08")) {
            return "Não foi possível conectar ao banco. Confira se o MySQL está ligado.";
        }
        switch (e.getErrorCode()) {
            case 1451:  
                return "Não é possível excluir: existem notas fiscais ligadas a este fornecedor.";
            case 1062:  
                return "Já existe um registro com esses dados (CNPJ ou número da nota repetido).";
            case 3819: 
                return "Valores inválidos: confira o valor e as datas.";
            case 0:     
            case 1045:  
            case 1049: 
                return "Não foi possível conectar ao banco. Confira se o MySQL está ligado "
                     + "e se o script.sql foi executado.\n(" + e.getMessage() + ")";
            default:
                return "Erro no banco de dados: " + e.getMessage();
        }
    }
}