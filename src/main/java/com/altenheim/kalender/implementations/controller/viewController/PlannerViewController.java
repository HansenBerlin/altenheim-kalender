package com.altenheim.kalender.implementations.controller.viewController;

import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.logicController.IOController;
import com.altenheim.kalender.interfaces.models.CalendarEntriesModel;
import com.altenheim.kalender.interfaces.viewController.PopupViewController;
import com.calendarfx.model.Calendar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PlannerViewController extends ResponsiveController 
{

    @FXML private Button btnImport, btnExport;

    private IOController iOController;
    private EntryFactory entryFactory;
    private PopupViewController popups;
    private CalendarEntriesModel calendars;

    public PlannerViewController(IOController iOController, EntryFactory entryFactory, PopupViewController popups, CalendarEntriesModel calendars)
    {
        this.iOController = iOController;
        this.entryFactory = entryFactory;
        this.popups = popups;
        this.calendars = calendars;
    }  
    
    @FXML
    void openFilePicker(ActionEvent event) 
    {
        var button = (Button)event.getSource();        
        var stage = button.getScene().getWindow();
        if (button.equals(btnImport))        
            popups.importDialog(entryFactory, stage);        
        else        
            popups.exportDialog(calendars, stage);  
    }

    public void updateCustomCalendarView(CustomViewOverride calendarView) 
    {
        if (childContainer.getChildren().contains(calendarView)) 
        {
            childContainer.getChildren().remove(calendarView);
        }
        childContainer.add(calendarView, 0, 0, 1, 1);
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
                iOController.saveCalendar(calendar); 
            }
        });  
    }
}