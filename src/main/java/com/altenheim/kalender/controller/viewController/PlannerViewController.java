package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.*;
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
    private IImportController importController;
    private ICalendarEntriesModel calendars;
    private IExportController exportController;

    public PlannerViewController(CustomViewOverride custumCalendar, IIOController iOController, IEntryFactory entryFactory, 
        IPopupViewController popups, IImportController importController, ICalendarEntriesModel calendars, IExportController exportController) 
    {
        this.customCalendar = custumCalendar;
        this.iOController = iOController;
        this.entryFactory = entryFactory;
        this.popups = popups;
        this.importController = importController;
        this.calendars = calendars;
        this.exportController = exportController;
    }  
    
    @FXML
    void openFilePicker(ActionEvent event) 
    {
        var button = (Button)event.getSource();        
        var stage = button.getScene().getWindow();
        if (button.equals(btnImport))        
            popups.importDialog(importController, entryFactory, stage);        
        else        
            popups.exportDialog(exportController, calendars, stage);  
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
                    return;
                entryFactory.addCalendarToView(calendar, calName);
                iOController.saveCalendar(calendar, exportController); 

            }
        });  
    }
}