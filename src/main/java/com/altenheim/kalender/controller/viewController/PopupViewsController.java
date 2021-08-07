package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.*;
import com.altenheim.kalender.models.SettingsModelImpl;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import jfxtras.styles.jmetro.JMetro;

public class PopupViewsController implements IPopupViewController 
{
    private SettingsModel settings;
    private IImportController importController; 
    private IExportController exportController;

    public PopupViewsController(SettingsModel settings, IImportController importController, IExportController exportController)
    {
        this.settings = settings;
        this.importController = importController;
        this.exportController = exportController;
    }

    public void showEntryAddedDialogWithMailOption(String date, String dateEnd, String start, String end, String title, Button sendMailButton) 
    {
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

    public boolean isRevalidationWanted() 
    {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        var jmetro = new JMetro(settings.getCssStyle());
        jmetro.setScene(alert.getDialogPane().getScene());
        alert.setTitle("Eingabe fehlgeschlagen");
        alert.setHeaderText("Das Passwort war falsch.");
        alert.setContentText("Der Kalender kann trotzdem genutzt werden, aber \nerweiterte "
                            + "Funktionen wie der automatische Abruf von\n"
                            + "Öffnungszeiten werden nicht funktionieren.\n"
                            + "Nochmal versuchen?");

        var result = alert.showAndWait();

        return result.get() == ButtonType.OK;
    }

    public void showConfirmationDialog() 
    {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        var jmetro = new JMetro(settings.getCssStyle());
        jmetro.setScene(alert.getDialogPane().getScene());
        alert.setTitle("Validierung erfolgreich");
        alert.setHeaderText(null);
        alert.setContentText("Passwortvalidierung erfolgreich. Viel Spaß bei \n"
                + "der Nutzung der erweiterten Funktionen \n"+"des Smart Planners!");
        alert.showAndWait();
    }

    public void showCancelDialog() 
    {
        var alert = new Alert(Alert.AlertType.WARNING);
        var jmetro = new JMetro(settings.getCssStyle());
        jmetro.setScene(alert.getDialogPane().getScene());
        alert.setTitle("Erweiterte Funktionen nicht aktiv.");
        alert.setHeaderText(null);
        alert.setContentText("Du kannst beim nächsten Start das Passwort\n"
                            +"erneut eingeben um die erweiterten Funktionen\n"
                            +"zu nutzen.");
        alert.showAndWait();
    }

    public String showPasswordInputDialog() 
    {
        var dialog = new Dialog<String>();
        var jmetro = new JMetro(settings.getCssStyle());
        jmetro.setScene(dialog.getDialogPane().getScene());
        dialog.setTitle("Entschlüsselung");
        dialog.setHeaderText(null);
        var userInfo = new Text();
        userInfo.setText("Einmalige Passworteingabe um erweiterte Funktionen\n"
                + "(Wegstreckenberechnung, Öffnungszeiten berücksichtigen)\n"
                + "über die Google API zu nutzen. Das Passwort kannst du über\n" + "die Entwickler beziehen.");
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

    public String showChooseCalendarNameDialog() 
    {
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
        Platform.runLater(() -> textInput.requestFocus());
        dialog.showAndWait();

        var dialogResult = dialog.resultProperty().toString();
        if (dialogResult.equals("ObjectProperty [value: ButtonType [text=OK, buttonData=OK_DONE]]"))
            return textInput.getText();
        else
            return "";
    }

    public void importDialog(IEntryFactory entryFactory, Window stage) 
    {
        var filePicker = new FileChooser();
        var file = filePicker.showOpenDialog(stage);
        if (file == null)
            return;
        var importedCalendar = importController.importFile(file.getAbsolutePath());
        entryFactory.addCalendarToView(importedCalendar, file.getName());
    }

    private void showCalendarExportedDialog(int exportedCount, boolean isSuccessful) 
    {
        String message = "";
        if (isSuccessful)
            message = "Es wurden " + exportedCount + " Kalenderdateien exportiert.";
        else
            message = "Aufrund eines Fehlers wurde kein Kalender exportiert.";

        var alert = new Alert(Alert.AlertType.INFORMATION);
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
        
        for (var calendar : calendars) 
        {
            try 
            {
                var directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Speicherort für Kalender " + calendar.getName() + " wählen.");
                var path = directoryChooser.showDialog(stage);
                if (path == null)
                {
                    showCalendarExportedDialog(0, false);
                    return;
                }
                exportController.exportCalendarAsFile(calendar, path.getAbsolutePath());
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
                showCalendarExportedDialog(0, false);
                return;
            }
        }
        showCalendarExportedDialog(calendars.size(), true);
    }    
}