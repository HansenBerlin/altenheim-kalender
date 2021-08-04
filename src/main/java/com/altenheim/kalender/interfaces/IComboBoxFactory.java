package com.altenheim.kalender.interfaces;

import com.altenheim.kalender.resourceClasses.ComboBoxCreate;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public interface IComboBoxFactory 
{
    ComboBox<String> create(ComboBoxCreate type);
    ObservableList<String> getMailTemplateSelectorTemplate();
}
