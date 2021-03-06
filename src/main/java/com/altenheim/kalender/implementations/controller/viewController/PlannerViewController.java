package com.altenheim.kalender.implementations.controller.viewController;

import com.altenheim.kalender.interfaces.factorys.EntryFactory;
import com.altenheim.kalender.interfaces.logicController.IOController;
import com.altenheim.kalender.interfaces.viewController.CalendarEntriesController;
import com.altenheim.kalender.interfaces.viewController.PopupViewController;
import com.calendarfx.model.Calendar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PlannerViewController extends ResponsiveController 
{
    @FXML private Button btnImport, btnExport;

    private final IOController iOController;
    private final EntryFactory entryFactory;
    private final PopupViewController popups;
    private final CalendarEntriesController calendars;

    public PlannerViewController(IOController iOController, EntryFactory entryFactory, PopupViewController popups, CalendarEntriesController calendars)
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
        childContainer.getChildren().remove(calendarView);
        childContainer.add(calendarView, 0, 0, 1, 1);
    }

    public void changeContentPosition(double width, double height) 
    {
        //currently not used
    } 
    
    public void registerButtonEvents()
    {
        Button addButton = (Button)childContainer.lookup("#add-calendar-button");
        addButton.setOnAction(event -> 
        {
            var calendar = new Calendar();
            String calName = popups.showChooseCalendarNameDialog();
            if (calName.isBlank())
                return;
            entryFactory.addCalendarToView(calendar, calName);
            iOController.saveCalendar(calendar);
        });
    }

}
