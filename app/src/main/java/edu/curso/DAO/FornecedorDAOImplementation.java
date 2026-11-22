package edu.curso.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.curso.entity.Fornecedor;

public class FornecedorDAOImplementation implements FornecedorDAO {

    
    @Override
    public void adicionar(Fornecedor f) throws SQLException {
        String sql = "INSERT INTO fornecedor (cnpj, razao_social, telefone, email) VALUES (?, ?, ?, ?)";
        try (Connection con = ResourceManager.getConnection();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, f.getCnpj());
            stm.setString(2, f.getRazaoSocial());
            stm.setString(3, f.getTelefone());
            stm.setString(4, f.getEmail());
            stm.executeUpdate();
        }
    }

    
    @Override
    public void atualizar(Fornecedor f) throws SQLException {
        String sql = "UPDATE fornecedor SET cnpj = ?, razao_social = ?, telefone = ?, email = ? WHERE id = ?";
        try (Connection con = ResourceManager.getConnection();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, f.getCnpj());
            stm.setString(2, f.getRazaoSocial());
            stm.setString(3, f.getTelefone());
            stm.setString(4, f.getEmail());
            stm.setInt(5, f.getId());
            stm.executeUpdate();
        }
    }

    
    @Override
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM fornecedor WHERE id = ?";
        try (Connection con = ResourceManager.getConnection();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, id);
            stm.executeUpdate();
        }
    }

    
    @Override
    public List<Fornecedor> listarTodos() throws SQLException {
        String sql = "SELECT * FROM fornecedor ORDER BY razao_social";
        try (Connection con = ResourceManager.getConnection();
             PreparedStatement stm = con.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {
            return montarLista(rs);
        }
    }

    @Override
    public List<Fornecedor> pesquisarPorNome(String trecho) throws SQLException {
        String sql = "SELECT * FROM fornecedor WHERE razao_social LIKE ? ORDER BY razao_social";
        try (Connection con = ResourceManager.getConnection();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, "%" + trecho + "%");
            try (ResultSet rs = stm.executeQuery()) {
                return montarLista(rs);
            }
        }
    }

    
    @Override
    public boolean cnpjExiste(String cnpj, int idIgnorar) throws SQLException {
        String sql = "SELECT 1 FROM fornecedor WHERE cnpj = ? AND id <> ?";
        try (Connection con = ResourceManager.getConnection();
             PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, cnpj);
            stm.setInt(2, idIgnorar);
            try (ResultSet rs = stm.executeQuery()) {
                return rs.next();
            }
        }
    }

    private List<Fornecedor> montarLista(ResultSet rs) throws SQLException {
        List<Fornecedor> lista = new ArrayList<>();
        while (rs.next()) {
            Fornecedor f = new Fornecedor();
            f.setId(rs.getInt("id"));
            f.setCnpj(rs.getString("cnpj"));
            f.setRazaoSocial(rs.getString("razao_social"));
            f.setTelefone(rs.getString("telefone"));
            f.setEmail(rs.getString("email"));
            lista.add(f);
        }
        return lista;
    }
}