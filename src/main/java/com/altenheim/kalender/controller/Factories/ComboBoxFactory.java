package com.altenheim.kalender.controller.Factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.altenheim.kalender.interfaces.IComboBoxFactory;
import com.altenheim.kalender.resourceClasses.ComboBoxCreate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class ComboBoxFactory implements IComboBoxFactory
{
    
    public ComboBoxFactory () {
        
        vehicles.addAll("zu Fuß", "Fahrrad", "Öffis", "Auto");
        destinations.addAll("Stern Center, Potsdam, Deutschland", "Casablanca, Rigaer Straße, Berlin, Deutschland", "Hauptbahnhof Berlin");
        recurrenceOptions.addAll("täglich", "wöchentlich", "monatlich", "halbjährlich", "jährlich");
        
        mailTemplateSelectorTemplate.add("test");
        
        content.add(vehicles);
        content.add(destinations);
        content.add(destinations);
        content.add(recurrenceOptions);
        content.add(mailTemplateSelectorTemplate);
    }  

    private ObservableList<String> vehicles = FXCollections.observableArrayList();
    private ObservableList<String> destinations = FXCollections.observableArrayList();
    private ObservableList<String> recurrenceOptions = FXCollections.observableArrayList();
    private ObservableList<String> mailTemplateSelectorTemplate = FXCollections.observableArrayList(); //getter und dann ändern im Mail ViewController
    private List<ObservableList<String>> content = new ArrayList<ObservableList<String>>();
    private String[] headers = {"Verkehrsmittel", "Start", "Ziel", "Intervall", "Auswahl Templates"};

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
    
    public void updateMailTemplates(Map<String, String> templates) {
        
        content.remove(content.indexOf(mailTemplateSelectorTemplate));        
        mailTemplateSelectorTemplate.clear();
        for (Entry<String, String> entry : templates.entrySet()) {
            mailTemplateSelectorTemplate.add(entry.getKey());
        }
        content.add(mailTemplateSelectorTemplate);
    }
}
