package edu.curso.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.curso.entity.NotaFiscal;

public class NotaFiscalDAOImplementation implements NotaFiscalDAO {

    Connection con;

    public NotaFiscalDAOImplementation() {
        try {
            Connection con = ResourceManager.getConnection();
            this.con = con;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar na DB");
            e.printStackTrace();
        }
    }

    public void adicionar(NotaFiscal nf) throws SQLException {
        try {
            String sql = "INSERT INTO notas_fiscais (numero, dataEmissao, fornecedor, valorTotal, dataVencimento, observacao) " 
                                                        + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, nf.getNumero());
            stm.setString(2, nf.getDataEmissao().toString());
            stm.setString(3, nf.getFornecedor());
            stm.setDouble(4, nf.getValorTotal());
            stm.setString(5, nf.getDataVencimento().toString());
            stm.setString(6, nf.getObservacao());
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar na DB");
            e.printStackTrace();
        }
    }

    public List<NotaFiscal> pesquisar (int numero) {
        List<NotaFiscal> lista = new ArrayList<>();
        try {
           String sql = "SELECT * FROM notas_fiscais WHERE numero = ?";
           PreparedStatement stm = con.prepareStatement(sql); 
           stm.setInt(1, numero);
           ResultSet rs = stm.executeQuery();
           while (rs.next()) {
                NotaFiscal nf = new NotaFiscal();
                nf.setNumero(rs.getInt("numero"));
                String dataTexto = (rs.getString("dataEmissao"));
                LocalDate data = LocalDate.parse(dataTexto);
                nf.setDataEmissao(data);
                nf.setFornecedor(rs.getString("fornecedor"));
                nf.setValorTotal(rs.getDouble("valorTotal"));
                dataTexto = (rs.getString("dataVencimento"));
                data = LocalDate.parse(dataTexto);
                nf.setDataVencimento(data);
                nf.setObservacao(rs.getString("observacao"));
                lista.add(nf);
           }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar na DB");
            e.printStackTrace();
        }
        return lista;
    }

    public void atualizar(NotaFiscal nf, int numeroOriginal, String fornecedorOriginal) {
        try {
            String sql = "UPDATE notas_fiscais SET numero = ?, dataEmissao = ?, fornecedor = ?, valorTotal = ?, dataVencimento = ?, observacao = ? WHERE numero = ? AND fornecedor = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, nf.getNumero());
            stm.setString(2, nf.getDataEmissao().toString());
            stm.setString(3, nf.getFornecedor());
            stm.setDouble(4, nf.getValorTotal());
            stm.setString(5, nf.getDataVencimento().toString());
            stm.setString(6, nf.getObservacao());
            stm.setInt(7, numeroOriginal);
            stm.setString(8, fornecedorOriginal);
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar na DB");
            e.printStackTrace();
        }
    }

    public void deletar(int numero, String fornecedor) {
        try {
            String sql = "DELETE FROM notas_fiscais WHERE numero = ? AND fornecedor = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, numero);
            stm.setString(2, fornecedor);
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar na DB");
        }
    }

    public boolean notaExiste(NotaFiscal nf) {
        try {
            String sql = "SELECT * FROM notas_fiscais WHERE numero = ? AND fornecedor = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, nf.getNumero());
            stm.setString(2, nf.getFornecedor());
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar na DB");
            e.printStackTrace();
        }
        return false;
    }
    public List<NotaFiscal> listarTodas() {
        List<NotaFiscal> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM notas_fiscais ORDER BY numero, fornecedor";
            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                NotaFiscal nf = new NotaFiscal();
                nf.setNumero(rs.getInt("numero"));
                String dataTexto = (rs.getString("dataEmissao"));
                LocalDate data = LocalDate.parse(dataTexto);
                nf.setDataEmissao(data);
                nf.setFornecedor(rs.getString("fornecedor"));
                nf.setValorTotal(rs.getDouble("valorTotal"));
                dataTexto = (rs.getString("dataVencimento"));
                data = LocalDate.parse(dataTexto);
                nf.setDataVencimento(data);
                nf.setObservacao(rs.getString("observacao"));
                lista.add(nf);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar na DB");
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void adicionarViaProcedure(NotaFiscal nf) throws SQLException {
        String sql = "{CALL sp_inserir_nota_fiscal(?, ?, ?, ?, ?, ?)}";

        try(CallableStatement cs = con.prepareCall(sql)){
            cs.setInt(1, nf.getNumero());
             cs.setDate(2, java.sql.Date.valueOf(nf.getDataEmissao()));
            cs.setString(3, nf.getFornecedor());
            cs.setDouble(4, nf.getValorTotal());
            cs.setDate(5, java.sql.Date.valueOf(nf.getDataVencimento()));
            cs.setString(6, nf.getObservacao());
            cs.execute();
        }
    }

}


