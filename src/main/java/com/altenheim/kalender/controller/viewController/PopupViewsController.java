package com.altenheim.kalender.controller.viewController;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class PopupViewsController
{
    public boolean isRevalidationWanted()
    {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        var jmetro = new JMetro(Style.LIGHT);
        jmetro.setScene(alert.getDialogPane().getScene());
        alert.setTitle("Eingabe fehlgeschlagen");
        alert.setHeaderText("Das Passwort war falsch.");
        alert.setContentText("Der Kalender kann trotzdem genutzt werden, aber erweiterte " +
                "Funktionen wie der automatische Abruf von Öffnungszeiten " +
                "werden nicht funktionieren. Nochmal versuchen?");

        var result = alert.showAndWait();

        return result.get() == ButtonType.OK;
    }

    public void showConfirmationDialog()
    {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        var jmetro = new JMetro(Style.LIGHT);
        jmetro.setScene(alert.getDialogPane().getScene());
        alert.setTitle("Validierung erfolgreich");
        alert.setHeaderText(null);
        alert.setContentText("Passwortvalidierung erfolgreich. Viel Spaß bei " +
                "der Nutzung der erweiterten Funktionen des Smart Planners!");
        alert.showAndWait();
    }

    public void showCancelDialog()
    {
        var alert = new Alert(Alert.AlertType.WARNING);
        var jmetro = new JMetro(Style.LIGHT);
        jmetro.setScene(alert.getDialogPane().getScene());
        alert.setTitle("Erweiterte Funktionen nicht aktiv.");
        alert.setHeaderText(null);
        alert.setContentText("Du kannst beim nächsten Start das Passwort erneut eingeben " +
                "um die erweiterten Funktionen zu nutzen");
        alert.showAndWait();
    }

    public String showPasswordInputDialog()
    {
        var dialog = new Dialog();
        var jmetro = new JMetro(Style.LIGHT);
        jmetro.setScene(dialog.getDialogPane().getScene());
        dialog.setTitle("Entschlüsselung");
        dialog.setHeaderText(null);
        var userInfo = new Text();
        userInfo.setText("Einmalige Passworteingabe um erweiterte Funktionen\n" +
                "(Wegstreckenberechnung, Öffnungszeiten berücksichtigen)\n" +
                "über die Google API zu nutzen. Das Passwort kannst du über\n" +
                "die Entwickler beziehen.");
        var loginButtonType = new ButtonType("Bestätigen", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        var grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        var password = new PasswordField();
        password.setPromptText("Password");
        grid.add(userInfo, 0, 0, 2, 1);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> password.requestFocus());
        dialog.showAndWait();

        return password.getText();
    }
}