package com.altenheim.kalender.implementations.controller.models;

import java.util.ArrayList;
import java.util.List;

import com.altenheim.kalender.interfaces.models.ContactModel;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class ContactModelImpl implements ContactModel
{
    public static final ObservableList<ContactModelImpl> data = FXCollections.observableArrayList();
    public static final ObservableList<String> destinations = FXCollections.observableArrayList();
    public static final ObservableList<String> mailadresses = FXCollections.observableArrayList();
    
    private String mail;
    private String phone;
    private String fullName;
    private String address; 
    private transient Button button;
    
    public ContactModelImpl() {}

    public ContactModelImpl(String firstName, String surName, String mail, String streetAndNumber, String city,
                            String postalCode, String phone)
    {        
        this.mail = mail;
        this.phone = phone;
        this.button = new Button("LÖSCHEN");
        fullName = "%s %s".formatted(firstName, surName);
        address = "%s, %s %s".formatted(streetAndNumber, postalCode, city);
        if (!address.isBlank())
            destinations.add(address);
        if (!mail.isBlank())
            mailadresses.add(mail);
        registerButtonEvent();
    }

    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getMail() { return mail; }
    public String getPhone() { return phone; }
    public Button getButton() { return button; }

    private void registerButtonEvent()
    {
        button.setOnAction(new EventHandler<ActionEvent>() 
        {
            public void handle(ActionEvent e) 
            {
                removeContactModel();
            }
        });
    }

    public List<ContactModelImpl> getDataToSerialize()
    { 
        var listFromObservable = new ArrayList<ContactModelImpl>();
        for (var contactModel : data) 
        {
            listFromObservable.add(contactModel);            
        }
        return listFromObservable; 
    } 

    public void rebuildObservableListFromSerializedData(List<ContactModelImpl> serialized)
    { 
        for (var contactModel : serialized) 
        {
            contactModel.button = new Button("LÖSCHEN");
            contactModel.registerButtonEvent();
            ContactModelImpl.data.add(contactModel);
            ContactModelImpl.destinations.add(contactModel.address);
            ContactModelImpl.mailadresses.add(contactModel.mail);
        }
    }

    private void removeContactModel()
    {
        data.remove(this);
        mailadresses.remove(mail);
        destinations.remove(address);
    }
}
