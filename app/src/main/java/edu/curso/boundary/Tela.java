package edu.curso.boundary;

import java.util.Optional;
import edu.curso.control.NotaFiscalController;
import edu.curso.entity.NotaFiscal;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Tela extends Application {

    NotaFiscalController c = new NotaFiscalController();

    TextField txtNumero = new TextField();
    DatePicker dpDataEmissao = new DatePicker();
    TextField txtFornecedor = new TextField();
    TextField txtValorTotal = new TextField();
    DatePicker dpDataVencimento = new DatePicker();
    TextField txtObservacao = new TextField();
    TextField txtPesquisar = new TextField();
    TableView<NotaFiscal> tabela = new TableView<>();

    public static void main (String [] args) {
        Application.launch(Tela.class, args);
    }

    public void start(Stage stage) {

        PainelNotas pane = new PainelNotas();
        Scene scen = new Scene(pane, 620, 620);
        stage.setTitle("CRUD - Nota Fiscal");
        stage.setScene(scen);
        stage.show();

    }

    class PainelNotas extends Pane {

        public PainelNotas() {

            Label lblNumero = new Label("Número da NF *");
            Label lblDataEmissao = new Label("Data de Emissão *");
            Label lblFornecedor = new Label("Fornecedor *");
            Label lblValorTotal = new Label("Valor Total *");
            Label lblDataVencimento = new Label("Data de Vencimento *");
            Label lblObservacao = new Label("Observação");

            lblNumero.relocate(10, 10);
            lblNumero.setFont(new Font(20));
            lblFornecedor.relocate(10, 60);
            lblFornecedor.setFont(new Font(20));
            lblDataEmissao.relocate(10, 110);
            lblDataEmissao.setFont(new Font(20));
            lblDataVencimento.relocate(10, 160);
            lblDataVencimento.setFont(new Font (20));
            lblValorTotal.setFont(new Font(20));
            lblValorTotal.relocate(10, 210);
            lblObservacao.setFont(new Font(20));
            lblObservacao.relocate(10, 260);

            txtNumero.relocate(300, 10);
            txtFornecedor.relocate(300, 60);
            dpDataEmissao.relocate(300,110);
            dpDataVencimento.relocate(300, 160);
            txtValorTotal.relocate(300, 210);
            txtObservacao.relocate(300, 260);
            txtPesquisar.relocate(300, 300);
            txtPesquisar.setPromptText("Digite o n° da NF");

            Button btnAdicionar = new Button("Adicionar");
            Button btnPesquisar = new Button("Pesquisar");
            Button btnDeletar = new Button("Deletar");
            Button btnAtualizar = new Button("Atualizar");
            Button btnLimpar = new Button("Limpar");

            btnAdicionar.relocate(10, 300);
            btnPesquisar.relocate(460, 300);
            btnDeletar.relocate(80, 300);
            btnAtualizar.relocate(137, 300);
            btnLimpar.relocate(460, 10);

            TableColumn<NotaFiscal, String> colNumero = new TableColumn<>("Número");
            colNumero.setCellValueFactory(
                f -> new ReadOnlyStringWrapper(String.valueOf(f.getValue().getNumero()))
            );
            TableColumn<NotaFiscal, String> colDataEmissao = new TableColumn<>("Emissão");
            colDataEmissao.setCellValueFactory(
                f -> new ReadOnlyStringWrapper(String.valueOf(f.getValue().getDataEmissao()))
            );
            TableColumn<NotaFiscal, String> colFornecedor = new TableColumn<>("Fornecedor");
            colFornecedor.setCellValueFactory(
                f -> new ReadOnlyStringWrapper(String.valueOf(f.getValue().getFornecedor()))
            );
            TableColumn<NotaFiscal, String> colValorTotal = new TableColumn<>("Valor Total");
            colValorTotal.setCellValueFactory(
                f -> new ReadOnlyStringWrapper(String.valueOf(f.getValue().getValorTotal()))
            );
            TableColumn<NotaFiscal, String> colDataVencimento = new TableColumn<>("Vencimento");
            colDataVencimento.setCellValueFactory(
                f -> new ReadOnlyStringWrapper(String.valueOf(f.getValue().getDataVencimento()))
            );
            TableColumn<NotaFiscal, String> colObservacao = new TableColumn<>("Observação");
            colObservacao.setCellValueFactory(
                f -> new ReadOnlyStringWrapper(String.valueOf(f.getValue().getObservacao()))
            );
            tabela.getColumns().addAll(colNumero, colFornecedor, colDataEmissao, colDataVencimento, colValorTotal, colObservacao);
            tabela.relocate(10, 350);
            tabela.setPrefSize(600, 400);

            tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, nf) -> {
            if (nf != null) {
                txtNumero.setText(String.valueOf(nf.getNumero()));
                dpDataEmissao.setValue(nf.getDataEmissao());
                txtFornecedor.setText(nf.getFornecedor());
                txtValorTotal.setText(String.valueOf(nf.getValorTotal()));
                dpDataVencimento.setValue(nf.getDataVencimento());
                txtObservacao.setText(nf.getObservacao());
            }
            });
            
            EventHandler<ActionEvent> manipulador = new Manipulador();

            btnAdicionar.setOnAction(manipulador);
            btnPesquisar.setOnAction(manipulador);
            btnDeletar.setOnAction(manipulador);
            btnAtualizar.setOnAction(manipulador);
            btnLimpar.setOnAction(manipulador);

            this.getChildren().addAll(lblNumero, lblDataEmissao, lblFornecedor, lblValorTotal,
                                lblDataVencimento, lblObservacao, txtNumero, txtFornecedor, dpDataEmissao,
                                dpDataVencimento, txtValorTotal, txtObservacao, txtPesquisar, btnAdicionar, 
                                btnPesquisar, btnDeletar, btnAtualizar, btnLimpar, tabela);

        }
    }

    class Manipulador implements EventHandler<ActionEvent> {

        public void handle (ActionEvent e) {

            Button botao = (Button) e.getSource();
            if (botao.getText().equals("Adicionar")) {
                String aviso = c.validarCampos(txtNumero.getText(), dpDataEmissao.getValue(), txtFornecedor.getText(),
                            txtValorTotal.getText(), dpDataVencimento.getValue(), txtObservacao.getText());
                if (!aviso.isBlank()) {
                    mensagemErroAdicao(aviso);
                    return;
                }
                NotaFiscal nf = c.toEntity(txtNumero.getText(), dpDataEmissao.getValue(), txtFornecedor.getText(),
                                txtValorTotal.getText(), dpDataVencimento.getValue(), txtObservacao.getText());
                if (c.notaExiste(nf)) {
                    mensagemErroAdicao("Já existe uma nota com esse número e fornecedor.\nSelecione-a na tabela e use 'Atualizar'.");
                } else {
                    c.adicionar(nf);
                    mensagemSucessoAdicao();
                    limparCampos();
                    tabela.setItems(c.listarTodas());
                }
            }
            if (botao.getText().equals("Pesquisar")) {
                String texto = txtPesquisar.getText();
                if (texto.isBlank()) {
                    tabela.setItems(c.listarTodas());
                    return;
                }
                try {
                    int numero = Integer.parseInt(texto);
                } catch (NumberFormatException n) {
                    return;
                }
                if (!texto.isEmpty()) {
                    tabela.setItems(c.pesquisar(texto));
                }
            }
            if (botao.getText().equals("Deletar")) {
                NotaFiscal nf = tabela.getSelectionModel().getSelectedItem();
                if (nf != null) {
                    if(mensagemConfirmacao()) {
                        c.deletar(nf.getNumero(), nf.getFornecedor());
                        mensagemSucessoDelecao();
                        limparCampos();
                        tabela.setItems(c.listarTodas());
                    }
                }
            }
            if (botao.getText().equals("Atualizar")) {
                NotaFiscal original = tabela.getSelectionModel().getSelectedItem();
                if (original == null) {
                    mensagemErroAdicao("Selecione uma nota na tabela para atualizar.");
                    return;
                }
                String aviso = c.validarCampos(txtNumero.getText(), dpDataEmissao.getValue(), txtFornecedor.getText(),
                                               txtValorTotal.getText(), dpDataVencimento.getValue(), txtObservacao.getText());
                if (!aviso.isBlank()) {
                    mensagemErroAdicao(aviso);
                    return;
                }
                NotaFiscal nova = c.toEntity(txtNumero.getText(), dpDataEmissao.getValue(), txtFornecedor.getText(),
                                             txtValorTotal.getText(), dpDataVencimento.getValue(), txtObservacao.getText());
                boolean chaveMudou = nova.getNumero() != original.getNumero() || !nova.getFornecedor().equals(original.getFornecedor());   
                if (chaveMudou && c.notaExiste(nova)) {
                    mensagemErroAdicao("Já existe outra nota com esse número e fornecedor.");
                    return;
                }
                c.atualizar(nova, original.getNumero(), original.getFornecedor());
                mensagemSucessoAtualizacao();
                limparCampos();
                tabela.setItems(c.listarTodas());                         
            }
            if (botao.getText().equals("Limpar")) {
                limparCampos();
            }
        }
    }

    public void mensagemErroAdicao(String aviso) {
        new Alert(AlertType.ERROR, "Não foi possível completar a ação!\n" +aviso).show();
    }

    public void mensagemSucessoAdicao() {
        new Alert(AlertType.INFORMATION, "Item adicionado com sucesso!\n").show();
    }

    public void mensagemSucessoAtualizacao() {
        new Alert(AlertType.INFORMATION, "Item atualizado com sucesso!\n").show();
    }

    public boolean mensagemConfirmacao() {
        Alert alerta = new Alert(AlertType.CONFIRMATION, "Tem certeja que deseja remover esse item?");
        ButtonType sim = new ButtonType("Sim");
        ButtonType nao = new ButtonType("Não");
        alerta.getButtonTypes().setAll(sim, nao);
        Optional<ButtonType> opcao = alerta.showAndWait();
        if (opcao.get() == sim) {
            return true;
        } else {
            return false;
        }
    }

    public void mensagemSucessoDelecao() {
        new Alert(AlertType.INFORMATION, "Item removido com sucesso!\n").show();
    }

    public void limparCampos() {
        tabela.getSelectionModel().clearSelection();
        txtNumero.clear();
        dpDataEmissao.setValue(null);
        txtFornecedor.clear();
        txtValorTotal.clear();
        dpDataVencimento.setValue(null);
        txtObservacao.clear();
    }

}




    
     

