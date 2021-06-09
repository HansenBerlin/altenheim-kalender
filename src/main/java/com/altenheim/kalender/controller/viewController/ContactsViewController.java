package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.interfaces.IGoogleAPIController;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.models.ContactsModel;

public class ContactsViewController extends ResponsiveController
{ 
    private ContactsModel contacts;
    private IGoogleAPIController api;
    private IIOController ioController;

    public ContactsViewController(ContactsModel contacts, IGoogleAPIController api, IIOController ioController)
    {
        this.ioController = ioController;;
        this.contacts = contacts;
        this.api = api;
    }

    public void changeContentPosition() 
    {
        
    }    
}


