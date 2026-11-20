package edu.curso.DAO;

import java.sql.SQLException;
import java.util.List;

import edu.curso.entity.NotaFiscal;

public interface NotaFiscalDAO {

    public void adicionar(NotaFiscal nf) throws SQLException;

    public void deletar(int numero, String fornecedor) throws SQLException;

    public List<NotaFiscal> pesquisar(int numero) throws SQLException;

    public List<NotaFiscal> listarTodas() throws SQLException;

    public void atualizar(NotaFiscal nf, int numeroOriginal, String fornecedorOriginal) throws SQLException;

    public boolean notaExiste(NotaFiscal nf) throws SQLException;
    
}
