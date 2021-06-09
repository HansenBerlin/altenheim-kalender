package com.altenheim.kalender.controller.logicController;

import java.awt.*;
import java.net.*;
import com.altenheim.kalender.interfaces.IMailCreationController;
import com.altenheim.kalender.models.MailTemplateModel;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MailCreationController implements IMailCreationController
{    
    public void sendMail(String recipient, String subject, String body) throws IOException, URISyntaxException
    {
        String uriStr = String.format("mailto:%s?subject=%s&body=%s",
            recipient, encodeUrl(subject), encodeUrl(body));
        Desktop.getDesktop().browse(new URI(uriStr));
    }


    public String processPlaceholders(String body, String date, String time, int template)
    {
        var templates = new MailTemplateModel();
        if (template == 1)        
            body = templates.getTemplateOne();     
        else if (template == 2)
            body = templates.getTemplateTwo();
        
        body = body.replace("[Datum]", date);
        body = body.replace("[Uhrzeit]", time);

        return body;
    }


    private String encodeUrl(String uri) 
    {
        try 
        {
            return URLEncoder.encode(uri, "UTF-8").replace("+", "%20");
        } 
        catch (UnsupportedEncodingException e) 
        {
            throw new RuntimeException(e);
        }
    } 
}
