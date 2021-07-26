package com.altenheim.kalender.models;

import java.io.Serializable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

public class StatsModel implements Serializable
{
    public int automaticAddedEntriesSmart = 1;
    public int manuallyAddedEntriesSmart = 1;
    public int manuallyAddedEnries = 1;
    public int importedEntries = 1;

    public ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("automatisch generiert (Smartsearch)", automaticAddedEntriesSmart),
                new PieChart.Data("manuell eingetragen (Smartsearch)", manuallyAddedEntriesSmart),
                new PieChart.Data("manuell eingetragen (direkt)", manuallyAddedEnries),
                new PieChart.Data("durch Import", importedEntries));
    
}
