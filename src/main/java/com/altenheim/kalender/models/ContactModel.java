package com.altenheim.kalender.models;

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ContactModel implements Serializable
{
    final static public ObservableList<ContactModel> data = FXCollections.observableArrayList();    
    private static int globalId = 1;
    private int iD;
    private String firstName;
    private String surName;
    private String streetAndNumber;
    private String city;
    private String postalCode;
    private String mail;
    private SimpleStringProperty phone = new SimpleStringProperty();
    private String fullName;
    private String address;
    
    public int getContactId() { return iD; }

    public ContactModel(String firstName, String surName, String mail, String streetAndNumber, String city, String postalCode, String phone)
    {
        globalId++;
        this.iD = ContactModel.globalId;
        this.firstName = firstName;
        this.surName = surName;
        this.streetAndNumber = streetAndNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.mail = mail;
        this.phone.set(phone);
        fullName = "%s %s".formatted(firstName, surName);
        address = "%s, %s %s".formatted(streetAndNumber, postalCode, city);
    }

    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getMail() { return mail; }
    public String getPhone() { return phone.get(); }

    final static public void addToList(String firstName, String surName, String mail, String streetAndNumber, String city, String postalCode, String phone)
    {
        ContactModel.data.add(new ContactModel(firstName, surName, mail, streetAndNumber, city, postalCode, phone));
    }    
}
