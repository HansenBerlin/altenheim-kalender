package com.altenheim.kalender.interfaces;

import java.io.IOException;
import java.net.URISyntaxException;

public interface IMailCreationController 
{
    void sendMail(String recipient, String subject, String body) throws IOException, URISyntaxException;
    String processPlaceholders(String body, String date, String time, int template);
}
