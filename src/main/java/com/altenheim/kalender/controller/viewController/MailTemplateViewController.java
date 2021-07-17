package com.altenheim.kalender.controller.viewController;

import java.util.List;
import java.util.Map;
import com.altenheim.kalender.interfaces.IComboBoxFactory;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.interfaces.IMailCreationController;
import com.altenheim.kalender.models.ContactModel;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.models.SettingsModel;
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
    private IIOController ioController;
    private SettingsModel settings;
    private IMailCreationController mailController;
    private List<ContactModel> contacts;
    private MailTemplateModel mailTemplatesModel;
    private IComboBoxFactory comboBoxFactory;
    private ComboBox<String> comboBoxTemplates;
    @FXML
    private Button btnMailTemplateDate, btnMailTemplateTime, btnMailTemplateNewTemplate, btnMailTemplaterRemoveTemplate, btnMailTemplateSave, btnTemplateLoad;
    @FXML
    private HBox containerComboBoxSelectorTemplate;
    @FXML
    private TextArea mailTemplatetxtArea;
    @FXML
    private TextField txtFieldNameTemplate;
    @FXML
    private Text txtError;
    @FXML

    private void initialize()
    {
        mailTemplatesModel.initialize();    
        comboBoxTemplates = comboBoxFactory.create(ComboBoxCreate.MAILTEMPLATESELECTORTEMPLATE);
        containerComboBoxSelectorTemplate.getChildren().add(comboBoxTemplates);
        comboBoxFactory.updateMailTemplates(mailTemplates);
    }

    public MailTemplateViewController(IIOController ioController, SettingsModel settings, 
        IMailCreationController mailController, List<ContactModel> contacts, MailTemplateModel mailTemplatesModel, IComboBoxFactory comboBoxFactory)
    {
        this.ioController = ioController;
        this.settings = settings;
        this.mailController = mailController;
        this.contacts = contacts;
        this.mailTemplatesModel = mailTemplatesModel;
        this.comboBoxFactory = comboBoxFactory;
        this.mailTemplates = mailTemplatesModel.getTemplates();
    }
    
    public void changeContentPosition(double width, double height) 
    {
        
    }

    private Map<String, String> mailTemplates;
    @FXML
    void removeTemplate(ActionEvent event) 
    {
        var templateName = comboBoxTemplates.getValue();
        
        if (mailTemplates.get(templateName)!=null) {
            mailTemplates.remove(templateName);
            comboBoxFactory.updateMailTemplates(mailTemplates);
            updateTextArea("");
            txtFieldNameTemplate.setText("");
        }
        
    }
    private String templateKey;
    @FXML
    void saveMailTemplate(ActionEvent event) {
        comboBoxFactory.updateMailTemplates(mailTemplates);
        var templateName = txtFieldNameTemplate.getText();
        var templateValue = mailTemplatetxtArea.getText();
        if (templateName.isBlank()) {
            printErrorMessage("Der Name der Templates darf nicht leer sein!");
        }else if(!templateValue.contains("[Datum]") ){
            printErrorMessage("Datum fehlt im Template. Klicke auf den Knopf Datum um den Platzhalter einzufügen.");
        }else if(!templateValue.contains("[Uhrzeit]") ){
            printErrorMessage("Uhrzeit fehlt im Template. Klicke auf den Knopf Uhrzeit um den Platzhalter einzufügen.");
        }else {
            txtError.setVisible(false);
            if (templateKey == null || templateKey.isBlank()) {
                mailTemplates.put(templateName, templateValue);
            }else if(!templateKey.equals(templateName)){
                mailTemplates.remove(templateKey);
                mailTemplates.put(templateName, templateValue);
            } else {
                mailTemplates.replace(templateName, templateValue);
            }
            comboBoxFactory.updateMailTemplates(mailTemplates);
            comboBoxTemplates.setValue(templateName);
        }
    }

    private void printErrorMessage(String error){
        txtError.setVisible(true);
        txtError.setText(error);
        txtError.setFill(Color.RED);
    }
    
    @FXML
    void txtFieldtxtChanged() {
        if (txtFieldNameTemplate.getText().isBlank()) {
            txtError.setVisible(true);
            txtError.setText("Der Name der Templates darf nicht leer sein!");
            txtError.setFill(Color.RED);
        }else {
            txtError.setVisible(false);
        }
    }

    @FXML
    void createNewTemplate(ActionEvent event) {
        updateTextArea("");
        txtFieldNameTemplate.setText(""); 
        templateKey = ""; 
    }

    @FXML
    void insertDateMailTemplate(ActionEvent event) {
        var position = mailTemplatetxtArea.getCaretPosition();
        var text = mailTemplatetxtArea.getText();
        mailTemplatetxtArea.setText(text.substring(0, position)+"[Datum] "+text.substring(position, text.length()));
        if (txtError.getText().contains("Datum fehlt im Template.")) {
            txtError.setVisible(false);
        }
    }

    @FXML
    void insertTimeMailTemplate(ActionEvent event) {
        var position = mailTemplatetxtArea.getCaretPosition();
        var text = mailTemplatetxtArea.getText();
        mailTemplatetxtArea.setText(text.substring(0, position)+"[Uhrzeit] "+text.substring(position, text.length()));
        if (txtError.getText().contains("Uhrzeit fehlt im Template.")) {
            txtError.setVisible(false);
        }
    }

    @FXML
    void loadTemplate(ActionEvent event) {
        var value = comboBoxTemplates.getValue();
        updateTextArea(mailTemplates.get(value));
        txtFieldNameTemplate.setText(value);
        templateKey = value;
    }

    private void updateTextArea(String value) {
        mailTemplatetxtArea.setText(value);
    }
}
