package com.altenheim.kalender.controller.Factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.altenheim.kalender.interfaces.IComboBoxFactory;
import com.altenheim.kalender.models.ContactModel;
import com.altenheim.kalender.resourceClasses.ComboBoxCreate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class ComboBoxFactory implements IComboBoxFactory
{  
    private ObservableList<String> vehicles = FXCollections.observableArrayList();
    private ObservableList<String> destinations = FXCollections.observableArrayList();
    private ObservableList<String> recurrenceOptions = FXCollections.observableArrayList();
    private ObservableList<String> notificationMin = FXCollections.observableArrayList();
    private ObservableList<String> selectionSpecialField = FXCollections.observableArrayList();
    private ObservableList<String> selectionCourse = FXCollections.observableArrayList();
    private ObservableList<String> selectionSemester = FXCollections.observableArrayList();
    private ObservableList<String> mailTemplateSelectorTemplate = FXCollections.observableArrayList(); //getter und dann ändern im Mail ViewController
    private List<ObservableList<String>> content = new ArrayList<ObservableList<String>>();
    private String[] headers = {"Verkehrsmittel", "Start", "Ziel", "Intervall", "Min.", "FB", "Kurs", "Semester"};

    public void addToDestinations(String address) { destinations.add(address); }

    public ComboBoxFactory()
    {
        vehicles.addAll("zu Fuß", "Fahrrad", "Öffis", "Auto");
        destinations = ContactModel.destinations;
        recurrenceOptions.addAll("täglich", "wöchentlich", "monatlich", "halbjährlich", "jährlich");
        notificationMin.addAll("5", "15", "30", "60");
        selectionSpecialField.addAll("IP", "Bank", "Bauwesen", "DL", "Elektrotechnik", "FM", "Handel", "IBA", "Immobilien", "Industrie", 
            "Informatik", "Maschinenbau", "PPM", "Spedition", "Steuern", "Tourismus", "Versicherung", "WI");
        selectionCourse.addAll("A", "B", "C", "keine Kurse");
        selectionSemester.addAll("Sem. 1","Sem. 2", "Sem. 3", "Sem. 4", "Sem. 5", "Sem. 6");
        mailTemplateSelectorTemplate.add("test");        
        content.add(vehicles);
        content.add(destinations);
        content.add(destinations);
        content.add(recurrenceOptions);
        content.add(notificationMin);
        content.add(selectionSpecialField);
        content.add(selectionCourse);
        content.add(selectionSemester);    
        content.add(mailTemplateSelectorTemplate);
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
    
    public void updateMailTemplates(Map<String, String> templates) {
        
        content.remove(content.indexOf(mailTemplateSelectorTemplate));        
        mailTemplateSelectorTemplate.clear();
        for (Entry<String, String> entry : templates.entrySet()) {
            mailTemplateSelectorTemplate.add(entry.getKey());
        }
        content.add(mailTemplateSelectorTemplate);
    }
}
