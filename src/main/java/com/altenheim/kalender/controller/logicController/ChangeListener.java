package com.altenheim.kalender.controller.logicController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class ChangeListener implements PropertyChangeListener
{
    public void propertyChange(PropertyChangeEvent evt) 
    {
        System.out.println("Name = " + evt.getPropertyName()); 
        System.out.println("Old Value = " + evt.getOldValue()); 
        System.out.println("New Value = " + evt.getNewValue()); 
        System.out.println("**********************************");
    }
    
}