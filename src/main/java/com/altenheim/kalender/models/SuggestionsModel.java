package com.altenheim.kalender.models;

import java.time.LocalDate;
import java.time.LocalTime;
import com.altenheim.kalender.controller.Factories.EntryFactory;
import com.altenheim.kalender.controller.viewController.PopupViewsController;
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

    public SuggestionsModel(LocalTime startTime, LocalTime endTime, LocalDate day, Button button, String title)
    {      
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.button = button;
	    button.setOnAction(new EventHandler<ActionEvent>() 
        {
            public void handle(ActionEvent e) 
            {
				EntryFactory.createNewUserEntry(day, day, startTime, endTime, title);
                //SearchViewController.recurrences--;
                //if (SearchViewController.recurrences <= 0)
                //{
                  //  SuggestionsModel.data.clear();
                //}
                PopupViewsController.showEntryAddedDialog(day.toString(), startTime.toString(), endTime.toString(), title);
            }
        });
    }

    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public LocalDate getDay() { return day; }
    public Button getButton() { return button; }

    final static public void addToList(LocalTime startTime, LocalTime endTime, LocalDate startDate, Button button, String title)
    {        
        SuggestionsModel.data.add(new SuggestionsModel(startTime, endTime, startDate, button, title));
    }   
}