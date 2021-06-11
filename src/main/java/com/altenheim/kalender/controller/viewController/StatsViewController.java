package com.altenheim.kalender.controller.viewController;

import java.util.List;

import com.altenheim.kalender.interfaces.ICalendarEntriesModel;
import com.altenheim.kalender.models.ContactModel;

public class StatsViewController extends ResponsiveController
{    
    private List<ContactModel> contacts;
    private ICalendarEntriesModel allEntries;

    public StatsViewController(List<ContactModel> contacts, ICalendarEntriesModel allEntries)
    {
        this.contacts = contacts;
        this.allEntries = allEntries;
    }

    public void changeContentPosition() 
    {
        
    }    
}


