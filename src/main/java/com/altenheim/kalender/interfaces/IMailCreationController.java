package com.altenheim.kalender.interfaces;

public interface IMailCreationController 
{
    public void processMailWrapper(String templateName, String date, String time, String recipient);
}
