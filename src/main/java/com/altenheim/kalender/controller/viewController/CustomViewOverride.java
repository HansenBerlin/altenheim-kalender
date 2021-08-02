package com.altenheim.kalender.controller.viewController;

import com.calendarfx.view.CalendarView;
import com.altenheim.kalender.resourceClasses.StylePresets;

public class CustomViewOverride extends CalendarView 
{
    public CustomViewOverride(String cssMode) 
    {
        initCss(cssMode);        
    }

    private void initCss(String cssMode)
    {
        String stylePreset = switch (cssMode) 
        {
            case "Light" -> StylePresets.LIGHT_CALENDAR_CSS_FILE;
            case "Dark" -> StylePresets.DARK_CALENDAR_CSS_FILE;            
            default -> StylePresets.LIGHT_CALENDAR_CSS_FILE;
        };
        getStylesheets().add(stylePreset);        
    }

    public void updateCss(String oldPath, String newPath) 
    {
        getStylesheets().remove(oldPath);
        getStylesheets().add(newPath);
    }    
}