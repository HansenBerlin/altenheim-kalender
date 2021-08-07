package com.altenheim.kalender.implementations.controller.factories;

import java.util.ArrayList;
import java.util.List;
import com.altenheim.kalender.interfaces.factorys.ComboBoxFactory;
import com.altenheim.kalender.implementations.controller.models.CalendarEntriesModelImpl;
import com.altenheim.kalender.implementations.controller.models.ContactModelImpl;
import com.altenheim.kalender.resourceClasses.ComboBoxCreate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class ComboBoxFactoryImpl implements ComboBoxFactory
{
    private final ObservableList<String> mailTemplateSelectorTemplate = FXCollections.observableArrayList();
    private final List<ObservableList<String>> content = new ArrayList<ObservableList<String>>();
    private final String[] headers = { "Verkehrsmittel", "Start", "Ziel", "Intervall", "Min.", "FB", "Kurs",
        "Semester", "MailTemplate", "Standardkalender", "Mailadressen" };    

    public ComboBoxFactoryImpl()
    {
        ObservableList<String> vehicles = FXCollections.observableArrayList();
        vehicles.addAll("zu Fuß", "Fahrrad", "Öffis", "Auto");
        ObservableList<String> destinations = ContactModelImpl.destinations;
        ObservableList<String> calendars = CalendarEntriesModelImpl.calendarsComboBox;
        ObservableList<String> mailAdresses = ContactModelImpl.mailadresses;
        ObservableList<String> recurrenceOptions = FXCollections.observableArrayList();
        recurrenceOptions.addAll("täglich", "wöchentlich", "monatlich", "halbjährlich", "jährlich");
        ObservableList<String> notificationMin = FXCollections.observableArrayList();
        notificationMin.addAll("5", "15", "30", "60");
        ObservableList<String> selectionSpecialField = FXCollections.observableArrayList();
        selectionSpecialField.addAll("IP", "Bank", "Bauwesen", "DL", "Elektrotechnik", "FM", "Handel", "IBA",
                "Immobilien", "Industrie", "Informatik", "Maschinenbau", "PPM", "Spedition", "Steuern", "Tourismus",
                "Versicherung", "WI");
        ObservableList<String> selectionCourse = FXCollections.observableArrayList();
        selectionCourse.addAll("A", "B", "C", "keine Kurse");
        ObservableList<String> selectionSemester = FXCollections.observableArrayList();
        selectionSemester.addAll("Sem. 1", "Sem. 2", "Sem. 3", "Sem. 4", "Sem. 5", "Sem. 6");
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
        content.add(calendars);
        content.add(mailAdresses);
    }

    public ObservableList<String> getMailTemplateSelectorTemplate() { return mailTemplateSelectorTemplate; }

    public ComboBox<String> create(ComboBoxCreate type) 
    {
        int typeOrdinal = type.ordinal();
        var comboBox = new ComboBox<String>();
        comboBox.setPrefWidth(140);
        comboBox.setPromptText(headers[typeOrdinal]);
        comboBox.setItems(content.get(typeOrdinal));
        if (typeOrdinal == 1 || typeOrdinal == 2 || typeOrdinal == 10)
            comboBox.setEditable(true);
        return comboBox;
    }
}