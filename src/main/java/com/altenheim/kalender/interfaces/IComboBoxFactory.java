package com.altenheim.kalender.interfaces;

import java.util.Map;

import com.altenheim.kalender.resourceClasses.ComboBoxCreate;
import javafx.scene.control.ComboBox;

public interface IComboBoxFactory 
{
    ComboBox<String> create(ComboBoxCreate type);
    void updateMailTemplates(Map<String, String> templates);
}
