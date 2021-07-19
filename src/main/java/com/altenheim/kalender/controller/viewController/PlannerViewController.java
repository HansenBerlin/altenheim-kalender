package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

public class PlannerViewController extends ResponsiveController
{
    @FXML private Button btnImport, btnExport;
    private CustomViewOverride customCalendar;
    private ICalendarEntriesModel allEntries;
    private IEntryFactory entryFactory;
    private IImportController importController;
    private IExportController exportController;
    private IPopupViewController popupViewController;

    public PlannerViewController(ICalendarEntriesModel allEntries, IEntryFactory entryFactory, IImportController importController, 
        IExportController exportController, CustomViewOverride custumCalendar, IPopupViewController popupViewController)
    {
        this.allEntries = allEntries;
        this.entryFactory = entryFactory;
        this.importController = importController;
        this.exportController = exportController;
        this.customCalendar = custumCalendar;
        this.popupViewController = popupViewController;
    }

    int switcher = 0;
    @FXML
    private void openFilePicker(ActionEvent event) throws IOException
    {   
        
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
    }    
}
