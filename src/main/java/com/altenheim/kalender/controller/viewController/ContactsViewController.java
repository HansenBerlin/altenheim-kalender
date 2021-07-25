package com.altenheim.kalender.controller.viewController;

import java.io.IOException;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.models.ContactModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class ContactsViewController extends ResponsiveController {

    @FXML
    private TextField txtFieldFirstName, txtFieldSurName, txtFieldMail, txtFieldStreet, txtFieldPostalCode,
            txtFieldCity, txtFieldPhone;
    @FXML
    private Button btnAddContact;
    @FXML
    private VBox tableContainer;

    private IIOController ioController;

    public ContactsViewController(IIOController ioController) {
        this.ioController = ioController;
    }

    @FXML
    private void initialize() {
        TableView<ContactModel> contactsTable = createTable();
        tableContainer.getChildren().add(contactsTable);
    }

    @FXML
    private void buttonClicked(ActionEvent event) {
        var newContact = new ContactModel(txtFieldFirstName.getText(), txtFieldSurName.getText(),
                txtFieldMail.getText(), txtFieldStreet.getText(), txtFieldCity.getText(), txtFieldPostalCode.getText(),
                txtFieldPhone.getText());
        ContactModel.data.add(newContact);
        try {
            ioController.saveContactsToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final void changeContentPosition(double width, double height) {
        //
    }

    private TableView<ContactModel> createTable() {
        TableColumn<ContactModel, String> name = new TableColumn<>("Name");
        TableColumn<ContactModel, String> address = new TableColumn<>("Adresse");
        TableColumn<ContactModel, String> mail = new TableColumn<>("Mailadresse");
        TableColumn<ContactModel, String> phone = new TableColumn<>("Telefon");
        TableView<ContactModel> table = new TableView<ContactModel>(ContactModel.data);

        name.setCellValueFactory(new PropertyValueFactory<ContactModel, String>("fullName"));
        address.setCellValueFactory(new PropertyValueFactory<ContactModel, String>("address"));
        mail.setCellValueFactory(new PropertyValueFactory<ContactModel, String>("mail"));
        phone.setCellValueFactory(new PropertyValueFactory<ContactModel, String>("phone"));

        name.setPrefWidth(200);
        address.setPrefWidth(400);
        mail.setPrefWidth(200);
        phone.setPrefWidth(200);

        table.getColumns().add(name);
        table.getColumns().add(address);
        table.getColumns().add(mail);
        table.getColumns().add(phone);

        return table;
    }
    
}
