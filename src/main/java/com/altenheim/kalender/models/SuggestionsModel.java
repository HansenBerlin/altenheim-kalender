package com.altenheim.kalender.models;

import java.time.LocalDate;
import java.time.LocalTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;

public class SuggestionsModel 
{
    final static public ObservableList<SuggestionsModel> data = FXCollections.observableArrayList();
    public static boolean toggleTravelTime = false;
    public static int travelTime;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate dayStart;
    private LocalDate dayEnd;
    private Button button;

    public SuggestionsModel(LocalTime startTime, LocalTime endTime, LocalDate dayStart, LocalDate dayEnd, Button button, String title) 
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayStart = dayStart;
        this.dayEnd = dayEnd;
        this.button = button;
    }

    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public LocalDate getDayStart() { return dayStart; }
    public LocalDate getDayEnd() { return dayEnd; } 
    public Button getButton() { return button; }    

    static public void addToList(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate, Button button, String title) 
    {
        SuggestionsModel.data.add(new SuggestionsModel(startTime, endTime, startDate, endDate, button, title));
    } 
}