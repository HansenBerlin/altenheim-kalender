package com.altenheim.kalender.controller.Factories;

import java.util.ArrayList;
import java.util.List;
import com.altenheim.kalender.interfaces.IComboBoxFactory;
import com.altenheim.kalender.resourceClasses.ComboBoxCreate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class ComboBoxFactory implements IComboBoxFactory
{
    

    private ObservableList<String> vehicles = FXCollections.observableArrayList();
    private ObservableList<String> destinations = FXCollections.observableArrayList();
    private ObservableList<String> recurrenceOptions = FXCollections.observableArrayList();
    private ObservableList<Integer> notificationHour = FXCollections.observableArrayList();
    private ObservableList<Integer> notificationMin = FXCollections.observableArrayList();

    private List<ObservableList<String>> content = new ArrayList<ObservableList<String>>();
    private String[] headers = {"Verkehrsmittel", "Start", "Ziel", "Intervall"};

    private List<ObservableList<Integer>> contentIntenger = new ArrayList<ObservableList<Integer>>();
    private String[] headersInteger = {"Min.", "Stunden"};


    public ComboBoxFactory()
    {
        vehicles.addAll("zu Fuß", "Fahrrad", "Öffis", "Auto");
        destinations.addAll("Stern Center, Potsdam, Deutschland", "Casablanca, Rigaer Straße, Berlin, Deutschland", "Hauptbahnhof Berlin");
        recurrenceOptions.addAll("täglich", "wöchentlich", "monatlich", "halbjährlich", "jährlich");
        notificationHour.addAll(1, 2, 4, 8);
        notificationMin.addAll(5, 15, 30, 60);
        content.add(vehicles);
        content.add(destinations);
        content.add(destinations);
        content.add(recurrenceOptions);
        contentIntenger.add(notificationHour);
        contentIntenger.add(notificationMin);
    }

    public ComboBox<String> create(ComboBoxCreate type)
    {
        int typeOrdinal = type.ordinal();
        var comboBox = new ComboBox<String>();
        comboBox.setPrefWidth(140);
        comboBox.setPromptText(headers[typeOrdinal]);
        comboBox.setItems(content.get(typeOrdinal));
        if (typeOrdinal == 2 || typeOrdinal == 3)        
            comboBox.setEditable(true);        
        return comboBox;
    }   

    public ComboBox<Integer> createComboBoxInteger(ComboBoxCreate type)
    {
        int typeOrdinal = type.ordinal()-4;
        var comboBox = new ComboBox<Integer>();
        comboBox.setPrefWidth(140);
        comboBox.setPromptText(headersInteger[typeOrdinal]);
        comboBox.setItems(contentIntenger.get(typeOrdinal));       
        return comboBox;
    } 
}
