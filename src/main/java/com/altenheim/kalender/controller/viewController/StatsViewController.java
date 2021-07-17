package com.altenheim.kalender.controller.viewController;

import java.util.List;
import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.models.ContactModel;

public class StatsViewController extends ResponsiveController
{    
    private ICalendarEntriesModel allEntries;

    public StatsViewController(ICalendarEntriesModel allEntries)
    {
        this.allEntries = allEntries;
    }

    public void changeContentPosition(double width, double height) 
    {
        
    }    
}


