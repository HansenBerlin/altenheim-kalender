package com.altenheim.kalender.models;

import java.time.LocalTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SuggestionsModel 
{
    final static public ObservableList<SuggestionsModel> data = FXCollections.observableArrayList(
      new SuggestionsModel(LocalTime.now(), LocalTime.now()),
      new SuggestionsModel(LocalTime.now(), LocalTime.now()),
      new SuggestionsModel(LocalTime.now(), LocalTime.now())      
    );  
    
    final private LocalTime startTime;
    final private LocalTime endTime;

    public SuggestionsModel(LocalTime startTime, LocalTime endTime) 
    {      
      this.startTime = startTime;
      this.endTime = endTime;
    }

    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void addToList(LocalTime startTime, LocalTime endTime) {};
  }