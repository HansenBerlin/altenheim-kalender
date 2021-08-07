package com.altenheim.kalender.implementations.controller.models;

import java.time.LocalDate;
import java.time.LocalTime;

import com.altenheim.kalender.resourceClasses.DateFormatConverter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;

public class SuggestionsModel 
{
    final static public ObservableList<SuggestionsModel> data = FXCollections.observableArrayList();
    private final String startTime;
    private final String endTime;
    private final String dayStart;
    private final String dayEnd;
    private final Button button;

    public SuggestionsModel(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate, Button button)
    {
        this.startTime = DateFormatConverter.formatTime(startTime);
        this.endTime = DateFormatConverter.formatTime(endTime);
        this.dayStart = DateFormatConverter.formatDate(startDate);
        this.dayEnd = DateFormatConverter.formatDate(endDate);
        this.button = button;
    }

    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getDayStart() { return dayStart; }
    public String getDayEnd() { return dayEnd; } 
    public Button getButton() { return button; }    

    static public void addToList(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate, Button button, String title) 
    {
        SuggestionsModel.data.add(new SuggestionsModel(startTime, endTime, startDate, endDate, button));
    } 
}