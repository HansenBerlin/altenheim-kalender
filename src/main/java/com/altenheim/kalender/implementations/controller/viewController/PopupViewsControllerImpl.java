package com.altenheim.kalender.implementations.controller.viewController;

import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.logicController.ExportController;
import com.altenheim.kalender.interfaces.logicController.ImportController;
import com.altenheim.kalender.interfaces.models.CalendarEntriesModel;
import com.altenheim.kalender.interfaces.models.SettingsModel;
import com.altenheim.kalender.interfaces.viewController.PopupViewController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import jfxtras.styles.jmetro.JMetro;

public record PopupViewsControllerImpl(SettingsModel settings,
                                       ImportController importController,
                                       ExportController exportController) implements PopupViewController {

    public void showEntryAddedDialogWithMailOption(String date, String dateEnd, String start, String end, Button sendMailButton) {
        var dialog = new Dialog<String>();
        var jmetro = new JMetro(settings.getCssStyle());
        jmetro.setScene(dialog.getDialogPane().getScene());
        dialog.setTitle("Eintrag erstellt");
        dialog.setHeaderText(null);
        var userInfo = new Text();
        userInfo.setText(String.format("Ein Termin von %s %s bis %s %s wurde im "
                        + "aktuell gewählten Kalender erstellt",
                date, start, dateEnd, end));
        var loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType);
        var grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(userInfo, 0, 0, 2, 1);
        grid.add(sendMailButton, 1, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait();

    }

    public boolean isRevalidationWanted() {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        var jmetro = new JMetro(settings.getCssStyle());
        jmetro.setScene(alert.getDialogPane().getScene());
        alert.setTitle("Eingabe fehlgeschlagen");
        alert.setHeaderText("Das Passwort war falsch.");
        alert.setContentText("""
                Der Kalender kann trotzdem genutzt werden, aber\s
                erweiterte Funktionen wie der automatische Abruf von
                Öffnungszeiten werden nicht funktionieren.
                Nochmal versuchen?""");

        var result = alert.showAndWait();

        return result.get() == ButtonType.OK;
    }

    public void showPasswordCorrectConfirmationDialog() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        var jmetro = new JMetro(settings.getCssStyle());
        jmetro.setScene(alert.getDialogPane().getScene());
        alert.setTitle("Validierung erfolgreich");
        alert.setHeaderText(null);
        alert.setContentText("""
                Passwortvalidierung erfolgreich. Viel Spaß bei\s
                der Nutzung der erweiterten Funktionen\s
                des Smart Planners!""");
        alert.showAndWait();
    }

    public void showPasswordWrongDialog() {
        var alert = new Alert(Alert.AlertType.WARNING);
        var jmetro = new JMetro(settings.getCssStyle());
        jmetro.setScene(alert.getDialogPane().getScene());
        alert.setTitle("Erweiterte Funktionen nicht aktiv.");
        alert.setHeaderText(null);
        alert.setContentText("""
                Du kannst beim nächsten Start das Passwort
                erneut eingeben um die erweiterten Funktionen
                zu nutzen.""");
        alert.showAndWait();
    }

    public String showPasswordInputDialog() {
        var dialog = new Dialog<String>();
        var jmetro = new JMetro(settings.getCssStyle());
        jmetro.setScene(dialog.getDialogPane().getScene());
        dialog.setTitle("Entschlüsselung");
        dialog.setHeaderText(null);
        var userInfo = new Text();
        userInfo.setText("""
                Einmalige Passworteingabe um erweiterte Funktionen
                (Wegstreckenberechnung, Öffnungszeiten berücksichtigen)
                über die Google API zu nutzen. Das Passwort kannst du über
                die Entwickler beziehen.""");
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
        Platform.runLater(password::requestFocus);
        dialog.showAndWait();

        return password.getText();
    }

    public String showChooseCalendarNameDialog() {
        var dialog = new Dialog<String>();
        var jmetro = new JMetro(settings.getCssStyle());
        jmetro.setScene(dialog.getDialogPane().getScene());
        dialog.setTitle("Kalendername wählen");
        dialog.setHeaderText(null);
        var userInfo = new Text();
        userInfo.setText("Bitte gebe einen Namen für den neuen Kalender ein.\n"
                + "Der Kalender wird erst gespeichert wenn ein Termin eingetragen wurde.");
        var loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        var grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        var textInput = new TextField();
        textInput.setPromptText("Kalendername");
        grid.add(userInfo, 0, 0, 2, 1);
        grid.add(new Label("Kalendername:"), 0, 1);
        grid.add(textInput, 1, 1);
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(textInput::requestFocus);
        dialog.showAndWait();

        var dialogResult = dialog.resultProperty().toString();
        if (dialogResult.equals("ObjectProperty [value: ButtonType [text=OK, buttonData=OK_DONE]]"))
            return textInput.getText();
        else
            return "";
    }

    public void importDialog(EntryFactory entryFactory, Window stage) {
        var filePicker = new FileChooser();
        var file = filePicker.showOpenDialog(stage);
        if (file == null)
            return;
        if (!importController.canCalendarFileBeImported(file.getAbsolutePath()))
            showCalendarImportedError();
        if (importController.canCalendarFileBeParsed())
            importController.importCalendar(file.getName());
        else
            showCalendarImportedError();
    }

    private void showCalendarExportedDialog(int exportedCount, boolean isSuccessful) {
        String message;
        Alert.AlertType alert;
        if (isSuccessful)
        {
            message = "Es wurden " + exportedCount + " Kalenderdateien exportiert.";
            alert = Alert.AlertType.CONFIRMATION;
        }
        else
        {
            message = "Aufrund eines Fehlers wurde kein Kalender exportiert.";
            alert = Alert.AlertType.ERROR;
        }
        showDefaultDialog(message, alert);
    }

    public void showCalendarImportedError()
    {
        var message = ("""
            Es gab einen Fehler beim Importieren der Kalender. 
            Eventuell ist die Datei nicht vorhanden oder er konnte nicht von 
            der Webseite der HWR runtergeladen werden.""");

        showDefaultDialog(message, Alert.AlertType.ERROR);
    }

    public void showCalendarImportedSuccess()
    {
        var message = ("""
            Der gewünschte HWR Kalender wurde erfolgreich
            importiert.""");

        showDefaultDialog(message, Alert.AlertType.CONFIRMATION);
    }

    private void showDefaultDialog(String message, Alert.AlertType alertType)
    {
        var alert = new Alert(alertType);
        var jmetro = new JMetro(settings.getCssStyle());
        jmetro.setScene(alert.getDialogPane().getScene());
        alert.setTitle("Kalender erfolgreich exportiert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void exportDialog(CalendarEntriesModel allEntries, Window stage)
    {
        var calendars = allEntries.getAllCalendars();

        for (var calendar : calendars) {
            try {
                var directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Speicherort für Kalender " + calendar.getName() + " wählen.");
                var path = directoryChooser.showDialog(stage);
                if (path == null) {
                    showCalendarExportedDialog(0, false);
                    return;
                }
                exportController.exportCalendarAsFile(calendar, path.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                showCalendarExportedDialog(0, false);
                return;
            }
        }
        showCalendarExportedDialog(calendars.size(), true);
    }
}