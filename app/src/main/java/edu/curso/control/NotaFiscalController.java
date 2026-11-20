package edu.curso.control;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import edu.curso.DAO.NotaFiscalDAO;
import edu.curso.DAO.NotaFiscalDAOImplementation;
import edu.curso.entity.NotaFiscal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NotaFiscalController {

    private final NotaFiscalDAO i = new NotaFiscalDAOImplementation();

    public NotaFiscal toEntity(String numero, LocalDate dataEmissao, String fornecedor, String valorTotal, LocalDate dataVencimento, String observacao) {
        NotaFiscal nf = new NotaFiscal();
        nf.setNumero(Integer.parseInt(numero));
        nf.setDataEmissao(dataEmissao);
        nf.setFornecedor(fornecedor);
        nf.setValorTotal(Double.parseDouble(valorTotal.replace(",", ".")));
        nf.setDataVencimento(dataVencimento);
        nf.setObservacao(observacao); 
        return nf;
    }
 
    public String validarCampos(String txtNumero, LocalDate dpDataEmissao, String txtFornecedor, String txtValorTotal, LocalDate dpDataVencimento, String txtObservacao) {
        StringBuilder erros = new StringBuilder();
        double valorTotal = 0;
        if (txtNumero.isBlank() || dpDataEmissao == null || txtFornecedor.isBlank() || txtValorTotal.isBlank() || dpDataVencimento == null) {
            erros.append("- Todos os campos devem ser preenchidos corretamente\n");
            return erros.toString();
        }
        try {
            int numero = Integer.parseInt(txtNumero);
        } catch (NumberFormatException e) {
            erros.append("- Campo 'Número da NF' deve ser numérico e do tipo inteiro\n");
        }
        try {
            valorTotal = Double.parseDouble(txtValorTotal.replace(",", "."));
        } catch (NumberFormatException e) {
            erros.append("- Campo 'Valor Total' deve ser numérico\n");
        }
        if (valorTotal < 0) {
            erros.append(" - Campo 'Valor Total' deve ser positivo\n");
        }
        if (dpDataVencimento.isBefore(dpDataEmissao)) {
            erros.append("- Data de vencimento não pode ser inferior à data de emissão\n");
        }
        return erros.toString();
    }

    public void adicionar(NotaFiscal nf) {
        try {
            i.adicionar(nf);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar na DB");
            e.printStackTrace();
        }       
    }

    public ObservableList<NotaFiscal> pesquisar(String texto) {
        List<NotaFiscal> lista = new ArrayList<>();
        ObservableList<NotaFiscal> listaPesquisa = FXCollections.observableArrayList();
        try {
            lista = i.pesquisar(Integer.parseInt(texto));
            for (NotaFiscal nf : lista) {
                listaPesquisa.add(nf);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar na DB");
            e.printStackTrace();
        }
        return listaPesquisa;
    }

    public ObservableList<NotaFiscal> listarTodas() {
        ObservableList<NotaFiscal> lista = FXCollections.observableArrayList();
        try {
            lista.addAll(i.listarTodas());
        } catch (SQLException e) {
            System.out.println("Erro ao conectar na DB");
            e.printStackTrace();
        }
        return lista;
    }

    public void atualizar(NotaFiscal nf, int numeroOriginal, String fornecedorOriginal) {
        try {
            i.atualizar(nf, numeroOriginal, fornecedorOriginal);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar na DB");
            e.printStackTrace();
        }
    }

    public void deletar(int numero, String fornecedor) {
        try {
            i.deletar(numero, fornecedor);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar na DB");
            e.printStackTrace();
        }
    }

    public boolean notaExiste(NotaFiscal nf) {
        try {
            if (i.notaExiste(nf)) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar na DB");
            e.printStackTrace();
        }
        return false;
    }
    
}
