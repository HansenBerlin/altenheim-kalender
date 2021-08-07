package com.altenheim.kalender.interfaces.logicController;

public interface MailClientAccessController
{
    public void processMailWrapper(String templateName, String date, String time, String recipient);
}
