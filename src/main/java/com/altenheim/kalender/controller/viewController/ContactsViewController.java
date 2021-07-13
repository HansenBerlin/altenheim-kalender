package com.altenheim.kalender.controller.viewController;

import java.util.List;

import com.altenheim.kalender.interfaces.IContactFactory;
import com.altenheim.kalender.interfaces.IGoogleAPIController;
import com.altenheim.kalender.interfaces.IIOController;
import com.altenheim.kalender.models.ContactModel;

public class ContactsViewController extends ResponsiveController
{ 
    private List<ContactModel> contacts;
    private IContactFactory contactFactory;
    private IGoogleAPIController api;
    private IIOController ioController;

    public ContactsViewController(List<ContactModel> contacts, IContactFactory contactFactory, IGoogleAPIController api, IIOController ioController)
    {
        this.contacts = contacts;
        this.contactFactory = contactFactory;
        this.ioController = ioController;
        this.api = api;
    }

    public void changeContentPosition() 
    {
        
    }    
}


