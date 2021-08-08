package com.altenheim.kalender.implementations.controller.factories;

import java.util.List;
import com.calendarfx.model.Calendar;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;

public class SplitMenuButtonFactory 
{
    public static SplitMenuButton createButtonForAvailableCalendars(List<Calendar> allCalendars)
    {
        var splitMenuButton = new SplitMenuButton();
        splitMenuButton.setText("Folgende Kalender berücksichtigen");
        for (var calendar : allCalendars)
        {
            var checkBox = new CheckBox(calendar.getName());                     
            var menuItem = new MenuItem();
            menuItem.setGraphic(checkBox);
            splitMenuButton.getItems().add(menuItem);           
        }
        return splitMenuButton;
    }    
}
