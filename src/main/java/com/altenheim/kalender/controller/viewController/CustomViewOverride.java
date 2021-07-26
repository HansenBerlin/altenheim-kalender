package com.altenheim.kalender.controller.viewController;

import com.calendarfx.view.CalendarView;
import com.altenheim.kalender.resourceClasses.StylePresets;

public class CustomViewOverride extends CalendarView {
    public CustomViewOverride(String cssMode) {
        switch (cssMode) {
            case "Light":
                getStylesheets().add(com.altenheim.kalender.resourceClasses.StylePresets.LIGHT_CALENDAR_CSS_FILE);
                break;
            case "Dark":
                getStylesheets().add(StylePresets.DARK_CALENDAR_CSS_FILE);
                break;
            default:
                getStylesheets().add(StylePresets.LIGHT_CALENDAR_CSS_FILE);
                break;
        }

    }

    public void updateCss(String oldPath, String newPath) {
        getStylesheets().remove(oldPath);
        getStylesheets().add(newPath);
    }
    
}