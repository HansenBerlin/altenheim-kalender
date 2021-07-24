package com.altenheim.kalender.interfaces;

import java.util.List;
import java.util.Map;

import com.altenheim.kalender.resourceClasses.ComboBoxCreate;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public interface IComboBoxFactory 
{
    ComboBox<String> create(ComboBoxCreate type);
    //void updateMailTemplates(Map<String, String> templates);
    ObservableList<String> getMailTemplateSelectorTemplate() ;
    List<ObservableList<String>> getContent();
}
