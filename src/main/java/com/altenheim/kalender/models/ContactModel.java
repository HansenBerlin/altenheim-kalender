package com.altenheim.kalender.models;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class ContactModel implements Serializable 
{
    public static ObservableList<ContactModel> data = FXCollections.observableArrayList();
    public static ObservableList<String> destinations = FXCollections.observableArrayList();
    public static ObservableList<String> mailadresses = FXCollections.observableArrayList();
    
    private String mail;
    private String phone;
    private String fullName;
    private String address; 
    private transient Button button;
    
    public ContactModel() {}

    public ContactModel(String firstName, String surName, String mail, String streetAndNumber, String city,
            String postalCode, String phone) 
    {        
        this.mail = mail;
        this.phone = phone;
        this.button = new Button("LÖSCHEN");
        fullName = "%s %s".formatted(firstName, surName);
        address = "%s, %s %s".formatted(streetAndNumber, postalCode, city);
        if (address.isBlank() == false)
            destinations.add(address);
        if (mail.isBlank() == false)
            mailadresses.add(mail);
        registerButtonEvent();
    }

    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getMail() { return mail; }
    public String getPhone() { return phone; }
    public Button getButton() { return button; }

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
        mailadresses.remove(mail);
        destinations.remove(address);
    }    

    public List<ContactModel> getDataToSerialize() 
    { 
        var listFromObservable = new ArrayList<ContactModel>();
        for (var contactModel : data) 
        {
            listFromObservable.add(contactModel);            
        }
        return listFromObservable; 
    } 

    public void rebuildObservableListFromSerializedData(List<ContactModel> serialized) 
    { 
        for (var contactModel : serialized) 
        {
            contactModel.button = new Button("LÖSCHEN");
            contactModel.registerButtonEvent();
            ContactModel.data.add(contactModel);
            ContactModel.destinations.add(contactModel.address);
            ContactModel.mailadresses.add(contactModel.mail);
        }
    } 
}
