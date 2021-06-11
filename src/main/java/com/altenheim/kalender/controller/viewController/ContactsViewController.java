package com.altenheim.kalender.controller.viewController;

import java.util.List;
import com.altenheim.kalender.interfaces.IGoogleAPIController;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.models.ContactModel;

public class ContactsViewController extends ResponsiveController
{ 
    private List<ContactModel> contacts;
    private IGoogleAPIController api;
    private IIOController ioController;

    public ContactsViewController(List<ContactModel> contacts, IGoogleAPIController api, IIOController ioController)
    {
        this.ioController = ioController;
        this.contacts = contacts;
        this.api = api;
    }

    public void changeContentPosition() 
    {
        
    }    
}


