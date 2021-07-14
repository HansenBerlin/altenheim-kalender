package com.altenheim.kalender.controller.viewController;

import java.util.List;

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
import javafx.scene.layout.HBox;

public class MailTemplateViewController extends ResponsiveController
{ 
    private IIOController ioController;
    private SettingsModel settings;
    private IMailCreationController mailController;
    private List<ContactModel> contacts;
    private MailTemplateModel mailTemplates;
    private IComboBoxFactory comboBoxFactory;
    private ComboBox<String> comboBoxTemplates;
    @FXML
    private Button btnMailTemplateDate, btnMailTemplateTime, btnMailTemplateNewTemplate, btnMailTemplaterRemoveTemplate, btnMailTemplateSave;
    @FXML
    private HBox containerComboBoxSelectorTemplate;
    @FXML
    private TextArea mailTemplatetxtArea;


    @FXML
    private void initialize()
    {
        mailTemplates.initialize();    
        comboBoxTemplates = comboBoxFactory.create(ComboBoxCreate.MAILTEMPLATESELECTORTEMPLATE);
        containerComboBoxSelectorTemplate.getChildren().add(comboBoxTemplates);   
    }

    public MailTemplateViewController(IIOController ioController, SettingsModel settings, 
        IMailCreationController mailController, List<ContactModel> contacts, MailTemplateModel mailTemplates, IComboBoxFactory comboBoxFactory)
    {
        this.ioController = ioController;
        this.settings = settings;
        this.mailController = mailController;
        this.contacts = contacts;
        this.mailTemplates = mailTemplates;
        this.comboBoxFactory = comboBoxFactory;
    }
    
    public void changeContentPosition(double width, double height) 
    {
        
    }
    
    @FXML
    void removeTemplate(ActionEvent event) 
    {
        System.out.println(comboBoxTemplates.promptTextProperty().getValue().toString());
        mailTemplates.removeTemplate(comboBoxTemplates.promptTextProperty().getValue());
        
        //settings.setStreet(txtStreet.getText());
    }

    @FXML
    void SaveMailTemplate(ActionEvent event) {

    }

    @FXML
    void createNewTemplate(ActionEvent event) {

    }

    @FXML
    void insertDateMailTemplate(ActionEvent event) {

    }

    @FXML
    void insertTimeMailTemplate(ActionEvent event) {

    }
}


