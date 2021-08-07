package com.altenheim.kalender.implementations.controller.viewController;

import com.altenheim.kalender.interfaces.models.ContactModel;
import com.altenheim.kalender.interfaces.logicController.IOController;
import com.altenheim.kalender.implementations.controller.models.ContactModelImpl;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class ContactsViewController extends ResponsiveController 
{

    @FXML private TextField txtFieldFirstName, txtFieldSurName, txtFieldMail, txtFieldStreet, txtFieldPostalCode, txtFieldCity, txtFieldPhone;
    @FXML private Button btnAddContact;
    @FXML private VBox tableContainer;

    private final IOController ioController;
    private final ContactModel contacts;

    public ContactsViewController(IOController ioController, ContactModel contacts)
    {
        this.ioController = ioController;
        this.contacts = contacts;
    }

    @FXML
    private void initialize() 
    {
        TableView<ContactModelImpl> contactsTable = createTable();
        tableContainer.getChildren().add(contactsTable);
        saveChangesToContacts();
    }

    @FXML
    private void buttonClicked()
    {
        var newContact = new ContactModelImpl(txtFieldFirstName.getText(), txtFieldSurName.getText(),
                txtFieldMail.getText(), txtFieldStreet.getText(), txtFieldCity.getText(), txtFieldPostalCode.getText(),
                txtFieldPhone.getText());
        ContactModelImpl.data.add(newContact);
        clearFields();     
    }

    private void clearFields()
    {
        TextField[] allTextFields = { txtFieldFirstName, txtFieldSurName, txtFieldMail, 
            txtFieldStreet, txtFieldPostalCode, txtFieldCity, txtFieldPhone };
        for (var textField : allTextFields) 
        {
            textField.clear();            
        }        
    }

    private void saveChangesToContacts()
    {
        ContactModelImpl.data.addListener((ListChangeListener<ContactModelImpl>) c -> {
            try
            {
                ioController.saveContactsToFile(contacts);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    private TableView<ContactModelImpl> createTable()
    {
        TableColumn<ContactModelImpl, String> name = new TableColumn<>("Name");
        TableColumn<ContactModelImpl, String> address = new TableColumn<>("Adresse");
        TableColumn<ContactModelImpl, String> mail = new TableColumn<>("Mailadresse");
        TableColumn<ContactModelImpl, String> phone = new TableColumn<>("Telefon");
        TableColumn<ContactModelImpl, String> button = new TableColumn<>("");
        TableView<ContactModelImpl> table = new TableView<>(ContactModelImpl.data);

        name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        mail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        button.setCellValueFactory(new PropertyValueFactory<>("button"));

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

    protected void changeContentPosition(double width, double height) 
    {
        
    }    
}
