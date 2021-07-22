package com.altenheim.kalender.models;

import java.time.LocalDate;
import java.time.LocalTime;

import com.altenheim.kalender.controller.Factories.EntryFactory;
import com.altenheim.kalender.controller.logicController.SmartSearchController;
import com.altenheim.kalender.controller.viewController.SearchViewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class SuggestionsModel 
{
    final static public ObservableList<SuggestionsModel> data = FXCollections.observableArrayList();    
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate day;
    private Button button;

    public SuggestionsModel(LocalTime startTime, LocalTime endTime, LocalDate day, Button button)
    {      
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.button = button;
	    button.setOnAction(new EventHandler<ActionEvent>() 
        {
            public void handle(ActionEvent e) 
            {
				EntryFactory.createNewUserEntry(day, day, startTime, endTime);
                SearchViewController.recurrences--;
                if (SearchViewController.recurrences == 0)
                {
                    SuggestionsModel.data.clear();
                }
            }
        });
    }

    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public LocalDate getDay() { return day; }
    public Button getButton() { return button; }

    final static public void addToList(LocalTime startTime, LocalTime endTime, LocalDate startDate, Button button)
    {        
        SuggestionsModel.data.add(new SuggestionsModel(startTime, endTime, startDate, button));
    }   
}