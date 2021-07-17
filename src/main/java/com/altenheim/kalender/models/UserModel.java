package com.altenheim.kalender.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserModel 
{
    final static public ObservableList<UserModel> data = FXCollections.observableArrayList();    
    private String name;
    private String street;

    public UserModel(String name, String street)
    {      
      this.name = name;
      this.street = street;
      
    }

    public String getName() { return name; }
    public String getStreet() { return street; }
    public void setName(String name) { this.name = name; }
    public void setStreet(String street) { this.street = street; }

    final static public void addToList(String name, String street)
    {
        UserModel.data.add(new UserModel(name, street));
    }
}