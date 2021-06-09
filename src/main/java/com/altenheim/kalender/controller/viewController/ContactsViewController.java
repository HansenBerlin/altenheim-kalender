package com.altenheim.kalender.controller.viewController;

import com.altenheim.kalender.controller.logicController.GoogleAPIController;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.models.ContactsModel;

public class ContactsViewController extends ResponsiveController
{ 
    private ContactsModel contacts;
    private GoogleAPIController api;
    private IIOController ioController;

    public ContactsViewController(ContactsModel contacts, GoogleAPIController api, IIOController ioController)
    {
        this.ioController = ioController;;
        this.contacts = contacts;
        this.api = api;
    }

    public void changeContentPosition() 
    {
        
    }    
}


