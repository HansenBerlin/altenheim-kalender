package com.altenheim.kalender.models;

import java.time.LocalDate;
import java.time.LocalTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SuggestionsModel 
{
    final static public ObservableList<SuggestionsModel> data = FXCollections.observableArrayList();    
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate day;

    public SuggestionsModel(LocalTime startTime, LocalTime endTime, LocalDate day)
    {      
      this.startTime = startTime;
      this.endTime = endTime;
      this.day = day;
    }

    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public LocalDate getDay() { return day; }

    final static public void addToList(LocalTime startTime, LocalTime endTime, LocalDate startDate)
    {
        SuggestionsModel.data.add(new SuggestionsModel(startTime, endTime, startDate));
    }
}