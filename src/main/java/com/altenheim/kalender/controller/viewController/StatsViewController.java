package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.CalendarEntriesModel;
import com.altenheim.kalender.models.StatsModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class StatsViewController extends ResponsiveController
{    
    @FXML private PieChart typesOfAddedEntries;
    @FXML private Button testbutton;
    @FXML private Pane addedEntriesContainer;

    private StatsModel statsData;

    
    private CalendarEntriesModel allEntries;

    public StatsViewController(CalendarEntriesModel allEntries)
    {
        this.allEntries = allEntries;
        
    }

    @FXML
    private void initialize()
    {
        statsData = new StatsModel();
        initializeTypeOfAddedEntries();

    }

    @FXML
    void testclick(ActionEvent event) 
    {
        statsData.automaticAddedEntriesSmart++;
        statsData.importedEntries++;

    }

    private void initializeTypeOfAddedEntries()
    {
        
        typesOfAddedEntries = new PieChart(statsData.pieChartData);
        typesOfAddedEntries.setTitle("Anzahl der erstellten Kalendereinträge");
        //addedEntriesContainer.getChildren().add(typesOfAddedEntries);

    }

    public void changeContentPosition(double width, double height) 
    {
        
    }    
}




