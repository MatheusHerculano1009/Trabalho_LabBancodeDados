package edu.curso.DAO;

import java.sql.SQLException;
import java.util.List;

import edu.curso.entity.Fornecedor;

public interface FornecedorDAO {

    void adicionar(Fornecedor f) throws SQLException;

    void atualizar(Fornecedor f) throws SQLException;

    void deletar(int id) throws SQLException;

    List<Fornecedor> listarTodos() throws SQLException;

    List<Fornecedor> pesquisarPorNome(String trecho) throws SQLException;

    boolean cnpjExiste(String cnpj, int idIgnorar) throws SQLException;
}