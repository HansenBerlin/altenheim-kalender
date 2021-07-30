package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.IEntryFactory;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.interfaces.IPopupViewController;
import com.calendarfx.model.Calendar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PlannerViewController extends ResponsiveController 
{

    @FXML private Button btnImport, btnExport;

    private CustomViewOverride customCalendar;
    private IIOController iOController;
    private IEntryFactory entryFactory;
    private IPopupViewController popups;

    public PlannerViewController(CustomViewOverride custumCalendar, IIOController iOController, IEntryFactory entryFactory, IPopupViewController popups) 
    {
        this.customCalendar = custumCalendar;
        this.iOController = iOController;
        this.entryFactory = entryFactory;
        this.popups = popups;
    }  
    
    @FXML
    void openFilePicker(ActionEvent event) 
    {
        var button = (Button)event.getSource();
        var stage = button.getScene().getWindow();
        if (button.equals(btnImport))
        {
            //popups.importDialog(importController, entryFactory, stage);
        }
        else
        {
            //popups.exportDialog(exportController, allEntries, stage);
        }

    }

    public void updateCustomCalendarView(CustomViewOverride calendarView) 
    {
        if (childContainer.getChildren().contains(this.customCalendar)) 
        {
            childContainer.getChildren().remove(this.customCalendar);
            this.customCalendar = calendarView;
        }
        childContainer.add(this.customCalendar, 0, 0, 1, 1);
    }

    public void changeContentPosition(double width, double height) 
    {
        //

    } 
    
    public void registerButtonEvents()
    {
        Button addButton = (Button)childContainer.lookup("#add-calendar-button");
        addButton.setOnAction(new EventHandler<ActionEvent>()
        { 
            public void handle(ActionEvent event) 
            {
                var calendar = new Calendar();
                String calName = popups.showChooseCalendarNameDialog();
                if (calName.isBlank())
                    calName = "Neuer Kalender";
                entryFactory.addCalendarToView(calendar, calName);
                iOController.saveCalendar(calendar);                   
            }
        });  
    }

    
}