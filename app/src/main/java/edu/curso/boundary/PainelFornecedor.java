package edu.curso.boundary;

import java.sql.SQLException;

import edu.curso.control.FornecedorController;
import edu.curso.entity.Fornecedor;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// CRUD de Fornecedor
public class PainelFornecedor extends BorderPane {

    private final FornecedorController c = new FornecedorController();

    private final TextField txtCnpj = new TextField();
    private final TextField txtRazaoSocial = new TextField();
    private final TextField txtTelefone = new TextField();
    private final TextField txtEmail = new TextField();
    private final TextField txtPesquisar = new TextField();
    private final TableView<Fornecedor> tabela = new TableView<>();

    public PainelFornecedor() {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.addRow(0, new Label("CNPJ *"), txtCnpj, new Label("Razão social *"), txtRazaoSocial);
        form.addRow(1, new Label("Telefone"), txtTelefone, new Label("E-mail"), txtEmail);
        txtCnpj.setPromptText("Somente números");
        txtRazaoSocial.setPrefWidth(300);

        Button btnAdicionar = new Button("Adicionar");
        Button btnAtualizar = new Button("Atualizar");
        Button btnDeletar = new Button("Deletar");
        Button btnLimpar = new Button("Limpar");
        Button btnPesquisar = new Button("Pesquisar");
        txtPesquisar.setPromptText("Parte da razão social");
        HBox botoes = new HBox(8, btnAdicionar, btnAtualizar, btnDeletar, btnLimpar,
                new Label("   Pesquisar:"), txtPesquisar, btnPesquisar);

        btnAdicionar.setOnAction(e -> adicionar());
        btnAtualizar.setOnAction(e -> atualizar());
        btnDeletar.setOnAction(e -> deletar());
        btnLimpar.setOnAction(e -> limparCampos());
        btnPesquisar.setOnAction(e -> pesquisar());
        txtPesquisar.setOnAction(e -> pesquisar());   // Enter também pesquisa

        montarTabela();

        VBox topo = new VBox(12, form, botoes);
        topo.setPadding(new Insets(0, 0, 10, 0));
        setTop(topo);
        setCenter(tabela);
        setPadding(new Insets(12));

        pesquisar();
    }

    private void montarTabela() {
        TableColumn<Fornecedor, String> colCnpj = new TableColumn<>("CNPJ");
        colCnpj.setCellValueFactory(f -> new ReadOnlyStringWrapper(formatarCnpj(f.getValue().getCnpj())));
        TableColumn<Fornecedor, String> colRazao = new TableColumn<>("Razão social");
        colRazao.setCellValueFactory(f -> new ReadOnlyStringWrapper(f.getValue().getRazaoSocial()));
        colRazao.setPrefWidth(300);
        TableColumn<Fornecedor, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(f -> new ReadOnlyStringWrapper(f.getValue().getTelefone()));
        TableColumn<Fornecedor, String> colEmail = new TableColumn<>("E-mail");
        colEmail.setCellValueFactory(f -> new ReadOnlyStringWrapper(f.getValue().getEmail()));
        colEmail.setPrefWidth(250);

        tabela.getColumns().addAll(colCnpj, colRazao, colTelefone, colEmail);

        // Clicou numa linha: joga os dados no formulário
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, f) -> {
            if (f != null) {
                txtCnpj.setText(f.getCnpj());
                txtRazaoSocial.setText(f.getRazaoSocial());
                txtTelefone.setText(f.getTelefone());
                txtEmail.setText(f.getEmail());
            }
        });
    }

    private void adicionar() {
        String aviso = c.validarCampos(txtCnpj.getText(), txtRazaoSocial.getText(), txtEmail.getText());
        if (!aviso.isBlank()) {
            Alertas.erro(aviso);
            return;
        }
        try {
            if (c.cnpjExiste(txtCnpj.getText(), 0)) {
                Alertas.erro("Já existe um fornecedor com esse CNPJ.");
                return;
            }
            c.adicionar(c.toEntity(0, txtCnpj.getText(), txtRazaoSocial.getText(),
                    txtTelefone.getText(), txtEmail.getText()));
            Alertas.sucesso("Fornecedor adicionado com sucesso!");
            limparCampos();
            pesquisar();
        } catch (SQLException e) {
            Alertas.erroBanco(e);
        }
    }

    private void atualizar() {
        Fornecedor selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            Alertas.erro("Selecione um fornecedor na tabela para atualizar.");
            return;
        }
        String aviso = c.validarCampos(txtCnpj.getText(), txtRazaoSocial.getText(), txtEmail.getText());
        if (!aviso.isBlank()) {
            Alertas.erro(aviso);
            return;
        }
        try {
            if (c.cnpjExiste(txtCnpj.getText(), selecionado.getId())) {
                Alertas.erro("Já existe outro fornecedor com esse CNPJ.");
                return;
            }
            c.atualizar(c.toEntity(selecionado.getId(), txtCnpj.getText(), txtRazaoSocial.getText(),
                    txtTelefone.getText(), txtEmail.getText()));
            Alertas.sucesso("Fornecedor atualizado com sucesso!");
            limparCampos();
            pesquisar();
        } catch (SQLException e) {
            Alertas.erroBanco(e);
        }
    }

    private void deletar() {
        Fornecedor selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            Alertas.erro("Selecione um fornecedor na tabela para deletar.");
            return;
        }
        if (!Alertas.confirmar("Tem certeza que deseja remover " + selecionado.getRazaoSocial() + "?")) {
            return;
        }
        try {
            c.deletar(selecionado.getId());
            Alertas.sucesso("Fornecedor removido com sucesso!");
            limparCampos();
            pesquisar();
        } catch (SQLException e) {
            Alertas.erroBanco(e);  
        }
    }

    private void pesquisar() {
        try {
            tabela.setItems(FXCollections.observableArrayList(c.pesquisar(txtPesquisar.getText())));
        } catch (SQLException e) {
            Alertas.erroBanco(e);
        }
    }

    private void limparCampos() {
        tabela.getSelectionModel().clearSelection();
        txtCnpj.clear();
        txtRazaoSocial.clear();
        txtTelefone.clear();
        txtEmail.clear();
    }

    private String formatarCnpj(String c) {
        if (c == null || c.length() != 14) {
            return c;
        }
        return c.substring(0, 2) + "." + c.substring(2, 5) + "." + c.substring(5, 8) + "/"
             + c.substring(8, 12) + "-" + c.substring(12);
    }
}