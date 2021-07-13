package com.altenheim.kalender.interfaces;

import com.altenheim.kalender.resourceClasses.ComboBoxCreate;
import javafx.scene.control.ComboBox;

public interface IComboBoxFactory 
{
    ComboBox<String> create(ComboBoxCreate type);
    ComboBox<Integer> createComboBoxInteger(ComboBoxCreate type);
}
