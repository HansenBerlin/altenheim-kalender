package com.altenheim.kalender.implementations.controller.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserModel 
{
  	static public final ObservableList<UserModel> data = FXCollections.observableArrayList();
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

  	static public void addToList(String name, String street) 
	{
    	UserModel.data.add(new UserModel(name, street));
  	}  
}