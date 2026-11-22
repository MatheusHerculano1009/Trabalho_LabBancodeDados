package edu.curso.boundary;

import java.sql.SQLException;
import java.util.Optional;

import edu.curso.control.ErroBanco;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Alertas {

    public static void erro(String mensagem) {
        new Alert(AlertType.ERROR, "Não foi possível completar a ação!\n" + mensagem).show();
    }

    public static void erroBanco(SQLException e) {
        e.printStackTrace();   // detalhes técnicos ficam no terminal
        new Alert(AlertType.ERROR, ErroBanco.traduzir(e)).show();
    }

    public static void sucesso(String mensagem) {
        new Alert(AlertType.INFORMATION, mensagem).show();
    }

    public static boolean confirmar(String pergunta) {
        ButtonType sim = new ButtonType("Sim");
        ButtonType nao = new ButtonType("Não");
        Alert alerta = new Alert(AlertType.CONFIRMATION, pergunta, sim, nao);
        Optional<ButtonType> opcao = alerta.showAndWait();
        return opcao.isPresent() && opcao.get() == sim;
    }
}