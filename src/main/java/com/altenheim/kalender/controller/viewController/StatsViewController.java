package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.models.ContactsModel;

public class StatsViewController extends ResponsiveController
{    
    private ContactsModel contacts;
    private ICalendarEntriesModel allEntries;

    public StatsViewController(ContactsModel contacts, ICalendarEntriesModel allEntries)
    {
        this.contacts = contacts;
        this.allEntries = allEntries;
    }

    public void changeContentPosition() 
    {
        
    }    
}


