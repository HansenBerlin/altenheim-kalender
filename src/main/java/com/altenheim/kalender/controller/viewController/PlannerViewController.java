package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.*;
import com.calendarfx.view.CalendarView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URI;

public class PlannerViewController extends ResponsiveController
{
    @FXML private Button btnImport, btnExport;
    private CalendarView customCalendar;
    private ICalendarEntriesModel allEntries;
    private IEntryFactory entryFactory;
    private IImportController importController;
    private IExportController exportController;
    private IPopupViewController popupViewController;

    public PlannerViewController(ICalendarEntriesModel allEntries, IEntryFactory entryFactory, IImportController importController, 
        IExportController exportController, CalendarView custumCalendar, IPopupViewController popupViewController)
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
        String uri; 
        String uri2;
        if (switcher %2 == 0)
        {
            uri = "/calendarDark.css";
            uri2 = "/darkMode.css";
        }
        else
        {
            uri = "/calendarLight.css";
            uri2 = "/lightMode.css";
        }
        switcher++;

        var scene = btnExport.getScene();
        scene.getStylesheets().remove(0);
        scene.getStylesheets().add(0, uri);
        var styles = com.sun.javafx.css.StyleManager.getInstance().userAgentStylesheetContainers;
        var default2 = com.sun.javafx.css.StyleManager.getInstance().hasDefaultUserAgentStylesheet;
        //com.sun.javafx.css.StyleManager.getInstance().removeUserAgentStylesheet("C:/Users/Hannes/.m2/repository/com/calendarfx/view/11.10.1/view-11.10.1.jar!/com/calendarfx/view/calendar.css");
        //com.sun.javafx.css.StyleManager.getInstance().removeUserAgentStylesheet("/calender.css");

        System.out.println(default2);

        System.out.println(styles.get(0));
        System.out.println(styles.get(1));
        System.out.println(styles.get(2));
        System.out.println(styles.get(3));

        

        com.sun.javafx.css.StyleManager.getInstance().addUserAgentStylesheet(scene, uri);
        com.sun.javafx.css.StyleManager.getInstance().addUserAgentStylesheet(scene, uri2);
        com.sun.javafx.css.StyleManager.getInstance().setDefaultUserAgentStylesheet(scene, uri); 
        com.sun.javafx.css.StyleManager.getInstance().setDefaultUserAgentStylesheet(scene, uri2);        

        /*
        var button = (Button)event.getSource();
        var stage = button.getScene().getWindow();
        if (button.equals(btnImport))
        {
            popupViewController.importDialog(importController, entryFactory, stage);
        }
        else
        {
            popupViewController.exportDialog(exportController, allEntries, stage);
        }*/
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

    public void changeContentPosition(double width, double height) 
    {
    }    
}


