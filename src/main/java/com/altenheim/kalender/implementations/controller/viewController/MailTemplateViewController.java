package com.altenheim.kalender.implementations.controller.viewController;

import com.altenheim.kalender.interfaces.factorys.ComboBoxFactory;
import com.altenheim.kalender.interfaces.logicController.IOController;
import com.altenheim.kalender.interfaces.models.MailTemplateModel;
import com.altenheim.kalender.resourceClasses.ComboBoxCreate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MailTemplateViewController extends ResponsiveController 
{
    private MailTemplateModel mailTemplatesModel;
    private ComboBoxFactory comboBoxFactory;
    private IOController iOController;

    private String templateKey;
    private ComboBox<String> comboBoxTemplates;

    @FXML private Button btnMailTemplateDate, btnMailTemplateTime, btnMailTemplateNewTemplate, 
        btnMailTemplaterRemoveTemplate, btnMailTemplateSave, btnTemplateLoad;
    @FXML private HBox containerComboBoxSelectorTemplate;
    @FXML private TextArea mailTemplatetxtArea;
    @FXML private TextField txtFieldNameTemplate;
    @FXML private Text txtError;    

    public MailTemplateViewController(MailTemplateModel mailTemplateModel, ComboBoxFactory comboBoxFactory, IOController iOController)
    {
        this.comboBoxFactory = comboBoxFactory;
        this.mailTemplatesModel = mailTemplateModel;
        this.iOController = iOController;
    }

    @FXML
    private void initialize() 
    {
        comboBoxTemplates = comboBoxFactory.create(ComboBoxCreate.MAILTEMPLATESELECTORTEMPLATE);
        containerComboBoxSelectorTemplate.getChildren().add(comboBoxTemplates);
        updateComboBoxTemplates();
    }

    @FXML
    private void removeTemplate(ActionEvent event) {
        var templateName = comboBoxTemplates.getValue();

        if (mailTemplatesModel.getTemplates().get(templateName) != null) {
            mailTemplatesModel.getTemplates().remove(templateName);
            updateComboBoxTemplates();
            updateTextArea("");
            txtFieldNameTemplate.setText("");
        }
    }


    @FXML
    private void saveMailTemplate(ActionEvent event) {
        updateComboBoxTemplates();
        var templateName = txtFieldNameTemplate.getText();
        var templateValue = mailTemplatetxtArea.getText();
        if (templateName.isBlank()) {
            printErrorMessage("Der Name der Templates darf nicht leer sein!");
        } else if (!templateValue.contains("[Datum]")) {
            printErrorMessage("Datum fehlt im Template. Klicke auf den Knopf Datum um den Platzhalter einzufügen.");
        } else if (!templateValue.contains("[Uhrzeit]")) {
            printErrorMessage("Uhrzeit fehlt im Template. Klicke auf den Knopf Uhrzeit um den Platzhalter einzufügen.");
        } else {
            txtError.setVisible(false);
            if (templateKey == null || templateKey.isBlank()) {
                mailTemplatesModel.getTemplates().put(templateName, templateValue);
            } else if (!templateKey.equals(templateName)) {
                mailTemplatesModel.getTemplates().remove(templateKey);
                mailTemplatesModel.getTemplates().put(templateName, templateValue);
            } else {
                mailTemplatesModel.getTemplates().replace(templateName, templateValue);
            }
            updateComboBoxTemplates();
            comboBoxTemplates.setValue(templateName);
        }
        iOController.saveMailTemplatesToFile(mailTemplatesModel);
    }

    private void printErrorMessage(String error) {
        txtError.setVisible(true);
        txtError.setText(error);
        txtError.setFill(Color.RED);
    }

    @FXML
    private void txtFieldtxtChanged() {
        if (txtFieldNameTemplate.getText().isBlank()) {
            txtError.setVisible(true);
            txtError.setText("Der Name der Templates darf nicht leer sein!");
            txtError.setFill(Color.RED);
        } else {
            txtError.setVisible(false);
        }
    }

    @FXML
    private void createNewTemplate(ActionEvent event) {
        updateTextArea("");
        txtFieldNameTemplate.setText("");
        templateKey = "";
    }

    @FXML
    private void insertDateMailTemplate(ActionEvent event) {
        insertSomethingInMailTemplateTxtArea("Datum");
    }

    @FXML
    private void insertTimeMailTemplate(ActionEvent event) {
        insertSomethingInMailTemplateTxtArea("Uhrzeit");
    }

    private void insertSomethingInMailTemplateTxtArea(String txt) {
        var position = mailTemplatetxtArea.getCaretPosition();
        var text = mailTemplatetxtArea.getText();
        if (txtError.getText().contains(txt + "fehlt im Template."))
            txtError.setVisible(false);
        txt = "[" + txt + "] ";
        mailTemplatetxtArea.setText(text.substring(0, position) + txt + text.substring(position, text.length()));
        mailTemplatetxtArea.requestFocus();
        mailTemplatetxtArea.positionCaret(position + txt.length());
    }

    @FXML
    private void loadTemplate(ActionEvent event) {
        var value = comboBoxTemplates.getValue();
        updateTextArea(mailTemplatesModel.getTemplates().get(value));
        txtFieldNameTemplate.setText(value);
        templateKey = value;
    }

    private void updateTextArea(String value) {
        mailTemplatetxtArea.setText(value);
    }

    private void updateComboBoxTemplates() 
    {
        var mailTemplate = comboBoxFactory.getMailTemplateSelectorTemplate();
        mailTemplate.clear();
        for (var entry : mailTemplatesModel.getTemplates().entrySet()) 
        {
            mailTemplate.add(entry.getKey());
        }
    }

    public void changeContentPosition(double width, double height) {
        //
    }    
}
