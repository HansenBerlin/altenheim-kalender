package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.models.ContactModel;

public class StatsViewController extends ResponsiveController
{    
    private ContactModel contacts;
    private ICalendarEntriesModel allEntries;

    public StatsViewController(ContactModel contacts, ICalendarEntriesModel allEntries)
    {
        this.contacts = contacts;
        this.allEntries = allEntries;
    }

    public void changeContentPosition() 
    {
        
    }    
}


