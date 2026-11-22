package edu.curso.control;

import java.sql.SQLException;
import java.util.List;

import edu.curso.DAO.FornecedorDAO;
import edu.curso.DAO.FornecedorDAOImplementation;
import edu.curso.entity.Fornecedor;

public class FornecedorController {

    private final FornecedorDAO dao = new FornecedorDAOImplementation();

    public String validarCampos(String cnpj, String razaoSocial, String email) {
        StringBuilder erros = new StringBuilder();
        String cnpjNumeros = somenteNumeros(cnpj);
        if (cnpjNumeros.length() != 14) {
            erros.append("- CNPJ deve ter 14 dígitos\n");
        }
        if (razaoSocial == null || razaoSocial.isBlank()) {
            erros.append("- Razão social é obrigatória\n");
        }
        if (email != null && !email.isBlank() && !email.contains("@")) {
            erros.append("- E-mail inválido\n");
        }
        return erros.toString();
    }

    public Fornecedor toEntity(int id, String cnpj, String razaoSocial, String telefone, String email) {
        Fornecedor f = new Fornecedor();
        f.setId(id);
        f.setCnpj(somenteNumeros(cnpj));
        f.setRazaoSocial(razaoSocial.trim());
        f.setTelefone(telefone);
        f.setEmail(email);
        return f;
    }

    public void adicionar(Fornecedor f) throws SQLException {
        dao.adicionar(f);
    }

    public void atualizar(Fornecedor f) throws SQLException {
        dao.atualizar(f);
    }

    public void deletar(int id) throws SQLException {
        dao.deletar(id);
    }

    public List<Fornecedor> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public List<Fornecedor> pesquisar(String trecho) throws SQLException {
        if (trecho == null || trecho.isBlank()) {
            return dao.listarTodos();
        }
        return dao.pesquisarPorNome(trecho.trim());
    }

    public boolean cnpjExiste(String cnpj, int idIgnorar) throws SQLException {
        return dao.cnpjExiste(somenteNumeros(cnpj), idIgnorar);
    }

    private String somenteNumeros(String texto) {
        return texto == null ? "" : texto.replaceAll("\\D", "");
    }
}