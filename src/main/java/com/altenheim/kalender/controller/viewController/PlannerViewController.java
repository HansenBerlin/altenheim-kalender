package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.*;
import com.calendarfx.view.CalendarView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.IOException;

public class PlannerViewController extends ResponsiveController
{
    @FXML private Button btnImport, btnExport;
    private CalendarView customCalendar;
    private ICalendarEntriesModel allEntries;
    private IEntryFactory entryFactory;
    private IImportController importController;
    private IExportController exportController;

    public PlannerViewController(ICalendarEntriesModel allEntries, IEntryFactory entryFactory, 
        IImportController importController, IExportController exportController, CalendarView custumCalendar)
    {
        this.allEntries = allEntries;
        this.entryFactory = entryFactory;
        this.importController = importController;
        this.exportController = exportController;
        this.customCalendar = custumCalendar;
    }

    @FXML
    private void openFilePicker(ActionEvent event) throws IOException
    {
        var button = (Button)event.getSource();
        var stage = button.getScene().getWindow();
        if (button.equals(btnImport))
        {
            var filePicker = new FileChooser();
            var file = filePicker.showOpenDialog(stage);
            if (file == null)
                return;
            var importedCalendar = importController.importFile(file.getAbsolutePath());
            entryFactory.addCalendarToView(importedCalendar);
        }
        else
        {
            var calendars = allEntries.getAllCalendars();
            var directoryChooser = new DirectoryChooser();
            var path = directoryChooser.showDialog(stage);
            if (path == null)
                return;
            for (var calendar: calendars)
            {
                exportController.exportCalendarAsFile(calendar, path.getAbsolutePath());
            }
        }
    }
    
    public void updateCustomCalendarView(CalendarView calendarView)
    {
        if (childContainer.getChildren().contains(this.customCalendar))
        {
            childContainer.getChildren().remove(this.customCalendar);
            this.customCalendar = calendarView;
        }
        childContainer.add(this.customCalendar, 0, 0, 1, 1);
    }

    public void changeContentPosition() 
    {
    }    
}


