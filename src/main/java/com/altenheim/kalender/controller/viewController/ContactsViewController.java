package com.altenheim.kalender.controller.viewController;

import java.io.IOException;
import java.util.List;

import com.altenheim.kalender.interfaces.IContactFactory;
import com.altenheim.kalender.interfaces.IGoogleAPIController;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.models.ContactModel;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class ContactsViewController extends ResponsiveController
{ 

    @FXML private TextField txtFieldFirstName, txtFieldSurName, txtFieldMail, txtFieldStreet, txtFieldPostalCode, txtFieldCity, txtFieldPhone;
    @FXML private Button btnAddContact;
    @FXML private VBox tableContainer;

    private IGoogleAPIController api;
    private IIOController ioController;

    public ContactsViewController(IGoogleAPIController api, IIOController ioController)
    {        
        this.ioController = ioController;
        this.api = api;
    }

    @FXML
    private void initialize()
    {
        TableView<ContactModel> contactsTable = createTable();
        tableContainer.getChildren().add(contactsTable);
        saveChangesToContacts();
    }

    @FXML
    private void buttonClicked(ActionEvent event) 
    {
        var newContact = new ContactModel(txtFieldFirstName.getText(), txtFieldSurName.getText(), txtFieldMail.getText(), 
            txtFieldStreet.getText(), txtFieldCity.getText(), txtFieldPostalCode.getText(), txtFieldPhone.getText());
        ContactModel.data.add(newContact);
        
    }

    private void saveChangesToContacts()
    {
        ContactModel.data.addListener(new ListChangeListener<ContactModel>() 
        {
            public void onChanged(Change<? extends ContactModel> c) 
            {
                try 
                {
                    ioController.saveContactsToFile();
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }               
            }
        });
    }

    public final void changeContentPosition(double width, double height) 
    {
        
    }   
    
    private TableView<ContactModel> createTable()
    {
        TableColumn<ContactModel, String> name = new TableColumn<>("Name");
        TableColumn<ContactModel, String> address = new TableColumn<>("Adresse");
        TableColumn<ContactModel, String> mail = new TableColumn<>("Mailadresse");
        TableColumn<ContactModel, String> phone = new TableColumn<>("Telefon");
        TableColumn<ContactModel, String> button = new TableColumn<>("");
        TableView<ContactModel> table = new TableView<ContactModel>(ContactModel.data);

        name.setCellValueFactory(new PropertyValueFactory<ContactModel, String>("fullName"));
        address.setCellValueFactory(new PropertyValueFactory<ContactModel, String>("address"));
        mail.setCellValueFactory(new PropertyValueFactory<ContactModel, String>("mail"));
        phone.setCellValueFactory(new PropertyValueFactory<ContactModel, String>("phone"));
        button.setCellValueFactory(new PropertyValueFactory<ContactModel, String>("button"));

        name.setPrefWidth(200);
        address.setPrefWidth(400);
        mail.setPrefWidth(200);
        phone.setPrefWidth(200);        
        button.setPrefWidth(200);
        
        table.getColumns().add(name);
        table.getColumns().add(address);
        table.getColumns().add(mail);
        table.getColumns().add(phone);
        table.getColumns().add(button);

        return table;
    }
}


