package com.altenheim.kalender.models;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class ContactModel implements Serializable {
    final static public ObservableList<ContactModel> data = FXCollections.observableArrayList();
    public static ObservableList<String> destinations = FXCollections.observableArrayList();

    private static int globalId = 1;
    private int iD;
    private String firstName;
    private String surName;
    private String streetAndNumber;
    private String city;
    private String postalCode;
    private String mail;
    private String phone;
    private String fullName;
    private String address; 
    private transient Button button;

    public ContactModel() {
    }

    public ContactModel(String firstName, String surName, String mail, String streetAndNumber, String city,
            String postalCode, String phone) {
        globalId++;
        this.iD = ContactModel.globalId;
        this.firstName = firstName;
        this.surName = surName;
        this.streetAndNumber = streetAndNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.mail = mail;
        this.phone = phone;
        this.button = new Button("LÖSCHEN");
        fullName = "%s %s".formatted(firstName, surName);
        address = "%s, %s %s".formatted(streetAndNumber, postalCode, city);
        destinations.add(address);
        registerButtonEvent();
    }

    public void registerButtonEvent()
    {
        button.setOnAction(new EventHandler<ActionEvent>() 
        {
            public void handle(ActionEvent e) 
            {
                removeContactModel();
            }
        });
    }    

    private void removeContactModel()
    {
        data.remove(this);
    }

    public int getContactId() { return iD; }
    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getMail() { return mail; }
    public String getPhone() { return phone; }
    public Button getButton() { return button; }

    public List<ContactModel> getDataToSerialize() 
    { 
        var listFromObservable = new ArrayList<ContactModel>();
        for (var contactModel : data) 
        {
            listFromObservable.add(contactModel);            
        }
        return listFromObservable; 
    } 

    public void rebuildObservablaListFromSerializedData(List<ContactModel> serialized) 
    { 
        for (var contactModel : serialized) 
        {
            contactModel.button = new Button("LÖSCHEN");
            contactModel.registerButtonEvent();
            ContactModel.data.add(contactModel);
            ContactModel.destinations.add(contactModel.address);
        }
    } 
}
