package com.altenheim.kalender.models;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalTime;
import com.altenheim.kalender.controller.viewController.PopupViewsController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class SuggestionsModel {
    final static public ObservableList<SuggestionsModel> data = FXCollections.observableArrayList();
    public static boolean toggleTravelTime = false;
    public static int travelTime;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate dayStart;
    private LocalDate dayEnd;
    private Button button;
    private String title;

    public SuggestionsModel(LocalTime startTime, LocalTime endTime, LocalDate dayStart, LocalDate dayEnd, Button button, String title) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayStart = dayStart;
        this.dayEnd = dayEnd;
        this.button = button;
        this.title = title;
        registerButtonEvent();	    
    }

    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public LocalDate getDayStart() { return dayStart; }
    public LocalDate getDayEnd() { return dayEnd; } 
    public Button getButton() { return button; }

    private void registerButtonEvent()
    {
        button.setOnAction(new EventHandler<ActionEvent>() 
        {
            public void handle(ActionEvent e) 
            {
                if (toggleTravelTime == false)
                    travelTime = 0;                               
                invokeEntryFactoryCreation(dayStart, dayEnd, startTime, endTime, title, travelTime);                 
                                  
                PopupViewsController.showEntryAddedDialog(dayStart.toString(), dayEnd.toString(), startTime.toString(), endTime.toString(), title);
            }
        });
    }    

    final static public void addToList(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate,
            Button button, String title) {
        SuggestionsModel.data.add(new SuggestionsModel(startTime, endTime, startDate, endDate, button, title));
    } 

    
    private void invokeEntryFactoryCreation(LocalDate dateStart, LocalDate dateEnd,
        LocalTime timeStart, LocalTime timeEnd, String title, int timeTravel)
    {
        try 
        {
            var entryFactory = Class.forName("EntryFactory");
            var method = entryFactory.getDeclaredMethod("createNewUserEntryIncludingTravelTimes", LocalDate.class, LocalDate.class,
            LocalTime.class, LocalTime.class, String.class, int.class);
            method.invoke(null, dayStart, dayEnd, startTime, endTime, title, travelTime);
        } 
        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) 
        {
            e.printStackTrace();
        }        
    }
}