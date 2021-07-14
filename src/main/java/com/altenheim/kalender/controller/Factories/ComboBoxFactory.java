package com.altenheim.kalender.controller.Factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.altenheim.kalender.interfaces.IComboBoxFactory;
import com.altenheim.kalender.models.MailTemplateModel;
import com.altenheim.kalender.resourceClasses.ComboBoxCreate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class ComboBoxFactory implements IComboBoxFactory
{
    private MailTemplateModel mailTemplateModel;
    public ComboBoxFactory (MailTemplateModel mailTemplateModel) {
        this.mailTemplateModel = mailTemplateModel;
        vehicles.addAll("zu Fuß", "Fahrrad", "Öffis", "Auto");
        destinations.addAll("Stern Center, Potsdam, Deutschland", "Casablanca, Rigaer Straße, Berlin, Deutschland", "Hauptbahnhof Berlin");
        recurrenceOptions.addAll("täglich", "wöchentlich", "monatlich", "halbjährlich", "jährlich");
        for (var entry : mailTemplateModel.getTemplates().entrySet()) {
            mailTemplateselectorTemplate.add(entry.getKey());
        }
        content.add(vehicles);
        content.add(destinations);
        content.add(destinations);
        content.add(recurrenceOptions);
        content.add(mailTemplateselectorTemplate);
    }  

    private ObservableList<String> vehicles = FXCollections.observableArrayList();
    private ObservableList<String> destinations = FXCollections.observableArrayList();
    private ObservableList<String> recurrenceOptions = FXCollections.observableArrayList();
    private ObservableList<String> mailTemplateselectorTemplate = FXCollections.observableArrayList();
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
}
