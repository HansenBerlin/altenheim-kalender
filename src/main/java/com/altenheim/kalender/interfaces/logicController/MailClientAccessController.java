package com.altenheim.kalender.interfaces.logicController;

public interface MailClientAccessController
{
    void processMailWrapper(String templateName, String date, String time, String recipient);
}
